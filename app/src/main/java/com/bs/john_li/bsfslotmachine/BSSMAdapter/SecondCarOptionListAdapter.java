package com.bs.john_li.bsfslotmachine.BSSMAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bs.john_li.bsfslotmachine.BSSMModel.CarModel;
import com.bs.john_li.bsfslotmachine.R;

import java.util.List;

/**
 * Created by John_Li on 9/8/2017.
 */

public class SecondCarOptionListAdapter extends BaseAdapter{
    private List<String> list;
    private LayoutInflater inflater;
    private Context mContext;
    public SecondCarOptionListAdapter(Context context, List<String> list) {
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
            convertView = inflater.inflate(R.layout.item_string, null);
            holder.item_string_tv = convertView.findViewById(R.id.item_string_tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.item_string_tv.setText(list.get(i));
        return convertView;
    }

    class ViewHolder {
        public TextView item_string_tv;
    }

    public void refreshListView(List<String> newList){
        this.list = newList;
        notifyDataSetChanged();
    }

    /**
     * 充值圖片點擊接口
     */
    public interface CarRechargeCallBack {
        void carRechargeClick(View view);
    }
}
