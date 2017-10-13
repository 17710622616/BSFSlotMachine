package com.bs.john_li.bsfslotmachine.BSSMActivity.Parking;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.BoolRes;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bs.john_li.bsfslotmachine.BSSMActivity.BaseActivity;
import com.bs.john_li.bsfslotmachine.BSSMActivity.Mine.CarListActivity;
import com.bs.john_li.bsfslotmachine.BSSMAdapter.PhotoAdapter;
import com.bs.john_li.bsfslotmachine.BSSMModel.CarModel;
import com.bs.john_li.bsfslotmachine.BSSMModel.CommonModel;
import com.bs.john_li.bsfslotmachine.BSSMModel.SlotMachineListOutsideModel;
import com.bs.john_li.bsfslotmachine.BSSMModel.SlotOrderModel;
import com.bs.john_li.bsfslotmachine.BSSMModel.TestCarListModel;
import com.bs.john_li.bsfslotmachine.BSSMUtils.BSSMCommonUtils;
import com.bs.john_li.bsfslotmachine.BSSMUtils.BSSMConfigtor;
import com.bs.john_li.bsfslotmachine.BSSMUtils.SPUtils;
import com.bs.john_li.bsfslotmachine.BSSMView.BSSMHeadView;
import com.bs.john_li.bsfslotmachine.BSSMView.NoScrollGridView;
import com.bs.john_li.bsfslotmachine.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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
    private LinearLayout carManageLL, startTimeLL, orderMoneyLL, orderRemarkLL, orderAreaLL, voucherLL, orderColorLL, orderPhotoLL;
    private RelativeLayout carManageRL;
    public TextView carManagetv, carBrandTv, carTypeTv, carNumTv, orderLocationTv, orderMoneyTv, remarkTv, areaTv, startTimeTv, submitTv,meterColorTv;
    public NoScrollGridView photoGv;

    private String way;
    private List<String> imgUrlList;
    private ImageOptions options = new ImageOptions.Builder().setSize(0, 0).setFailureDrawableId(R.mipmap.car_sample).build();
    private List<TestCarListModel.CarModel> carInsideModelList;
    private SlotMachineListOutsideModel.SlotMachineListModel.SlotMachineModel mSlotMachineModel;
    public SlotOrderModel mSlotOrderModel;  // 未知咪錶拍照時記得用saveinstans保存，完成之後還需把原來的數據擺回界面
    private PhotoAdapter mPhotoAdapter;

    // 拍照的參數
    public static final int TAKE_PHOTO = 1;
    public static final int TAKE_PHOTO_FROM_ALBUM = 2;
    private File dir; //圖片文件夾路徑
    private File file;  //照片文件

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
        photoGv = findViewById(R.id.parking_order_gv);
        carManageLL = findViewById(R.id.parking_order_car_manage_ll);
        startTimeLL = findViewById(R.id.parking_order_starttime_ll);
        orderMoneyLL = findViewById(R.id.parking_order_money_ll);
        orderRemarkLL = findViewById(R.id.parking_order_remark_ll);
        orderAreaLL = findViewById(R.id.parking_order_area_ll);
        orderColorLL = findViewById(R.id.parking_order_color_ll);
        orderPhotoLL = findViewById(R.id.parking_order_photo_ll);
        carManageRL = findViewById(R.id.parking_order_car_manage_rl);
        carManagetv = findViewById(R.id.parking_order_car_manage_tv);
        carBrandTv = findViewById(R.id.parking_order_car_brand);
        carTypeTv = findViewById(R.id.parking_order_car_type);
        carNumTv = findViewById(R.id.parking_order_car_num);
        orderLocationTv = findViewById(R.id.parking_order_location_tv);
        orderMoneyTv = findViewById(R.id.parking_order_money_tv);
        remarkTv = findViewById(R.id.parking_order_remark_tv);
        areaTv = findViewById(R.id.parking_order_area_tv);
        voucherLL = findViewById(R.id.parking_order_voucher_ll);
        meterColorTv = findViewById(R.id.parking_order_color_tv);
        startTimeTv = findViewById(R.id.parking_order_starttime_tv);
        submitTv = findViewById(R.id.parking_order_submit);
    }

    @Override
    public void setListener() {
        carManageLL.setOnClickListener(this);
        startTimeLL.setOnClickListener(this);
        orderMoneyLL.setOnClickListener(this);
        orderRemarkLL.setOnClickListener(this);
        orderAreaLL.setOnClickListener(this);
        orderColorLL.setOnClickListener(this);
        voucherLL.setOnClickListener(this);
        submitTv.setOnClickListener(this);

        photoGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == imgUrlList.size() - 1) {
                    if (imgUrlList.size() < 6) {
                        NiceDialog.init()
                                .setLayoutId(R.layout.dialog_photo)
                                .setConvertListener(new ViewConvertListener() {
                                    @Override
                                    protected void convertView(ViewHolder viewHolder, final BaseNiceDialog baseNiceDialog) {
                                        viewHolder.setOnClickListener(R.id.photo_camare, new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                if(BSSMCommonUtils.IsThereAnAppToTakePictures(ParkingOrderActivity.this)) {
                                                    dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "BSSMPictures");
                                                    if (!dir.exists()) {
                                                        dir.mkdir();
                                                    }

                                                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                                    SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
                                                    Date date = new Date(System.currentTimeMillis());
                                                    file = new File(dir, "location" + format.format(date) + ".jpg");
                                                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                                                    startActivityForResult(intent, TAKE_PHOTO);
                                                } else {
                                                    Toast.makeText(ParkingOrderActivity.this,"您的照相機不可用哦，請檢測相機先！",Toast.LENGTH_SHORT).show();
                                                }
                                                baseNiceDialog.dismiss();
                                            }
                                        });
                                        viewHolder.setOnClickListener(R.id.photo_album, new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "BSSMPictures");
                                                if (!dir.exists()) {
                                                    dir.mkdir();
                                                }

                                                Intent getAlbum;
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                                                    getAlbum = new Intent(Intent.ACTION_PICK);
                                                } else {
                                                    getAlbum = new Intent(Intent.ACTION_GET_CONTENT);
                                                }
                                                getAlbum.setType("image/*");
                                                startActivityForResult(getAlbum, TAKE_PHOTO_FROM_ALBUM);
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
                        Toast.makeText(ParkingOrderActivity.this, "照片數量不可多於5張哦~", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    @Override
    public void initData() {
        headView.setTitle("下單");
        headView.setLeft(this);

        // 停車訂單
        mSlotOrderModel = new SlotOrderModel();
        // 車輛類
        carInsideModelList = new ArrayList<>();
        // 判断车辆是否选择车辆
        isChooseCar();
        // 現在時間
        Date date = new Date( );
        SimpleDateFormat fdt = new SimpleDateFormat ("hh:mm:ss");
        SimpleDateFormat yearFdt = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
        startTimeTv.setText("投幣時間[預計" + fdt.format(date) + "]");
        mSlotOrderModel.setStartSlotTime(yearFdt.format(date));
        mSlotOrderModel.setRemark("");

        // 判斷是哪種打開方式
        Intent intent = getIntent();
        way = intent.getStringExtra("way");
        imgUrlList = new ArrayList<>();
        // 判斷訂單類別
        if (way.equals(BSSMConfigtor.SLOT_MACHINE_NOT_EXIST)) { // 咪錶不存在
            String imgUri = intent.getStringExtra("imageUri");
            imgUrlList.add(imgUri);
            imgUrlList.add("");
            mPhotoAdapter = new PhotoAdapter(this, imgUrlList);
            orderLocationTv.setText("停車位置：未知");
            photoGv.setAdapter(mPhotoAdapter);
            photoGv.setVisibility(View.VISIBLE);
            orderPhotoLL.setVisibility(View.VISIBLE);
            orderAreaLL.setVisibility(View.VISIBLE);
            orderColorLL.setVisibility(View.VISIBLE);
            isAreaAndColorChoose(); // 是否可選擇金額
        } else if (way.equals(BSSMConfigtor.SLOT_MACHINE_EXIST)){   //咪錶存在，定位停車
            mSlotMachineModel = new Gson().fromJson(intent.getStringExtra("SlotMachine"), SlotMachineListOutsideModel.SlotMachineListModel.SlotMachineModel.class);
            mSlotOrderModel.setMachineNo(mSlotMachineModel.getMachineNo());
            orderLocationTv.setText("停車位置：" + mSlotMachineModel.getAddress() + mSlotMachineModel.getMachineNo() + "號");
            orderAreaLL.setVisibility(View.GONE);
            orderColorLL.setVisibility(View.GONE);
            orderPhotoLL.setVisibility(View.GONE);
            photoGv.setVisibility(View.GONE);
        } else {    // 咪錶存在，搜索停車
            mSlotMachineModel = new Gson().fromJson(intent.getStringExtra("SlotMachine"), SlotMachineListOutsideModel.SlotMachineListModel.SlotMachineModel.class);
            mSlotOrderModel.setMachineNo(mSlotMachineModel.getMachineNo());
            orderLocationTv.setText("停車位置：" + mSlotMachineModel.getAddress() + mSlotMachineModel.getMachineNo() + "號");
            orderAreaLL.setVisibility(View.GONE);
            orderColorLL.setVisibility(View.GONE);
            orderPhotoLL.setVisibility(View.GONE);
            photoGv.setVisibility(View.GONE);
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.head_left:
                finish();
                break;
            case R.id.parking_order_car_manage_ll:
                // 选择车辆
                /*Intent updateCarIntent = new Intent(this, ChooseCarActivity.class);
                if (carInsideModelList.size() > 0) {  // 有已充值的車輛，默認選擇是第一個車輛，點擊修改選擇車輛
                    updateCarIntent.putExtra("carList", new Gson().toJson(carInsideModelList));
                } else {    // 沒有已充值的車輛，點擊添加車輛

                }*/
                startActivityForResult(new Intent(this, ChooseCarActivity.class), 6);
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
            case R.id.parking_order_color_ll:
                chooseOrderColor();
                break;
            case R.id.parking_order_voucher_ll:
                Toast.makeText(this, "暫無優惠券~", Toast.LENGTH_SHORT).show();
                break;
            case R.id.parking_order_submit:
                mSlotOrderModel.setSlotAmount("20");
                if (way.equals(BSSMConfigtor.SLOT_MACHINE_NOT_EXIST)) { // 咪錶不存在，提交未知訂單
                    Intent intent = new Intent(ParkingOrderActivity.this, PaymentAcvtivity.class);
                    intent.putExtra("orderNo", "123456789");
                    startActivity(intent);
                } else {    // 咪錶存在，提交已知訂單
                    if (mSlotOrderModel.getMachineNo() != null && mSlotOrderModel.getCarId() != 0 && mSlotOrderModel.getStartSlotTime() != null && mSlotOrderModel.getSlotAmount() != null) {
                        submitOrderSlotMachineExist();
                    } else {
                            Toast.makeText(this, "請填寫全訂單信息~", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

    /**
     * 提交已知咪錶訂單
     */
    private void submitOrderSlotMachineExist() {
        RequestParams params = new RequestParams(BSSMConfigtor.BASE_URL + BSSMConfigtor.SUBMIT_ORDER_SLOT_MACHINE_EXIST + SPUtils.get(this, "UserToken", ""));
        params.setAsJsonContent(true);
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("slotAmount",mSlotOrderModel.getSlotAmount());
            jsonObj.put("machineNo",mSlotOrderModel.getMachineNo());
            jsonObj.put("carId",mSlotOrderModel.getCarId());
            jsonObj.put("startSlotTime",mSlotOrderModel.getStartSlotTime());
            jsonObj.put("remark",mSlotOrderModel.getRemark());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        params.setBodyContent(jsonObj.toString());
        String uri = params.getUri();
        x.http().request(HttpMethod.POST ,params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                CommonModel model = new Gson().fromJson(result.toString(), CommonModel.class);
                if (model.getCode().equals("200")) {
                    String orderNo = model.getData();
                    Intent intent = new Intent(ParkingOrderActivity.this, PaymentAcvtivity.class);
                    intent.putExtra("orderNo", orderNo);
                    startActivity(intent);
                } else {
                    Toast.makeText(ParkingOrderActivity.this, "訂單提交失敗╮(╯▽╰)╭請重新提交", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(ParkingOrderActivity.this, "訂單提交失敗╮(╯▽╰)╭請重新提交", Toast.LENGTH_SHORT).show();
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
     * 判断是否选择车辆
     */
    private void isChooseCar() {
        callNetGetCarList();
    }

    /**
     * 獲取已充值車輛列表
     */
    private void callNetGetCarList() {
        RequestParams params = new RequestParams(BSSMConfigtor.BASE_URL + BSSMConfigtor.GET_CAR_LIST_RECHARGE + SPUtils.get(this, "UserToken", ""));
        x.http().request(HttpMethod.POST ,params, new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        TestCarListModel model = new Gson().fromJson(result.toString(), TestCarListModel.class);
                        if (model.getCode() == 200) {
                            carInsideModelList.clear();
                            for (TestCarListModel.CarModel carModel : model.getData()) {
                                if (carModel.getImgUrl() == null) {
                                    carModel.setImgUrl("objectNam1");
                                }
                                if (carModel.getModelForCar() == null) {
                                    carModel.setModelForCar("");
                                }
                                if (carModel.getCarBrand() == null) {
                                    carModel.setCarBrand("");
                                }
                                if (carModel.getCarStyle() == null) {
                                    carModel.setCarStyle("");
                                }
                                carInsideModelList.add(carModel);
                            }
                        } else {
                            Toast.makeText(ParkingOrderActivity.this, "車輛獲取失敗╮(╯▽╰)╭", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {
                        Toast.makeText(ParkingOrderActivity.this, "車輛獲取失敗╮(╯▽╰)╭", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(CancelledException cex) {

                    }

                    @Override
                    public void onFinished() {
                        refreshCarChoosed();
                    }
                });
    }

    /**
     * 刷新車輛信息
     */
    private void refreshCarChoosed() {
        if (carInsideModelList.size() > 0) {
            carManagetv.setText("車輛管理");
            carManageRL.setVisibility(View.VISIBLE);
            carBrandTv.setText("品牌：" + carInsideModelList.get(0).getCarBrand());
            carTypeTv.setText("車型：" + carInsideModelList.get(0).getModelForCar());
            carNumTv.setText("車牌號：" + carInsideModelList.get(0).getCarNo());
            mSlotOrderModel.setCarId(carInsideModelList.get(0).getId());
        } else {
            carManagetv.setText("未選擇車輛，請先選擇車輛");
            carManageRL.setVisibility(View.GONE);
        }
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
                                Date date = new Date( );
                                SimpleDateFormat yearFdt = new SimpleDateFormat ("yyyy-MM-dd");
                                mSlotOrderModel.setStartSlotTime(yearFdt.format(date) + " " +time);
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
        Toast.makeText(this, "選擇金額", Toast.LENGTH_SHORT).show();
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
                            editText.setHint("請填寫備註(備註內容中請務必包含詳細地址！)");
                        } else {
                            editText.setHint("請填寫備註");
                        }
                        BSSMCommonUtils.showKeyboard(editText);
                        holder.setOnClickListener(R.id.car_edit_submit, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                mSlotOrderModel.setRemark(editText.getText().toString());
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
     * 选择订单咪錶顏色
     */
    private void chooseOrderColor() {
        NiceDialog.init()
                .setLayoutId(R.layout.dialog_meter_color)
                .setConvertListener(new ViewConvertListener() {
                    @Override
                    protected void convertView(ViewHolder viewHolder, final BaseNiceDialog baseNiceDialog) {
                        viewHolder.setOnClickListener(R.id.meter_blue, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                meterColorTv.setText("顏      色：" + "藍色");
                                isAreaAndColorChoose();
                                baseNiceDialog.dismiss();
                            }
                        });
                        viewHolder.setOnClickListener(R.id.meter_gray, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                meterColorTv.setText("顏      色：" + "灰色");
                                isAreaAndColorChoose();
                                baseNiceDialog.dismiss();
                            }
                        });
                        viewHolder.setOnClickListener(R.id.meter_green, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                meterColorTv.setText("顏      色：" + "綠色");
                                isAreaAndColorChoose();
                                baseNiceDialog.dismiss();
                            }
                        });
                        viewHolder.setOnClickListener(R.id.meter_red, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                meterColorTv.setText("顏      色：" + "紅色");
                                isAreaAndColorChoose();
                                baseNiceDialog.dismiss();
                            }
                        });
                        viewHolder.setOnClickListener(R.id.meter_yellow, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                meterColorTv.setText("顏      色：" + "黃色");
                                isAreaAndColorChoose();
                                baseNiceDialog.dismiss();
                            }
                        });
                        viewHolder.setOnClickListener(R.id.meter_cancel, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                isAreaAndColorChoose();
                                baseNiceDialog.dismiss();
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
                                isAreaAndColorChoose();
                                baseNiceDialog.dismiss();
                            }
                        });
                        viewHolder.setOnClickListener(R.id.order_area_wangde_tv, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                areaTv.setText("地      區：" + "望德堂區");
                                isAreaAndColorChoose();
                                baseNiceDialog.dismiss();
                            }
                        });
                        viewHolder.setOnClickListener(R.id.order_area_fengshun_tv, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                areaTv.setText("地      區：" + "風順堂區");
                                isAreaAndColorChoose();
                                baseNiceDialog.dismiss();
                            }
                        });
                        viewHolder.setOnClickListener(R.id.order_area_huawang_tv, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                areaTv.setText("地      區：" + "花王堂區");
                                isAreaAndColorChoose();
                                baseNiceDialog.dismiss();
                            }
                        });
                        viewHolder.setOnClickListener(R.id.order_area_huadima_tv, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                areaTv.setText("地      區：" + "花地瑪堂區");
                                isAreaAndColorChoose();
                                baseNiceDialog.dismiss();
                            }
                        });
                        viewHolder.setOnClickListener(R.id.order_area_jiamo_tv, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                areaTv.setText("地      區：" + "嘉模堂區");
                                isAreaAndColorChoose();
                                baseNiceDialog.dismiss();
                            }
                        });
                        viewHolder.setOnClickListener(R.id.order_area_other_tv, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                areaTv.setText("地      區：" + "其他");
                                isAreaAndColorChoose();
                                baseNiceDialog.dismiss();
                            }
                        });
                        viewHolder.setOnClickListener(R.id.order_area_cancel_tv, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                isAreaAndColorChoose();
                                baseNiceDialog.dismiss();
                            }
                        });
                    }
                })
                .setShowBottom(true)
                .show(getSupportFragmentManager());
    }

    /**
     * 位置咪錶判斷是否選擇區域和咪錶顏色
     */
    public void isAreaAndColorChoose() {
        if (!areaTv.getText().toString().equals("請選擇地區") && !meterColorTv.getText().toString().equals("請選擇柱色")) {
            orderMoneyLL.setClickable(true);
            orderMoneyLL.setBackgroundColor(getResources().getColor(R.color.colorWight));
            orderMoneyTv.setTextColor(getResources().getColor(R.color.colorBlack));
        } else {
            orderMoneyLL.setClickable(false);
            orderMoneyLL.setBackgroundColor(getResources().getColor(R.color.colorLineGray));
            orderMoneyTv.setTextColor(getResources().getColor(R.color.colorMineGray));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 6: // 選擇車輛
                    TestCarListModel.CarModel carModel = new Gson().fromJson(data.getStringExtra("carModel"), TestCarListModel.CarModel.class);
                    int position = -1;
                    for (int i = 0; i < carInsideModelList.size(); i ++) {
                        if (carInsideModelList.get(i).getCarNo().equals(carModel.getCarNo())) {
                            position = i;
                        }
                    }
                    if (position != -1) {
                        carInsideModelList.remove(position);
                    }
                    carInsideModelList.add(0, carModel);
                    refreshCarChoosed();
                    break;
                case TAKE_PHOTO:
                    Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    Uri contentUri = Uri.fromFile(file);
                    mediaScanIntent.setData(contentUri);
                    sendBroadcast(mediaScanIntent);
                    imgUrlList.add(0, file.getPath());
                    mPhotoAdapter.refreshData(imgUrlList);
                    break;
                case TAKE_PHOTO_FROM_ALBUM:
                    String imagePath = BSSMCommonUtils.getRealFilePath(this, data.getData());
                    file = new File(imagePath);
                    imgUrlList.add(0, file.getPath());
                    for(String url : imgUrlList) {
                        Log.d("imgUrl", url);
                    }
                    mPhotoAdapter.refreshData(imgUrlList);
                    break;
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "BSSMPictures");
        if (file != null){
            outState.putString("file_path", file.getPath());
        }

        outState.putString("way", way);
        outState.putString("imgUrlList", new Gson().toJson(imgUrlList));
        outState.putString("carInsideModelList", new Gson().toJson(carInsideModelList));
        outState.putString("mSlotOrderModel", new Gson().toJson(mSlotOrderModel));
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "BSSMPictures");
        String cacheFileName = savedInstanceState.get("file_path").toString();
        if (cacheFileName != null) {
            if (!cacheFileName.equals("")) {
                file = new File(cacheFileName);
            }
        }

        way = savedInstanceState.getString("way");
        imgUrlList.clear();
        imgUrlList = new Gson().fromJson(savedInstanceState.getString("imgUrlList"), new TypeToken<List<String>>() {
        }.getType());
        carInsideModelList = new Gson().fromJson(savedInstanceState.getString("carInsideModelList"), new TypeToken<List<TestCarListModel.CarModel>>() {
        }.getType());
        mSlotOrderModel = new Gson().fromJson(savedInstanceState.getString("mSlotOrderModel"), SlotOrderModel.class);
        mPhotoAdapter.refreshData(imgUrlList);
    }
}
