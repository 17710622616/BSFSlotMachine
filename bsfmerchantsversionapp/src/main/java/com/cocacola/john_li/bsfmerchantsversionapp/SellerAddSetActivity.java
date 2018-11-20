package com.cocacola.john_li.bsfmerchantsversionapp;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.cocacola.john_li.bsfmerchantsversionapp.Model.CommonModel;
import com.cocacola.john_li.bsfmerchantsversionapp.Utils.BSFCommonUtils;
import com.cocacola.john_li.bsfmerchantsversionapp.Utils.BSFMerchantConfigtor;
import com.cocacola.john_li.bsfmerchantsversionapp.Utils.SPUtils;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by John_Li on 20/11/2018.
 */

public class SellerAddSetActivity extends FragmentActivity  implements View.OnClickListener{
    private ImageView backIv, submitIv;
    private EditText nameEt, costPriceEt, marktPriceEt, desEt;
    private DatePicker exprieTimeDP;
    ProgressDialog dialog;

    private String expireTime;
    @RequiresApi(api = 26)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_add_set);
        initView();
        setListener();
        initData();
    }

    private void initView() {
        backIv = findViewById(R.id.seller_add_set_back);
        submitIv = findViewById(R.id.seller_add_set_submit);
        nameEt = findViewById(R.id.seller_add_set_name_et);
        costPriceEt = findViewById(R.id.seller_add_set_market_price_et);
        marktPriceEt = findViewById(R.id.seller_add_set_cost_price_et);
        desEt = findViewById(R.id.seller_add_set_des_et);
        exprieTimeDP = findViewById(R.id.seller_add_set_exprie_dp);
    }

    @RequiresApi(api = 26)
    private void setListener() {
        backIv.setOnClickListener(this);
        submitIv.setOnClickListener(this);
    }

    private void initData() {
        //
        dialog = new ProgressDialog(this);
        exprieTimeDP.init(BSFCommonUtils.getYear(), BSFCommonUtils.getMonth(), BSFCommonUtils.getDayOfMonth(), new DatePicker.OnDateChangedListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // 获取一个日历对象，并初始化为当前选中的时间
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, monthOfYear, dayOfMonth);
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd ");
                expireTime = format.format(calendar.getTime()).toString() + " 00:00:00";
            }
        });
        Calendar nowTime = Calendar.getInstance();
        exprieTimeDP.setMinDate(nowTime.getTimeInMillis()-1000);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.seller_add_set_back:
                finish();
                break;
            case R.id.seller_add_set_submit:
                checkDataFull();
                break;
        }
    }

    private void checkDataFull() {
        String name = nameEt.getText().toString();
        String costPrice = costPriceEt.getText().toString();
        String marketPrice = marktPriceEt.getText().toString();
        String des = desEt.getText().toString();

        if (name != null && costPrice != null && marketPrice != null && des != null) {
            if (!name.equals("") && !costPrice.equals("") && !marketPrice.equals("") && !des.equals("")) {
                submitSellerSet(name, costPrice, marketPrice, des);
            } else {
                Toast.makeText(this, "請完善信息", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "請完善信息", Toast.LENGTH_SHORT).show();
        }
    }

    private void submitSellerSet(String name, String costPrice, String marketPrice, String des) {
        dialog.setTitle("提示");
        dialog.setMessage("正在提交套餐......");
        dialog.setCancelable(false);
        dialog.show();
        RequestParams params = new RequestParams(BSFMerchantConfigtor.BASE_URL + BSFMerchantConfigtor.SELLER_ADD_SET);
        params.setAsJsonContent(true);
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("chargeName",name);
            jsonObj.put("marketPrice",marketPrice);
            jsonObj.put("costPrice",costPrice);
            jsonObj.put("description",des);
            jsonObj.put(" exprieTime",expireTime);
            jsonObj.put("sellerToken", SPUtils.get(SellerAddSetActivity.this.getApplicationContext(), "SellerUserToken", ""));
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
                    setResult(RESULT_OK);
                    Toast.makeText(SellerAddSetActivity.this,  "添加套餐成功！", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    finish();
                } else if (model.getCode() == 10000){
                    dialog.dismiss();
                    SPUtils.put(SellerAddSetActivity.this, "SellerUserToken", "");
                    Toast.makeText(SellerAddSetActivity.this,  String.valueOf(model.getMsg().toString()), Toast.LENGTH_SHORT).show();
                } else {
                    dialog.dismiss();
                    Toast.makeText(SellerAddSetActivity.this,  String.valueOf(model.getMsg().toString()), Toast.LENGTH_SHORT).show();
                }
            }
            //请求异常后的回调方法
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if (ex instanceof SocketTimeoutException) {
                    dialog.dismiss();
                    Toast.makeText(SellerAddSetActivity.this, "請求超時，請重試！", Toast.LENGTH_SHORT).show();
                } else {
                    dialog.dismiss();
                    Toast.makeText(SellerAddSetActivity.this, getString(R.string.no_net), Toast.LENGTH_SHORT).show();
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
}
