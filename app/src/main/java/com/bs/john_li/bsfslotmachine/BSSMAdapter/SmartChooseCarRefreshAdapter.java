package com.bs.john_li.bsfslotmachine.BSSMAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.sdk.android.oss.OSSClient;
import com.bs.john_li.bsfslotmachine.BSSMModel.CarModel;
import com.bs.john_li.bsfslotmachine.BSSMUtils.AliyunOSSUtils;
import com.bs.john_li.bsfslotmachine.R;

import java.util.List;

/**
 * 選擇車輛适配器
 * Created by John_Li on 28/11/2017.
 */

public class SmartChooseCarRefreshAdapter extends RecyclerView.Adapter implements View.OnClickListener {
    private List<CarModel.CarCountAndListModel.CarInsideModel> carList;
    private final Context mContext;
    private OSSClient oss;
    private OnItemClickListener mOnitemClickListener = null;

    public SmartChooseCarRefreshAdapter(Context context, List<CarModel.CarCountAndListModel.CarInsideModel> list, OSSClient oss) {
        this.carList = list;
        mContext = context;
        this.oss = oss;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_car_list, parent, false);
        SmartRefreshViewHolder vh = new SmartRefreshViewHolder(view);
        view.setOnClickListener(this);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((SmartRefreshViewHolder)holder).carlistLicensenum.setText("車牌：" + carList.get(position).getCarNo());
        ((SmartRefreshViewHolder)holder).carlistBrand.setText("品牌：" + carList.get(position).getCarBrand());
        ((SmartRefreshViewHolder)holder).carlistStyle.setText("款式：" + carList.get(position).getCarStyle());
        ((SmartRefreshViewHolder)holder).carlistModel.setText("型號：" + carList.get(position).getModelForCar());
        AliyunOSSUtils.downloadImg(carList.get(position).getImgUrl(), oss, ((SmartRefreshViewHolder)holder).carlistIv, mContext);
        switch (carList.get(position).getIfPerson()) {
            case 1:
                ((SmartRefreshViewHolder)holder).carTypeTv.setText("車輛類型：" + "輕重型電單車");
                break;
            case 2:
                ((SmartRefreshViewHolder)holder).carTypeTv.setText("車輛類型：" + "私家車");
                break;
            case 3:
                ((SmartRefreshViewHolder)holder).carTypeTv.setText("車輛類型：" + "重型汽車");
                break;
        }

        if (carList.get(position).getIfPay() == 0) {
            ((SmartRefreshViewHolder)holder).notRechrageTv.setVisibility(View.VISIBLE);
            ((SmartRefreshViewHolder)holder).carlistCb.setEnabled(false);
            ((SmartRefreshViewHolder)holder).carRecharge.setImageResource(R.mipmap.recharge);
        } else {
            ((SmartRefreshViewHolder)holder).notRechrageTv.setVisibility(View.GONE);
            ((SmartRefreshViewHolder)holder).carlistCb.setEnabled(true);
            ((SmartRefreshViewHolder)holder).carRecharge.setImageResource(R.mipmap.year);
        }

        ((SmartRefreshViewHolder)holder).carlistCb.setVisibility(View.GONE);
        ((SmartRefreshViewHolder)holder).carRecharge.setVisibility(View.VISIBLE);
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return carList.size();
    }

    @Override
    public void onClick(View v) {
        if (mOnitemClickListener != null) {
            mOnitemClickListener.onItemClick(v, (int)v.getTag());
        }
    }

    public class SmartRefreshViewHolder extends RecyclerView.ViewHolder {
        public CheckBox carlistCb;
        public ImageView carlistIv;
        public TextView carlistLicensenum;
        public TextView carlistBrand;
        public TextView carlistModel;
        public TextView carlistStyle;
        public TextView carTypeTv;
        public TextView notRechrageTv;
        public ImageView carRecharge;

        public SmartRefreshViewHolder(View view){
            super(view);
            carlistCb = (CheckBox) view.findViewById(R.id.item_carlist_cb);
            carlistIv = (ImageView) view.findViewById(R.id.item_carlist_iv);
            carlistLicensenum = (TextView) view.findViewById(R.id.item_carlist_licensenum);
            carlistBrand = (TextView) view.findViewById(R.id.item_carlist_brand);
            carlistModel = (TextView) view.findViewById(R.id.item_carlist_model);
            carlistStyle = (TextView) view.findViewById(R.id.item_carlist_style);
            carTypeTv = (TextView) view.findViewById(R.id.item_carlist_car_type);
            notRechrageTv = view.findViewById(R.id.item_carlist_not_rechrage_tv);
            carRecharge = (ImageView) view.findViewById(R.id.item_carlist_recharge);
        }
    }

    public void setOnItemClickListenr(OnItemClickListener listenr) {
        this.mOnitemClickListener = listenr;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
}
