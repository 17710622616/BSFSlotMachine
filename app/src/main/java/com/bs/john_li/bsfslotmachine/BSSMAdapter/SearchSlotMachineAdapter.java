package com.bs.john_li.bsfslotmachine.BSSMAdapter;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bs.john_li.bsfslotmachine.R;

import java.util.List;

/**
 * Created by John_Li on 22/9/2017.
 */

public class SearchSlotMachineAdapter extends ArrayAdapter {
    private List<String> smList;
    private LayoutInflater inflater;
    private Context mContext;

    public SearchSlotMachineAdapter(Context context, List objects) {
        super(context, R.layout.item_search_slot_machine, R.id.item_search_slot_machine_tv, objects);
        this.smList = objects;
        this.mContext = context;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return smList.size();
    }

    @Override
    public Object getItem(int i) {
        return smList.get(i);
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
            convertView = inflater.inflate(R.layout.item_search_slot_machine, null);
            holder.carTypeTv = convertView.findViewById(R.id.item_search_slot_machine_tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.carTypeTv.setText(smList.get(i));
        return convertView;
    }

    class ViewHolder {
        public TextView carTypeTv;
    }

    public void refreshData(List<String> newList) {
        this.smList = newList;
        notifyDataSetChanged();
    }
}
