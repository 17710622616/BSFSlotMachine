package com.bs.john_li.bsfslotmachine.BSSMFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bs.john_li.bsfslotmachine.BSSMView.BSSMHeadView;
import com.bs.john_li.bsfslotmachine.R;

/**
 * Created by John_Li on 9/11/2018.
 */

public class CarServiceFragment extends BaseFragment {
    public static String TAG = CarServiceFragment.class.getName();
    private View carServiceView;
    private BSSMHeadView headView;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        carServiceView = inflater.inflate(R.layout.fragment_car_service, null);
        initView();
        setListenter();
        initData();
        return carServiceView;
    }

    @Override
    public void initView() {
        headView = carServiceView.findViewById(R.id.car_service_head);
    }

    @Override
    public void setListenter() {

    }

    @Override
    public void initData() {
        headView.setTitle("汽車服務");
    }
}
