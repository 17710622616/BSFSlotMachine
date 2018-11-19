package com.bs.john_li.bsfslotmachine.BSSMActivity.CarService;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bs.john_li.bsfslotmachine.BSSMActivity.BaseActivity;
import com.bs.john_li.bsfslotmachine.BSSMModel.CWUserOrderDetialOutModel;
import com.bs.john_li.bsfslotmachine.BSSMModel.CommonJieModel;
import com.bs.john_li.bsfslotmachine.BSSMModel.CommonModel;
import com.bs.john_li.bsfslotmachine.BSSMUtils.BSSMConfigtor;
import com.bs.john_li.bsfslotmachine.BSSMUtils.SPUtils;
import com.bs.john_li.bsfslotmachine.BSSMView.BSSMHeadView;
import com.bs.john_li.bsfslotmachine.R;
import com.google.gson.Gson;
import com.othershe.nicedialog.NiceDialog;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.net.SocketTimeoutException;

/**
 * Created by John_Li on 19/11/2018.
 */

public class ApplyForRefundActivity extends BaseActivity implements View.OnClickListener{
    private BSSMHeadView headView;
    private RadioGroup applyForRefundRg;
    private TextView refundMoney;
    ProgressDialog dialog;

    private TextView submitRefund;
    private String refundReason;
    private CWUserOrderDetialOutModel.DataBean.SellerOrderBean mSellerOrderBean;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_for_refund);
        initView();
        setListener();
        initData();
    }

    @Override
    public void initView() {
        headView = findViewById(R.id.apply_for_refund_head);
        applyForRefundRg = findViewById(R.id.apply_for_refund_rg);
        refundMoney = findViewById(R.id.refund_money);
        submitRefund = findViewById(R.id.submit_refund_order);
    }

    @Override
    public void setListener() {
        submitRefund.setOnClickListener(this);
        applyForRefundRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                RadioButton radioButton = (RadioButton) group.findViewById(checkedId);
                refundReason = radioButton.getText().toString();
            }
        });
    }

    @Override
    public void initData() {
        headView.setTitle("申請退款");
        headView.setLeft(this);
        mSellerOrderBean = new Gson().fromJson(getIntent().getStringExtra("mSellerOrderBean"), CWUserOrderDetialOutModel.DataBean.SellerOrderBean.class);
        refundMoney.setText("現金：MOP" + mSellerOrderBean.getPayAmount());
        applyForRefundRg.check(R.id.apply_refund_regret);
        dialog = new ProgressDialog(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_left:
                finish();
                break;
            case R.id.submit_refund_order:
                dialog.setTitle("提示");
                dialog.setMessage("正在提交車輛......");
                dialog.setCancelable(false);
                dialog.show();
                callNetRefundCarWashOrder();
                break;
        }
    }

    private void callNetRefundCarWashOrder() {
        RequestParams params = new RequestParams(BSSMConfigtor.BASE_URL + BSSMConfigtor.POST_REFUND_CAR_WASH_ORDER + SPUtils.get(ApplyForRefundActivity.this.getApplicationContext(), "UserToken", ""));
        params.setAsJsonContent(true);
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("orderNo",mSellerOrderBean.getOrderNo());
            jsonObj.put("moneyBackReason", refundReason);
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
                CommonJieModel model = new Gson().fromJson(result.toString(), CommonJieModel.class);
                if (model.getCode() == 200) {
                    Toast.makeText(ApplyForRefundActivity.this,  "退款申請提交成功!", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                } else if (model.getCode() == 10000){
                    SPUtils.put(ApplyForRefundActivity.this, "UserToken", "");
                    Toast.makeText(ApplyForRefundActivity.this,  String.valueOf(model.getMsg()), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ApplyForRefundActivity.this,  String.valueOf(model.getMsg()), Toast.LENGTH_SHORT).show();
                }
            }
            //请求异常后的回调方法
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if (ex instanceof SocketTimeoutException) {
                    Toast.makeText(ApplyForRefundActivity.this, "請求超時，請重試！", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ApplyForRefundActivity.this, getString(R.string.no_net), Toast.LENGTH_SHORT).show();
                }
            }
            //主动调用取消请求的回调方法
            @Override
            public void onCancelled(CancelledException cex) {
            }
            @Override
            public void onFinished() {
                dialog.dismiss();
            }
        });
    }
}
