package com.bs.john_li.bsfslotmachine.BSSMAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bs.john_li.bsfslotmachine.R;

import java.util.List;

/**
 * Created by John_Li on 6/8/2017.
 */

public class DiscountAdapter extends BaseAdapter {
    private List<String> discountList;
    private LayoutInflater inflater;
    public  DiscountAdapter(Context context,List<String> discountList){
        this.discountList = discountList;
        inflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return discountList.size();
    }

    @Override
    public Object getItem(int i) {
        return discountList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View contentView = view;
        ViewHolder holder = null;
        if (contentView == null){
            holder = new ViewHolder();
            contentView = inflater.inflate(R.layout.item_discount, null);
            holder.moneyTv = contentView.findViewById(R.id.item_discount_money);
            contentView.setTag(holder);
        } else {
            holder = (ViewHolder) contentView.getTag();
        }
        holder.moneyTv.setText(discountList.get(i));
        if (i / 2 == 0){
            holder.newDiscountIv.setVisibility(View.VISIBLE);
        } else {
            holder.newDiscountIv.setVisibility(View.INVISIBLE);
        }
        return contentView;
    }

    public class ViewHolder{
        public TextView moneyTv;
        public ImageView newDiscountIv;

        public TextView getMoneyTv() {
            return moneyTv;
        }

        public void setMoneyTv(TextView moneyTv) {
            this.moneyTv = moneyTv;
        }
    }
}
