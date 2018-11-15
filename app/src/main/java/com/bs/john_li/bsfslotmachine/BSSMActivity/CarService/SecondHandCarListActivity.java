package com.bs.john_li.bsfslotmachine.BSSMActivity.CarService;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.bs.john_li.bsfslotmachine.BSSMActivity.BaseActivity;
import com.bs.john_li.bsfslotmachine.BSSMView.BSSMHeadView;
import com.bs.john_li.bsfslotmachine.R;

/**
 * Created by John_Li on 15/11/2018.
 */

public class SecondHandCarListActivity extends BaseActivity implements View.OnClickListener{
    private BSSMHeadView headView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_hand_car_list);
        initView();
        setListener();
        initData();
    }

    @Override
    public void initView() {
        headView = findViewById(R.id.shcl_headview);
    }

    @Override
    public void setListener() {

    }

    @Override
    public void initData() {
        headView.setLeft(this);
        headView.setTitle("二手車買賣");
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
