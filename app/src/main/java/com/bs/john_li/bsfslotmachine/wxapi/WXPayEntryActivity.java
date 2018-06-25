package com.bs.john_li.bsfslotmachine.wxapi;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bs.john_li.bsfslotmachine.BSSMUtils.BSSMConfigtor;
import com.bs.john_li.bsfslotmachine.BSSMView.BSSMHeadView;
import com.bs.john_li.bsfslotmachine.R;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by John_Li on 21/6/2018.
 */

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {
    private static final String TAG = "WXPayEntryActivity";
    private IWXAPI api;
    private TextView wxPayResultTv;
    private BSSMHeadView headView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wx_result);

        api = WXAPIFactory.createWXAPI(this, BSSMConfigtor.WECHAT_APPID);
        api.handleIntent(getIntent(), this);
        headView = findViewById(R.id.wx_pay_result_head);
        wxPayResultTv = findViewById(R.id.wx_pay_result_tv);

        headView.setTitle("微信支付結果");
        headView.setLeft(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {
        Log.d(TAG, "onPayFinish, errCode = " + resp.errCode);
        Toast.makeText(this, "onPayFinish, errCode =" + resp.errCode, Toast.LENGTH_LONG).show();

        try {
            if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            /*AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("提示");
            builder.setMessage(getString(R.string.pay_result_callback_msg, String.valueOf(resp.errCode)));
            builder.show();*/
                if (resp.errCode == 0) {
                    wxPayResultTv.setText("付款成功");
                    Toast.makeText(this, "付款成功", Toast.LENGTH_LONG).show();
                    EventBus.getDefault().post("WX_PAY_SUCCESS");
                    finish();
                } else if (resp.errCode == -2){
                    wxPayResultTv.setText("您已取消微信付款！");
                    Toast.makeText(this, "您已取消微信付款！", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    wxPayResultTv.setText("微信支付參數錯誤");
                    Toast.makeText(this, "微信支付參數錯誤", Toast.LENGTH_LONG).show();
                    finish();
                }
            } else {
                wxPayResultTv.setText("微信支付參數錯誤");
                Toast.makeText(this, "微信支付參數錯誤", Toast.LENGTH_LONG).show();
                finish();
            }
        } catch (Exception e) {
            wxPayResultTv.setText("微信支付參數錯誤");
            Toast.makeText(this, "微信支付參數錯誤", Toast.LENGTH_LONG).show();
            finish();
        }
    }
}
