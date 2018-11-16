package com.bs.john_li.bsfslotmachine.BSSMActivity.CarService;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.bs.john_li.bsfslotmachine.BSSMActivity.BaseActivity;
import com.bs.john_li.bsfslotmachine.BSSMAdapter.OrderFgAdapter;
import com.bs.john_li.bsfslotmachine.BSSMFragment.BSSMCarWashFragment.AllCWOrderFragment;
import com.bs.john_li.bsfslotmachine.BSSMFragment.BSSMCarWashFragment.CWOrderCancelledFragment;
import com.bs.john_li.bsfslotmachine.BSSMFragment.BSSMCarWashFragment.CWOrderCompletedFragment;
import com.bs.john_li.bsfslotmachine.BSSMFragment.BSSMCarWashFragment.CWOrderFailureFragment;
import com.bs.john_li.bsfslotmachine.BSSMFragment.BSSMCarWashFragment.CWOrderPaidFragment;
import com.bs.john_li.bsfslotmachine.BSSMFragment.BSSMCarWashFragment.CWOrderPaymentFragment;
import com.bs.john_li.bsfslotmachine.BSSMFragment.BSSMCarWashFragment.CWOrderRefundFragment;
import com.bs.john_li.bsfslotmachine.BSSMFragment.BSSMCarWashFragment.CWOrderRefundingFragment;
import com.bs.john_li.bsfslotmachine.BSSMView.BSSMHeadView;
import com.bs.john_li.bsfslotmachine.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by John_Li on 16/11/2018.
 */

public class CarWashOrderListActivity extends BaseActivity implements View.OnClickListener{
    private BSSMHeadView historyHead;
    public static final String [] sTitle = new String[]{"全部","待支付","已支付", "已完成", "退款中", "已退款", "已失效", "已取消"};  // 支付中
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_wash_order_list);
        initView();
        setListener();
        initData();
    }

    @Override
    public void initView() {
        historyHead = findViewById(R.id.history_head);
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
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            historyHead.setHeadHight();
        }
    }

    @Override
    public void setListener() {
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

    @Override
    public void initData() {
        historyHead.setTitle("洗車訂單列表");
        historyHead.setLeft(this);

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

        OrderFgAdapter adapter = new OrderFgAdapter(getSupportFragmentManager(),fgList, Arrays.asList(sTitle));
        mViewPager.setOffscreenPageLimit(7);
        mViewPager.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.head_left:
                finish();
                break;
        }
    }
}
