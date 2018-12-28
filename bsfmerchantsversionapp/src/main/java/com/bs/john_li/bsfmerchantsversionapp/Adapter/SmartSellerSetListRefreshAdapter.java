package com.bs.john_li.bsfmerchantsversionapp.Adapter;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.sdk.android.oss.OSSClient;
import com.bs.john_li.bsfmerchantsversionapp.Model.SellerSetOutModel;
import com.bs.john_li.bsfmerchantsversionapp.R;
import com.bs.john_li.bsfmerchantsversionapp.Utils.BSFCommonUtils;

import org.xutils.image.ImageOptions;

import java.util.List;

/**
 * Created by John_Li on 28/11/2017.
 */

public class SmartSellerSetListRefreshAdapter extends RecyclerView.Adapter<SmartSellerSetListRefreshAdapter.SmartRefreshViewHolder> implements View.OnClickListener {
    private List<SellerSetOutModel.DataBeanX.SellerSetModel> list;
    private final Context mContext;
    private OSSClient oss;
    private LayoutInflater mInflater;
    private LruCache<String ,BitmapDrawable> mMemoryCache;
    private OnItemClickListener mOnitemClickListener = null;
    private ImageOptions options = new ImageOptions.Builder().setSize(0, 0).setLoadingDrawableId(R.mipmap.img_loading_list).setFailureDrawableId(R.mipmap.load_img_fail_list).build();

    public SmartSellerSetListRefreshAdapter(Context context, List<SellerSetOutModel.DataBeanX.SellerSetModel> list, OSSClient oss) {
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
    public SmartSellerSetListRefreshAdapter.SmartRefreshViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_seller_list, parent, false);
        SmartRefreshViewHolder vh = new SmartRefreshViewHolder(view);
        vh.item_seller_set_iv = (ImageView) view.findViewById(R.id.item_seller_set_iv);
        vh.item_seller_set_name = (TextView) view.findViewById(R.id.item_seller_set_name);
        vh.item_seller_set_expiration_time = (TextView) view.findViewById(R.id.item_seller_set_expiration_time);
        vh.item_seller_set_market_price = (TextView) view.findViewById(R.id.item_seller_set_market_price);
        vh.item_seller_set_cost_price = (TextView) view.findViewById(R.id.item_seller_set_cost_price);
        vh.item_seller_set_des = (TextView) view.findViewById(R.id.item_seller_set_des);
        vh.item_seller_set_status = (TextView) view.findViewById(R.id.item_seller_set_status);
        view.setOnClickListener(this);
        return vh;
    }

    @Override
    public void onBindViewHolder(SmartSellerSetListRefreshAdapter.SmartRefreshViewHolder holder, int position) {
        //x.image().bind(holder.item_seller_set_iv, list.get(position).getSellerLogo(),options);
        holder.item_seller_set_name.setText(list.get(position).getChargeName());
        holder.item_seller_set_expiration_time.setText("到期時間：" + BSFCommonUtils.stampToDate(String.valueOf(list.get(position).getExprieTime())));
        holder.item_seller_set_market_price.setText("市面金額：MOP" + String.valueOf(list.get(position).getMarketPrice()));
        holder.item_seller_set_cost_price.setText("套 餐 價：MOP" + String.valueOf(list.get(position).getCostPrice()));
        holder.item_seller_set_des.setText("介       紹：" + String.valueOf(list.get(position).getDescription()));
        switch (list.get(position).getStatus()) {
            case 0: // 待支付
                holder.item_seller_set_status.setText("審核狀態：創建中");
                break;
            case 1: // 支付中
                holder.item_seller_set_status.setText("訂單狀態：審核中");
                break;
            case 2: // 已支付
                holder.item_seller_set_status.setText("訂單狀態：審核通過");
                break;
            case 3: // 已完成
                holder.item_seller_set_status.setText("訂單狀態：審核失敗");
                break;
        }
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
        public ImageView item_seller_set_iv;
        public TextView item_seller_set_name;
        public TextView item_seller_set_des;
        public TextView item_seller_set_expiration_time;
        public TextView item_seller_set_market_price;
        public TextView item_seller_set_cost_price;
        public TextView item_seller_set_status;
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

    public void refreshListView(List<SellerSetOutModel.DataBeanX.SellerSetModel> newList) {
        list = newList;
        notifyDataSetChanged();
    }
}
