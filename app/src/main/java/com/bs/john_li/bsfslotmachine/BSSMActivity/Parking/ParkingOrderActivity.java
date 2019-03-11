package com.bs.john_li.bsfslotmachine.BSSMActivity.Parking;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.bs.john_li.bsfslotmachine.BSSMActivity.BaseActivity;
import com.bs.john_li.bsfslotmachine.BSSMActivity.LoginActivity;
import com.bs.john_li.bsfslotmachine.BSSMActivity.Mine.ChooseDiscountActivity;
import com.bs.john_li.bsfslotmachine.BSSMActivity.Mine.ShareActivity;
import com.bs.john_li.bsfslotmachine.BSSMAdapter.PhotoAdapter;
import com.bs.john_li.bsfslotmachine.BSSMModel.CarModel;
import com.bs.john_li.bsfslotmachine.BSSMModel.CommonJieModel;
import com.bs.john_li.bsfslotmachine.BSSMModel.DiscountOutModel;
import com.bs.john_li.bsfslotmachine.BSSMModel.OrderModel;
import com.bs.john_li.bsfslotmachine.BSSMModel.RatesModel;
import com.bs.john_li.bsfslotmachine.BSSMModel.SlotMachineListOutsideModel;
import com.bs.john_li.bsfslotmachine.BSSMModel.SlotOrderModel;
import com.bs.john_li.bsfslotmachine.BSSMModel.SlotUnknowOrderModel;
import com.bs.john_li.bsfslotmachine.BSSMModel.TestCarListModel;
import com.bs.john_li.bsfslotmachine.BSSMUtils.AliyunOSSUtils;
import com.bs.john_li.bsfslotmachine.BSSMUtils.BSSMCommonUtils;
import com.bs.john_li.bsfslotmachine.BSSMUtils.BSSMConfigtor;
import com.bs.john_li.bsfslotmachine.BSSMUtils.PhotoUtils;
import com.bs.john_li.bsfslotmachine.BSSMUtils.SPUtils;
import com.bs.john_li.bsfslotmachine.BSSMView.BSSMHeadView;
import com.bs.john_li.bsfslotmachine.BSSMView.LoadDialog;
import com.bs.john_li.bsfslotmachine.BSSMView.NoScrollGridView;
import com.bs.john_li.bsfslotmachine.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 停車訂單界面
 * Created by John_Li on 23/9/2017.
 */

public class ParkingOrderActivity extends BaseActivity implements View.OnClickListener{
    private BSSMHeadView headView;
    private LinearLayout carManageLL, startTimeLL, endTimeLL, orderMoneyLL, orderCouponLL, orderRemarkLL, orderAreaLL, machinenoUnknowLL, orderColorLL, orderPhotoLL;
    private RelativeLayout carManageRL;
    public TextView warmPromptTv, carManagetv, carBrandTv, carTypeTv, carNumTv, orderLocationTv, orderMoneyTv, orderCouponTv, machinenoUnknowTv, remarkTv, areaTv, startTimeTv, endTimeTv, submitTv,meterColorTv, orderMoneyDiscountTv, orderAmountTv, orderShareTv;
    private ImageView carRecargeIv, parkingIv;
    public NoScrollGridView photoGv;

    private String way;
    // 是否是明天
    private boolean isTomorrow;
    private List<String> imgUrlList;
    private RatesModel mRatesModel;
    private CarModel.CarCountAndListModel.CarInsideModel mCarInsideModel;
    private List<TestCarListModel.CarModel> carInsideModelList;
    private SlotMachineListOutsideModel.SlotMachineListModel.SlotMachineModel mSlotMachineModel;
    private DiscountOutModel.DataBeanX.DiscountModel mDiscountModel;
    public SlotOrderModel mSlotOrderModel;//已知咪錶的訂單
    public SlotUnknowOrderModel mSlotUnknowOrderModel;  // 未知咪錶拍照時記得用saveinstans保存，完成之後還需把原來的數據擺回界面
    private PhotoAdapter mPhotoAdapter;
    private int amountLimit;
    private ImageOptions options = new ImageOptions.Builder().setSize(0, 0).setLoadingDrawableId(R.mipmap.img_loading).setFailureDrawableId(R.mipmap.load_img_fail_list).build();

    // 拍照的參數
    //private File file;  //照片文件
    private File dir; //圖片文件夾路徑
    private File fileUri;//照片文件路徑
    private Uri imageUri;//照片文件路徑
    private static final int CODE_GALLERY_REQUEST = 2;   //0xa0
    private static final int CODE_CAMERA_REQUEST = 1;    //0xa1
    private static final int CAMERA_PERMISSIONS_REQUEST_CODE = 0x03;
    private static final int STORAGE_PERMISSIONS_REQUEST_CODE = 0x04;
    private OSSClient oss;

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
        photoGv = findViewById(R.id.parking_order_gv);
        carManageLL = findViewById(R.id.parking_order_car_manage_ll);
        startTimeLL = findViewById(R.id.parking_order_starttime_ll);
        endTimeLL = findViewById(R.id.parking_order_endtime_ll);
        orderMoneyLL = findViewById(R.id.parking_order_money_ll);
        orderCouponLL = findViewById(R.id.parking_order_coupon_ll);
        orderRemarkLL = findViewById(R.id.parking_order_remark_ll);
        orderAreaLL = findViewById(R.id.parking_order_area_ll);
        orderColorLL = findViewById(R.id.parking_order_color_ll);
        orderPhotoLL = findViewById(R.id.parking_order_photo_ll);
        carManageRL = findViewById(R.id.parking_order_car_manage_rl);
        warmPromptTv = findViewById(R.id.parking_order_warm_prompt);
        carManagetv = findViewById(R.id.parking_order_car_manage_tv);
        carBrandTv = findViewById(R.id.parking_order_car_brand);
        carTypeTv = findViewById(R.id.parking_order_car_type);
        carNumTv = findViewById(R.id.parking_order_car_num);
        carRecargeIv = findViewById(R.id.parking_order_car_recharge);
        orderLocationTv = findViewById(R.id.parking_order_location_tv);
        orderMoneyTv = findViewById(R.id.parking_order_money_tv);
        orderCouponTv = findViewById(R.id.parking_order_coupon_tv);
        orderMoneyDiscountTv = findViewById(R.id.parking_order_discounts_tv);
        remarkTv = findViewById(R.id.parking_order_remark_tv);
        areaTv = findViewById(R.id.parking_order_area_tv);
        machinenoUnknowTv = findViewById(R.id.parking_order_machineno_tv);
        machinenoUnknowLL = findViewById(R.id.parking_order_machineno_ll);
        meterColorTv = findViewById(R.id.parking_order_color_tv);
        startTimeTv = findViewById(R.id.parking_order_starttime_tv);
        endTimeTv = findViewById(R.id.parking_order_endtime_tv);
        orderAmountTv = findViewById(R.id.parking_order_amount_tv);
        submitTv = findViewById(R.id.parking_order_submit);
        orderShareTv = findViewById(R.id.parking_order_share_tv);
        parkingIv = findViewById(R.id.parking_iv);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            headView.setHeadHight();
        }
    }

    @Override
    public void setListener() {
        carManageLL.setOnClickListener(this);
        startTimeLL.setOnClickListener(this);
        endTimeLL.setOnClickListener(this);
        //orderMoneyLL.setOnClickListener(this);
        orderRemarkLL.setOnClickListener(this);
        orderAreaLL.setOnClickListener(this);
        orderColorLL.setOnClickListener(this);
        machinenoUnknowLL.setOnClickListener(this);
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

        oss = AliyunOSSUtils.initOSS(this);

        // 車輛類
        carInsideModelList = new ArrayList<>();
        // 判断车辆是否选择车辆
        //isChooseCar();
        callNetGetCouponList();
        // 現在時間
        String startTime = (BSSMCommonUtils.getHour() + 1) + ":" + BSSMCommonUtils.getMinute() + ":00";
        String startTimeForDay = BSSMCommonUtils.getYear() + "-" + BSSMCommonUtils.getMonth() + "-" + BSSMCommonUtils.getDayOfMonth() + " " + startTime;
        Date date = new Date( );
        SimpleDateFormat fdt = new SimpleDateFormat ("hh:mm:ss");
        SimpleDateFormat yearFdt = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
        //startTimeTv.setText("投幣時間[預計" + startTime + "]");

        // 判斷是哪種打開方式
        Intent intent = getIntent();
        way = intent.getStringExtra("way");
        imgUrlList = new ArrayList<>();
        // 判斷訂單類別
        if (way.equals(BSSMConfigtor.SLOT_MACHINE_NOT_EXIST)) { // 咪錶不存在
            warmPromptTv.setVisibility(View.VISIBLE);
            String imgUri = intent.getStringExtra("imageUri");
            imgUrlList.add(imgUri);
            imgUrlList.add("");
            mPhotoAdapter = new PhotoAdapter(this, imgUrlList);
            // 是否是明天
            isTomorrow = intent.getBooleanExtra("isTomorrow", false);
            // 收費標準
            mRatesModel = new Gson().fromJson(intent.getStringExtra("RatesModel"), RatesModel.class);
            // 車輛
            mCarInsideModel = new Gson().fromJson(intent.getStringExtra("carModel"), CarModel.CarCountAndListModel.CarInsideModel.class);
            // 未知咪錶訂單
            mSlotUnknowOrderModel = new Gson().fromJson(intent.getStringExtra("unknowSlotOrder"), SlotUnknowOrderModel.class);

            orderLocationTv.setText("停車位置：未知");
            photoGv.setAdapter(mPhotoAdapter);
            photoGv.setVisibility(View.VISIBLE);
            orderPhotoLL.setVisibility(View.VISIBLE);
            orderAreaLL.setVisibility(View.VISIBLE);
            orderColorLL.setVisibility(View.VISIBLE);
            machinenoUnknowLL.setVisibility(View.VISIBLE);
            switch (mSlotUnknowOrderModel.getAreaCode()) {
                case "DT":
                    areaTv.setText("地      區：" + "大堂區");
                    break;
                case "WDT":
                    areaTv.setText("地      區：" + "望德堂區");
                    break;
                case "FST":
                    areaTv.setText("地      區：" + "風順堂區");
                    break;
                case "HWT":
                    areaTv.setText("地      區：" + "花王堂區");
                    break;
                case "HDMT":
                    areaTv.setText("地      區：" + "花地瑪堂區");
                    break;
                case "JMT":
                    areaTv.setText("地      區：" + "嘉模堂區");
                    break;
                case "QT":
                    areaTv.setText("地      區：" + "其他");
                    break;
            }
            switch (mSlotUnknowOrderModel.getPillarColor()) {
                case "gray":
                    meterColorTv.setText("咪錶顏色：" + "灰色");
                    break;
                case "green":
                    meterColorTv.setText("咪錶顏色：" + "綠色");
                    break;
                case "red":
                    meterColorTv.setText("咪錶顏色：" + "紅色");
                    break;
                case "yellow":
                    meterColorTv.setText("咪錶顏色：" + "黃色");
                    break;
            }
            remarkTv.setText("地        址：" + mSlotUnknowOrderModel.getRemark());
            startTimeTv.setText("開始投幣時間[預計"  + mSlotUnknowOrderModel.getStartSlotTime() + "]");
            endTimeTv.setText("結束投幣時間[預計" + mSlotUnknowOrderModel.getEndSlotTime() + "]");
            orderMoneyTv.setText("總金額：MOP" + mSlotUnknowOrderModel.getSlotAmount());
            orderAmountTv.setText("金額：MOP" + mSlotUnknowOrderModel.getSlotAmount());
            machinenoUnknowTv.setText("咪錶編號：" + mSlotUnknowOrderModel.getRemark().substring(0, 5));
            refreshCarChoosed();
        } else if (way.equals(BSSMConfigtor.SLOT_MACHINE_EXIST)){   //咪錶存在，無子列表
            /*warmPromptTv.setVisibility(View.GONE);
            // 停車訂單
            mSlotOrderModel = new SlotOrderModel();
            mSlotOrderModel.setStartSlotTime(startTimeForDay);
            mSlotOrderModel.setRemark("");

            mSlotMachineModel = new Gson().fromJson(intent.getStringExtra("SlotMachine"), SlotMachineListOutsideModel.SlotMachineListModel.SlotMachineModel.class);
            mSlotOrderModel.setMachineNo(mSlotMachineModel.getMachineNo());
            orderLocationTv.setText("停車位置：" + mSlotMachineModel.getAddress() + mSlotMachineModel.getMachineNo() + "號");
            orderAreaLL.setVisibility(View.GONE);
            orderColorLL.setVisibility(View.GONE);
            orderPhotoLL.setVisibility(View.GONE);
            machinenoUnknowLL.setVisibility(View.GONE);
            photoGv.setVisibility(View.GONE);
            if (BSSMCommonUtils.isLoginNow(ParkingOrderActivity.this)) {
                callNetGetMaxAmount(mSlotMachineModel.getMachineNo());
            } else {
                startActivityForResult(new Intent(ParkingOrderActivity.this, LoginActivity.class), BSSMConfigtor.LOGIN_FOR_RQUEST);
            }*/
        } else {    // 咪錶存在，有子列表
            warmPromptTv.setVisibility(View.GONE);
            // 子車位
            String childMachinePosition = intent.getStringExtra("childPosition");
            // 是否是明天
            isTomorrow = intent.getBooleanExtra("isTomorrow", false);
            // 收費標準
            mRatesModel = new Gson().fromJson(intent.getStringExtra("RatesModel"), RatesModel.class);
            // 車輛
            mCarInsideModel = new Gson().fromJson(intent.getStringExtra("carModel"), CarModel.CarCountAndListModel.CarInsideModel.class);
            // 停車訂單
            mSlotOrderModel = new Gson().fromJson(intent.getStringExtra("SlotOrder"), SlotOrderModel.class);
            //mSlotOrderModel.setStartSlotTime(startTimeForDay);
            //mSlotOrderModel.setRemark("");
            // 咪錶
            mSlotMachineModel = new Gson().fromJson(intent.getStringExtra("SlotMachine"), SlotMachineListOutsideModel.SlotMachineListModel.SlotMachineModel.class);
            //mSlotOrderModel.setMachineNo(mSlotMachineModel.getMachineNo());
            orderLocationTv.setText("停車位置：" + mSlotMachineModel.getAddress() + mSlotMachineModel.getMachineNo() + "號" + mSlotMachineModel.getParkingSpaces().get(Integer.parseInt(childMachinePosition)));
            //mSlotOrderModel.setParkingSpace(mSlotMachineModel.getParkingSpaces().get(Integer.parseInt(childMachinePosition)));
            startTimeTv.setText("開始投幣時間[預計"  + mSlotOrderModel.getStartSlotTime() + "]");
            endTimeTv.setText("結束投幣時間[預計" + mSlotOrderModel.getEndSlotTime() + "]");
            orderMoneyTv.setText("總金額：MOP" + mSlotOrderModel.getSlotAmount());
            orderAmountTv.setText("金額：MOP" + mSlotOrderModel.getSlotAmount());
            refreshCarChoosed();
            orderAreaLL.setVisibility(View.GONE);
            orderColorLL.setVisibility(View.GONE);
            orderPhotoLL.setVisibility(View.GONE);
            machinenoUnknowLL.setVisibility(View.GONE);
            photoGv.setVisibility(View.GONE);
            /*if (BSSMCommonUtils.isLoginNow(ParkingOrderActivity.this)) {
                callNetGetMaxAmount(mSlotMachineModel.getMachineNo());
            } else {
                startActivityForResult(new Intent(ParkingOrderActivity.this, LoginActivity.class), BSSMConfigtor.LOGIN_FOR_RQUEST);
            }*/
        }

        dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "BSSMPictures");
        if (!dir.exists()) {
            dir.mkdir();
        }
    }

    /**
     * 獲取優惠券列表
     */
    private void callNetGetCouponList() {
        RequestParams params = new RequestParams(BSSMConfigtor.BASE_URL + BSSMConfigtor.GET_USE_COUPON_NUMBER + SPUtils.get(this, "UserToken", ""));
        params.setAsJsonContent(true);
        params.setConnectTimeout(30 * 1000);
        x.http().request(HttpMethod.POST ,params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                CommonJieModel model = new Gson().fromJson(result.toString(), CommonJieModel.class);
                if (model.getCode() == 200) {
                    if (Integer.parseInt(model.getData()) == 0) {
                        orderShareTv.setVisibility(View.VISIBLE);
                        orderCouponTv.setText("紅包：暫無可用紅包");
                        orderCouponLL.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(new Intent(ParkingOrderActivity.this, ShareActivity.class));
                            }
                        });
                    } else {
                        orderShareTv.setVisibility(View.GONE);
                        orderCouponTv.setText("紅包：" + model.getData() + "個可用紅包");
                        orderCouponLL.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // 打開可用紅包列表
                                Intent intent = new Intent(ParkingOrderActivity.this, ChooseDiscountActivity.class);
                                if (way.equals(BSSMConfigtor.SLOT_MACHINE_NOT_EXIST)) { // 咪錶不存在，提交未知訂單
                                    intent.putExtra("orderModey", mSlotUnknowOrderModel.getSlotAmount());
                                } else {
                                    intent.putExtra("orderModey", mSlotOrderModel.getSlotAmount());
                                }
                                intent.putExtra("orderType", "smOrder");
                                intent.putExtra("CarInsideModel", new Gson().toJson(mCarInsideModel));
                                startActivityForResult(intent, 7);
                            }
                        });
                    }
                }
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

    @Override
    public void onClick(View view) {
        if (BSSMCommonUtils.isFastDoubleClick()) {
            return;
        }

        switch (view.getId()) {
            case R.id.head_left:
                finish();
                break;
            case R.id.parking_order_car_manage_ll:
                // 选择车辆
                Intent intent = new Intent(this, ChooseCarActivity.class);
                intent.putExtra("way", "ORDER_CHOOSE_CAR");
                startActivityForResult(intent, 6);
                break;
            case R.id.parking_order_starttime_ll:
                finish();
                //chooseOrderStartTime();
                break;
            case R.id.parking_order_endtime_ll:
                finish();
                //chooseOrderEndTime();
                break;
            case R.id.parking_order_money_ll:
                //chooseOrderMoney();
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
            case R.id.parking_order_machineno_ll:
                editUnkowMachineno();
                break;
            case R.id.parking_order_submit:
                LoadDialog loadDialog = new LoadDialog(this, false, "提交中......");
                loadDialog.show();
                if (way.equals(BSSMConfigtor.SLOT_MACHINE_NOT_EXIST)) { // 咪錶不存在，提交未知訂單
                    if (mSlotUnknowOrderModel.getSlotAmount() != null && mSlotUnknowOrderModel.getStartSlotTime() != null && mSlotUnknowOrderModel.getRemark() != null && mSlotUnknowOrderModel.getUnknowMachineno() != null) {
                        if (mSlotUnknowOrderModel.getUnknowMachineno().length() == 6) {
                            if (!mSlotUnknowOrderModel.getSlotAmount().equals("0")) {
                                submitImgToOss(loadDialog);
                            } else {
                                Toast.makeText(this, "請選擇訂單金額~", Toast.LENGTH_SHORT).show();
                                loadDialog.dismiss();
                            }
                        } else {
                            Toast.makeText(this, "請填寫六位數咪錶編號~", Toast.LENGTH_SHORT).show();
                            loadDialog.dismiss();
                        }
                    } else {
                        Toast.makeText(this, "請填寫全訂單信息~", Toast.LENGTH_SHORT).show();
                        loadDialog.dismiss();
                    }
                } else {    // 咪錶存在，提交已知訂單
                    if (mSlotOrderModel.getMachineNo() != null && mSlotOrderModel.getCarId() != 0 && mSlotOrderModel.getStartSlotTime() != null && mSlotOrderModel.getEndSlotTime() != null && mSlotOrderModel.getSlotAmount() != null) {
                        if (!mSlotOrderModel.getSlotAmount().equals("0")) {
                            if (BSSMCommonUtils.isLoginNow(ParkingOrderActivity.this)) {
                                submitOrderSlotMachineExist(loadDialog);
                            }  else {
                                startActivityForResult(new Intent(ParkingOrderActivity.this, LoginActivity.class), BSSMConfigtor.LOGIN_FOR_RQUEST);
                            }
                        } else {
                            Toast.makeText(this, "請選擇訂單金額~", Toast.LENGTH_SHORT).show();
                            loadDialog.dismiss();
                        }
                    } else {
                        Toast.makeText(this, "請填寫全訂單信息~", Toast.LENGTH_SHORT).show();
                        loadDialog.dismiss();
                    }
                }
                break;
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
                    Toast.makeText(ParkingOrderActivity.this, "您已经拒绝过一次", Toast.LENGTH_SHORT).show();
                }
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, CAMERA_PERMISSIONS_REQUEST_CODE);
            } else {//有权限直接调用系统相机拍照
                SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
                Date date = new Date(System.currentTimeMillis());
                fileUri = new File(dir.getPath() + "/order" + format.format(date) + ".jpg");
                imageUri = Uri.fromFile(fileUri);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                    imageUri = FileProvider.getUriForFile(ParkingOrderActivity.this, "com.bs.john_li.bsfslotmachine" + ".fileprovider", fileUri);//通过FileProvider创建一个content类型的Uri
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
        /*if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSIONS_REQUEST_CODE);
        } else {
            PhotoUtils.openPic(this, CODE_GALLERY_REQUEST);
        }*/

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
                        imageUri = FileProvider.getUriForFile(ParkingOrderActivity.this, "com.bs.john_li.bsfslotmachine" + ".fileprovider", fileUri);//通过FileProvider创建一个content类型的Uri
                    PhotoUtils.takePicture(this, imageUri, CODE_CAMERA_REQUEST);
                } else {
                    Toast.makeText(ParkingOrderActivity.this, "请允许打开相机", Toast.LENGTH_SHORT).show();
                }
                break;


            }
            case STORAGE_PERMISSIONS_REQUEST_CODE://调用系统相册申请Sdcard权限回调
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    PhotoUtils.openPic(this, CODE_GALLERY_REQUEST);
                } else {
                    Toast.makeText(ParkingOrderActivity.this, "请允许打操作SDCard", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    /**
     * 提交位置咪錶的照片
     * @param loadDialog
     */
    private void submitImgToOss(final LoadDialog loadDialog) {
        // 提交成功的集合
        // 照片數組
        final String[] imgArr = new String[imgUrlList.size() - 1];
        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 1:
                        putNum = 0;
                        // 提交未知咪錶的訂單
                        if (BSSMCommonUtils.isLoginNow(ParkingOrderActivity.this)) {
                            submitOrderSlotMachineUnKnow(loadDialog);
                        } else {
                            startActivityForResult(new Intent(ParkingOrderActivity.this, LoginActivity.class), BSSMConfigtor.LOGIN_FOR_RQUEST);
                        }
                        break;
                }
            }
        };
        putImg(imgArr, handler);
    }

    /**
     * 提交未知咪錶的訂單
     */
    private void submitOrderSlotMachineUnKnow(final LoadDialog loadDialog) {
        RequestParams params = new RequestParams(BSSMConfigtor.BASE_URL + BSSMConfigtor.SUBMIT_ORDER_SLOT_MACHINE_UNKOWN + SPUtils.get(this, "UserToken", ""));
        params.setAsJsonContent(true);
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("slotAmount",mSlotUnknowOrderModel.getSlotAmount());
            jsonObj.put("carId",mSlotUnknowOrderModel.getCarId());
            jsonObj.put("carType", mSlotUnknowOrderModel.getCarType());//mSlotUnknowOrderModel.getCarType()
            jsonObj.put("pillarColor",mSlotUnknowOrderModel.getPillarColor());
            jsonObj.put("areaCode",mSlotUnknowOrderModel.getAreaCode());
            jsonObj.put("startSlotTime",mSlotUnknowOrderModel.getStartSlotTime());
            jsonObj.put("endSlotTime",mSlotUnknowOrderModel.getEndSlotTime());
            jsonObj.put("remark", "咪錶編號：" + mSlotUnknowOrderModel.getUnknowMachineno() + "，地址：" + mSlotUnknowOrderModel.getRemark());
            if (mDiscountModel != null) {
                jsonObj.put("couponId", mDiscountModel.getCouponId());
            }
            //jsonObj.put("imgUrls", BSSMCommonUtils.getJSONArrayByList(BSSMCommonUtils.deleteDirName(imgUrlList)));
            List<String> cachList = new ArrayList();
            for (int i = 0; i < imgUrlList.size(); i++) {
                if (i != imgUrlList.size() - 1) {
                    File fileUri = new File(imgUrlList.get(i));
                    cachList.add(fileUri.getName());
                }
            }
            jsonObj.put("imgUrls", BSSMCommonUtils.getJSONArrayByList(cachList));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        params.setBodyContent(jsonObj.toString());
        String uri = params.getUri();
        params.setConnectTimeout(30 * 1000);
        x.http().request(HttpMethod.POST ,params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                OrderModel model = new Gson().fromJson(result.toString(), OrderModel.class);
                if (model.getCode() == 200) {
                    String orderNo = model.getData().getOrderNo();
                    Intent intent = new Intent(ParkingOrderActivity.this, PaymentAcvtivity.class);
                    intent.putExtra("startWay", 1);   // parkingOrder
                    intent.putExtra("orderNo", model.getData().getOrderNo());
                    intent.putExtra("amount", model.getData().getAmount());
                    intent.putExtra("createTime", model.getData().getCreateTime());
                    intent.putExtra("exchange", model.getData().getExchange());
                    intent.putExtra("exchangeAmountPay", model.getData().getExchangeAmountPay());
                    startActivityForResult(intent, 4);
                } else if (model.getCode() == 10000) {
                    SPUtils.put(ParkingOrderActivity.this, "UserToken", "");
                    startActivityForResult(new Intent(ParkingOrderActivity.this, LoginActivity.class), BSSMConfigtor.LOGIN_FOR_RQUEST);
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
                loadDialog.dismiss();
            }
        });
    }

    /**
     * 提交已知咪錶訂單
     */
    private void submitOrderSlotMachineExist(final LoadDialog loadDialog) {
        RequestParams params = new RequestParams(BSSMConfigtor.BASE_URL + BSSMConfigtor.SUBMIT_ORDER_SLOT_MACHINE_EXIST + SPUtils.get(this, "UserToken", ""));
        params.setAsJsonContent(true);
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("machineNo",mSlotOrderModel.getMachineNo());
            jsonObj.put("carId",mSlotOrderModel.getCarId());
            jsonObj.put("startSlotTime",mSlotOrderModel.getStartSlotTime());
            jsonObj.put("endSlotTime",mSlotOrderModel.getEndSlotTime());
            jsonObj.put("remark",mSlotOrderModel.getRemark());
            jsonObj.put("parkingSpace",mSlotOrderModel.getParkingSpace());
            if (mDiscountModel != null) {
                jsonObj.put("couponId", mDiscountModel.getCouponId());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        params.setBodyContent(jsonObj.toString());
        String uri = params.getUri();
        x.http().request(HttpMethod.POST ,params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                OrderModel model = new Gson().fromJson(result.toString(), OrderModel.class);
                if (model.getCode() == 200) {
                    Intent intent = new Intent(ParkingOrderActivity.this, PaymentAcvtivity.class);
                    intent.putExtra("startWay", 1);   // parkingOrder
                    intent.putExtra("orderNo", model.getData().getOrderNo());
                    intent.putExtra("amount", model.getData().getAmount());
                    intent.putExtra("createTime", model.getData().getCreateTime());
                    intent.putExtra("exchange", model.getData().getExchange());
                    intent.putExtra("exchangeAmountPay", model.getData().getExchangeAmountPay());
                    startActivityForResult(intent, 3);
                } else if (model.getCode() == 10000) {
                    SPUtils.put(ParkingOrderActivity.this, "UserToken", "");
                    startActivityForResult(new Intent(ParkingOrderActivity.this, LoginActivity.class), BSSMConfigtor.LOGIN_FOR_RQUEST);
                } else {
                    Toast.makeText(ParkingOrderActivity.this, "訂單提交失敗," + String.valueOf(model.getMsg()), Toast.LENGTH_SHORT).show();
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
                loadDialog.dismiss();
            }
        });
    }

    /**
     * 刷新車輛信息
     */
    private void refreshCarChoosed() {
        carManagetv.setText("車輛管理");
        carManageRL.setVisibility(View.VISIBLE);
        //carBrandTv.setText("品牌：" + carInsideModelList.get(0).getCarBrand());
        switch (mCarInsideModel.getIfPerson()) {
            case 1:
                carTypeTv.setText("車     型：輕重型電單車");
                break;
            case 2:
                carTypeTv.setText("車     型：輕型汽車");
                break;
            case 3:
                carTypeTv.setText("車     型：重型汽車");
                break;
        }
        carNumTv.setText("車牌號：" + mCarInsideModel.getCarNo());
        //AliyunOSSUtils.downloadImg(carInsideModelList.get(0).getImgUrl(), AliyunOSSUtils.initOSS(this), parkingIv, this, R.mipmap.load_img_fail_list);
        x.image().bind(parkingIv, mCarInsideModel.getImgUrl(), options);

        if (mCarInsideModel.getVipType() == 0) {
            if (mCarInsideModel.getIfPay() == 0) {
                carRecargeIv.setImageResource(R.mipmap.recharge);
            } else {
                carRecargeIv.setImageResource(R.mipmap.member);
            }
        } else if (mCarInsideModel.getVipType() == 1) {   // 日費費
            carRecargeIv.setImageResource(R.mipmap.member);
        } else if (mCarInsideModel.getVipType() == 2) {   // 月費
            carRecargeIv.setImageResource(R.mipmap.recharge_mouth);
        } else if (mCarInsideModel.getVipType() == 3) {   // 季度費
            carRecargeIv.setImageResource(R.mipmap.recharge_quarter);
        } else if (mCarInsideModel.getVipType() == 4) {   // 半年費
            carRecargeIv.setImageResource(R.mipmap.recharge_halfyear);
        } else if (mCarInsideModel.getVipType() == 5) {   // 年費
            carRecargeIv.setImageResource(R.mipmap.year);
        } else {
            carRecargeIv.setImageResource(R.mipmap.member);
        }

        if (way.equals(BSSMConfigtor.SLOT_MACHINE_NOT_EXIST)) { // 咪錶不存在，提交未知訂單
            mSlotUnknowOrderModel.setCarId(String.valueOf(mCarInsideModel.getId()));
        } else {
            mSlotOrderModel.setCarId(mCarInsideModel.getId());
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
                    public void convertView(final ViewHolder holder, final BaseNiceDialog dialog) {
                        final TextView timeTv = holder.getView(R.id.dialog_time_tv);
                        TextView submitTv = holder.getView(R.id.dialog_time_submit);
                        TimePicker timePicker = holder.getView(R.id.dialog_time_picker);
                        timePicker.setIs24HourView(true);
                        if (way.equals(BSSMConfigtor.SLOT_MACHINE_NOT_EXIST)) { // 咪錶不存在
                            timePicker.setCurrentHour(Integer.parseInt(mSlotUnknowOrderModel.getStartSlotTime().substring(11, 13)));
                            timePicker.setCurrentMinute(Integer.parseInt(mSlotUnknowOrderModel.getStartSlotTime().substring(14, 16)));
                            timeTv.setText("開始投幣時間[預計" + mSlotUnknowOrderModel.getStartSlotTime()+ "]");
                        } else {
                            String start = mSlotOrderModel.getStartSlotTime();
                            timePicker.setCurrentHour(Integer.parseInt(mSlotOrderModel.getStartSlotTime().substring(11, 13)));
                            timePicker.setCurrentMinute(Integer.parseInt(mSlotOrderModel.getStartSlotTime().substring(14, 16)));
                            timeTv.setText("開始投幣時間[預計" + mSlotOrderModel.getStartSlotTime()+ "]");
                        }
                        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
                            @Override
                            public void onTimeChanged(TimePicker timePicker, int hourOfDay, int minute) {
                                boolean v = timePicker.is24HourView();
                                if (hourOfDay < BSSMCommonUtils.getHour() + 1 || minute < BSSMCommonUtils.getMinute()) {
                                    timePicker.setCurrentHour(BSSMCommonUtils.getHour() + 1);
                                    timePicker.setCurrentMinute(BSSMCommonUtils.getMinute());
                                    Toast.makeText(ParkingOrderActivity.this, "請選擇一個小時之後的時間", Toast.LENGTH_LONG).show();
                                } else {
                                    if (hourOfDay > 20 || hourOfDay < 9) {
                                        timePicker.setCurrentHour(BSSMCommonUtils.getHour() + 1);
                                        timePicker.setCurrentMinute(BSSMCommonUtils.getMinute());
                                        Toast.makeText(ParkingOrderActivity.this, "請選擇規定的時間(9:00-20:00)之間下單！", Toast.LENGTH_LONG).show();
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
                                        if (way.equals(BSSMConfigtor.SLOT_MACHINE_NOT_EXIST)){
                                            mSlotUnknowOrderModel.setStartSlotTime(mSlotOrderModel.getStartSlotTime().substring(0,10) + " " +time);
                                            timeTv.setText("開始投幣時間[預計" + mSlotUnknowOrderModel.getStartSlotTime() + "]");
                                            startTimeTv.setText("開始投幣時間[預計" + mSlotUnknowOrderModel.getStartSlotTime() + "]");
                                        } else {
                                            mSlotOrderModel.setStartSlotTime(mSlotOrderModel.getStartSlotTime().substring(0,10) + " " +time);
                                            timeTv.setText("開始投幣時間[預計" + mSlotOrderModel.getStartSlotTime() + "]");
                                            startTimeTv.setText("開始投幣時間[預計" + mSlotOrderModel.getStartSlotTime() + "]");
                                        }
                                    }
                                }

                                chooseOrderMoney();
                            }
                        });

                        holder.setOnClickListener(R.id.dialog_time_submit, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });
                    }
                })
                .setWidth(285)
                .show(getSupportFragmentManager());
    }

    /**
     * 选择订单結束时间
     */
    private void chooseOrderEndTime() {
        NiceDialog.init()
                .setLayoutId(R.layout.dialog_choose_time)
                .setConvertListener(new ViewConvertListener() {
                    @Override
                    public void convertView(final ViewHolder holder, final BaseNiceDialog dialog) {
                        final TextView timeTv = holder.getView(R.id.dialog_time_tv);
                        TextView submitTv = holder.getView(R.id.dialog_time_submit);
                        TimePicker timePicker = holder.getView(R.id.dialog_time_picker);
                        timePicker.setIs24HourView(true);
                        if (way.equals(BSSMConfigtor.SLOT_MACHINE_NOT_EXIST)) {
                            timePicker.setCurrentHour(Integer.parseInt(mSlotUnknowOrderModel.getEndSlotTime().substring(11, 13)));
                            timePicker.setCurrentMinute(Integer.parseInt(mSlotUnknowOrderModel.getEndSlotTime().substring(14, 16)));
                            timeTv.setText("結束投幣時間[預計" + mSlotUnknowOrderModel.getEndSlotTime() + "]");
                        } else {
                            timePicker.setCurrentHour(Integer.parseInt(mSlotOrderModel.getEndSlotTime().substring(11, 13)));
                            timePicker.setCurrentMinute(Integer.parseInt(mSlotOrderModel.getEndSlotTime().substring(14, 16)));
                            timeTv.setText("結束投幣時間[預計" + mSlotOrderModel.getEndSlotTime() + "]");
                        }
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
                                String endSlotTime = "";
                                String time = hourTime + ":" + minuteTime + ":00";
                                if (isTomorrow) {
                                    endSlotTime = BSSMCommonUtils.getTomorrowDate();
                                } else {
                                    endSlotTime = BSSMCommonUtils.getTodayDate();
                                }
                                try {
                                    if (way.equals(BSSMConfigtor.SLOT_MACHINE_NOT_EXIST)){
                                        if (BSSMCommonUtils.compareTwoTime(endSlotTime + " " +time, mSlotUnknowOrderModel.getStartSlotTime())){  // 判斷不小於開始時間
                                            Toast.makeText(ParkingOrderActivity.this, "結束投幣時間不可小於開始投幣時間！", Toast.LENGTH_SHORT).show();
                                        } else {
                                            mSlotUnknowOrderModel.setEndSlotTime(endSlotTime + " " +time);
                                        }
                                    } else {
                                        if (BSSMCommonUtils.compareTwoTime(endSlotTime + " " +time, mSlotOrderModel.getStartSlotTime())){  // 判斷不小於開始時間
                                            Toast.makeText(ParkingOrderActivity.this, "結束投幣時間不可小於開始投幣時間！", Toast.LENGTH_SHORT).show();
                                        } else {
                                            mSlotOrderModel.setEndSlotTime(endSlotTime + " " +time);
                                        }
                                    }
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                    Toast.makeText(ParkingOrderActivity.this, "時間格式錯誤", Toast.LENGTH_SHORT).show();
                                }

                                chooseOrderMoney();
                            }
                        });

                        holder.setOnClickListener(R.id.dialog_time_submit, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });
                    }
                })
                .setWidth(285)
                .show(getSupportFragmentManager());
    }
    /**
     * 計算订单金额
     */
    private void chooseOrderMoney() {
        if (mRatesModel != null) {
            double orderAmount = 0.0;
            double hourCost = new BigDecimal(mRatesModel.getData().getHourCost()).divide(new BigDecimal(60), 4, BigDecimal.ROUND_UP).doubleValue(); //
            double noVipHoursPay = new BigDecimal(mRatesModel.getData().getNoVipHoursPay()).divide(new BigDecimal(60), 4, BigDecimal.ROUND_UP).doubleValue();


            if (way.equals(BSSMConfigtor.SLOT_MACHINE_NOT_EXIST)) {
                // 未知咪錶
                long timeDiff = BSSMCommonUtils.compareTimestamps(mSlotUnknowOrderModel.getStartSlotTime(), mSlotUnknowOrderModel.getEndSlotTime());
                if (mCarInsideModel.getIfPay() == 0) {    // 非會員
                    try {
                        if (BSSMCommonUtils.compareTwoTime(mSlotUnknowOrderModel.getStartSlotTime(), BSSMCommonUtils.getYear() + "-" + BSSMCommonUtils.getMonth() + "-" + BSSMCommonUtils.getDayOfMonth() + " 09:30:00")) {
                            orderAmount = Math.ceil(Math.round((hourCost + noVipHoursPay) * (timeDiff + 60)*100)/100.00);    // 小於早上九點半
                        } else {
                            orderAmount = Math.ceil(Math.round((hourCost + noVipHoursPay) * timeDiff*100)/100.00);    // 大於早上九點半
                        }
                        mSlotUnknowOrderModel.setSlotAmount(String.valueOf(orderAmount));
                        orderMoneyTv.setText("總金額：MOP" + mSlotUnknowOrderModel.getSlotAmount());
                        orderAmountTv.setText("金額：MOP" + mSlotUnknowOrderModel.getSlotAmount());
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
                        orderMoneyTv.setText("總金額：MOP" + mSlotUnknowOrderModel.getSlotAmount());
                        orderAmountTv.setText("金額：MOP" + mSlotUnknowOrderModel.getSlotAmount());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                long timeDiff = BSSMCommonUtils.compareTimestamps(mSlotOrderModel.getStartSlotTime(), mSlotOrderModel.getEndSlotTime());
                if (mCarInsideModel.getIfPay() == 0) {    // 非會員
                    try {
                        if (BSSMCommonUtils.compareTwoTime(mSlotOrderModel.getStartSlotTime(), BSSMCommonUtils.getYear() + "-" + BSSMCommonUtils.getMonth() + "-" + BSSMCommonUtils.getDayOfMonth() + " 09:30:00")) {
                            orderAmount = Math.ceil(Math.round((hourCost + noVipHoursPay) * (timeDiff + 60)*10000)/10000.0000);    // 小於早上九點半
                        } else {
                            orderAmount = Math.ceil(Math.round((hourCost + noVipHoursPay) * timeDiff*10000)/10000.0000);    // 大於早上九點半
                        }
                        mSlotOrderModel.setSlotAmount(String.valueOf(orderAmount));
                        orderMoneyTv.setText("總金額：MOP" + mSlotOrderModel.getSlotAmount());
                        orderAmountTv.setText("金額：MOP" + mSlotOrderModel.getSlotAmount());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } else {    // 會員
                    try {
                        String time930 = BSSMCommonUtils.getYear() + "-" + BSSMCommonUtils.getMonth() + "-" + BSSMCommonUtils.getDayOfMonth() + " 09:30:00";
                        if (BSSMCommonUtils.compareTwoTime(mSlotOrderModel.getStartSlotTime(), time930)) {
                            orderAmount = Math.ceil(Math.round(hourCost * (timeDiff + 60)*10000)/10000.0000);// 小於早上九點半
                        } else {
                            orderAmount = Math.ceil(Math.round(hourCost * timeDiff*10000)/10000.0000);     // 大於早上九點半
                        }
                        mSlotOrderModel.setSlotAmount(String.valueOf(orderAmount));
                        orderMoneyTv.setText("總金額：MOP" + mSlotOrderModel.getSlotAmount());
                        orderAmountTv.setText("金額：MOP" + mSlotOrderModel.getSlotAmount());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * 填写未知咪錶訂單的咪錶編號
     */
    private void editUnkowMachineno() {
        NiceDialog.init()
                .setLayoutId(R.layout.dialog_car_edit)
                .setConvertListener(new ViewConvertListener() {
                    @Override
                    public void convertView(ViewHolder holder, final BaseNiceDialog dialog) {
                        final EditText editText = holder.getView(R.id.car_edit);
                        editText.setHint("請填寫六位數咪錶編號(例如：咪錶號2225，車位06則填寫222506！)");
                        BSSMCommonUtils.showKeyboard(editText);
                        holder.setOnClickListener(R.id.car_edit_submit, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (editText.getText().toString().length() == 6) {
                                    mSlotUnknowOrderModel.setUnknowMachineno(editText.getText().toString());
                                    machinenoUnknowTv.setText("咪錶編號：" + editText.getText().toString());
                                    dialog.dismiss();
                                } else {
                                    Toast.makeText(ParkingOrderActivity.this, "請填寫六位數咪錶編號(例如：咪錶號2225，車位06則填寫222506！)", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                })
                .setShowBottom(true)
                .show(getSupportFragmentManager());
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
                            editText.setHint("請填寫地址(備註內容中請務必包含詳細地址！)");
                        } else {
                            editText.setHint("請填寫備註");
                        }
                        BSSMCommonUtils.showKeyboard(editText);
                        holder.setOnClickListener(R.id.car_edit_submit, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (way.equals(BSSMConfigtor.SLOT_MACHINE_NOT_EXIST)){
                                    mSlotUnknowOrderModel.setRemark(editText.getText().toString());
                                    remarkTv.setText("地      址：" + editText.getText().toString());
                                } else {
                                    mSlotOrderModel.setRemark(editText.getText().toString());
                                    remarkTv.setText("備      註：" + editText.getText().toString());
                                }
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
                        viewHolder.setOnClickListener(R.id.meter_gray, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                meterColorTv.setText("咪錶顏色：" + "灰色");
                                mSlotUnknowOrderModel.setPillarColor("gray");
                                isAreaAndColorChoose();
                                baseNiceDialog.dismiss();
                            }
                        });
                        viewHolder.setOnClickListener(R.id.meter_green, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                meterColorTv.setText("咪錶顏色：" + "綠色");
                                mSlotUnknowOrderModel.setPillarColor("green");
                                isAreaAndColorChoose();
                                baseNiceDialog.dismiss();
                            }
                        });
                        viewHolder.setOnClickListener(R.id.meter_red, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                meterColorTv.setText("咪錶顏色：" + "紅色");
                                mSlotUnknowOrderModel.setPillarColor("red");
                                isAreaAndColorChoose();
                                baseNiceDialog.dismiss();
                            }
                        });
                        viewHolder.setOnClickListener(R.id.meter_yellow, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                meterColorTv.setText("咪錶顏色：" + "黃色");
                                mSlotUnknowOrderModel.setPillarColor("yellow");
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
                                mSlotUnknowOrderModel.setAreaCode("DT");
                                isAreaAndColorChoose();
                                baseNiceDialog.dismiss();
                            }
                        });
                        viewHolder.setOnClickListener(R.id.order_area_wangde_tv, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                areaTv.setText("地      區：" + "望德堂區");
                                mSlotUnknowOrderModel.setAreaCode("WDT");
                                isAreaAndColorChoose();
                                baseNiceDialog.dismiss();
                            }
                        });
                        viewHolder.setOnClickListener(R.id.order_area_fengshun_tv, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                areaTv.setText("地      區：" + "風順堂區");
                                mSlotUnknowOrderModel.setAreaCode("FST");
                                isAreaAndColorChoose();
                                baseNiceDialog.dismiss();
                            }
                        });
                        viewHolder.setOnClickListener(R.id.order_area_huawang_tv, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                areaTv.setText("地      區：" + "花王堂區");
                                mSlotUnknowOrderModel.setAreaCode("HWT");
                                isAreaAndColorChoose();
                                baseNiceDialog.dismiss();
                            }
                        });
                        viewHolder.setOnClickListener(R.id.order_area_huadima_tv, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                areaTv.setText("地      區：" + "花地瑪堂區");
                                mSlotUnknowOrderModel.setAreaCode("HDMT");
                                isAreaAndColorChoose();
                                baseNiceDialog.dismiss();
                            }
                        });
                        viewHolder.setOnClickListener(R.id.order_area_jiamo_tv, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                areaTv.setText("地      區：" + "嘉模堂區");
                                mSlotUnknowOrderModel.setAreaCode("JMT");
                                isAreaAndColorChoose();
                                baseNiceDialog.dismiss();
                            }
                        });
                        viewHolder.setOnClickListener(R.id.order_area_other_tv, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                areaTv.setText("地      區：" + "其他");
                                mSlotUnknowOrderModel.setAreaCode("QT");
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
     * 未知咪錶選擇區域和咪錶顏色重新計算計費標準
     */
    public void isAreaAndColorChoose() {
        callNetGetRates();
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
                    chooseOrderMoney();
                } else if (mRatesModel.getCode() == 10000) {
                    SPUtils.put(ParkingOrderActivity.this, "UserToken", "");
                    startActivityForResult(new Intent(ParkingOrderActivity.this, LoginActivity.class), BSSMConfigtor.LOGIN_FOR_RQUEST);
                } else {
                    Toast.makeText(ParkingOrderActivity.this, "獲取收費標準失敗請重新提交", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(ParkingOrderActivity.this, "獲取收費標準失敗請重新提交", Toast.LENGTH_SHORT).show();
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 7: // 選擇優惠券
                    mDiscountModel = new Gson().fromJson(data.getStringExtra("couponModel"), DiscountOutModel.DataBeanX.DiscountModel.class);
                    orderCouponTv.setText("紅包：-MOP" + mDiscountModel.getCouponValue());
                    orderMoneyDiscountTv.setText("|已優惠：" + mDiscountModel.getCouponValue() + ".00");
                    if (way.equals(BSSMConfigtor.SLOT_MACHINE_NOT_EXIST)) {
                        orderAmountTv.setText("金額：MOP" + (Double.parseDouble(mSlotUnknowOrderModel.getSlotAmount()) - (double)mDiscountModel.getCouponValue()));
                    } else {
                        orderAmountTv.setText("金額：MOP" + (Double.parseDouble(mSlotOrderModel.getSlotAmount()) - (double)mDiscountModel.getCouponValue()));
                    }
                    break;
                case 6: // 選擇車輛
                    mCarInsideModel = new Gson().fromJson(data.getStringExtra("carModel"), CarModel.CarCountAndListModel.CarInsideModel.class);
                    refreshCarChoosed();
                    // 重新計算金額
                    chooseOrderMoney();
                    break;
                case CODE_CAMERA_REQUEST:
                    Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    Uri contentUri = Uri.fromFile(fileUri);
                    mediaScanIntent.setData(contentUri);
                    sendBroadcast(mediaScanIntent);
                    imgUrlList.add(0, fileUri.getPath());
                    mPhotoAdapter.refreshData(imgUrlList);
                    break;
                case CODE_GALLERY_REQUEST:
                    String imagePath = BSSMCommonUtils.getRealFilePath(this, data.getData());
                    fileUri = new File(imagePath);
                    imgUrlList.add(0, fileUri.getPath());
                    mPhotoAdapter.refreshData(imgUrlList);
                    break;
                case 3:
                    finish();
                    break;
                case 4:
                    finish();
                    break;
            }
        }
    }

    // 提交的照片數量
    private int putNum;

    /**
     * 上傳圖片到OSS
     */
    private void putImg(final String[] imgArr, final Handler handler) {
        putNum ++;
        if (putNum == imgUrlList.size() || imgUrlList.get(putNum - 1).equals("")) {
            // 结束的处理逻辑，并退出该方法
            return;
        }

        Bitmap bitmap = BSSMCommonUtils.compressImageFromFile(imgUrlList.get(putNum - 1), 1024f);// 按尺寸压缩图片
        File filePut = BSSMCommonUtils.compressImage(bitmap, imgUrlList.get(putNum - 1));  //按质量压缩图片

        final String fileName = filePut.getName();
        String filePath = filePut.getPath();
        // 构造上传请求
        final PutObjectRequest put = new PutObjectRequest(BSSMConfigtor.BucketName, fileName, filePath);

        // 異步請求
        OSSAsyncTask task = oss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                Message msg = new Message();
                msg.what  = 0;
                imgArr[putNum - 1] = BSSMConfigtor.OSS_SERVER_CALLBACK_ADDRESS + fileName;
                /*"http://test-pic-666.oss-cn-hongkong.aliyuncs.com/" +*/
                // 这里进行递归单张图片上传，在外面判断是否进行跳出， 最後一張的添加圖片的空路徑所以-2
                if (putNum <= imgUrlList.size() - 2) {
                    putImg(imgArr, handler);
                } else {
                    Message message = new Message();
                    message.what = 1;
                    handler.sendMessage(message);
                }
            }

            @Override
            public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                // 请求异常
                if (clientExcepion != null) {
                    // 本地异常如网络异常等
                    clientExcepion.printStackTrace();
                }
                if (serviceException != null) {
                    // 服务异常
                    Log.e("ErrorCode", serviceException.getErrorCode());
                    Log.e("RequestId", serviceException.getRequestId());
                    Log.e("HostId", serviceException.getHostId());
                    Log.e("RawMessage", serviceException.getRawMessage());
                }

                putNum = 0;
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "BSSMPictures");
        if (fileUri != null){
            outState.putString("file_path", fileUri.getPath());
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
        if (savedInstanceState.get("file_path") != null) {
            String cacheFileName = savedInstanceState.get("file_path").toString();
            if (!cacheFileName.equals("")) {
                fileUri = new File(cacheFileName);
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
