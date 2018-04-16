package com.bs.john_li.bsfslotmachine.BSSMActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.bs.john_li.bsfslotmachine.BSSMFragment.ForumFragment;
import com.bs.john_li.bsfslotmachine.BSSMFragment.MineFragment;
import com.bs.john_li.bsfslotmachine.BSSMFragment.ParkingFragment;
import com.bs.john_li.bsfslotmachine.BSSMUtils.BSSMCommonUtils;
import com.bs.john_li.bsfslotmachine.BSSMUtils.SPUtils;
import com.bs.john_li.bsfslotmachine.BSSMUtils.StatusBarUtil;
import com.bs.john_li.bsfslotmachine.R;

import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;

/**
 * Created by John_Li on 27/7/2017.
 */

public class MainActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {
    private RadioButton park_rb,forum_rb,mine_rb;
    private RadioGroup bottom_group;
    private FragmentManager fm;
    private Fragment cacheFragment;
    private ImageView iv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //StatusBarUtil.setColor(this,getResources().getColor(R.color.colorPrimary),0);
        // 判斷是否登錄
        isLoginNow();

        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }*/
        initView();
        setListener();
        initData();
    }

    @Override
    public void initView() {
        bottom_group = (RadioGroup)findViewById(R.id.bottom_main_group);
        park_rb = (RadioButton) findViewById(R.id.bottom_main_parking);
        forum_rb = (RadioButton) findViewById(R.id.bottom_main_forum);
        mine_rb = (RadioButton) findViewById(R.id.bottom_main_mine);

        //iv = findViewById(R.id.order_icon);
    }

    @Override
    public void setListener() {
        bottom_group.setOnCheckedChangeListener(this);
    }

    @Override
    public void initData() {
        fm = getSupportFragmentManager();
        FragmentTransaction traslation = fm.beginTransaction();
        cacheFragment = new ParkingFragment();
        traslation.add(R.id.main_containor,cacheFragment,ParkingFragment.TAG);
        traslation.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        /*String orderStartTimeStamp = String.valueOf(SPUtils.get(this, "orderStartTimeStamp", ""));
        String orderTimeStamp = String.valueOf(SPUtils.get(this, "orderTimeStamp", ""));
        if (!orderTimeStamp.equals("")) {   // 判斷停車訂單的時間戳是否存在
            SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            long between = 0;
            try {
                // 訂單開始時間
                java.util.Date begin=dfs.parse(BSSMCommonUtils.stampToDate(orderStartTimeStamp));
                // 訂單時長
                java.util.Date timeLen =dfs.parse(BSSMCommonUtils.stampToDate(orderTimeStamp));
                // 現在時間
                java.util.Date end = dfs.parse(BSSMCommonUtils.getTimeNoW());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }*/
    }

    /**
     * 判斷是否登錄
     */
    private void isLoginNow() {
        String userToken = (String) SPUtils.get(this, "UserToken", "");
        if (userToken == null) {
            if (userToken.equals("")){
                Toast.makeText(this, getString(R.string.not_login), Toast.LENGTH_SHORT).show();
            }
            Toast.makeText(this,  getString(R.string.not_login), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
        switch (i){
            case R.id.bottom_main_parking:
                park_rb.setTextColor(getResources().getColor(R.color.colorSkyBlue));
                forum_rb.setTextColor(getResources().getColor(R.color.colorDrakGray));
                mine_rb.setTextColor(getResources().getColor(R.color.colorDrakGray));
                switchPages(ParkingFragment.class,ParkingFragment.TAG);
                break;
            case R.id.bottom_main_forum:
                park_rb.setTextColor(getResources().getColor(R.color.colorDrakGray));
                forum_rb.setTextColor(getResources().getColor(R.color.colorSkyBlue));
                mine_rb.setTextColor(getResources().getColor(R.color.colorDrakGray));
                switchPages(ForumFragment.class,ForumFragment.TAG);
                break;
            case R.id.bottom_main_mine:
                park_rb.setTextColor(getResources().getColor(R.color.colorDrakGray));
                forum_rb.setTextColor(getResources().getColor(R.color.colorDrakGray));
                mine_rb.setTextColor(getResources().getColor(R.color.colorSkyBlue));
                switchPages(MineFragment.class,MineFragment.TAG);
                break;
        }
    }

    private void switchPages(Class<?> cls, String tag){
        FragmentTransaction transaction = fm.beginTransaction();
        if (cacheFragment != null){
            transaction.hide(cacheFragment);
        }
        cacheFragment = fm.findFragmentByTag(tag);
        if (cacheFragment != null){
            transaction.show(cacheFragment);
        } else {
            try{
               cacheFragment = (Fragment) cls.getConstructor().newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (Exception e){
                e.printStackTrace();
            }
            transaction.add(R.id.main_containor, cacheFragment, tag);
        }
        transaction.commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
