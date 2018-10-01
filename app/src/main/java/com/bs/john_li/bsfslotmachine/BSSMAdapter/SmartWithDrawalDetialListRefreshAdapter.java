package com.bs.john_li.bsfslotmachine.BSSMAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bs.john_li.bsfslotmachine.BSSMModel.WalletRecordOutModel;
import com.bs.john_li.bsfslotmachine.BSSMModel.WithDrawalDetialModel;
import com.bs.john_li.bsfslotmachine.BSSMUtils.BSSMCommonUtils;
import com.bs.john_li.bsfslotmachine.R;

import java.util.List;

/**
 * 车辆列表刷新
 * Created by John_Li on 28/11/2017.
 */

public class SmartWithDrawalDetialListRefreshAdapter extends RecyclerView.Adapter<SmartWithDrawalDetialListRefreshAdapter.SmartRefreshViewHolder> implements View.OnClickListener{
    private List<WithDrawalDetialModel.WithDrawalDetialInsideModel.DataBean> list;
    private Context mContext;
    private LayoutInflater mInflater;
    private SmartWithDrawalDetialListRefreshAdapter.OnItemClickListener mOnitemClickListener = null;

    public SmartWithDrawalDetialListRefreshAdapter(Context context, List<WithDrawalDetialModel.WithDrawalDetialInsideModel.DataBean> list) {
        this.list = list;
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }
    @Override
    public SmartWithDrawalDetialListRefreshAdapter.SmartRefreshViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_transaction_detial, parent, false);
        SmartRefreshViewHolder vh = new SmartRefreshViewHolder(view);
        vh.item_td_num = (TextView) view.findViewById(R.id.item_td_num);
        vh.item_td_remark = (TextView) view.findViewById(R.id.item_td_remark);
        vh.item_td_time = (TextView) view.findViewById(R.id.item_td_time);
        vh.item_td_type = (TextView) view.findViewById(R.id.item_td_type);
        vh.item_td_bank_name = (TextView) view.findViewById(R.id.item_td_bank_name);
        vh.item_td_wd_name = (TextView) view.findViewById(R.id.item_td_wd_name);
        vh.item_td_wd_tel = (TextView) view.findViewById(R.id.item_td_wd_tel);
        view.setOnClickListener(this);
        return vh;
    }

    @Override
    public void onBindViewHolder(SmartWithDrawalDetialListRefreshAdapter.SmartRefreshViewHolder holder, int position) {
        holder.item_td_num.setText(String.format("%.2f", list.get(position).getMoney()).toString());
        holder.item_td_time.setText("時間：" + BSSMCommonUtils.stampToDate(String.valueOf(list.get(position).getCreateDate())));
        holder.item_td_bank_name.setText("匯入銀行：" + list.get(position).getBankName());
        holder.item_td_remark.setText("銀行卡號：" + list.get(position).getBankCardNo());
        holder.item_td_wd_name.setText("提現人：" + list.get(position).getRealName());
        holder.item_td_wd_tel.setText("電話：" + list.get(position).getPhoneNumber());

        holder.item_td_bank_name.setVisibility(View.VISIBLE);
        holder.item_td_wd_name.setVisibility(View.VISIBLE);
        holder.item_td_wd_tel.setVisibility(View.VISIBLE);
        holder.item_td_type.setVisibility(View.GONE);
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
        public TextView item_td_remark;
        public TextView item_td_time;
        public TextView item_td_num;
        public TextView item_td_type;
        private TextView item_td_bank_name;
        private TextView item_td_wd_name;
        private TextView item_td_wd_tel;

        public SmartRefreshViewHolder(View view){
            super(view);
        }
    }

    public void setOnItemClickListenr(SmartWithDrawalDetialListRefreshAdapter.OnItemClickListener listenr) {
        this.mOnitemClickListener = listenr;
    }

    public static interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void refreshListView(List<WithDrawalDetialModel.WithDrawalDetialInsideModel.DataBean> newList){
        this.list = newList;
        notifyDataSetChanged();
    }
}
