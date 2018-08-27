package com.bs.john_li.bsfslotmachine.BSSMActivity.Mine;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.bs.john_li.bsfslotmachine.BSSMActivity.BaseActivity;
import com.bs.john_li.bsfslotmachine.BSSMView.BSSMHeadView;
import com.bs.john_li.bsfslotmachine.R;

/**
 * Created by John_Li on 27/8/2018.
 */

public class ServiceActivity extends BaseActivity implements View.OnClickListener{
    private BSSMHeadView headView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);
        initView();
        setListener();
        initData();
    }

    @Override
    public void initView() {
        headView = findViewById(R.id.service_head);
    }

    @Override
    public void setListener() {
        headView.setLeft(this);
        headView.setTitle("客服聯繫");
    }

    @Override
    public void initData() {

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
