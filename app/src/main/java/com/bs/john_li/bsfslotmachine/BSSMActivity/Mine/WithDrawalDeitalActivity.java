package com.bs.john_li.bsfslotmachine.BSSMActivity.Mine;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.bs.john_li.bsfslotmachine.BSSMActivity.BaseActivity;
import com.bs.john_li.bsfslotmachine.BSSMActivity.LoginActivity;
import com.bs.john_li.bsfslotmachine.BSSMAdapter.SmartTransactionDetialListRefreshAdapter;
import com.bs.john_li.bsfslotmachine.BSSMAdapter.SmartWithDrawalDetialListRefreshAdapter;
import com.bs.john_li.bsfslotmachine.BSSMModel.SlotMachineListOutsideModel;
import com.bs.john_li.bsfslotmachine.BSSMModel.WalletRecordOutModel;
import com.bs.john_li.bsfslotmachine.BSSMModel.WithDrawalDetialModel;
import com.bs.john_li.bsfslotmachine.BSSMModel.WithDrawalModel;
import com.bs.john_li.bsfslotmachine.BSSMUtils.BSSMCommonUtils;
import com.bs.john_li.bsfslotmachine.BSSMUtils.BSSMConfigtor;
import com.bs.john_li.bsfslotmachine.BSSMUtils.SPUtils;
import com.bs.john_li.bsfslotmachine.BSSMView.BSSMHeadView;
import com.bs.john_li.bsfslotmachine.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.reflect.Type;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by John on 30/9/2018.
 */

public class WithDrawalDeitalActivity extends BaseActivity  implements View.OnClickListener {
    private BSSMHeadView headView;
    private RefreshLayout mRefreshLayout;
    private RecyclerView mRecycleView;

    // 總數
    private long totalCount;
    // 提現明細集合
    private List<WithDrawalDetialModel.WithDrawalDetialInsideModel.DataBean> withDrawalInsideModelList;
    private SmartWithDrawalDetialListRefreshAdapter mTransactionDetialAdapter;
    private int pageNo = 1;
    private int pageSize = 10;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_transaction_detail);
        initView();
        setListener();
        initData();
    }

    @Override
    public void initView() {
        headView = findViewById(R.id.transaction_detial_head);
        mRefreshLayout = findViewById(R.id.transaction_detial_srl);
        mRecycleView = findViewById(R.id.transaction_detial_lv);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            headView.setHeadHight();
        }
        mRefreshLayout.setEnableAutoLoadmore(false);//是否启用列表惯性滑动到底部时自动加载更多
        mRefreshLayout.setDisableContentWhenRefresh(true);//是否在刷新的时候禁止列表的操作
        mRefreshLayout.setDisableContentWhenLoading(true);//是否在加载的时候禁止列表的操作
        // 设置header的高度
        mRefreshLayout.setHeaderHeightPx((int)(BSSMCommonUtils.getDeviceWitdh(this) / 4.05));//Header标准高度（显示下拉高度>=标准高度 触发刷新）
    }

    @Override
    public void setListener() {
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                withDrawalInsideModelList.clear();
                pageNo = 1;
                if (BSSMCommonUtils.isLoginNow(WithDrawalDeitalActivity.this)) {
                    callNetChangeData();
                } else {
                    startActivityForResult(new Intent(WithDrawalDeitalActivity.this, LoginActivity.class), BSSMConfigtor.LOGIN_FOR_RQUEST);
                }
            }
        });
        mRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                //和最大的数据比较
                if (pageSize * (pageNo + 1) > totalCount){
                    Toast.makeText(WithDrawalDeitalActivity.this, "沒有更多數據了誒~", Toast.LENGTH_SHORT).show();
                    mRefreshLayout.finishRefresh();
                    mRefreshLayout.finishLoadmore();
                } else {
                    pageNo ++;
                    if (BSSMCommonUtils.isLoginNow(WithDrawalDeitalActivity.this)) {
                        callNetChangeData();
                    } else {
                        startActivityForResult(new Intent(WithDrawalDeitalActivity.this, LoginActivity.class), BSSMConfigtor.LOGIN_FOR_RQUEST);
                    }
                }
            }
        });

    }

    @Override
    public void initData() {
        Intent intent = getIntent();

        headView.setLeft(this);
        headView.setTitle("提現記錄");
        Type type = new TypeToken<List<SlotMachineListOutsideModel.SlotMachineListModel.SlotMachineModel>>() { }.getType();
        withDrawalInsideModelList = new ArrayList<>();
        mTransactionDetialAdapter = new SmartWithDrawalDetialListRefreshAdapter(this, withDrawalInsideModelList);
        mTransactionDetialAdapter.setOnItemClickListenr(new SmartWithDrawalDetialListRefreshAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                // 点击事件
            }
        });
        mRecycleView.setLayoutManager(new LinearLayoutManager(this));
        mRecycleView.setAdapter(mTransactionDetialAdapter);
        mRefreshLayout.autoRefresh();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.head_left:
                finish();
                break;
        }
    }
    private void callNetChangeData() {
        RequestParams params = new RequestParams(BSSMConfigtor.BASE_URL + BSSMConfigtor.GET_WITH_DRAWAL_RECORDE + SPUtils.get(this, "UserToken", ""));
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("pageSize",pageSize);
            jsonObj.put("pageNo",pageNo);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String urlJson = jsonObj.toString();
        params.setBodyContent(urlJson);
        params.setConnectTimeout(30 * 1000);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                WithDrawalDetialModel model = new Gson().fromJson(result, WithDrawalDetialModel.class);
                if (model.getCode() == 200) {
                    List<WithDrawalDetialModel.WithDrawalDetialInsideModel.DataBean> list = model.getData().getData();
                    totalCount = model.getData().getTotalCount();
                    withDrawalInsideModelList.addAll(list);
                }  else if (model.getCode() == 10000) {
                    SPUtils.put(WithDrawalDeitalActivity.this, "UserToken", "");
                    startActivityForResult(new Intent(WithDrawalDeitalActivity.this, LoginActivity.class), BSSMConfigtor.LOGIN_FOR_RQUEST);
                }else {
                    Toast.makeText(WithDrawalDeitalActivity.this, "提現記錄獲取失敗╮(╯▽╰)╭" + String.valueOf(model.getMsg()), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if (ex instanceof SocketTimeoutException) {
                    Toast.makeText(WithDrawalDeitalActivity.this, "提現記錄獲取超時，請重試", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(WithDrawalDeitalActivity.this, "提現記錄獲取失敗╮(╯▽╰)╭", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                refreshContentsList();
            }
        });
    }
    
    private void refreshContentsList() {
        mTransactionDetialAdapter.notifyDataSetChanged();
        mRefreshLayout.finishLoadmore();
        mRefreshLayout.finishRefresh();
    }
}
