package com.bs.john_li.bsfmerchantsversionapp;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bs.john_li.bsfmerchantsversionapp.Model.UserLoginOutModel;
import com.bs.john_li.bsfmerchantsversionapp.Utils.BSFMerchantConfigtor;
import com.bs.john_li.bsfmerchantsversionapp.Utils.DigestUtils;
import com.bs.john_li.bsfmerchantsversionapp.Utils.SPUtils;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * Created by John on 18/11/2018.
 */

public class LoginActivity extends FragmentActivity  implements View.OnClickListener{
    private EditText usernameEt,pwEt;
    private TextView loginTv, notLoginTv, forgetPwTv;

    private String username, pw;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        setListener();
        initData();
    }

    public void initView() {
        usernameEt = findViewById(R.id.login_username);
        pwEt = findViewById(R.id.login_pw);
        loginTv = findViewById(R.id.login_tv);
    }

    public void setListener() {
        loginTv.setOnClickListener(this);
    }

    public void initData() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
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
            Toast.makeText(LoginActivity.this, "賬戶密碼唔可以為空！", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 登錄
     */
    private void doLogin(String username, String pw) {
        dialog = new ProgressDialog(this);
        dialog.setTitle("提示");
        dialog.setMessage("正在登錄中......");
        dialog.setCancelable(false);
        dialog.show();
        RequestParams params = new RequestParams(BSFMerchantConfigtor.BASE_URL + BSFMerchantConfigtor.SELLER_LOGIN);
        params.setAsJsonContent(true);
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("account",username);
            jsonObj.put("pwd", DigestUtils.encryptPw(pw));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String urlJson = jsonObj.toString();
        params.setBodyContent(urlJson);
        params.setConnectTimeout(30 * 1000);
        x.http().request(HttpMethod.POST ,params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                UserLoginOutModel model = new Gson().fromJson(result.toString(), UserLoginOutModel.class);
                if (model.getCode() == 200) {
                    SPUtils.put(LoginActivity.this, "SellerUserToken", model.getData().getSellertoken());
                    SPUtils.put(LoginActivity.this, "SellerUserName", model.getData().getUsername());
                    Toast.makeText(LoginActivity.this, "登錄成功！", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "登錄失敗！" + String.valueOf(model.getMsg()), Toast.LENGTH_SHORT).show();
                }
            }
            //请求异常后的回调方法
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if (ex instanceof java.net.SocketTimeoutException) {
                    Toast.makeText(LoginActivity.this, "網絡連接超時，請重試", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LoginActivity.this, "登錄失敗，請重新提交", Toast.LENGTH_SHORT).show();
                }
            }
            //主动调用取消请求的回调方法
            @Override
            public void onCancelled(CancelledException cex) {
            }
            @Override
            public void onFinished() {
                dialog.dismiss();
            }
        });
    }


    /**
     * 不可返回
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }
}
