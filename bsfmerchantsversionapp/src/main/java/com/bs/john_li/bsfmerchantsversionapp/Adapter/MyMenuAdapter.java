package com.bs.john_li.bsfmerchantsversionapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bs.john_li.bsfmerchantsversionapp.R;

/**
 * Created by John_Li on 17/11/2018.
 */

public class MyMenuAdapter extends BaseAdapter {
    private String menu[] = {"埋單\n", "訂單\n列表", "發佈\n套餐", "報表\n", "登出\n"};
    private LayoutInflater inflater;
    private Context mContext;
    public MyMenuAdapter(Context context) {
        mContext = context;
        inflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return menu.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_string, null);
            holder.menuTv = convertView.findViewById(R.id.item_string_tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.menuTv.setText(menu[position]);
        return convertView;
    }

    class ViewHolder {
        public TextView menuTv;
    }
}
