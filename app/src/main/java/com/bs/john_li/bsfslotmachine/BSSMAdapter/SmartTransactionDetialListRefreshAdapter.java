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
import com.bs.john_li.bsfslotmachine.BSSMModel.TransactionDetialOutModel;
import com.bs.john_li.bsfslotmachine.BSSMModel.WalletRecordOutModel;
import com.bs.john_li.bsfslotmachine.BSSMUtils.BSSMCommonUtils;
import com.bs.john_li.bsfslotmachine.BSSMUtils.BSSMConfigtor;
import com.bs.john_li.bsfslotmachine.R;

import java.io.InputStream;
import java.util.List;

/**
 * 车辆列表刷新
 * Created by John_Li on 28/11/2017.
 */

public class SmartTransactionDetialListRefreshAdapter extends RecyclerView.Adapter<SmartTransactionDetialListRefreshAdapter.SmartRefreshViewHolder> implements View.OnClickListener{
    private List<WalletRecordOutModel.DataBeanX.WalletRecordModel> list;
    private Context mContext;
    private LayoutInflater mInflater;
    private SmartTransactionDetialListRefreshAdapter.OnItemClickListener mOnitemClickListener = null;

    public SmartTransactionDetialListRefreshAdapter(Context context, List<WalletRecordOutModel.DataBeanX.WalletRecordModel> list) {
        this.list = list;
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }
    @Override
    public SmartTransactionDetialListRefreshAdapter.SmartRefreshViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_transaction_detial, parent, false);
        SmartRefreshViewHolder vh = new SmartRefreshViewHolder(view);
        vh.item_td_num = (TextView) view.findViewById(R.id.item_td_num);
        vh.item_td_remark = (TextView) view.findViewById(R.id.item_td_remark);
        vh.item_td_time = (TextView) view.findViewById(R.id.item_td_time);
        vh.item_td_type = (TextView) view.findViewById(R.id.item_td_type);
        view.setOnClickListener(this);
        return vh;
    }

    @Override
    public void onBindViewHolder(SmartTransactionDetialListRefreshAdapter.SmartRefreshViewHolder holder, int position) {
        holder.item_td_num.setText(String.format("%.2f", list.get(position).getAmount()).toString());
        holder.item_td_time.setText("時間：" + BSSMCommonUtils.stampToDate(String.valueOf(list.get(position).getCreateTime())));
        switch (list.get(position).getPayType()) {
            case 1:
                holder.item_td_remark.setText("支付方式：微信");
                break;
            case 2:
                holder.item_td_remark.setText("支付方式：支付寶");
                break;
            case 3:
                holder.item_td_remark.setText("支付方式：賬戶錢包");
                break;
        }
        if (list.get(position).getFlag() == 1) {
            holder.item_td_type.setText("+");
            holder.item_td_type.setTextColor(mContext.getResources().getColor(R.color.colorSubmitGreen));
        } else {
            holder.item_td_type.setText("-");
            holder.item_td_type.setTextColor(mContext.getResources().getColor(R.color.colorLoginOutRed));
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
        public TextView item_td_remark;
        public TextView item_td_time;
        public TextView item_td_num;
        public TextView item_td_type;
        public SmartRefreshViewHolder(View view){
            super(view);
        }
    }

    public void setOnItemClickListenr(SmartTransactionDetialListRefreshAdapter.OnItemClickListener listenr) {
        this.mOnitemClickListener = listenr;
    }

    public static interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void refreshListView(List<WalletRecordOutModel.DataBeanX.WalletRecordModel> newList){
        this.list = newList;
        notifyDataSetChanged();
    }
}
