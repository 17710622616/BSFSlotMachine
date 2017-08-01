package com.bs.john_li.bsfslotmachine.BSSMFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bs.john_li.bsfslotmachine.R;

/**
 * 首页停车的碎片
 * Created by John_Li on 28/7/2017.
 */

public class MineFragment extends BaseFragment {
    public static String TAG = MineFragment.class.getName();
    private View mineView;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mineView = inflater.inflate(R.layout.fragment_mine, null);
        initView();
        setListenter();
        initData();
        return mineView;
    }

    @Override
    public void initView() {

    }

    @Override
    public void setListenter() {

    }

    @Override
    public void initData() {

    }
}
