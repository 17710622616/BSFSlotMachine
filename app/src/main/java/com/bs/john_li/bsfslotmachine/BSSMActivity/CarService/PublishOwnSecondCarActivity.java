package com.bs.john_li.bsfslotmachine.BSSMActivity.CarService;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bs.john_li.bsfslotmachine.BSSMActivity.BaseActivity;
import com.bs.john_li.bsfslotmachine.R;

/**
 * Created by John_Li on 22/11/2018.
 */

public class PublishOwnSecondCarActivity extends BaseActivity implements View.OnClickListener{
    private LinearLayout brandLL, seriesLL, styleLL,firstTimeLL,miealgeLL,gearsLL,priceLL,telLL,desLL,colorLL,modelLL,exhaustLL,countryLL,deliveryTimeLL,configInfoLL,repiarStatrLL,insideBodyLL,testConclusionLL;
    private TextView brandTv,seriesTv,styleTv,firstTimeLLTv,miealgeTv,gearsTv,priceTv,telTv,desTv,colorTv,modelTv,exhaustTv,countryTv,deliveryTimeTv,configInfoTv,repiarStatrTv,insideBodyTv,testConclusionTv;
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

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.publish_own_second_car_brand_ll:
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
}
