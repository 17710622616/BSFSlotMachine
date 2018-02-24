package com.bs.john_li.bsfslotmachine.BSSMActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bs.john_li.bsfslotmachine.BSSMModel.CommonModel;
import com.bs.john_li.bsfslotmachine.BSSMUtils.BSSMConfigtor;
import com.bs.john_li.bsfslotmachine.BSSMUtils.CountDownButtonHelper;
import com.bs.john_li.bsfslotmachine.BSSMView.BSSMHeadView;
import com.bs.john_li.bsfslotmachine.R;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * Created by John on 14/9/2017.
 */

public class ForgetPwActivity extends BaseActivity implements View.OnClickListener{
    private BSSMHeadView headView;
    private EditText registerUn, registerVCode,registerPw;
    private TextView registerGetVC, registerTv;
    private CountDownButtonHelper helper;

    private String verificationCode;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
        setListener();
        initData();
    }

    @Override
    public void initView() {
        headView = findViewById(R.id.register_head);
        registerUn = findViewById(R.id.register_username);
        registerVCode = findViewById(R.id.register_verification_code);
        registerPw = findViewById(R.id.register_pw);
        registerTv = findViewById(R.id.register_tv);
        registerGetVC = findViewById(R.id.register_get_vc);
        registerGetVC.setEnabled(false);
    }

    @Override
    public void setListener() {
        registerTv.setOnClickListener(this);
        registerGetVC.setOnClickListener(this);
        registerUn.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String text = charSequence.toString();
                if (!text.equals("") && text.length() >= 7) {
                    registerGetVC.setEnabled(true);
                } else {
                    registerGetVC.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public void initData() {
        headView.setTitle("修改密碼");
        headView.setLeft(R.mipmap.back, this);

         helper = new CountDownButtonHelper(registerGetVC, "倒計時", 60,1);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.head_left:

                break;
            case R.id.register_tv:
                checkRegisterData();
                break;
            case R.id.register_get_vc:
                helper.setOnFinishListener(new CountDownButtonHelper.OnFinishListener() {
                    @Override
                    public void finish() {
                        registerGetVC.setText(getString(R.string.get_verification_code));
                    }
                });
                helper.start();
                callNetGetVerCode();
                break;
        }
    }

    /**
     * 检查注册信息
     */
    private void checkRegisterData() {
        if(!registerUn.getText().toString().equals("") && !registerVCode.getText().toString().equals("") && !registerPw.getText().toString().equals("")) {
            callNetSubmitRegister();
        } else {
            Toast.makeText(this, getString(R.string.register_not_null), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 请求注册
     */
    private void callNetSubmitRegister() {
        //Toast.makeText(this, "註冊成功！", Toast.LENGTH_SHORT).show();
        RequestParams params = new RequestParams(BSSMConfigtor.BASE_URL + BSSMConfigtor.USER_FORGET_PW);
        params.setAsJsonContent(true);
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("mobile",registerUn.getText().toString());
            jsonObj.put("password",registerPw.getText().toString());
            jsonObj.put("code",registerVCode.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String urlJson = jsonObj.toString();
        params.setBodyContent(urlJson);
        String uri = params.getUri();
        x.http().request(HttpMethod.POST, params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                CommonModel model = new Gson().fromJson(result.toString(), CommonModel.class);
                if (model.getCode().equals("200")) {
                    Intent intent = new Intent();
                    intent.putExtra("mobile", registerUn.getText().toString());
                    intent.putExtra("password", registerPw.getText().toString());
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    Toast.makeText(ForgetPwActivity.this, "注册失败" + model.getMsg() + "~~~", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(ForgetPwActivity.this, getString(R.string.no_net), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    /**
     *
     * 請求獲取驗證碼
     */
    private void callNetGetVerCode() {
        RequestParams params = new RequestParams(BSSMConfigtor.BASE_URL + BSSMConfigtor.GET_VERIFICATION_CODE);
        params.addQueryStringParameter("mobile", registerUn.getText().toString());
        String uri = params.getUri();
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                CommonModel model = new Gson().fromJson(result.toString(), CommonModel.class);
                if (model.getCode() != null) {
                    if (model.getMsg() != null) {
                        if (model.getMsg().equals("验证码已发送！")){
                            Toast.makeText(ForgetPwActivity.this, model.getMsg() + model.getData(), Toast.LENGTH_LONG).show();
                        } else {
                            helper.finishTimer(getString(R.string.get_verification_code));
                            Toast.makeText(ForgetPwActivity.this, getString(R.string.get_verification_code_fail), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        helper.finishTimer(getString(R.string.get_verification_code));
                        Toast.makeText(ForgetPwActivity.this, getString(R.string.get_verification_code_fail), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    helper.finishTimer(getString(R.string.get_verification_code));
                    Toast.makeText(ForgetPwActivity.this, getString(R.string.get_verification_code_fail), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                helper.finishTimer(getString(R.string.get_verification_code));
                Toast.makeText(ForgetPwActivity.this, getString(R.string.get_verification_code_fail), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }
}
