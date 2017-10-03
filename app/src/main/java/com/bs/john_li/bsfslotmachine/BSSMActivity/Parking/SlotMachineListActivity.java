package com.bs.john_li.bsfslotmachine.BSSMActivity.Parking;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bs.john_li.bsfslotmachine.BSSMActivity.BaseActivity;
import com.bs.john_li.bsfslotmachine.BSSMAdapter.SlotMachineListAdapter;
import com.bs.john_li.bsfslotmachine.BSSMModel.SlotMachineListOutsideModel;
import com.bs.john_li.bsfslotmachine.BSSMUtils.BSSMConfigtor;
import com.bs.john_li.bsfslotmachine.BSSMView.BSSMHeadView;
import com.bs.john_li.bsfslotmachine.BSSMView.ExpandSwipeRefreshLayout;
import com.bs.john_li.bsfslotmachine.R;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * 咪錶列表
 * Created by John_Li on 20/9/2017.
 */

public class SlotMachineListActivity extends BaseActivity implements View.OnClickListener{
    private static final int radius = 100;
    private BSSMHeadView headView;
    private GridView smGv;
    private LinearLayout smLL;
    private ExpandSwipeRefreshLayout smSwipe;

    private String mAddress;
    private String mLatitude;
    private String mLongitude;
    private SlotMachineListAdapter mAdapter;
    private List<SlotMachineListOutsideModel.SlotMachineListModel.SlotMachineModel> smList;
    private int totalCount;
    private int pageSize = 10;
    private int pageNo = 1;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slotmachine_list);
        initView();
        setListener();
        initData();
    }

    @Override
    public void initView() {
        headView = findViewById(R.id.sm_list_head);
        smGv = findViewById(R.id.sm_list_gv);
        smLL = findViewById(R.id.sm_list_ll);
        smSwipe = findViewById(R.id.sm_list_swipe);
    }

    @Override
    public void setListener() {
        smGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = null;
                if (smList.get(i).getParkingSpaces() == null) { //沒有子列表
                    intent = new Intent(SlotMachineListActivity.this, ParkingOrderActivity.class);
                    intent.putExtra("way", BSSMConfigtor.SLOT_MACHINE_EXIST);
                    intent.putExtra("SlotMachine", new Gson().toJson(smList.get(i)));
                } else {    // 有子列表
                    intent = new Intent(SlotMachineListActivity.this, SlotMachineChildListActivity.class);
                    intent.putExtra("SlotMachineModel", new Gson().toJson(smList.get(i)));
                }
                startActivity(intent);
            }
        });
        smLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SlotMachineListActivity.this, SearchSlotMachineActivity.class));
                finish();
            }
        });

        smSwipe.setOnLoadListener(new ExpandSwipeRefreshLayout.OnLoadListener() {
            @Override
            public void onLoad() {
                smSwipe.postDelayed(new Runnable() {
                    @Override
                    public void run() { //和最大的数据比较
                        if (pageSize * (pageNo + 1) > totalCount){
                            Toast.makeText(SlotMachineListActivity.this, "沒有更多數據了誒~", Toast.LENGTH_SHORT).show();
                            smSwipe.setLoading(false);
                        } else {
                            pageNo ++;
                            callNetGetSlotMachineList();
                        }
                    }
                }, 500);
            }
        });

        smSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                smList.clear();
                pageNo = 1;
                callNetGetSlotMachineList();
            }
        });
    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        mAddress = intent.getStringExtra("Address");
        mLatitude = intent.getStringExtra("Latitude");
        mLongitude = intent.getStringExtra("Longitude");
        headView.setLeft(this);
        smList = new ArrayList<>();

        if (mAddress != null) {
            String titleAddress = null;
            if (mAddress.length() > 8) {
                titleAddress = mAddress.substring(0, 8);
            }
            headView.setTitle(titleAddress + "附近的咪錶");
        } else {
            Toast.makeText(this, "0.0獲取列表失敗誒~",Toast.LENGTH_SHORT).show();
        }
        if (mLatitude != null  && mLongitude != null) {
            smSwipe.setRefreshing(true);
            callNetGetSlotMachineList();
        } else {
            Toast.makeText(this, "定位信息錯誤，請重新定位~", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    /**
     * 請求獲取咪錶列表
     */
    private void callNetGetSlotMachineList() {
        RequestParams params = new RequestParams(BSSMConfigtor.BASE_URL + BSSMConfigtor.USER_LOCATION);
        params.setAsJsonContent(true);params.setAsJsonContent(true);
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("longitude","113.560976");//mLongitude"113.560976"
            jsonObj.put("latitude","22.191441");//mLatitude"22.191441"
            jsonObj.put("radius",radius);
            jsonObj.put("pageSize",pageSize);
            jsonObj.put("pageNo",pageSize);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String urlJson = jsonObj.toString();
        params.setBodyContent(urlJson);
        x.http().request(HttpMethod.POST, params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                SlotMachineListOutsideModel model = new Gson().fromJson(result.toString(), SlotMachineListOutsideModel.class);
                if (model.getCode() == 200) {
                    if (model.getData() != null) {
                        totalCount = model.getData().getTotalCount();
                        smList = model.getData().getData();
                    }
                } else if (model.getCode() == 10001){
                    Toast.makeText(SlotMachineListActivity.this, getString(R.string.check_user_fail), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SlotMachineListActivity.this, model.getCode() + model.getMsg().toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(SlotMachineListActivity.this, "網絡連接錯誤誒~~~", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                updateUIAfterGetData();
            }
        });
    }

    /**
     * 獲取數據后刷新界面
     */
    private void updateUIAfterGetData() {
        if (smList.size() > 0) {
            smLL.setVisibility(View.GONE);
        } else {
            smLL.setVisibility(View.VISIBLE);
        }
        mAdapter = new SlotMachineListAdapter(smList, SlotMachineListActivity.this);
        smGv.setAdapter(mAdapter);
        if (smSwipe.isRefreshing()) {
            smSwipe.setRefreshing(false);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.head_left:
                finish();
                break;
        }
    }
}
