package com.bs.john_li.bsfslotmachine.BSSMFragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.bs.john_li.bsfslotmachine.BSSMActivity.Mine.ServiceActivity;
import com.bs.john_li.bsfslotmachine.BSSMActivity.Mine.SettingActivity;
import com.bs.john_li.bsfslotmachine.BSSMActivity.Mine.ShareActivity;
import com.bs.john_li.bsfslotmachine.BSSMActivity.Mine.WalletActivity;
import com.bs.john_li.bsfslotmachine.BSSMModel.CommonModel;
import com.bs.john_li.bsfslotmachine.BSSMModel.UserInfoOutsideModel;
import com.bs.john_li.bsfslotmachine.BSSMUtils.AliyunOSSUtils;
import com.bs.john_li.bsfslotmachine.BSSMUtils.BSSMCommonUtils;
import com.bs.john_li.bsfslotmachine.BSSMUtils.BSSMConfigtor;
import com.bs.john_li.bsfslotmachine.BSSMUtils.SPUtils;
import com.bs.john_li.bsfslotmachine.BSSMView.BSSMHeadView;
import com.bs.john_li.bsfslotmachine.R;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.io.File;
import java.net.SocketTimeoutException;

import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * 首页我的的碎片
 * Created by John_Li on 28/7/2017.
 */

public class MineFragment extends BaseFragment implements View.OnClickListener {
    public static String TAG = MineFragment.class.getName();
    private View mineView;
    private BSSMHeadView mineHeadView;
    private RefreshLayout mRefreshLayout;
    private LinearLayout personalLL,walletLL,discountLL,integralLL,historyLL, myCarLL,shareLL,opinionLL,serverLL,gjlLL;
    private TextView balanceTv, nickNameTv, phoneTv;
    private ImageView headIv;

    private UserInfoOutsideModel.UserInfoModel mUserInfoModel;
    private ImageOptions options = new ImageOptions.Builder().setSize(0, 0).setLoadingDrawableId(R.mipmap.head_boy).setFailureDrawableId(R.mipmap.head_boy).build();
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
        mRefreshLayout = (RefreshLayout) mineView.findViewById(R.id.mine_srl);
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
        balanceTv = mineView.findViewById(R.id.mine_wallet_balance);
        nickNameTv = mineView.findViewById(R.id.mine_nickname);
        phoneTv = mineView.findViewById(R.id.mine_info_phone);
        headIv = mineView.findViewById(R.id.personal_setting_head_iv);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            mineHeadView.setHeadHight();
        }
        mRefreshLayout.setEnableAutoLoadmore(false);//是否启用列表惯性滑动到底部时自动加载更多
        mRefreshLayout.setDisableContentWhenRefresh(true);//是否在刷新的时候禁止列表的操作
        mRefreshLayout.setDisableContentWhenLoading(true);//是否在加载的时候禁止列表的操作
        // 设置header的高度
        mRefreshLayout.setHeaderHeightPx((int)(BSSMCommonUtils.getDeviceWitdh(getActivity()) / 4.05));//Header标准高度（显示下拉高度>=标准高度 触发刷新）
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

        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshUI();
            }
        });
    }

    @Override
    public void initData() {
        mineHeadView.setTitle("我的");
        mineHeadView.setRight(R.mipmap.setting, this);
        String userToken = (String) SPUtils.get(getActivity(), "UserToken", "");
        if (userToken != null) {
            mRefreshLayout.autoRefresh();
        }
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
            //mRefreshLayout.autoRefresh();
        }
    }

    @Subscribe
    public void onEvent(String msg){
        if (msg.equals("LOGIN")) {
            mRefreshLayout.autoRefresh();
        } else {
            mRefreshLayout.autoRefresh();
        }
    }

    /**
     * 刷新UI
     */
    private void refreshUI() {
        String userToken = (String) SPUtils.get(getActivity(), "UserToken", "");
        String userInfoJson = (String) SPUtils.get(getActivity(), "UserInfo", "");
        if (!userToken.equals("")){
            if (!userInfoJson.equals("")){
                callNetGetWalletBalance();
                mUserInfoModel = new Gson().fromJson(userInfoJson, UserInfoOutsideModel.UserInfoModel.class);
                nickNameTv.setText(mUserInfoModel.getNickname());
                phoneTv.setText(BSSMCommonUtils.change3to6ByStar(mUserInfoModel.getMobile()));
                //AliyunOSSUtils.downloadImg(mUserInfoModel.getHeadimg(), AliyunOSSUtils.initOSS(getActivity()), headIv, getActivity(), R.mipmap.head_boy);
                x.image().bind(headIv, mUserInfoModel.getHeadimg(), options);
            } else {
                mUserInfoModel = new UserInfoOutsideModel.UserInfoModel();
                nickNameTv.setText("立即登錄");
                phoneTv.setText("登錄后獲得更多權限");
                balanceTv.setText("0.0蚊");
                //AliyunOSSUtils.downloadImg("", AliyunOSSUtils.initOSS(getActivity()), headIv, getActivity(), R.mipmap.head_boy);
                x.image().bind(headIv, "", options);
                Toast.makeText(getActivity(), "用戶信息錯誤，請重新登錄！", Toast.LENGTH_SHORT).show();
            }
        } else {
            //AliyunOSSUtils.downloadImg("", AliyunOSSUtils.initOSS(getActivity()), headIv, getActivity(), R.mipmap.head_boy);
            x.image().bind(headIv, "", options);
            nickNameTv.setText("立即登錄");
            phoneTv.setText("登錄后獲得更多權限");
            balanceTv.setText("0.0蚊");
        }
        
        mRefreshLayout.finishRefresh(1000);
    }

    /**
     * 獲取我的餘額
     */
    private void callNetGetWalletBalance() {
        RequestParams params = new RequestParams(BSSMConfigtor.BASE_URL + BSSMConfigtor.GET_WALLET_BALANCE + SPUtils.get(getActivity(), "UserToken", ""));
        params.setAsJsonContent(true);
        params.setConnectTimeout(30 * 1000);
        x.http().request(HttpMethod.POST, params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                CommonModel model = new Gson().fromJson(result.toString(), CommonModel.class);
                if (model.getCode().equals("200")) {
                    balanceTv.setText(String.format("%.2f", Double.parseDouble(model.getData())).toString() + "蚊");
                } else if (model.getCode().equals("10000")) {
                    SPUtils.put(getActivity(), "UserToken", "");
                    SPUtils.put(getActivity(), "UserInfo", "");
                    refreshUI();
                }else {
                    Toast.makeText(getActivity(), "獲取餘額失敗" + String.valueOf(model.getMsg()), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if (ex instanceof SocketTimeoutException) {
                    Toast.makeText(getActivity(), "獲取餘額" + getString(R.string.timeout), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), getString(R.string.no_net), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.head_right:
                getActivity().startActivity(new Intent(getActivity(), SettingActivity.class));
                break;
            case R.id.personal_setting_ll:
                String userToken = (String) SPUtils.get(getActivity(), "UserToken", "");
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
                /*if (BSSMCommonUtils.isLoginNow(getActivity())) {
                    getActivity().startActivity(new Intent(getActivity(), DiscountActivity.class));
                } else {
                    startActivityForResult(new Intent(getActivity(), LoginActivity.class), BSSMConfigtor.LOGIN_FOR_RQUEST);
                }*/
                Toast.makeText(getActivity(), getResources().getString(R.string.not_open), Toast.LENGTH_LONG).show();
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
                if (BSSMCommonUtils.isLoginNow(getActivity())) {
                    getActivity().startActivity(new Intent(getActivity(), HistoryOrderActivity.class));
                } else {
                    startActivityForResult(new Intent(getActivity(), LoginActivity.class), BSSMConfigtor.LOGIN_FOR_RQUEST);
                }
                break;
            case R.id.mine_recommend:
                startActivity(new Intent(getActivity(), ShareActivity.class));

                String fileName = "IMG_5002.PNG";
                String dirPath = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "BSSMPictures").getPath();
                File filesDir = getActivity().getExternalFilesDir(null);
                //存到本地的绝对路径
                final String filePath = dirPath + "/" + fileName;
                File file = new File(filePath);
                //如果不存在
                if (!file.exists()) {
                    File dirFile = new File(dirPath);
                    if (dirFile.exists()) {
                        //创建
                        dirFile.mkdirs();
                    }
                    RequestParams entity = new RequestParams("https://test-pic-666.oss-cn-hongkong.aliyuncs.com/" + fileName);
                    entity.setSaveFilePath(filePath);
                    x.http().get(entity, new Callback.CommonCallback<File>() {
                        @Override
                        public void onSuccess(File result) {
                            //BSSMCommonUtils.openShare(getActivity(), "掌泊寶", "http://www.bsmaco.icoc.bz/", "掌泊寶官網", filePath);
                        }

                        @Override
                        public void onError(Throwable ex, boolean isOnCallback) {
                        }

                        @Override
                        public void onCancelled(CancelledException cex) {
                        }

                        @Override
                        public void onFinished() {
                            BSSMCommonUtils.openShare(getActivity(), "掌泊寶", "http://www.bsmaco.icoc.bz/", "掌泊寶官網", filePath);
                        }
                    });
                } else {
                    BSSMCommonUtils.openShare(getActivity(), "掌泊寶", "http://www.bsmaco.icoc.bz/", "掌泊寶官網", filePath);
                }
                break;
            case R.id.mine_opinion:
                getActivity().startActivity(new Intent(getActivity(), OpinionActivity.class));
                break;
            case R.id.mine_server:
                // 使用系统的电话拨号服务，必须去声明权限，在AndroidManifest.xml中进行声明
                /*Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:65999631"));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                this.startActivity(intent);*/
                startActivity(new Intent(getActivity(), ServiceActivity.class));
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
