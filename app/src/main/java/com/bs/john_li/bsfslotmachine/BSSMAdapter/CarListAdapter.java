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

public class CarListAdapter extends BaseAdapter implements View.OnClickListener {
    private List<CarModel.CarCountAndListModel.CarInsideModel> carList;
    private LayoutInflater inflater;
    private Context mContext;
    private CarRechargeCallBack callBack;
    public  CarListAdapter(Context context, List<CarModel.CarCountAndListModel.CarInsideModel> carList, CarRechargeCallBack callBack) {
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
            convertView = inflater.inflate(R.layout.item_car_list, null);
            holder.carlistCb = convertView.findViewById(R.id.item_carlist_cb);
            holder.carlistIv = convertView.findViewById(R.id.item_carlist_iv);
            holder.carlistLicensenum = convertView.findViewById(R.id.item_carlist_licensenum);
            holder.carlistBrand = convertView.findViewById(R.id.item_carlist_brand);
            holder.carlistModel = convertView.findViewById(R.id.item_carlist_model);
            holder.carlistStyle = convertView.findViewById(R.id.item_carlist_style);
            holder.carTypeTv = convertView.findViewById(R.id.item_carlist_car_type);
            holder.carRecharge = convertView.findViewById(R.id.item_carlist_recharge);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.carlistLicensenum.setText("車牌號碼：" +carList.get(i).getCarNo());
        holder.carlistBrand.setText("車輛品牌：" + carList.get(i).getCarBrand());
        holder.carlistStyle.setText("車輛型號：" + carList.get(i).getCarStyle());
        holder.carlistModel.setText("車      型：" +carList.get(i).getModelForCar());
        switch (carList.get(i).getIfPerson()) {
            case 1:
            holder.carTypeTv.setText("車輛類型：" + "輕重型電單車");
            break;
            case 2:
                holder.carTypeTv.setText("車輛類型：" + "私家車");
                break;
            case 3:
                holder.carTypeTv.setText("車輛類型：" + "重型汽車");
                break;
        }

        if (carList.get(i).getIfPay() == 0) {
            holder.carRecharge.setImageResource(R.mipmap.recharge);
        } else {
            holder.carRecharge.setImageResource(R.mipmap.year);
        }

        holder.carRecharge.setOnClickListener(this);
        holder.carRecharge.setTag(String.valueOf(i));
        holder.carlistCb.setVisibility(View.GONE);
        holder.carRecharge.setVisibility(View.VISIBLE);
        return convertView;
    }

    @Override
    public void onClick(View view) {
        callBack.carRechargeClick(view);
    }

    class ViewHolder {
        public CheckBox carlistCb;
        public ImageView carlistIv;
        public TextView carlistLicensenum;
        public TextView carlistBrand;
        public TextView carlistModel;
        public TextView carlistStyle;
        public TextView carTypeTv;
        public ImageView carRecharge;
    }

    public void refreshListView(List<CarModel.CarCountAndListModel.CarInsideModel> newList){
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
