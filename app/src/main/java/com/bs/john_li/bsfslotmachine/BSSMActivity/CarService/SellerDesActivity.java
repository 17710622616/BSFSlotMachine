package com.bs.john_li.bsfslotmachine.BSSMActivity.CarService;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
    private ImageOptions options = new ImageOptions.Builder().setImageScaleType(ImageView.ScaleType.CENTER_CROP).setLoadingDrawableId(R.mipmap.second_ad).setFailureDrawableId(R.mipmap.second_ad).build();
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
                    startCallPhone(mSellerDetialModel.getPhone());
                }
                break;
        }
    }

    private String phoneNumber;
    /**
     * 打电话
     *
     * @param phoneNumber
     */
    protected void startCallPhone(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        //判断Android版本是否大于23
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);

            if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 1);
                return;
            } else {
                callPhone(phoneNumber);
            }
        } else {
            callPhone(phoneNumber);
            // 检查是否获得了权限（Android6.0运行时权限）
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                // 没有获得授权，申请授权
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) this,
                        Manifest.permission.CALL_PHONE)) {
                    // 返回值：
//                          如果app之前请求过该权限,被用户拒绝, 这个方法就会返回true.
//                          如果用户之前拒绝权限的时候勾选了对话框中”Don’t ask again”的选项,那么这个方法会返回false.
//                          如果设备策略禁止应用拥有这条权限, 这个方法也返回false.
                    // 弹窗需要解释为何需要该权限，再次请求授权
                    Toast.makeText(this, "您未授權，請先授權！", Toast.LENGTH_LONG).show();

                    // 帮跳转到该应用的设置界面，让用户手动授权
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                    intent.setData(uri);
                    startActivity(intent);
                } else {
                    // 不需要解释为何需要该权限，直接请求授权
                    ActivityCompat.requestPermissions((Activity) this,
                            new String[]{Manifest.permission.CALL_PHONE}, 1);
                }
            } else {
                // 已经获得授权，可以打电话
                callPhone(phoneNumber);
            }
        }

    }

    private void callPhone(String phoneNumber) {
        // 拨号：激活系统的拨号组件 -- 直接拨打电话
        //Intent intent = new Intent(); // 意图对象：动作 + 数据
        //intent.setAction(Intent.ACTION_CALL); // 设置动作
        //Uri data = Uri.parse("tel:" + phoneNumber); // 设置数据
        //intent.setData(data);
        //startActivity(intent); // 激活Activity组件


//打开拨号界面，填充输入手机号码，让用户自主的选择
        Intent intent = new Intent(Intent.ACTION_DIAL,Uri.parse("tel:"+phoneNumber));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    // 处理权限申请的回调
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 授权成功，继续打电话
                    callPhone(this.phoneNumber);
                } else {
                    // 授权失败！
                    Toast.makeText(this, "授权失败！", Toast.LENGTH_LONG).show();
                }
                break;
            }
        }

    }
}
