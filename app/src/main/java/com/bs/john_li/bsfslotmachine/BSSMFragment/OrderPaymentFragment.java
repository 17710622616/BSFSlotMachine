package com.bs.john_li.bsfslotmachine.BSSMFragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bs.john_li.bsfslotmachine.BSSMActivity.Forum.ArticleDetialActivity;
import com.bs.john_li.bsfslotmachine.BSSMActivity.Mine.CarListActivity;
import com.bs.john_li.bsfslotmachine.BSSMActivity.Mine.OrderDetialActivity;
import com.bs.john_li.bsfslotmachine.BSSMAdapter.ContentsAdapter;
import com.bs.john_li.bsfslotmachine.BSSMAdapter.SmartOrderRefreshAdapter;
import com.bs.john_li.bsfslotmachine.BSSMAdapter.SmartRefreshAdapter;
import com.bs.john_li.bsfslotmachine.BSSMModel.CarModel;
import com.bs.john_li.bsfslotmachine.BSSMModel.OrderModel;
import com.bs.john_li.bsfslotmachine.BSSMModel.UserOrderOutModel;
import com.bs.john_li.bsfslotmachine.BSSMUtils.BSSMCommonUtils;
import com.bs.john_li.bsfslotmachine.BSSMUtils.BSSMConfigtor;
import com.bs.john_li.bsfslotmachine.BSSMUtils.SPUtils;
import com.bs.john_li.bsfslotmachine.R;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
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
import java.util.List;

/**
 * 待支付訂單
 * Created by John_Li on 5/1/2018.
 */

public class OrderPaymentFragment extends BaseFragment {
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
    private long totolCarCount;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_orderlist, null);
        initView();
        setListenter();
        initData();
        return view;
    }

    @Override
    public void initView() {
        mRefreshLayout = view.findViewById(R.id.order_list_srl);
        mRecycleView = view.findViewById(R.id.order_list_lv);
        noOrderLL = view.findViewById(R.id.no_order_ll);

        // 设置header的高度
        mRefreshLayout.setHeaderHeightPx((int)(BSSMCommonUtils.getDeviceWitdh(getActivity()) / 4.05));//Header标准高度（显示下拉高度>=标准高度 触发刷新）
    }

    @Override
    public void setListenter() {
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                orderList.clear();
                pageNo = 1;
                callNetGetCarList();
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
                    callNetGetCarList();
                }
            }
        });
    }

    @Override
    public void initData() {
        orderList = new ArrayList<>();
        mSmartOrderRefreshAdapter = new SmartOrderRefreshAdapter(getActivity(), orderList);
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
        mRefreshLayout.autoRefresh();
        callNetGetCarList();
    }

    private void callNetGetCarList() {
        RequestParams params = new RequestParams(BSSMConfigtor.BASE_URL + BSSMConfigtor.GET_ORDER_LIST + SPUtils.get(getActivity().getApplicationContext(), "UserToken", ""));
        params.setAsJsonContent(true);
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("orderStatus",1);
            jsonObj.put("pageSize",pageSize);
            jsonObj.put("pageNo",pageNo);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String urlJson = jsonObj.toString();
        params.setBodyContent(urlJson);
        String uri = params.getUri();
        x.http().request(HttpMethod.POST ,params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                UserOrderOutModel model = new Gson().fromJson(result.toString(), UserOrderOutModel.class);
                if (model.getCode() ==200) {
                    totolCarCount = model.getData().getTotalCount();
                    List<UserOrderOutModel.UserOrderInsideModel.UserOrderModel> orderModelsFromNet = model.getData().getData();
                    orderList.addAll(orderModelsFromNet);
                } else if (model.getCode() == 10001){
                    Toast.makeText(getActivity(), model.getMsg().toString(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), model.getMsg().toString(), Toast.LENGTH_SHORT).show();
                }
            }
            //请求异常后的回调方法
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(getActivity(), getString(R.string.no_net), Toast.LENGTH_SHORT).show();
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

    private void refreshView() {
        if (orderList.size() > 0) {
            noOrderLL.setVisibility(View.GONE);
        } else {
            noOrderLL.setVisibility(View.VISIBLE);
        }
        mSmartOrderRefreshAdapter.refreshListView(orderList);
        mRefreshLayout.finishRefresh();
        mRefreshLayout.finishLoadmore();
    }
}
