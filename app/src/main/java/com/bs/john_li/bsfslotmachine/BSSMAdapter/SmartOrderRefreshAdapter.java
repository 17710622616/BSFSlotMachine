package com.bs.john_li.bsfslotmachine.BSSMAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bs.john_li.bsfslotmachine.BSSMModel.ContentsListModel;
import com.bs.john_li.bsfslotmachine.BSSMModel.OrderModel;
import com.bs.john_li.bsfslotmachine.BSSMModel.UserOrderOutModel;
import com.bs.john_li.bsfslotmachine.R;

import java.util.List;

/**
 * Created by John_Li on 28/11/2017.
 */

public class SmartOrderRefreshAdapter extends RecyclerView.Adapter implements View.OnClickListener {
    private List<UserOrderOutModel.UserOrderInsideModel.UserOrderModel> list;
    private final Context mContext;
    private OnItemClickListener mOnitemClickListener = null;

    public SmartOrderRefreshAdapter(Context context, List<UserOrderOutModel.UserOrderInsideModel.UserOrderModel> list) {
        this.list = list;
        mContext = context;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_user_order, parent, false);
        SmartRefreshViewHolder vh = new SmartRefreshViewHolder(view);
        view.setOnClickListener(this);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (list.get(position).getOrderType()) {
            case 1:
                ((SmartRefreshViewHolder)holder).item_order_type.setText("訂單類型：充值订单");
                break;
            case 2:
                ((SmartRefreshViewHolder)holder).item_order_type.setText("訂單類型： 会员续费订单");
                break;
            case 3:
                ((SmartRefreshViewHolder)holder).item_order_type.setText("訂單類型：确定投币机订单 ");
                break;
            case 4:
                ((SmartRefreshViewHolder)holder).item_order_type.setText("訂單類型：未知投币机订单");
                break;
        }
        ((SmartRefreshViewHolder)holder).item_order_no.setText(list.get(position).getOrderNo());
        ((SmartRefreshViewHolder)holder).item_order_money.setText(list.get(position).getTotalAmount());
        switch (list.get(position).getOrderStatus()) {
            case 1:
                ((SmartRefreshViewHolder)holder).item_order_status.setText("訂單狀態：待支付");
                break;
            case 3:
                ((SmartRefreshViewHolder)holder).item_order_status.setText("訂單狀態：已支付");
                break;
            case 4:
                ((SmartRefreshViewHolder)holder).item_order_status.setText("訂單狀態：4.已投币 ");
                break;
            case 9:
                ((SmartRefreshViewHolder)holder).item_order_status.setText("訂單狀態：已取消");
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
        public ImageView item_order_iv;
        public TextView item_order_type;
        public TextView item_order_no;
        public TextView item_order_money;
        public TextView item_order_status;
        public SmartRefreshViewHolder(View view){
            super(view);
            item_order_iv = (ImageView) view.findViewById(R.id.item_order_iv);
            item_order_type = (TextView) view.findViewById(R.id.item_order_type);
            item_order_no = (TextView) view.findViewById(R.id.item_order_no);
            item_order_status = (TextView) view.findViewById(R.id.item_order_status);
        }
    }

    public void setOnItemClickListenr(OnItemClickListener listenr) {
        this.mOnitemClickListener = listenr;
    }

    public static interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void refreshListView(List<UserOrderOutModel.UserOrderInsideModel.UserOrderModel> newList) {
        list = newList;
        notifyDataSetChanged();
    }
}
