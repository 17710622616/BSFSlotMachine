package com.bs.john_li.bsfslotmachine.BSSMActivity.Parking;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.bs.john_li.bsfslotmachine.BSSMActivity.BaseActivity;
import com.bs.john_li.bsfslotmachine.BSSMActivity.Mine.CarListActivity;
import com.bs.john_li.bsfslotmachine.BSSMAdapter.SearchSlotMachineAdapter;
import com.bs.john_li.bsfslotmachine.BSSMAdapter.SearchSlotMchineListAdapter;
import com.bs.john_li.bsfslotmachine.BSSMModel.SlotMachineListOutsideModel;
import com.bs.john_li.bsfslotmachine.BSSMUtils.BSSMConfigtor;
import com.bs.john_li.bsfslotmachine.BSSMView.BSSMHeadView;
import com.bs.john_li.bsfslotmachine.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.x;

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
    private int pageNo = 1;
    private int pageSize = 10;
    private String textChange = "";

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
                smList.clear();
                pageNo = 1;
                callNetChangeData();
            }
        });
        mRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                //和最大的数据比较
                if (pageSize * (pageNo + 1) > totalCount){
                    Toast.makeText(SearchSlotMachineListActivity.this, "沒有更多數據了誒~", Toast.LENGTH_SHORT).show();
                    mRefreshLayout.finishRefresh();
                    mRefreshLayout.finishLoadmore();
                } else {
                    pageNo ++;
                    callNetChangeData();
                }
            }
        });
    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        headView.setTitle("查看更多");
        headView.setLeft(this);

        totalCount = Long.parseLong(intent.getStringExtra("totalCount"));
        textChange = intent.getStringExtra("textChange");
        Type type = new TypeToken<List<SlotMachineListOutsideModel.SlotMachineListModel.SlotMachineModel>>() { }.getType();
        smList = new Gson().fromJson(intent.getStringExtra("smList"), type);
        smList.remove(smList.size() - 1);
        mSearchSlotMachineAdapter = new SearchSlotMchineListAdapter(smList, this);
        mSearchSlotMachineAdapter.setOnItemClickListenr(new SearchSlotMchineListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intentClick = null;
                if (smList.get(position).getParkingSpaces() != null) {// 有子列表
                    intentClick = new Intent(SearchSlotMachineListActivity.this, SlotMachineChildListActivity.class);
                    intentClick.putExtra("SlotMachineModel", new Gson().toJson(smList.get(position)));
                } else {// 無子列表
                    intentClick = new Intent(SearchSlotMachineListActivity.this, ParkingOrderActivity.class);
                    intentClick.putExtra("way", BSSMConfigtor.SLOT_MACHINE_EXIST);
                    intentClick.putExtra("SlotMachine", new Gson().toJson(smList.get(position)));
                }
                startActivity(intentClick);
                finish();
            }
        });
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

    /**
     * 請求網絡搜索咪錶號
     */
    private void callNetChangeData() {
        RequestParams params = new RequestParams(BSSMConfigtor.BASE_URL + BSSMConfigtor.SEARCH_SLOT_MACHINE);
        params.setAsJsonContent(true);
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("key",textChange);
            jsonObj.put("pageSize",pageSize);
            jsonObj.put("pageNo",pageNo);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        params.setBodyContent(jsonObj.toString());
        String uri = params.getUri();
        x.http().request(HttpMethod.POST, params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                SlotMachineListOutsideModel model = new Gson().fromJson(result.toString(), SlotMachineListOutsideModel.class);
                if (model.getCode() == 200) {
                    if (model.getData() != null) {
                        totalCount = model.getData().getTotalCount();
                        smList.addAll(model.getData().getData());
                    } else {
                        Toast.makeText(SearchSlotMachineListActivity.this, "獲取咪錶錯誤！", Toast.LENGTH_SHORT).show();
                    }
                } else if (model.getCode() == 10001){
                    Toast.makeText(SearchSlotMachineListActivity.this, model.getMsg().toString(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SearchSlotMachineListActivity.this, model.getMsg().toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(SearchSlotMachineListActivity.this, "獲取咪錶錯誤！", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                mSearchSlotMachineAdapter.notifyDataSetChanged();
                mRefreshLayout.finishRefresh();
                mRefreshLayout.finishLoadmore();
            }
        });
    }
}
