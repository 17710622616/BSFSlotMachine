package com.bs.john_li.bsfslotmachine.BSSMAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.bs.john_li.bsfslotmachine.BSSMModel.CarModel;
import com.bs.john_li.bsfslotmachine.BSSMModel.CarRechargeWayListModel;
import com.bs.john_li.bsfslotmachine.R;

import java.util.List;

/**
 * Created by John_Li on 9/8/2017.
 */

public class CarRechargeWayListAdapter extends BaseAdapter implements View.OnClickListener {
    private List<CarRechargeWayListModel.CarRechargeWayModel> carList;
    private LayoutInflater inflater;
    private Context mContext;
    private CarRechargeCallBack callBack;
    public CarRechargeWayListAdapter(Context context, List<CarRechargeWayListModel.CarRechargeWayModel> carList, CarRechargeCallBack callBack) {
        this.carList = carList;
        this.mContext = context;
        inflater = LayoutInflater.from(context);
        this.callBack = callBack;
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
            convertView = inflater.inflate(R.layout.item_car_recharge_way, null);
            holder.rechargeWayRb = convertView.findViewById(R.id.item_car_recharge_tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        return convertView;
    }

    @Override
    public void onClick(View view) {
        callBack.carRechargeClick(view);
    }

    class ViewHolder {
        public RadioButton rechargeWayRb;
    }

    public void refreshListView(List<CarRechargeWayListModel.CarRechargeWayModel> newList){
        this.carList = newList;
        notifyDataSetChanged();
    }

    /**
     * 充值圖片點擊接口
     */
    public interface CarRechargeCallBack {
        void carRechargeClick(View view);
    }
}
