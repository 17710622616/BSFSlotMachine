package com.bs.john_li.bsfslotmachine.BSSMActivity.CarService;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bs.john_li.bsfslotmachine.BSSMActivity.BaseActivity;
import com.bs.john_li.bsfslotmachine.BSSMActivity.LoginActivity;
import com.bs.john_li.bsfslotmachine.BSSMAdapter.CarPartsAdapter;
import com.bs.john_li.bsfslotmachine.BSSMAdapter.SecondCarOptionListAdapter;
import com.bs.john_li.bsfslotmachine.BSSMModel.CarPartsOutModel;
import com.bs.john_li.bsfslotmachine.BSSMModel.CarWashMerchantOutModel;
import com.bs.john_li.bsfslotmachine.BSSMUtils.BSSMCommonUtils;
import com.bs.john_li.bsfslotmachine.BSSMUtils.BSSMConfigtor;
import com.bs.john_li.bsfslotmachine.BSSMUtils.SPUtils;
import com.bs.john_li.bsfslotmachine.BSSMView.BSSMHeadView;
import com.bs.john_li.bsfslotmachine.BSSMView.NoScrollGridView;
import com.bs.john_li.bsfslotmachine.R;
import com.google.gson.Gson;
import com.othershe.nicedialog.BaseNiceDialog;
import com.othershe.nicedialog.NiceDialog;
import com.othershe.nicedialog.ViewConvertListener;
import com.othershe.nicedialog.ViewHolder;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by John_Li on 4/1/2019.
 */

public class SellerCarPartsSetListActivity extends BaseActivity implements View.OnClickListener {
    private BSSMHeadView headView;
    private ImageView sellerIv, telIv;
    private TextView nameTv, businessHoursTv, soldTv, addressTv;
    private LinearLayout addressLL;
    private NoScrollGridView sellerSetGv;
    private RefreshLayout mRefreshLayout;

    // 商家類
    private CarWashMerchantOutModel.DataBeanX.CarWashMerchatModel merchatModel;
    // 套餐列表
    private List<CarPartsOutModel.DataBeanX.CarPatsModel> mCarPatsList;
    private CarPartsAdapter mCarPartsAdapter;
    // 每頁加載數量
    private int pageSize = 10;
    // 頁數
    private int pageNo = 1;
    // 套餐總數量
    private long totalCount;
    //定位都要通过LocationManager这个类实现
    private LocationManager locationManager;
    private Location mLocation;
    private String provider;
    private ImageOptions options = new ImageOptions.Builder().setImageScaleType(ImageView.ScaleType.FIT_CENTER).setLoadingDrawableId(R.mipmap.img_loading_list).setFailureDrawableId(R.mipmap.load_img_fail).build();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_car_parts);
        initView();
        setListener();
        initData();
    }

    @Override
    public void initView() {
        headView = findViewById(R.id.seller_car_parts_head);
        sellerIv = findViewById(R.id.car_parts_iv);
        telIv = findViewById(R.id.car_parts_tel_ll);
        nameTv = findViewById(R.id.car_parts_name_tv);
        businessHoursTv = findViewById(R.id.car_parts_business_hours_tv);
        soldTv = findViewById(R.id.car_parts_sold_tv);
        addressTv = findViewById(R.id.car_parts_address_tv);
        addressLL = findViewById(R.id.car_parts_address_ll);
        sellerSetGv = findViewById(R.id.car_parts_set_gv);

        mRefreshLayout = findViewById(R.id.car_parts_srl);
        mRefreshLayout.setEnableAutoLoadmore(false);//是否启用列表惯性滑动到底部时自动加载更多
        mRefreshLayout.setDisableContentWhenRefresh(true);//是否在刷新的时候禁止列表的操作
        mRefreshLayout.setDisableContentWhenLoading(true);//是否在加载的时候禁止列表的操作
        // 设置header的高度
        mRefreshLayout.setHeaderHeightPx((int)(BSSMCommonUtils.getDeviceWitdh(this) / 4.05));//Header标准高度（显示下拉高度>=标准高度 触发刷新）
    }

    @Override
    public void setListener() {
        telIv.setOnClickListener(this);
        addressLL.setOnClickListener(this);
        sellerSetGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(SellerCarPartsSetListActivity.this, CarPartSetDetialActivity.class);
                intent.putExtra("carPartId", String.valueOf(mCarPatsList.get(position).getId()));
                startActivity(intent);
            }
        });

        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                mCarPatsList.clear();
                pageNo = 1;
                if (BSSMCommonUtils.isLoginNow(SellerCarPartsSetListActivity.this)) {
                    callNetGetMerchatData();
                } else {
                    startActivityForResult(new Intent(SellerCarPartsSetListActivity.this, LoginActivity.class), BSSMConfigtor.LOGIN_FOR_RQUEST);
                }
            }
        });
        mRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                //和最大的数据比较
                if (pageSize * (pageNo) > totalCount){
                    Toast.makeText(SellerCarPartsSetListActivity.this, "沒有更多數據了誒~", Toast.LENGTH_SHORT).show();
                    mRefreshLayout.finishRefresh();
                    mRefreshLayout.finishLoadmore();
                } else {
                    pageNo ++;
                    if (BSSMCommonUtils.isLoginNow(SellerCarPartsSetListActivity.this)) {
                        callNetGetMerchatData();
                    } else {
                        startActivityForResult(new Intent(SellerCarPartsSetListActivity.this, LoginActivity.class), BSSMConfigtor.LOGIN_FOR_RQUEST);
                    }
                }
            }
        });
    }

    @Override
    public void initData() {
        headView.setLeft(this);
        headView.setTitle("汽車零件");

        mCarPatsList = new ArrayList<>();
        mCarPartsAdapter = new CarPartsAdapter(this, mCarPatsList);
        sellerSetGv.setAdapter(mCarPartsAdapter);
        merchatModel = new Gson().fromJson(getIntent().getStringExtra("merchatModel"), CarWashMerchantOutModel.DataBeanX.CarWashMerchatModel.class);
        // 填充商家信息
        x.image().bind(sellerIv, merchatModel.getSellerLogo(), options);
        nameTv.setText(merchatModel.getSellerName());
        businessHoursTv.setText(merchatModel.getSellerDes());
        soldTv.setText("已售" + merchatModel.getOrderCount());
        addressTv.setText("地址：" + merchatModel.getAddress());

        // 獲取商家套餐列表
        mRefreshLayout.autoRefresh();
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
        RequestParams params = new RequestParams(BSSMConfigtor.BASE_URL + BSSMConfigtor.GET_SELLER_PARTS);
        params.setAsJsonContent(true);
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("sellerId", merchatModel.getId());
            jsonObj.put("pageNo", pageNo);
            jsonObj.put("pageSize", pageSize);
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
                CarPartsOutModel model = new Gson().fromJson(result.toString(), CarPartsOutModel.class);
                if (model.getCode() ==200) {
                    totalCount = model.getData().getTotalCount();
                    mCarPatsList.addAll(model.getData().getData());
                } else if (model.getCode() == 10000){
                    SPUtils.put(SellerCarPartsSetListActivity.this, "UserToken", "");
                    Toast.makeText(SellerCarPartsSetListActivity.this,  String.valueOf(model.getMsg()), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SellerCarPartsSetListActivity.this,  String.valueOf(model.getMsg()), Toast.LENGTH_SHORT).show();
                }
            }
            //请求异常后的回调方法
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if (ex instanceof SocketTimeoutException) {
                    Toast.makeText(SellerCarPartsSetListActivity.this, "請求超時，請重試！", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SellerCarPartsSetListActivity.this, getString(R.string.no_net), Toast.LENGTH_SHORT).show();
                }
            }
            //主动调用取消请求的回调方法
            @Override
            public void onCancelled(CancelledException cex) {
            }
            @Override
            public void onFinished() {
                mCarPartsAdapter.refreshListView(mCarPatsList);
                mRefreshLayout.finishRefresh();
                mRefreshLayout.finishLoadmore();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.head_left:
                finish();
                break;
            case R.id.car_parts_tel_ll:
                if (merchatModel != null) {
                    startCallPhone(merchatModel.getPhone()
                    );
                }
                break;
            case R.id.car_parts_address_ll:
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
                                lv.setAdapter(new SecondCarOptionListAdapter(SellerCarPartsSetListActivity.this, list));
                                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                        switch (i) {
                                            case 0:
                                                BSSMCommonUtils.openBaiduMap(SellerCarPartsSetListActivity.this, 22.203822, 113.546757);
                                                break;
                                            case 1:
                                                BSSMCommonUtils.openGaodeMap(SellerCarPartsSetListActivity.this, 22.203822, 113.546757);
                                                break;
                                            case 2:
                                                BSSMCommonUtils.openGoogleMap(SellerCarPartsSetListActivity.this, 22.203822, 113.546757);
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
