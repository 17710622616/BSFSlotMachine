package com.bs.john_li.bsfslotmachine.BSSMActivity.Mine;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bs.john_li.bsfslotmachine.BSSMActivity.BaseActivity;
import com.bs.john_li.bsfslotmachine.BSSMActivity.Parking.ParkingOrderActivity;
import com.bs.john_li.bsfslotmachine.BSSMActivity.Parking.PaymentAcvtivity;
import com.bs.john_li.bsfslotmachine.BSSMModel.MaxAmountModel;
import com.bs.john_li.bsfslotmachine.BSSMModel.OrderModel;
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

/**
 * Created by John_Li on 28/3/2018.
 */

public class WalletRechargeActivity extends BaseActivity implements View.OnClickListener{
    private BSSMHeadView headView;
    private EditText rechargeNum;
    private CheckBox isagreeCb;
    private TextView rechargeAgreement, rechargeSubmit;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_recharge);
        initView();
        setListener();
        initData();
    }

    @Override
    public void initView() {
        headView = findViewById(R.id.wallet_recharge_head);
        isagreeCb = findViewById(R.id.wallet_recharge_isagree);
        rechargeNum = findViewById(R.id.wallet_recharge_num);
        rechargeAgreement = findViewById(R.id.wallet_recharge_agreement);
        rechargeSubmit = findViewById(R.id.wallet_recharge_submit);
    }

    @Override
    public void setListener() {
        rechargeAgreement.setOnClickListener(this);
        rechargeSubmit.setOnClickListener(this);
    }

    @Override
    public void initData() {
        headView.setLeft(this);
        headView.setTitle("餘額充值");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_left:
                finish();
                break;
            case R.id.wallet_recharge_agreement:
                finish();
                break;
            case R.id.wallet_recharge_submit:
                try {
                    if (isagreeCb.isChecked() && rechargeNum.getText() != null) {
                        if (Double.parseDouble(String.valueOf(rechargeNum.getText())) < 5000) {
                            final LoadDialog loadDialog = new LoadDialog(this, false, "請求中......");
                            loadDialog.show();

                            RequestParams params = new RequestParams(BSSMConfigtor.BASE_URL + BSSMConfigtor.POST_CHARGE_ORDER + SPUtils.get(this, "UserToken", ""));
                            params.setAsJsonContent(true);
                            JSONObject jsonObj = new JSONObject();
                            try {
                                jsonObj.put("totalAmount", String.valueOf(rechargeNum.getText()));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            params.setBodyContent(jsonObj.toString());
                            String uri = params.getUri();
                            x.http().request(HttpMethod.POST ,params, new Callback.CommonCallback<String>() {
                                @Override
                                public void onSuccess(String result) {
                                    OrderModel model = new Gson().fromJson(result.toString(), OrderModel.class);
                                    if (model.getCode() == 200) {
                                        Intent intent = new Intent(WalletRechargeActivity.this, PaymentAcvtivity.class);
                                        intent.putExtra("startWay", 3);   // parkingOrder
                                        intent.putExtra("orderNo", model.getData().getOrderNo());
                                        intent.putExtra("createTime", model.getData().getCreateTime());
                                        startActivityForResult(intent, 3);
                                        loadDialog.dismiss();
                                        finish();
                                    } else {
                                        loadDialog.dismiss();
                                        Toast.makeText(WalletRechargeActivity.this, "充值訂單生成失敗：" + model.getMsg().toString(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onError(Throwable ex, boolean isOnCallback) {
                                    loadDialog.dismiss();
                                    Toast.makeText(WalletRechargeActivity.this, "充值訂單生成失敗", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onCancelled(CancelledException cex) {
                                }

                                @Override
                                public void onFinished() {
                                }
                            });
                        } else {
                            Toast.makeText(WalletRechargeActivity.this, "充值金額不可大于5000！", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(WalletRechargeActivity.this, "請選填寫金額及勾選支付服務協議！", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(WalletRechargeActivity.this, "請輸入正確的充值數額！", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
