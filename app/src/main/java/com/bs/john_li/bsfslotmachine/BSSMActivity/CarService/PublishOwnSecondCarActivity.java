package com.bs.john_li.bsfslotmachine.BSSMActivity.CarService;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bs.john_li.bsfslotmachine.BSSMActivity.BaseActivity;
import com.bs.john_li.bsfslotmachine.BSSMAdapter.SecondCarOptionListAdapter;
import com.bs.john_li.bsfslotmachine.BSSMModel.CarBrandOutModel;
import com.bs.john_li.bsfslotmachine.BSSMModel.SecondCarOutModel;
import com.bs.john_li.bsfslotmachine.BSSMModel.SubmitSecondCarModel;
import com.bs.john_li.bsfslotmachine.BSSMUtils.BSSMConfigtor;
import com.bs.john_li.bsfslotmachine.BSSMUtils.SPUtils;
import com.bs.john_li.bsfslotmachine.BSSMView.BSSMHeadView;
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
 * Created by John_Li on 22/11/2018.
 */

public class PublishOwnSecondCarActivity extends BaseActivity implements View.OnClickListener{
    private BSSMHeadView headView;
    private LinearLayout brandLL, seriesLL, styleLL,firstTimeLL,miealgeLL,gearsLL,priceLL,telLL,desLL,colorLL,modelLL,exhaustLL,countryLL,deliveryTimeLL,configInfoLL,repiarStatrLL,insideBodyLL,testConclusionLL;
    private TextView brandTv,seriesTv,styleTv,firstTimeLLTv,miealgeTv,gearsTv,priceTv,telTv,desTv,colorTv,modelTv,exhaustTv,countryTv,deliveryTimeTv,configInfoTv,repiarStatrTv,insideBodyTv,testConclusionTv;

    private SubmitSecondCarModel mSubmitSecondCarModel;
    private List<CarBrandOutModel.CarBrandModel> mCarBrandModelList;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_own_second_car);
        initView();
        setListener();
        initData();
    }

    @Override
    public void initView() {
        headView = findViewById(R.id.publish_own_second_car_head);
        brandLL = findViewById(R.id.publish_own_second_car_brand_ll);
        seriesLL = findViewById(R.id.publish_own_second_car_series_ll);
        styleLL = findViewById(R.id.publish_own_second_car_style_ll);
        firstTimeLL = findViewById(R.id.publish_own_second_car_first_time_ll);
        miealgeLL = findViewById(R.id.publish_own_second_car_miealge_ll);
        gearsLL = findViewById(R.id.publish_own_second_car_gears_ll);
        priceLL = findViewById(R.id.publish_own_second_car_price_ll);
        telLL = findViewById(R.id.publish_own_second_car_tel_ll);
        desLL = findViewById(R.id.publish_own_second_car_des_ll);
        colorLL = findViewById(R.id.publish_own_second_car_color_ll);
        modelLL = findViewById(R.id.publish_own_second_car_model_ll);
        exhaustLL = findViewById(R.id.publish_own_second_car_exhaust_ll);
        countryLL = findViewById(R.id.publish_own_second_car_country_ll);
        deliveryTimeLL = findViewById(R.id.publish_own_second_car_delivery_time_ll);
        configInfoLL = findViewById(R.id.publish_own_second_car_config_info_ll);
        repiarStatrLL = findViewById(R.id.publish_own_second_car_repiar_statr_ll);
        insideBodyLL = findViewById(R.id.publish_own_second_car_repiar_statr_ll);
        testConclusionLL = findViewById(R.id.publish_own_second_car_test_conclusion_ll);

        brandTv = findViewById(R.id.publish_own_second_car_brand_tv);
        seriesTv = findViewById(R.id.publish_own_second_car_series_tv);
        styleTv = findViewById(R.id.publish_own_second_car_style_tv);
        firstTimeLLTv = findViewById(R.id.publish_own_second_car_first_time_tv);
        miealgeTv = findViewById(R.id.publish_own_second_car_miealge_tv);
        gearsTv = findViewById(R.id.publish_own_second_car_gears_tv);
        priceTv = findViewById(R.id.publish_own_second_car_price_tv);
        telTv = findViewById(R.id.publish_own_second_car_tel_tv);
        desTv = findViewById(R.id.publish_own_second_car_des_tv);
        colorTv = findViewById(R.id.publish_own_second_car_color_tv);
        modelTv = findViewById(R.id.publish_own_second_car_model_tv);
        exhaustTv = findViewById(R.id.publish_own_second_car_exhaust_tv);
        countryTv = findViewById(R.id.publish_own_second_car_country_tv);
        deliveryTimeTv = findViewById(R.id.publish_own_second_car_delivery_time_tv);
        configInfoTv = findViewById(R.id.publish_own_second_car_config_info_tv);
        repiarStatrTv = findViewById(R.id.publish_own_second_car_repiar_statr_tv);
        insideBodyTv = findViewById(R.id.publish_own_second_car_inside_body_tv);
        testConclusionTv = findViewById(R.id.publish_own_second_car_test_conclusion_tv);
    }

    @Override
    public void setListener() {
        brandLL.setOnClickListener(this);
        seriesLL.setOnClickListener(this);
        styleLL.setOnClickListener(this);
        firstTimeLL.setOnClickListener(this);
        miealgeLL.setOnClickListener(this);
        gearsLL.setOnClickListener(this);
        priceLL.setOnClickListener(this);
        telLL.setOnClickListener(this);
        desLL.setOnClickListener(this);
        colorLL.setOnClickListener(this);
        modelLL.setOnClickListener(this);
        exhaustLL.setOnClickListener(this);
        countryLL.setOnClickListener(this);
        deliveryTimeLL.setOnClickListener(this);
        configInfoLL.setOnClickListener(this);
        repiarStatrLL.setOnClickListener(this);
        insideBodyLL.setOnClickListener(this);
        testConclusionLL.setOnClickListener(this);
    }

    @Override
    public void initData() {
        headView.setLeft(this);
        headView.setTitle("發佈車輛");
        headView.setRight(R.mipmap.ok, this);

        mSubmitSecondCarModel = new SubmitSecondCarModel();
        mCarBrandModelList = new ArrayList<>();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_left:
                finish();
                break;
            case R.id.head_right:
                //submitSecondCar();
                break;
            case R.id.publish_own_second_car_brand_ll:
                if (mCarBrandModelList.size() > 0) {
                    NiceDialog.init()
                            .setLayoutId(R.layout.dialog_car_brand_list)
                            .setConvertListener(new ViewConvertListener() {
                                @Override
                                protected void convertView(ViewHolder viewHolder, final BaseNiceDialog baseNiceDialog) {
                                    ListView lv = viewHolder.getView(R.id.dialog_car_brand_lv);
                                    final List<String> list = new ArrayList<String>();
                                    for (CarBrandOutModel.CarBrandModel carBrandModel : mCarBrandModelList) {
                                        list.add(carBrandModel.getEngName());
                                    }
                                    lv.setAdapter(new SecondCarOptionListAdapter(PublishOwnSecondCarActivity.this, list));
                                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                            mSubmitSecondCarModel.setCarBrand(list.get(i));
                                            brandTv.setText(mSubmitSecondCarModel.getCarBrand());
                                            baseNiceDialog.dismiss();
                                        }
                                    });
                                }
                            })
                            .setShowBottom(true)
                            .show(getSupportFragmentManager());
                } else {
                    callNetGetCarBrand();
                }
                break;
            case R.id.publish_own_second_car_series_ll:
                break;
            case R.id.publish_own_second_car_style_ll:
                break;
            case R.id.publish_own_second_car_first_time_ll:
                break;
            case R.id.publish_own_second_car_miealge_ll:
                break;
            case R.id.publish_own_second_car_gears_ll:
                break;
            case R.id.publish_own_second_car_price_ll:
                break;
            case R.id.publish_own_second_car_tel_ll:
                break;
            case R.id.publish_own_second_car_des_ll:
                break;
            case R.id.publish_own_second_car_color_ll:
                break;
            case R.id.publish_own_second_car_model_ll:
                break;
            case R.id.publish_own_second_car_exhaust_ll:
                break;
            case R.id.publish_own_second_car_country_ll:
                break;
            case R.id.publish_own_second_car_delivery_time_ll:
                break;
            case R.id.publish_own_second_car_config_info_ll:
                break;
            case R.id.publish_own_second_car_repiar_statr_ll:
                break;
            case R.id.publish_own_second_car_inside_body_tv:
                break;
            case R.id.publish_own_second_car_test_conclusion_ll:
                break;
        }
    }

    /**
     * 獲取車輛品牌列表
     */
    private void callNetGetCarBrand() {

        RequestParams params = new RequestParams(BSSMConfigtor.BASE_URL + BSSMConfigtor.GET_CAR_BRAND_LIST + SPUtils.get(this, "UserToken", ""));
        params.setAsJsonContent(true);
        params.setConnectTimeout(30 * 1000);
        x.http().request(HttpMethod.POST ,params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                CarBrandOutModel model = new Gson().fromJson(result.toString(), CarBrandOutModel.class);
                if (model.getCode() == 200) {
                    mCarBrandModelList.addAll(model.getData());
                    NiceDialog.init()
                            .setLayoutId(R.layout.dialog_car_brand_list)
                            .setConvertListener(new ViewConvertListener() {
                                @Override
                                protected void convertView(ViewHolder viewHolder, final BaseNiceDialog baseNiceDialog) {
                                    ListView lv = viewHolder.getView(R.id.dialog_car_brand_lv);
                                    final List<String> list = new ArrayList<String>();
                                    for (CarBrandOutModel.CarBrandModel carBrandModel : mCarBrandModelList) {
                                        list.add(carBrandModel.getEngName());
                                    }
                                    lv.setAdapter(new SecondCarOptionListAdapter(PublishOwnSecondCarActivity.this, list));
                                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                            mSubmitSecondCarModel.setCarBrand(list.get(i));
                                        }
                                    });
                                }
                            })
                            .setShowBottom(true)
                            .show(getSupportFragmentManager());
                } else if (model.getCode() == 10000){
                    SPUtils.put(PublishOwnSecondCarActivity.this, "UserToken", "");
                    Toast.makeText(PublishOwnSecondCarActivity.this,  String.valueOf(model.getMsg()), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(PublishOwnSecondCarActivity.this, String.valueOf(model.getMsg()), Toast.LENGTH_SHORT).show();
                }
            }
            //请求异常后的回调方法
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if (ex instanceof SocketTimeoutException) {
                    Toast.makeText(PublishOwnSecondCarActivity.this, getString(R.string.timeout), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(PublishOwnSecondCarActivity.this, getString(R.string.no_net), Toast.LENGTH_SHORT).show();
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
