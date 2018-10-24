package com.bs.john_li.bsfslotmachine.BSSMAdapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
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
import android.widget.Toast;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.GetObjectRequest;
import com.alibaba.sdk.android.oss.model.GetObjectResult;
import com.bs.john_li.bsfslotmachine.BSSMActivity.Mine.AddCarActivity;
import com.bs.john_li.bsfslotmachine.BSSMActivity.Mine.CarListActivity;
import com.bs.john_li.bsfslotmachine.BSSMModel.CarModel;
import com.bs.john_li.bsfslotmachine.BSSMModel.ContentsListModel;
import com.bs.john_li.bsfslotmachine.BSSMUtils.AliyunOSSUtils;
import com.bs.john_li.bsfslotmachine.BSSMUtils.BSSMCommonUtils;
import com.bs.john_li.bsfslotmachine.BSSMUtils.BSSMConfigtor;
import com.bs.john_li.bsfslotmachine.BSSMUtils.ProgressInputStream;
import com.bs.john_li.bsfslotmachine.R;
import com.google.gson.Gson;

import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 车辆列表刷新
 * Created by John_Li on 28/11/2017.
 */

public class SmartCarListRefreshAdapter extends RecyclerView.Adapter<SmartCarListRefreshAdapter.SmartRefreshViewHolder> implements View.OnClickListener,View.OnLongClickListener {
    private List<CarModel.CarCountAndListModel.CarInsideModel> carList;
    private final Context mContext;
    private OSSClient oss;
    private OnItemLongClickListener mOnItemLongClickListener = null;
    private CarRechargeCallBack mCarRechargeCallBack = null;
    private CarUpdateCallBack mCarUpdateCallBack = null;
    private LayoutInflater mInflater;
    private LruCache<String ,BitmapDrawable> mMemoryCache;
    private ImageOptions options = new ImageOptions.Builder().setSize(0, 0).setLoadingDrawableId(R.mipmap.img_loading).setFailureDrawableId(R.mipmap.load_img_fail_list).build();

    public SmartCarListRefreshAdapter(Context context, List<CarModel.CarCountAndListModel.CarInsideModel> list, OSSClient oss) {
        this.carList = list;
        this.oss = oss;
        mContext = context;
        mCarRechargeCallBack = (CarListActivity)mContext;
        mCarUpdateCallBack = (CarListActivity)mContext;
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
    public SmartCarListRefreshAdapter.SmartRefreshViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_car_list, parent, false);
        SmartRefreshViewHolder vh = new SmartRefreshViewHolder(view);
        vh.carlistCb = (CheckBox) view.findViewById(R.id.item_carlist_cb);
        vh.carlistIv = (ImageView) view.findViewById(R.id.item_carlist_iv);
        vh.carlistLicensenum = (TextView) view.findViewById(R.id.item_carlist_licensenum);
        vh.carlistBrand = (TextView) view.findViewById(R.id.item_carlist_brand);
        vh.carlistModel = (TextView) view.findViewById(R.id.item_carlist_model);
        vh.carlistStyle = (TextView) view.findViewById(R.id.item_carlist_style);
        vh.carTypeTv = (TextView) view.findViewById(R.id.item_carlist_car_type);
        vh.expireTimeTv = view.findViewById(R.id.item_carlist_expiry_time);
        vh.carRecharge = (ImageView) view.findViewById(R.id.item_carlist_recharge);
        vh.carListLL = view.findViewById(R.id.item_carlist_ll);
        view.setOnLongClickListener(this);
        return vh;
    }

    @Override
    public void onBindViewHolder(SmartCarListRefreshAdapter.SmartRefreshViewHolder holder, int position) {
        holder.carlistLicensenum.setText("車牌號碼：" +carList.get(position).getCarNo());
        holder.carlistBrand.setText("車輛品牌：" + carList.get(position).getCarBrand());
        holder.carlistStyle.setText("車輛型號：" + carList.get(position).getCarStyle());
        holder.carlistModel.setText("車      型：" +carList.get(position).getModelForCar());
        switch (carList.get(position).getIfPerson()) {
            case 1:
                holder.carTypeTv.setText("車輛類型：" + "輕重型電單車");
                break;
            case 2:
                holder.carTypeTv.setText("車輛類型：" + "私家車");
                break;
            case 3:
                holder.carTypeTv.setText("車輛類型：" + "重型汽車");
                break;
        }

        if (carList.get(position).getIfPay() == 0) {
            holder.expireTimeTv.setVisibility(View.GONE);
            holder.carRecharge.setImageResource(R.mipmap.recharge);
        } else {
            holder.expireTimeTv.setVisibility(View.VISIBLE);
            if (carList.get(position).getExpiryTime()!=null) {
                holder.expireTimeTv.setText("到期時間：" + BSSMCommonUtils.stampToDate(carList.get(position).getExpiryTime()));
            }
            holder.carRecharge.setImageResource(R.mipmap.member);
        }

        x.image().bind(holder.carlistIv, carList.get(position).getImgUrl(), options);

        holder.carListLL.setOnClickListener(this);
        holder.carRecharge.setOnClickListener(this);
        holder.carRecharge.setTag(String.valueOf(position));
        holder.carListLL.setTag(String.valueOf(position));
        holder.carlistCb.setVisibility(View.GONE);
        holder.carRecharge.setVisibility(View.VISIBLE);
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
    public static void downloadImgByTag(final String object, OSSClient oss, final ImageView iv, final Context context, final int fialImgRes,final SmartCarListRefreshAdapter adapter) {
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
        switch (v.getId()) {
            case R.id.item_carlist_recharge:
                mCarRechargeCallBack.carRechargeClick(v);
                break;
            case R.id.item_carlist_ll:
                mCarUpdateCallBack.carUpateClick(v);
                break;
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if (mOnItemLongClickListener != null) {
            mOnItemLongClickListener.onItemLongClick(v, (int)v.getTag());
        }
        return true;
    }

    public class SmartRefreshViewHolder extends RecyclerView.ViewHolder {
        public CheckBox carlistCb;
        public ImageView carlistIv;
        public TextView carlistLicensenum;
        public TextView carlistBrand;
        public TextView carlistModel;
        public TextView carlistStyle;
        public TextView carTypeTv;
        public TextView expireTimeTv;
        public ImageView carRecharge;
        public LinearLayout carListLL;
        public SmartRefreshViewHolder(View view){
            super(view);
        }
    }

    public void setOnItemLongClickListenr(OnItemLongClickListener listenr) {
        this.mOnItemLongClickListener = listenr;
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(View view, int position);
    }
    /**
     * 充值圖片點擊接口
     */
    public interface CarRechargeCallBack {
        void carRechargeClick(View view);
    }
    /**
     * 充值圖片點擊接口
     */
    public interface CarUpdateCallBack {
        void carUpateClick(View view);
    }

    public void refreshListView(List<CarModel.CarCountAndListModel.CarInsideModel> newList){
        this.carList = newList;
        notifyDataSetChanged();
    }
}
