package com.bs.john_li.bsfslotmachine.BSSMAdapter;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.sdk.android.oss.OSSClient;
import com.bs.john_li.bsfslotmachine.BSSMModel.CarWashMerchantOutModel;
import com.bs.john_li.bsfslotmachine.BSSMModel.UserOrderOutModel;
import com.bs.john_li.bsfslotmachine.R;

import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.util.List;

/**
 * Created by John_Li on 28/11/2017.
 */

public class SmartCarWashMerchatRefreshAdapter extends RecyclerView.Adapter<SmartCarWashMerchatRefreshAdapter.SmartRefreshViewHolder> implements View.OnClickListener {
    private List<CarWashMerchantOutModel.DataBeanX.CarWashMerchatModel> list;
    private final Context mContext;
    private OSSClient oss;
    private LayoutInflater mInflater;
    private LruCache<String ,BitmapDrawable> mMemoryCache;
    private OnItemClickListener mOnitemClickListener = null;
    private ImageOptions options = new ImageOptions.Builder().setImageScaleType(ImageView.ScaleType.FIT_CENTER).setLoadingDrawableId(R.mipmap.img_loading).setFailureDrawableId(R.mipmap.load_img_fail_list).build();

    public SmartCarWashMerchatRefreshAdapter(Context context, List<CarWashMerchantOutModel.DataBeanX.CarWashMerchatModel> list, OSSClient oss) {
        this.list = list;
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
    public SmartCarWashMerchatRefreshAdapter.SmartRefreshViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_car_wash_merchat, parent, false);
        SmartRefreshViewHolder vh = new SmartRefreshViewHolder(view);
        vh.item_cwm_iv = (ImageView) view.findViewById(R.id.item_cwm_iv);
        vh.item_cwm_name_tv = (TextView) view.findViewById(R.id.item_cwm_name_tv);
        vh.item_cwm_sold_tv = (TextView) view.findViewById(R.id.item_cwm_sold_tv);
        vh.item_cwm_address_tv = (TextView) view.findViewById(R.id.item_cwm_address_tv);
        vh.item_cwm_distance_tv = (TextView) view.findViewById(R.id.item_cwm_distance_tv);
        vh.item_cwm_distance_ll = (LinearLayout) view.findViewById(R.id.item_cwm_distance_ll);
        view.setOnClickListener(this);
        return vh;
    }

    @Override
    public void onBindViewHolder(SmartCarWashMerchatRefreshAdapter.SmartRefreshViewHolder holder, int position) {
        x.image().bind(holder.item_cwm_iv, list.get(position).getSellerLogo(), options);
        holder.item_cwm_name_tv.setText(list.get(position).getSellerName());
        holder.item_cwm_sold_tv.setText("已售：" + list.get(position).getOrderCount());
        holder.item_cwm_address_tv.setText("地址：" +list.get(position).getAddress());
        holder.item_cwm_distance_tv.setText(list.get(position).getMeter());
        holder.itemView.setTag(position);
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
        public ImageView item_cwm_iv;
        public TextView item_cwm_name_tv;
        public TextView item_cwm_sold_tv;
        public TextView item_cwm_address_tv;
        public TextView item_cwm_distance_tv;
        public LinearLayout item_cwm_distance_ll;
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

    public void refreshListView(List<CarWashMerchantOutModel.DataBeanX.CarWashMerchatModel> newList) {
        list = newList;
        notifyDataSetChanged();
    }
}
