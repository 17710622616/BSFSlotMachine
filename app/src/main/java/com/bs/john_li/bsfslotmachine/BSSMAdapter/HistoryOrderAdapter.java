package com.bs.john_li.bsfslotmachine.BSSMAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bs.john_li.bsfslotmachine.R;

import java.util.List;

/**
 * Created by John_Li on 9/8/2017.
 */

public class HistoryOrderAdapter extends BaseAdapter {
    private List<String> historyList;
    private LayoutInflater inflater;
    private Context mContext;

    public  HistoryOrderAdapter(List<String> historyList, Context context) {
        this.historyList = historyList;
        this.mContext = context;
        inflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return historyList.size();
    }

    @Override
    public Object getItem(int i) {
        return historyList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        View convertView = view;
        if (convertView == null){
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_history_order, null);
            holder.licenseNumTv = convertView.findViewById(R.id.item_history_licensenum);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.licenseNumTv.setText(historyList.get(i));
        return convertView;
    }

    class ViewHolder {
        public TextView licenseNumTv;
    }
}
