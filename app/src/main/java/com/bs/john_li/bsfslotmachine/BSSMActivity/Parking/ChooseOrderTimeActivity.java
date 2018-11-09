package com.bs.john_li.bsfslotmachine.BSSMActivity.Parking;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.text.format.DateFormat;
import android.util.Log;
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
import com.bs.john_li.bsfslotmachine.BSSMInterface.EndTimeDataCallBack;
import com.bs.john_li.bsfslotmachine.BSSMInterface.StartTimeDataCallBack;
import com.bs.john_li.bsfslotmachine.BSSMModel.CarModel;
import com.bs.john_li.bsfslotmachine.BSSMModel.RatesModel;
import com.bs.john_li.bsfslotmachine.BSSMModel.SlotMachineListOutsideModel;
import com.bs.john_li.bsfslotmachine.BSSMModel.SlotOrderModel;
import com.bs.john_li.bsfslotmachine.BSSMModel.SlotUnknowOrderModel;
import com.bs.john_li.bsfslotmachine.BSSMUtils.BSSMCommonUtils;
import com.bs.john_li.bsfslotmachine.BSSMUtils.BSSMConfigtor;
import com.bs.john_li.bsfslotmachine.BSSMUtils.PhotoUtils;
import com.bs.john_li.bsfslotmachine.BSSMUtils.SPUtils;
import com.bs.john_li.bsfslotmachine.BSSMView.BSSMHeadView;
import com.bs.john_li.bsfslotmachine.R;
import com.google.gson.Gson;
import com.othershe.nicedialog.BaseNiceDialog;
import com.othershe.nicedialog.NiceDialog;
import com.othershe.nicedialog.ViewConvertListener;
import com.othershe.nicedialog.ViewHolder;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.io.File;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by John_Li on 24/10/2018.
 */

public class ChooseOrderTimeActivity extends BaseActivity implements View.OnClickListener, StartTimeDataCallBack , EndTimeDataCallBack{
    private BSSMHeadView headView;
    private LinearLayout carManageLL;
    private TextView carTypeTv, carNoTv, amountTv,serviceChargeTv;
    private ImageView carIv, carRechargeIv;
    //private TimePicker startTimePicker, endTimePicker;
    private TextView startTimeTv, endTimeTv;
    private CheckBox tomorrowCb;
    private StartTimePickerFragment mStartTimePicker;
    private EndTimePickerFragment mEndTimePicker;

    private File fileUri;//照片文件路徑
    private Uri imageUri;//照片文件路徑
    private static final int CODE_GALLERY_REQUEST = 2;   //0xa0
    private static final int CODE_CAMERA_REQUEST = 1;    //0xa1
    private static final int CAMERA_PERMISSIONS_REQUEST_CODE = 0x03;
    private static final int STORAGE_PERMISSIONS_REQUEST_CODE = 0x04;
    private File dir; //圖片文件夾路徑

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
        serviceChargeTv = findViewById(R.id.choose_order_service_charge_tv);
        /*startTimePicker = findViewById(R.id.time_picker_start);
        endTimePicker = findViewById(R.id.time_picker_end);*/
        startTimeTv = findViewById(R.id.time_start_tv);
        endTimeTv = findViewById(R.id.time_end_tv);
        tomorrowCb = findViewById(R.id.choose_order_time_cb);
    }

    @Override
    public void setListener() {
        carManageLL.setOnClickListener(this);
        startTimeTv.setOnClickListener(this);
        endTimeTv.setOnClickListener(this);
        tomorrowCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (startWay.equals(BSSMConfigtor.SLOT_MACHINE_FROM_SEARCH)) {
                        mSlotOrderModel.setStartSlotTime(BSSMCommonUtils.getTomorrowDate() + " " + mSlotOrderModel.getStartSlotTime().substring(11, 19));
                        mSlotOrderModel.setEndSlotTime(BSSMCommonUtils.getTomorrowDate() + " " + mSlotOrderModel.getEndSlotTime().substring(11, 19));
                        //Toast.makeText(ChooseOrderTimeActivity.this, "开始时间：" + mSlotOrderModel.getStartSlotTime() + ",结束时间：" + mSlotOrderModel.getEndSlotTime(), Toast.LENGTH_SHORT).show();
                    } else {
                        mSlotUnknowOrderModel.setStartSlotTime(BSSMCommonUtils.getTomorrowDate() + " " + mSlotUnknowOrderModel.getStartSlotTime().substring(11, 19));
                        mSlotUnknowOrderModel.setEndSlotTime(BSSMCommonUtils.getTomorrowDate() + " " + mSlotUnknowOrderModel.getEndSlotTime().substring(11, 19));
                        //Toast.makeText(ChooseOrderTimeActivity.this, "开始时间：" + mSlotUnknowOrderModel.getStartSlotTime() + ",结束时间：" + mSlotUnknowOrderModel.getEndSlotTime(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if (startWay.equals(BSSMConfigtor.SLOT_MACHINE_FROM_SEARCH)) {
                        mSlotOrderModel.setStartSlotTime(BSSMCommonUtils.getTodayDate() + " " + mSlotOrderModel.getStartSlotTime().substring(11, 19));
                        mSlotOrderModel.setEndSlotTime(BSSMCommonUtils.getTodayDate() + " " + mSlotOrderModel.getEndSlotTime().substring(11, 19));
                        //Toast.makeText(ChooseOrderTimeActivity.this, "开始时间：" + mSlotOrderModel.getStartSlotTime() + ",结束时间：" + mSlotOrderModel.getEndSlotTime(), Toast.LENGTH_SHORT).show();
                    } else {
                        mSlotUnknowOrderModel.setStartSlotTime(BSSMCommonUtils.getTodayDate() + " " + mSlotUnknowOrderModel.getStartSlotTime().substring(11, 19));
                        mSlotUnknowOrderModel.setEndSlotTime(BSSMCommonUtils.getTodayDate() + " " + mSlotUnknowOrderModel.getEndSlotTime().substring(11, 19));
                        //Toast.makeText(ChooseOrderTimeActivity.this, "开始时间：" + mSlotUnknowOrderModel.getStartSlotTime() + ",结束时间：" + mSlotUnknowOrderModel.getEndSlotTime(), Toast.LENGTH_SHORT).show();
                    }
                }

                calculationOrderAmount();
            }
        });
        /*startTimePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int hourOfDay, int minute) {
                boolean v = timePicker.is24HourView();
                try {
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
                                mSlotUnknowOrderModel.setStartSlotTime(BSSMCommonUtils.getTomorrowDate() + " " +time);
                                if (BSSMCommonUtils.compareTwoTime(mSlotUnknowOrderModel.getEndSlotTime(), mSlotUnknowOrderModel.getStartSlotTime())) {
                                    endTimePicker.setCurrentHour(startTimePicker.getCurrentHour() + 1);
                                    endTimePicker.setCurrentMinute(startTimePicker.getCurrentMinute());
                                }
                            } else {
                                mSlotOrderModel.setStartSlotTime(BSSMCommonUtils.getTomorrowDate() + " " +time);
                                if (BSSMCommonUtils.compareTwoTime(mSlotOrderModel.getEndSlotTime(), mSlotOrderModel.getStartSlotTime())) {
                                    endTimePicker.setCurrentHour(startTimePicker.getCurrentHour() + 1);
                                    endTimePicker.setCurrentMinute(startTimePicker.getCurrentMinute());
                                }
                            }
                        }
                    } else {    // 未选中代表当天
                        if (hourOfDay < BSSMCommonUtils.getHour() + 1 || minute < BSSMCommonUtils.getMinute()) {    //BSSMCommonUtils.compareTwoTime(timePicker.get, new Date(new Date().getTime() - 1 * 60 * 60 * 1000))
                            if(hourOfDay < 20){
                                timePicker.setCurrentHour(BSSMCommonUtils.getHour() + 1);
                                timePicker.setCurrentMinute(BSSMCommonUtils.getMinute());
                                Toast.makeText(ChooseOrderTimeActivity.this, "請選擇一個小時之後的時間", Toast.LENGTH_SHORT).show();
                            } else {

                            }
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
                                if (startWay.equals(BSSMConfigtor.SLOT_MACHINE_NOT_EXIST)){
                                    mSlotUnknowOrderModel.setStartSlotTime(BSSMCommonUtils.getTodayDate() + " " +time);
                                    if (BSSMCommonUtils.compareTwoTime(mSlotUnknowOrderModel.getEndSlotTime(), mSlotUnknowOrderModel.getStartSlotTime())) {
                                        endTimePicker.setCurrentHour(startTimePicker.getCurrentHour() + 1);
                                        endTimePicker.setCurrentMinute(startTimePicker.getCurrentMinute());
                                    }
                                } else {
                                    mSlotOrderModel.setStartSlotTime(BSSMCommonUtils.getTodayDate() + " " +time);
                                    if (BSSMCommonUtils.compareTwoTime(mSlotOrderModel.getEndSlotTime(), mSlotOrderModel.getStartSlotTime())) {
                                        endTimePicker.setCurrentHour(startTimePicker.getCurrentHour() + 1);
                                        endTimePicker.setCurrentMinute(startTimePicker.getCurrentMinute());
                                    }
                                }
                            }
                        }
                    }

                    calculationOrderAmount();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
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
                    endSlotTime = BSSMCommonUtils.getTodayDate();
                }
                try {
                    if (startWay.equals(BSSMConfigtor.SLOT_MACHINE_NOT_EXIST)){
                        if (BSSMCommonUtils.compareTwoTime(endSlotTime + " " +time, mSlotUnknowOrderModel.getStartSlotTime())){  // 判斷不小於開始時間
                            timePicker.setCurrentHour(startTimePicker.getCurrentHour() + 1);
                            timePicker.setCurrentMinute(startTimePicker.getCurrentMinute());
                            Toast.makeText(ChooseOrderTimeActivity.this, "結束投幣時間不可小於開始投幣時間！", Toast.LENGTH_SHORT).show();
                        } else {
                            mSlotUnknowOrderModel.setEndSlotTime(endSlotTime + " " +time);
                        }
                    } else {
                        if (BSSMCommonUtils.compareTwoTime(endSlotTime + " " +time, mSlotOrderModel.getStartSlotTime())){  // 判斷不小於開始時間
                            timePicker.setCurrentHour(startTimePicker.getCurrentHour() + 1);
                            timePicker.setCurrentMinute(startTimePicker.getCurrentMinute());
                            Toast.makeText(ChooseOrderTimeActivity.this, "結束投幣時間不可小於開始投幣時間！", Toast.LENGTH_SHORT).show();
                        } else {
                            mSlotOrderModel.setEndSlotTime(endSlotTime + " " +time);
                        }
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                    Toast.makeText(ChooseOrderTimeActivity.this, "時間格式錯誤", Toast.LENGTH_SHORT).show();
                }
                calculationOrderAmount();
            }
        });*/
    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        headView.setTitle("選擇訂單時間");
        headView.setLeft(this);

        // 獲取傳遞數據
        startWay = intent.getStringExtra("way");
        mCarModel = new Gson().fromJson(intent.getStringExtra("carModel"), CarModel.CarCountAndListModel.CarInsideModel.class);
        // 填充車輛信息
        x.image().bind(carIv, mCarModel.getImgUrl(), options);
        if (mCarModel.getVipType() == 0) {
            if (mCarModel.getIfPay() == 0) {
                carRechargeIv.setImageResource(R.mipmap.recharge);
            } else {
                carRechargeIv.setImageResource(R.mipmap.member);
            }
        } else if (mCarModel.getVipType() == 1) {   // 日費費
            carRechargeIv.setImageResource(R.mipmap.member);
        } else if (mCarModel.getVipType() == 2) {   // 月費
            carRechargeIv.setImageResource(R.mipmap.recharge_mouth);
        } else if (mCarModel.getVipType() == 3) {   // 季度費
            carRechargeIv.setImageResource(R.mipmap.recharge_quarter);
        } else if (mCarModel.getVipType() == 4) {   // 半年費
            carRechargeIv.setImageResource(R.mipmap.recharge_halfyear);
        } else if (mCarModel.getVipType() == 5) {   // 年費
            carRechargeIv.setImageResource(R.mipmap.year);
        } else {
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
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar nowTime = Calendar.getInstance();
        nowTime.add(Calendar.MINUTE, 60);//60分钟后的时间
        String startTimeForDay = sdf.format(nowTime.getTime());
        Calendar nowTime2 = Calendar.getInstance();
        nowTime2.add(Calendar.MINUTE, 120);//120分钟后的时间
        String endTimeForDay =  sdf.format(nowTime2.getTime());

        if (startWay.equals(BSSMConfigtor.SLOT_MACHINE_NOT_EXIST)) { // 咪錶不存在
            headView.setRightText("拍照", this);
            mSlotUnknowOrderModel = new Gson().fromJson(intent.getStringExtra("unknowSlotOrder"), SlotUnknowOrderModel.class);
            mSlotUnknowOrderModel.setSlotAmount("0");
            mSlotUnknowOrderModel.setStartSlotTime(startTimeForDay);
            mSlotUnknowOrderModel.setEndSlotTime(endTimeForDay);
            mSlotUnknowOrderModel.setCarId(String.valueOf(mCarModel.getId()));
            mSlotUnknowOrderModel.setCarType(mCarModel.getIfPerson());

            mStartTimePicker = new StartTimePickerFragment(mSlotUnknowOrderModel.getStartSlotTime());
            mEndTimePicker = new EndTimePickerFragment(mSlotUnknowOrderModel.getEndSlotTime());
            startTimeTv.setText(mSlotOrderModel.getStartSlotTime().substring(11,19));
            endTimeTv.setText(mSlotOrderModel.getEndSlotTime().substring(11,19));
        } else if (startWay.equals(BSSMConfigtor.SLOT_MACHINE_EXIST)){   //咪錶存在，唔子列表

        } else {    // 咪錶存在，有子列表
            headView.setRightText("下一步", this);
            // 子車位的數字下標
            childPosition = intent.getStringExtra("childPosition");
            // 咪錶
            mSlotMachineModel = new Gson().fromJson(intent.getStringExtra("SlotMachine"), SlotMachineListOutsideModel.SlotMachineListModel.SlotMachineModel.class);
            // 停車訂單
            mSlotOrderModel = new SlotOrderModel();
            mSlotOrderModel.setSlotAmount("0");
            mSlotOrderModel.setStartSlotTime(startTimeForDay);
            mSlotOrderModel.setEndSlotTime(endTimeForDay);
            mSlotOrderModel.setRemark("");
            mSlotOrderModel.setMachineNo(mSlotMachineModel.getMachineNo());
            mSlotOrderModel.setParkingSpace(mSlotMachineModel.getParkingSpaces().get(Integer.parseInt(childPosition)));
            mSlotOrderModel.setCarId((long) mCarModel.getId());

            mStartTimePicker = new StartTimePickerFragment(mSlotOrderModel.getStartSlotTime());
            mEndTimePicker = new EndTimePickerFragment(mSlotOrderModel.getEndSlotTime());
            startTimeTv.setText(mSlotOrderModel.getStartSlotTime().substring(11,19));
            endTimeTv.setText(mSlotOrderModel.getEndSlotTime().substring(11,19));
        }

        if (BSSMCommonUtils.isLoginNow(ChooseOrderTimeActivity.this)) {
            // 獲取收費標準
            callNetGetRates();
        } else {
            startActivityForResult(new Intent(ChooseOrderTimeActivity.this, LoginActivity.class), BSSMConfigtor.LOGIN_FOR_RQUEST);
        }

        dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "BSSMPictures");
        if (!dir.exists()) {
            dir.mkdir();
        }
    }


    /**
     * 計算訂單金額
     */
    private void calculationOrderAmount() {
        if (mRatesModel != null) {
            double orderAmount = 0.0;
            double hourCost = new BigDecimal(mRatesModel.getData().getHourCost()).divide(new BigDecimal(60), 4, BigDecimal.ROUND_UP).doubleValue(); //
            double noVipHoursPay = new BigDecimal(mRatesModel.getData().getNoVipHoursPay()).divide(new BigDecimal(60), 4, BigDecimal.ROUND_UP).doubleValue();

            if (startWay.equals(BSSMConfigtor.SLOT_MACHINE_NOT_EXIST)){
                // 未知咪錶
                long timeDiff = BSSMCommonUtils.compareTimestamps(mSlotUnknowOrderModel.getStartSlotTime(), mSlotUnknowOrderModel.getEndSlotTime());
                if (mCarModel.getIfPay() == 0) {    // 非會員
                    try {
                        if (BSSMCommonUtils.compareTwoTime(mSlotUnknowOrderModel.getStartSlotTime(), BSSMCommonUtils.getYear() + "-" + BSSMCommonUtils.getMonth() + "-" + BSSMCommonUtils.getDayOfMonth() + " 09:30:00")) {
                            orderAmount = Math.ceil(Math.round((hourCost + noVipHoursPay) * (timeDiff + 60)*100)/100.00);    // 小於早上九點半
                        } else {
                            orderAmount = Math.ceil(Math.round((hourCost + noVipHoursPay) * timeDiff*100)/100.00);    // 大於早上九點半
                        }
                        mSlotUnknowOrderModel.setSlotAmount(String.valueOf(orderAmount));
                        amountTv.setText("MOP" + String.valueOf(orderAmount));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } else {    // 會員
                    try {
                        String time930 = BSSMCommonUtils.getYear() + "-" + BSSMCommonUtils.getMonth() + "-" + BSSMCommonUtils.getDayOfMonth() + " 09:30:00";
                        if (BSSMCommonUtils.compareTwoTime(mSlotUnknowOrderModel.getStartSlotTime(), time930)) {
                            orderAmount = Math.ceil(Math.round(hourCost * (timeDiff + 60)*100)/100.00);// 小於早上九點半
                        } else {
                            orderAmount = Math.ceil(Math.round(hourCost * timeDiff*100)/100.00);     // 大於早上九點半
                        }
                        mSlotUnknowOrderModel.setSlotAmount(String.valueOf(orderAmount));
                        amountTv.setText("MOP" + String.valueOf(orderAmount));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            } else {    // 已知咪錶
                long timeDiff = BSSMCommonUtils.compareTimestamps(mSlotOrderModel.getStartSlotTime(), mSlotOrderModel.getEndSlotTime());
                if (mCarModel.getIfPay() == 0) {    // 非會員
                    try {
                        if (BSSMCommonUtils.compareTwoTime(mSlotOrderModel.getStartSlotTime(), BSSMCommonUtils.getYear() + "-" + BSSMCommonUtils.getMonth() + "-" + BSSMCommonUtils.getDayOfMonth() + " 09:30:00")) {
                            orderAmount = Math.ceil(Math.round((hourCost + noVipHoursPay) * (timeDiff + 60)*100)/100.00);    // 小於早上九點半
                        } else {
                            orderAmount = Math.ceil(Math.round((hourCost + noVipHoursPay) * timeDiff*100)/100.00);    // 大於早上九點半
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
                            orderAmount = Math.ceil(Math.round(hourCost * (timeDiff + 60)*100)/100.00);// 小於早上九點半
                        } else {
                            orderAmount = Math.ceil(Math.round(hourCost * timeDiff*100)/100.00);     // 大於早上九點半
                        }
                        mSlotOrderModel.setSlotAmount(String.valueOf(orderAmount));
                        amountTv.setText("MOP" + String.valueOf(orderAmount));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
    }

    /**
     * 獲取收費標準
     */
    private void callNetGetRates() {
        RequestParams params = new RequestParams(BSSMConfigtor.BASE_URL + BSSMConfigtor.GET_ORDER_RATES + SPUtils.get(this, "UserToken", ""));
        params.setAsJsonContent(true);
        JSONObject jsonObj = new JSONObject();
        try {
            if (startWay.equals(BSSMConfigtor.SLOT_MACHINE_FROM_SEARCH)) {
                jsonObj.put("carType",mSlotMachineModel.getCarType());
                jsonObj.put("pillarColor",mSlotMachineModel.getPillarColor());
                jsonObj.put("areaCode",mSlotMachineModel.getAreaCode());
            } else {
                jsonObj.put("carType",mSlotUnknowOrderModel.getCarType());
                jsonObj.put("pillarColor",mSlotUnknowOrderModel.getPillarColor());
                jsonObj.put("areaCode",mSlotUnknowOrderModel.getAreaCode());
            }
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
                    calculationOrderAmount();
                    serviceChargeTv.setText("(非會員訂單，每個鐘收取" + mRatesModel.getData().getNoVipHoursPay() + "蚊代入服務費)");
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
                    try {
                        if (BSSMCommonUtils.compareTwoTime(mSlotUnknowOrderModel.getStartSlotTime().substring(0, 10) + " 08:59:00", mSlotUnknowOrderModel.getStartSlotTime()) && BSSMCommonUtils.compareTwoTime(mSlotUnknowOrderModel.getStartSlotTime(), mSlotUnknowOrderModel.getStartSlotTime().substring(0, 10) + " 20:01:00")) {
                            if (!tomorrowCb.isChecked()) {
                                // 現在時間
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                Calendar nowTime = Calendar.getInstance();
                                nowTime.add(Calendar.MINUTE, 58);//60分钟后的时间
                                String startTimeForDay = sdf.format(nowTime.getTime());
                                if (!BSSMCommonUtils.compareTwoTime(mSlotUnknowOrderModel.getStartSlotTime(), startTimeForDay)) {
                                    NiceDialog.init()
                                            .setLayoutId(R.layout.dialog_photo)
                                            .setConvertListener(new ViewConvertListener() {
                                                @Override
                                                protected void convertView(ViewHolder viewHolder, final BaseNiceDialog baseNiceDialog) {
                                                    viewHolder.setOnClickListener(R.id.photo_camare, new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View view) {
                                                            autoObtainCameraPermission();
                                                            baseNiceDialog.dismiss();
                                                        }
                                                    });
                                                    viewHolder.setOnClickListener(R.id.photo_album, new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View view) {
                                                            autoObtainStoragePermission();
                                                            baseNiceDialog.dismiss();
                                                        }
                                                    });
                                                    viewHolder.setOnClickListener(R.id.photo_cancel, new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View view) {
                                                            baseNiceDialog.dismiss();
                                                        }
                                                    });
                                                }
                                            })
                                            .setShowBottom(true)
                                            .show(getSupportFragmentManager());
                                } else {
                                    Toast.makeText(this, "请选择一个钟后嘅时间！", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                NiceDialog.init()
                                        .setLayoutId(R.layout.dialog_photo)
                                        .setConvertListener(new ViewConvertListener() {
                                            @Override
                                            protected void convertView(ViewHolder viewHolder, final BaseNiceDialog baseNiceDialog) {
                                                viewHolder.setOnClickListener(R.id.photo_camare, new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {
                                                        autoObtainCameraPermission();
                                                        baseNiceDialog.dismiss();
                                                    }
                                                });
                                                viewHolder.setOnClickListener(R.id.photo_album, new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {
                                                        autoObtainStoragePermission();
                                                        baseNiceDialog.dismiss();
                                                    }
                                                });
                                                viewHolder.setOnClickListener(R.id.photo_cancel, new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {
                                                        baseNiceDialog.dismiss();
                                                    }
                                                });
                                            }
                                        })
                                        .setShowBottom(true)
                                        .show(getSupportFragmentManager());
                            }
                        } else {
                            Toast.makeText(this, "開始投幣時間不得超過9:00-20:00！", Toast.LENGTH_SHORT).show();
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } else if (startWay.equals(BSSMConfigtor.SLOT_MACHINE_EXIST)){   //咪錶存在，無子列表

                } else {
                    if (mSlotOrderModel.getStartSlotTime() != null && mSlotOrderModel.getEndSlotTime() != null) {
                        try {
                            if (BSSMCommonUtils.compareTwoTime(mSlotOrderModel.getStartSlotTime().substring(0, 10) + " 08:59:00", mSlotOrderModel.getStartSlotTime()) && BSSMCommonUtils.compareTwoTime(mSlotOrderModel.getStartSlotTime(), mSlotOrderModel.getStartSlotTime().substring(0, 10) + " 20:01:00")) {
                                if (!tomorrowCb.isChecked()) {
                                    // 現在時間
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    Calendar nowTime = Calendar.getInstance();
                                    nowTime.add(Calendar.MINUTE, 58);//60分钟后的时间
                                    String startTimeForDay = sdf.format(nowTime.getTime());
                                    if (!BSSMCommonUtils.compareTwoTime(mSlotOrderModel.getStartSlotTime(), startTimeForDay)) {
                                        Intent intent = new Intent(this, ParkingOrderActivity.class);
                                        intent.putExtra("way", BSSMConfigtor.SLOT_MACHINE_FROM_SEARCH);
                                        intent.putExtra("SlotMachine", getIntent().getStringExtra("SlotMachine"));
                                        intent.putExtra("childPosition", getIntent().getStringExtra("childPosition"));
                                        intent.putExtra("carModel", getIntent().getStringExtra("carModel"));
                                        intent.putExtra("SlotOrder", new Gson().toJson(mSlotOrderModel));
                                        intent.putExtra("RatesModel", new Gson().toJson(mRatesModel));
                                        intent.putExtra("isTomorrow", tomorrowCb.isChecked());
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(this, "请选择一个钟后嘅时间！", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Intent intent = new Intent(this, ParkingOrderActivity.class);
                                    intent.putExtra("way", BSSMConfigtor.SLOT_MACHINE_FROM_SEARCH);
                                    intent.putExtra("SlotMachine", getIntent().getStringExtra("SlotMachine"));
                                    intent.putExtra("childPosition", getIntent().getStringExtra("childPosition"));
                                    intent.putExtra("carModel", getIntent().getStringExtra("carModel"));
                                    intent.putExtra("SlotOrder", new Gson().toJson(mSlotOrderModel));
                                    intent.putExtra("RatesModel", new Gson().toJson(mRatesModel));
                                    intent.putExtra("isTomorrow", tomorrowCb.isChecked());
                                    startActivity(intent);
                                }
                            } else {
                                Toast.makeText(this, "開始投幣時間不得超過9:00-20:00！", Toast.LENGTH_SHORT).show();
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(this, "請選擇開始投幣時間及結束投幣時間！", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.choose_order_time_car_manage_ll:
                finish();
                break;
            case R.id.time_start_tv:
                mStartTimePicker.show(getSupportFragmentManager(), "time_picker");
                break;
            case R.id.time_end_tv:
                mEndTimePicker.show(getSupportFragmentManager(), "time_picker");
                break;
        }
    }

    @Override
    public void getStarttImeData(String time) {
        //data即为fragment调用该函数传回的日期时间
        try {
            String date = BSSMCommonUtils.getTodayDate();
            if (tomorrowCb.isChecked()) {
                date = BSSMCommonUtils.getTomorrowDate();
            }

            if (startWay.equals(BSSMConfigtor.SLOT_MACHINE_NOT_EXIST)){
                mSlotUnknowOrderModel.setStartSlotTime(date + " " + time);
                if (BSSMCommonUtils.compareTwoTime(mSlotUnknowOrderModel.getEndSlotTime(), mSlotUnknowOrderModel.getStartSlotTime())) {
                    Toast.makeText(this, "結束時間唔可以大過開始時間！", Toast.LENGTH_SHORT).show();
                    mSlotUnknowOrderModel.setEndSlotTime(BSSMCommonUtils.getHalfHourTime(mSlotUnknowOrderModel.getStartSlotTime()));
                }

                startTimeTv.setText(mSlotUnknowOrderModel.getStartSlotTime().substring(11,19));
                endTimeTv.setText(mSlotUnknowOrderModel.getEndSlotTime().substring(11,19));
            } else {
                mSlotOrderModel.setStartSlotTime(date + " " + time);
                if (BSSMCommonUtils.compareTwoTime(mSlotOrderModel.getEndSlotTime(), mSlotOrderModel.getStartSlotTime())) {
                    Toast.makeText(this, "結束時間唔可以大過開始時間！", Toast.LENGTH_SHORT).show();
                    mSlotOrderModel.setEndSlotTime(BSSMCommonUtils.getHalfHourTime(mSlotOrderModel.getStartSlotTime()));
                }

                startTimeTv.setText(mSlotOrderModel.getStartSlotTime().substring(11,19));
                endTimeTv.setText(mSlotOrderModel.getEndSlotTime().substring(11,19));
            }

            calculationOrderAmount();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getEndImeData(String time) {
        //data即为fragment调用该函数传回的日期时间
        try {
            String date = BSSMCommonUtils.getTodayDate();
            if (tomorrowCb.isChecked()) {
                date = BSSMCommonUtils.getTomorrowDate();
            }

            if (startWay.equals(BSSMConfigtor.SLOT_MACHINE_NOT_EXIST)){
                mSlotUnknowOrderModel.setEndSlotTime(date + " " + time);
                if (BSSMCommonUtils.compareTwoTime(mSlotUnknowOrderModel.getEndSlotTime(), mSlotUnknowOrderModel.getStartSlotTime())) {
                    Toast.makeText(this, "結束時間唔可以大過開始時間！", Toast.LENGTH_SHORT).show();
                    mSlotUnknowOrderModel.setEndSlotTime(BSSMCommonUtils.getHalfHourTime(mSlotUnknowOrderModel.getStartSlotTime()));
                }

                startTimeTv.setText(mSlotUnknowOrderModel.getStartSlotTime().substring(11,19));
                endTimeTv.setText(mSlotUnknowOrderModel.getEndSlotTime());
            } else {
                mSlotOrderModel.setEndSlotTime(date + " " + time);
                if (BSSMCommonUtils.compareTwoTime(mSlotOrderModel.getEndSlotTime(), mSlotOrderModel.getStartSlotTime())) {
                    Toast.makeText(this, "結束時間唔可以大過開始時間！", Toast.LENGTH_SHORT).show();
                    mSlotOrderModel.setEndSlotTime(BSSMCommonUtils.getHalfHourTime(mSlotOrderModel.getStartSlotTime()));
                }

                startTimeTv.setText(mSlotOrderModel.getStartSlotTime().substring(11,19));
                endTimeTv.setText(mSlotOrderModel.getEndSlotTime().substring(11,19));
            }
            calculationOrderAmount();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    //实现开始时间OnTimeSetListener接口
    public static class StartTimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener{
        String startTime;
        public StartTimePickerFragment (String startTime) {
            this.startTime = startTime;
        }
        private String time = "";
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            //返回TimePickerDialog对象
            //因为实现了OnTimeSetListener接口，所以第二个参数直接传入this
            String sh = startTime.substring(11, 13);
            String sm = startTime.substring(14, 16);
            return new TimePickerDialog(getActivity(), AlertDialog.THEME_HOLO_LIGHT, this, Integer.parseInt(sh), Integer.parseInt(sm), true);
        }

        //实现OnTimeSetListener的onTimeSet方法
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            //判断activity是否是DataCallBack(这是自己定义的一个接口)的一个实例
            if(getActivity() instanceof StartTimeDataCallBack){
                //将activity强转为DataCallBack
                StartTimeDataCallBack dataCallBack = (StartTimeDataCallBack) getActivity();
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
                time = hourTime + ":" + minuteTime + ":00";
                //调用activity的getData方法将数据传回activity显示
                dataCallBack.getStarttImeData(time);
            }
        }

        @Override
        public void show(FragmentManager manager, String tag) {
            super.show(manager, tag);
            Log.d("", "");
        }

        public void setTime(String date){
            time += date;
        }
    }

    //实现结束时间OnTimeSetListener接口
    public static class EndTimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener{
        private String time = "";
        String endTime;
        public EndTimePickerFragment (String endTime) {
            this.endTime = endTime;
        }
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            //新建日历类用于获取当前时间
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MINUTE, 120);
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);
            //返回TimePickerDialog对象
            //因为实现了OnTimeSetListener接口，所以第二个参数直接传入this
            return new TimePickerDialog(getActivity(), AlertDialog.THEME_HOLO_LIGHT, this, Integer.parseInt(endTime.substring(11, 13)), Integer.parseInt(endTime.substring(14, 16)), true);
        }

        //实现OnTimeSetListener的onTimeSet方法
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            //判断activity是否是DataCallBack(这是自己定义的一个接口)的一个实例
            if(getActivity() instanceof StartTimeDataCallBack){
                //将activity强转为DataCallBack
                EndTimeDataCallBack dataCallBack = (EndTimeDataCallBack) getActivity();
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
                time = hourTime + ":" + minuteTime + ":00";
                //调用activity的getData方法将数据传回activity显示
                dataCallBack.getEndImeData(time);
            }
        }

        @Override
        public void show(FragmentManager manager, String tag) {
            super.show(manager, tag);
            Log.d("", "");
        }

        public void setTime(String date){
            time += date;
        }
    }

    /**
     * 自动获取相机权限
     */
    private void autoObtainCameraPermission() {
        try {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                    Toast.makeText(ChooseOrderTimeActivity.this, "您已经拒绝过一次", Toast.LENGTH_SHORT).show();
                }
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, CAMERA_PERMISSIONS_REQUEST_CODE);
            } else {//有权限直接调用系统相机拍照
                SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
                Date date = new Date(System.currentTimeMillis());
                fileUri = new File(dir.getPath() + "/order" + format.format(date) + ".jpg");
                imageUri = Uri.fromFile(fileUri);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                    imageUri = FileProvider.getUriForFile(ChooseOrderTimeActivity.this, "com.bs.john_li.bsfslotmachine" + ".fileprovider", fileUri);//通过FileProvider创建一个content类型的Uri
                }

                PhotoUtils.takePicture(this, imageUri, CODE_CAMERA_REQUEST);
            }
        }catch (Exception e) {

        }
    }

    /**
     * 自动获取相冊权限
     */
    private void autoObtainStoragePermission() {
        // 使用意图直接调用手机相册  
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // 打开手机相册,设置请求码  
        startActivityForResult(intent, CODE_GALLERY_REQUEST);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case CAMERA_PERMISSIONS_REQUEST_CODE: {//调用系统相机申请拍照权限回调
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
                    Date date = new Date(System.currentTimeMillis());
                    fileUri = new File(dir.getPath() + "/order" + format.format(date) + ".jpg");
                    imageUri = Uri.fromFile(fileUri);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                        imageUri = FileProvider.getUriForFile(ChooseOrderTimeActivity.this, "com.bs.john_li.bsfslotmachine" + ".fileprovider", fileUri);//通过FileProvider创建一个content类型的Uri
                    PhotoUtils.takePicture(this, imageUri, CODE_CAMERA_REQUEST);
                } else {
                    Toast.makeText(ChooseOrderTimeActivity.this, "请允许打开相机", Toast.LENGTH_SHORT).show();
                }
                break;


            }
            case STORAGE_PERMISSIONS_REQUEST_CODE://调用系统相册申请Sdcard权限回调
                // 使用意图直接调用手机相册  
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                // 打开手机相册,设置请求码  
                startActivityForResult(intent, CODE_GALLERY_REQUEST);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            Toast.makeText(ChooseOrderTimeActivity.this, "影相失敗！", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent1 = new Intent(this, ParkingOrderActivity.class);
        switch(requestCode) {
            case CODE_CAMERA_REQUEST:
                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                Uri contentUri = Uri.fromFile(fileUri);
                mediaScanIntent.setData(contentUri);
                sendBroadcast(mediaScanIntent);
                intent1.putExtra("way", BSSMConfigtor.SLOT_MACHINE_NOT_EXIST);
                intent1.putExtra("imageUri", fileUri.getPath());
                intent1.putExtra("unknowSlotOrder", new Gson().toJson(mSlotUnknowOrderModel));
                intent1.putExtra("carModel", getIntent().getStringExtra("carModel"));
                intent1.putExtra("RatesModel", new Gson().toJson(mRatesModel));
                intent1.putExtra("isTomorrow", tomorrowCb.isChecked());
                startActivity(intent1);
                finish();
                break;
            case CODE_GALLERY_REQUEST:
                String imagePath = BSSMCommonUtils.getRealFilePath(this, data.getData());
                fileUri = new File(imagePath);
                intent1.putExtra("way", BSSMConfigtor.SLOT_MACHINE_NOT_EXIST);
                intent1.putExtra("imageUri", fileUri.getPath());
                intent1.putExtra("unknowSlotOrder", new Gson().toJson(mSlotUnknowOrderModel));
                intent1.putExtra("carModel", getIntent().getStringExtra("carModel"));
                intent1.putExtra("RatesModel", new Gson().toJson(mRatesModel));
                intent1.putExtra("isTomorrow", tomorrowCb.isChecked());
                startActivity(intent1);
                finish();
                break;
            default:
                break;
        }
    }
}
