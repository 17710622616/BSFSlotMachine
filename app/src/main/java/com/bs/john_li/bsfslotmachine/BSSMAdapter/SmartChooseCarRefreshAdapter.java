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
import com.bs.john_li.bsfslotmachine.BSSMActivity.Parking.ChooseCarActivity;
import com.bs.john_li.bsfslotmachine.BSSMModel.CarModel;
import com.bs.john_li.bsfslotmachine.BSSMUtils.AliyunOSSUtils;
import com.bs.john_li.bsfslotmachine.BSSMUtils.BSSMCommonUtils;
import com.bs.john_li.bsfslotmachine.BSSMUtils.BSSMConfigtor;
import com.bs.john_li.bsfslotmachine.R;

import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.io.InputStream;
import java.util.List;

/**
 * 選擇車輛适配器
 * Created by John_Li on 28/11/2017.
 */

public class SmartChooseCarRefreshAdapter extends RecyclerView.Adapter<SmartChooseCarRefreshAdapter.SmartRefreshViewHolder> implements View.OnClickListener {
    private List<CarModel.CarCountAndListModel.CarInsideModel> carList;
    private final Context mContext;
    private OSSClient oss;
    //private OnItemClickListener mOnitemClickListener = null;
    private CarRechargeCallBack mCarRechargeCallBack = null;
    private CarChooseCallBack mCarChooseCallBack = null;
    private LayoutInflater mInflater;
    private LruCache<String, BitmapDrawable> mMemoryCache;
    private ImageOptions options = new ImageOptions.Builder().setSize(0, 0).setLoadingDrawableId(R.mipmap.img_loading).setFailureDrawableId(R.mipmap.load_img_fail_list).build();

    public SmartChooseCarRefreshAdapter(Context context, List<CarModel.CarCountAndListModel.CarInsideModel> list, OSSClient oss) {
        this.carList = list;
        mContext = context;
        this.oss = oss;
        mInflater = LayoutInflater.from(context);
        mCarRechargeCallBack = (ChooseCarActivity)mContext;
        mCarChooseCallBack = (ChooseCarActivity)mContext;
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
    public SmartChooseCarRefreshAdapter.SmartRefreshViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_car_list, parent, false);
        SmartRefreshViewHolder vh = new SmartRefreshViewHolder(view);
        vh.carlistCb = (CheckBox) view.findViewById(R.id.item_carlist_cb);
        vh.carlistIv = (ImageView) view.findViewById(R.id.item_carlist_iv);
        vh.carlistLicensenum = (TextView) view.findViewById(R.id.item_carlist_licensenum);
        vh.carlistBrand = (TextView) view.findViewById(R.id.item_carlist_brand);
        vh.carlistModel = (TextView) view.findViewById(R.id.item_carlist_model);
        vh.carlistStyle = (TextView) view.findViewById(R.id.item_carlist_style);
        vh.carTypeTv = (TextView) view.findViewById(R.id.item_carlist_car_type);
        //vh.notRechrageTv = view.findViewById(R.id.item_carlist_not_rechrage_tv);
        vh.carListLL = view.findViewById(R.id.item_carlist_ll);
        vh.carRecharge = (ImageView) view.findViewById(R.id.item_carlist_recharge);
        vh.carExpandeTimeTv = view.findViewById(R.id.item_carlist_expiry_time);
        view.setOnClickListener(this);
        return vh;
    }

    @Override
    public void onBindViewHolder(SmartChooseCarRefreshAdapter.SmartRefreshViewHolder holder, int position) {
        holder.carlistLicensenum.setText("車牌：" + carList.get(position).getCarNo());
        holder.carlistBrand.setText("品牌：" + carList.get(position).getCarBrand());
        holder.carlistStyle.setText("款式：" + carList.get(position).getCarStyle());
        holder.carlistModel.setText("型號：" + carList.get(position).getModelForCar());
        //AliyunOSSUtils.downloadImg(carList.get(position).getImgUrl(), oss, holder.carlistIv, mContext, R.mipmap.load_img_fail_list);
        x.image().bind(holder.carlistIv, carList.get(position).getImgUrl(), options);
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

        if (carList.get(position).getVipType() == 0) {
            if (carList.get(position).getIfPay() == 0) {
                holder.carRecharge.setImageResource(R.mipmap.recharge);
            } else {
                holder.carRecharge.setImageResource(R.mipmap.member);
            }
        } else if (carList.get(position).getVipType() == 1) {   // 日費費
            holder.carRecharge.setImageResource(R.mipmap.member);
        } else if (carList.get(position).getVipType() == 2) {   // 月費
            holder.carRecharge.setImageResource(R.mipmap.recharge_mouth);
        } else if (carList.get(position).getVipType() == 3) {   // 季度費
            holder.carRecharge.setImageResource(R.mipmap.recharge_quarter);
        } else if (carList.get(position).getVipType() == 4) {   // 半年費
            holder.carRecharge.setImageResource(R.mipmap.recharge_halfyear);
        } else if (carList.get(position).getVipType() == 5) {   // 年費
            holder.carRecharge.setImageResource(R.mipmap.year);
        } else {
            holder.carRecharge.setImageResource(R.mipmap.member);
        }
        if (carList.get(position).getExpiryTime() != null) {
            holder.carExpandeTimeTv.setVisibility(View.VISIBLE);
            holder.carExpandeTimeTv.setText("到期時間：" + BSSMCommonUtils.stampToDate(carList.get(position).getExpiryTime()));
        }
        holder.carListLL.setOnClickListener(this);
        holder.carRecharge.setOnClickListener(this);
        holder.carlistCb.setVisibility(View.GONE);
        holder.carRecharge.setVisibility(View.VISIBLE);
        holder.carRecharge.setTag(String.valueOf(position));
        holder.carListLL.setTag(String.valueOf(position));
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
    public static void downloadImgByTag(final String object, OSSClient oss, final ImageView iv, final Context context, final int fialImgRes,final SmartChooseCarRefreshAdapter adapter) {
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
                mCarChooseCallBack.carChooseClick(v);
                break;
        }

        /*if (mOnitemClickListener != null) {
            mOnitemClickListener.onItemClick(v, (int)v.getTag());
        }*/
    }

    public class SmartRefreshViewHolder extends RecyclerView.ViewHolder {
        public CheckBox carlistCb;
        public ImageView carlistIv;
        public TextView carlistLicensenum;
        public TextView carlistBrand;
        public TextView carlistModel;
        public TextView carlistStyle;
        public TextView carTypeTv;
        //public TextView notRechrageTv;
        public ImageView carRecharge;
        public LinearLayout carListLL;
        public TextView carExpandeTimeTv;

        public SmartRefreshViewHolder(View view){
            super(view);
        }
    }

    /*public void setOnItemClickListenr(OnItemClickListener listenr) {
        this.mOnitemClickListener = listenr;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }*/

    /**
     * 充值圖片點擊接口
     */
    public interface CarRechargeCallBack {
        void carRechargeClick(View view);
    }
    /**
     * 除充值圖片外区域點擊接口
     */
    public interface CarChooseCallBack {
        void carChooseClick(View view);
    }
}
