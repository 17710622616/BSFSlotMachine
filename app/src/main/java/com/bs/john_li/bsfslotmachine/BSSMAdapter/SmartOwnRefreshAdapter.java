package com.bs.john_li.bsfslotmachine.BSSMAdapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.GetObjectRequest;
import com.alibaba.sdk.android.oss.model.GetObjectResult;
import com.bs.john_li.bsfslotmachine.BSSMModel.ContentsListModel;
import com.bs.john_li.bsfslotmachine.BSSMUtils.AliyunOSSUtils;
import com.bs.john_li.bsfslotmachine.BSSMUtils.BSSMCommonUtils;
import com.bs.john_li.bsfslotmachine.BSSMUtils.BSSMConfigtor;
import com.bs.john_li.bsfslotmachine.R;
import com.google.gson.Gson;

import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 个人博文列表刷新
 * Created by John_Li on 28/11/2017.
 */

public class SmartOwnRefreshAdapter extends RecyclerView.Adapter<SmartOwnRefreshAdapter.SmartRefreshViewHolder> implements View.OnClickListener {
    private List<ContentsListModel.DataBean.ContentsModel> list;
    private final Context mContext;
    private OSSClient oss;
    private LayoutInflater mInflater;
    private LruCache<String ,BitmapDrawable> mMemoryCache;
    private OnItemClickListener mOnitemClickListener = null;
    private ImageOptions options = new ImageOptions.Builder().setSize(0, 0).setLoadingDrawableId(R.mipmap.img_loading).setFailureDrawableId(R.mipmap.load_img_fail_list).build();

    public SmartOwnRefreshAdapter(Context context, List<ContentsListModel.DataBean.ContentsModel> list, OSSClient oss) {
        this.list = list;
        mContext = context;
        this.oss = oss;
        mInflater = LayoutInflater.from(context);
        //计算内存，并且给Lrucache 设置缓存大小
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int cacheSize = maxMemory/6;
        mMemoryCache = new LruCache<String ,BitmapDrawable>(cacheSize){
            @Override
            protected int sizeOf(String key, BitmapDrawable value) {
                return  value.getBitmap().getByteCount();
            }
        };
    }
    @Override
    public SmartOwnRefreshAdapter.SmartRefreshViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_contents, parent, false);
        SmartRefreshViewHolder vh = new SmartRefreshViewHolder(view);
        vh.contentsIv = (ImageView) view.findViewById(R.id.item_contents_iv);
        vh.contentsTitle = (TextView) view.findViewById(R.id.item_contents_title);
        vh.contentsPostID = (TextView) view.findViewById(R.id.item_contents_postid);
        vh.contentsComment = (TextView) view.findViewById(R.id.item_contents_comments);
        view.setOnClickListener(this);
        return vh;
    }

    @Override
    public void onBindViewHolder(SmartOwnRefreshAdapter.SmartRefreshViewHolder holder, int position) {
        holder.contentsPostID.setText("發佈者：" + list.get(position).getCreator());
        holder.contentsComment.setText(Integer.toString(list.get(position).getCommentcount()) + "條評論");
        try {
            if (list.get(position).getCover() != null) {
                String[] coverArr = new Gson().fromJson(list.get(position).getCover(), String[].class);
                if (coverArr.length > 0) {
                    holder.contentsTitle.setText(list.get(position).getTitle());
                    x.image().bind(holder.contentsIv, coverArr[0], options);
                    holder.contentsIv.setVisibility(View.VISIBLE);
                } else {
                    //x.image().bind(holder.contentsIv, "", options);
                    holder.contentsTitle.setText("   " + list.get(position).getTitle());
                    holder.contentsIv.setVisibility(View.GONE);
                }
            } else {
                holder.contentsTitle.setText("   " + list.get(position).getTitle());
                holder.contentsIv.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            holder.contentsTitle.setText(list.get(position).getTitle());
            x.image().bind(holder.contentsIv, "", options);
        }
        /*String cover = list.get(position).getCover();
        try {
            Map<String, String> coverMap = new Gson().fromJson(cover, HashMap.class);
            cover = coverMap.get("mainPic");
        } catch (Exception e) {
            cover = "";
        }
        BitmapDrawable bitmap = getBitmapDrawableFromMemoryCache(cover);
        if (bitmap != null) {
            holder.contentsIv.setImageDrawable(bitmap);
        } else {
            downloadImgByTag(cover, oss, holder.contentsIv, mContext, R.mipmap.load_img_fail_list, this);
        }*/
        holder.itemView.setTag(position);
    }

    /**
     * 從缓存中获取已存在的图片
     * @param imageUrl
     * @return
     */
    private BitmapDrawable getBitmapDrawableFromMemoryCache(String imageUrl) {
        return mMemoryCache.get(imageUrl);
    }


    /**
     * 添加图片到缓存中
     * @param imageUrl
     * @param drawable
     */
    public void addBitmapDrawableToMemoryCache(String imageUrl,BitmapDrawable drawable){
        if (getBitmapDrawableFromMemoryCache(imageUrl) == null ){
            mMemoryCache.put(imageUrl, drawable);
        }
    }

    /**
     *  从OSS上下载图片
     * @param object    圖片名稱
     * @param oss   OSS對象
     * @param iv    綁定的ImageView
     * @param context   上下文，用來做UI線程的執行操作
     */
    public static void downloadImgByTag(final String object, OSSClient oss, final ImageView iv, final Context context, final int fialImgRes,final SmartOwnRefreshAdapter adapter) {
        final Activity activity = ((Activity)context);
        if ((object != null)) {
            if (object.equals("")){
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {iv.setImageResource(fialImgRes);
                    }
                });
                return;
            }
        } else {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    iv.setImageResource(fialImgRes);
                }
            });
            return;
        }

        GetObjectRequest get = new GetObjectRequest(BSSMConfigtor.BucketName, object);
        OSSAsyncTask task = oss.asyncGetObject(get, new OSSCompletedCallback<GetObjectRequest, GetObjectResult>() {
            @Override
            public void onSuccess(GetObjectRequest request, GetObjectResult result) {
                // 请求成功
                InputStream inputStream = result.getObjectContent();
                try {
                    byte[] date = new byte[0];
                    date = BSSMCommonUtils.readStream(inputStream);
                    //获取bitmap
                    final Bitmap bm = BitmapFactory.decodeByteArray(date,0,date.length);
                    BitmapDrawable drawable = new BitmapDrawable(activity.getResources(),bm);
                    adapter.addBitmapDrawableToMemoryCache(object,drawable);
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            iv.setImageBitmap(bm);
                            System.gc();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(GetObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                // 请求异常
                if (clientExcepion != null) {
                    // 本地异常如网络异常等
                    clientExcepion.printStackTrace();
                }
                if (serviceException != null) {
                    // 服务异常
                    Log.e("服务异常", "ErrorCode:" + serviceException.getErrorCode()+"，RequestId:" +serviceException.getRequestId()+"，HostId:"+serviceException.getHostId()+"，RawMessage:" + serviceException.getRawMessage());
                }

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        iv.setImageResource(fialImgRes);
                        System.gc();
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onClick(View v) {
        if (mOnitemClickListener != null) {
            mOnitemClickListener.onItemClick(v, (int)v.getTag());
        }
    }

    public class SmartRefreshViewHolder extends RecyclerView.ViewHolder {
        public ImageView contentsIv;
        public TextView contentsTitle;
        public TextView contentsPostID;
        public TextView contentsComment;
        public SmartRefreshViewHolder(View view){
            super(view);
        }
    }

    public void setOnItemClickListenr(OnItemClickListener listenr) {
        this.mOnitemClickListener = listenr;
    }

    public static interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
}
