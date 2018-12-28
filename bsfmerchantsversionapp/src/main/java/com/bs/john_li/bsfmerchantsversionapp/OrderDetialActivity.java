package com.bs.john_li.bsfmerchantsversionapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bs.john_li.bsfmerchantsversionapp.Adapter.CWCouponAdapter;
import com.bs.john_li.bsfmerchantsversionapp.BSFView.NoScrollListView;
import com.bs.john_li.bsfmerchantsversionapp.Model.SellerOrderDetialOutModel;
import com.bs.john_li.bsfmerchantsversionapp.Model.SellerOrderOutModel;
import com.bs.john_li.bsfmerchantsversionapp.Utils.BSFCommonUtils;
import com.bs.john_li.bsfmerchantsversionapp.Utils.BSFMerchantConfigtor;
import com.bs.john_li.bsfmerchantsversionapp.Utils.SPUtils;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by John_Li on 20/11/2018.
 */

public class OrderDetialActivity extends FragmentActivity implements View.OnClickListener{
    private ImageView backIv, setIv;
    private TextView setName, setPriceTv, finishTimeTv, orderStatusTv, soldTv, expirationTimeTv, orderNoTv, createTimeTv, totalPriceTv;
    private NoScrollListView mCouponLv;

    private String orderNo;
    private CWCouponAdapter mCWCouponAdapter;
    private SellerOrderOutModel.DataBeanX.SellerOrderModel mSellerOrderModel;
    private SellerOrderDetialOutModel.DataBean.SellerBean mSellerBean;
    private SellerOrderDetialOutModel.DataBean.SellerOrderBean mSellerOrderBean;
    private List<SellerOrderDetialOutModel.DataBean.CouponListBean> mCouponList;
    private ImageOptions options = new ImageOptions.Builder().setSize(0, 0).setLoadingDrawableId(R.mipmap.img_loading_list).setFailureDrawableId(R.mipmap.load_img_fail_list).build();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_wash_order_detial);
        initView();
        setListener();
        initData();
    }

    public void initView() {
        backIv = findViewById(R.id.order_detial_back);
        setIv = findViewById(R.id.order_detial_iv);
        setName = findViewById(R.id.order_detial_name_tv);
        setPriceTv = findViewById(R.id.order_detial_price_tv1);
        soldTv = findViewById(R.id.order_detial_sold_tv);
        expirationTimeTv = findViewById(R.id.cwod_expiration_time_tv);
        orderNoTv = findViewById(R.id.order_detialno_tv);
        createTimeTv = findViewById(R.id.order_detial_createtime_tv);
        finishTimeTv = findViewById(R.id.order_detial_finishtime_tv);
        totalPriceTv = findViewById(R.id.order_detial_total_price_tv);
        orderStatusTv = findViewById(R.id.cwod_status_tv);
        mCouponLv = findViewById(R.id.cwod_coupon_lv);
    }

    public void setListener() {
        backIv.setOnClickListener(this);
    }

    public void initData() {
        mSellerOrderModel = new Gson().fromJson(getIntent().getStringExtra("orderModel"), SellerOrderOutModel.DataBeanX.SellerOrderModel.class);

        mCouponList = new ArrayList<>();
        mCWCouponAdapter = new CWCouponAdapter(this, mCouponList);
        mCouponLv.setAdapter(mCWCouponAdapter);

        callNetGetOrderDetial();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.order_detial_back:
                finish();
                break;
        }
    }

    private void callNetGetOrderDetial() {
        RequestParams params = new RequestParams(BSFMerchantConfigtor.BASE_URL + BSFMerchantConfigtor.SELLER_ORDER_DETIAL);
        params.setAsJsonContent(true);
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("orderNo", mSellerOrderModel.getOrderNo());
            jsonObj.put("sellerToken", SPUtils.get(this, "SellerUserToken", ""));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String urlJson = jsonObj.toString();
        params.setBodyContent(urlJson);
        params.setConnectTimeout(30 * 1000);
        x.http().request(HttpMethod.POST ,params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                SellerOrderDetialOutModel model = new Gson().fromJson(result.toString(), SellerOrderDetialOutModel.class);
                if (model.getCode() == 200) {
                    mSellerOrderBean = model.getData().getSellerOrder();
                    mSellerBean = model.getData().getSeller();
                    mCouponList.addAll(model.getData().getCouponList());
                    refreshUI();
                } else {
                    Toast.makeText(OrderDetialActivity.this, "獲取訂單詳情失敗！" + String.valueOf(model.getMsg()), Toast.LENGTH_SHORT).show();
                }
            }
            //请求异常后的回调方法
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if (ex instanceof java.net.SocketTimeoutException) {
                    Toast.makeText(OrderDetialActivity.this, "網絡連接超時，請重試", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(OrderDetialActivity.this, "獲取訂單詳情失敗，請重新提交", Toast.LENGTH_SHORT).show();
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
        x.image().bind(setIv, mSellerOrderBean.getSellerLogo(), options);
        setName.setText(mSellerOrderBean.getChargeRemark());
        setPriceTv.setText("價        格：" + mSellerOrderBean.getTotalAmount());
        soldTv.setText("已        售：" + mSellerBean.getOrderCount());
        expirationTimeTv.setText("有效時間：" + BSFCommonUtils.stampToDate(String.valueOf(mSellerOrderBean.getExprieTime())));
        switch (mSellerOrderBean.getOrderStatus()) {    // 1 待支付  2支付中  3已支付  4已完成 5退款中 6已退款 7已失效 9取消
            case 1:
                orderStatusTv.setText("待支付");
                break;
            case 2:
                orderStatusTv.setText("支付中");
                break;
            case 3:
                orderStatusTv.setText("已支付");
                break;
            case 4:
                orderStatusTv.setText("已完成");
                break;
            case 5:
                orderStatusTv.setText("退款中");
                break;
            case 6:
                orderStatusTv.setText("已退款");
                break;
            case 7:
                orderStatusTv.setText("已失效");
                break;
            case 9:
                orderStatusTv.setText("已取消");
                break;
        }
        finishTimeTv.setText("消費時間：" + BSFCommonUtils.stampToDate(String.valueOf(mSellerOrderBean.getFinishedTime())));
        orderNoTv.setText("訂單編號：" + mSellerOrderBean.getOrderNo());
        createTimeTv.setText("創建時間：" + BSFCommonUtils.stampToDate(String.valueOf(mSellerOrderBean.getCreateTime())));
        totalPriceTv.setText("支付金額：" + mSellerOrderBean.getPayAmount());
        mCWCouponAdapter.notifyDataSetChanged();

    }
}
