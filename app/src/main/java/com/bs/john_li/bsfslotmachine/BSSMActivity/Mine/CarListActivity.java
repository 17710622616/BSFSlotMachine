package com.bs.john_li.bsfslotmachine.BSSMActivity.Mine;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ListView;

import com.bs.john_li.bsfslotmachine.BSSMActivity.BaseActivity;
import com.bs.john_li.bsfslotmachine.BSSMAdapter.CarListAdapter;
import com.bs.john_li.bsfslotmachine.BSSMView.BSSMHeadView;
import com.bs.john_li.bsfslotmachine.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的車輛列表
 * Created by John_Li on 9/8/2017.
 */

public class CarListActivity extends BaseActivity implements View.OnClickListener {
    private BSSMHeadView carListHead;
    private ListView carLv;
    private List<String> carList;
    private CarListAdapter mCarListAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_list);
        initView();
        setListener();
        initData();
    }

    @Override
    public void initView() {
        carListHead = findViewById(R.id.carlist_head);
        carLv = findViewById(R.id.carlist_lv);
    }

    @Override
    public void setListener() {

    }

    @Override
    public void initData() {
        carListHead.setTitle("我的車輛");
        carListHead.setLeft(this);
        carListHead.setRight(R.mipmap.push_invitation, this);

        carList = new ArrayList<String>();
        carList.add("私家車");
        carList.add("電單車");

        mCarListAdapter = new CarListAdapter(this, carList);
        carLv.setAdapter(mCarListAdapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.head_left:
                finish();
                break;
            case R.id.head_right:
                break;
        }
    }
}
