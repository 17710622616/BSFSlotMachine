package com.bs.john_li.bsfslotmachine.BSSMActivity.Mine;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bs.john_li.bsfslotmachine.BSSMActivity.BaseActivity;
import com.bs.john_li.bsfslotmachine.BSSMModel.CommonModel;
import com.bs.john_li.bsfslotmachine.BSSMModel.ServiceChargeModel;
import com.bs.john_li.bsfslotmachine.BSSMModel.WithDrawalModel;
import com.bs.john_li.bsfslotmachine.BSSMUtils.BSSMCommonUtils;
import com.bs.john_li.bsfslotmachine.BSSMUtils.BSSMConfigtor;
import com.bs.john_li.bsfslotmachine.BSSMUtils.SPUtils;
import com.bs.john_li.bsfslotmachine.BSSMView.BSSMHeadView;
import com.bs.john_li.bsfslotmachine.BSSMView.LoadDialog;
import com.bs.john_li.bsfslotmachine.R;
import com.google.gson.Gson;
import com.othershe.nicedialog.BaseNiceDialog;
import com.othershe.nicedialog.NiceDialog;
import com.othershe.nicedialog.ViewConvertListener;
import com.othershe.nicedialog.ViewHolder;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.net.SocketTimeoutException;

/**
 * Created by John_Li on 28/3/2018.
 */

public class WithDrawalActivity extends BaseActivity implements View.OnClickListener{
    private BSSMHeadView headView;
    private EditText withdrawNum;
    private CheckBox isagreeCb;
    private TextView withdrawAgreement, withdrawSubmit, handlingFeeTv;
    private TextView nameTv, telTv, bankCardNumberTv, bankAccountTv;

    private ServiceChargeModel.DataBean mServiceChargeDataModel;
    private WithDrawalModel mWithDrawalModel;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_withdraw);
        initView();
        setListener();
        initData();
    }

    @Override
    public void initView() {
        headView = findViewById(R.id.wallet_withdraw_head);
        isagreeCb = findViewById(R.id.wallet_withdraw_isagree);
        withdrawNum = findViewById(R.id.wallet_withdraw_num);
        withdrawAgreement = findViewById(R.id.wallet_withdraw_agreement);
        withdrawSubmit = findViewById(R.id.wallet_withdraw_submit);
        handlingFeeTv = findViewById(R.id.wallet_withdraw_handling_fee);
        nameTv = findViewById(R.id.wallet_withdraw_name_tv);
        telTv = findViewById(R.id.wallet_withdraw_tel_tv);
        bankCardNumberTv = findViewById(R.id.wallet_withdraw_bank_card_number_tv);
        bankAccountTv = findViewById(R.id.wallet_withdraw_bank_account_tv);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            headView.setHeadHight();
        }
    }

    @Override
    public void setListener() {
        withdrawAgreement.setOnClickListener(this);
        withdrawSubmit.setOnClickListener(this);
        nameTv.setOnClickListener(this);
        telTv.setOnClickListener(this);
        bankCardNumberTv.setOnClickListener(this);
        bankAccountTv.setOnClickListener(this);
    }

    @Override
    public void initData() {
        headView.setLeft(this);
        headView.setTitle("餘額提現");
        headView.setRightText("提現記錄", this);
        mWithDrawalModel = new WithDrawalModel();
        mWithDrawalModel.setType("2");

        callNetGetServiceCharge();
    }

    /**
     * 獲取手續費
     */
    private void callNetGetServiceCharge() {
        RequestParams params = new RequestParams(BSSMConfigtor.BASE_URL + BSSMConfigtor.GET_SERVICE_CHARGE + SPUtils.get(this, "UserToken", ""));
        params.setAsJsonContent(true);
        params.setConnectTimeout(30 * 1000);
        x.http().request(HttpMethod.POST ,params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                ServiceChargeModel model = new Gson().fromJson(result.toString(), ServiceChargeModel.class);
                if (model.getCode() == 200) {
                    mServiceChargeDataModel = model.getData();
                    handlingFeeTv.setText("提現金額(" + mServiceChargeDataModel.getFdesc() + ")");
                    Toast.makeText(WithDrawalActivity.this, "提現手續費獲取成功！", Toast.LENGTH_SHORT).show();
                } else if (model.getCode() == 10000){
                    SPUtils.put(WithDrawalActivity.this, "UserToken", "");
                    Toast.makeText(WithDrawalActivity.this,  String.valueOf(model.getMsg()), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(WithDrawalActivity.this, "提現手續費獲取失敗！" + String.valueOf(model.getMsg()), Toast.LENGTH_SHORT).show();
                }
            }
            //请求异常后的回调方法
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if (ex instanceof SocketTimeoutException) {
                    Toast.makeText(WithDrawalActivity.this, "提現手續費獲取超時，請重試！", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(WithDrawalActivity.this, "提現手續費獲取失敗！", Toast.LENGTH_SHORT).show();
                }
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_left:
                finish();
                break;
            case R.id.head_right_tv:
                startActivity(new Intent(this, WithDrawalDeitalActivity.class));
                break;
            case R.id.wallet_withdraw_agreement:
                startActivity(new Intent(this, PaymentAgreementActivity.class));
                break;
            case R.id.wallet_withdraw_name_tv:
                NiceDialog.init()
                        .setLayoutId(R.layout.dialog_car_edit)
                        .setConvertListener(new ViewConvertListener() {
                            @Override
                            public void convertView(ViewHolder holder, final BaseNiceDialog dialog) {
                                final EditText editText = holder.getView(R.id.car_edit);
                                editText.setHint("請填寫您的銀行卡綁定姓名");
                                BSSMCommonUtils.showKeyboard(editText);
                                holder.setOnClickListener(R.id.car_edit_submit, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        mWithDrawalModel.setBankName(editText.getText().toString());
                                        nameTv.setText("姓        名：" + mWithDrawalModel.getBankName());
                                        dialog.dismiss();
                                    }
                                });
                            }
                        })
                        .setShowBottom(false)
                        .setWidth(260)
                        .show(getSupportFragmentManager());
                break;
            case R.id.wallet_withdraw_tel_tv:
                NiceDialog.init()
                        .setLayoutId(R.layout.dialog_car_edit)
                        .setConvertListener(new ViewConvertListener() {
                            @Override
                            public void convertView(ViewHolder holder, final BaseNiceDialog dialog) {
                                final EditText editText = holder.getView(R.id.car_edit);
                                editText.setHint("請填寫您的賬戶電話");
                                BSSMCommonUtils.showKeyboard(editText);
                                holder.setOnClickListener(R.id.car_edit_submit, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        mWithDrawalModel.setPhoneNumber(editText.getText().toString());
                                        telTv.setText("電        話：" + mWithDrawalModel.getPhoneNumber());
                                        dialog.dismiss();
                                    }
                                });
                            }
                        })
                        .setShowBottom(false)
                        .setWidth(260)
                        .show(getSupportFragmentManager());
                break;
            case R.id.wallet_withdraw_bank_card_number_tv:
                NiceDialog.init()
                        .setLayoutId(R.layout.dialog_car_edit)
                        .setConvertListener(new ViewConvertListener() {
                            @Override
                            public void convertView(ViewHolder holder, final BaseNiceDialog dialog) {
                                final EditText editText = holder.getView(R.id.car_edit);
                                editText.setHint("請填寫銀行卡號");
                                BSSMCommonUtils.showKeyboard(editText);
                                holder.setOnClickListener(R.id.car_edit_submit, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        mWithDrawalModel.setBankCardNo(editText.getText().toString());
                                        bankCardNumberTv.setText("銀行卡號：" + mWithDrawalModel.getBankCardNo());
                                        dialog.dismiss();
                                    }
                                });
                            }
                        })
                        .setShowBottom(false)
                        .setWidth(260)
                        .show(getSupportFragmentManager());
                break;
            case R.id.wallet_withdraw_bank_account_tv:
                NiceDialog.init()
                        .setLayoutId(R.layout.dialog_car_edit)
                        .setConvertListener(new ViewConvertListener() {
                            @Override
                            public void convertView(ViewHolder holder, final BaseNiceDialog dialog) {
                                final EditText editText = holder.getView(R.id.car_edit);
                                editText.setHint("請填寫開戶銀行");
                                BSSMCommonUtils.showKeyboard(editText);
                                holder.setOnClickListener(R.id.car_edit_submit, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        mWithDrawalModel.setBankName(editText.getText().toString());
                                        bankAccountTv.setText("開戶銀行：" + mWithDrawalModel.getBankName());
                                        dialog.dismiss();
                                    }
                                });
                            }
                        })
                        .setShowBottom(false)
                        .setWidth(260)
                        .show(getSupportFragmentManager());
                break;
            case R.id.wallet_withdraw_submit:
                if (isagreeCb.isChecked() && withdrawNum.getText() != null) {
                    try {
                        mWithDrawalModel.setMoney(Double.parseDouble(String.valueOf(withdrawNum.getText())));
                        final LoadDialog loadDialog = new LoadDialog(this, false, "請求中......");
                        loadDialog.show();

                        RequestParams params = new RequestParams(BSSMConfigtor.BASE_URL + BSSMConfigtor.POST_WITH_DRAWAL + SPUtils.get(this, "UserToken", ""));
                        params.setAsJsonContent(true);
                        JSONObject jsonObj = new JSONObject();
                        try {
                            jsonObj.put("type",mWithDrawalModel.getType());
                            jsonObj.put("money",mWithDrawalModel.getMoney());
                            jsonObj.put("realName",mWithDrawalModel.getRealName());
                            jsonObj.put("phoneNumber",mWithDrawalModel.getPhoneNumber());
                            jsonObj.put("bankName",mWithDrawalModel.getBankName());
                            jsonObj.put("bankCardNo",mWithDrawalModel.getBankCardNo());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        String urlJson = jsonObj.toString();
                        params.setBodyContent(urlJson);
                        params.setConnectTimeout(30 * 1000);
                        x.http().request(HttpMethod.POST ,params, new Callback.CommonCallback<String>() {
                            @Override
                            public void onSuccess(String result) {
                                CommonModel model = new Gson().fromJson(result.toString(), CommonModel.class);
                                if (model.getCode().equals("200")) {
                                    finish();
                                    Toast.makeText(WithDrawalActivity.this, "提現申請提交成功！", Toast.LENGTH_SHORT).show();
                                    finish();
                                } else if (model.getCode().equals("10000")){
                                    SPUtils.put(WithDrawalActivity.this, "UserToken", "");
                                    Toast.makeText(WithDrawalActivity.this,  String.valueOf(model.getMsg()), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(WithDrawalActivity.this, "提現申請提交失敗！" + String.valueOf(model.getMsg()), Toast.LENGTH_SHORT).show();
                                }
                            }
                            //请求异常后的回调方法
                            @Override
                            public void onError(Throwable ex, boolean isOnCallback) {
                                if (ex instanceof SocketTimeoutException) {
                                    Toast.makeText(WithDrawalActivity.this, "提現申請提交超時，請重試！", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(WithDrawalActivity.this, "提現申請提交失敗！", Toast.LENGTH_SHORT).show();
                                }
                            }
                            //主动调用取消请求的回调方法
                            @Override
                            public void onCancelled(CancelledException cex) {
                            }
                            @Override
                            public void onFinished() {
                                loadDialog.dismiss();
                            }
                        });
                    } catch (Exception e) {
                        Toast.makeText(WithDrawalActivity.this, "提交失敗，請檢查提交信息是否正確！", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(WithDrawalActivity.this, "請選填寫金額及勾選支付服務協議！", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
