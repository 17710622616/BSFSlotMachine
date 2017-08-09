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
 * Created by John_Li on 10/8/2017.
 */

public class GuoJiangLongAdapter extends BaseAdapter {
    private List<String> gjlList;
    private LayoutInflater inflater;
    private Context mContext;
    public GuoJiangLongAdapter(Context context, List<String> gjlList) {
        this.gjlList = gjlList;
        inflater = LayoutInflater.from(context);
        mContext = context;
    }
    @Override
    public int getCount() {
        return gjlList.size();
    }

    @Override
    public Object getItem(int i) {
        return gjlList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.item_guojianglong, null);
        TextView tv = view.findViewById(R.id.item_gjl_tv);
        tv.setText(gjlList.get(i));
        return view;
    }
}
