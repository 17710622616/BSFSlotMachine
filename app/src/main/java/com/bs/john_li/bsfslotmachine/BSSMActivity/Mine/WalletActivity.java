package com.bs.john_li.bsfslotmachine.BSSMActivity.Mine;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.bs.john_li.bsfslotmachine.BSSMActivity.BaseActivity;
import com.bs.john_li.bsfslotmachine.BSSMView.BSSMHeadView;
import com.bs.john_li.bsfslotmachine.R;

/**
 * 我的錢包
 * Created by John_Li on 5/8/2017.
 */

public class WalletActivity extends BaseActivity implements View.OnClickListener{
    private BSSMHeadView walletHead;
    private TextView rechargeTv, withdrawDdepositTV, FAQTV;
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
                Intent intent2 = new Intent(this, WithDrawalActivity.class);
                startActivity(intent2);
                break;
            case R.id.wallet_FAQ:
                startActivity(new Intent(this, WalletFQAActivity.class));
                break;
        }
    }
}
