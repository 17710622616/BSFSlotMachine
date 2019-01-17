package com.bs.john_li.bsfmerchantsversionapp.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bs.john_li.bsfmerchantsversionapp.Adapter.SmartCWOrderRefreshAdapter;
import com.bs.john_li.bsfmerchantsversionapp.LoginActivity;
import com.bs.john_li.bsfmerchantsversionapp.Model.SellerOrderOutModel;
import com.bs.john_li.bsfmerchantsversionapp.OrderDetialActivity;
import com.bs.john_li.bsfmerchantsversionapp.R;
import com.bs.john_li.bsfmerchantsversionapp.Utils.AliyunOSSUtils;
import com.bs.john_li.bsfmerchantsversionapp.Utils.BSFCommonUtils;
import com.bs.john_li.bsfmerchantsversionapp.Utils.BSFMerchantConfigtor;
import com.bs.john_li.bsfmerchantsversionapp.Utils.SPUtils;
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
 * 已失效洗車訂單
 * Created by John_Li on 5/1/2018.
 */

public class CWOrderFailureFragment extends LazyLoadFragment {
    private View view;
    private RefreshLayout mRefreshLayout;
    private RecyclerView mRecycleView;
    private List<SellerOrderOutModel.DataBeanX.SellerOrderModel> orderList;
    private SmartCWOrderRefreshAdapter mSmartOrderRefreshAdapter;
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
        mRefreshLayout.setHeaderHeightPx((int)(BSFCommonUtils.getDeviceWitdh(getActivity()) / 4.05));//Header标准高度（显示下拉高度>=标准高度 触发刷新）
    }

    private void setListener() {
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                orderList.clear();
                pageNo = 1;
                if (BSFCommonUtils.isLoginNow(getActivity())) {
                    callNetGetCarList();
                } else {
                    startActivityForResult(new Intent(getActivity(), LoginActivity.class), BSFMerchantConfigtor.LOGIN_FOR_RQUEST);
                }
            }
        });
        mRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                //和最大的数据比较
                if (pageSize * (pageNo) > totolCarCount){
                    Toast.makeText(getActivity(), "沒有更多數據了誒~", Toast.LENGTH_SHORT).show();
                    mRefreshLayout.finishRefresh();
                    mRefreshLayout.finishLoadmore();
                } else {
                    pageNo ++;
                    if (BSFCommonUtils.isLoginNow(getActivity())) {
                        callNetGetCarList();
                    } else {
                        startActivityForResult(new Intent(getActivity(), LoginActivity.class), BSFMerchantConfigtor.LOGIN_FOR_RQUEST);
                    }
                }
            }
        });
    }

    private void initData() {
        orderList = new ArrayList<>();
        mSmartOrderRefreshAdapter = new SmartCWOrderRefreshAdapter(getActivity(), orderList, AliyunOSSUtils.initOSS(getActivity(),BSFMerchantConfigtor.END_POINT));
        mRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecycleView.setAdapter(mSmartOrderRefreshAdapter);

        mSmartOrderRefreshAdapter.setOnItemClickListenr(new SmartCWOrderRefreshAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getActivity(), OrderDetialActivity.class);
                intent.putExtra("orderModel", new Gson().toJson(orderList.get(position)));
                startActivity(intent);
            }
        });
        //mRefreshLayout.autoRefresh();
        if (BSFCommonUtils.isLoginNow(getActivity())) {
            callNetGetCarList();
        } else {
            startActivityForResult(new Intent(getActivity(), LoginActivity.class), BSFMerchantConfigtor.LOGIN_FOR_RQUEST);
        }
    }

    /**
     * 请求网络刷新数据
     */
    private void callNetGetCarList() {
        RequestParams params = new RequestParams(BSFMerchantConfigtor.BASE_URL + BSFMerchantConfigtor.SELLER_ORDER_LIST);
        params.setAsJsonContent(true);
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("orderStatus",7);
            jsonObj.put("pageSize",pageSize);
            jsonObj.put("pageNo",pageNo);
            jsonObj.put("sellerToken", SPUtils.get(getActivity().getApplicationContext(), "SellerUserToken", ""));
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
                SellerOrderOutModel model = new Gson().fromJson(result.toString(), SellerOrderOutModel.class);
                if (model.getCode() ==200) {
                    totolCarCount = model.getData().getTotalCount();
                    List<SellerOrderOutModel.DataBeanX.SellerOrderModel> orderModelsFromNet = model.getData().getData();
                    orderList.addAll(orderModelsFromNet);
                } else if (model.getCode() == 10000) {
                    SPUtils.put(getActivity(), "UserToken", "");
                    startActivityForResult(new Intent(getActivity(), LoginActivity.class), BSFMerchantConfigtor.LOGIN_FOR_RQUEST);
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
