package com.bs.john_li.bsfslotmachine.BSSMFragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bs.john_li.bsfslotmachine.BSSMActivity.Mine.OrderDetialActivity;
import com.bs.john_li.bsfslotmachine.BSSMAdapter.SmartOrderRefreshAdapter;
import com.bs.john_li.bsfslotmachine.BSSMModel.UserOrderOutModel;
import com.bs.john_li.bsfslotmachine.R;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 已投幣訂單
 * Created by John_Li on 5/1/2018.
 */

public class OrderCompletedFragment extends LazyLoadFragment {
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
    }

    private void setListener() {
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

    private void initData() {
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
        callNetGetCarList();
    }

    /**
     * 请求网络刷新数据
     */
    private void callNetGetCarList() {
        if (orderList == null) {
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
        mRefreshLayout.finishLoadmore();
    }
}
