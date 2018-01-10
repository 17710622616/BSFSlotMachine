package com.bs.john_li.bsfslotmachine.BSSMActivity.Mine;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

import com.bs.john_li.bsfslotmachine.BSSMActivity.BaseActivity;
import com.bs.john_li.bsfslotmachine.BSSMAdapter.OrderPhotoAdapter;
import com.bs.john_li.bsfslotmachine.BSSMModel.UserOrderOutModel;
import com.bs.john_li.bsfslotmachine.BSSMView.BSSMHeadView;
import com.bs.john_li.bsfslotmachine.BSSMView.NoScrollGridView;
import com.bs.john_li.bsfslotmachine.R;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * 訂單詳情
 * Created by John_Li on 10/1/2018.
 */

public class OrderDetialActivity extends BaseActivity implements View.OnClickListener{
    private BSSMHeadView headView;
    private NoScrollGridView orderImgGv;
    private TextView orderTypeTv, orderNoTv,statusTV,payMoneyTV,startTimeTV,timeTV,machineNoTV,colorTV,addressTV,carTypeTV,carNoTV,carStyleTV,carBrandTV,remarkTV,totalAmountTV,returnAmountTV,monthTV;

    private UserOrderOutModel.UserOrderInsideModel.UserOrderModel mUserOrderModel;
    private List<Integer> imgList;
    private OrderPhotoAdapter mOrderPhotoAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actvity_order_detial);
        mUserOrderModel = new Gson().fromJson(getIntent().getStringExtra("OrderModel"), UserOrderOutModel.UserOrderInsideModel.UserOrderModel.class);
        initView();
        setListener();
        initData();
    }

    @Override
    public void initView() {
        headView = findViewById(R.id.order_detial_head);
        orderImgGv = findViewById(R.id.order_detial_gv);
        orderNoTv = findViewById(R.id.order_detial_no);
        orderTypeTv = findViewById(R.id.order_detial_type);
        statusTV = findViewById(R.id.order_detial_status);
        payMoneyTV = findViewById(R.id.order_detial_pay_money);
        startTimeTV = findViewById(R.id.order_detial_starttime);
        timeTV = findViewById(R.id.order_detial_time);
        machineNoTV = findViewById(R.id.order_detial_machine_no);
        colorTV = findViewById(R.id.order_detial_color);
        addressTV = findViewById(R.id.order_detial_address);
        carTypeTV = findViewById(R.id.order_detial_car_type);
        carNoTV = findViewById(R.id.order_detial_car_no);
        carStyleTV = findViewById(R.id.order_detial_car_style);
        carBrandTV = findViewById(R.id.order_detial_car_brand);
        remarkTV = findViewById(R.id.order_detial_remark);
        totalAmountTV = findViewById(R.id.order_detial_total_amount);
        returnAmountTV = findViewById(R.id.order_detial_return_amount);
        monthTV = findViewById(R.id.order_detial_month);
    }

    @Override
    public void setListener() {

    }

    @Override
    public void initData() {
        headView.setTitle("訂單詳情");
        headView.setLeft(this);
        headView.setRight(R.mipmap.ok, this);

        imgList = new ArrayList<>();
        switch (mUserOrderModel.getOrderType()) {
            case 1: // 充值訂單
                imgList.add(R.mipmap.top_up);
                orderTypeTv.setText("訂單類型：充值訂單");
                break;
            case 2: // 會員續費訂單
                imgList.add(R.mipmap.renewal);
                orderTypeTv.setText("訂單類型：會員續費訂單");
                break;
            case 3: // 確定投幣機訂單
                imgList.add(R.mipmap.car_sample);
                orderTypeTv.setText("訂單類型：確定投幣機訂單");
                break;
            case 4: // 未知投幣機訂單
                imgList.add(R.mipmap.car_sample);
                imgList.add(R.mipmap.sure_order);
                imgList.add(R.mipmap.renewal);
                imgList.add(R.mipmap.top_up);
                imgList.add(R.mipmap.car_sample);
                orderTypeTv.setText("訂單類型：未知投幣機訂單");
                break;
        }

        mOrderPhotoAdapter = new OrderPhotoAdapter(this, imgList);
        orderImgGv.setAdapter(mOrderPhotoAdapter);
        orderNoTv.setText("訂單編號：" + mUserOrderModel.getOrderNo());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_left:
                finish();
                break;
            case R.id.head_right_tv:
                finish();
                break;
        }
    }
}
