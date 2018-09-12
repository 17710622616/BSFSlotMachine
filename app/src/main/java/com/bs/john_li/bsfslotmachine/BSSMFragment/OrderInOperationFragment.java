package com.bs.john_li.bsfslotmachine.BSSMFragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bs.john_li.bsfslotmachine.BSSMActivity.LoginActivity;
import com.bs.john_li.bsfslotmachine.BSSMActivity.Mine.OrderDetialActivity;
import com.bs.john_li.bsfslotmachine.BSSMAdapter.SmartOrderRefreshAdapter;
import com.bs.john_li.bsfslotmachine.BSSMModel.UserOrderOutModel;
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
 * 已接單訂單
 * Created by John_Li on 5/1/2018.
 */

public class OrderInOperationFragment extends LazyLoadFragment {
    private View view;
    private RefreshLayout mRefreshLayout;
    private RecyclerView mRecycleView;
    private List<UserOrderOutModel.UserOrderInsideModel.UserOrderModel> orderList;
    private SmartOrderRefreshAdapter mSmartOrderRefreshAdapter;
    private LinearLayout noOrderLL;
    // 每頁加載數量
    private int pageSize = 10;
    // 頁數
    private int pageNo = 1;
    // 車輛總數
    private long totolCarCount = 30;

    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        setContentView(R.layout.fragment_orderlist);
        initView();
        setListener();
        initData();
    }

    private void initView() {
        mRefreshLayout = (RefreshLayout) findViewById(R.id.order_list_srl);
        mRecycleView = (RecyclerView) findViewById(R.id.order_list_lv);
        noOrderLL = (LinearLayout) findViewById(R.id.no_order_ll);

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
                orderList.clear();
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
    }

    private void initData() {
        orderList = new ArrayList<>();
        mSmartOrderRefreshAdapter = new SmartOrderRefreshAdapter(getActivity(), orderList, AliyunOSSUtils.initOSS(getActivity()));
        mRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecycleView.setAdapter(mSmartOrderRefreshAdapter);

        mSmartOrderRefreshAdapter.setOnItemClickListenr(new SmartOrderRefreshAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getActivity(), OrderDetialActivity.class);
                intent.putExtra("OrderModel", new Gson().toJson(orderList.get(position)));
                startActivity(intent);
            }
        });
        //mRefreshLayout.autoRefresh();
        if (BSSMCommonUtils.isLoginNow(getActivity())) {
            callNetGetCarList();
        } else {
            startActivityForResult(new Intent(getActivity(), LoginActivity.class), BSSMConfigtor.LOGIN_FOR_RQUEST);
        }
    }

    /**
     * 请求网络刷新数据
     */
    private void callNetGetCarList() {
        /*if (orderList == null) {
            orderList = new ArrayList<>();
        }
        int count = orderList.size();
        for (int i= count; i< count + 10; i++) {
            UserOrderOutModel.UserOrderInsideModel.UserOrderModel model = new UserOrderOutModel.UserOrderInsideModel.UserOrderModel();
            model.setOrderType(3);
            model.setOrderStatus(4);
            model.setCreateTime(1508685619000L);
            model.setPayAmount(100);
            model.setOrderNo("測試已投幣：" + i);
            model.setMachineNo("66579");
            model.setPillarColor(3);
            model.setCarId(123);
            model.setCarType(1);
            model.setStartSlotTime(1508686130000L);
            orderList.add(model);
        }
        mSmartOrderRefreshAdapter.refreshListView(orderList);
        mRefreshLayout.finishRefresh();
        mRefreshLayout.finishLoadmore();*/

        RequestParams params = new RequestParams(BSSMConfigtor.BASE_URL + BSSMConfigtor.GET_ORDER_LIST + SPUtils.get(getActivity().getApplicationContext(), "UserToken", ""));
        params.setAsJsonContent(true);
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("orderStatus",5);
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
                UserOrderOutModel model = new Gson().fromJson(result.toString(), UserOrderOutModel.class);
                if (model.getCode() ==200) {
                    totolCarCount = model.getData().getTotalCount();
                    List<UserOrderOutModel.UserOrderInsideModel.UserOrderModel> orderModelsFromNet = model.getData().getData();
                    orderList.addAll(orderModelsFromNet);
                } else if (model.getCode() == 10001) {
                    SPUtils.put(getActivity(), "UserToken", "");
                    startActivityForResult(new Intent(getActivity(), LoginActivity.class), BSSMConfigtor.LOGIN_FOR_RQUEST);
                } else {
                    Toast.makeText(getActivity(), String.valueOf(model.getMsg()), Toast.LENGTH_SHORT).show();
                }
            }
            //请求异常后的回调方法
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if (ex instanceof SocketTimeoutException) {
                    Toast.makeText(getActivity(), "連接超時，請重試！", Toast.LENGTH_SHORT).show();
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
                if (orderList.size() > 0) {
                    noOrderLL.setVisibility(View.GONE);
                } else {
                    noOrderLL.setVisibility(View.VISIBLE);
                }
                mSmartOrderRefreshAdapter.refreshListView(orderList);
                mRefreshLayout.finishRefresh();
                mRefreshLayout.finishLoadmore();
            }
        });
    }
}
