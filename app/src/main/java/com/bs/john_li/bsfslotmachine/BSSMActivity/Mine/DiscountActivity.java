package com.bs.john_li.bsfslotmachine.BSSMActivity.Mine;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.bs.john_li.bsfslotmachine.BSSMActivity.BaseActivity;
import com.bs.john_li.bsfslotmachine.BSSMView.BSSMHeadView;
import com.bs.john_li.bsfslotmachine.R;

/**
 * 優惠券
 * Created by John_Li on 5/8/2017.
 */

public class DiscountActivity extends BaseActivity implements View.OnClickListener{
    private BSSMHeadView discount_head;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discount);
        initView();
        setListener();
        initData();
    }

    @Override
    public void initView() {
        discount_head = findViewById(R.id.discount_head);
    }

    @Override
    public void setListener() {

    }

    @Override
    public void initData() {
        discount_head.setTitle("優惠券");
        discount_head.setLeft(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.head_left:
                finish();
                break;
        }
    }
}
