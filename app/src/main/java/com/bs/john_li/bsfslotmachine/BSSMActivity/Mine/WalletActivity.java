package com.bs.john_li.bsfslotmachine.BSSMActivity.Mine;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;

import com.bs.john_li.bsfslotmachine.BSSMActivity.BaseActivity;
import com.bs.john_li.bsfslotmachine.BSSMView.BSSMHeadView;
import com.bs.john_li.bsfslotmachine.R;

/**
 * 我的錢包
 * Created by John_Li on 5/8/2017.
 */

public class WalletActivity extends BaseActivity implements View.OnClickListener{
    private BSSMHeadView walletHead;
    private LinearLayout transactionDetialLL;
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
        transactionDetialLL = findViewById(R.id.mine_transaction_detial);
    }

    @Override
    public void setListener() {
        transactionDetialLL.setOnClickListener(this);
    }

    @Override
    public void initData() {
        walletHead.setTitle("錢包");
        walletHead.setLeft(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.head_left:
                finish();
                break;
            case R.id.mine_transaction_detial:
                Intent intent = new Intent(this, TransactionDetailActivity.class);
                startActivity(intent);
                break;
        }
    }
}
