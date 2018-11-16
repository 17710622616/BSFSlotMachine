package com.bs.john_li.bsfslotmachine.BSSMActivity.CarService;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.bs.john_li.bsfslotmachine.BSSMActivity.BaseActivity;
import com.bs.john_li.bsfslotmachine.BSSMAdapter.SecondCarOptionListAdapter;
import com.bs.john_li.bsfslotmachine.BSSMView.BSSMHeadView;
import com.bs.john_li.bsfslotmachine.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by John_Li on 15/11/2018.
 */

public class SecondHandCarListActivity extends BaseActivity implements View.OnClickListener{
    private BSSMHeadView headView;
    private TabLayout mTabLayout;
    private PopupWindow popMenu;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_hand_car_list);
        initView();
        setListener();
        initData();
    }

    @Override
    public void initView() {
        headView = findViewById(R.id.shcl_headview);

        mTabLayout = (TabLayout) findViewById(R.id.order_tabLayout);
        mTabLayout.addTab(mTabLayout.newTab().setText("車廠"));
        mTabLayout.addTab(mTabLayout.newTab().setText("車種"));
        mTabLayout.addTab(mTabLayout.newTab().setText("傳動"));
        mTabLayout.addTab(mTabLayout.newTab().setText("年份"));
        mTabLayout.addTab(mTabLayout.newTab().setText("價格"));
        mTabLayout.addTab(mTabLayout.newTab().setText("全部"));
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            headView.setHeadHight();
        }
    }

    @Override
    public void setListener() {
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Toast.makeText(SecondHandCarListActivity.this, "1", Toast.LENGTH_SHORT).show();
                switch (tab.getPosition()) {
                    case 0:
                        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View contentView = inflater.inflate(R.layout.pop_second_car_option_list, null);
                        popMenu = new PopupWindow(contentView, LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.MATCH_PARENT);
                        popMenu.setOutsideTouchable(true);
                        //popMenu.setFocusable(true);
                        popMenu.setAnimationStyle(R.style.AnimTopMiddle);
                        popMenu.setOnDismissListener(new PopupWindow.OnDismissListener() {
                            public void onDismiss() {
                                Toast.makeText(SecondHandCarListActivity.this, "關閉pop", Toast.LENGTH_SHORT).show();
                            }
                        });
                        ListView lv = contentView.findViewById(R.id.pop_second_car_option_lv);
                        SecondCarOptionListAdapter adapter = new SecondCarOptionListAdapter(SecondHandCarListActivity.this, getData());
                        lv.setAdapter(adapter);
                        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                popMenu.dismiss();
                            }
                        });
                        popMenu.showAsDropDown(mTabLayout, 0, 0);
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                    case 4:
                        break;
                    case 5:
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                Toast.makeText(SecondHandCarListActivity.this, "2", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                Toast.makeText(SecondHandCarListActivity.this, "3", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private ArrayList<String> getData() {
        ArrayList<String> list = new ArrayList<>();
        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");
        list.add("5");
        list.add("6");
        list.add("7");
        list.add("8");
        list.add("9");
        list.add("10");
        list.add("11");
        list.add("12");
        list.add("13");
        list.add("14");
        list.add("15");
        return list;
    }

    @Override
    public void initData() {
        headView.setLeft(this);
        headView.setTitle("二手車買賣");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_left:
                finish();
                break;
        }
    }
}
