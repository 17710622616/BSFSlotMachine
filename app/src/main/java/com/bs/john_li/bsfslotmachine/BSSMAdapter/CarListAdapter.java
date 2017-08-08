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

public class CarListAdapter extends BaseAdapter {
    private List<String> carList;
    private LayoutInflater inflater;
    private Context mContext;
    public  CarListAdapter(Context context, List<String> carList) {
        this.carList = carList;
        this.mContext = context;
        inflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return carList.size();
    }

    @Override
    public Object getItem(int i) {
        return carList.get(i);
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
            convertView = inflater.inflate(R.layout.item_car_list, null);
            holder.carTypeTv = convertView.findViewById(R.id.item_carlist_car_type);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.carTypeTv.setText(carList.get(i));
        return convertView;
    }

    class ViewHolder {
        public TextView carTypeTv;
    }
}
