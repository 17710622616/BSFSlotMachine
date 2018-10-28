package com.bs.john_li.bsfslotmachine.BSSMActivity.Parking;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bs.john_li.bsfslotmachine.BSSMActivity.BaseActivity;
import com.bs.john_li.bsfslotmachine.BSSMActivity.LoginActivity;
import com.bs.john_li.bsfslotmachine.BSSMModel.CarModel;
import com.bs.john_li.bsfslotmachine.BSSMModel.RatesModel;
import com.bs.john_li.bsfslotmachine.BSSMModel.SlotMachineListOutsideModel;
import com.bs.john_li.bsfslotmachine.BSSMModel.SlotOrderModel;
import com.bs.john_li.bsfslotmachine.BSSMModel.SlotUnknowOrderModel;
import com.bs.john_li.bsfslotmachine.BSSMUtils.BSSMCommonUtils;
import com.bs.john_li.bsfslotmachine.BSSMUtils.BSSMConfigtor;
import com.bs.john_li.bsfslotmachine.BSSMUtils.SPUtils;
import com.bs.john_li.bsfslotmachine.BSSMView.BSSMHeadView;
import com.bs.john_li.bsfslotmachine.R;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by John_Li on 24/10/2018.
 */

public class ChooseOrderTimeActivity extends BaseActivity implements View.OnClickListener {
    private BSSMHeadView headView;
    private LinearLayout carManageLL;
    private TextView carTypeTv, carNoTv, amountTv;
    private ImageView carIv, carRechargeIv;
    private TimePicker startTimePicker, endTimePicker;
    private CheckBox tomorrowCb;

    private String startWay;
    private String childPosition;
    private CarModel.CarCountAndListModel.CarInsideModel mCarModel;
    private SlotMachineListOutsideModel.SlotMachineListModel.SlotMachineModel mSlotMachineModel;
    private SlotOrderModel mSlotOrderModel;//已知咪錶的訂單
    private SlotUnknowOrderModel mSlotUnknowOrderModel;  // 未知咪錶拍照時記得用saveinstans保存，完成之後還需把原來的數據擺回界面
    private RatesModel mRatesModel; //收費標準
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
        carManageLL = findViewById(R.id.choose_order_time_car_manage_ll);
        carTypeTv = findViewById(R.id.choose_order_time_car_type);
        carNoTv = findViewById(R.id.choose_order_time_car_num);
        carIv = findViewById(R.id.choose_order_time_parking_iv);
        carRechargeIv = findViewById(R.id.choose_order_time_car_recharge);
        amountTv = findViewById(R.id.choose_order_amount_tv);
        startTimePicker = findViewById(R.id.time_picker_start);
        endTimePicker = findViewById(R.id.time_picker_end);
        tomorrowCb = findViewById(R.id.choose_order_time_cb);
    }

    @Override
    public void setListener() {
        carManageLL.setOnClickListener(this);
        tomorrowCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    startTimePicker.setCurrentHour(9);
                    startTimePicker.setCurrentMinute(1);
                    endTimePicker.setCurrentHour(9);
                    endTimePicker.setCurrentMinute(30);
                } else {
                    startTimePicker.setCurrentHour(BSSMCommonUtils.getHour() + 1);
                    startTimePicker.setCurrentMinute(BSSMCommonUtils.getMinute());
                    endTimePicker.setCurrentHour(BSSMCommonUtils.getHour() + 2);
                    endTimePicker.setCurrentMinute(BSSMCommonUtils.getMinute());
                }
            }
        });
        startTimePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int hourOfDay, int minute) {
                boolean v = timePicker.is24HourView();
                if(tomorrowCb.isChecked()) {    // 选中，从第二天九点开始
                    if (hourOfDay > 20 || hourOfDay < 9) {
                        timePicker.setCurrentHour(9);
                        timePicker.setCurrentMinute(1);
                        Toast.makeText(ChooseOrderTimeActivity.this, "請選擇規定的時間(9:00-20:00)之間下單！", Toast.LENGTH_SHORT).show();
                    } else {
                        String hourTime = null;
                        String minuteTime = null;
                        if (hourOfDay < 10) {
                            hourTime = "0" + hourOfDay;
                        } else {
                            hourTime = Integer.toString(hourOfDay);
                        }
                        if (minute < 10) {
                            minuteTime = "0" + minute;
                        } else {
                            minuteTime = Integer.toString(minute);
                        }
                        String time = hourTime + ":" + minuteTime + ":00";
                        if (startWay.equals(BSSMConfigtor.SLOT_MACHINE_NOT_EXIST)){
                            //mSlotUnknowOrderModel.setStartSlotTime(yearFdt.format(date) + " " +time);
                        } else {
                            mSlotOrderModel.setStartSlotTime(BSSMCommonUtils.getTomorrowDate() + " " +time);
                        }
                    }
                } else {    // 未选中代表当天
                    if (hourOfDay < BSSMCommonUtils.getHour() + 1 || minute < BSSMCommonUtils.getMinute()) {
                        timePicker.setCurrentHour(BSSMCommonUtils.getHour() + 1);
                        timePicker.setCurrentMinute(BSSMCommonUtils.getMinute());
                        Toast.makeText(ChooseOrderTimeActivity.this, "請選擇一個小時之後的時間", Toast.LENGTH_SHORT).show();
                    } else {
                        if (hourOfDay > 20 || hourOfDay < 9) {
                            timePicker.setCurrentHour(BSSMCommonUtils.getHour() + 1);
                            timePicker.setCurrentMinute(BSSMCommonUtils.getMinute());
                            Toast.makeText(ChooseOrderTimeActivity.this, "請選擇規定的時間(9:00-20:00)之間下單！", Toast.LENGTH_SHORT).show();
                        } else {
                            String hourTime = null;
                            String minuteTime = null;
                            if (hourOfDay < 10) {
                                hourTime = "0" + hourOfDay;
                            } else {
                                hourTime = Integer.toString(hourOfDay);
                            }
                            if (minute < 10) {
                                minuteTime = "0" + minute;
                            } else {
                                minuteTime = Integer.toString(minute);
                            }
                            String time = hourTime + ":" + minuteTime + ":00";
                            Date date = new Date( );
                            SimpleDateFormat yearFdt = new SimpleDateFormat ("yyyy-MM-dd");
                            if (startWay.equals(BSSMConfigtor.SLOT_MACHINE_NOT_EXIST)){
                                //mSlotUnknowOrderModel.setStartSlotTime(yearFdt.format(date) + " " +time);
                            } else {
                                mSlotOrderModel.setStartSlotTime(yearFdt.format(date) + " " +time);
                            }
                        }
                    }
                }

                calculationOrderAmount();
            }
        });
        endTimePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onTimeChanged(TimePicker timePicker, int hourOfDay, int minute) {
                String hourTime = null;
                String minuteTime = null;
                if (hourOfDay < 10) {
                    hourTime = "0" + hourOfDay;
                } else {
                    hourTime = Integer.toString(hourOfDay);
                }
                if (minute < 10) {
                    minuteTime = "0" + minute;
                } else {
                    minuteTime = Integer.toString(minute);
                }
                String endSlotTime = "";
                String time = hourTime + ":" + minuteTime + ":00";
                if (tomorrowCb.isChecked()) {
                    endSlotTime = BSSMCommonUtils.getTomorrowDate();
                } else {
                    Date date = new Date( );
                    SimpleDateFormat yearFdt = new SimpleDateFormat ("yyyy-MM-dd");
                    endSlotTime = yearFdt.format(date);
                }
                try {
                    if (BSSMCommonUtils.compareTwoTime(endSlotTime + " " +time, mSlotOrderModel.getStartSlotTime())){  // 判斷不小於開始時間
                        timePicker.setCurrentHour(startTimePicker.getHour() + 1);
                        timePicker.setCurrentMinute(startTimePicker.getMinute());
                        Toast.makeText(ChooseOrderTimeActivity.this, "結束投幣時間不可小於開始投幣時間！", Toast.LENGTH_SHORT).show();
                    } else {
                        mSlotOrderModel.setEndSlotTime(endSlotTime + " " +time);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                    Toast.makeText(ChooseOrderTimeActivity.this, "時間格式錯誤", Toast.LENGTH_SHORT).show();
                }
                calculationOrderAmount();
            }
        });
    }

    /**
     * 計算訂單金額
     */
    private void calculationOrderAmount() {
        String startTime = mSlotOrderModel.getStartSlotTime();
        String endtTime = mSlotOrderModel.getEndSlotTime();

        if (mRatesModel != null) {
            double orderAmount = 0.0;
            double hourCost = new BigDecimal(mRatesModel.getData().getHourCost()).divide(new BigDecimal(60), 4, BigDecimal.ROUND_UP).doubleValue(); //
            double noVipHoursPay = new BigDecimal(mRatesModel.getData().getNoVipHoursPay()).divide(new BigDecimal(60), 4, BigDecimal.ROUND_UP).doubleValue();
            long timeDiff = BSSMCommonUtils.compareTimestamps(mSlotOrderModel.getStartSlotTime(), mSlotOrderModel.getEndSlotTime());

            if (mCarModel.getIfPay() == 0) {    // 非會員
                try {
                    if (BSSMCommonUtils.compareTwoTime(mSlotOrderModel.getStartSlotTime(), BSSMCommonUtils.getYear() + "-" + BSSMCommonUtils.getMonth() + "-" + BSSMCommonUtils.getDayOfMonth() + " 09:30:00")) {
                        orderAmount = Math.round((hourCost + noVipHoursPay) * (timeDiff + 60)*10000)/10000.0000;    // 小於早上九點半
                    } else {
                        orderAmount = Math.round((hourCost + noVipHoursPay) * timeDiff*10000)/10000.0000;    // 大於早上九點半
                    }
                    mSlotOrderModel.setSlotAmount(String.valueOf(orderAmount));
                    amountTv.setText("MOP" + String.valueOf(orderAmount));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else {    // 會員
                try {
                    String time930 = BSSMCommonUtils.getYear() + "-" + BSSMCommonUtils.getMonth() + "-" + BSSMCommonUtils.getDayOfMonth() + " 09:30:00";
                    if (BSSMCommonUtils.compareTwoTime(mSlotOrderModel.getStartSlotTime(), time930)) {
                        orderAmount = Math.round(hourCost * (timeDiff + 60)*10000)/10000.0000;// 小於早上九點半
                    } else {
                        orderAmount = Math.round(hourCost * timeDiff*10000)/10000.0000;     // 大於早上九點半
                    }
                    mSlotOrderModel.setSlotAmount(String.valueOf(orderAmount));
                    amountTv.setText("MOP" + String.valueOf(orderAmount));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
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
        mSlotMachineModel = new Gson().fromJson(intent.getStringExtra("SlotMachine"), SlotMachineListOutsideModel.SlotMachineListModel.SlotMachineModel.class);
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
        // 現在時間
        String startTime = (BSSMCommonUtils.getHour() + 1) + ":" + BSSMCommonUtils.getMinute() + ":00";
        String startTimeForDay = BSSMCommonUtils.getYear() + "-" + BSSMCommonUtils.getMonth() + "-" + BSSMCommonUtils.getDayOfMonth() + " " + startTime;
        String endTime = (BSSMCommonUtils.getHour() + 2) + ":" + BSSMCommonUtils.getMinute() + ":00";
        String endTimeForDay = BSSMCommonUtils.getYear() + "-" + BSSMCommonUtils.getMonth() + "-" + BSSMCommonUtils.getDayOfMonth() + " " + endTime;

        if (startWay.equals(BSSMConfigtor.SLOT_MACHINE_NOT_EXIST)) { // 咪錶不存在

        } else if (startWay.equals(BSSMConfigtor.SLOT_MACHINE_EXIST)){   //咪錶存在，定位停車

        } else {    // 咪錶存在，有子列表
            String childMachinePosition = intent.getStringExtra("childPosition");

            // 停車訂單
            mSlotOrderModel = new SlotOrderModel();
            mSlotOrderModel.setStartSlotTime(startTimeForDay);
            mSlotOrderModel.setEndSlotTime(endTimeForDay);
            mSlotOrderModel.setRemark("");
            mSlotOrderModel.setMachineNo(mSlotMachineModel.getMachineNo());
            mSlotOrderModel.setParkingSpace(childMachinePosition);
            mSlotOrderModel.setCarId((long) mCarModel.getId());
            mSlotOrderModel.setSlotAmount("0");
            if (BSSMCommonUtils.isLoginNow(ChooseOrderTimeActivity.this)) {
                // 獲取收費標準
                callNetGetRates();
            } else {
                startActivityForResult(new Intent(ChooseOrderTimeActivity.this, LoginActivity.class), BSSMConfigtor.LOGIN_FOR_RQUEST);
            }
        }

        // 初始化時間選擇器
        startTimePicker.setIs24HourView(true);
        startTimePicker.setCurrentHour(BSSMCommonUtils.getHour() + 1);
        startTimePicker.setCurrentMinute(BSSMCommonUtils.getMinute());
        endTimePicker.setIs24HourView(true);
        endTimePicker.setCurrentHour(BSSMCommonUtils.getHour() + 2);
        endTimePicker.setCurrentMinute(BSSMCommonUtils.getMinute());
    }

    /**
     * 獲取收費標準
     */
    private void callNetGetRates() {
        RequestParams params = new RequestParams(BSSMConfigtor.BASE_URL + BSSMConfigtor.GET_ORDER_RATES + SPUtils.get(this, "UserToken", ""));
        params.setAsJsonContent(true);
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("carType",mSlotMachineModel.getCarType());
            jsonObj.put("pillarColor",mSlotMachineModel.getPillarColor());
            jsonObj.put("areaCode",mSlotMachineModel.getAreaCode());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        params.setBodyContent(jsonObj.toString());
        String uri = params.getUri();
        params.setConnectTimeout(30 * 1000);
        x.http().request(HttpMethod.POST ,params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                mRatesModel = new Gson().fromJson(result.toString(), RatesModel.class);
                if (mRatesModel.getCode() == 200) {

                } else if (mRatesModel.getCode() == 10000) {
                    SPUtils.put(ChooseOrderTimeActivity.this, "UserToken", "");
                    startActivityForResult(new Intent(ChooseOrderTimeActivity.this, LoginActivity.class), BSSMConfigtor.LOGIN_FOR_RQUEST);
                } else {
                    Toast.makeText(ChooseOrderTimeActivity.this, "獲取收費標準失敗請重新提交", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(ChooseOrderTimeActivity.this, "獲取收費標準失敗請重新提交", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_left:
                finish();
                break;
            case R.id.head_right_tv:
                if (startWay.equals(BSSMConfigtor.SLOT_MACHINE_NOT_EXIST)) { // 咪錶不存在

                } else if (startWay.equals(BSSMConfigtor.SLOT_MACHINE_EXIST)){   //咪錶存在，定位停車

                } else {
                    if (mSlotOrderModel.getStartSlotTime() != null && mSlotOrderModel.getEndSlotTime() != null) {
                        Intent intent = new Intent(this, ParkingOrderActivity.class);
                        intent.putExtra("way", BSSMConfigtor.SLOT_MACHINE_FROM_SEARCH);
                        intent.putExtra("SlotMachine", getIntent().getStringExtra("SlotMachine"));
                        intent.putExtra("childPosition", getIntent().getStringExtra("childPosition"));
                        intent.putExtra("carModel", getIntent().getStringExtra("carModel"));
                        intent.putExtra("SlotOrder", new Gson().toJson(mSlotOrderModel));
                        intent.putExtra("isTomorrow", tomorrowCb.isChecked());
                        startActivity(intent);
                    } else {
                        Toast.makeText(this, "請選擇開始投幣時間及結束投幣時間！", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.choose_order_time_car_manage_ll:
                finish();
                break;
        }
    }
}
