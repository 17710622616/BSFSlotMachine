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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.bs.john_li.bsfslotmachine.BSSMActivity.BaseActivity;
import com.bs.john_li.bsfslotmachine.BSSMAdapter.SecondCarOptionListAdapter;
import com.bs.john_li.bsfslotmachine.BSSMView.BSSMHeadView;
import com.bs.john_li.bsfslotmachine.R;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by John_Li on 15/11/2018.
 */

public class SecondHandCarListActivity extends BaseActivity implements View.OnClickListener{
    private BSSMHeadView headView;
    private ImageView sellerIv1, sellerIv2, sellerIv3, sellerIv4, sellerIv5, sellerIv6, sellerIv7, sellerIv8;
    private ImageView hotCarIv1, hotCarIv2, hotCarIv3, hotCarIv4, hotCarIv5, hotCarIv6;
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
        sellerIv1 = findViewById(R.id.second_car_seller_iv1);
        sellerIv2 = findViewById(R.id.second_car_seller_iv2);
        sellerIv3 = findViewById(R.id.second_car_seller_iv3);
        sellerIv4 = findViewById(R.id.second_car_seller_iv4);
        sellerIv5 = findViewById(R.id.second_car_seller_iv5);
        sellerIv6 = findViewById(R.id.second_car_seller_iv6);
        sellerIv7 = findViewById(R.id.second_car_seller_iv7);
        sellerIv8 = findViewById(R.id.second_car_seller_iv8);

        hotCarIv1 = findViewById(R.id.second_car_hot_car_iv1);
        hotCarIv2 = findViewById(R.id.second_car_hot_car_iv2);
        hotCarIv3 = findViewById(R.id.second_car_hot_car_iv3);
        hotCarIv4 = findViewById(R.id.second_car_hot_car_iv4);
        hotCarIv5 = findViewById(R.id.second_car_hot_car_iv5);
        hotCarIv6 = findViewById(R.id.second_car_hot_car_iv6);

        mTabLayout = (TabLayout) findViewById(R.id.second_car_tabLayout);
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
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    private ArrayList<String> getData() {
        ArrayList<String> list = new ArrayList<>();
        list.add("1");
        list.add("2");
        list.add("3");
        return list;
    }

    @Override
    public void initData() {
        headView.setLeft(this);
        headView.setTitle("汽車買賣");

        for (int i = 0; i < mTabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = mTabLayout.getTabAt(i);
            if (tab == null) return;
            //这里使用到反射，拿到Tab对象后获取Class
            Class c = tab.getClass();
            try {
                //Filed “字段、属性”的意思,c.getDeclaredField 获取私有属性。
                //"mView"是Tab的私有属性名称(可查看TabLayout源码),类型是 TabView,TabLayout私有内部类。
                Field field = c.getDeclaredField("mView");
                //值为 true 则指示反射的对象在使用时应该取消 Java 语言访问检查。值为 false 则指示反射的对象应该实施 Java 语言访问检查。
                //如果不这样会报如下错误
                // java.lang.IllegalAccessException:
                //Class com.test.accessible.Main
                //can not access
                //a member of class com.test.accessible.AccessibleTest
                //with modifiers "private"
                field.setAccessible(true);
                final View view = (View) field.get(tab);
                if (view == null) return;
                view.setTag(i);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = (int) view.getTag();
                        //这里就可以根据业务需求处理点击事件了。
                        TabLayout.Tab tab = mTabLayout.getTabAt(position);
                        if (!tab.isSelected()) {
                            tab.select();

                            LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            View contentView = inflater.inflate(R.layout.pop_second_car_option_list, null);
                            popMenu = new PopupWindow(contentView, LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT);
                            //popMenu.setFocusable(true);
                            //popMenu.setOutsideTouchable(true);
                            //popMenu.setAnimationStyle(R.style.AnimTopMiddle);
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
                        } else {
                            int position1 = (int) v.getTag();
                            if (popMenu != null) {
                                if (popMenu.isShowing()) {
                                    popMenu.dismiss();
                                }
                            }

                            TabLayout.Tab tab1 = mTabLayout.getTabAt(position1);
                            tab1.select();
                            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            View contentView = inflater.inflate(R.layout.pop_second_car_option_list, null);
                            popMenu = new PopupWindow(contentView, LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT);
                            //popMenu.setFocusable(true);
                            //popMenu.setOutsideTouchable(true);
                            //popMenu.setAnimationStyle(R.style.AnimTopMiddle);
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
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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
