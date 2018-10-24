package com.bs.john_li.bsfslotmachine.BSSMActivity.Parking;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.bs.john_li.bsfslotmachine.BSSMActivity.BaseActivity;
import com.bs.john_li.bsfslotmachine.BSSMModel.CarModel;
import com.bs.john_li.bsfslotmachine.BSSMModel.SlotMachineListOutsideModel;
import com.bs.john_li.bsfslotmachine.BSSMUtils.BSSMCommonUtils;
import com.bs.john_li.bsfslotmachine.BSSMView.BSSMHeadView;
import com.bs.john_li.bsfslotmachine.R;
import com.google.gson.Gson;

import org.xutils.image.ImageOptions;
import org.xutils.x;

/**
 * Created by John_Li on 24/10/2018.
 */

public class ChooseOrderTimeActivity extends BaseActivity implements View.OnClickListener {
    private BSSMHeadView headView;
    private LinearLayout carManageLL;
    private TextView carTypeTv, carNoTv;
    private ImageView carIv, carRechargeIv;
    private TimePicker startTimePicker, endTimePicker;
    private CheckBox tomorrowCb;

    private String startWay;
    private String childPosition;
    private CarModel.CarCountAndListModel.CarInsideModel mCarModel;
    private SlotMachineListOutsideModel.SlotMachineListModel.SlotMachineModel mSlotMachineModel;
    private ImageOptions options = new ImageOptions.Builder().setSize(0, 0).setLoadingDrawableId(R.mipmap.img_loading).setFailureDrawableId(R.mipmap.load_img_fail_list).build();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_order_time);
        initView();
        setListener();
        initData();
    }

    @Override
    public void initView() {
        headView = findViewById(R.id.choose_order_time_head);
        carManageLL = findViewById(R.id.choose_order_time_head);
        carTypeTv = findViewById(R.id.choose_order_time_car_type);
        carNoTv = findViewById(R.id.choose_order_time_car_num);
        carIv = findViewById(R.id.choose_order_time_parking_iv);
        carRechargeIv = findViewById(R.id.choose_order_time_car_recharge);
        startTimePicker = findViewById(R.id.time_picker_start);
        endTimePicker = findViewById(R.id.time_picker_end);
    }

    @Override
    public void setListener() {
        startTimePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int hourOfDay, int minute) {
            }
        });
        endTimePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int hourOfDay, int minute) {
            }
        });
    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        headView.setTitle("選擇訂單時間");
        headView.setLeft(this);
        headView.setRightText("下一步", this);

        // 獲取傳遞數據
        startWay = intent.getStringExtra("way");
        childPosition = intent.getStringExtra("childPosition");
        mCarModel = new Gson().fromJson(intent.getStringExtra("carModel"), CarModel.CarCountAndListModel.CarInsideModel.class);
        // 填充車輛信息
        x.image().bind(carIv, mCarModel.getImgUrl(), options);
        if (mCarModel.getIfPay() != 0) {
            carRechargeIv.setImageResource(R.mipmap.member);
        }
        carNoTv.setText(mCarModel.getCarNo());
        switch (mCarModel.getIfPerson()) {
            case 1:
                carTypeTv.setText("車輛類型：輕重型電單車");
                break;
            case 2:
                carTypeTv.setText("車輛類型：私家車");
                break;
            case 3:
                carTypeTv.setText("車輛類型：重型汽車");
                break;
        }

        // 初始化時間選擇器
        startTimePicker.setIs24HourView(true);
        startTimePicker.setCurrentHour(BSSMCommonUtils.getHour() + 1);
        startTimePicker.setCurrentMinute(BSSMCommonUtils.getMinute());
        endTimePicker.setIs24HourView(true);
        endTimePicker.setCurrentHour(BSSMCommonUtils.getHour() + 1);
        endTimePicker.setCurrentMinute(BSSMCommonUtils.getMinute());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_left:
                finish();
                break;
            case R.id.head_right_tv:
                break;
        }
    }
}
