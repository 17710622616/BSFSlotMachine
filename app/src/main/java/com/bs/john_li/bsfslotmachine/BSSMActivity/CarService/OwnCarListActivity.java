package com.bs.john_li.bsfslotmachine.BSSMActivity.CarService;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bs.john_li.bsfslotmachine.BSSMActivity.BaseActivity;
import com.bs.john_li.bsfslotmachine.BSSMActivity.LoginActivity;
import com.bs.john_li.bsfslotmachine.BSSMAdapter.SmartCarListRefreshAdapter;
import com.bs.john_li.bsfslotmachine.BSSMAdapter.SmartOwnCarListRefreshAdapter;
import com.bs.john_li.bsfslotmachine.BSSMModel.CarModel;
import com.bs.john_li.bsfslotmachine.BSSMModel.OwnCarListOutModel;
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
 * 我的二手車列表
 * Created by John_Li on 22/11/2018.
 */

public class OwnCarListActivity extends BaseActivity implements View.OnClickListener{
    private BSSMHeadView headView;
    private LinearLayout noCarLL;
    private RefreshLayout mRefreshLayout;
    private RecyclerView mRecycleView;

    private List<OwnCarListOutModel.DataBeanX.OwnCarListModel> carModelList;
    private SmartOwnCarListRefreshAdapter mSmartOwnCarListRefreshAdapter;
    //private CarListAdapter mCarListAdapter;
    // 每頁加載數量
    private int pageSize = 10;
    // 頁數
    private int pageNo = 1;
    // 車輛總數
    private long totolCarCount;
    // 修改的位置
    private int updatePosition = 0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_own_car_list);
        initView();
        setListener();
        initData();
    }

    @Override
    public void initView() {
        headView = findViewById(R.id.own_car_list_head);
        mRecycleView = findViewById(R.id.own_carlist_lv);
        mRefreshLayout = findViewById(R.id.own_car_list_expand_swipe);
        noCarLL = findViewById(R.id.no_own_car_ll);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            headView.setHeadHight();
        }
        mRefreshLayout.setEnableAutoLoadmore(false);//是否启用列表惯性滑动到底部时自动加载更多
        mRefreshLayout.setDisableContentWhenRefresh(true);//是否在刷新的时候禁止列表的操作
        mRefreshLayout.setDisableContentWhenLoading(true);//是否在加载的时候禁止列表的操作
        // 设置header的高度
        mRefreshLayout.setHeaderHeightPx((int)(BSSMCommonUtils.getDeviceWitdh(this) / 4.05));//Header标准高度（显示下拉高度>=标准高度 触发刷新）
    }

    @Override
    public void setListener() {

        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                carModelList.clear();
                pageNo = 1;
                if (BSSMCommonUtils.isLoginNow(OwnCarListActivity.this)) {
                    callNetGetCarList();
                } else {
                    startActivityForResult(new Intent(OwnCarListActivity.this, LoginActivity.class), BSSMConfigtor.LOGIN_FOR_RQUEST);
                }
            }
        });
        mRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                //和最大的数据比较
                if (pageSize * (pageNo + 1) > totolCarCount){
                    Toast.makeText(OwnCarListActivity.this, "沒有更多數據了誒~", Toast.LENGTH_SHORT).show();
                    mRefreshLayout.finishRefresh();
                    mRefreshLayout.finishLoadmore();
                } else {
                    pageNo ++;
                    if (BSSMCommonUtils.isLoginNow(OwnCarListActivity.this)) {
                        callNetGetCarList();
                    } else {
                        startActivityForResult(new Intent(OwnCarListActivity.this, LoginActivity.class), BSSMConfigtor.LOGIN_FOR_RQUEST);
                    }
                }
            }
        });

        noCarLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                carModelList.clear();
                pageNo = 1;
                if (BSSMCommonUtils.isLoginNow(OwnCarListActivity.this)) {
                    callNetGetCarList();
                } else {
                    startActivityForResult(new Intent(OwnCarListActivity.this, LoginActivity.class), BSSMConfigtor.LOGIN_FOR_RQUEST);
                }
            }
        });
    }

    @Override
    public void initData() {
        headView.setLeft(this);
        headView.setTitle("發佈車輛列表");

        carModelList = new ArrayList<>();
        mSmartOwnCarListRefreshAdapter = new SmartOwnCarListRefreshAdapter(this, carModelList, AliyunOSSUtils.initOSS(this));
        mRecycleView.setLayoutManager(new LinearLayoutManager(this));
        mRecycleView.setAdapter(mSmartOwnCarListRefreshAdapter);
        mSmartOwnCarListRefreshAdapter.setOnItemClickListenr(new SmartOwnCarListRefreshAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }
        });
        mRefreshLayout.autoRefresh();
    }


    /**
     * 獲取車輛列表
     */
    private void callNetGetCarList() {
        RequestParams params = new RequestParams(BSSMConfigtor.BASE_URL + BSSMConfigtor.GET_OWN_OLD_CAR_LIST + SPUtils.get(this, "UserToken", ""));
        params.setAsJsonContent(true);
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("pageSize",pageSize);
            jsonObj.put("pageNo",pageNo);
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
                OwnCarListOutModel model = new Gson().fromJson(result.toString(), OwnCarListOutModel.class);
                if (model.getCode() ==200) {
                    totolCarCount = model.getData().getTotalCount();
                    carModelList.addAll(model.getData().getData());
                } else if (model.getCode() == 10000){
                    SPUtils.put(OwnCarListActivity.this, "UserToken", "");
                    Toast.makeText(OwnCarListActivity.this,  String.valueOf(model.getMsg()), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(OwnCarListActivity.this, String.valueOf(model.getMsg()), Toast.LENGTH_SHORT).show();
                }
            }
            //请求异常后的回调方法
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if (ex instanceof SocketTimeoutException) {
                    Toast.makeText(OwnCarListActivity.this, "獲取車輛列表" + getString(R.string.timeout), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(OwnCarListActivity.this, getString(R.string.no_net), Toast.LENGTH_SHORT).show();
                }
            }
            //主动调用取消请求的回调方法
            @Override
            public void onCancelled(CancelledException cex) {
            }
            @Override
            public void onFinished() {
                refreshView();
            }
        });
    }

    /**
     * 請求完成，刷新界面
     */
    public void refreshView(){
        if (carModelList.size() > 0) {
            noCarLL.setVisibility(View.GONE);
        } else {
            noCarLL.setVisibility(View.VISIBLE);
        }
        mSmartOwnCarListRefreshAdapter.refreshListView(carModelList);
        mRefreshLayout.finishRefresh();
        mRefreshLayout.finishLoadmore();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_left:
                finish();
                break;
        }
    }
}
