package com.bs.john_li.bsfslotmachine.BSSMActivity.CarService;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bs.john_li.bsfslotmachine.BSSMActivity.BaseActivity;
import com.bs.john_li.bsfslotmachine.BSSMActivity.Parking.PaymentAcvtivity;
import com.bs.john_li.bsfslotmachine.BSSMAdapter.MerchartSetAdapter;
import com.bs.john_li.bsfslotmachine.BSSMAdapter.SecondCarOptionListAdapter;
import com.bs.john_li.bsfslotmachine.BSSMModel.CarBrandOutModel;
import com.bs.john_li.bsfslotmachine.BSSMModel.MerchatSetOutModel;
import com.bs.john_li.bsfslotmachine.BSSMModel.PlaceCarWashOrderOutModel;
import com.bs.john_li.bsfslotmachine.BSSMUtils.BSSMCommonUtils;
import com.bs.john_li.bsfslotmachine.BSSMUtils.BSSMConfigtor;
import com.bs.john_li.bsfslotmachine.BSSMUtils.SPUtils;
import com.bs.john_li.bsfslotmachine.BSSMView.BSSMHeadView;
import com.bs.john_li.bsfslotmachine.BSSMView.NoScrollListView;
import com.bs.john_li.bsfslotmachine.R;
import com.google.android.gms.appindexing.Thing;
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

import java.net.SocketTimeoutException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by John on 14/11/2018.
 */

public class MerchatSetActivity extends BaseActivity implements View.OnClickListener{
    private BSSMHeadView headView;
    private ImageView merchartIv, telIv;
    private TextView nameTv, businessHoursTv, soldTv, addressTv, orderAmountTv, submitTv;
    private LinearLayout addressLL;
    private NoScrollListView merchartSetLv;
    private ProgressDialog dialog;

    private int merchatId;
    //定位都要通过LocationManager这个类实现
    private LocationManager locationManager;
    private Location mLocation;
    private String provider;
    private MerchartSetAdapter merchartSetAdapter;
    private MerchatSetOutModel.MerchatSetModel merchatSetModel;
    private List<MerchatSetOutModel.MerchatSetModel.SellerChargeBean> setList;
    private ImageOptions options = new ImageOptions.Builder().setSize(0, 0).setLoadingDrawableId(R.mipmap.img_loading_list).setFailureDrawableId(R.mipmap.load_img_fail).build();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchat_set);
        initView();
        setListener();
        initData();
    }

    @Override
    public void initView() {
        headView = findViewById(R.id.merchar_set_head);
        merchartIv = findViewById(R.id.merchart_iv);
        telIv = findViewById(R.id.merchart_tel_ll);
        nameTv = findViewById(R.id.merchart_name_tv);
        businessHoursTv = findViewById(R.id.merchart_business_hours_tv);
        soldTv = findViewById(R.id.merchart_sold_tv);
        addressTv = findViewById(R.id.merchart_address_tv);
        orderAmountTv = findViewById(R.id.ms_order_amount);
        submitTv = findViewById(R.id.submit_ms_order);
        addressLL = findViewById(R.id.merchart_address_ll);
        merchartSetLv = findViewById(R.id.merchart_set_lv1);
    }

    @Override
    public void setListener() {
        submitTv.setOnClickListener(this);
        telIv.setOnClickListener(this);
        addressLL.setOnClickListener(this);
        merchartSetLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                merchartSetAdapter.select(position);
                orderAmountTv.setText("MOP" + setList.get(position).getCostPrice());
            }
        });
    }

    @Override
    public void initData() {
        headView.setLeft(this);
        headView.setTitle("特價洗車");

        setList = new ArrayList<>();
        merchartSetAdapter = new MerchartSetAdapter(this, setList);
        merchartSetLv.setAdapter(merchartSetAdapter);
        merchatId = Integer.parseInt(getIntent().getStringExtra("merchatId"));
        callNetGetMerchatData();
        //
        dialog = new ProgressDialog(this);
    }

    private void getLocation() {
        //获取定位服务
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //获取当前可用的位置控制器
        List<String> list = locationManager.getProviders(true);

        if (list.contains(LocationManager.GPS_PROVIDER)) {
            //是否为GPS位置控制器
            provider = LocationManager.GPS_PROVIDER;
        } else if (list.contains(LocationManager.NETWORK_PROVIDER)) {
            //是否为网络位置控制器
            provider = LocationManager.NETWORK_PROVIDER;

        } else {
            Toast.makeText(this, "请检查网络或GPS是否打开",
                    Toast.LENGTH_LONG).show();
            return;
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLocation = locationManager.getLastKnownLocation(provider);
    }

    private void callNetGetMerchatData() {
        RequestParams params = new RequestParams(BSSMConfigtor.BASE_URL + BSSMConfigtor.GET_MERCHART_SET);
        params.setAsJsonContent(true);
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("id",merchatId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String urlJson = jsonObj.toString();
        params.setBodyContent(urlJson);
        String uri = params.getUri();
        params.setConnectTimeout(30 * 1000);
        x.http().request(HttpMethod.POST ,params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                MerchatSetOutModel model = new Gson().fromJson(result.toString(), MerchatSetOutModel.class);
                if (model.getCode() ==200) {
                    merchatSetModel = model.getData();
                    setList.addAll(merchatSetModel.getSellerCharge());
                } else if (model.getCode() == 10000){
                    SPUtils.put(MerchatSetActivity.this, "UserToken", "");
                    Toast.makeText(MerchatSetActivity.this,  String.valueOf(model.getMsg()), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MerchatSetActivity.this,  String.valueOf(model.getMsg()), Toast.LENGTH_SHORT).show();
                }
            }
            //请求异常后的回调方法
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if (ex instanceof SocketTimeoutException) {
                    Toast.makeText(MerchatSetActivity.this, "請求超時，請重試！", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MerchatSetActivity.this, getString(R.string.no_net), Toast.LENGTH_SHORT).show();
                }
            }
            //主动调用取消请求的回调方法
            @Override
            public void onCancelled(CancelledException cex) {
            }
            @Override
            public void onFinished() {
                refreshUI();
            }
        });
    }

    /**
     * 獲取資料完成，刷新UI
     */
    private void refreshUI() {
        x.image().bind(merchartIv, merchatSetModel.getSeller().getSellerLogo(), options);
        nameTv.setText(merchatSetModel.getSeller().getSellerName());
        businessHoursTv.setText(merchatSetModel.getSeller().getSellerDes());
        soldTv.setText("已售" + merchatSetModel.getSeller().getOrderCount());
        addressTv.setText("地址：" + merchatSetModel.getSeller().getAddress());
        merchartSetAdapter.refreshListView(setList);

        if (setList.size() > 0) {
            merchartSetAdapter.select(0);
            orderAmountTv.setText("MOP" + setList.get(0).getCostPrice());
        }
    }

    /**
     * 提交訂單
     */
    private void sunbitMerchartOrder(int merchatId) {
        dialog.setTitle("提示");
        dialog.setMessage("正在提交訂單......");
        dialog.setCancelable(false);
        dialog.show();
        RequestParams params = new RequestParams(BSSMConfigtor.BASE_URL + BSSMConfigtor.SUBMIT_CAR_WASH_ORDER + SPUtils.get(this, "UserToken", ""));
        params.setAsJsonContent(true);
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("chargeId", merchatId);
            jsonObj.put("remark", merchatId);
            jsonObj.put("num", 1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String urlJson = jsonObj.toString();
        params.setBodyContent(urlJson);
        String uri = params.getUri();
        params.setConnectTimeout(30 * 1000);
        x.http().request(HttpMethod.POST ,params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                PlaceCarWashOrderOutModel model = new Gson().fromJson(result.toString(), PlaceCarWashOrderOutModel.class);
                if (model.getCode() ==200) {
                    Intent intent2 = new Intent(MerchatSetActivity.this, PaymentAcvtivity.class);
                    intent2.putExtra("startWay", 4);   // carWashOrder
                    intent2.putExtra("orderNo", model.getData().getOrderNo());
                    intent2.putExtra("amount", String.valueOf(model.getData().getAmount()));
                    intent2.putExtra("createTime", model.getData().getCreateTime());
                    intent2.putExtra("exchange", model.getData().getExchange());
                    intent2.putExtra("exchangeAmountPay", model.getData().getExchangeAmountPay());
                    dialog.dismiss();
                    startActivityForResult(intent2, 1);
                } else if (model.getCode() == 10000){
                    dialog.dismiss();
                    SPUtils.put(MerchatSetActivity.this, "UserToken", "");
                    Toast.makeText(MerchatSetActivity.this,  String.valueOf(model.getMsg()), Toast.LENGTH_SHORT).show();
                } else {
                    dialog.dismiss();
                    Toast.makeText(MerchatSetActivity.this,  String.valueOf(model.getMsg()), Toast.LENGTH_SHORT).show();
                }
            }
            //请求异常后的回调方法
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                dialog.dismiss();
                if (ex instanceof SocketTimeoutException) {
                    Toast.makeText(MerchatSetActivity.this, "請求超時，請重試！", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MerchatSetActivity.this, getString(R.string.no_net), Toast.LENGTH_SHORT).show();
                }
            }
            //主动调用取消请求的回调方法
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
        switch (view.getId()) {
            case R.id.head_left:
                finish();
                break;
            case R.id.merchart_tel_ll:
                if (merchatSetModel != null) {
                    startCallPhone(merchatSetModel.getSeller().getPhone()
                    );
                }
                break;
            case R.id.merchart_address_ll:
                NiceDialog.init()
                        .setLayoutId(R.layout.dialog_map_list)
                        .setConvertListener(new ViewConvertListener() {
                            @Override
                            protected void convertView(ViewHolder viewHolder, final BaseNiceDialog baseNiceDialog) {
                                ListView lv = viewHolder.getView(R.id.dialog_map_lv);
                                final List<String> list = new ArrayList<String>();
                                list.add("百度地圖");
                                list.add("高德地圖");
                                list.add("谷歌地圖");
                                lv.setAdapter(new SecondCarOptionListAdapter(MerchatSetActivity.this, list));
                                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                        switch (i) {
                                            case 0:
                                                BSSMCommonUtils.openBaiduMap(MerchatSetActivity.this, 22.203822, 113.546757);
                                                break;
                                            case 1:
                                                BSSMCommonUtils.openGaodeMap(MerchatSetActivity.this, 22.203822, 113.546757);
                                                break;
                                            case 2:
                                                BSSMCommonUtils.openGoogleMap(MerchatSetActivity.this, 22.203822, 113.546757);
                                                break;
                                        }
                                        baseNiceDialog.dismiss();
                                    }
                                });
                            }
                        })
                        .setShowBottom(true)
                        .show(getSupportFragmentManager());
                break;
            case R.id.submit_ms_order:
                int id = 0;
                for (int i = 0; i< setList.size(); i++) {
                    if (setList.get(i).isSelected()) {
                        id = setList.get(i).getId();
                    }
                }
                if (id > 0) {
                    sunbitMerchartOrder(id);
                } else {
                    Toast.makeText(this, "請選擇購買的套餐！", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private String phoneNumber;
    /**
     * 打电话
     *
     * @param phoneNumber
     */
    protected void startCallPhone(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        //判断Android版本是否大于23
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);

            if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 1);
                return;
            } else {
                callPhone(phoneNumber);
            }
        } else {
            callPhone(phoneNumber);
            // 检查是否获得了权限（Android6.0运行时权限）
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                // 没有获得授权，申请授权
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) this,
                        Manifest.permission.CALL_PHONE)) {
                    // 返回值：
//                          如果app之前请求过该权限,被用户拒绝, 这个方法就会返回true.
//                          如果用户之前拒绝权限的时候勾选了对话框中”Don’t ask again”的选项,那么这个方法会返回false.
//                          如果设备策略禁止应用拥有这条权限, 这个方法也返回false.
                    // 弹窗需要解释为何需要该权限，再次请求授权
                    Toast.makeText(this, "您未授權，請先授權！", Toast.LENGTH_LONG).show();

                    // 帮跳转到该应用的设置界面，让用户手动授权
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                    intent.setData(uri);
                    startActivity(intent);
                } else {
                    // 不需要解释为何需要该权限，直接请求授权
                    ActivityCompat.requestPermissions((Activity) this,
                            new String[]{Manifest.permission.CALL_PHONE}, 1);
                }
            } else {
                // 已经获得授权，可以打电话
                callPhone(phoneNumber);
            }
        }

    }

    private void callPhone(String phoneNumber) {
        // 拨号：激活系统的拨号组件 -- 直接拨打电话
        //Intent intent = new Intent(); // 意图对象：动作 + 数据
        //intent.setAction(Intent.ACTION_CALL); // 设置动作
        //Uri data = Uri.parse("tel:" + phoneNumber); // 设置数据
        //intent.setData(data);
        //startActivity(intent); // 激活Activity组件


//打开拨号界面，填充输入手机号码，让用户自主的选择
        Intent intent = new Intent(Intent.ACTION_DIAL,Uri.parse("tel:"+phoneNumber));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    // 处理权限申请的回调
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 授权成功，继续打电话
                    callPhone(this.phoneNumber);
                } else {
                    // 授权失败！
                    Toast.makeText(this, "授权失败！", Toast.LENGTH_LONG).show();
                }
                break;
            }
        }

    }
}
