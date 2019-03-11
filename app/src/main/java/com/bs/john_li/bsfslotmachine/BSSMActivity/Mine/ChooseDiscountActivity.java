package com.bs.john_li.bsfslotmachine.BSSMActivity.Mine;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bs.john_li.bsfslotmachine.BSSMActivity.BaseActivity;
import com.bs.john_li.bsfslotmachine.BSSMActivity.LoginActivity;
import com.bs.john_li.bsfslotmachine.BSSMActivity.Parking.ChooseCarActivity;
import com.bs.john_li.bsfslotmachine.BSSMAdapter.SmartDiscountRefreshAdapter;
import com.bs.john_li.bsfslotmachine.BSSMModel.CarModel;
import com.bs.john_li.bsfslotmachine.BSSMModel.DiscountOutModel;
import com.bs.john_li.bsfslotmachine.BSSMUtils.BSSMCommonUtils;
import com.bs.john_li.bsfslotmachine.BSSMUtils.BSSMConfigtor;
import com.bs.john_li.bsfslotmachine.BSSMUtils.SPUtils;
import com.bs.john_li.bsfslotmachine.BSSMView.BSSMHeadView;
import com.bs.john_li.bsfslotmachine.R;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

/**
 * 優惠券
 * Created by John_Li on 5/8/2017.
 */

public class ChooseDiscountActivity extends BaseActivity implements View.OnClickListener{
    private BSSMHeadView discount_head;
    private RefreshLayout mRefreshLayout;
    private RecyclerView mRecycleView;
    private List<DiscountOutModel.DataBeanX.DiscountModel> discountList;
    private SmartDiscountRefreshAdapter mSmartDiscountRefreshAdapter;
    private LinearLayout noDiscountLL;
    private LinearLayout discountRecommend;
    private CarModel.CarCountAndListModel.CarInsideModel mCarInsideModel;
    private int orderType = 1;  // 1：咪錶訂單，2：會員續費
    private double orderMoney;
    // 每頁加載數量
    private int pageSize = 10;
    // 頁數
    private int pageNo = 1;
    // 車輛總數
    private long totolCarCount = 30;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discount);
        initView();
        setListener();
        initData();
    }

    @Override
    public void initView() {
        discount_head = findViewById(R.id.discount_head);
        mRefreshLayout = (RefreshLayout) findViewById(R.id.discount_srl);
        mRecycleView = (RecyclerView) findViewById(R.id.discount_lv);
        noDiscountLL = (LinearLayout) findViewById(R.id.no_discount_ll);
        discountRecommend = findViewById(R.id.discount_recommend);
        // 设置header的高度
        mRefreshLayout.setEnableAutoLoadmore(false);//是否启用列表惯性滑动到底部时自动加载更多
        mRefreshLayout.setDisableContentWhenRefresh(true);//是否在刷新的时候禁止列表的操作
        mRefreshLayout.setDisableContentWhenLoading(true);//是否在加载的时候禁止列表的操作
        mRefreshLayout.setHeaderHeightPx((int)(BSSMCommonUtils.getDeviceWitdh(this) / 4.05));//Header标准高度（显示下拉高度>=标准高度 触发刷新）
    }

    @Override
    public void setListener() {
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                discountList.clear();
                pageNo = 1;
                if (BSSMCommonUtils.isLoginNow(ChooseDiscountActivity.this)) {
                    callNetGetActiveDiscountList();
                } else {
                    startActivityForResult(new Intent(ChooseDiscountActivity.this, LoginActivity.class), BSSMConfigtor.LOGIN_FOR_RQUEST);
                }
            }
        });
        mRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                //和最大的数据比较
                if (pageSize * (pageNo) > totolCarCount){
                    Toast.makeText(ChooseDiscountActivity.this, "沒有更多數據了誒~", Toast.LENGTH_SHORT).show();
                    mRefreshLayout.finishRefresh();
                    mRefreshLayout.finishLoadmore();
                } else {
                    pageNo ++;
                    if (BSSMCommonUtils.isLoginNow(ChooseDiscountActivity.this)) {
                        callNetGetActiveDiscountList();
                    } else {
                        startActivityForResult(new Intent(ChooseDiscountActivity.this, LoginActivity.class), BSSMConfigtor.LOGIN_FOR_RQUEST);
                    }
                }
            }
        });

        discountRecommend.setOnClickListener(this);
    }

    @Override
    public void initData() {
        discount_head.setTitle("優惠券");
        discount_head.setLeft(this);
        discount_head.setRightText("過期紅包", this);
        mCarInsideModel = new Gson().fromJson(getIntent().getStringExtra("CarInsideModel"), CarModel.CarCountAndListModel.CarInsideModel.class);
        if (String.valueOf(getIntent().getStringExtra("orderType")).equals("MemberPrepaidOrder")) {
            orderType = 2;
        }
        orderMoney = Double.parseDouble(getIntent().getStringExtra("orderModey"));

        discountList = new ArrayList<>();
        mSmartDiscountRefreshAdapter = new SmartDiscountRefreshAdapter(this, discountList,0);
        mRecycleView.setLayoutManager(new LinearLayoutManager(this));
        mRecycleView.setAdapter(mSmartDiscountRefreshAdapter);

        mSmartDiscountRefreshAdapter.setOnItemClickListenr(new SmartDiscountRefreshAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (discountList.get(position).getType() == orderType) { //type优惠券类型1、咪表代金券；2、会员充值代金券
                    if (orderType == 1) {
                        if (discountList.get(position).getIfVip() == mCarInsideModel.getIfPay()) {  // 咪表代金券
                            if (orderMoney >= discountList.get(position).getMinAmount()) {
                                Intent intent = new Intent();
                                intent.putExtra("couponModel", new Gson().toJson(discountList.get(position)));
                                setResult(RESULT_OK, intent);
                                finish();
                            } else {
                                Toast.makeText(ChooseDiscountActivity.this, "您的訂單金額不足紅包最低面額！", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(ChooseDiscountActivity.this, "您選擇的車輛系非會員車輛，請選擇對應的非會員紅包！", Toast.LENGTH_LONG).show();
                        }
                    } else {    // 会员充值代金券
                        Intent intent = new Intent();
                        intent.putExtra("couponModel", new Gson().toJson(discountList.get(position)));
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                } else {
                    Toast.makeText(ChooseDiscountActivity.this, "請選擇正確的紅包類型，不可用原因查看紅包使用限制！", Toast.LENGTH_LONG).show();
                }
            }
        });
        mRefreshLayout.autoRefresh();
    }


    /**
     * 请求网络刷新数据
     */
    private void callNetGetActiveDiscountList() {
        RequestParams params = new RequestParams(BSSMConfigtor.BASE_URL + BSSMConfigtor.GET_DISCOUNT_LIST + SPUtils.get(this.getApplicationContext(), "UserToken", ""));
        params.setAsJsonContent(true);
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("pageSize",pageSize);
            jsonObj.put("pageNo",pageNo);
            jsonObj.put("type",0);
            jsonObj.put("status",0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String urlJson = jsonObj.toString();
        params.setBodyContent(urlJson);
        String uri = params.getUri();
        params.setConnectTimeout(30 * 1000);
        x.http().request(HttpMethod.POST ,params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                DiscountOutModel model = new Gson().fromJson(result.toString(), DiscountOutModel.class);
                if (model.getCode() ==200) {
                    totolCarCount = model.getData().getTotalCount();
                    List<DiscountOutModel.DataBeanX.DiscountModel> discountModelsFromNet = model.getData().getData();
                    discountList.addAll(discountModelsFromNet);
                } else if (model.getCode() == 10000){
                    SPUtils.put(ChooseDiscountActivity.this, "UserToken", "");
                    Toast.makeText(ChooseDiscountActivity.this,  String.valueOf(model.getMsg()), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ChooseDiscountActivity.this,  String.valueOf(model.getMsg()), Toast.LENGTH_SHORT).show();
                }
            }
            //请求异常后的回调方法
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if (ex instanceof SocketTimeoutException) {
                    Toast.makeText(ChooseDiscountActivity.this, "請求超時，請重試！", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ChooseDiscountActivity.this, getString(R.string.no_net), Toast.LENGTH_SHORT).show();
                }
            }
            //主动调用取消请求的回调方法
            @Override
            public void onCancelled(CancelledException cex) {
            }
            @Override
            public void onFinished() {
                if (discountList.size() > 0) {
                    noDiscountLL.setVisibility(View.GONE);
                } else {
                    noDiscountLL.setVisibility(View.VISIBLE);
                }
                mSmartDiscountRefreshAdapter.refreshListView(discountList);
                mRefreshLayout.finishRefresh();
                mRefreshLayout.finishLoadmore();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case 1:
                    mRefreshLayout.autoRefresh();
                    break;
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.head_left:
                finish();
                break;
            case R.id.head_right_tv:
                startActivity(new Intent(this, OverdueDiscountActivity.class));
                break;
            case R.id.discount_recommend:
                startActivity(new Intent(this, ShareActivity.class));
                break;
        }
    }
}
