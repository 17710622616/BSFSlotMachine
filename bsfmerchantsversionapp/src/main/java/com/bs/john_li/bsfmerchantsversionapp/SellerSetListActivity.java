package com.bs.john_li.bsfmerchantsversionapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bs.john_li.bsfmerchantsversionapp.Adapter.SmartSellerSetListRefreshAdapter;
import com.bs.john_li.bsfmerchantsversionapp.Model.CommonModel;
import com.bs.john_li.bsfmerchantsversionapp.Model.SellerSetOutModel;
import com.bs.john_li.bsfmerchantsversionapp.Utils.AliyunOSSUtils;
import com.bs.john_li.bsfmerchantsversionapp.Utils.BSFCommonUtils;
import com.bs.john_li.bsfmerchantsversionapp.Utils.BSFMerchantConfigtor;
import com.bs.john_li.bsfmerchantsversionapp.Utils.SPUtils;
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
 * Created by John_Li on 20/11/2018.
 */

public class SellerSetListActivity extends FragmentActivity implements View.OnClickListener{
    private View view;
    private ImageView backIv, addIv;
    private RefreshLayout mRefreshLayout;
    private RecyclerView mRecycleView;
    private List<SellerSetOutModel.DataBeanX.SellerSetModel> orderList;
    private SmartSellerSetListRefreshAdapter mSmartSellerSetListRefreshAdapter;
    private LinearLayout noOrderLL;
    // 每頁加載數量
    private int pageSize = 10;
    // 頁數
    private int pageNo = 1;
    // 車輛總數
    private long totolCarCount = 30;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_set_list);
        initView();
        setListener();
        initData();
    }

    private void initView() {
        backIv = findViewById(R.id.seller_set_list_back);
        addIv = findViewById(R.id.seller_set_list_add);
        mRefreshLayout = (RefreshLayout) findViewById(R.id.seller_set_list_srl);
        mRecycleView = (RecyclerView) findViewById(R.id.seller_set_list_lv);
        noOrderLL = (LinearLayout) findViewById(R.id.no_set_ll);
        // 设置header的高度
        mRefreshLayout.setEnableAutoLoadmore(false);//是否启用列表惯性滑动到底部时自动加载更多
        mRefreshLayout.setDisableContentWhenRefresh(true);//是否在刷新的时候禁止列表的操作
        mRefreshLayout.setDisableContentWhenLoading(true);//是否在加载的时候禁止列表的操作
        mRefreshLayout.setHeaderHeightPx((int)(BSFCommonUtils.getDeviceWitdh(this) / 4.05));//Header标准高度（显示下拉高度>=标准高度 触发刷新）
    }

    private void setListener() {
        backIv.setOnClickListener(this);
        addIv.setOnClickListener(this);
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                orderList.clear();
                pageNo = 1;
                if (BSFCommonUtils.isLoginNow(SellerSetListActivity.this)) {
                    callNetGetCarList();
                } else {
                    startActivityForResult(new Intent(SellerSetListActivity.this, LoginActivity.class), 1);
                }
            }
        });
        mRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                //和最大的数据比较
                if (pageSize * (pageNo) > totolCarCount){
                    Toast.makeText(SellerSetListActivity.this, "沒有更多數據了誒~", Toast.LENGTH_SHORT).show();
                    mRefreshLayout.finishRefresh();
                    mRefreshLayout.finishLoadmore();
                } else {
                    pageNo ++;
                    if (BSFCommonUtils.isLoginNow(SellerSetListActivity.this)) {
                        callNetGetCarList();
                    } else {
                        startActivityForResult(new Intent(SellerSetListActivity.this, LoginActivity.class), 1);
                    }
                }
            }
        });
    }

    private void initData() {
        orderList = new ArrayList<>();
        mSmartSellerSetListRefreshAdapter = new SmartSellerSetListRefreshAdapter(SellerSetListActivity.this, orderList, AliyunOSSUtils.initOSS(SellerSetListActivity.this,BSFMerchantConfigtor.END_POINT));
        mRecycleView.setLayoutManager(new LinearLayoutManager(SellerSetListActivity.this));
        mRecycleView.setAdapter(mSmartSellerSetListRefreshAdapter);

        mSmartSellerSetListRefreshAdapter.setOnItemClickListenr(new SmartSellerSetListRefreshAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, final int position) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SellerSetListActivity.this);
                builder.setTitle("提示");
                builder.setMessage("是否提交審核套餐");
                //点击对话框以外的区域是否让对话框消失
                builder.setCancelable(true);
                //设置正面按钮
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        callNetSubmitSet(position);
                        dialog.dismiss();
                    }
                });
                //设置中立按钮
                builder.setNeutralButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                //显示对话框
                dialog.show();
            }
        });
        mRefreshLayout.autoRefresh();
    }

    private void callNetSubmitSet(int position) {
        RequestParams params = new RequestParams(BSFMerchantConfigtor.BASE_URL + BSFMerchantConfigtor.SELLER_SUBMIT_SET);
        params.setAsJsonContent(true);
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("id", orderList.get(position).getId());
            jsonObj.put("sellerToken", SPUtils.get(SellerSetListActivity.this.getApplicationContext(), "SellerUserToken", ""));
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
                CommonModel model = new Gson().fromJson(result.toString(), CommonModel.class);
                if (model.getCode() == 200) {
                    Toast.makeText(SellerSetListActivity.this, "提交審核成功！", Toast.LENGTH_SHORT).show();
                    mRefreshLayout.autoRefresh();
                } else if (model.getCode() == 10000){
                    SPUtils.put(SellerSetListActivity.this, "SellerUserToken", "");
                    Toast.makeText(SellerSetListActivity.this,  String.valueOf(model.getMsg()), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SellerSetListActivity.this,  String.valueOf(model.getMsg()), Toast.LENGTH_SHORT).show();
                }
            }
            //请求异常后的回调方法
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if (ex instanceof SocketTimeoutException) {
                    Toast.makeText(SellerSetListActivity.this, "請求超時，請重試！", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SellerSetListActivity.this, getString(R.string.no_net), Toast.LENGTH_SHORT).show();
                }
            }
            //主动调用取消请求的回调方法
            @Override
            public void onCancelled(CancelledException cex) {
            }
            @Override
            public void onFinished() {
                if (orderList.size() > 0) {
                    noOrderLL.setVisibility(View.GONE);
                } else {
                    noOrderLL.setVisibility(View.VISIBLE);
                }
                mSmartSellerSetListRefreshAdapter.refreshListView(orderList);
                mRefreshLayout.finishRefresh();
                mRefreshLayout.finishLoadmore();
            }
        });
    }


    /**
     * 请求网络刷新数据
     */
    private void callNetGetCarList() {
        RequestParams params = new RequestParams(BSFMerchantConfigtor.BASE_URL + BSFMerchantConfigtor.SELLER_SET_LIST);
        params.setAsJsonContent(true);
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("pageSize",pageSize);
            jsonObj.put("pageNo",pageNo);
            jsonObj.put("sellerToken", SPUtils.get(SellerSetListActivity.this.getApplicationContext(), "SellerUserToken", ""));
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
                SellerSetOutModel model = new Gson().fromJson(result.toString(), SellerSetOutModel.class);
                if (model.getCode() == 200) {
                    totolCarCount = model.getData().getTotalCount();
                    orderList.addAll(model.getData().getData());
                } else if (model.getCode() == 10000){
                    SPUtils.put(SellerSetListActivity.this, "SellerUserToken", "");
                    Toast.makeText(SellerSetListActivity.this,  String.valueOf(model.getMsg()), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SellerSetListActivity.this,  String.valueOf(model.getMsg()), Toast.LENGTH_SHORT).show();
                }
            }
            //请求异常后的回调方法
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if (ex instanceof SocketTimeoutException) {
                    Toast.makeText(SellerSetListActivity.this, "請求超時，請重試！", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SellerSetListActivity.this, getString(R.string.no_net), Toast.LENGTH_SHORT).show();
                }
            }
            //主动调用取消请求的回调方法
            @Override
            public void onCancelled(CancelledException cex) {
            }
            @Override
            public void onFinished() {
                if (orderList.size() > 0) {
                    noOrderLL.setVisibility(View.GONE);
                } else {
                    noOrderLL.setVisibility(View.VISIBLE);
                }
                mSmartSellerSetListRefreshAdapter.refreshListView(orderList);
                mRefreshLayout.finishRefresh();
                mRefreshLayout.finishLoadmore();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.seller_set_list_back:
                finish();
                break;
            case R.id.seller_set_list_add:
                startActivityForResult(new Intent(this, SellerAddSetActivity.class), 1);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                mRefreshLayout.autoRefresh();
            }
        }
    }
}
