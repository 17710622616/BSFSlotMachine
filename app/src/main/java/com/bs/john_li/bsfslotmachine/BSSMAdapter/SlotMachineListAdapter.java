package com.bs.john_li.bsfslotmachine.BSSMAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bs.john_li.bsfslotmachine.BSSMModel.SlotMachineModel;
import com.bs.john_li.bsfslotmachine.R;
import com.google.android.gms.vision.text.Text;

import java.util.List;

/**
 * Created by John_Li on 21/9/2017.
 */

public class SlotMachineListAdapter extends BaseAdapter {
    private List<SlotMachineModel> smList;
    private LayoutInflater mInflater;
    private Context mContext;

    public SlotMachineListAdapter(List<SlotMachineModel> smList, Context context) {
        this.smList = smList;
        mInflater = LayoutInflater.from(context);
        mContext = context;
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
        ViewHolder holder = null;
        if (view == null) {
            holder = new ViewHolder();
            view = mInflater.inflate(R.layout.item_slot_machine_list, null);
            holder.machineNo = (TextView) view.findViewById(R.id.item_sm_machineno);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.machineNo.setText(smList.get(i).getMachineNo().toString());
        return view;
    }

    public class ViewHolder {
        public TextView machineNo;
    }
}
