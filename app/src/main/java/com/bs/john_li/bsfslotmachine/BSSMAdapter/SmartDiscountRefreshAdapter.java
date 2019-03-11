package com.bs.john_li.bsfslotmachine.BSSMAdapter;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.sdk.android.oss.OSSClient;
import com.bs.john_li.bsfslotmachine.BSSMActivity.MainActivity;
import com.bs.john_li.bsfslotmachine.BSSMActivity.Mine.DiscountActivity;
import com.bs.john_li.bsfslotmachine.BSSMModel.DiscountOutModel;
import com.bs.john_li.bsfslotmachine.BSSMModel.UserOrderOutModel;
import com.bs.john_li.bsfslotmachine.BSSMUtils.BSSMCommonUtils;
import com.bs.john_li.bsfslotmachine.R;

import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.util.List;

/**
 * Created by John_Li on 28/11/2017.
 */

public class SmartDiscountRefreshAdapter extends RecyclerView.Adapter<SmartDiscountRefreshAdapter.SmartRefreshViewHolder> implements OnClickListener {
    private List<DiscountOutModel.DataBeanX.DiscountModel> list;
    private final Context mContext;
    private LayoutInflater mInflater;
    private CouponGoCallBack mCouponGoCallBack = null;
    private int type;   // 0：可用， 1：過期紅包
    private SmartDiscountRefreshAdapter.OnItemClickListener mOnitemClickListener = null;
    public SmartDiscountRefreshAdapter(Context context, List<DiscountOutModel.DataBeanX.DiscountModel> list, int type) {
        this.list = list;
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mCouponGoCallBack = (DiscountActivity)mContext;
        this.type = type;
    }
    @Override
    public SmartDiscountRefreshAdapter.SmartRefreshViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_discount, parent, false);
        SmartRefreshViewHolder vh = new SmartRefreshViewHolder(view);
        vh.item_discount_money = (TextView) view.findViewById(R.id.item_discount_money);
        vh.item_discount_condition = (TextView) view.findViewById(R.id.item_discount_condition);
        vh.item_discount_type = (TextView) view.findViewById(R.id.item_discount_type);
        vh.item_discount_expire = (TextView) view.findViewById(R.id.item_discount_expire);
        vh.item_discount_go = (TextView) view.findViewById(R.id.item_discount_go);
        vh.item_discount_limiting_condition = (TextView) view.findViewById(R.id.item_discount_limiting_condition);
        vh.item_discount_overdue = (ImageView) view.findViewById(R.id.item_discount_overdue);
        view.setOnClickListener(this);
        return vh;
    }

    @Override
    public void onBindViewHolder(SmartDiscountRefreshAdapter.SmartRefreshViewHolder holder, int position) {
        holder.item_discount_type.setText(String.valueOf(list.get(position).getCouponName()));
        holder.item_discount_money.setText(String.valueOf(list.get(position).getCouponValue()));
        holder.item_discount_condition.setText(String.valueOf(list.get(position).getRemark()));
        holder.item_discount_limiting_condition.setText(String.valueOf(list.get(position).getRemark2()));
        holder.item_discount_expire.setText("到期時間：" + BSSMCommonUtils.stampToDate(String.valueOf(list.get(position).getUseEndTime())));
        if (type == 0) {
            holder.item_discount_go.setVisibility(View.VISIBLE);
            holder.item_discount_overdue.setVisibility(View.GONE);
        } else {
            holder.item_discount_go.setVisibility(View.GONE);
            holder.item_discount_overdue.setVisibility(View.VISIBLE);
        }
        holder.item_discount_go.setOnClickListener(this);
        holder.item_discount_go.setTag(String.valueOf(position));
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.item_discount_go:
                mCouponGoCallBack.couponGoClick(v);
                break;
        }
    }

    public void setOnItemClickListenr(SmartCWOrderRefreshAdapter.OnItemClickListener listenr) {
    }

    public void setOnItemClickListenr(OnItemClickListener onItemClickListener) {
        this.mOnitemClickListener = onItemClickListener;
    }

    public static interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public class SmartRefreshViewHolder extends RecyclerView.ViewHolder {
        public TextView item_discount_money;
        public TextView item_discount_condition;
        public TextView item_discount_type;
        public TextView item_discount_expire;
        public TextView item_discount_go;
        public TextView item_discount_limiting_condition;
        public ImageView item_discount_overdue;
        public SmartRefreshViewHolder(View view){
            super(view);
        }
    }

    /**
     * 充值圖片點擊接口
     */
    public interface CouponGoCallBack {
        void couponGoClick(View view);
    }

    public void refreshListView(List<DiscountOutModel.DataBeanX.DiscountModel> newList) {
        list = newList;
        notifyDataSetChanged();
    }
}
