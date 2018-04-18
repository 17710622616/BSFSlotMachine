package com.bs.john_li.bsfslotmachine.BSSMActivity.Mine;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.widget.TextViewCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bs.john_li.bsfslotmachine.BSSMActivity.BaseActivity;
import com.bs.john_li.bsfslotmachine.BSSMActivity.Parking.PaymentAcvtivity;
import com.bs.john_li.bsfslotmachine.BSSMAdapter.CarRechargeWayListAdapter;
import com.bs.john_li.bsfslotmachine.BSSMModel.CarModel;
import com.bs.john_li.bsfslotmachine.BSSMModel.CarRechargeWayListModel;
import com.bs.john_li.bsfslotmachine.BSSMModel.CommonModel;
import com.bs.john_li.bsfslotmachine.BSSMModel.OrderModel;
import com.bs.john_li.bsfslotmachine.BSSMUtils.AliyunOSSUtils;
import com.bs.john_li.bsfslotmachine.BSSMUtils.BSSMConfigtor;
import com.bs.john_li.bsfslotmachine.BSSMUtils.SPUtils;
import com.bs.john_li.bsfslotmachine.BSSMView.BSSMHeadView;
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
import java.util.List;

/**
 * 車輛充值
 * Created by John on 22/10/2017.
 */

public class CarRechargeActivity extends BaseActivity implements View.OnClickListener{
    private BSSMHeadView headView;
    private TextView carNoTv, carModelTv, carStyleTv, carBrandTv, carTypeTv,submitTv;
    private ImageView carIv;
    private RadioGroup rechargeWayRg;
    private RadioButton monthRb, quarterlyRb, sixMonthRb, yearRb;

    private long memberChargeId;
    private CarModel.CarCountAndListModel.CarInsideModel mCarInsideModel;
    private List<CarRechargeWayListModel.CarRechargeWayModel> mCarRechargeWayModelList;
    private ImageOptions options = new ImageOptions.Builder().setSize(0, 0).setLoadingDrawableId(R.mipmap.img_loading).setFailureDrawableId(R.mipmap.load_img_fail_list).build();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_recharge);
        initView();
        setListener();
        initData();
    }

    @Override
    public void initView() {
        headView = findViewById(R.id.recharge_head);
        carNoTv = findViewById(R.id.recharge_car_carno);
        carModelTv = findViewById(R.id.recharge_car_model);
        carStyleTv = findViewById(R.id.recharge_car_style);
        carBrandTv = findViewById(R.id.recharge_car_brand);
        carTypeTv = findViewById(R.id.recharge_car_type);
        submitTv = findViewById(R.id.recharge_car_submit);
        carIv = findViewById(R.id.recharge_car_iv);
        rechargeWayRg = findViewById(R.id.recharge_car_rg);
        monthRb = findViewById(R.id.one_month_rb);
        quarterlyRb = findViewById(R.id.one_quarterly_rb);
        sixMonthRb = findViewById(R.id.one_sixmonth_rb);
        yearRb = findViewById(R.id.one_year_rb);
    }

    @Override
    public void setListener() {
        submitTv.setOnClickListener(this);
        rechargeWayRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                if (mCarRechargeWayModelList != null) {
                    switch (i) {
                        case R.id.one_month_rb:
                            monthRb.setTextColor(getResources().getColor(R.color.colorSubmitGreen));
                            quarterlyRb.setTextColor(getResources().getColor(R.color.colorWayOringe));
                            sixMonthRb.setTextColor(getResources().getColor(R.color.colorWayOringe));
                            yearRb.setTextColor(getResources().getColor(R.color.colorWayOringe));
                            memberChargeId = mCarRechargeWayModelList.get(0).getId();
                            break;
                        case R.id.one_quarterly_rb:
                            monthRb.setTextColor(getResources().getColor(R.color.colorWayOringe));
                            quarterlyRb.setTextColor(getResources().getColor(R.color.colorSubmitGreen));
                            sixMonthRb.setTextColor(getResources().getColor(R.color.colorWayOringe));
                            yearRb.setTextColor(getResources().getColor(R.color.colorWayOringe));
                            memberChargeId = mCarRechargeWayModelList.get(1).getId();
                            break;
                        case R.id.one_sixmonth_rb:
                            monthRb.setTextColor(getResources().getColor(R.color.colorWayOringe));
                            quarterlyRb.setTextColor(getResources().getColor(R.color.colorWayOringe));
                            sixMonthRb.setTextColor(getResources().getColor(R.color.colorSubmitGreen));
                            yearRb.setTextColor(getResources().getColor(R.color.colorWayOringe));
                            memberChargeId = mCarRechargeWayModelList.get(2).getId();
                            break;
                        case R.id.one_year_rb:
                            monthRb.setTextColor(getResources().getColor(R.color.colorWayOringe));
                            quarterlyRb.setTextColor(getResources().getColor(R.color.colorWayOringe));
                            sixMonthRb.setTextColor(getResources().getColor(R.color.colorWayOringe));
                            yearRb.setTextColor(getResources().getColor(R.color.colorSubmitGreen));
                            memberChargeId = mCarRechargeWayModelList.get(3).getId();
                            break;
                    }
                }
            }
        });
    }

    @Override
    public void initData() {
        Intent intent = getIntent();

        headView.setTitle("車輛充值");
        headView.setLeft(this);
        mCarInsideModel = new Gson().fromJson(intent.getStringExtra("carModel"), CarModel.CarCountAndListModel.CarInsideModel.class);
        carNoTv.setText("車牌號碼：" + mCarInsideModel.getCarNo());
        carModelTv.setText("品牌："+mCarInsideModel.getModelForCar());
        carStyleTv.setText("車型："+mCarInsideModel.getCarStyle());
        carBrandTv.setText("型號："+mCarInsideModel.getCarBrand());
        //AliyunOSSUtils.downloadImg(mCarInsideModel.getImgUrl(), AliyunOSSUtils.initOSS(this), carIv, this, R.mipmap.load_img_fail_list);
        x.image().bind(carIv, mCarInsideModel.getImgUrl(), options);
        switch (mCarInsideModel.getIfPerson()) {
            case 1:
                carTypeTv.setText("車輛類型：輕重型電單車");
                break;
            case 2:
                carTypeTv.setText("車輛類型：私家車");
                break;
            case 3:
                carTypeTv.setText("車輛類型：重型汽車");
                break;
        }
        // 獲取充值方式
        getRechargeWay();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.head_left:
                finish();
                break;
            case R.id.recharge_car_submit:
                LoadDialog loadDialog = new LoadDialog(this, false, "提交中......");
                loadDialog.show();
                submitRechargeOrder(loadDialog);
                break;
        }
    }

    /**
     * 提交車輛充值訂單
     */
    private void getRechargeWay() {
        RequestParams params = new RequestParams(BSSMConfigtor.BASE_URL + BSSMConfigtor.CAR_CAHRGE_WAY_LIST);
        params.setConnectTimeout(30 * 1000);
        x.http().request(HttpMethod.POST ,params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                CarRechargeWayListModel model = new Gson().fromJson(result.toString(), CarRechargeWayListModel.class);
                if (model.getCode() ==200) {
                    mCarRechargeWayModelList = model.getData();
                } else {
                    Toast.makeText(CarRechargeActivity.this, String.valueOf(model.getMsg()), Toast.LENGTH_SHORT).show();
                }
            }
            //请求异常后的回调方法
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if (ex instanceof SocketTimeoutException) {
                    Toast.makeText(CarRechargeActivity.this, "充值方式提交失敗！", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(CarRechargeActivity.this, getString(R.string.no_net), Toast.LENGTH_SHORT).show();
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

    /**
     *提交車輛充值訂單
     */
    private void submitRechargeOrder(final LoadDialog loadDialog) {
        RequestParams params = new RequestParams(BSSMConfigtor.BASE_URL + BSSMConfigtor.SUBMIT_CAR_CAHRGE_ORDER + SPUtils.get(this, "UserToken", ""));
        params.setAsJsonContent(true);
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("memberChargeId",memberChargeId);
            jsonObj.put("carId",mCarInsideModel.getId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        params.setBodyContent(jsonObj.toString());
        String uri = params.getUri();
        params.setConnectTimeout(30 * 1000);
        x.http().request(HttpMethod.POST ,params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                OrderModel model = new Gson().fromJson(result.toString(), OrderModel.class);
                if (model.getCode() == 200) {
                    Intent intent = new Intent(CarRechargeActivity.this, PaymentAcvtivity.class);
                    intent.putExtra("startWay", 2);   // carChargeOrder
                    intent.putExtra("orderNo", model.getData().getOrderNo());
                    intent.putExtra("createTime", model.getData().getCreateTime());
                    startActivityForResult(intent, 1);
                } else {
                    Toast.makeText(CarRechargeActivity.this,  String.valueOf(model.getMsg()), Toast.LENGTH_SHORT).show();
                }
            }
            //请求异常后的回调方法
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if (ex instanceof SocketTimeoutException) {
                    Toast.makeText(CarRechargeActivity.this, "提交充值訂單超時", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(CarRechargeActivity.this, getString(R.string.no_net), Toast.LENGTH_SHORT).show();
                }
            }
            //主动调用取消请求的回调方法
            @Override
            public void onCancelled(CancelledException cex) {
            }
            @Override
            public void onFinished() {
                loadDialog.dismiss();
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
