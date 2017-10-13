package com.bs.john_li.bsfslotmachine.BSSMActivity.Parking;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bs.john_li.bsfslotmachine.BSSMActivity.BaseActivity;
import com.bs.john_li.bsfslotmachine.BSSMView.BSSMHeadView;
import com.bs.john_li.bsfslotmachine.R;

/**
 * 投幣訂單支付界面
 * Created by John_Li on 14/10/2017.
 */

public class PaymentAcvtivity extends BaseActivity implements View.OnClickListener{
    private BSSMHeadView headView;
    private TextView orderNoTv;

    private String orderNo;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        initView();
        setListener();
        initData();
    }

    @Override
    public void initView() {
        headView = findViewById(R.id.payment_head);
        orderNoTv = findViewById(R.id.payment_orderNo);
    }

    @Override
    public void setListener() {

    }

    @Override
    public void initData() {
        headView.setTitle("訂單支付");
        headView.setLeft(this);

        Intent intent = getIntent();
        orderNo = intent.getStringExtra("orderNo");
        orderNoTv.setText("訂單號：" + orderNo);
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
                        PaymentAcvtivity.this.finish();
                    }})
                .setNegativeButton("取消", null)
                .create().show();
    }
}
