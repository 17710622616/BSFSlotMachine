package com.bs.john_li.bsfslotmachine.BSSMActivity.Parking;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bs.john_li.bsfslotmachine.BSSMActivity.BaseActivity;
import com.bs.john_li.bsfslotmachine.BSSMAdapter.SlotMachineListAdapter;
import com.bs.john_li.bsfslotmachine.BSSMModel.SlotMachineListModel;
import com.bs.john_li.bsfslotmachine.BSSMUtils.BSSMConfigtor;
import com.bs.john_li.bsfslotmachine.BSSMView.BSSMHeadView;
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

    private String mAddress;
    private String mLatitude;
    private String mLongitude;
    private SlotMachineListAdapter mAdapter;
    private List<SlotMachineListModel.SlotMachineModel> smList;
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
            callNetGetSlotMachineList(mLatitude, mLongitude);
        } else {
            Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 請求獲取咪錶列表
     * @param mLatitude
     * @param mLongitude
     */
    private void callNetGetSlotMachineList(String mLatitude, String mLongitude) {
        RequestParams params = new RequestParams(BSSMConfigtor.BASE_URL + BSSMConfigtor.USER_LOCATION);
        params.setAsJsonContent(true);params.setAsJsonContent(true);
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("longitude",mLongitude);//mLongitude"113.560976"
            jsonObj.put("latitude",mLatitude);//mLatitude"22.191441"
            jsonObj.put("radius",radius);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String urlJson = jsonObj.toString();
        params.setBodyContent(urlJson);
        x.http().request(HttpMethod.POST, params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                SlotMachineListModel model = new Gson().fromJson(result.toString(), SlotMachineListModel.class);
                if (model.getCode() == 200) {
                    if (model.getData() != null) {
                        smList = model.getData();
                    }
                    updateUIAfterGetData();
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
