package com.bs.john_li.bsfslotmachine.BSSMAdapter;

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
import com.bs.john_li.bsfslotmachine.BSSMModel.CWUserOrderOutModel;
import com.bs.john_li.bsfslotmachine.BSSMModel.UserOrderOutModel;
import com.bs.john_li.bsfslotmachine.BSSMUtils.BSSMCommonUtils;
import com.bs.john_li.bsfslotmachine.R;

import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.util.List;

/**
 * Created by John_Li on 28/11/2017.
 */

public class SmartCWOrderRefreshAdapter extends RecyclerView.Adapter<SmartCWOrderRefreshAdapter.SmartRefreshViewHolder> implements View.OnClickListener {
    private List<CWUserOrderOutModel.DataBeanX.CWUserOrderModel> list;
    private final Context mContext;
    private OSSClient oss;
    private LayoutInflater mInflater;
    private LruCache<String ,BitmapDrawable> mMemoryCache;
    private OnItemClickListener mOnitemClickListener = null;
    private ImageOptions options = new ImageOptions.Builder().setImageScaleType(ImageView.ScaleType.FIT_CENTER).setLoadingDrawableId(R.mipmap.img_loading).setFailureDrawableId(R.mipmap.load_img_fail_list).build();

    public SmartCWOrderRefreshAdapter(Context context, List<CWUserOrderOutModel.DataBeanX.CWUserOrderModel> list, OSSClient oss) {
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
    public SmartCWOrderRefreshAdapter.SmartRefreshViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_cw_order, parent, false);
        SmartRefreshViewHolder vh = new SmartRefreshViewHolder(view);
        vh.item_cworder_iv = (ImageView) view.findViewById(R.id.item_cworder_iv);
        vh.item_cworder_name = (TextView) view.findViewById(R.id.item_cworder_name);
        vh.item_cworder_expiration_time = (TextView) view.findViewById(R.id.item_cworder_expiration_time);
        vh.item_cworder_price = (TextView) view.findViewById(R.id.item_cworder_price);
        vh.item_cworder_status = (TextView) view.findViewById(R.id.item_cworder_status);
        vh.item_cworder_type = (TextView) view.findViewById(R.id.item_cworder_type);
        vh.item_cworder_ordertype = (TextView) view.findViewById(R.id.item_cworder_ordertype);
        view.setOnClickListener(this);
        return vh;
    }

    @Override
    public void onBindViewHolder(SmartCWOrderRefreshAdapter.SmartRefreshViewHolder holder, int position) {
        x.image().bind(holder.item_cworder_iv, list.get(position).getSellerLogo(),options);
        holder.item_cworder_name.setText(list.get(position).getChargeRemark() + "：" +list.get(position).getSellerName());
        holder.item_cworder_expiration_time.setText("到期時間：" + BSSMCommonUtils.stampToDate(String.valueOf(list.get(position).getCreateTime())));
        holder.item_cworder_price.setText("訂單金額：" + String.valueOf(list.get(position).getPayAmount()));
        switch (list.get(position).getOrderStatus()) {
            case 1: // 待支付
                holder.item_cworder_status.setText("訂單狀態：待支付");
                break;
            case 2: // 支付中
                holder.item_cworder_status.setText("訂單狀態：支付中");
                break;
            case 3: // 已支付
                holder.item_cworder_status.setText("訂單狀態：已支付");
                break;
            case 4: // 已完成
                holder.item_cworder_status.setText("訂單狀態：已完成");
                break;
            case 5: // 退款中
                holder.item_cworder_status.setText("訂單狀態：退款中");
                break;
            case 6: // 已退款
                holder.item_cworder_status.setText("訂單狀態：已退款");
                break;
            case 7: // 已失效
                holder.item_cworder_status.setText("訂單狀態：已失效");
                break;
            case 9: // 已取消
                holder.item_cworder_status.setText("訂單狀態：已取消");
                break;
        }

        switch (list.get(position).getType()) {
            case 0: // 洗車訂單
                holder.item_cworder_type.setText("訂單類型：洗車訂單");
                break;
            case 1: // 汽車零件訂單
                holder.item_cworder_type.setText("訂單類型：汽車零件訂單");
                break;
        }

        switch (list.get(position).getOrderType()) {
            case 0: // 正常購買
                holder.item_cworder_ordertype.setVisibility(View.GONE);
                break;
            case 1: // 系統贈送
                holder.item_cworder_ordertype.setVisibility(View.VISIBLE);
                holder.item_cworder_ordertype.setText("訂單類別：系統贈送");
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
        public ImageView item_cworder_iv;
        public TextView item_cworder_name;
        public TextView item_cworder_expiration_time;
        public TextView item_cworder_price;
        public TextView item_cworder_status;
        public TextView item_cworder_type;
        public TextView item_cworder_ordertype;
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

    public void refreshListView(List<CWUserOrderOutModel.DataBeanX.CWUserOrderModel> newList) {
        list = newList;
        notifyDataSetChanged();
    }
}
