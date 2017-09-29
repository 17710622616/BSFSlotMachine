package com.bs.john_li.bsfslotmachine.BSSMActivity.Mine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bs.john_li.bsfslotmachine.BSSMActivity.BaseActivity;
import com.bs.john_li.bsfslotmachine.BSSMModel.UserInfoOutsideModel;
import com.bs.john_li.bsfslotmachine.BSSMUtils.BSSMCommonUtils;
import com.bs.john_li.bsfslotmachine.BSSMUtils.SPUtils;
import com.bs.john_li.bsfslotmachine.BSSMView.BSSMHeadView;
import com.bs.john_li.bsfslotmachine.R;
import com.google.gson.Gson;
import com.othershe.nicedialog.BaseNiceDialog;
import com.othershe.nicedialog.NiceDialog;
import com.othershe.nicedialog.ViewConvertListener;
import com.othershe.nicedialog.ViewHolder;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 個人設置，賬戶信息
 * Created by John_Li on 4/8/2017.
 */

public class PersonalSettingActivity extends BaseActivity implements View.OnClickListener{
    private BSSMHeadView personalHead;
    private LinearLayout headPortraitLL, usernameLL, phoneLL, pwLL, payPwLL;
    private TextView nickNameTv,phoneNumTv;
    private String nickName, phoneNum, loginPw, payPw;
    private UserInfoOutsideModel.UserInfoModel mUserInfoModel;
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
        usernameLL = findViewById(R.id.personal_nickname);
        phoneLL = findViewById(R.id.personal_phone);
        pwLL = findViewById(R.id.personal_pw);
        payPwLL = findViewById(R.id.personal_pay_pw);
        nickNameTv = findViewById(R.id.personal_nickname_tv);
        phoneNumTv = findViewById(R.id.personal_phone_tv);
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

        String userInfoJson = (String) SPUtils.get(this, "UserInfo", "");
        if (!userInfoJson.equals("")) {
            mUserInfoModel = new Gson().fromJson(userInfoJson, UserInfoOutsideModel.UserInfoModel.class);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.head_left:
                finish();
                break;
            case R.id.personal_head_portrait:   // 頭像
                startActivityForResult(new Intent(this, HeadViewActivity.class), 7);
                break;
            case R.id.personal_nickname:    // 暱稱
                NiceDialog.init()
                        .setLayoutId(R.layout.dialog_car_edit)
                        .setConvertListener(new ViewConvertListener() {
                            @Override
                            public void convertView(ViewHolder holder, final BaseNiceDialog dialog) {
                                final EditText editText = holder.getView(R.id.car_edit);
                                editText.setHint("請輸入新的暱稱");
                                BSSMCommonUtils.showKeyboard(editText);
                                holder.setOnClickListener(R.id.car_edit_submit, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        if (!editText.getText().toString().equals("")) {
                                            nickName = editText.getText().toString();
                                            nickNameTv.setText(nickName);
                                            dialog.dismiss();
                                        } else {
                                            Toast.makeText(PersonalSettingActivity.this, "請輸入要修改的新的暱稱~", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        })
                        .setShowBottom(true)
                        .show(getSupportFragmentManager());
                break;
            case R.id.personal_phone:
                NiceDialog.init()
                        .setLayoutId(R.layout.dialog_car_edit)
                        .setConvertListener(new ViewConvertListener() {
                            @Override
                            public void convertView(ViewHolder holder, final BaseNiceDialog dialog) {
                                final EditText editText = holder.getView(R.id.car_edit);
                                editText.setInputType(InputType.TYPE_CLASS_PHONE);
                                editText.setHint("請輸入新的手機號碼");
                                BSSMCommonUtils.showKeyboard(editText);
                                holder.setOnClickListener(R.id.car_edit_submit, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        if (!editText.getText().toString().equals("")) {
                                            phoneNum = editText.getText().toString();
                                            phoneNumTv.setText(phoneNum);
                                            dialog.dismiss();
                                        } else {
                                            Toast.makeText(PersonalSettingActivity.this, "請輸入要修改的手機號碼~", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        })
                        .setShowBottom(true)
                        .show(getSupportFragmentManager());
                break;
            case R.id.personal_pw:
                NiceDialog.init()
                        .setLayoutId(R.layout.dialog_update_pw)
                        .setConvertListener(new ViewConvertListener() {
                            @Override
                            public void convertView(ViewHolder holder, final BaseNiceDialog dialog) {
                                final EditText oldPwEt = holder.getView(R.id.old_pw);
                                final EditText newPwEt = holder.getView(R.id.new_pw);
                                final EditText newPwAffirm = holder.getView(R.id.new_pw_affirm);
                                BSSMCommonUtils.showKeyboard(oldPwEt);
                                oldPwEt.setHint("請輸入舊密碼");
                                newPwEt.setHint("請輸入新密碼");
                                newPwAffirm.setHint("請再次輸入新密碼");
                                holder.setOnClickListener(R.id.update_pw_submit, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        if (!oldPwEt.getText().toString().equals("") && !oldPwEt.getText().toString().equals("") && !oldPwEt.getText().toString().equals("")) {
                                            loginPw = newPwEt.getText().toString();
                                            dialog.dismiss();
                                        } else {
                                            Toast.makeText(PersonalSettingActivity.this, "新舊密碼及確認密碼都不可以為空哦~", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        })
                        .setShowBottom(true)
                        .show(getSupportFragmentManager());
                break;
            case R.id.personal_pay_pw:
                NiceDialog.init()
                        .setLayoutId(R.layout.dialog_update_pw)
                        .setConvertListener(new ViewConvertListener() {
                            @Override
                            public void convertView(ViewHolder holder, final BaseNiceDialog dialog) {
                                final EditText oldPwEt = holder.getView(R.id.old_pw);
                                final EditText newPwEt = holder.getView(R.id.new_pw);
                                final EditText newPwAffirm = holder.getView(R.id.new_pw_affirm);
                                BSSMCommonUtils.showKeyboard(oldPwEt);
                                oldPwEt.setHint("請輸入舊密碼");
                                newPwEt.setHint("請輸入新密碼");
                                newPwAffirm.setHint("請再次輸入新密碼");
                                holder.setOnClickListener(R.id.update_pw_submit, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        if (!oldPwEt.getText().toString().equals("") && !oldPwEt.getText().toString().equals("") && !oldPwEt.getText().toString().equals("")) {
                                            payPw = newPwEt.getText().toString();
                                            dialog.dismiss();
                                        } else {
                                            Toast.makeText(PersonalSettingActivity.this, "新舊密碼及確認密碼都不可以為空哦~", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        })
                        .setShowBottom(true)
                        .show(getSupportFragmentManager());
                break;
        }
    }
}
