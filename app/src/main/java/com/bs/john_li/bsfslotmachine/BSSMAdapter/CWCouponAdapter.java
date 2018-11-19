package com.bs.john_li.bsfslotmachine.BSSMAdapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bs.john_li.bsfslotmachine.BSSMModel.CWUserOrderDetialOutModel;
import com.bs.john_li.bsfslotmachine.BSSMModel.CarModel;
import com.bs.john_li.bsfslotmachine.R;

import java.util.List;

/**
 * 洗車券
 * Created by John_Li on 9/8/2017.
 */

public class CWCouponAdapter extends BaseAdapter {
    private List<CWUserOrderDetialOutModel.DataBean.CouponListBean> list;
    private LayoutInflater inflater;
    private Context mContext;
    public CWCouponAdapter(Context context, List<CWUserOrderDetialOutModel.DataBean.CouponListBean> list) {
        this.list = list;
        this.mContext = context;
        inflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View convertView = view;
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_cw_coupon, null);
            holder.cw_coupon_num_tv = convertView.findViewById(R.id.cw_coupon_num_tv);
            holder.cw_coupon_num_iv = convertView.findViewById(R.id.cw_coupon_num_iv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.cw_coupon_num_tv.setText("券號：" + list.get(i).getCouponCode());
        if (list.get(i).getStatus() == 1) {
            holder.cw_coupon_num_tv.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            holder.cw_coupon_num_iv.setVisibility(View.GONE);
        } else {
            holder.cw_coupon_num_iv.setVisibility(View.VISIBLE);
        }
        return convertView;
    }

    class ViewHolder {
        public TextView cw_coupon_num_tv;
        public ImageView cw_coupon_num_iv;
    }

    public void refreshListView(List<CWUserOrderDetialOutModel.DataBean.CouponListBean> newList){
        this.list = newList;
        notifyDataSetChanged();
    }
}
