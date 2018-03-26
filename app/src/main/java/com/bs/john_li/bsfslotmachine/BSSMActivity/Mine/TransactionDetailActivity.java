package com.bs.john_li.bsfslotmachine.BSSMActivity.Mine;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.bs.john_li.bsfslotmachine.BSSMActivity.BaseActivity;
import com.bs.john_li.bsfslotmachine.BSSMAdapter.SearchSlotMchineListAdapter;
import com.bs.john_li.bsfslotmachine.BSSMModel.SlotMachineListOutsideModel;
import com.bs.john_li.bsfslotmachine.BSSMModel.TransactionDetialOutModel;
import com.bs.john_li.bsfslotmachine.BSSMUtils.BSSMCommonUtils;
import com.bs.john_li.bsfslotmachine.BSSMUtils.BSSMConfigtor;
import com.bs.john_li.bsfslotmachine.BSSMView.BSSMHeadView;
import com.bs.john_li.bsfslotmachine.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.lang.reflect.Type;
import java.util.List;

/**
 * 交易明細
 * Created by John_Li on 26/3/2018.
 */

public class TransactionDetailActivity extends BaseActivity implements View.OnClickListener {
    private BSSMHeadView tdHead;
    private RefreshLayout mRefreshLayout;
    private RecyclerView mRecycleView;

    // 總數
    private long totalCount;
    // 搜索結果集合
    private List<TransactionDetialOutModel.TransactionDetialModel> smList;
    private SearchSlotMchineListAdapter mSearchSlotMachineAdapter;
    private int pageNo = 1;
    private int pageSize = 10;
    private String textChange = "";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_transaction_detail);
        initView();
        setListener();
        initData();
    }

    @Override
    public void initView() {
        tdHead = findViewById(R.id.transaction_detial_head);
        mRefreshLayout = findViewById(R.id.search_sm_list_srl);
        mRecycleView = findViewById(R.id.search_sm_list_lv);

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
                /*smList.clear();
                pageNo = 1;
                callNetChangeData();*/
            }
        });
        mRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                //和最大的数据比较
                /*if (pageSize * (pageNo + 1) > totalCount){
                    Toast.makeText(SearchSlotMachineListActivity.this, "沒有更多數據了誒~", Toast.LENGTH_SHORT).show();
                    mRefreshLayout.finishRefresh();
                    mRefreshLayout.finishLoadmore();
                } else {
                    pageNo ++;
                    callNetChangeData();
                }*/
            }
        });
    }

    @Override
    public void initData() {
        Intent intent = getIntent();

        tdHead.setLeft(this);
        tdHead.setTitle("交易明細");
        Type type = new TypeToken<List<SlotMachineListOutsideModel.SlotMachineListModel.SlotMachineModel>>() { }.getType();
        smList = new Gson().fromJson(intent.getStringExtra("smList"), type);
        smList.remove(smList.size() - 1);
        /*mSearchSlotMachineAdapter = new SearchSlotMchineListAdapter(smList, this);
        mSearchSlotMachineAdapter.setOnItemClickListenr(new SearchSlotMchineListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intentClick = null;
                if (smList.get(position).getParkingSpaces() != null) {// 有子列表
                    intentClick = new Intent(TransactionDetailActivity.this.toString().this, TransactionDetailActivity.class);
                    intentClick.putExtra("SlotMachineModel", new Gson().toJson(smList.get(position)));
                } else {// 無子列表
                    intentClick = new Intent(TransactionDetailActivity.this, ParkingOrderActivity.class);
                    intentClick.putExtra("way", BSSMConfigtor.SLOT_MACHINE_EXIST);
                    intentClick.putExtra("SlotMachine", new Gson().toJson(smList.get(position)));
                }
                startActivity(intentClick);
                finish();
            }
        });*/
        //mRecycleView.setLayoutManager(new LinearLayoutManager(this));
        //mRecycleView.setAdapter(mSearchSlotMachineAdapter);
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
