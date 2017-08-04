package com.bs.john_li.bsfslotmachine.BSSMFragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bs.john_li.bsfslotmachine.BSSMActivity.Mine.DiscountActivity;
import com.bs.john_li.bsfslotmachine.BSSMActivity.Mine.PersonalSettingActivity;
import com.bs.john_li.bsfslotmachine.BSSMActivity.Mine.SettingActivity;
import com.bs.john_li.bsfslotmachine.BSSMActivity.Mine.WalletActivity;
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
    private LinearLayout personalLL,walletLL,discountLL,integralLL;
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
        personalLL = mineView.findViewById(R.id.personal_setting_ll);
        walletLL = mineView.findViewById(R.id.mine_wallet_ll);
        integralLL = mineView.findViewById(R.id.mine_integral_ll);
        discountLL = mineView.findViewById(R.id.mine_discount_ll);
    }

    @Override
    public void setListenter() {
        personalLL.setOnClickListener(this);
        walletLL.setOnClickListener(this);
        integralLL.setOnClickListener(this);
        discountLL.setOnClickListener(this);
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
                getActivity().startActivity(new Intent(getActivity(), SettingActivity.class));
                break;
            case R.id.personal_setting_ll:
                getActivity().startActivity(new Intent(getActivity(), PersonalSettingActivity.class));
            case R.id.mine_wallet_ll:
                getActivity().startActivity(new Intent(getActivity(), WalletActivity.class));
                break;
            case R.id.mine_discount_ll:
                getActivity().startActivity(new Intent(getActivity(), DiscountActivity.class));
                break;
            case R.id.mine_integral_ll:
                Toast.makeText(getActivity(),getResources().getString(R.string.not_open),Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
