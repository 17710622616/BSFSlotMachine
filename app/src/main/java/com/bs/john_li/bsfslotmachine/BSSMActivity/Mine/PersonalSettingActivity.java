package com.bs.john_li.bsfslotmachine.BSSMActivity.Mine;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;

import com.bs.john_li.bsfslotmachine.BSSMActivity.BaseActivity;
import com.bs.john_li.bsfslotmachine.BSSMView.BSSMHeadView;
import com.bs.john_li.bsfslotmachine.R;

/**
 * 個人設置，賬戶信息
 * Created by John_Li on 4/8/2017.
 */

public class PersonalSettingActivity extends BaseActivity implements View.OnClickListener{
    private BSSMHeadView personalHead;
    private LinearLayout headPortraitLL, usernameLL, phoneLL, pwLL, payPwLL;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_setting);
        initView();
        setListener();
        initData();
    }

    @Override
    public void initView() {
        personalHead = (BSSMHeadView) findViewById(R.id.personal_head);
        headPortraitLL = findViewById(R.id.personal_head_portrait);
        usernameLL = findViewById(R.id.personal_username);
        phoneLL = findViewById(R.id.personal_phone);
        pwLL = findViewById(R.id.personal_pw);
        payPwLL = findViewById(R.id.personal_pay_pw);
    }

    @Override
    public void setListener() {
        headPortraitLL.setOnClickListener(this);
        usernameLL.setOnClickListener(this);
        phoneLL.setOnClickListener(this);
        pwLL.setOnClickListener(this);
        payPwLL.setOnClickListener(this);
    }

    @Override
    public void initData() {
        personalHead.setTitle("賬戶信息");
        personalHead.setLeft(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.head_left:
                finish();
                break;
            case R.id.personal_head_portrait:
                startActivityForResult(new Intent(this, HeadViewActivity.class), 7);
                break;
            case R.id.personal_username:
                finish();
                break;
            case R.id.personal_phone:
                finish();
                break;
            case R.id.personal_pw:
                finish();
                break;
            case R.id.personal_pay_pw:
                finish();
                break;
        }
    }
}
