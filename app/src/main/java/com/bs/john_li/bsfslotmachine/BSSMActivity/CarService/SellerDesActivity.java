package com.bs.john_li.bsfslotmachine.BSSMActivity.CarService;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bs.john_li.bsfslotmachine.BSSMActivity.BaseActivity;
import com.bs.john_li.bsfslotmachine.BSSMAdapter.CollapsingAdapter;
import com.bs.john_li.bsfslotmachine.BSSMModel.SellerDetialOutModel;
import com.bs.john_li.bsfslotmachine.BSSMView.BSSMHeadView;
import com.bs.john_li.bsfslotmachine.R;
import com.google.gson.Gson;

import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by John on 28/11/2018.
 */

public class SellerDesActivity extends BaseActivity implements View.OnClickListener{
    private BSSMHeadView headView;
    private ImageView logoIv;
    private TextView nameTv, addressTv, desTv;
    private ImageView telIv;

    // 商家详情
    private SellerDetialOutModel.SellerDetialModel mSellerDetialModel;
    private ImageOptions options = new ImageOptions.Builder().setSize(0, 0).setImageScaleType(ImageView.ScaleType.CENTER_CROP).setLoadingDrawableId(R.mipmap.second_ad).setFailureDrawableId(R.mipmap.second_ad).build();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_des);
        initView();
        setListener();
        initData();
    }

    @Override
    public void initView() {
        headView = findViewById(R.id.seller_des_head);
        logoIv = findViewById(R.id.seller_des_logo);
        nameTv = findViewById(R.id.seller_des_name_tv);
        addressTv = findViewById(R.id.seller_des_address_tv);
        desTv = findViewById(R.id.seller_des_tv);
        telIv = findViewById(R.id.seller_des_tel_iv);
    }

    @Override
    public void setListener() {
        telIv.setOnClickListener(this);
    }

    @Override
    public void initData() {
        headView.setLeft(this);
        headView.setTitle("商家详情");

        mSellerDetialModel = new Gson().fromJson(getIntent().getStringExtra("SellerDetialModel"), SellerDetialOutModel.SellerDetialModel.class);
        nameTv.setText(mSellerDetialModel.getSellerName());
        addressTv.setText(mSellerDetialModel.getAddress());
        desTv.setText(mSellerDetialModel.getSellerDes());
        x.image().bind(logoIv, mSellerDetialModel.getSellerLogo(), options);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.head_left:
                finish();
                break;
            case R.id.seller_des_tel_iv:
                if (mSellerDetialModel != null) {
                    Intent intent1 = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + mSellerDetialModel.getPhone()));
                    intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    this.startActivity(intent1);
                }
                break;
        }
    }
}
