package com.bs.john_li.bsfslotmachine.BSSMActivity.CarService;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import com.bs.john_li.bsfslotmachine.BSSMActivity.BaseActivity;
import com.bs.john_li.bsfslotmachine.BSSMModel.MerchatSetOutModel;
import com.bs.john_li.bsfslotmachine.BSSMUtils.BSSMConfigtor;
import com.bs.john_li.bsfslotmachine.BSSMUtils.SPUtils;
import com.bs.john_li.bsfslotmachine.BSSMView.BSSMHeadView;
import com.bs.john_li.bsfslotmachine.R;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.net.SocketTimeoutException;
import java.util.List;

/**
 * Created by John on 14/11/2018.
 */

public class MerchatSetActivity extends BaseActivity implements View.OnClickListener{
    private BSSMHeadView headView;

    private int merchatId;
    private MerchatSetOutModel.MerchatSetModel merchatSetModel;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchat_set);
        initView();
        setListener();
        initData();
    }

    @Override
    public void initView() {
        headView = findViewById(R.id.merchar_set_head);
    }

    @Override
    public void setListener() {

    }

    @Override
    public void initData() {
        headView.setLeft(this);
        headView.setTitle("特價洗車");

        merchatId = Integer.parseInt(getIntent().getStringExtra("merchatId"));
        callNetGetMerchatData();
    }

    private void callNetGetMerchatData() {

        RequestParams params = new RequestParams(BSSMConfigtor.BASE_URL + BSSMConfigtor.GET_MERCHART_SET);
        params.setAsJsonContent(true);
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("id",merchatId);
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
                MerchatSetOutModel model = new Gson().fromJson(result.toString(), MerchatSetOutModel.class);
                if (model.getCode() ==200) {
                    merchatSetModel = model.getData();
                } else if (model.getCode() == 10000){
                    SPUtils.put(MerchatSetActivity.this, "UserToken", "");
                    Toast.makeText(MerchatSetActivity.this,  String.valueOf(model.getMsg()), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MerchatSetActivity.this,  String.valueOf(model.getMsg()), Toast.LENGTH_SHORT).show();
                }
            }
            //请求异常后的回调方法
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if (ex instanceof SocketTimeoutException) {
                    Toast.makeText(MerchatSetActivity.this, "請求超時，請重試！", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MerchatSetActivity.this, getString(R.string.no_net), Toast.LENGTH_SHORT).show();
                }
            }
            //主动调用取消请求的回调方法
            @Override
            public void onCancelled(CancelledException cex) {
            }
            @Override
            public void onFinished() {
                refreshUI();
            }
        });
    }

    /**
     * 獲取資料完成，刷新UI
     */
    private void refreshUI() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.head_left:
                finish();
                break;
        }
    }
}
