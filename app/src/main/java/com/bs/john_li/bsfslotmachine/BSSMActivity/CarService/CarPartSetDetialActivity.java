package com.bs.john_li.bsfslotmachine.BSSMActivity.CarService;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.sdk.android.oss.OSSClient;
import com.bs.john_li.bsfslotmachine.BSSMActivity.LoginActivity;
import com.bs.john_li.bsfslotmachine.BSSMActivity.Parking.PaymentAcvtivity;
import com.bs.john_li.bsfslotmachine.BSSMAdapter.CollapsingAdapter;
import com.bs.john_li.bsfslotmachine.BSSMModel.CarPartSetOutModel;
import com.bs.john_li.bsfslotmachine.BSSMModel.CarPartsOrderOutModel;
import com.bs.john_li.bsfslotmachine.BSSMModel.OrderModel;
import com.bs.john_li.bsfslotmachine.BSSMUtils.AliyunOSSUtils;
import com.bs.john_li.bsfslotmachine.BSSMUtils.BSSMConfigtor;
import com.bs.john_li.bsfslotmachine.BSSMUtils.SPUtils;
import com.bs.john_li.bsfslotmachine.BSSMUtils.StatusBarUtil;
import com.bs.john_li.bsfslotmachine.BSSMView.LoadDialog;
import com.bs.john_li.bsfslotmachine.R;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by John_Li on 4/1/2019.
 */

public class CarPartSetDetialActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView nameTv, priceTv, marketPriceTv, desTv, submitTv;
    private AppBarLayout appbar;
    private Toolbar articalToolbar;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private ViewPager mViewPager;
    private CollapsingAdapter mCollapsingAdapter;
    private NestedScrollView mNestedScrollView;

    private String id;
    private List<ImageView> imgList;
    private CarPartSetOutModel.DataBean.SellerPartsBean mSellerPartsBean;
    //private ImageView mSubmitDialogIv;
    // 是否是第一次初始化點讚狀態
    private boolean isInitIsLikeStatus = false;
    private OSSClient oss;
    private ImageOptions options = new ImageOptions.Builder().setSize(0, 0).setImageScaleType(ImageView.ScaleType.CENTER_CROP).setLoadingDrawableId(R.mipmap.img_loading_list).setFailureDrawableId(R.mipmap.load_img_fail_list).build();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setColor(this, getResources().getColor(R.color.colorAlpha));
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        setContentView(R.layout.activity_car_part_detial);
        initView();
        setListener();
        initData();
    }

    public void initView() {
        appbar = (AppBarLayout) findViewById(R.id.car_part_set_appbar);
        articalToolbar = (Toolbar) findViewById(R.id.car_part_set_toolbar);
        mNestedScrollView = (NestedScrollView) findViewById(R.id.collapsing_car_part_set_sv);
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.car_part_set_collapsing_toolbar);
        mViewPager = (ViewPager) findViewById(R.id.car_part_set_vp);
        nameTv = (TextView) findViewById(R.id.car_part_set_name_tv);
        priceTv = (TextView) findViewById(R.id.car_part_set_price_tv);
        marketPriceTv = (TextView) findViewById(R.id.car_part_set_market_price_tv);
        desTv = (TextView) findViewById(R.id.car_part_set_des);
        marketPriceTv.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG);
        submitTv = (TextView) findViewById(R.id.car_part_set_submit);
    }

    public void setListener() {
        submitTv.setOnClickListener(this);
    }

    public void initData() {
        Intent intent = getIntent();
        id = intent.getStringExtra("carPartId");
        oss = AliyunOSSUtils.initOSS(this);

        // toolbar
        setSupportActionBar(articalToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // 頭部的圖片列表
        imgList = new ArrayList<>();

        // 獲取套餐詳情
        callNetGetSetDetial();
    }

    private void callNetGetSetDetial() {
        RequestParams params = new RequestParams(BSSMConfigtor.BASE_URL + BSSMConfigtor.GET_PARTS_SET_DETIAL);
        params.setAsJsonContent(true);
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("id", id);
            jsonObj.put("token", SPUtils.get(this, "UserToken", ""));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String urlJson = jsonObj.toString();
        params.setBodyContent(urlJson);
        String uri = params.getUri();
        params.setConnectTimeout(30 * 1000);
        x.http().request(HttpMethod.POST, params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                CarPartSetOutModel model = new Gson().fromJson(result.toString(), CarPartSetOutModel.class);
                if (model.getCode() == 200) {
                    mSellerPartsBean = model.getData().getSellerParts();
                    refreshUI();
                } else if (model.getCode() == 10000) {
                    SPUtils.put(CarPartSetDetialActivity.this, "UserToken", "");
                    Toast.makeText(CarPartSetDetialActivity.this, String.valueOf(model.getMsg()), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(CarPartSetDetialActivity.this, String.valueOf(model.getMsg()), Toast.LENGTH_SHORT).show();
                }
            }

            //请求异常后的回调方法
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if (ex instanceof SocketTimeoutException) {
                    Toast.makeText(CarPartSetDetialActivity.this, getString(R.string.timeout), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(CarPartSetDetialActivity.this, getString(R.string.no_net), Toast.LENGTH_SHORT).show();
                }
            }

            //主动调用取消请求的回调方法
            @Override
            public void onCancelled(CancelledException cex) {
            }

            @Override
            public void onFinished() {
            }
        });
    }


    private void refreshUI() {
        //  設置標題
        mCollapsingToolbarLayout.setTitle(mSellerPartsBean.getName());
        nameTv.setText(mSellerPartsBean.getName());
        priceTv.setText("MOP" + mSellerPartsBean.getCostPrice());
        marketPriceTv.setText("原價：MOP：" + mSellerPartsBean.getMarketPrice());
        desTv.setText(String.valueOf(mSellerPartsBean.getDescription()));

        // 如果有圖片則顯示，無則不設置
        if (mSellerPartsBean.getPics() != null) {
            if (!mSellerPartsBean.getPics().equals("")) {
                try {
                    //Map<String, String> coverMap = new Gson().fromJson(mContentsModel.getCover(), HashMap.class);
                    List<String> result = Arrays.asList(mSellerPartsBean.getPics().split(","));
                    for (String cover : result) {
                        ImageView iv = new ImageView(this);
                        iv.setBackgroundColor(getResources().getColor(R.color.colorMineGray));
                        iv.setImageResource(R.mipmap.img_loading_list);
                        //AliyunOSSUtils.downloadImg(entry.getValue(), oss, iv, this, R.mipmap.load_img_fail);
                        x.image().bind(iv, cover, options);
                        imgList.add(iv);
                    }

                    mCollapsingAdapter = new CollapsingAdapter(imgList);
                    mViewPager.setAdapter(mCollapsingAdapter);
                } catch (Exception e) {
                    ViewGroup.LayoutParams params = mViewPager.getLayoutParams();
                    params.height = 270;
                    mViewPager.setLayoutParams(params);
                    appbar.setExpanded(false);
                    articalToolbar.setCollapsible(false);
                }
            } else {
                ViewGroup.LayoutParams params = mViewPager.getLayoutParams();
                params.height = 270;
                mViewPager.setLayoutParams(params);
                appbar.setExpanded(false);
                articalToolbar.setCollapsible(false);
            }
        } else {
            ViewGroup.LayoutParams params = mViewPager.getLayoutParams();
            params.height = 270;
            mViewPager.setLayoutParams(params);
            appbar.setExpanded(false);
            articalToolbar.setCollapsible(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.car_part_set_submit:
                final LoadDialog loadDialog = new LoadDialog(this, false, "提交中......");
                loadDialog.show();
                RequestParams params = new RequestParams(BSSMConfigtor.BASE_URL + BSSMConfigtor.SUBMIT_PARTS_ORDER + SPUtils.get(this, "UserToken", ""));
                params.setAsJsonContent(true);
                JSONObject jsonObj = new JSONObject();
                try {
                    jsonObj.put("partId",id);
                    jsonObj.put("num", 1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                params.setBodyContent(jsonObj.toString());
                String uri = params.getUri();
                x.http().request(HttpMethod.POST ,params, new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        CarPartsOrderOutModel model = new Gson().fromJson(result.toString(), CarPartsOrderOutModel.class);
                        if (model.getCode() == 200) {
                            Intent intent = new Intent(CarPartSetDetialActivity.this, PaymentAcvtivity.class);
                            intent.putExtra("startWay", 5);   // parkingOrder
                            intent.putExtra("orderNo", model.getData().getOrderNo());
                            intent.putExtra("amount", model.getData().getAmount());
                            intent.putExtra("createTime", model.getData().getCreateTime());
                            intent.putExtra("exchange", model.getData().getExchange());
                            intent.putExtra("exchangeAmountPay", model.getData().getExchangeAmountPay());
                            startActivityForResult(intent, 3);
                        } else if (model.getCode() == 10000) {
                            SPUtils.put(CarPartSetDetialActivity.this, "UserToken", "");
                            startActivityForResult(new Intent(CarPartSetDetialActivity.this, LoginActivity.class), BSSMConfigtor.LOGIN_FOR_RQUEST);
                        } else {
                            Toast.makeText(CarPartSetDetialActivity.this, "訂單提交失敗," + String.valueOf(model.getMsg()), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {
                        Toast.makeText(CarPartSetDetialActivity.this, "訂單提交失敗╮(╯▽╰)╭請重新提交", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(CancelledException cex) {

                    }

                    @Override
                    public void onFinished() {
                        loadDialog.dismiss();
                    }
                });
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode){
                case 3:
                    finish();
                    break;
            }
        }
    }
}
