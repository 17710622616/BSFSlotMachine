package com.bs.john_li.bsfslotmachine.BSSMActivity.Parking;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.SearchView;

import com.bs.john_li.bsfslotmachine.BSSMActivity.BaseActivity;
import com.bs.john_li.bsfslotmachine.R;

/**
 * 搜索界面
 * Created by John_Li on 21/9/2017.
 */

public class SearchSlotMachineActivity extends BaseActivity {
    private SearchView mSearchView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_slot_machine);
    }

    @Override
    public void initView() {
        mSearchView = findViewById(R.id.search_slot_sv);
        mSearchView.onActionViewExpanded();
    }

    @Override
    public void setListener() {
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    @Override
    public void initData() {

    }
}
