package com.bs.john_li.bsfslotmachine.BSSMActivity.Parking;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebView;
import android.widget.Toast;

import com.bs.john_li.bsfslotmachine.BSSMActivity.BaseActivity;
import com.bs.john_li.bsfslotmachine.BSSMView.BSSMHeadView;
import com.bs.john_li.bsfslotmachine.R;

/**
 * Created by John_Li on 8/10/2018.
 */

public class CreditCardActivity extends BaseActivity implements View.OnClickListener {
    private BSSMHeadView credit_card_head;
    private WebView credit_card_wb;
    private String paymentUrl;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_card);
        initView();
        setListener();
        initData();
    }

    @Override
    public void initView() {
        credit_card_head = findViewById(R.id.credit_card_head);
        credit_card_wb = findViewById(R.id.credit_card_wb);
    }

    @Override
    public void setListener() {
    }

    @Override
    public void initData() {
        credit_card_head.setTitle("信用卡支付");
        credit_card_head.setLeft(this);
        Intent intent = getIntent();
        paymentUrl = intent.getStringExtra("crediUrl");
        if (paymentUrl != null) {
            credit_card_wb.loadUrl(paymentUrl);
        } else {
            Toast.makeText(this, "信用卡支付頁面打開失敗，請重新打開！", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_left:
                finish();
                break;
        }
    }
}
