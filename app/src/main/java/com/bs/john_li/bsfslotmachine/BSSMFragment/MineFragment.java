package com.bs.john_li.bsfslotmachine.BSSMFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bs.john_li.bsfslotmachine.BSSMView.BSSMHeadView;
import com.bs.john_li.bsfslotmachine.R;

/**
 * 首页停车的碎片
 * Created by John_Li on 28/7/2017.
 */

public class MineFragment extends BaseFragment implements View.OnClickListener {
    public static String TAG = MineFragment.class.getName();
    private View mineView;
    private BSSMHeadView mineHeadView;
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
        mineHeadView = (BSSMHeadView) mineView.findViewById(R.id.mine_head);
    }

    @Override
    public void setListenter() {

    }

    @Override
    public void initData() {
        mineHeadView.setTitle("我的");
        mineHeadView.setRight(R.mipmap.setting, this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.head_right:
                Toast.makeText(getActivity(), "设置", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
