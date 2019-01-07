package com.bs.john_li.bsfslotmachine.BSSMActivity.CarService;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.sdk.android.oss.OSSClient;
import com.bs.john_li.bsfslotmachine.BSSMActivity.BaseActivity;
import com.bs.john_li.bsfslotmachine.BSSMActivity.LoginActivity;
import com.bs.john_li.bsfslotmachine.BSSMAdapter.CollapsingAdapter;
import com.bs.john_li.bsfslotmachine.BSSMModel.ArticalLikeOutModel;
import com.bs.john_li.bsfslotmachine.BSSMModel.CommonBOModel;
import com.bs.john_li.bsfslotmachine.BSSMModel.CommonJieModel;
import com.bs.john_li.bsfslotmachine.BSSMModel.ContentsListModel;
import com.bs.john_li.bsfslotmachine.BSSMModel.HotCarOutModel;
import com.bs.john_li.bsfslotmachine.BSSMModel.SecondCarDetialOutModel;
import com.bs.john_li.bsfslotmachine.BSSMModel.SecondCarOutModel;
import com.bs.john_li.bsfslotmachine.BSSMUtils.AliyunOSSUtils;
import com.bs.john_li.bsfslotmachine.BSSMUtils.BSSMCommonUtils;
import com.bs.john_li.bsfslotmachine.BSSMUtils.BSSMConfigtor;
import com.bs.john_li.bsfslotmachine.BSSMUtils.SPUtils;
import com.bs.john_li.bsfslotmachine.BSSMUtils.StatusBarUtil;
import com.bs.john_li.bsfslotmachine.BSSMView.CustomExpandableListView;
import com.bs.john_li.bsfslotmachine.BSSMView.FloatingTestButton;
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
import java.util.Arrays;
import java.util.List;

/**
 * Created by John on 25/11/2018.
 */

public class SecondCarDetailActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView nameTv, priceTv, pageViewTv, publishTimeTv, seriesTv, noTv, typeTv, colorTv, gearsTv, firstTimeTv, idTv;
    private TextView periodValidityTv, brandTv, styleTv, mileageTv, exhaustTv, dscriptionTv, remarkTv, telTv;
    private AppBarLayout appbar;
    private Toolbar articalToolbar;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private ViewPager mViewPager;
    private CollapsingAdapter mCollapsingAdapter;
    private NestedScrollView mNestedScrollView;
    private CustomExpandableListView contentsLv;
    private CheckBox collectionCb;

    private String id;
    private List<ImageView> imgList;
    private SecondCarDetialOutModel.SecondCarDetialModel mSecondCarDetialModel;
    //private ImageView mSubmitDialogIv;
    // 是否是第一次初始化點讚狀態
    private boolean isInitIsLikeStatus = false;
    private OSSClient oss;
    private ImageOptions options = new ImageOptions.Builder().setSize(0, 0).setImageScaleType(ImageView.ScaleType.CENTER_CROP).setLoadingDrawableId(R.mipmap.img_loading_list).setFailureDrawableId(R.mipmap.load_img_fail_list).build();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setColor(this, getResources().getColor(R.color.colorAlpha));
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        setContentView(R.layout.activity_second_car_detial);
        initView();
        setListener();
        initData();
    }

    public void initView() {
        appbar = (AppBarLayout) findViewById(R.id.second_car_detial_appbar);
        articalToolbar = (Toolbar) findViewById(R.id.second_car_detial_toolbar);
        mNestedScrollView = (NestedScrollView) findViewById(R.id.collapsing_car_detial_sv);
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.second_car_detial_collapsing_toolbar);
        mViewPager = (ViewPager) findViewById(R.id.second_car_detial_vp);
        nameTv = (TextView) findViewById(R.id.second_car_detial_name_tv);
        priceTv = (TextView) findViewById(R.id.second_car_detial_price_tv);
        pageViewTv = (TextView) findViewById(R.id.second_car_detial_page_view_tv);
        publishTimeTv = (TextView) findViewById(R.id.second_car_detial_publish_time_tv);
        seriesTv = (TextView) findViewById(R.id.second_car_detial_car_series_tv);
        noTv = (TextView) findViewById(R.id.second_car_detial_car_no_tv);
        typeTv = (TextView) findViewById(R.id.second_car_detial_type_tv);
        colorTv = (TextView) findViewById(R.id.second_car_detial_color_tv);
        gearsTv = (TextView) findViewById(R.id.second_car_detial_gears_tv);
        firstTimeTv = (TextView) findViewById(R.id.second_car_detial_first_time_tv);
        idTv = (TextView) findViewById(R.id.second_car_detial_id_tv);
        periodValidityTv = (TextView) findViewById(R.id.second_car_detial_period_validity_tv);
        brandTv = (TextView) findViewById(R.id.second_car_detial_brand_tv);
        styleTv = (TextView) findViewById(R.id.second_car_detial_car_style_tv);
        mileageTv = (TextView) findViewById(R.id.second_car_detial_driver_mileage_tv);
        exhaustTv = (TextView) findViewById(R.id.second_car_detial_exhaust_tv);
        dscriptionTv = (TextView) findViewById(R.id.second_car_detial_dscription_tv);
        remarkTv = (TextView) findViewById(R.id.second_car_detial_remark_tv);
        telTv = (TextView) findViewById(R.id.second_car_detial_tel_tv);
        collectionCb = (CheckBox) findViewById(R.id.second_car_detial_collection_cb);
        // 默認取消觸發事件，等待獲取完收藏狀態再開啟
        collectionCb.setEnabled(false);
        collectionCb.setFocusable(true);
        collectionCb.setFocusableInTouchMode(true);
        collectionCb.requestFocus();
    }

    public void setListener() {
        telTv.setOnClickListener(this);

        collectionCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isInitIsLikeStatus) {   // 當是第一次點讚時取消點讚操作
                    isInitIsLikeStatus = false;
                } else {    // 當不是第一次點讚操作時判定為點讚或取消點讚
                    if (!String.valueOf(SPUtils.get(SecondCarDetailActivity.this, "UserToken", "")).equals("null") && !String.valueOf(SPUtils.get(SecondCarDetailActivity.this, "UserToken", "")).equals("")) {
                        callNetSubmitLike(isChecked);
                    } else {
                        Toast.makeText(SecondCarDetailActivity.this, R.string.not_login, Toast.LENGTH_LONG);
                        startActivity(new Intent(SecondCarDetailActivity.this, LoginActivity.class));
                        finish();
                    }
                }
            }
        });
    }

    public void initData() {
        Intent intent = getIntent();
        id = intent.getStringExtra("seoncdeCarId");
        oss = AliyunOSSUtils.initOSS(this);

        // toolbar
        setSupportActionBar(articalToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // 頭部的圖片列表
        imgList = new ArrayList<>();

        // 獲取車輛詳情
        callNetGetCarDetial();
    }

    /**
     * 獲取車輛詳情
     */
    private void callNetGetCarDetial() {
        RequestParams params = new RequestParams(BSSMConfigtor.BASE_URL + BSSMConfigtor.GET_CAR_DETIAL);
        params.setAsJsonContent(true);
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("id", id);
            jsonObj.put("token", SPUtils.get(this, "UserToken", ""));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String urlJson = jsonObj.toString();
        params.setBodyContent(urlJson);
        String uri = params.getUri();
        params.setConnectTimeout(30 * 1000);
        x.http().request(HttpMethod.POST, params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                SecondCarDetialOutModel model = new Gson().fromJson(result.toString(), SecondCarDetialOutModel.class);
                if (model.getCode() == 200) {
                    mSecondCarDetialModel = model.getData();
                    refreshUI();
                } else if (model.getCode() == 10000) {
                    SPUtils.put(SecondCarDetailActivity.this, "UserToken", "");
                    Toast.makeText(SecondCarDetailActivity.this, String.valueOf(model.getMsg()), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SecondCarDetailActivity.this, String.valueOf(model.getMsg()), Toast.LENGTH_SHORT).show();
                }
            }

            //请求异常后的回调方法
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if (ex instanceof SocketTimeoutException) {
                    Toast.makeText(SecondCarDetailActivity.this, getString(R.string.timeout), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SecondCarDetailActivity.this, getString(R.string.no_net), Toast.LENGTH_SHORT).show();
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

    private void refreshUI() {
        nameTv.setText(mSecondCarDetialModel.getCarBrand() + " " + mSecondCarDetialModel.getCarSeries());
        priceTv.setText(String.valueOf(mSecondCarDetialModel.getCarPrices()) + "萬");
        pageViewTv.setText("瀏覽量：" + String.valueOf(mSecondCarDetialModel.getPageView()));
        publishTimeTv.setText("發佈日期：" + BSSMCommonUtils.stampToDateTime(String.valueOf(mSecondCarDetialModel.getFirstRegisterationTime())));
        seriesTv.setText("車        系：" + String.valueOf(mSecondCarDetialModel.getCarSeries()));
        noTv.setText("車        牌：" + String.valueOf(mSecondCarDetialModel.getCarNo()));
        typeTv.setText("汽車類型：" + String.valueOf(mSecondCarDetialModel.getType()));
        colorTv.setText("顏        色：" + String.valueOf(mSecondCarDetialModel.getCarColor()));
        if (mSecondCarDetialModel.getCarGears() == 0) {
            gearsTv.setText("排        擋：手動擋");
        } else {
            gearsTv.setText("排        擋：自動擋");
        }
        firstTimeTv.setText("落地時間：" + BSSMCommonUtils.stampToDateTime(String.valueOf(mSecondCarDetialModel.getFirstRegisterationTime())));
        idTv.setText("編        號：" + String.valueOf(mSecondCarDetialModel.getId()));
        periodValidityTv.setText("有  效  期：" + BSSMCommonUtils.stampToDateTime(String.valueOf(mSecondCarDetialModel.getPeriodValidity())));
        brandTv.setText("品        牌：" + String.valueOf(mSecondCarDetialModel.getCarBrand()));
        styleTv.setText("車        型：" + String.valueOf(mSecondCarDetialModel.getCarSeries()));
        mileageTv.setText("行         程：" + String.valueOf(mSecondCarDetialModel.getDriverMileage()) + "萬公里");
        exhaustTv.setText("排         量：" + String.valueOf(mSecondCarDetialModel.getCarSeries()) + "C.C.");
        dscriptionTv.setText("汽車狀況：" + String.valueOf(mSecondCarDetialModel.getCarDescription()));
        remarkTv.setText(String.valueOf(mSecondCarDetialModel.getConfigInfo()) + "," + String.valueOf(mSecondCarDetialModel.getStateOfRepiar()) + "," + String.valueOf(mSecondCarDetialModel.getInsideBody()) + "," + String.valueOf(mSecondCarDetialModel.getTestConclusion()));
        //  設置標題
        mCollapsingToolbarLayout.setTitle(mSecondCarDetialModel.getCarBrand());
        if (mSecondCarDetialModel.getIfCollection() == 1) {
            isInitIsLikeStatus = true;
            collectionCb.setChecked(true);
        } else {
            collectionCb.setChecked(false);
        }
        collectionCb.setEnabled(true);



        // 如果有圖片則顯示，無則不設置
        if (mSecondCarDetialModel.getCarImg() != null) {
            if (!mSecondCarDetialModel.getCarImg().equals("")) {
                try {
                    //Map<String, String> coverMap = new Gson().fromJson(mContentsModel.getCover(), HashMap.class);
                    List<String> result = Arrays.asList(mSecondCarDetialModel.getCarImg().split(","));
                    for (String cover : result) {
                        ImageView iv = new ImageView(this);
                        iv.setBackgroundColor(getResources().getColor(R.color.colorMineGray));
                        iv.setImageResource(R.mipmap.img_loading_list);
                        //AliyunOSSUtils.downloadImg(entry.getValue(), oss, iv, this, R.mipmap.load_img_fail);
                        x.image().bind(iv, cover, options);
                        imgList.add(iv);
                    }

                    mCollapsingAdapter = new CollapsingAdapter(imgList);
                    mViewPager.setAdapter(mCollapsingAdapter);
                } catch (Exception e) {
                    ViewGroup.LayoutParams params = mViewPager.getLayoutParams();
                    params.height = 270;
                    mViewPager.setLayoutParams(params);
                    appbar.setExpanded(false);
                    articalToolbar.setCollapsible(false);
                }
            } else {
                ViewGroup.LayoutParams params = mViewPager.getLayoutParams();
                params.height = 270;
                mViewPager.setLayoutParams(params);
                appbar.setExpanded(false);
                articalToolbar.setCollapsible(false);
            }
        } else {
            ViewGroup.LayoutParams params = mViewPager.getLayoutParams();
            params.height = 270;
            mViewPager.setLayoutParams(params);
            appbar.setExpanded(false);
            articalToolbar.setCollapsible(false);
        }
    }


    /**
     * 点赞及取消点赞
     * @param isChecked
     */
    private void callNetSubmitLike(final boolean isChecked) {
        RequestParams params = null;
        if (isChecked) {
            params = new RequestParams(BSSMConfigtor.BASE_URL + BSSMConfigtor.COLLECTION_CAR + SPUtils.get(this, "UserToken", ""));
        } else {
            params = new RequestParams(BSSMConfigtor.BASE_URL + BSSMConfigtor.UNCOLLECTION_CAR + SPUtils.get(this, "UserToken", ""));
        }

        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("sellerCarId", id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String urlJson = jsonObj.toString();
        params.setBodyContent(urlJson);
        params.setAsJsonContent(true);
        params.setConnectTimeout(30 * 1000);
        x.http().request(HttpMethod.POST, params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                CommonBOModel model = new Gson().fromJson(result.toString(), CommonBOModel.class);
                if (model.getCode() == 200) {
                    if (isChecked) {
                        if (model.isData()) {
                            Toast.makeText(SecondCarDetailActivity.this, "收藏成功", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(SecondCarDetailActivity.this, "收藏失敗", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        if (model.isData()) {
                            Toast.makeText(SecondCarDetailActivity.this, "取消收藏成功", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(SecondCarDetailActivity.this, "取消收藏失敗", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    if (isChecked) {
                        Toast.makeText(SecondCarDetailActivity.this, "收藏失敗," + String.valueOf(model.getMsg()), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(SecondCarDetailActivity.this, "取消收藏失敗," + String.valueOf(model.getMsg()), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if (ex instanceof SocketTimeoutException) {
                    if (isChecked) {
                        Toast.makeText(SecondCarDetailActivity.this, "收藏超時，請重試！", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(SecondCarDetailActivity.this, "取消收藏超時，請重試！", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if (isChecked) {
                        Toast.makeText(SecondCarDetailActivity.this, "收藏失敗╮(╯▽╰)╭", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(SecondCarDetailActivity.this, "取消收藏失敗╮(╯▽╰)╭", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;

        }
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.second_car_detial_tel_tv:
                if (mSecondCarDetialModel != null) {
                    startCallPhone(mSecondCarDetialModel.getTel());
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
