package com.bs.john_li.bsfslotmachine.BSSMFragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bs.john_li.bsfslotmachine.BSSMActivity.Mine.WalletActivity;
import com.bs.john_li.bsfslotmachine.BSSMView.BSSMHeadView;
import com.bs.john_li.bsfslotmachine.R;

/**
 * 首页停车的碎片
 * Created by John_Li on 28/7/2017.
 */

public class ParkingFragment extends BaseFragment implements View.OnClickListener{
    public static String TAG = ParkingFragment.class.getName();
    private View parkingView;
    private BSSMHeadView headView;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        parkingView = inflater.inflate(R.layout.fragment_parking, null);
        initView();
        setListenter();
        initData();
        return parkingView;
    }

    @Override
    public void initView() {
        headView = (BSSMHeadView) parkingView.findViewById(R.id.parking_head);
    }

    @Override
    public void setListenter() {

    }

    @Override
    public void initData() {
        headView.setTitle("停車");
        headView.setLeft(R.mipmap.search,this);
        headView.setRight(R.mipmap.wallet,this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.head_left:
                Toast.makeText(getActivity(), "查询",Toast.LENGTH_SHORT).show();
                break;
            case R.id.head_right:
                startActivity(new Intent(getActivity(), WalletActivity.class));
                break;
        }
    }
}
