package com.bs.john_li.bsfslotmachine.BSSMAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bs.john_li.bsfslotmachine.BSSMModel.CarModel;
import com.bs.john_li.bsfslotmachine.R;

import java.util.List;

/**
 * Created by John_Li on 9/8/2017.
 */

public class CarListAdapter extends BaseAdapter {
    private List<CarModel.CarCountAndListModel.CarInsideModel> carList;
    private LayoutInflater inflater;
    private Context mContext;
    public  CarListAdapter(Context context, List<CarModel.CarCountAndListModel.CarInsideModel> carList) {
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
            holder.carlistIv = convertView.findViewById(R.id.item_carlist_iv);
            holder.carlistLicensenum = convertView.findViewById(R.id.item_carlist_licensenum);
            holder.carlistBrand = convertView.findViewById(R.id.item_carlist_brand);
            holder.carlistModel = convertView.findViewById(R.id.item_carlist_model);
            holder.carlistStyle = convertView.findViewById(R.id.item_carlist_style);
            holder.carTypeTv = convertView.findViewById(R.id.item_carlist_car_type);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.carlistLicensenum.setText(carList.get(i).getCarNo());
        holder.carlistBrand.setText(carList.get(i).getCarBrand());
        holder.carlistStyle.setText(carList.get(i).getCarStyle());
        holder.carlistModel.setText(carList.get(i).getModelForCar());
        switch (carList.get(i).getIfPerson()) {
            case 0:
                holder.carTypeTv.setText("私家車");
                break;
            case 1:
                holder.carTypeTv.setText("電單車");
                break;
        }
        return convertView;
    }

    class ViewHolder {
        public ImageView carlistIv;
        public TextView carlistLicensenum;
        public TextView carlistBrand;
        public TextView carlistModel;
        public TextView carlistStyle;
        public TextView carTypeTv;
    }
}
