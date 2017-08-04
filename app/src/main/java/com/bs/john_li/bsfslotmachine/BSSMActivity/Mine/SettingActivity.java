package com.bs.john_li.bsfslotmachine.BSSMActivity.Mine;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.bs.john_li.bsfslotmachine.BSSMActivity.BaseActivity;
import com.bs.john_li.bsfslotmachine.BSSMView.BSSMHeadView;
import com.bs.john_li.bsfslotmachine.R;

/**
 * 系統設置
 * Created by John_Li on 4/8/2017.
 */

public class SettingActivity extends BaseActivity implements View.OnClickListener {
    private BSSMHeadView settingHead;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activty_setting);
        initView();
        setListener();
        initData();
    }

    @Override
    public void initView() {
        settingHead = (BSSMHeadView) findViewById(R.id.setting_head);
    }

    @Override
    public void setListener() {

    }

    @Override
    public void initData() {
        settingHead.setTitle("設置");
        settingHead.setLeft(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.head_left:
                this.finish();
                break;
        }
    }
}
