package com.bs.john_li.bsfslotmachine.BSSMActivity.Parking;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebView;

import com.bs.john_li.bsfslotmachine.BSSMActivity.BaseActivity;
import com.bs.john_li.bsfslotmachine.BSSMUtils.BSSMConfigtor;
import com.bs.john_li.bsfslotmachine.BSSMUtils.SPUtils;
import com.bs.john_li.bsfslotmachine.BSSMView.BSSMHeadView;
import com.bs.john_li.bsfslotmachine.R;

/**
 * Created by John_Li on 28/3/2018.
 */

public class CourseActivity extends BaseActivity implements View.OnClickListener{
    private BSSMHeadView headView;
    private WebView mWebView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_fqa);
        initView();
        setListener();
        initData();
    }

    @Override
    public void initView() {
        headView = findViewById(R.id.wallet_FAQ_head);
        mWebView = findViewById(R.id.wallet_FAQ_wv);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            headView.setHeadHight();
        }
    }

    @Override
    public void setListener() {
    }

    @Override
    public void initData() {
        headView.setLeft(this);
        headView.setTitle("教程");
        String url = BSSMConfigtor.BASE_URL + BSSMConfigtor.GET_COURSE_WEB;
        mWebView.loadUrl(url);
    }

    @Override
    public void onClick(View v){
            switch (v.getId()) {
                case R.id.head_left:
                    finish();
                    break;
            }
    }
}
