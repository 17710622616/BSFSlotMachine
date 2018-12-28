package com.bs.john_li.bsfslotmachine.BSSMActivity.CarService;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.bs.john_li.bsfslotmachine.BSSMActivity.BaseActivity;
import com.bs.john_li.bsfslotmachine.BSSMActivity.LoginActivity;
import com.bs.john_li.bsfslotmachine.BSSMAdapter.SmartCarListRefreshAdapter;
import com.bs.john_li.bsfslotmachine.BSSMAdapter.SmartOwnCarListRefreshAdapter;
import com.bs.john_li.bsfslotmachine.BSSMFragment.CollectionCarListFragment;
import com.bs.john_li.bsfslotmachine.BSSMFragment.OwnCarListFragment;
import com.bs.john_li.bsfslotmachine.BSSMModel.CarModel;
import com.bs.john_li.bsfslotmachine.BSSMModel.OwnCarListOutModel;
import com.bs.john_li.bsfslotmachine.BSSMUtils.AliyunOSSUtils;
import com.bs.john_li.bsfslotmachine.BSSMUtils.BSSMCommonUtils;
import com.bs.john_li.bsfslotmachine.BSSMUtils.BSSMConfigtor;
import com.bs.john_li.bsfslotmachine.BSSMUtils.SPUtils;
import com.bs.john_li.bsfslotmachine.BSSMView.BSSMHeadView;
import com.bs.john_li.bsfslotmachine.R;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.reflect.InvocationTargetException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

/**
 * 我的二手車列表
 * Created by John_Li on 22/11/2018.
 */

public class OwnCarListActivity extends BaseActivity implements View.OnClickListener{
    private BSSMHeadView headView;
    private RadioGroup mRg;
    private RadioButton mMyCarRb, mCollectionRb;

    private FragmentManager fm;
    private Fragment cacheFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_own_car_list);
        initView();
        setListener();
        initData();
    }

    @Override
    public void initView() {
        headView = findViewById(R.id.own_car_list_head);
        mRg = findViewById(R.id.own_carlist_rg);
        mMyCarRb = findViewById(R.id.own_carlist_my_car_rb);
        mCollectionRb = findViewById(R.id.own_carlist_collection_rb);


        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            headView.setHeadHight();
        }
    }

    @Override
    public void setListener() {
        mRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if (checkedId == R.id.own_carlist_my_car_rb) {
                    mMyCarRb.setTextColor(getResources().getColor(R.color.colorSkyBlue));
                    mCollectionRb.setTextColor(getResources().getColor(R.color.colorDrakGray));
                    switchPages(OwnCarListFragment.class,OwnCarListFragment.TAG);
                } else if (checkedId == R.id.own_carlist_collection_rb) {
                    mMyCarRb.setTextColor(getResources().getColor(R.color.colorDrakGray));
                    mCollectionRb.setTextColor(getResources().getColor(R.color.colorSkyBlue));
                    switchPages(CollectionCarListFragment.class,CollectionCarListFragment.TAG);
                }
            }
        });
    }

    @Override
    public void initData() {
        headView.setLeft(this);
        headView.setTitle("發佈車輛列表");
        headView.setRightText("發車", this);


        fm = getSupportFragmentManager();
        FragmentTransaction traslation = fm.beginTransaction();
        cacheFragment = new OwnCarListFragment();
        traslation.add(R.id.own_carlist_cotainor,cacheFragment,OwnCarListFragment.TAG);
        traslation.commit();
        mMyCarRb.setChecked(true);
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
            transaction.add(R.id.own_carlist_cotainor, cacheFragment, tag);
        }
        transaction.commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_left:
                finish();
                break;
            case R.id.head_right_tv:
                startActivityForResult(new Intent(OwnCarListActivity.this, PublishOwnSecondCarActivity.class), 1);
                break;
        }
    }
}
