package com.bs.john_li.bsfslotmachine.BSSMActivity.CarService;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bs.john_li.bsfslotmachine.BSSMActivity.BaseActivity;
import com.bs.john_li.bsfslotmachine.BSSMModel.CWUserOrderOutModel;
import com.bs.john_li.bsfslotmachine.BSSMView.BSSMHeadView;
import com.bs.john_li.bsfslotmachine.R;

/**
 * Created by John_Li on 16/11/2018.
 */

public class CarWashOrderDetialActivity extends BaseActivity implements View.OnClickListener{
    private BSSMHeadView headView;
    private ImageView setIv, qrcodeIv;
    private TextView setName, businessHoursTv, soldTv;

    private CWUserOrderOutModel.DataBeanX.CWUserOrderModel mCWUserOrderModel;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_wash_order_detial);
        initView();
        setListener();
        initData();
    }

    @Override
    public void initView() {
        headView = findViewById(R.id.car_wash_order_detial_head);
        setIv = findViewById(R.id.car_wash_set_iv);
        qrcodeIv = findViewById(R.id.car_wash_set_iv);
        setName = findViewById(R.id.car_wash_set_name_tv);
        businessHoursTv = findViewById(R.id.car_wash_set_business_hours_tv);
        soldTv = findViewById(R.id.car_wash_set_sold_tv);
    }

    @Override
    public void setListener() {

    }

    @Override
    public void initData() {
        headView.setLeft(this);
        headView.setTitle("洗車訂單");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_left:
                break;
        }
    }
}
