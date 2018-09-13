package com.bs.john_li.bsfslotmachine.BSSMActivity.Mine;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bs.john_li.bsfslotmachine.BSSMActivity.BaseActivity;
import com.bs.john_li.bsfslotmachine.BSSMActivity.LoginActivity;
import com.bs.john_li.bsfslotmachine.BSSMModel.CommonModel;
import com.bs.john_li.bsfslotmachine.BSSMUtils.BSSMCommonUtils;
import com.bs.john_li.bsfslotmachine.BSSMUtils.BSSMConfigtor;
import com.bs.john_li.bsfslotmachine.BSSMUtils.SPUtils;
import com.bs.john_li.bsfslotmachine.BSSMView.BSSMHeadView;
import com.bs.john_li.bsfslotmachine.R;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.net.SocketTimeoutException;

/**
 * 我的錢包
 * Created by John_Li on 5/8/2017.
 */

public class WalletActivity extends BaseActivity implements View.OnClickListener{
    private BSSMHeadView walletHead;
    private TextView rechargeTv, withdrawDdepositTV, FAQTV, balanceTv;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
        initView();
        setListener();
        initData();
    }

    @Override
    public void initView() {
        walletHead = findViewById(R.id.wallet_head);
        rechargeTv = findViewById(R.id.wallet_recharge);
        withdrawDdepositTV = findViewById(R.id.wallet_withdraw_deposit);
        FAQTV = findViewById(R.id.wallet_FAQ);
        balanceTv = findViewById(R.id.wallet_balance);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            walletHead.setHeadHight();
        }
    }

    @Override
    public void setListener() {
        rechargeTv.setOnClickListener(this);
        withdrawDdepositTV.setOnClickListener(this);
        FAQTV.setOnClickListener(this);
    }

    @Override
    public void initData() {
        walletHead.setTitle("錢包");
        walletHead.setLeft(this);
        walletHead.setRightText("明細", this);

        if (BSSMCommonUtils.isLoginNow(WalletActivity.this)) {
            callNetGetWalletBalance();
        } else {
            startActivityForResult(new Intent(WalletActivity.this, LoginActivity.class), BSSMConfigtor.LOGIN_FOR_RQUEST);
        }
    }

    /**
     * 獲取我的餘額
     */
    private void callNetGetWalletBalance() {
        RequestParams params = new RequestParams(BSSMConfigtor.BASE_URL + BSSMConfigtor.GET_WALLET_BALANCE + SPUtils.get(this, "UserToken", ""));
        params.setAsJsonContent(true);
        params.setConnectTimeout(30 * 1000);
        x.http().request(HttpMethod.POST, params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                CommonModel model = new Gson().fromJson(result.toString(), CommonModel.class);
                if (model.getCode().equals("200")) {
                    balanceTv.setText("MOP $ " + String.format("%.2f", Double.parseDouble(model.getData())).toString());
                } else if (model.getCode().equals("10000")) {
                    SPUtils.put(WalletActivity.this, "UserToken", "");
                    startActivityForResult(new Intent(WalletActivity.this, LoginActivity.class), BSSMConfigtor.LOGIN_FOR_RQUEST);
                } else {
                    Toast.makeText(WalletActivity.this, "獲取餘額失敗" + String.valueOf(model.getMsg()), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if (ex instanceof SocketTimeoutException) {
                    Toast.makeText(WalletActivity.this, "獲取餘額" + getString(R.string.timeout), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(WalletActivity.this, getString(R.string.no_net), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.head_left:
                finish();
                break;
            case R.id.head_right_tv:
                Intent intent = new Intent(this, TransactionDetailActivity.class);
                startActivity(intent);
                break;
            case R.id.wallet_recharge:
                Intent intent1 = new Intent(this, WalletRechargeActivity.class);
                startActivity(intent1);
                break;
            case R.id.wallet_withdraw_deposit:
                /*Intent intent2 = new Intent(this, WithDrawalActivity.class);
                startActivity(intent2);*/
                Toast.makeText(this, "請聯繫客服進行提現，謝謝！", Toast.LENGTH_LONG).show();
                break;
            case R.id.wallet_FAQ:
                startActivity(new Intent(this, WalletFQAActivity.class));
                break;
        }
    }
}
