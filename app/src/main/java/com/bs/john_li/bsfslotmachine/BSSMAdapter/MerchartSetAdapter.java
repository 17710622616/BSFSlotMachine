package com.bs.john_li.bsfslotmachine.BSSMAdapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.bs.john_li.bsfslotmachine.BSSMModel.MerchatSetOutModel;
import com.bs.john_li.bsfslotmachine.R;

import java.util.List;

/**
 * Created by John_Li on 15/11/2018.
 */

public class MerchartSetAdapter extends BaseAdapter {
    private List<MerchatSetOutModel.MerchatSetModel.SellerChargeBean> list;
    private LayoutInflater inflater;
    private Context mContext;
    private MerchatSetOutModel.MerchatSetModel.SellerChargeBean sellerChargeBean;

    public MerchartSetAdapter(Context context, List<MerchatSetOutModel.MerchatSetModel.SellerChargeBean> list) {
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


    public void setList(List<MerchatSetOutModel.MerchatSetModel.SellerChargeBean> list) {
        this.list = list;
    }

    // 选中当前选项时，让其他选项不被选中
    public void select(int position) {
        if (!list.get(position).isSelected()) {
            list.get(position).setSelected(true);
            for (int i = 0; i < list.size(); i++) {
                if (i != position) {
                    list.get(i).setSelected(false);
                }
            }
        }
        refreshListView(list);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View convertView = view;
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_merchart_set, null);
            holder.item_ms_rb = convertView.findViewById(R.id.item_ms_rb);
            holder.item_ms_name_tv = convertView.findViewById(R.id.item_ms_name_tv);
            holder.item_ms_original_price_tv = convertView.findViewById(R.id.item_ms_original_price_tv);
            holder.item_ms_price_tv = convertView.findViewById(R.id.item_ms_price_tv);

            holder.item_ms_rb.setClickable(false);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.item_ms_original_price_tv.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        holder.item_ms_original_price_tv.setText("原價:" + list.get(i).getMarketPrice());
        holder.item_ms_price_tv.setText("MOP" + list.get(i).getCostPrice());
        holder.item_ms_name_tv.setText(list.get(i).getChargeName());

        sellerChargeBean = (MerchatSetOutModel.MerchatSetModel.SellerChargeBean) getItem(i);
        holder.item_ms_rb.setChecked(sellerChargeBean.isSelected());
        return convertView;
    }

    class ViewHolder {
        public RadioButton item_ms_rb;
        public TextView item_ms_name_tv;
        public TextView item_ms_original_price_tv;
        public TextView item_ms_price_tv;
    }

    public void refreshListView(List<MerchatSetOutModel.MerchatSetModel.SellerChargeBean> newList) {
        this.list = newList;
        notifyDataSetChanged();
    }
}