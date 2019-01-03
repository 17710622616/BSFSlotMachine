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
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.GetObjectRequest;
import com.alibaba.sdk.android.oss.model.GetObjectResult;
import com.bs.john_li.bsfslotmachine.BSSMActivity.Mine.CarListActivity;
import com.bs.john_li.bsfslotmachine.BSSMModel.CarModel;
import com.bs.john_li.bsfslotmachine.BSSMModel.OwnCarListOutModel;
import com.bs.john_li.bsfslotmachine.BSSMUtils.BSSMCommonUtils;
import com.bs.john_li.bsfslotmachine.BSSMUtils.BSSMConfigtor;
import com.bs.john_li.bsfslotmachine.R;

import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

/**
 * 我的二手車列表刷新
 * Created by John_Li on 28/11/2017.
 */

public class SmartOwnCarListRefreshAdapter extends RecyclerView.Adapter<SmartOwnCarListRefreshAdapter.SmartRefreshViewHolder> implements View.OnClickListener {
    private List<OwnCarListOutModel.DataBeanX.OwnCarListModel> carList;
    private final Context mContext;
    private OSSClient oss;
    private OnItemClickListener mOnItemClickListener = null;
    private LayoutInflater mInflater;
    private LruCache<String ,BitmapDrawable> mMemoryCache;
    private ImageOptions options = new ImageOptions.Builder().setSize(0, 0).setImageScaleType(ImageView.ScaleType.FIT_XY).setLoadingDrawableId(R.mipmap.img_loading).setFailureDrawableId(R.mipmap.load_img_fail_list).build();

    public SmartOwnCarListRefreshAdapter(Context context, List<OwnCarListOutModel.DataBeanX.OwnCarListModel> list, OSSClient oss) {
        this.carList = list;
        this.oss = oss;
        mContext = context;
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
    public SmartOwnCarListRefreshAdapter.SmartRefreshViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_second_car_list, parent, false);
        SmartRefreshViewHolder vh = new SmartRefreshViewHolder(view);
        vh.item_second_car_iv = (ImageView) view.findViewById(R.id.item_second_car_iv);
        vh.item_second_car_brand = (TextView) view.findViewById(R.id.item_second_car_brand);
        vh.item_second_car_year = (TextView) view.findViewById(R.id.item_second_car_year);
        vh.item_second_car_mileage = (TextView) view.findViewById(R.id.item_second_car_mileage);
        vh.item_second_car_money = (TextView) view.findViewById(R.id.item_second_car_money);
        vh.item_second_car_popularity_values =  view.findViewById(R.id.item_second_car_popularity_values);
        view.setOnClickListener(this);
        return vh;
    }

    @Override
    public void onBindViewHolder(SmartOwnCarListRefreshAdapter.SmartRefreshViewHolder holder, int position) {
        holder.item_second_car_brand.setText(carList.get(position).getCarBrand() + " " + carList.get(position).getCarSeries() + " " + carList.get(position).getCarStyle());
        holder.item_second_car_year.setText(BSSMCommonUtils.stampToDate(String.valueOf(carList.get(position).getFirstRegisterationTime())) + "年/");
        holder.item_second_car_mileage.setText(carList.get(position).getDriverMileage() + "萬公里");
        holder.item_second_car_money.setText("MOP" + carList.get(position).getCarPrices() + "萬");
        holder.item_second_car_money.setTextSize(20);
        holder.item_second_car_popularity_values.setVisibility(View.VISIBLE);
        holder.item_second_car_popularity_values.setText("人氣值：" + carList.get(position).getPageView());
        List<String> result = Arrays.asList(carList.get(position).getCarImg().split(","));
        if (result.size() > 0) {
            x.image().bind(holder.item_second_car_iv, result.get(0), options);
        }
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
    public static void downloadImgByTag(final String object, OSSClient oss, final ImageView iv, final Context context, final int fialImgRes,final SmartOwnCarListRefreshAdapter adapter) {
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
        return carList.size();
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(v, (int)v.getTag());
        }
    }

    public class SmartRefreshViewHolder extends RecyclerView.ViewHolder {
        public ImageView item_second_car_iv;
        public TextView item_second_car_brand;
        public TextView item_second_car_year;
        public TextView item_second_car_mileage;
        public TextView item_second_car_money;
        private TextView item_second_car_popularity_values;
        public SmartRefreshViewHolder(View view){
            super(view);
        }
    }

    public void setOnItemClickListenr(OnItemClickListener listenr) {
        this.mOnItemClickListener = listenr;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void refreshListView(List<OwnCarListOutModel.DataBeanX.OwnCarListModel> newList){
        this.carList = newList;
        notifyDataSetChanged();
    }
}
