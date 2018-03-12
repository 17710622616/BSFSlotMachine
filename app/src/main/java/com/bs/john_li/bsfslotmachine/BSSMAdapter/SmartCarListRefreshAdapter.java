package com.bs.john_li.bsfslotmachine.BSSMAdapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.GetObjectRequest;
import com.alibaba.sdk.android.oss.model.GetObjectResult;
import com.bs.john_li.bsfslotmachine.BSSMActivity.Mine.AddCarActivity;
import com.bs.john_li.bsfslotmachine.BSSMActivity.Mine.CarListActivity;
import com.bs.john_li.bsfslotmachine.BSSMModel.CarModel;
import com.bs.john_li.bsfslotmachine.BSSMModel.ContentsListModel;
import com.bs.john_li.bsfslotmachine.BSSMUtils.AliyunOSSUtils;
import com.bs.john_li.bsfslotmachine.BSSMUtils.BSSMCommonUtils;
import com.bs.john_li.bsfslotmachine.BSSMUtils.BSSMConfigtor;
import com.bs.john_li.bsfslotmachine.BSSMUtils.ProgressInputStream;
import com.bs.john_li.bsfslotmachine.R;

import java.io.InputStream;
import java.util.List;

/**
 * 车辆列表刷新
 * Created by John_Li on 28/11/2017.
 */

public class SmartCarListRefreshAdapter extends RecyclerView.Adapter implements View.OnClickListener,View.OnLongClickListener {
    private List<CarModel.CarCountAndListModel.CarInsideModel> carList;
    private final Context mContext;
    private OSSClient oss;
    private OnItemLongClickListener mOnItemLongClickListener = null;
    private CarRechargeCallBack mCarRechargeCallBack = null;
    private CarUpdateCallBack mCarUpdateCallBack = null;

    public SmartCarListRefreshAdapter(Context context, List<CarModel.CarCountAndListModel.CarInsideModel> list, OSSClient oss) {
        this.carList = list;
        this.oss = oss;
        mContext = context;
        mCarRechargeCallBack = (CarListActivity)mContext;
        mCarUpdateCallBack = (CarListActivity)mContext;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_car_list, parent, false);
        SmartRefreshViewHolder vh = new SmartRefreshViewHolder(view);
        view.setOnLongClickListener(this);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((SmartRefreshViewHolder)holder).carlistLicensenum.setText("車牌號碼：" +carList.get(position).getCarNo());
        ((SmartRefreshViewHolder)holder).carlistBrand.setText("車輛品牌：" + carList.get(position).getCarBrand());
        ((SmartRefreshViewHolder)holder).carlistStyle.setText("車輛型號：" + carList.get(position).getCarStyle());
        ((SmartRefreshViewHolder)holder).carlistModel.setText("車      型：" +carList.get(position).getModelForCar());
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
            ((SmartRefreshViewHolder)holder).carRecharge.setImageResource(R.mipmap.recharge);
        } else {
            ((SmartRefreshViewHolder)holder).carRecharge.setImageResource(R.mipmap.year);
        }

        ((SmartRefreshViewHolder)holder).carlistIv.setTag(carList.get(position).getImgUrl());
        //downloadImg(((SmartRefreshViewHolder)holder).carlistIv, carList.get(position).getImgUrl());
        AliyunOSSUtils.downloadImg(carList.get(position).getImgUrl(), oss, ((SmartRefreshViewHolder)holder).carlistIv, mContext);

        ((SmartRefreshViewHolder)holder).carRecharge.setOnClickListener(this);
        ((SmartRefreshViewHolder)holder).carListLL.setOnClickListener(this);
        ((SmartRefreshViewHolder)holder).carRecharge.setTag(String.valueOf(position));
        ((SmartRefreshViewHolder)holder).carListLL.setTag(String.valueOf(position));
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
        switch (v.getId()) {
            case R.id.item_carlist_recharge:
                mCarRechargeCallBack.carRechargeClick(v);
                break;
            case R.id.item_carlist_ll:
                mCarUpdateCallBack.carUpateClick(v);
                break;
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if (mOnItemLongClickListener != null) {
            mOnItemLongClickListener.onItemLongClick(v, (int)v.getTag());
        }
        return true;
    }

    public class SmartRefreshViewHolder extends RecyclerView.ViewHolder {
        public CheckBox carlistCb;
        public ImageView carlistIv;
        public TextView carlistLicensenum;
        public TextView carlistBrand;
        public TextView carlistModel;
        public TextView carlistStyle;
        public TextView carTypeTv;
        public ImageView carRecharge;
        public LinearLayout carListLL;
        public SmartRefreshViewHolder(View view){
            super(view);
            carlistCb = (CheckBox) view.findViewById(R.id.item_carlist_cb);
            carlistIv = (ImageView) view.findViewById(R.id.item_carlist_iv);
            carlistLicensenum = (TextView) view.findViewById(R.id.item_carlist_licensenum);
            carlistBrand = (TextView) view.findViewById(R.id.item_carlist_brand);
            carlistModel = (TextView) view.findViewById(R.id.item_carlist_model);
            carlistStyle = (TextView) view.findViewById(R.id.item_carlist_style);
            carTypeTv = (TextView) view.findViewById(R.id.item_carlist_car_type);
            carRecharge = (ImageView) view.findViewById(R.id.item_carlist_recharge);
            carListLL = view.findViewById(R.id.item_carlist_ll);
        }
    }

    public void setOnItemLongClickListenr(OnItemLongClickListener listenr) {
        this.mOnItemLongClickListener = listenr;
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(View view, int position);
    }
    /**
     * 充值圖片點擊接口
     */
    public interface CarRechargeCallBack {
        void carRechargeClick(View view);
    }
    /**
     * 充值圖片點擊接口
     */
    public interface CarUpdateCallBack {
        void carUpateClick(View view);
    }

    public void refreshListView(List<CarModel.CarCountAndListModel.CarInsideModel> newList){
        this.carList = newList;
        notifyDataSetChanged();
    }
}
