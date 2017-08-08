package com.bs.john_li.bsfslotmachine.BSSMActivity.Mine;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.bs.john_li.bsfslotmachine.BSSMActivity.BaseActivity;
import com.bs.john_li.bsfslotmachine.BSSMAdapter.HistoryOrderAdapter;
import com.bs.john_li.bsfslotmachine.BSSMView.BSSMHeadView;
import com.bs.john_li.bsfslotmachine.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 歷史訂單
 * Created by John_Li on 9/8/2017.
 */

public class HistoryOrderActivity extends BaseActivity implements View.OnClickListener{
    private BSSMHeadView historyHead;
    private ListView historyLv;
    private List<String> mHistoryList;
    private HistoryOrderAdapter mHistoryOrderAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_order);
        initView();
        setListener();
        initData();
    }

    @Override
    public void initView() {
        historyHead = findViewById(R.id.history_head);
        historyLv = findViewById(R.id.history_lv);
    }

    @Override
    public void setListener() {
        historyLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
    }

    @Override
    public void initData() {
        historyHead.setTitle("歷史訂單");
        historyHead.setLeft(this);
        mHistoryList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            mHistoryList.add("車牌號碼：0000" + i+1);
        }

        mHistoryOrderAdapter = new HistoryOrderAdapter(mHistoryList, this);
        historyLv.setAdapter(mHistoryOrderAdapter);
        historyLv.setDivider(null);
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
