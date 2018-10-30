package com.bs.john_li.bsfslotmachine.BSSMActivity.Mine;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.bs.john_li.bsfslotmachine.BSSMActivity.BaseActivity;
import com.bs.john_li.bsfslotmachine.BSSMActivity.LoginActivity;
import com.bs.john_li.bsfslotmachine.BSSMActivity.Parking.PaymentAcvtivity;
import com.bs.john_li.bsfslotmachine.BSSMAdapter.OrderPhotoAdapter;
import com.bs.john_li.bsfslotmachine.BSSMModel.CommonModel;
import com.bs.john_li.bsfslotmachine.BSSMModel.UserOrderOutModel;
import com.bs.john_li.bsfslotmachine.BSSMUtils.BSSMCommonUtils;
import com.bs.john_li.bsfslotmachine.BSSMUtils.BSSMConfigtor;
import com.bs.john_li.bsfslotmachine.BSSMUtils.SPUtils;
import com.bs.john_li.bsfslotmachine.BSSMView.BSSMHeadView;
import com.bs.john_li.bsfslotmachine.BSSMView.NoScrollGridView;
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
import org.xutils.x;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

/**
 * 訂單詳情
 * Created by John_Li on 10/1/2018.
 */

public class OrderDetialActivity extends BaseActivity implements View.OnClickListener{
    private BSSMHeadView headView;
    private NoScrollGridView orderImgGv;
    private TextView orderTypeTv, orderNoTv,statusTV,payMoneyTV,startTimeTV,timeTV,machineNoTV,colorTV,addressTV,carTypeTV,carNoTV,carStyleTV,carBrandTV,remarkTV,totalAmountTV,returnAmountTV,monthTV, cancellationOfOrderTv;

    private UserOrderOutModel.UserOrderInsideModel.UserOrderModel mUserOrderModel;
    private List<String> imgList;
    private OrderPhotoAdapter mOrderPhotoAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actvity_order_detial);
        mUserOrderModel = new Gson().fromJson(getIntent().getStringExtra("OrderModel"), UserOrderOutModel.UserOrderInsideModel.UserOrderModel.class);
        initView();
        setListener();
        initData();
    }

    @Override
    public void initView() {
        headView = findViewById(R.id.order_detial_head);
        orderImgGv = findViewById(R.id.order_detial_gv);
        orderNoTv = findViewById(R.id.order_detial_no);
        orderTypeTv = findViewById(R.id.order_detial_type);
        statusTV = findViewById(R.id.order_detial_status);
        payMoneyTV = findViewById(R.id.order_detial_pay_money);
        startTimeTV = findViewById(R.id.order_detial_starttime);
        timeTV = findViewById(R.id.order_detial_time);
        machineNoTV = findViewById(R.id.order_detial_machine_no);
        colorTV = findViewById(R.id.order_detial_color);
        addressTV = findViewById(R.id.order_detial_address);
        carTypeTV = findViewById(R.id.order_detial_car_type);
        carNoTV = findViewById(R.id.order_detial_car_no);
        carStyleTV = findViewById(R.id.order_detial_car_style);
        carBrandTV = findViewById(R.id.order_detial_car_brand);
        remarkTV = findViewById(R.id.order_detial_remark);
        totalAmountTV = findViewById(R.id.order_detial_total_amount);
        returnAmountTV = findViewById(R.id.order_detial_return_amount);
        monthTV = findViewById(R.id.order_detial_month);
        cancellationOfOrderTv = findViewById(R.id.cancellation_of_order);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            headView.setHeadHight();
        }
    }

    @Override
    public void setListener() {
        cancellationOfOrderTv.setOnClickListener(this);
    }

    @Override
    public void initData() {
        headView.setTitle("訂單詳情");
        headView.setLeft(this);
        if (mUserOrderModel.getOrderStatus() == 1) {
            headView.setRightText("支付", this);
        }

        imgList = new ArrayList<>();
        switch (mUserOrderModel.getOrderType()) {
            case 1: // 充值訂單
                //imgList.add(R.mipmap.top_up);
                orderTypeTv.setText("訂單類型：充值訂單");
                totalAmountTV.setVisibility(View.VISIBLE);
                totalAmountTV.setText("總  金  額：MOP" + mUserOrderModel.getTotalAmount());
                break;
            case 2: // 會員續費訂單
                //imgList.add(R.mipmap.renewal);
                orderTypeTv.setText("訂單類型：會員續費訂單");
                totalAmountTV.setVisibility(View.VISIBLE);
                totalAmountTV.setText("總  金  額：MOP" + mUserOrderModel.getTotalAmount());
                returnAmountTV.setVisibility(View.VISIBLE);
                returnAmountTV.setText("返現金額：MOP" + mUserOrderModel.getDiscountAmount());
                monthTV.setVisibility(View.VISIBLE);
                monthTV.setText("充值月份：" + mUserOrderModel.getMonthNum());
                break;
            case 3: // 確定投幣機訂單
                imgList.add(mUserOrderModel.getImg1());
                orderTypeTv.setText("訂單類型：確定投幣機訂單");

                machineNoTV.setVisibility(View.VISIBLE);
                machineNoTV.setText("咪錶編號：" + mUserOrderModel.getMachineNo());
                carNoTV.setVisibility(View.GONE);
                //carNoTV.setText("車牌號碼：" + mUserOrderModel.getCarNo());
                carStyleTV.setVisibility(View.GONE);
                carStyleTV.setText("車輛型號：" + mUserOrderModel.getCarId());
                carBrandTV.setVisibility(View.GONE);
                carBrandTV.setText("車輛品牌：" + mUserOrderModel.getCarId());
                startTimeTV.setVisibility(View.VISIBLE);
                startTimeTV.setText("開始投幣時間：" + BSSMCommonUtils.stampToDate(String.valueOf(mUserOrderModel.getStartSlotTime())));
                addressTV.setVisibility(View.VISIBLE);
                if (mUserOrderModel.getParkingSpace() != null) {
                    if (!mUserOrderModel.getParkingSpace().equals("")) {
                        addressTV.setText("地        址：" + mUserOrderModel.getMachineNo() + mUserOrderModel.getParkingSpace() + "號");
                    } else {
                        addressTV.setText("地        址：" + mUserOrderModel.getMachineNo());
                    }
                } else {
                    addressTV.setText("地        址：" + mUserOrderModel.getMachineNo());
                }
                colorTV.setVisibility(View.VISIBLE);
                if (mUserOrderModel.getPillarColor() != null) {
                    switch (mUserOrderModel.getPillarColor()) {
                        case "yellow":
                            colorTV.setText("咪錶柱色：黃色");
                            break;
                        case "gray":
                            colorTV.setText("咪錶柱色：灰色");
                            break;
                        case "blue":
                            colorTV.setText("咪錶柱色：藍色");
                            break;
                        case "green":
                            colorTV.setText("咪錶柱色：綠色");
                            break;
                    }
                }
                carTypeTV.setVisibility(View.VISIBLE);
                switch (mUserOrderModel.getCarType()) {
                    case 1:
                        carTypeTV.setText("車輛類型：電單車");
                        break;
                    case 2:
                        carTypeTV.setText("車輛類型：私家車");
                        break;
                    case 3:
                        carTypeTV.setText("車輛類型：重型貨車");
                        break;
                }
                if (mUserOrderModel.getOrderStatus() == 1 || mUserOrderModel.getOrderStatus() == 2 || mUserOrderModel.getOrderStatus() == 3 || mUserOrderModel.getOrderStatus() == 4 || mUserOrderModel.getOrderStatus() == 5) {
                    cancellationOfOrderTv.setVisibility(View.VISIBLE);
                }
                break;
            case 4: // 未知投幣機訂單
                if (mUserOrderModel.getImg1() != null) {
                    imgList.add(mUserOrderModel.getImg1());
                }
                if (mUserOrderModel.getImg2() != null) {
                    imgList.add(mUserOrderModel.getImg2());
                }
                if (mUserOrderModel.getImg3() != null) {
                    imgList.add(mUserOrderModel.getImg3());
                }
                if (mUserOrderModel.getImg4() != null) {
                    imgList.add(mUserOrderModel.getImg4());
                }
                if (mUserOrderModel.getImg5() != null) {
                    imgList.add(mUserOrderModel.getImg5());
                }
                orderTypeTv.setText("訂單類型：未知投幣機訂單");

                machineNoTV.setVisibility(View.VISIBLE);
                machineNoTV.setText("咪錶編號：" + mUserOrderModel.getMachineNo());
                carNoTV.setVisibility(View.GONE);
                //carNoTV.setText("車牌號碼：" + mUserOrderModel.getCarNo());
                carStyleTV.setVisibility(View.GONE);
                carStyleTV.setText("車輛型號：" + mUserOrderModel.getCarId());
                carBrandTV.setVisibility(View.GONE);
                carBrandTV.setText("車輛品牌：" + mUserOrderModel.getCarId());
                startTimeTV.setVisibility(View.VISIBLE);
                startTimeTV.setText("開始投幣時間：" + BSSMCommonUtils.stampToDate(String.valueOf(mUserOrderModel.getStartSlotTime())));
                addressTV.setVisibility(View.VISIBLE);
                if (mUserOrderModel.getParkingSpace() != null) {
                    if (!mUserOrderModel.getParkingSpace().equals("")) {
                        addressTV.setText("地        址：" + mUserOrderModel.getMachineNo() + mUserOrderModel.getParkingSpace() + "號");
                    } else {
                        addressTV.setText("地        址：" + mUserOrderModel.getMachineNo());
                    }
                } else {
                    addressTV.setText("地        址：" + mUserOrderModel.getMachineNo());
                }
                colorTV.setVisibility(View.VISIBLE);
                switch (mUserOrderModel.getPillarColor()) {
                    case "yellow":
                        colorTV.setText("咪錶柱色：黃色");
                        break;
                    case "gray":
                        colorTV.setText("咪錶柱色：灰色");
                        break;
                    case "blue":
                        colorTV.setText("咪錶柱色：藍色");
                        break;
                    case "green":
                        colorTV.setText("咪錶柱色：綠色");
                        break;
                }

                carTypeTV.setVisibility(View.VISIBLE);
                switch (mUserOrderModel.getCarType()) {
                case 1:
                    carTypeTV.setText("車輛類型：電單車");
                    break;
                case 2:
                    carTypeTV.setText("車輛類型：私家車");
                    break;
                case 3:
                    carTypeTV.setText("車輛類型：重型貨車");
                    break;
                }
                if (mUserOrderModel.getOrderStatus() == 1 || mUserOrderModel.getOrderStatus() == 2 || mUserOrderModel.getOrderStatus() == 3 || mUserOrderModel.getOrderStatus() == 4 || mUserOrderModel.getOrderStatus() == 5) {
                    cancellationOfOrderTv.setVisibility(View.VISIBLE);
                }
                break;
        }

        mOrderPhotoAdapter = new OrderPhotoAdapter(this, imgList);
        orderImgGv.setAdapter(mOrderPhotoAdapter);
        orderNoTv.setText("訂單編號：" + String.valueOf(mUserOrderModel.getOrderNo()));
        payMoneyTV.setText("付款金額：MOP" + String.valueOf(mUserOrderModel.getPayAmount()));
        timeTV.setText("下單時間：" + BSSMCommonUtils.stampToDate(String.valueOf(mUserOrderModel.getCreateTime())));
        if(String.valueOf(mUserOrderModel.getRemark()).equals("")) {
            remarkTV.setText("備        註：暫無");
        } else {
            remarkTV.setText("備        註：" + String.valueOf(mUserOrderModel.getRemark()));
        }
        switch (mUserOrderModel.getOrderStatus()) {
            case 1: // 待支付
                statusTV.setText("訂單狀態：待支付");
                break;
            case 2: // 支付中
                statusTV.setText("訂單狀態：支付中");
                break;
            case 3: // 已支付
                statusTV.setText("訂單狀態：已支付");
                break;
            case 4: // 已接單
                statusTV.setText("訂單狀態：已投幣");
                break;
            case 5: // 操作中
                statusTV.setText("訂單狀態：操作中");
                break;
            case 6: // 已完成
                statusTV.setText("訂單狀態：已完成");
                break;
            case 9: // 已取消
                statusTV.setText("訂單狀態：已取消");
                break;
        }
    }

    @Override
    public void onClick(View v) {
        if(BSSMCommonUtils.isFastDoubleClick()) {
            return;
        }

        switch (v.getId()) {
            case R.id.head_left:
                finish();
                break;
            case R.id.head_right_tv:
                switch (mUserOrderModel.getOrderType()) {
                    case 1: // 充值訂單
                        Intent intent1 = new Intent(OrderDetialActivity.this, PaymentAcvtivity.class);
                        intent1.putExtra("startWay", 3);   // carChargeOrder
                        intent1.putExtra("orderNo", mUserOrderModel.getOrderNo());
                        intent1.putExtra("amount", String.valueOf(mUserOrderModel.getTotalAmount()));
                        intent1.putExtra("createTime", mUserOrderModel.getCreateTime());
                        intent1.putExtra("exchange", mUserOrderModel.getExchange());
                        intent1.putExtra("exchangeAmountPay", mUserOrderModel.getExchangeAmountPay());
                        startActivityForResult(intent1, 1);
                        break;
                    case 2: // 會員續費訂單
                        Intent intent2 = new Intent(OrderDetialActivity.this, PaymentAcvtivity.class);
                        intent2.putExtra("startWay", 2);   // carChargeOrder
                        intent2.putExtra("orderNo", mUserOrderModel.getOrderNo());
                        intent2.putExtra("amount", String.valueOf(mUserOrderModel.getTotalAmount()));
                        intent2.putExtra("createTime", mUserOrderModel.getCreateTime());
                        intent2.putExtra("exchange", mUserOrderModel.getExchange());
                        intent2.putExtra("exchangeAmountPay", mUserOrderModel.getExchangeAmountPay());
                        startActivityForResult(intent2, 1);
                        break;
                    case 3: // 確定投幣機訂單
                        Intent intent3 = new Intent(OrderDetialActivity.this, PaymentAcvtivity.class);
                        intent3.putExtra("startWay", 1);   // parkingOrder
                        intent3.putExtra("orderNo", mUserOrderModel.getOrderNo());
                        intent3.putExtra("amount", String.valueOf(mUserOrderModel.getTotalAmount()));
                        intent3.putExtra("createTime", mUserOrderModel.getCreateTime());
                        intent3.putExtra("exchange", mUserOrderModel.getExchange());
                        intent3.putExtra("exchangeAmountPay", mUserOrderModel.getExchangeAmountPay());
                        startActivityForResult(intent3, 1);
                        break;
                    case 4: // 未知投幣機訂單
                        Intent intent4 = new Intent(OrderDetialActivity.this, PaymentAcvtivity.class);
                        intent4.putExtra("startWay", 1);   // parkingOrder
                        intent4.putExtra("orderNo", mUserOrderModel.getOrderNo());
                        intent4.putExtra("amount", String.valueOf(mUserOrderModel.getTotalAmount()));
                        intent4.putExtra("createTime", mUserOrderModel.getCreateTime());
                        intent4.putExtra("exchange", mUserOrderModel.getExchange());
                        intent4.putExtra("exchangeAmountPay", mUserOrderModel.getExchangeAmountPay());
                        startActivityForResult(intent4, 1);
                        break;
                }
                break;
            case R.id.cancellation_of_order:
                NiceDialog.init()
                        .setLayoutId(R.layout.dialog_normal)
                        .setConvertListener(new ViewConvertListener() {
                            @Override
                            protected void convertView(ViewHolder viewHolder, final BaseNiceDialog baseNiceDialog) {
                                TextView msgTv = viewHolder.getView(R.id.dialog_normal_msg);
                                msgTv.setText("是否取消訂單，取消訂單只會返還下個時間段嘅錢！");
                                viewHolder.setOnClickListener(R.id.dialog_normal_no, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        baseNiceDialog.dismiss();
                                    }
                                });
                                viewHolder.setOnClickListener(R.id.dialog_normal_yes, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        if (BSSMCommonUtils.isLoginNow(OrderDetialActivity.this)) {
                                            callNetCancelOrder();
                                        } else {
                                            startActivityForResult(new Intent(OrderDetialActivity.this, LoginActivity.class), BSSMConfigtor.LOGIN_FOR_RQUEST);
                                        }
                                        baseNiceDialog.dismiss();
                                    }
                                });
                            }
                        })
                        .setWidth(210)
                        .show(getSupportFragmentManager());
                break;
        }
    }
    /**
     * 修改車輛信息
     */
    private void callNetCancelOrder() {
        RequestParams params = new RequestParams(BSSMConfigtor.BASE_URL + BSSMConfigtor.CANCELLATION_OF_ORDER + SPUtils.get(this, "UserToken", ""));
        params.setAsJsonContent(true);
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("orderNo",mUserOrderModel.getOrderNo());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        params.setBodyContent(jsonObj.toString());
        params.setConnectTimeout(30 * 1000);
        x.http().request(HttpMethod.POST, params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                CommonModel model = new Gson().fromJson(result.toString(), CommonModel.class);
                if (model.getCode().equals("200")) {
                    Toast.makeText(OrderDetialActivity.this, " 取消訂單成功！", Toast.LENGTH_SHORT).show();
                    finish();
                } else if (model.getCode().equals("10000")) {
                    SPUtils.put(OrderDetialActivity.this, "UserToken", "");
                    startActivityForResult(new Intent(OrderDetialActivity.this, LoginActivity.class), BSSMConfigtor.LOGIN_FOR_RQUEST);
                } else {
                    Toast.makeText(OrderDetialActivity.this, " 取消訂單失敗！" + String.valueOf(model.getMsg()), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if (ex instanceof SocketTimeoutException) {
                    Toast.makeText(OrderDetialActivity.this, "取消訂單失敗" + getString(R.string.timeout), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(OrderDetialActivity.this, getString(R.string.no_net), Toast.LENGTH_SHORT).show();
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 1:
                    setResult(RESULT_OK);
                    finish();
                    break;
            }
        }
    }
}
