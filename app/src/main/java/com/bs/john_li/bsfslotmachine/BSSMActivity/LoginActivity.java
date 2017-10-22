package com.bs.john_li.bsfslotmachine.BSSMActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bs.john_li.bsfslotmachine.BSSMModel.CommonModel;
import com.bs.john_li.bsfslotmachine.BSSMModel.UserInfoOutsideModel;
import com.bs.john_li.bsfslotmachine.BSSMUtils.BSSMConfigtor;
import com.bs.john_li.bsfslotmachine.BSSMUtils.SPUtils;
import com.bs.john_li.bsfslotmachine.BSSMView.BSSMHeadView;
import com.bs.john_li.bsfslotmachine.R;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * 登錄界面
 * Created by John on 11/9/2017.
 */

public class LoginActivity extends BaseActivity implements View.OnClickListener{
    private BSSMHeadView head;
    private EditText usernameEt,pwEt;
    private TextView loginTv, notLoginTv;

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
        notLoginTv = findViewById(R.id.not_login_now_tv);
    }

    @Override
    public void setListener() {
        loginTv.setOnClickListener(this);
        notLoginTv.setOnClickListener(this);
    }

    @Override
    public void initData() {
        head.setTitle(getString(R.string.login_btn));
        head.setRightText(getString(R.string.register_btn), this);
        head.setLeft(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.head_right_tv:
                startActivityForResult(new Intent(this, RegisterActivity.class), BSSMConfigtor.REQUEST_CODE);
                break;
            case R.id.head_left:
                finish();
                break;
            case R.id.login_tv:
                checkUP();
                break;
            case R.id.not_login_now_tv:
                finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            Toast.makeText(this, "註冊失敗╮(╯▽╰)╭", Toast.LENGTH_SHORT).show();
            return;
        }

        switch (requestCode) {
            case 100:     // 注册返回
                doLogin(data.getStringExtra("mobile"), data.getStringExtra("password"));
                //finish();
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
         * 登錄
         */
    private void doLogin(String username, String pw) {
        RequestParams params = new RequestParams(BSSMConfigtor.BASE_URL + BSSMConfigtor.USER_LOGIN);
        params.setAsJsonContent(true);
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("mobile",username);
            jsonObj.put("password",pw);
            jsonObj.put("osVersion",BSSMConfigtor.OS_TYPE);
            jsonObj.put("osType",BSSMConfigtor.OS_TYPE);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String urlJson = jsonObj.toString();
        params.setBodyContent(urlJson);
        String uri = params.getUri();
        x.http().request(HttpMethod.POST ,params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                CommonModel model = new Gson().fromJson(result.toString(), CommonModel.class);
                if (model.getCode().equals("200")) {
                    SPUtils.put(LoginActivity.this, "UserToken", model.getData().toString());
                    Toast.makeText(LoginActivity.this, getString(R.string.login_success), Toast.LENGTH_SHORT).show();
                    //setResult(BSSMConfigtor.LOGIN_FOR_RESULT);
                    getUserInfo(model.getData().toString());
                } else {
                    Toast.makeText(LoginActivity.this, getString(R.string.login_fail), Toast.LENGTH_SHORT).show();
                }
            }
            //请求异常后的回调方法
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(LoginActivity.this, getString(R.string.no_net), Toast.LENGTH_SHORT).show();
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

    /**
     * 獲取用戶信息
     * @param token
     */
    private void getUserInfo(String token) {
        RequestParams params = new RequestParams(BSSMConfigtor.BASE_URL + BSSMConfigtor.GET_USER_INFO + token);
        x.http().request(HttpMethod.GET ,params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                UserInfoOutsideModel model = new Gson().fromJson(result.toString(), UserInfoOutsideModel.class);
                if (model.getCode() == 200) {
                    if (model.getData().getNickname() == null) {
                        model.getData().setNickname("用戶");
                    }
                    if (model.getData().getAddress() == null) {
                        model.getData().setAddress("");
                    }
                    if (model.getData().getRealname() == null) {
                        model.getData().setRealname("");
                    }
                    if (model.getData().getHeadimg() == null) {
                        model.getData().setAddress("objectNam1");
                    }
                    if (model.getData().getDescx() == null) {
                        model.getData().setDescx("");
                    }
                    if (model.getData().getIdcardno() == null) {
                        model.getData().setIdcardno("");
                    }
                    if (model.getData().getBirthday() == null) {
                        model.getData().setBirthday("");
                    }
                    String userInfoJson = new Gson().toJson(model.getData());
                    SPUtils.put(LoginActivity.this, "UserInfo", userInfoJson);
                    Log.d("getUserURI", "獲取用戶信息成功");
                    EventBus.getDefault().post("LOGIN");
                    finish();
                } else {
                    Log.d("getUserURI", "獲取用戶信息失敗");
                }
            }
            //请求异常后的回调方法
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.d("getUserURI", "獲取用戶信息失敗");
                //Toast.makeText(LoginActivity.this, getString(R.string.no_net), Toast.LENGTH_SHORT).show();
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
