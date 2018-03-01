package com.bs.john_li.bsfslotmachine.BSSMActivity.Mine;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.bs.john_li.bsfslotmachine.BSSMActivity.BaseActivity;
import com.bs.john_li.bsfslotmachine.BSSMUtils.BSSMCommonUtils;
import com.bs.john_li.bsfslotmachine.BSSMView.BSSMHeadView;
import com.bs.john_li.bsfslotmachine.R;

/**
 * Created by John_Li on 1/3/2018.
 */

public class AboutUsActivity extends BaseActivity implements View.OnClickListener{
    private BSSMHeadView headView;
    private TextView vercodeTv;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        initView();
        setListener();
        initData();
    }

    @Override
    public void initView() {
        headView = findViewById(R.id.about_head);
        vercodeTv = findViewById(R.id.about_vercode);
    }

    @Override
    public void setListener() {

    }

    @Override
    public void initData() {
        headView.setTitle("關於易停車");
        headView.setLeft(this);
        vercodeTv.setText("version:" + BSSMCommonUtils.getVerCode(this.getApplicationContext()));
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
