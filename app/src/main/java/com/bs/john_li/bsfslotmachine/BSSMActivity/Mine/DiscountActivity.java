package com.bs.john_li.bsfslotmachine.BSSMActivity.Mine;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ListView;

import com.bs.john_li.bsfslotmachine.BSSMActivity.BaseActivity;
import com.bs.john_li.bsfslotmachine.BSSMAdapter.DiscountAdapter;
import com.bs.john_li.bsfslotmachine.BSSMView.BSSMHeadView;
import com.bs.john_li.bsfslotmachine.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 優惠券
 * Created by John_Li on 5/8/2017.
 */

public class DiscountActivity extends BaseActivity implements View.OnClickListener{
    private BSSMHeadView discount_head;
    private ListView discountLv;
    private List<String> discountList;
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
        discountLv = findViewById(R.id.discount_lv);
    }

    @Override
    public void setListener() {

    }

    @Override
    public void initData() {
        discount_head.setTitle("優惠券");
        discount_head.setLeft(this);

        discountList = new ArrayList<>();
        for (int i = 0;i<3;i++) {
            discountList.add("优惠券"+i);
        }
        discountLv.setAdapter(new DiscountAdapter(this, discountList));
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
