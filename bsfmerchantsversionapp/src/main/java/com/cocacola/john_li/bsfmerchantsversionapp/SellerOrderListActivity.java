package com.cocacola.john_li.bsfmerchantsversionapp;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.cocacola.john_li.bsfmerchantsversionapp.Adapter.OrderFgAdapter;
import com.cocacola.john_li.bsfmerchantsversionapp.Fragment.AllCWOrderFragment;
import com.cocacola.john_li.bsfmerchantsversionapp.Fragment.CWOrderCancelledFragment;
import com.cocacola.john_li.bsfmerchantsversionapp.Fragment.CWOrderCompletedFragment;
import com.cocacola.john_li.bsfmerchantsversionapp.Fragment.CWOrderFailureFragment;
import com.cocacola.john_li.bsfmerchantsversionapp.Fragment.CWOrderPaidFragment;
import com.cocacola.john_li.bsfmerchantsversionapp.Fragment.CWOrderPaymentFragment;
import com.cocacola.john_li.bsfmerchantsversionapp.Fragment.CWOrderRefundFragment;
import com.cocacola.john_li.bsfmerchantsversionapp.Fragment.CWOrderRefundingFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by John on 20/11/2018.
 */

public class SellerOrderListActivity extends FragmentActivity implements View.OnClickListener {
    private ImageView backIv;
    public static final String[] sTitle = new String[]{"全部", "待支付", "已支付", "已完成", "退款中", "已退款", "已失效", "已取消"};  // 支付中
    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_order_list);
        initView();
        setListener();
        initData();
    }

    public void initView() {
        backIv = findViewById(R.id.seller_order_back);
        mViewPager = (ViewPager) findViewById(R.id.order_view_pager);
        mTabLayout = (TabLayout) findViewById(R.id.order_tabLayout);
        mTabLayout.addTab(mTabLayout.newTab().setText(sTitle[0]));
        mTabLayout.addTab(mTabLayout.newTab().setText(sTitle[1]));
        mTabLayout.addTab(mTabLayout.newTab().setText(sTitle[2]));
        mTabLayout.addTab(mTabLayout.newTab().setText(sTitle[3]));
        mTabLayout.addTab(mTabLayout.newTab().setText(sTitle[4]));
        mTabLayout.addTab(mTabLayout.newTab().setText(sTitle[5]));
        mTabLayout.addTab(mTabLayout.newTab().setText(sTitle[6]));
        mTabLayout.addTab(mTabLayout.newTab().setText(sTitle[7]));
    }

    public void setListener() {
        backIv.setOnClickListener(this);
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void initData() {
        mTabLayout.addTab(mTabLayout.newTab().setText(sTitle[0]));
        mTabLayout.addTab(mTabLayout.newTab().setText(sTitle[1]));
        mTabLayout.addTab(mTabLayout.newTab().setText(sTitle[2]));
        mTabLayout.addTab(mTabLayout.newTab().setText(sTitle[3]));
        mTabLayout.addTab(mTabLayout.newTab().setText(sTitle[4]));
        mTabLayout.addTab(mTabLayout.newTab().setText(sTitle[5]));
        mTabLayout.addTab(mTabLayout.newTab().setText(sTitle[6]));
        mTabLayout.addTab(mTabLayout.newTab().setText(sTitle[7]));
        mTabLayout.setupWithViewPager(mViewPager);
        List<Fragment> fgList = new ArrayList<>();
        fgList.add(new AllCWOrderFragment());
        fgList.add(new CWOrderPaymentFragment());
        fgList.add(new CWOrderPaidFragment());
        fgList.add(new CWOrderCompletedFragment());
        fgList.add(new CWOrderRefundingFragment());
        fgList.add(new CWOrderRefundFragment());
        fgList.add(new CWOrderFailureFragment());
        fgList.add(new CWOrderCancelledFragment());

        OrderFgAdapter adapter = new OrderFgAdapter(getSupportFragmentManager(), fgList, Arrays.asList(sTitle));
        mViewPager.setOffscreenPageLimit(7);
        mViewPager.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.seller_order_back:
                finish();
                break;
        }
    }
}