package com.bs.john_li.bsfslotmachine.BSSMActivity.Parking;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.GridView;
import android.widget.Toast;

import com.bs.john_li.bsfslotmachine.BSSMActivity.BaseActivity;
import com.bs.john_li.bsfslotmachine.BSSMAdapter.SlotMachineListAdapter;
import com.bs.john_li.bsfslotmachine.BSSMModel.CommonModel;
import com.bs.john_li.bsfslotmachine.BSSMModel.SlotMachineModel;
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

    private String mAddress;
    private String mLatitude;
    private String mLongitude;
    private SlotMachineListAdapter mAdapter;
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
    }

    @Override
    public void setListener() {

    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        mAddress = intent.getStringExtra("Address");
        mLatitude = intent.getStringExtra("Latitude");
        mLongitude = intent.getStringExtra("Longitude");
        headView.setLeft(this);
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
            jsonObj.put("longitude",mLatitude);
            jsonObj.put("latitude",mLongitude);
            jsonObj.put("radius",radius);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String urlJson = jsonObj.toString();
        params.setBodyContent(urlJson);
        x.http().request(HttpMethod.POST, params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                CommonModel model = new Gson().fromJson(result.toString(), CommonModel.class);
                if (model.getCode().equals("200") || model.getCode().equals("10001")) {
                    List<SlotMachineModel> testList = new ArrayList<SlotMachineModel>();
                    for (int i = 0; i <= 10; i++) {
                        SlotMachineModel model1 = new SlotMachineModel();
                        model1.setMachineNo("#0000" + i);
                        testList.add(model1);
                        mAdapter = new SlotMachineListAdapter(testList, SlotMachineListActivity.this);
                        smGv.setAdapter(mAdapter);
                    }

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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.head_left:
                finish();
                break;
        }
    }
}
