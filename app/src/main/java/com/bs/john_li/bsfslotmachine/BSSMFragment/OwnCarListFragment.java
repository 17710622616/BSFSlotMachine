package com.bs.john_li.bsfslotmachine.BSSMFragment;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bs.john_li.bsfslotmachine.BSSMActivity.CarService.OwnCarListActivity;
import com.bs.john_li.bsfslotmachine.BSSMActivity.CarService.SecondCarDetailActivity;
import com.bs.john_li.bsfslotmachine.BSSMActivity.LoginActivity;
import com.bs.john_li.bsfslotmachine.BSSMAdapter.SmartOwnCarListRefreshAdapter;
import com.bs.john_li.bsfslotmachine.BSSMModel.OwnCarListOutModel;
import com.bs.john_li.bsfslotmachine.BSSMUtils.AliyunOSSUtils;
import com.bs.john_li.bsfslotmachine.BSSMUtils.BSSMCommonUtils;
import com.bs.john_li.bsfslotmachine.BSSMUtils.BSSMConfigtor;
import com.bs.john_li.bsfslotmachine.BSSMUtils.SPUtils;
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
 * Created by John_Li on 28/12/2018.
 */

public class OwnCarListFragment extends LazyLoadFragment {
    public static String TAG = OwnCarListFragment.class.getName();
    private LinearLayout noCarLL;
    private RefreshLayout mRefreshLayout;
    private RecyclerView mRecycleView;

    private List<OwnCarListOutModel.DataBeanX.OwnCarListModel> carModelList;
    private SmartOwnCarListRefreshAdapter mSmartOwnCarListRefreshAdapter;
    // 每頁加載數量
    private int pageSize = 10;
    // 頁數
    private int pageNo = 1;
    // 車輛總數
    private long totolCarCount;
    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        setContentView(R.layout.fragment_own_car_list);
        initView();
        setListener();
        initData();
    }

    private void initView() {
        mRecycleView = (RecyclerView) findViewById(R.id.own_carlist_lv);
        mRefreshLayout = (RefreshLayout) findViewById(R.id.own_car_list_expand_swipe);
        noCarLL = (LinearLayout) findViewById(R.id.no_own_car_ll);

        mRefreshLayout.setEnableAutoLoadmore(false);//是否启用列表惯性滑动到底部时自动加载更多
        mRefreshLayout.setDisableContentWhenRefresh(true);//是否在刷新的时候禁止列表的操作
        mRefreshLayout.setDisableContentWhenLoading(true);//是否在加载的时候禁止列表的操作
        // 设置header的高度
        mRefreshLayout.setHeaderHeightPx((int)(BSSMCommonUtils.getDeviceWitdh(getActivity()) / 4.05));//Header标准高度（显示下拉高度>=标准高度 触发刷新）
    }

    private void setListener() {
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                carModelList.clear();
                pageNo = 1;
                if (BSSMCommonUtils.isLoginNow(getActivity())) {
                    callNetGetCarList();
                } else {
                    startActivityForResult(new Intent(getActivity(), LoginActivity.class), BSSMConfigtor.LOGIN_FOR_RQUEST);
                }
            }
        });
        mRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                //和最大的数据比较
                if (pageSize * (pageNo + 1) > totolCarCount){
                    Toast.makeText(getActivity(), "沒有更多數據了誒~", Toast.LENGTH_SHORT).show();
                    mRefreshLayout.finishRefresh();
                    mRefreshLayout.finishLoadmore();
                } else {
                    pageNo ++;
                    if (BSSMCommonUtils.isLoginNow(getActivity())) {
                        callNetGetCarList();
                    } else {
                        startActivityForResult(new Intent(getActivity(), LoginActivity.class), BSSMConfigtor.LOGIN_FOR_RQUEST);
                    }
                }
            }
        });

        noCarLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                carModelList.clear();
                pageNo = 1;
                if (BSSMCommonUtils.isLoginNow(getActivity())) {
                    callNetGetCarList();
                } else {
                    startActivityForResult(new Intent(getActivity(), LoginActivity.class), BSSMConfigtor.LOGIN_FOR_RQUEST);
                }
            }
        });
    }

    private void initData() {
        carModelList = new ArrayList<>();
        mSmartOwnCarListRefreshAdapter = new SmartOwnCarListRefreshAdapter(getActivity(), carModelList, AliyunOSSUtils.initOSS(getActivity()));
        mRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecycleView.setAdapter(mSmartOwnCarListRefreshAdapter);
        mSmartOwnCarListRefreshAdapter.setOnItemClickListenr(new SmartOwnCarListRefreshAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getActivity(), SecondCarDetailActivity.class);
                intent.putExtra("seoncdeCarId", String.valueOf(carModelList.get(position).getId()));
                startActivity(intent);
            }
        });
        mRefreshLayout.autoRefresh();
    }

    /**
     * 獲取車輛列表
     */
    private void callNetGetCarList() {
        RequestParams params = new RequestParams(BSSMConfigtor.BASE_URL + BSSMConfigtor.GET_OWN_OLD_CAR_LIST + SPUtils.get(getActivity(), "UserToken", ""));
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
                    SPUtils.put(getActivity(), "UserToken", "");
                    Toast.makeText(getActivity(),  String.valueOf(model.getMsg()), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), String.valueOf(model.getMsg()), Toast.LENGTH_SHORT).show();
                }
            }
            //请求异常后的回调方法
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if (ex instanceof SocketTimeoutException) {
                    Toast.makeText(getActivity(), "獲取車輛列表" + getString(R.string.timeout), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), getString(R.string.no_net), Toast.LENGTH_SHORT).show();
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK) {
            if (requestCode == 1) {
                mRefreshLayout.autoRefresh();
            }
        }
    }
}
