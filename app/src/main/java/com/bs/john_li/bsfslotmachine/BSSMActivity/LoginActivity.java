package com.bs.john_li.bsfslotmachine.BSSMActivity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bs.john_li.bsfslotmachine.BSSMUtils.BSSMConfigtor;
import com.bs.john_li.bsfslotmachine.BSSMView.BSSMHeadView;
import com.bs.john_li.bsfslotmachine.R;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * 登錄界面
 * Created by John on 11/9/2017.
 */

public class LoginActivity extends BaseActivity implements View.OnClickListener{
    private BSSMHeadView head;
    private EditText usernameEt,pwEt;
    private TextView loginTv;

    private String username, pw;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        setListener();
        initData();
    }

    @Override
    public void initView() {
        head = findViewById(R.id.login_head);
        usernameEt = findViewById(R.id.login_username);
        pwEt = findViewById(R.id.login_pw);
        loginTv = findViewById(R.id.login_tv);
    }

    @Override
    public void setListener() {
        loginTv.setOnClickListener(this);
    }

    @Override
    public void initData() {
        head.setTitle(getString(R.string.login_btn));
        head.setRightText(getString(R.string.register_btn), this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.head_right_tv:
                break;
            case R.id.login_tv:
                checkUP();
                break;
        }
    }

    /**
         * 確認編輯框是否不為空
         */
    private void checkUP() {
        username = usernameEt.getText().toString();
        pw = pwEt.getText().toString();
        if (username != null && pw != null && !username.equals("") && !pw.equals("") ) {
            doLogin(username, pw);
        } else {
            Toast.makeText(LoginActivity.this, getString(R.string.up_not_null), Toast.LENGTH_SHORT).show();
        }
    }

    /**
         * 登錄的方法
         */
    private void doLogin(String username, String pw) {
        RequestParams params = new RequestParams(BSSMConfigtor.TEST_IP + BSSMConfigtor.USER_LOGIN);
        params.addHeader("mobile",username);
        params.addHeader("password",pw);
        params.addHeader("osVersion",BSSMConfigtor.OS_TYPE);
        params.addHeader("osType",BSSMConfigtor.OS_TYPE);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Toast.makeText(LoginActivity.this, getString(R.string.login_success), Toast.LENGTH_SHORT).show();
                LoginActivity.this.finish();
            }
            //请求异常后的回调方法
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(LoginActivity.this, getString(R.string.login_fail), Toast.LENGTH_SHORT).show();
            }
            //主动调用取消请求的回调方法
            @Override
            public void onCancelled(CancelledException cex) {
            }
            @Override
            public void onFinished() {
            }
        });
    }
}
