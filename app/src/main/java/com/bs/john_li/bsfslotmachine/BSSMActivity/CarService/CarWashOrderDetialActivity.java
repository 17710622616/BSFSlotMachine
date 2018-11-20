package com.bs.john_li.bsfslotmachine.BSSMActivity.CarService;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bs.john_li.bsfslotmachine.BSSMActivity.BaseActivity;
import com.bs.john_li.bsfslotmachine.BSSMActivity.Parking.PaymentAcvtivity;
import com.bs.john_li.bsfslotmachine.BSSMAdapter.CWCouponAdapter;
import com.bs.john_li.bsfslotmachine.BSSMAdapter.SecondCarOptionListAdapter;
import com.bs.john_li.bsfslotmachine.BSSMModel.CWUserOrderDetialOutModel;
import com.bs.john_li.bsfslotmachine.BSSMModel.CWUserOrderOutModel;
import com.bs.john_li.bsfslotmachine.BSSMModel.QrcodeReturnModel;
import com.bs.john_li.bsfslotmachine.BSSMUtils.BSSMCommonUtils;
import com.bs.john_li.bsfslotmachine.BSSMUtils.BSSMConfigtor;
import com.bs.john_li.bsfslotmachine.BSSMUtils.QRCodeUtil;
import com.bs.john_li.bsfslotmachine.BSSMUtils.SPUtils;
import com.bs.john_li.bsfslotmachine.BSSMView.BSSMHeadView;
import com.bs.john_li.bsfslotmachine.BSSMView.NoScrollListView;
import com.bs.john_li.bsfslotmachine.R;
import com.google.gson.Gson;
import com.othershe.nicedialog.BaseNiceDialog;
import com.othershe.nicedialog.NiceDialog;
import com.othershe.nicedialog.ViewConvertListener;
import com.othershe.nicedialog.ViewHolder;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.net.SocketTimeoutException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by John_Li on 16/11/2018.
 */

public class CarWashOrderDetialActivity extends BaseActivity implements View.OnClickListener{
    private BSSMHeadView headView;
    private ImageView setIv, telIv, navigationIv;
    private TextView setName, setPriceTv, businessHoursTv, soldTv, expirationTimeTv, applyForRefundTv, sellerNameTv, addressTv, orderNoTv, createTimeTv, totalPriceTv;
    private NoScrollListView mCouponLv;

    private CWCouponAdapter mCWCouponAdapter;
    private CWUserOrderDetialOutModel.DataBean.SellerBean mSellerBean;
    private CWUserOrderDetialOutModel.DataBean.SellerOrderBean mSellerOrderBean;
    private List<CWUserOrderDetialOutModel.DataBean.CouponListBean> mCouponList;
    private CWUserOrderOutModel.DataBeanX.CWUserOrderModel mCWUserOrderModel;
    private ImageOptions options = new ImageOptions.Builder().setSize(0, 0).setLoadingDrawableId(R.mipmap.img_loading_list).setFailureDrawableId(R.mipmap.load_img_fail).build();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_wash_order_detial);
        initView();
        setListener();
        initData();
    }

    @Override
    public void initView() {
        headView = findViewById(R.id.car_wash_order_detial_head);
        setIv = findViewById(R.id.car_wash_set_iv);
        telIv = findViewById(R.id.cwod_tel_iv);
        navigationIv = findViewById(R.id.cwod_navigation_iv);
        setName = findViewById(R.id.car_wash_set_name_tv);
        setPriceTv = findViewById(R.id.car_wash_set_price_tv1);
        businessHoursTv = findViewById(R.id.cwod_business_hours_tv);
        soldTv = findViewById(R.id.car_wash_set_sold_tv);
        expirationTimeTv = findViewById(R.id.cwod_expiration_time_tv);
        applyForRefundTv = findViewById(R.id.cwod_apply_for_refund_tv);
        sellerNameTv = findViewById(R.id.cwod_merchant_name_tv);
        addressTv = findViewById(R.id.cwod_merchant_address_tv);
        orderNoTv = findViewById(R.id.cwod_orderno_tv);
        createTimeTv = findViewById(R.id.cwod_order_createtime_tv);
        totalPriceTv = findViewById(R.id.cwod_order_total_price_tv);
        mCouponLv = findViewById(R.id.cwod_coupon_lv);
    }

    @Override
    public void setListener() {
        telIv.setOnClickListener(this);
        navigationIv.setOnClickListener(this);
        applyForRefundTv.setOnClickListener(this);
        mCouponLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                NiceDialog.init()
                        .setLayoutId(R.layout.dialog_qrcode)
                        .setConvertListener(new ViewConvertListener() {
                            @Override
                            public void convertView(final ViewHolder holder, final BaseNiceDialog dialog) {
                                final ImageView qrcodeIv = holder.getView(R.id.dialog_qrcode_iv);
                                QrcodeReturnModel model = new QrcodeReturnModel();
                                model.setOrderNo(mSellerOrderBean.getOrderNo());
                                model.setCouponCode(mCouponList.get(i).getCouponCode());
                                Bitmap mBitmap = QRCodeUtil.createQRCodeBitmap(new Gson().toJson(model), 250, 250);
                                qrcodeIv.setImageBitmap(mBitmap);
                                qrcodeIv.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        dialog.dismiss();
                                    }
                                });
                            }
                        })
                        .setWidth(250)
                        .show(getSupportFragmentManager());
            }
        });
    }

    @Override
    public void initData() {
        mCWUserOrderModel = new Gson().fromJson(getIntent().getStringExtra("CWOrderModel"), CWUserOrderOutModel.DataBeanX.CWUserOrderModel.class);
        headView.setLeft(this);
        headView.setTitle("洗車訂單");
        if (mCWUserOrderModel.getOrderStatus() == 1) {
            headView.setRightText("支付", this);
        }

        setName.setText(mCWUserOrderModel.getChargeRemark());
        setPriceTv.setText("MOP" + mCWUserOrderModel.getTotalAmount());

        mCouponList = new ArrayList<>();
        mCWCouponAdapter = new CWCouponAdapter(this, mCouponList);
        mCouponLv.setAdapter(mCWCouponAdapter);
        callNetGetOrderDetial();
    }

    private void callNetGetOrderDetial() {
        RequestParams params = new RequestParams(BSSMConfigtor.BASE_URL + BSSMConfigtor.GET_CW_ORDER_DETIAL + SPUtils.get(CarWashOrderDetialActivity.this.getApplicationContext(), "UserToken", ""));
        params.setAsJsonContent(true);
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("orderNo",mCWUserOrderModel.getOrderNo());
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
                CWUserOrderDetialOutModel model = new Gson().fromJson(result.toString(), CWUserOrderDetialOutModel.class);
                if (model.getCode() == 200) {
                    mSellerBean = model.getData().getSeller();
                    mSellerOrderBean = model.getData().getSellerOrder();
                    mCouponList.addAll(model.getData().getCouponList());
                    refreshUI();
                } else if (model.getCode() == 10000){
                    SPUtils.put(CarWashOrderDetialActivity.this, "UserToken", "");
                    Toast.makeText(CarWashOrderDetialActivity.this,  String.valueOf(model.getMsg()), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(CarWashOrderDetialActivity.this,  String.valueOf(model.getMsg()), Toast.LENGTH_SHORT).show();
                }
            }
            //请求异常后的回调方法
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if (ex instanceof SocketTimeoutException) {
                    Toast.makeText(CarWashOrderDetialActivity.this, "請求超時，請重試！", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(CarWashOrderDetialActivity.this, getString(R.string.no_net), Toast.LENGTH_SHORT).show();
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
        businessHoursTv.setText("營業時間：" + mSellerBean.getBusinessHours());
        soldTv.setText("已        售：" + mSellerBean.getOrderCount());
        expirationTimeTv.setText("有效時間：" + BSSMCommonUtils.stampToDate(String.valueOf(mSellerOrderBean.getExprieTime())));
        sellerNameTv.setText(mSellerBean.getSellerName());
        addressTv.setText(mSellerBean.getAddress());
        orderNoTv.setText("訂單編號：" + mSellerOrderBean.getOrderNo());
        createTimeTv.setText("創建時間：" + BSSMCommonUtils.stampToDate(String.valueOf(mSellerOrderBean.getCreateTime())));
        totalPriceTv.setText("支付金額：" + mSellerOrderBean.getPayAmount());
        if (mSellerOrderBean.getOrderStatus() == 3) {
            applyForRefundTv.setVisibility(View.VISIBLE);
        }
        mCWCouponAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_left:
                finish();
                break;
            case R.id.head_right_tv:
                Intent intent2 = new Intent(CarWashOrderDetialActivity.this, PaymentAcvtivity.class);
                intent2.putExtra("startWay", 4);   // carWashOrder
                intent2.putExtra("orderNo", mSellerOrderBean.getOrderNo());
                intent2.putExtra("amount", String.valueOf(mSellerOrderBean.getPayAmount()));
                intent2.putExtra("createTime", mSellerOrderBean.getCreateTime());
                intent2.putExtra("exchange", mSellerOrderBean.getExchange());
                intent2.putExtra("exchangeAmountPay", mSellerOrderBean.getExchangeAmountPay());
                startActivityForResult(intent2, 1);
                break;
            case R.id.cwod_tel_iv:
                if (mSellerBean != null){
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + mSellerBean.getPhone()));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    this.startActivity(intent);
                }
                break;
            case R.id.cwod_navigation_iv:
                break;
            case R.id.cwod_apply_for_refund_tv:
                if (mSellerOrderBean != null) {
                    Intent intent = new Intent(this, ApplyForRefundActivity.class);
                    intent.putExtra("mSellerOrderBean", new Gson().toJson(mSellerOrderBean));
                    startActivityForResult(intent, 1);
                } else {
                    Toast.makeText(this, "訂單信息獲取失敗，請重試！", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                finish();
            }
        }
    }
}
