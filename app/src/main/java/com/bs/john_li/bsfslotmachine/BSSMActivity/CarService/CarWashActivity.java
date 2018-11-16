package com.bs.john_li.bsfslotmachine.BSSMActivity.CarService;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.bs.john_li.bsfslotmachine.BSSMActivity.BaseActivity;
import com.bs.john_li.bsfslotmachine.BSSMActivity.LoginActivity;
import com.bs.john_li.bsfslotmachine.BSSMAdapter.SmartCarWashMerchatRefreshAdapter;
import com.bs.john_li.bsfslotmachine.BSSMAdapter.SmartOrderRefreshAdapter;
import com.bs.john_li.bsfslotmachine.BSSMModel.CarWashMerchantOutModel;
import com.bs.john_li.bsfslotmachine.BSSMUtils.AliyunOSSUtils;
import com.bs.john_li.bsfslotmachine.BSSMUtils.BSSMCommonUtils;
import com.bs.john_li.bsfslotmachine.BSSMUtils.BSSMConfigtor;
import com.bs.john_li.bsfslotmachine.BSSMUtils.SPUtils;
import com.bs.john_li.bsfslotmachine.BSSMView.BSSMHeadView;
import com.bs.john_li.bsfslotmachine.R;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by John on 13/11/2018.
 */

public class CarWashActivity extends BaseActivity implements View.OnClickListener {
    private BSSMHeadView headView;
    private RadioGroup mRg;
    private RefreshLayout mRefreshLayout;
    private RecyclerView mRecycleView;
    private LinearLayout noMerchatLL;

    private String orderBy = "nearBy";
    private List<CarWashMerchantOutModel.DataBeanX.CarWashMerchatModel> merchatList;
    private SmartCarWashMerchatRefreshAdapter mSmartCarWashMerchatRefreshAdapter;
    //定位都要通过LocationManager这个类实现
    private LocationManager locationManager;
    private Location mLocation;
    private String provider;
    // 每頁加載數量
    private int pageSize = 10;
    // 頁數
    private int pageNo = 1;
    // 車輛總數
    private long totolCarCount = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_wash);
        getLocation();
        initView();
        initData();
        setListener();
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

    @Override
    public void initView() {
        headView = findViewById(R.id.car_wash_head);
        mRg = findViewById(R.id.car_wash_rg);
        mRefreshLayout = (RefreshLayout) findViewById(R.id.car_wash_list_srl);
        mRecycleView = (RecyclerView) findViewById(R.id.car_wash_list_lv);
        noMerchatLL = (LinearLayout) findViewById(R.id.no_merchant_ll);
        // 设置header的高度
        mRefreshLayout.setEnableAutoLoadmore(false);//是否启用列表惯性滑动到底部时自动加载更多
        mRefreshLayout.setDisableContentWhenRefresh(true);//是否在刷新的时候禁止列表的操作
        mRefreshLayout.setDisableContentWhenLoading(true);//是否在加载的时候禁止列表的操作
        mRefreshLayout.setHeaderHeightPx((int)(BSSMCommonUtils.getDeviceWitdh(this) / 4.05));//Header标准高度（显示下拉高度>=标准高度 触发刷新）
    }

    @Override
    public void setListener() {
        mRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                switch (i) {
                    case R.id.distance_nearest_rb:
                        orderBy = "nearBy";
                        mRefreshLayout.autoRefresh();
                        break;
                    case R.id.highest_sales_rb:
                        orderBy = "sale";
                        mRefreshLayout.autoRefresh();
                        break;
                }
            }
        });

        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                merchatList.clear();
                pageNo = 1;
                if (BSSMCommonUtils.isLoginNow(CarWashActivity.this)) {
                    callNetGetCarWashMercahtList();
                } else {
                    startActivityForResult(new Intent(CarWashActivity.this, LoginActivity.class), BSSMConfigtor.LOGIN_FOR_RQUEST);
                }
            }
        });
        mRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                //和最大的数据比较
                if (pageSize * (pageNo + 1) > totolCarCount){
                    Toast.makeText(CarWashActivity.this, "沒有更多數據了誒~", Toast.LENGTH_SHORT).show();
                    mRefreshLayout.finishRefresh();
                    mRefreshLayout.finishLoadmore();
                } else {
                    pageNo ++;
                    if (BSSMCommonUtils.isLoginNow(CarWashActivity.this)) {
                        callNetGetCarWashMercahtList();
                    } else {
                        startActivityForResult(new Intent(CarWashActivity.this, LoginActivity.class), BSSMConfigtor.LOGIN_FOR_RQUEST);
                    }
                }
            }
        });
    }

    @Override
    public void initData() {
        headView.setLeft(this);
        headView.setTitle("特價洗車");

        merchatList = new ArrayList<>();
        mSmartCarWashMerchatRefreshAdapter = new SmartCarWashMerchatRefreshAdapter(this, merchatList, AliyunOSSUtils.initOSS(this));
        mRecycleView.setLayoutManager(new LinearLayoutManager(this));
        mRecycleView.setAdapter(mSmartCarWashMerchatRefreshAdapter);

        mSmartCarWashMerchatRefreshAdapter.setOnItemClickListenr(new SmartCarWashMerchatRefreshAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(CarWashActivity.this, MerchatSetActivity.class);
                intent.putExtra("merchatId", String.valueOf(merchatList.get(position).getId()));
                startActivity(intent);
            }
        });
        mRefreshLayout.autoRefresh();
    }

    /**
     * 请求网络刷新数据
     */
    private void callNetGetCarWashMercahtList() {
        RequestParams params = new RequestParams(BSSMConfigtor.BASE_URL + BSSMConfigtor.GET_SELLER_LIST);
        params.setAsJsonContent(true);
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("type",2);
            if (mLocation != null) {
                jsonObj.put("longitude",mLocation.getLongitude());
                jsonObj.put("latitude",mLocation.getLatitude());
            } else {
                jsonObj.put("longitude","113.548331");
                jsonObj.put("latitude","22.205702");
            }
            jsonObj.put("pageSize",pageSize);
            jsonObj.put("pageNo",pageNo);
            jsonObj.put("orderBy",orderBy);
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
                CarWashMerchantOutModel model = new Gson().fromJson(result.toString(), CarWashMerchantOutModel.class);
                if (model.getCode() ==200) {
                    totolCarCount = model.getData().getTotalCount();
                    List<CarWashMerchantOutModel.DataBeanX.CarWashMerchatModel> carWashMerchatModels = model.getData().getData();
                    merchatList.addAll(carWashMerchatModels);
                } else if (model.getCode() == 10000){
                    SPUtils.put(CarWashActivity.this, "UserToken", "");
                    Toast.makeText(CarWashActivity.this,  String.valueOf(model.getMsg()), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(CarWashActivity.this,  String.valueOf(model.getMsg()), Toast.LENGTH_SHORT).show();
                }
            }
            //请求异常后的回调方法
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if (ex instanceof SocketTimeoutException) {
                    Toast.makeText(CarWashActivity.this, "請求超時，請重試！", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(CarWashActivity.this, getString(R.string.no_net), Toast.LENGTH_SHORT).show();
                }
            }
            //主动调用取消请求的回调方法
            @Override
            public void onCancelled(CancelledException cex) {
            }
            @Override
            public void onFinished() {
                if (merchatList.size() > 0) {
                    noMerchatLL.setVisibility(View.GONE);
                } else {
                    noMerchatLL.setVisibility(View.VISIBLE);
                }
                mSmartCarWashMerchatRefreshAdapter.refreshListView(merchatList);
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
        }
    }
}
