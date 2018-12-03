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
 * Created by John on 12/11/2018.
 */

public class CarServiceAdapter extends BaseAdapter {
    private int[] serviceImgArr = {R.mipmap.car_service_wash, R.mipmap.car_service_deal, R.mipmap.car_service_maintain, R.mipmap.car_service_order, R.mipmap.car_service_license, R.mipmap.car_service_insurance1, R.mipmap.car_service_validate};
    private String[] serviceTitleArr = {"特價洗車", "汽車買賣", "汽車零件", "洗車訂單", "代辦駕照", "車險服務", "上門驗車"};
    private LayoutInflater inflater;
    public CarServiceAdapter(Context context) {
        inflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return serviceTitleArr.length;
    }

    @Override
    public Object getItem(int i) {
        return serviceTitleArr[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View convertView = view;
        CarServiceAdapter.ViewHolder holder = null;
        if (convertView == null) {
            holder = new CarServiceAdapter.ViewHolder();
            convertView = inflater.inflate(R.layout.item_car_service, null);
            holder.carServiceIv = convertView.findViewById(R.id.item_cs_iv);
            holder.carServiceTitleTv = convertView.findViewById(R.id.item_cs_title_tv);
            convertView.setTag(holder);
        } else {
            holder = (CarServiceAdapter.ViewHolder) convertView.getTag();
        }

        holder.carServiceIv.setImageResource(serviceImgArr[i]);
        holder.carServiceTitleTv.setText(serviceTitleArr[i]);
        return convertView;
    }

    class ViewHolder {
        public ImageView carServiceIv;
        public TextView carServiceTitleTv;
    }
}

