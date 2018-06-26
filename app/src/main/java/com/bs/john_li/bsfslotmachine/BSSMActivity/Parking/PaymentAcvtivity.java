package com.bs.john_li.bsfslotmachine.BSSMActivity.Parking;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.bs.john_li.bsfslotmachine.BSSMActivity.BaseActivity;
import com.bs.john_li.bsfslotmachine.BSSMActivity.MainActivity;
import com.bs.john_li.bsfslotmachine.BSSMActivity.Mine.HistoryOrderActivity;
import com.bs.john_li.bsfslotmachine.BSSMActivity.Mine.PersonalSettingActivity;
import com.bs.john_li.bsfslotmachine.BSSMModel.CommonModel;
import com.bs.john_li.bsfslotmachine.BSSMModel.JuheExchangeModel;
import com.bs.john_li.bsfslotmachine.BSSMModel.WechatPrePayIDModel;
import com.bs.john_li.bsfslotmachine.BSSMUtils.BSSMCommonUtils;
import com.bs.john_li.bsfslotmachine.BSSMUtils.BSSMConfigtor;
import com.bs.john_li.bsfslotmachine.BSSMUtils.DigestUtils;
import com.bs.john_li.bsfslotmachine.BSSMUtils.PayResult;
import com.bs.john_li.bsfslotmachine.BSSMUtils.SPUtils;
import com.bs.john_li.bsfslotmachine.BSSMView.BSSMHeadView;
import com.bs.john_li.bsfslotmachine.BSSMView.FaceView;
import com.bs.john_li.bsfslotmachine.BSSMView.ShowTiemTextView;
import com.bs.john_li.bsfslotmachine.R;
import com.bs.john_li.bsfslotmachine.SplashActivity;
import com.google.android.gms.vision.text.Text;
import com.google.gson.Gson;
import com.othershe.nicedialog.BaseNiceDialog;
import com.othershe.nicedialog.NiceDialog;
import com.othershe.nicedialog.ViewConvertListener;
import com.othershe.nicedialog.ViewHolder;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.common.util.MD5;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 投幣訂單支付界面
 * Created by John_Li on 14/10/2017.
 */

public class PaymentAcvtivity extends BaseActivity implements View.OnClickListener, ShowTiemTextView.EndPayTimeCallback {
    private BSSMHeadView headView;
    private TextView orderNoTv, submitTv;
    private ShowTiemTextView mShowTiemTextView;
    private CheckBox myWalletCb, alipayCb, wecahtPayCb;
    private ProgressBar payment_submit_progress;

    // 匯率
    private JuheExchangeModel exchangeModel;
    private String orderNo;
    private String orderTime;
    private int startWay = 0; // 1是停車訂單。2是會員充值訂單。3是錢包充值訂單
    // 支付金額
    private double payMoney;
    // 微信API
    IWXAPI wxApi;
    // 支付寶支付回調
    private static final int SDK_PAY_FLAG = 1;
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        Toast.makeText(PaymentAcvtivity.this, "支付寶支付成功", Toast.LENGTH_SHORT).show();
                        orderPaySuccess();
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(PaymentAcvtivity.this, "支付寶支付失敗", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
                default:
                    break;
            }
        };
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        EventBus.getDefault().register(this);
        exchangeMop();
        initView();
        setListener();
        initData();
    }

    @Override
    public void initView() {
        headView = findViewById(R.id.payment_head);
        orderNoTv = findViewById(R.id.payment_orderNo);
        submitTv = findViewById(R.id.payment_submit);
        mShowTiemTextView = findViewById(R.id.payment_showtime_tv);
        myWalletCb = findViewById(R.id.payment_my_wallet_cb);
        alipayCb = findViewById(R.id.payment_alipay_cb);
        wecahtPayCb = findViewById(R.id.payment_wecaht_pay_cb);
        payment_submit_progress = findViewById(R.id.payment_submit_progress);
    }

    @Override
    public void setListener() {
        myWalletCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    alipayCb.setChecked(false);
                    wecahtPayCb.setChecked(false);
                    submitTv.setText("MOP" + "100" + "  確認支付");
                }
            }
        });
        alipayCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    myWalletCb.setChecked(false);
                    wecahtPayCb.setChecked(false);
                    if(exchangeModel != null) {
                        submitTv.setText("RMB" + (String.format("%.2f",100 * Double.parseDouble(exchangeModel.getResult().get(0).getExchange())).toString()) + "元  確認支付");
                    } else {
                        exchangeMop();
                        Toast.makeText(PaymentAcvtivity.this, "匯率獲取失敗，請重試！", Toast.LENGTH_SHORT).show();
                        myWalletCb.setChecked(true);
                        alipayCb.setChecked(false);
                        wecahtPayCb.setChecked(false);
                    }
                }
            }
        });
        wecahtPayCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    myWalletCb.setChecked(false);
                    alipayCb.setChecked(false);
                    if(exchangeModel != null) {
                        submitTv.setText("RMB" + (String.format("%.2f",100 * Double.parseDouble(exchangeModel.getResult().get(0).getExchange())).toString()) + "元  確認支付");
                    } else {
                        exchangeMop();
                        Toast.makeText(PaymentAcvtivity.this, "匯率獲取失敗，請重試！", Toast.LENGTH_SHORT).show();
                        myWalletCb.setChecked(true);
                        alipayCb.setChecked(false);
                        wecahtPayCb.setChecked(false);
                    }
                }
            }
        });
        submitTv.setOnClickListener(this);
        mShowTiemTextView.setmEndPayTimeCallback(this);
    }

    @Override
    public void initData() {
        headView.setTitle("訂單支付");
        headView.setLeft(this);

        Intent intent = getIntent();
        startWay = intent.getIntExtra("startWay", 0);
        orderNo = intent.getStringExtra("orderNo");
        orderTime = String.valueOf(intent.getLongExtra("createTime", 0));
        orderNoTv.setText("訂單號：" + orderNo);
        // 微信註冊APPID
        wxApi = WXAPIFactory.createWXAPI(this, null);
        wxApi.registerApp(BSSMConfigtor.WECHAT_APPID);

        // 倒計時
        SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long between = 600;
        try {
            java.util.Date begin=dfs.parse(BSSMCommonUtils.stampToDate(orderTime));
            java.util.Date end = dfs.parse(BSSMCommonUtils.getTimeNoW());
            between = (end.getTime()-begin.getTime())/1000;
        } catch (Exception e) {
            e.printStackTrace();
        }
        mShowTiemTextView.setTime((int)between);
        mShowTiemTextView.beginRun();
        myWalletCb.setChecked(true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            // 监控返回键
            exitPayment();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.head_left:
                exitPayment();
                break;
            case R.id.payment_submit:
                payment_submit_progress.setVisibility(View.VISIBLE);
                if (myWalletCb.isChecked()) {
                    // 發起錢包支付，先查看是否有支付密碼0：請求失敗，1：請求成功且有支付密碼，2：請求成功但無支付密碼
                    callNetCheckHasPayPw();
                } else if (alipayCb.isChecked()){
                    callNetGetAlipayOrderInfo();
                } else if (wecahtPayCb.isChecked()) {
                    if (wxApi.isWXAppInstalled() && wxApi.isWXAppSupportAPI()) {
                        // 发起微信支付，先请求获取微信的prepay_id
                        callNetGetWechatPrepayId();
                    } else {
                        payment_submit_progress.setVisibility(View.GONE);
                        Toast.makeText(this, "請先安裝微信客戶端！", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    payment_submit_progress.setVisibility(View.GONE);
                    Toast.makeText(this, "請選擇支付方式", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    /**
     * 像后台获取支付寶支付的订单详情
     */
    private void callNetGetAlipayOrderInfo() {
        String orderInfo = "666";
        // 發起支付寶支付，喚起支付寶SDK
        callAliPay(orderInfo);
    }

    /**
     * 發起支付寶支付，喚起支付寶SDK
     * @param orderInfo
     */
    private void callAliPay(final String orderInfo) {
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(PaymentAcvtivity.this);
                Map<String, String> result = alipay.payV2(orderInfo, true);
                Log.i("msp", result.toString());

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    /**
     * 发起微信请求，向后台获取预支付Data
     */
    private void callNetGetWechatPrepayId() {
        RequestParams params = new RequestParams(BSSMConfigtor.BASE_URL + BSSMConfigtor.POST_WECHAT_PAY_PRE_PAY_ID + SPUtils.get(this, "UserToken", ""));
        params.setAsJsonContent(true);
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("orderNo", orderNo);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        params.setBodyContent(jsonObj.toString());
        String uri = params.getUri();
        x.http().request(HttpMethod.POST ,params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                WechatPrePayIDModel model = new Gson().fromJson(result.toString(), WechatPrePayIDModel.class);
                if (model.getCode() == 200) {
                    PayReq request = new PayReq();
                    request.appId = BSSMConfigtor.WECHAT_APPID;
                    request.partnerId = model.getData().getPartnerid();
                    request.prepayId= model.getData().getPrepayid();
                    request.packageValue = "Sign=WXPay";
                    request.nonceStr= model.getData().getNoncestr();
                    request.timeStamp= model.getData().getTimestamp();
                    request.sign = model.getData().getSign();
                    if (wxApi == null) {
                        // 微信註冊APPID
                        wxApi = WXAPIFactory.createWXAPI(PaymentAcvtivity.this, null);
                    }

                    wxApi.registerApp(BSSMConfigtor.WECHAT_APPID);
                    wxApi.sendReq(request);
                } else {
                    Toast.makeText(PaymentAcvtivity.this, "發起支付失敗," + model.getMsg().toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if (ex instanceof java.net.SocketTimeoutException) {
                    Toast.makeText(PaymentAcvtivity.this, "發起支付超時，請重試", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(PaymentAcvtivity.this, "發起支付錯誤，請重新提交", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                payment_submit_progress.setVisibility(View.GONE);
            }
        });
    }

    /**
     * 請求確認是否有支付密碼
     */
    private void callNetCheckHasPayPw() {
        payment_submit_progress.setVisibility(View.GONE);
        NiceDialog.init()
                .setLayoutId(R.layout.dialog_loading)
                .setConvertListener(new ViewConvertListener() {
                    @Override
                    public void convertView(ViewHolder holder, final BaseNiceDialog dialog) {
                        final ImageView loadingIv = holder.getView(R.id.dialog_loading_iv);
                        final TextView loadingTv = holder.getView(R.id.dialog_loading_tv);
                        final LinearLayout loadingLL = holder.getView(R.id.dialog_loading_ll);
                        loadingTv.setText("發起支付中......");

                        RequestParams params = new RequestParams(BSSMConfigtor.BASE_URL + BSSMConfigtor.GET_USER_HAS_PAY_PW + SPUtils.get(PaymentAcvtivity.this, "UserToken", ""));
                        params.setConnectTimeout(30 * 1000);
                        x.http().request(HttpMethod.GET, params, new Callback.CommonCallback<String>() {
                            @Override
                            public void onSuccess(String result) {
                                CommonModel model = new Gson().fromJson(result.toString(), CommonModel.class);
                                if (model.getCode().equals("200")) {
                                    String hasPayPw = model.getData().toString();
                                    Log.d("getUserURI", "獲取用戶是否有支付密碼成功");
                                    if (hasPayPw.equals("true")) {  // 發起支付成功，且有支付密碼
                                        SPUtils.put(PaymentAcvtivity.this, "HasPayPw", "1");
                                        dialog.dismiss();
                                        NiceDialog.init()
                                                .setLayoutId(R.layout.dialog_wallet_paypw)
                                                .setConvertListener(new ViewConvertListener() {
                                                    @Override
                                                    public void convertView(ViewHolder holder, final BaseNiceDialog dialog) {
                                                        final EditText editText = holder.getView(R.id.pay_pw_edit);
                                                        final LinearLayout payingLL = holder.getView(R.id.pay_paying_ll);
                                                        final FaceView pay_faceview = holder.getView(R.id.pay_faceview);
                                                        final TextView pay_status_tv = holder.getView(R.id.pay_status_tv);
                                                        BSSMCommonUtils.showKeyboard(editText);
                                                        holder.setOnClickListener(R.id.pay_pw_submit, new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View view) {
                                                                pay_faceview.reset();
                                                                payingLL.setVisibility(View.VISIBLE);
                                                                String enterPw = editText.getText().toString();
                                                                if (!enterPw.equals("")) {
                                                                    callNetSubmitPayment(enterPw, dialog, pay_faceview, payingLL, pay_status_tv);
                                                                } else {
                                                                    Toast.makeText(PaymentAcvtivity.this, "支付密碼不可為空！！！", Toast.LENGTH_SHORT);
                                                                }
                                                            }
                                                        });

                                                        holder.setOnClickListener(R.id.pay_paying_ll, new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View view) {
                                                                payingLL.setVisibility(View.GONE);
                                                                pay_faceview.reset();
                                                            }
                                                        });
                                                    }
                                                })
                                                .setShowBottom(true)
                                                .show(getSupportFragmentManager());
                                    } else {
                                        loadingIv.setVisibility(View.INVISIBLE);
                                        loadingTv.setText("您暫未設置支付密碼，點我去個人中心設置！");
                                        loadingLL.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent intent = new Intent(PaymentAcvtivity.this, PersonalSettingActivity.class);
                                                startActivity(intent);
                                                dialog.dismiss();
                                                finish();
                                            }
                                        });
                                    }
                                } else {
                                    dialog.dismiss();
                                    Toast.makeText(PaymentAcvtivity.this, "發起支付失敗，" + model.getMsg().toString(), Toast.LENGTH_SHORT);
                                }
                            }

                            //请求异常后的回调方法
                            @Override
                            public void onError(Throwable ex, boolean isOnCallback) {
                                dialog.dismiss();
                                if (ex instanceof java.net.SocketTimeoutException) {
                                    Toast.makeText(PaymentAcvtivity.this, "發起支付超時，請重試！！！", Toast.LENGTH_SHORT);
                                } else {
                                    Toast.makeText(PaymentAcvtivity.this, "發起支付失敗，請重試！！！", Toast.LENGTH_SHORT);
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
                })
                .setShowBottom(false)
                .setWidth(300)
                .show(getSupportFragmentManager());
    }

    /**
     * 請求錢包支付
     * @param enterPw
     */
    private void callNetSubmitPayment(String enterPw, final BaseNiceDialog dialog, final FaceView pay_faceview, final LinearLayout payingLL, final TextView pay_status_tv) {
        RequestParams params = new RequestParams(BSSMConfigtor.BASE_URL + BSSMConfigtor.POST_WALLET_PAY + SPUtils.get(this, "UserToken", ""));
        params.setAsJsonContent(true);
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("orderNo", orderNo);
            jsonObj.put("password", DigestUtils.getMD5Str(enterPw));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        params.setBodyContent(jsonObj.toString());
        String uri = params.getUri();
        x.http().request(HttpMethod.POST ,params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                CommonModel model = new Gson().fromJson(result.toString(), CommonModel.class);
                if (model.getCode().equals("200")) {
                    pay_faceview.setStatus(FaceView.SUCCESS);
                    pay_status_tv.setText("支付成功！");
                    Toast.makeText(PaymentAcvtivity.this, "支付成功！", Toast.LENGTH_SHORT).show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            /*if (startWay == 1) {
                                EventBus.getDefault().post("ParkingOrderSuccess");
                            }*/
                            dialog.dismiss();
                            /*setResult(RESULT_OK);
                            finish();*/
                            orderPaySuccess();
                        }
                    }, 1500);
                } else {
                    pay_faceview.setStatus(FaceView.FAILED);
                    pay_status_tv.setText("支付失敗，點我重試");
                    Toast.makeText(PaymentAcvtivity.this, "支付失敗," + model.getMsg().toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                pay_faceview.setStatus(FaceView.FAILED);
                pay_status_tv.setText("支付失敗，點我重試");
                if (ex instanceof java.net.SocketTimeoutException) {
                    Toast.makeText(PaymentAcvtivity.this, "支付超時，請重試", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(PaymentAcvtivity.this, "支付錯誤，請重新提交", Toast.LENGTH_SHORT).show();
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

    /**
     * 退出支付
     */
    private void exitPayment() {
        new AlertDialog.Builder(PaymentAcvtivity.this).setTitle("提醒")
                .setIconAttribute(android.R.attr.alertDialogIcon)
                .setMessage("確認要退出支付咩?")
                .setPositiveButton("確認", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mShowTiemTextView.stopRun();
                        PaymentAcvtivity.this.finish();
                    }})
                .setNegativeButton("取消", null)
                .create().show();
    }

    /**
     * 請求聚合數據，查詢匯率
     */
    private void exchangeMop() {
        RequestParams params = new RequestParams("http://op.juhe.cn/onebox/exchange/currency?key=" + BSSMConfigtor.JUHE_APPKEY + "&from=MOP&to=CNY");
        params.setConnectTimeout(30 * 1000);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                exchangeModel = new Gson().fromJson(result, JuheExchangeModel.class);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.d("失败", ex.toString());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
            }
        });
    }

    /**
     * 倒計時結束，訂單支付時間結束
     */
    @Override
    public void endPayTime() {
        finish();
    }

    @Subscribe
    public void onEvent(String msg){
        if (msg.equals("WX_PAY_SUCCESS")) {
            Toast.makeText(PaymentAcvtivity.this, "wechat支付成功！", Toast.LENGTH_SHORT).show();
            orderPaySuccess();
        }
    }

    /**
     * 訂單支付成功
     */
    private void orderPaySuccess() {
        startActivity(new Intent(this, HistoryOrderActivity.class));
        finish();
    }
}
