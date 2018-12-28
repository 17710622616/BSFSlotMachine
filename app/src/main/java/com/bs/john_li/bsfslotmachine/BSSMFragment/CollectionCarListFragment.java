package com.bs.john_li.bsfslotmachine.BSSMFragment;

import android.os.Bundle;

import com.bs.john_li.bsfslotmachine.R;

/**
 * Created by John_Li on 28/12/2018.
 */

public class CollectionCarListFragment extends LazyLoadFragment {
    public static String TAG = CollectionCarListFragment.class.getName();
    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        setContentView(R.layout.fragment_collection);
        initView();
        setListener();
        initData();
    }

    private void initView() {
        
    }

    private void setListener() {

    }

    private void initData() {

    }
}
