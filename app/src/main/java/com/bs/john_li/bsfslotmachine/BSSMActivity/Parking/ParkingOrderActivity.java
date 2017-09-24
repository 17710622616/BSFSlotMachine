package com.bs.john_li.bsfslotmachine.BSSMActivity.Parking;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ImageView;

import com.bs.john_li.bsfslotmachine.BSSMActivity.BaseActivity;
import com.bs.john_li.bsfslotmachine.BSSMUtils.BSSMConfigtor;
import com.bs.john_li.bsfslotmachine.BSSMView.BSSMHeadView;
import com.bs.john_li.bsfslotmachine.R;
import com.squareup.picasso.Picasso;

import org.xutils.common.Callback;
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
    private ImageOptions options = new ImageOptions.Builder().setFailureDrawableId(R.mipmap.car_sample).build();
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
            Log.d("imgUri", imgUri);
            imgUrlList.add(imgUri);
            File f =new File("/storage/emulated/0/DCIM/20170923000720.png");
            String path = f.getAbsolutePath();
            x.image().bind(parkingIv, "/storage/emulated/0/DCIM/20170923000720.png", options, new Callback.CommonCallback<Drawable>() {
                @Override
                public void onSuccess(Drawable result) {
                    Log.d("imgUriSuccess", result.toString());
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    Log.d("imgUriError", ex.toString());
                }

                @Override
                public void onCancelled(CancelledException cex) {

                }

                @Override
                public void onFinished() {

                }
            });
            //Picasso.with(this).load("/storage/emulated/0/DCIM/20170923000720.png").error(R.mipmap.car_sample).into(parkingIv);
        } else {

        }
    }
}
