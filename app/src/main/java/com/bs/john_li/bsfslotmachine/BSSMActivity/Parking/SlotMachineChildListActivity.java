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
import com.bs.john_li.bsfslotmachine.BSSMAdapter.SlotMachineChildListAdapter;
import com.bs.john_li.bsfslotmachine.BSSMAdapter.SlotMachineListAdapter;
import com.bs.john_li.bsfslotmachine.BSSMModel.SlotMachineListModel;
import com.bs.john_li.bsfslotmachine.BSSMUtils.BSSMConfigtor;
import com.bs.john_li.bsfslotmachine.BSSMView.BSSMHeadView;
import com.bs.john_li.bsfslotmachine.R;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * 咪錶子車位列表
 * Created by John_Li on 26/9/2017.
 */

public class SlotMachineChildListActivity extends BaseActivity implements View.OnClickListener{
    private static final int radius = 100;
    private BSSMHeadView headView;
    private GridView smGv;

    private String mAddress;
    private String mLatitude;
    private String mLongitude;
    private SlotMachineChildListAdapter mAdapter;
    private SlotMachineListModel.SlotMachineModel mSlotMachineModel;
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
        smGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(SlotMachineChildListActivity.this, ParkingOrderActivity.class);
                intent.putExtra("way", BSSMConfigtor.SLOT_MACHINE_EXIST);
                intent.putExtra("SlotMachine", new Gson().toJson(mSlotMachineModel));
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void initData() {
        // 獲取數據
        Intent intent = getIntent();
        mSlotMachineModel = new Gson().fromJson(intent.getStringExtra("SlotMachineModel"), SlotMachineListModel.SlotMachineModel.class);
        headView.setLeft(this);
        if (mSlotMachineModel != null) {
            // 配置headview
            String titleAddress = null;
            if (mSlotMachineModel.getAddress().length() > 8) {
                titleAddress = mSlotMachineModel.getAddress().substring(0, 8);
            } else {
                titleAddress = mSlotMachineModel.getAddress();
            }
            headView.setTitle(titleAddress + "的子列表");

            mAdapter = new SlotMachineChildListAdapter(mSlotMachineModel.getParkingSpaces(), this);
            smGv.setAdapter(mAdapter);
        } else {
            Toast.makeText(this, "0.0獲取子列表失敗，請重試~",Toast.LENGTH_SHORT).show();
            finish();
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
