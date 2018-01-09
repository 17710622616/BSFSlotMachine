package com.bs.john_li.bsfslotmachine.BSSMFragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bs.john_li.bsfslotmachine.BSSMActivity.LoginActivity;
import com.bs.john_li.bsfslotmachine.BSSMActivity.Mine.CarListActivity;
import com.bs.john_li.bsfslotmachine.BSSMActivity.Mine.DiscountActivity;
import com.bs.john_li.bsfslotmachine.BSSMActivity.Mine.GuoJiangLongActivity;
import com.bs.john_li.bsfslotmachine.BSSMActivity.Mine.HistoryOrderActivity;
import com.bs.john_li.bsfslotmachine.BSSMActivity.Mine.OpinionActivity;
import com.bs.john_li.bsfslotmachine.BSSMActivity.Mine.PersonalSettingActivity;
import com.bs.john_li.bsfslotmachine.BSSMActivity.Mine.SettingActivity;
import com.bs.john_li.bsfslotmachine.BSSMActivity.Mine.WalletActivity;
import com.bs.john_li.bsfslotmachine.BSSMModel.CommonModel;
import com.bs.john_li.bsfslotmachine.BSSMModel.UserInfoOutsideModel;
import com.bs.john_li.bsfslotmachine.BSSMUtils.BSSMCommonUtils;
import com.bs.john_li.bsfslotmachine.BSSMUtils.BSSMConfigtor;
import com.bs.john_li.bsfslotmachine.BSSMUtils.SPUtils;
import com.bs.john_li.bsfslotmachine.BSSMView.BSSMHeadView;
import com.bs.john_li.bsfslotmachine.R;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * 首页停车的碎片
 * Created by John_Li on 28/7/2017.
 */

public class MineFragment extends BaseFragment implements View.OnClickListener {
    public static String TAG = MineFragment.class.getName();
    private View mineView;
    private BSSMHeadView mineHeadView;
    private LinearLayout personalLL,walletLL,discountLL,integralLL,historyLL, myCarLL,shareLL,opinionLL,serverLL,gjlLL;
    private TextView nickNameTv, phoneTv;
    private UserInfoOutsideModel.UserInfoModel mUserInfoModel;

    private String userToken;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
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
        historyLL = mineView.findViewById(R.id.mine_history_order);
        myCarLL = mineView.findViewById(R.id.mine_mycar_ll);
        shareLL = mineView.findViewById(R.id.mine_recommend);
        opinionLL = mineView.findViewById(R.id.mine_opinion);
        serverLL = mineView.findViewById(R.id.mine_server);
        gjlLL = mineView.findViewById(R.id.mine_guojianglong);
        nickNameTv = mineView.findViewById(R.id.mine_nickname);
        phoneTv = mineView.findViewById(R.id.mine_info_phone);
    }

    @Override
    public void setListenter() {
        personalLL.setOnClickListener(this);
        walletLL.setOnClickListener(this);
        integralLL.setOnClickListener(this);
        discountLL.setOnClickListener(this);
        historyLL.setOnClickListener(this);
        myCarLL.setOnClickListener(this);
        shareLL.setOnClickListener(this);
        opinionLL.setOnClickListener(this);
        serverLL.setOnClickListener(this);
        gjlLL.setOnClickListener(this);
    }

    @Override
    public void initData() {
        mineHeadView.setTitle("我的");
        mineHeadView.setRight(R.mipmap.setting, this);
        refreshUI();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {   // 隐藏
        } else {    // 显示
            refreshUI();
        }
    }

    @Subscribe
    public void onEvent(String msg){
        if (msg.equals("LOGIN")) {
            refreshUI();
        } else {
            refreshUI();
        }
    }

    /**
     * 刷新UI
     */
    private void refreshUI() {
        userToken = (String) SPUtils.get(getActivity(), "UserToken", "");
        String userInfoJson = (String) SPUtils.get(getActivity(), "UserInfo", "");
        if (!userToken.equals("")){
            if (!userInfoJson.equals("")){
                mUserInfoModel = new Gson().fromJson(userInfoJson, UserInfoOutsideModel.UserInfoModel.class);
                nickNameTv.setText(mUserInfoModel.getNickname());
                phoneTv.setText(BSSMCommonUtils.change3to6ByStar(mUserInfoModel.getMobile()));
            } else {
                mUserInfoModel = new UserInfoOutsideModel.UserInfoModel();
                nickNameTv.setText("立即登錄");
                phoneTv.setText("登錄后獲得更多權限");
                Toast.makeText(getActivity(), "用戶信息錯誤，請重新登錄！", Toast.LENGTH_SHORT).show();
            }
        } else {
            nickNameTv.setText("立即登錄");
            phoneTv.setText("登錄后獲得更多權限");
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.head_right:
                getActivity().startActivity(new Intent(getActivity(), SettingActivity.class));
                break;
            case R.id.personal_setting_ll:
                userToken = (String) SPUtils.get(getActivity(), "UserToken", "");
                if (userToken != null) {
                    if (!userToken.equals("")) {
                        getActivity().startActivity(new Intent(getActivity(), PersonalSettingActivity.class));
                    } else {
                        startActivityForResult(new Intent(getActivity(), LoginActivity.class), BSSMConfigtor.LOGIN_FOR_RQUEST);
                    }
                } else {
                    startActivityForResult(new Intent(getActivity(), LoginActivity.class), BSSMConfigtor.LOGIN_FOR_RQUEST);
                }
                break;
            case R.id.mine_wallet_ll:
                if (BSSMCommonUtils.isLoginNow(getActivity())) {
                    getActivity().startActivity(new Intent(getActivity(), WalletActivity.class));
                } else {
                    startActivityForResult(new Intent(getActivity(), LoginActivity.class), BSSMConfigtor.LOGIN_FOR_RQUEST);
                }
                break;
            case R.id.mine_discount_ll:
                if (BSSMCommonUtils.isLoginNow(getActivity())) {
                    getActivity().startActivity(new Intent(getActivity(), DiscountActivity.class));
                } else {
                    startActivityForResult(new Intent(getActivity(), LoginActivity.class), BSSMConfigtor.LOGIN_FOR_RQUEST);
                }
                break;
            case R.id.mine_integral_ll:
                Toast.makeText(getActivity(),getResources().getString(R.string.not_open),Toast.LENGTH_SHORT).show();
                break;
            case R.id.mine_mycar_ll:
                if (BSSMCommonUtils.isLoginNow(getActivity())) {
                    getActivity().startActivity(new Intent(getActivity(), CarListActivity.class));
                } else {
                    startActivityForResult(new Intent(getActivity(), LoginActivity.class), BSSMConfigtor.LOGIN_FOR_RQUEST);
                }
                break;
            case R.id.mine_history_order:
                getActivity().startActivity(new Intent(getActivity(), HistoryOrderActivity.class));
                /*if (BSSMCommonUtils.isLoginNow(getActivity())) {
                    getActivity().startActivity(new Intent(getActivity(), HistoryOrderActivity.class));
                } else {
                    startActivityForResult(new Intent(getActivity(), LoginActivity.class), BSSMConfigtor.LOGIN_FOR_RQUEST);
                }*/
                break;
            case R.id.mine_recommend:
                Toast.makeText(getActivity(),"分享",Toast.LENGTH_SHORT).show();
                break;
            case R.id.mine_opinion:
                getActivity().startActivity(new Intent(getActivity(), OpinionActivity.class));
                break;
            case R.id.mine_server:
                // 使用系统的电话拨号服务，必须去声明权限，在AndroidManifest.xml中进行声明
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:65999631"));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                this.startActivity(intent);
                break;
            case R.id.mine_guojianglong:
                getActivity().startActivity(new Intent(getActivity(), GuoJiangLongActivity.class));
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
