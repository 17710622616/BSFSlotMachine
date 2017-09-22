package com.bs.john_li.bsfslotmachine.BSSMActivity.Parking;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bs.john_li.bsfslotmachine.BSSMActivity.BaseActivity;
import com.bs.john_li.bsfslotmachine.BSSMUtils.BSSMConfigtor;
import com.bs.john_li.bsfslotmachine.BSSMView.BSSMHeadView;
import com.bs.john_li.bsfslotmachine.R;
import com.squareup.picasso.Picasso;

import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 停車訂單界面
 * Created by John_Li on 23/9/2017.
 */

public class ParkingOrderActivity extends BaseActivity {
    private BSSMHeadView headView;
    private ImageView parkingIv;

    private String way;
    private List<String> imgUrlList;
    private ImageOptions options = new ImageOptions.Builder().setSize(0,0).setFailureDrawableId(R.mipmap.car_sample).build();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking_order);
        initView();
        setListener();
        initData();
    }

    @Override
    public void initView() {
        headView = findViewById(R.id.parking_order_head);
        parkingIv = findViewById(R.id.parking_iv);
    }

    @Override
    public void setListener() {

    }

    @Override
    public void initData() {
        headView.setTitle("下單");
        Intent intent = getIntent();
        way = intent.getStringExtra("way");
        imgUrlList = new ArrayList<>();
        if (way.equals(BSSMConfigtor.SLOT_MACHINE_NOT_EXIST)) {
            String imgUri = intent.getStringExtra("imageUri");
            imgUrlList.add(imgUri);
            //x.image().bind(parkingIv, imgUrlList.get(0), options);
            Picasso.with(this).load(imgUrlList.get(0)).error(R.mipmap.car_sample).into(parkingIv);
        } else {

        }
    }
}
