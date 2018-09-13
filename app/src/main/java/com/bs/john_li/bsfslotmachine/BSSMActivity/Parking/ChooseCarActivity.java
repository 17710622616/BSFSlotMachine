package com.bs.john_li.bsfslotmachine.BSSMActivity.Parking;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bs.john_li.bsfslotmachine.BSSMActivity.BaseActivity;
import com.bs.john_li.bsfslotmachine.BSSMActivity.LoginActivity;
import com.bs.john_li.bsfslotmachine.BSSMActivity.Mine.AddCarActivity;
import com.bs.john_li.bsfslotmachine.BSSMActivity.Mine.CarRechargeActivity;
import com.bs.john_li.bsfslotmachine.BSSMAdapter.ChooseCarAdapter;
import com.bs.john_li.bsfslotmachine.BSSMAdapter.SmartChooseCarRefreshAdapter;
import com.bs.john_li.bsfslotmachine.BSSMModel.CarModel;
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

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * 選擇車輛
 * Created by John_Li on 4/10/2017.
 */

public class ChooseCarActivity extends BaseActivity implements View.OnClickListener, ChooseCarAdapter.CarUpdateCallBack {
    private BSSMHeadView carListHead;
    /*private ListView carLv;
    private ExpandSwipeRefreshLayout mExpandSwipeRefreshLayout;*/
    private RefreshLayout mRefreshLayout;
    private RecyclerView mRecycleView;
    private LinearLayout noCarLL;

    private List<CarModel.CarCountAndListModel.CarInsideModel> carModelList;
    private ChooseCarAdapter mCarListAdapter;
    private SmartChooseCarRefreshAdapter mSmartChooseCarRefreshAdapter;
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
        setContentView(R.layout.activity_car_list);
        initView();
        setListener();
        initData();
    }

    @Override
    public void initView() {
        carListHead = findViewById(R.id.carlist_head);
        mRecycleView = findViewById(R.id.carlist_lv);
        mRefreshLayout = findViewById(R.id.car_list_expand_swipe);
        noCarLL = findViewById(R.id.no_car_ll);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            carListHead.setHeadHight();
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
                if (BSSMCommonUtils.isLoginNow(ChooseCarActivity.this)) {
                    callNetGetCarList();
                } else {
                    startActivity(new Intent(ChooseCarActivity.this, LoginActivity.class));
                }
            }
        });
        mRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                if (pageSize * (pageNo + 1) > totolCarCount){
                    Toast.makeText(ChooseCarActivity.this, "沒有更多數據了誒~", Toast.LENGTH_SHORT).show();
                    mRefreshLayout.finishLoadmore(true);
                    mRefreshLayout.finishRefresh(true);
                } else {
                    pageNo ++;
                    if (BSSMCommonUtils.isLoginNow(ChooseCarActivity.this)) {
                        callNetGetCarList();
                    } else {
                        startActivity(new Intent(ChooseCarActivity.this, LoginActivity.class));
                    }
                }
            }
        });


        noCarLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRefreshLayout.setEnableRefresh(true);
                noCarLL.setVisibility(View.GONE);
                carModelList.clear();
                pageNo = 1;
                if (BSSMCommonUtils.isLoginNow(ChooseCarActivity.this)) {
                    callNetGetCarList();
                } else {
                    startActivity(new Intent(ChooseCarActivity.this, LoginActivity.class));
                }
            }
        });
    }

    @Override
    public void initData() {
        carListHead.setTitle("我的車輛");
        carListHead.setLeft(this);
        carListHead.setRight(R.mipmap.push_invitation, this);

        carModelList = new ArrayList<>();
        mSmartChooseCarRefreshAdapter = new SmartChooseCarRefreshAdapter(this, carModelList, AliyunOSSUtils.initOSS(this));
        mRecycleView.setLayoutManager(new LinearLayoutManager(this));
        mRecycleView.setAdapter(mSmartChooseCarRefreshAdapter);
        mSmartChooseCarRefreshAdapter.setOnItemClickListenr(new SmartChooseCarRefreshAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (carModelList.get(position).getIfPay() == 0) {  // 未充值
                    Intent intent = new Intent(ChooseCarActivity.this, CarRechargeActivity.class);
                    intent.putExtra("carModel", new Gson().toJson(carModelList.get(position)));
                    startActivity(intent);
                } else {    // 已充值
                    Intent intent = new Intent();
                    intent.putExtra("carModel", new Gson().toJson(carModelList.get(position)));
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });
        mRefreshLayout.autoRefresh();
    }

    /**
     * 獲取車輛列表
     */
    private void callNetGetCarList() {
        RequestParams params = new RequestParams(BSSMConfigtor.BASE_URL + BSSMConfigtor.GET_CAR_LIST + SPUtils.get(this, "UserToken", ""));
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
                CarModel model = new Gson().fromJson(result.toString(), CarModel.class);
                if (model.getCode() ==200) {
                    totolCarCount = model.getData().getTotalCount();
                    List<CarModel.CarCountAndListModel.CarInsideModel> carInsideModelsFromNet = model.getData().getData();
                    carModelList.addAll(carInsideModelsFromNet);
                    // List去重
                    deWeightListById();
                } else if (model.getCode() == 10000) {
                    SPUtils.put(ChooseCarActivity.this, "UserToken", "");
                    startActivityForResult(new Intent(ChooseCarActivity.this, LoginActivity.class), BSSMConfigtor.LOGIN_FOR_RQUEST);
                } else {
                    Toast.makeText(ChooseCarActivity.this, getString(R.string.login_fail), Toast.LENGTH_SHORT).show();
                }
            }
            //请求异常后的回调方法
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(ChooseCarActivity.this, getString(R.string.no_net), Toast.LENGTH_SHORT).show();
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
        /*if (carModelList.size() > 0) {
            noCarLL.setVisibility(View.GONE);
        } else {
            noCarLL.setVisibility(View.VISIBLE);
        }
        mCarListAdapter.refreshListView(carModelList);
        if (mExpandSwipeRefreshLayout.isRefreshing()) {
            mExpandSwipeRefreshLayout.setRefreshing(false);
        }*/

        if (carModelList.size() > 0) {
            noCarLL.setVisibility(View.GONE);
        } else {
            noCarLL.setVisibility(View.VISIBLE);
        }
        mSmartChooseCarRefreshAdapter.notifyDataSetChanged();
        mRefreshLayout.finishRefresh();
        mRefreshLayout.finishLoadmore();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.head_left:
                finish();
                break;
            case R.id.head_right:
                Intent intent = new Intent(this, AddCarActivity.class);
                intent.putExtra("startWay", "add");
                startActivityForResult(intent, BSSMConfigtor.ADD_CAR_RQUEST);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            Toast.makeText(this, "添加車輛失敗！", Toast.LENGTH_SHORT).show();
            return;
        }

        switch (requestCode) {
            case 3: // 添加車輛的返回
                carModelList.add(0, new Gson().fromJson(data.getStringExtra("NEW_CAR_FROM_ADD"), CarModel.CarCountAndListModel.CarInsideModel.class));
                mCarListAdapter.refreshListView(carModelList);
                break;
            case 4: // 修改車輛的返回
                carModelList.set(updatePosition,new Gson().fromJson(data.getStringExtra("NEW_CAR_FROM_ADD"), CarModel.CarCountAndListModel.CarInsideModel.class));
                mCarListAdapter.refreshListView(carModelList);
                break;
        }
    }

    /**
     * List去重
     */
    private void deWeightListById() {
        Set<CarModel.CarCountAndListModel.CarInsideModel> s= new TreeSet<CarModel.CarCountAndListModel.CarInsideModel>(new Comparator<CarModel.CarCountAndListModel.CarInsideModel>(){

            @Override
            public int compare(CarModel.CarCountAndListModel.CarInsideModel o1, CarModel.CarCountAndListModel.CarInsideModel o2) {
                return Integer.toString(o1.getId()).compareTo(Integer.toString(o2.getId()));
            }

        });

        s.addAll(carModelList);
        carModelList = new ArrayList<CarModel.CarCountAndListModel.CarInsideModel>(s);
    }

    /**
     * 修改車輛
     * @param view
     */
    @Override
    public void carUpdateClick(View view) {

    }
}
