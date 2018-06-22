package com.bs.john_li.bsfslotmachine.wxapi;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.bs.john_li.bsfslotmachine.BSSMUtils.BSSMConfigtor;
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        api = WXAPIFactory.createWXAPI(this, BSSMConfigtor.WECHAT_APPID);
        api.handleIntent(getIntent(), this);
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

        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            /*AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("提示");
            builder.setMessage(getString(R.string.pay_result_callback_msg, String.valueOf(resp.errCode)));
            builder.show();*/
            if (resp.errCode == 0) {
                Toast.makeText(this, "付款成功", Toast.LENGTH_LONG).show();
                EventBus.getDefault().post("WX_PAY_SUCCESS");
            } else if (resp.errCode == -2){
                Toast.makeText(this, "您已取消微信付款！", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "參數錯誤", Toast.LENGTH_LONG).show();
            }
        }
    }
}
