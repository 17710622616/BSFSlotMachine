package com.bs.john_li.bsfslotmachine.BSSMActivity.Parking;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bs.john_li.bsfslotmachine.BSSMActivity.BaseActivity;
import com.bs.john_li.bsfslotmachine.BSSMActivity.Mine.CarListActivity;
import com.bs.john_li.bsfslotmachine.BSSMModel.CarModel;
import com.bs.john_li.bsfslotmachine.BSSMModel.SlotOrderModel;
import com.bs.john_li.bsfslotmachine.BSSMUtils.BSSMCommonUtils;
import com.bs.john_li.bsfslotmachine.BSSMUtils.BSSMConfigtor;
import com.bs.john_li.bsfslotmachine.BSSMUtils.SPUtils;
import com.bs.john_li.bsfslotmachine.BSSMView.BSSMHeadView;
import com.bs.john_li.bsfslotmachine.R;
import com.othershe.nicedialog.BaseNiceDialog;
import com.othershe.nicedialog.NiceDialog;
import com.othershe.nicedialog.ViewConvertListener;
import com.othershe.nicedialog.ViewHolder;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 停車訂單界面
 * Created by John_Li on 23/9/2017.
 */

public class ParkingOrderActivity extends BaseActivity implements View.OnClickListener{
    private BSSMHeadView headView;
    private ImageView parkingIv;
    private LinearLayout carManageLL, startTimeLL, orderMoneyLL, orderRemarkLL, orderAreaLL, voucherLL;
    private RelativeLayout carManageRL;
    public TextView carManagetv, remarkTv, areaTv, startTimeTv;

    private String way;
    private List<String> imgUrlList;
    private ImageOptions options = new ImageOptions.Builder().setSize(0, 0).setFailureDrawableId(R.mipmap.car_sample).build();
    private CarModel.CarCountAndListModel.CarInsideModel carInsideModel;
    public SlotOrderModel mSlotOrderModel;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking_order);
        initView();
        setListener();
        initData();
    }

    @Override
    public void initView() {
        headView = findViewById(R.id.parking_order_head);
        parkingIv = findViewById(R.id.parking_iv);
        carManageLL = findViewById(R.id.parking_order_car_manage_ll);
        startTimeLL = findViewById(R.id.parking_order_starttime_ll);
        orderMoneyLL = findViewById(R.id.parking_order_money_ll);
        orderRemarkLL = findViewById(R.id.parking_order_remark_ll);
        orderAreaLL = findViewById(R.id.parking_order_area_ll);
        carManageRL = findViewById(R.id.parking_order_car_manage_rl);
        carManagetv = findViewById(R.id.parking_order_car_manage_tv);
        remarkTv = findViewById(R.id.parking_order_remark_tv);
        areaTv = findViewById(R.id.parking_order_area_tv);
        voucherLL = findViewById(R.id.parking_order_voucher_ll);
        startTimeTv = findViewById(R.id.parking_order_starttime_tv);
    }

    @Override
    public void setListener() {
        carManageLL.setOnClickListener(this);
        startTimeLL.setOnClickListener(this);
        orderMoneyLL.setOnClickListener(this);
        orderRemarkLL.setOnClickListener(this);
        orderAreaLL.setOnClickListener(this);
        voucherLL.setOnClickListener(this);
    }

    @Override
    public void initData() {
        headView.setTitle("下單");
        headView.setLeft(this);
        Intent intent = getIntent();
        way = intent.getStringExtra("way");
        imgUrlList = new ArrayList<>();
        // 判斷訂單類別
        if (way.equals(BSSMConfigtor.SLOT_MACHINE_NOT_EXIST)) { // 咪錶不存在
            String imgUri = intent.getStringExtra("imageUri");
            Log.d("imgUri", imgUri);
            imgUrlList.add(imgUri);
            x.image().bind(parkingIv, imgUrlList.get(0), options);
            orderAreaLL.setVisibility(View.VISIBLE);
        } else if (way.equals(BSSMConfigtor.SLOT_MACHINE_EXIST)){   //咪錶存在，定位停車
            orderAreaLL.setVisibility(View.GONE);
        } else {    // 咪錶存在，搜索停車
            orderAreaLL.setVisibility(View.GONE);
        }

        // 停車訂單
        mSlotOrderModel = new SlotOrderModel();
        // 車輛類
        carInsideModel = new CarModel.CarCountAndListModel.CarInsideModel();
        // 判断车辆是否选择车辆
        isChooseCar();
        // 現在時間
        Date date = new Date( );
        SimpleDateFormat fdt = new SimpleDateFormat ("hh:mm:ss");
        startTimeTv.setText("投幣時間[預計" + fdt.format(date) + "]");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.head_left:
                finish();
                break;
            case R.id.parking_order_car_manage_ll:
                // 选择车辆
                Intent updateCarIntent = new Intent(this, CarListActivity.class);
                if (carInsideModel.getId() != 0) {  // 已選擇車輛，修改選擇車輛

                } else {    // 未選擇車輛

                }
                startActivity(updateCarIntent);
                break;
            case R.id.parking_order_starttime_ll:
                chooseOrderStartTime();
                break;
            case R.id.parking_order_money_ll:
                chooseOrderMoney();
                break;
            case R.id.parking_order_remark_ll:
                editOrderRemark();
                break;
            case R.id.parking_order_area_ll:
                chooseOrderArea();
                break;
            case R.id.parking_order_voucher_ll:
                Toast.makeText(this, "暫無優惠券~", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    /**
     * 判断是否选择车辆
     */
    private void isChooseCar() {
        callNetGetCarList();

        if (carInsideModel.getId() != 0) {
            carManagetv.setText("車輛管理");
            carManageRL.setVisibility(View.VISIBLE);
        } else {
            carManagetv.setText("未選擇車輛，請先選擇車輛");
            carManageRL.setVisibility(View.GONE);
        }
    }

    /**
     * 獲取已充值車輛列表
     */
    private void callNetGetCarList() {
        RequestParams params = new RequestParams(BSSMConfigtor.BASE_URL + BSSMConfigtor.GET_CAR_LIST_RECHARGE + SPUtils.get(this, "UserToken", ""));
        x.http().request(HttpMethod.POST ,params, new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        Toast.makeText(ParkingOrderActivity.this, "獲取成功", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {

                    }

                    @Override
                    public void onCancelled(CancelledException cex) {

                    }

                    @Override
                    public void onFinished() {

                    }
                });
    }

    /**
     * 选择订单开始时间
     */
    private void chooseOrderStartTime() {
        NiceDialog.init()
                .setLayoutId(R.layout.dialog_choose_time)
                .setConvertListener(new ViewConvertListener() {
                    @Override
                    public void convertView(ViewHolder holder, final BaseNiceDialog dialog) {
                        TimePicker timePicker = holder.getView(R.id.dialog_time_picker);
                        timePicker.setIs24HourView(true);
                        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
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
                                String time = hourTime + ":" + minuteTime + ":00";
                                mSlotOrderModel.setStartSlotTime(time);
                                startTimeTv.setText("投幣時間[預計" + time + "]");
                            }
                        });
                    }
                })
                .setWidth(210)
                .show(getSupportFragmentManager());
    }

    /**
     * 选择订单金额
     */
    private void chooseOrderMoney() {

    }

    /**
     * 填写备注
     */
    private void editOrderRemark() {
        NiceDialog.init()
                .setLayoutId(R.layout.dialog_car_edit)
                .setConvertListener(new ViewConvertListener() {
                    @Override
                    public void convertView(ViewHolder holder, final BaseNiceDialog dialog) {
                        final EditText editText = holder.getView(R.id.car_edit);
                        if (way.equals(BSSMConfigtor.SLOT_MACHINE_NOT_EXIST)) { // 已顯示
                            editText.setHint("請填寫備註");
                        } else {
                            editText.setHint("請填寫備註(備註內容中請務必包含詳細地址！)");
                        }
                        BSSMCommonUtils.showKeyboard(editText);
                        holder.setOnClickListener(R.id.car_edit_submit, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                remarkTv.setText("備      註：" + editText.getText().toString());
                                dialog.dismiss();
                            }
                        });
                    }
                })
                .setShowBottom(true)
                .show(getSupportFragmentManager());
    }

    /**
     * 選擇地區
     */
    private void chooseOrderArea() {
        NiceDialog.init()
                .setLayoutId(R.layout.dialog_order_area)
                .setConvertListener(new ViewConvertListener() {
                    @Override
                    protected void convertView(ViewHolder viewHolder, final BaseNiceDialog baseNiceDialog) {
                        viewHolder.setOnClickListener(R.id.order_area_datang_tv, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                areaTv.setText("地      區：" + "大堂區");
                                baseNiceDialog.dismiss();
                            }
                        });
                        viewHolder.setOnClickListener(R.id.order_area_wangde_tv, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                areaTv.setText("地      區：" + "望德堂區");
                                baseNiceDialog.dismiss();
                            }
                        });
                        viewHolder.setOnClickListener(R.id.order_area_fengshun_tv, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                areaTv.setText("地      區：" + "風順堂區");
                                baseNiceDialog.dismiss();
                            }
                        });
                        viewHolder.setOnClickListener(R.id.order_area_huawang_tv, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                areaTv.setText("地      區：" + "花王堂區");
                                baseNiceDialog.dismiss();
                            }
                        });
                        viewHolder.setOnClickListener(R.id.order_area_huadima_tv, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                areaTv.setText("地      區：" + "花地瑪堂區");
                                baseNiceDialog.dismiss();
                            }
                        });
                        viewHolder.setOnClickListener(R.id.order_area_jiamo_tv, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                areaTv.setText("地      區：" + "嘉模堂區");
                                baseNiceDialog.dismiss();
                            }
                        });
                        viewHolder.setOnClickListener(R.id.order_area_other_tv, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                areaTv.setText("地      區：" + "");
                                baseNiceDialog.dismiss();
                            }
                        });
                    }
                })
                .setShowBottom(true)
                .show(getSupportFragmentManager());
    }
}
