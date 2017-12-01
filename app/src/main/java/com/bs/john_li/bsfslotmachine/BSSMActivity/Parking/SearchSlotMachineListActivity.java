package com.bs.john_li.bsfslotmachine.BSSMActivity.Parking;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.bs.john_li.bsfslotmachine.BSSMActivity.BaseActivity;
import com.bs.john_li.bsfslotmachine.BSSMAdapter.SearchSlotMachineAdapter;
import com.bs.john_li.bsfslotmachine.BSSMAdapter.SearchSlotMchineListAdapter;
import com.bs.john_li.bsfslotmachine.BSSMModel.SlotMachineListOutsideModel;
import com.bs.john_li.bsfslotmachine.BSSMView.BSSMHeadView;
import com.bs.john_li.bsfslotmachine.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * 查询咪表列表，查看更多
 * Created by John_Li on 2/12/2017.
 */

public class SearchSlotMachineListActivity extends BaseActivity implements View.OnClickListener{
    private BSSMHeadView headView;
    private RefreshLayout mRefreshLayout;
    private RecyclerView mRecycleView;

    // 總數
    private long totalCount;
    // 顯示在搜索結果上的名稱集合
    private List<String> slotMachineList;
    // 搜索結果集合
    private List<SlotMachineListOutsideModel.SlotMachineListModel.SlotMachineModel> smList;
    private SearchSlotMchineListAdapter mSearchSlotMachineAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_slot_machine_list);
        initView();
        setListener();
        initData();
    }

    @Override
    public void initView() {
        headView = findViewById(R.id.search_sm_list_head);
        mRefreshLayout = findViewById(R.id.search_sm_list__srl);
        mRecycleView = findViewById(R.id.search_sm_list__lv);
    }

    @Override
    public void setListener() {
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {

            }
        });
        mRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {

            }
        });
    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        headView.setTitle("查看更多");
        headView.setLeft(this);

        totalCount = Long.parseLong(intent.getStringExtra("totalCount"));
        Type type = new TypeToken<List<SlotMachineListOutsideModel.SlotMachineListModel.SlotMachineModel>>() { }.getType();
        smList = new Gson().fromJson(intent.getStringExtra("smList"), type);;
        mSearchSlotMachineAdapter = new SearchSlotMchineListAdapter(smList, this);
        mRecycleView.setLayoutManager(new LinearLayoutManager(this));
        mRecycleView.setAdapter(mSearchSlotMachineAdapter);
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
