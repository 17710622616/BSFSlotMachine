package com.bs.john_li.bsfslotmachine.BSSMAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bs.john_li.bsfslotmachine.BSSMModel.SlotMachineListOutsideModel;
import com.bs.john_li.bsfslotmachine.R;

import java.util.List;

/**
 * Created by John_Li on 2/12/2017.
 */

public class SearchSlotMchineListAdapter extends RecyclerView.Adapter implements View.OnClickListener {
    private List<SlotMachineListOutsideModel.SlotMachineListModel.SlotMachineModel> smList;
    private LayoutInflater inflater;
    private OnItemClickListener mOnitemClickListener;
    private Context mContext;

    public SearchSlotMchineListAdapter(List<SlotMachineListOutsideModel.SlotMachineListModel.SlotMachineModel> smList, Context context) {
        this.smList = smList;
        this.mContext = context;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_search_slot_machine_list, parent, false);
        SearchSlotMchineListAdapter.SlotMachineListViewHolder vh = new SearchSlotMchineListAdapter.SlotMachineListViewHolder(view);
        view.setOnClickListener(this);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((SearchSlotMchineListAdapter.SlotMachineListViewHolder)holder).item_search_sm_list_no.setText("咪錶編號：" + smList.get(position).getMachineNo());
        ((SearchSlotMchineListAdapter.SlotMachineListViewHolder)holder).item_search_sm_list_address.setText("咪錶地址：" + smList.get(position).getAddress());
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return smList.size();
    }

    @Override
    public void onClick(View v) {
        if (mOnitemClickListener != null) {
            mOnitemClickListener.onItemClick(v, (int)v.getTag());
        }
    }

    public class SlotMachineListViewHolder extends RecyclerView.ViewHolder {
        public TextView item_search_sm_list_no;
        public TextView item_search_sm_list_address;
        public SlotMachineListViewHolder(View view){
            super(view);
            item_search_sm_list_no = (TextView) view.findViewById(R.id.item_search_sm_list_no);
            item_search_sm_list_address = (TextView) view.findViewById(R.id.item_search_sm_list_address);
        }
    }

    public void setOnItemClickListenr(SearchSlotMchineListAdapter.OnItemClickListener listenr) {
        this.mOnitemClickListener = listenr;
    }

    public static interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
}
