package com.bs.john_li.bsfslotmachine.BSSMActivity.Parking;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bs.john_li.bsfslotmachine.BSSMActivity.BaseActivity;
import com.bs.john_li.bsfslotmachine.BSSMModel.JuheExchangeModel;
import com.bs.john_li.bsfslotmachine.BSSMUtils.BSSMCommonUtils;
import com.bs.john_li.bsfslotmachine.BSSMUtils.BSSMConfigtor;
import com.bs.john_li.bsfslotmachine.BSSMView.BSSMHeadView;
import com.bs.john_li.bsfslotmachine.BSSMView.ShowTiemTextView;
import com.bs.john_li.bsfslotmachine.R;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * 投幣訂單支付界面
 * Created by John_Li on 14/10/2017.
 */

public class PaymentAcvtivity extends BaseActivity implements View.OnClickListener, ShowTiemTextView.EndPayTimeCallback {
    private BSSMHeadView headView;
    private TextView orderNoTv, submitTv;
    private ShowTiemTextView mShowTiemTextView;
    private CheckBox myWalletCb, alipayCb, wecahtPayCb;

    // 匯率
    private JuheExchangeModel exchangeModel;
    private String orderNo;
    private String orderTime;
    // 支付金額
    private double payMoney;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        exchangeMop();
        initView();
        setListener();
        initData();
    }

    @Override
    public void initView() {
        headView = findViewById(R.id.payment_head);
        orderNoTv = findViewById(R.id.payment_orderNo);
        submitTv = findViewById(R.id.payment_submit);
        mShowTiemTextView = findViewById(R.id.payment_showtime_tv);
        myWalletCb = findViewById(R.id.payment_my_wallet_cb);
        alipayCb = findViewById(R.id.payment_alipay_cb);
        wecahtPayCb = findViewById(R.id.payment_wecaht_pay_cb);
    }

    @Override
    public void setListener() {
        myWalletCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    alipayCb.setChecked(false);
                    wecahtPayCb.setChecked(false);
                    submitTv.setText("MOP" + "100" + "  確認支付");
                }
            }
        });
        alipayCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    myWalletCb.setChecked(false);
                    wecahtPayCb.setChecked(false);
                    if(exchangeModel != null) {
                        submitTv.setText("RMB" + (100 * Double.parseDouble(exchangeModel.getResult().get(0).getExchange())) + "元  確認支付");
                    } else {
                        exchangeMop();
                        Toast.makeText(PaymentAcvtivity.this, "匯率獲取失敗，請重試！", Toast.LENGTH_SHORT).show();
                        myWalletCb.setChecked(true);
                        alipayCb.setChecked(false);
                        wecahtPayCb.setChecked(false);
                    }
                }
            }
        });
        wecahtPayCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    myWalletCb.setChecked(false);
                    alipayCb.setChecked(false);
                    if(exchangeModel != null) {
                        submitTv.setText("RMB" + (100 * Double.parseDouble(exchangeModel.getResult().get(0).getExchange())) + "元  確認支付");
                    } else {
                        exchangeMop();
                        Toast.makeText(PaymentAcvtivity.this, "匯率獲取失敗，請重試！", Toast.LENGTH_SHORT).show();
                        myWalletCb.setChecked(true);
                        alipayCb.setChecked(false);
                        wecahtPayCb.setChecked(false);
                    }
                }
            }
        });
        submitTv.setOnClickListener(this);
        mShowTiemTextView.setmEndPayTimeCallback(this);
    }

    @Override
    public void initData() {
        headView.setTitle("訂單支付");
        headView.setLeft(this);

        Intent intent = getIntent();
        orderNo = intent.getStringExtra("orderNo");
        orderTime = String.valueOf(intent.getLongExtra("createTime", 0));
        orderNoTv.setText("訂單號：" + orderNo);
        SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long between = 600;
        try {
            java.util.Date begin=dfs.parse(BSSMCommonUtils.stampToDate(orderTime));
            java.util.Date end = dfs.parse(BSSMCommonUtils.getTimeNoW());
            between = (end.getTime()-begin.getTime())/1000;
        } catch (Exception e) {
            e.printStackTrace();
        }
        mShowTiemTextView.setTime((int)between);
        mShowTiemTextView.beginRun();
        myWalletCb.setChecked(true);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            // 监控返回键
            exitPayment();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.head_left:
                exitPayment();
                break;
            case R.id.payment_submit:
                if (myWalletCb.isChecked() || alipayCb.isChecked() || wecahtPayCb.isChecked()) {

                } else {
                    Toast.makeText(this, "請選擇支付方式", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    /**
     * 退出支付
     */
    private void exitPayment() {
        new AlertDialog.Builder(PaymentAcvtivity.this).setTitle("提醒")
                .setIconAttribute(android.R.attr.alertDialogIcon)
                .setMessage("確認要退出支付咩?")
                .setPositiveButton("確認", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mShowTiemTextView.stopRun();
                        PaymentAcvtivity.this.finish();
                    }})
                .setNegativeButton("取消", null)
                .create().show();
    }

    /**
     * 請求聚合數據，查詢匯率
     */
    private void exchangeMop() {
        RequestParams params = new RequestParams("http://op.juhe.cn/onebox/exchange/currency?key=" + BSSMConfigtor.JUHE_APPKEY + "&from=MOP&to=CNY");

        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                exchangeModel = new Gson().fromJson(result, JuheExchangeModel.class);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.d("失败", ex.toString());
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
     * 倒計時結束，訂單支付時間結束
     */
    @Override
    public void endPayTime() {
        finish();
    }
}
