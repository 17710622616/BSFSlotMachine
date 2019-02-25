package com.bs.john_li.bsfslotmachine.BSSMActivity.CarService;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bs.john_li.bsfslotmachine.BSSMActivity.BaseActivity;
import com.bs.john_li.bsfslotmachine.BSSMActivity.LoginActivity;
import com.bs.john_li.bsfslotmachine.BSSMAdapter.SecondCarOptionListAdapter;
import com.bs.john_li.bsfslotmachine.BSSMAdapter.SmartSecondCarListRefreshAdapter;
import com.bs.john_li.bsfslotmachine.BSSMModel.CarBrandOutModel;
import com.bs.john_li.bsfslotmachine.BSSMModel.RequestSecondCarModel;
import com.bs.john_li.bsfslotmachine.BSSMModel.SecondCarOutModel;
import com.bs.john_li.bsfslotmachine.BSSMModel.SellerDetialOutModel;
import com.bs.john_li.bsfslotmachine.BSSMUtils.AliyunOSSUtils;
import com.bs.john_li.bsfslotmachine.BSSMUtils.BSSMCommonUtils;
import com.bs.john_li.bsfslotmachine.BSSMUtils.BSSMConfigtor;
import com.bs.john_li.bsfslotmachine.BSSMUtils.SPUtils;
import com.bs.john_li.bsfslotmachine.BSSMView.BSSMHeadView;
import com.bs.john_li.bsfslotmachine.BSSMView.FullyLinearLayoutManager;
import com.bs.john_li.bsfslotmachine.R;
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarFinalValueListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.lang.reflect.Field;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by John on 28/11/2018.
 */

public class SellerDetialActivity extends BaseActivity implements View.OnClickListener{
    private BSSMHeadView headView;
    private TabLayout mTabLayout;
    private PopupWindow popMenu;
    private RefreshLayout mRefreshLayout;
    private RecyclerView mRecycleView;
    private ImageView sellerIv, telIv;
    private TextView nameTv, distanceTv, addressTv;
    private LinearLayout sellerDetialLL;

    // 商家ID
    private String sellerId;
    // 每頁加載數量
    private int pageSize = 5;
    // 頁數
    private int pageNo = 1;
    // 車輛總數
    private long totolCarCount;
    // 汽车品牌列表
    private List<CarBrandOutModel.CarBrandModel> mCarBrandModelList;
    // 二手車列表
    private List<SecondCarOutModel.DataBeanX.SecondCarModel> mSecondCarList;
    // 商家详情
    private SellerDetialOutModel.SellerDetialModel mSellerDetialModel;
    // 請求列表的類
    private RequestSecondCarModel mRequestSecondCarModel;
    //定位都要通过LocationManager这个类实现
    private LocationManager locationManager;
    private Location mLocation;
    private String provider;
    //
    private SmartSecondCarListRefreshAdapter mSmartSecondCarListRefreshAdapter;
    //private ImageOptions options = new ImageOptions.Builder().setSize(0, 0).setImageScaleType(ImageView.ScaleType.CENTER_CROP).setLoadingDrawableId(R.mipmap.second_ad).setFailureDrawableId(R.mipmap.second_ad).build();
    private ImageOptions options1 = new ImageOptions.Builder().setImageScaleType(ImageView.ScaleType.CENTER_CROP).setImageScaleType(ImageView.ScaleType.FIT_XY).setLoadingDrawableId(R.mipmap.second_ad).setFailureDrawableId(R.mipmap.second_ad).build();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_detial);
        getLocation();
        initView();
        setListener();
        initData();
    }

    private void getLocation() {
        //获取定位服务
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //获取当前可用的位置控制器
        List<String> list = locationManager.getProviders(true);

        if (list.contains(LocationManager.GPS_PROVIDER)) {
            //是否为GPS位置控制器
            provider = LocationManager.GPS_PROVIDER;
        } else if (list.contains(LocationManager.NETWORK_PROVIDER)) {
            //是否为网络位置控制器
            provider = LocationManager.NETWORK_PROVIDER;

        } else {
            Toast.makeText(this, "请检查网络或GPS是否打开",
                    Toast.LENGTH_LONG).show();
            return;
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLocation = locationManager.getLastKnownLocation(provider);
    }

    @Override
    public void initView() {
        headView = findViewById(R.id.seller_detial_head);
        sellerIv = findViewById(R.id.seller_iv);
        telIv = findViewById(R.id.merchart_tel_ll);
        nameTv = findViewById(R.id.seller_name_tv);
        distanceTv = findViewById(R.id.seller_distance_tv);
        addressTv = findViewById(R.id.seller_address_tv);
        sellerDetialLL = findViewById(R.id.seller_detial_ll);

        mTabLayout = (TabLayout) findViewById(R.id.seller_tabLayout);
        mTabLayout.addTab(mTabLayout.newTab().setText("車廠"));
        mTabLayout.addTab(mTabLayout.newTab().setText("車種"));
        mTabLayout.addTab(mTabLayout.newTab().setText("傳動"));
        mTabLayout.addTab(mTabLayout.newTab().setText("年份"));
        mTabLayout.addTab(mTabLayout.newTab().setText("價格"));
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            headView.setHeadHight();
        }

        mRecycleView = findViewById(R.id.seller_recycle_lv);
        mRefreshLayout = findViewById(R.id.seller_srl);
        mRefreshLayout.setEnableAutoLoadmore(false);//是否启用列表惯性滑动到底部时自动加载更多
        mRefreshLayout.setDisableContentWhenRefresh(true);//是否在刷新的时候禁止列表的操作
        mRefreshLayout.setDisableContentWhenLoading(true);//是否在加载的时候禁止列表的操作
        // 设置header的高度
        mRefreshLayout.setHeaderHeightPx((int)(BSSMCommonUtils.getDeviceWitdh(this) / 4.05));//Header标准高度（显示下拉高度>=标准高度 触发刷新）
    }

    @Override
    public void setListener() {
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                mSecondCarList.clear();
                pageNo = 1;
                if (BSSMCommonUtils.isLoginNow(SellerDetialActivity.this)) {
                    callNetGetSellerCarList();
                } else {
                    startActivityForResult(new Intent(SellerDetialActivity.this, LoginActivity.class), BSSMConfigtor.LOGIN_FOR_RQUEST);
                }
            }
        });
        mRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                //和最大的数据比较
                if (pageSize * (pageNo) > totolCarCount){
                    Toast.makeText(SellerDetialActivity.this, "沒有更多數據了誒~", Toast.LENGTH_SHORT).show();
                    mRefreshLayout.finishRefresh();
                    mRefreshLayout.finishLoadmore();
                } else {
                    pageNo ++;
                    if (BSSMCommonUtils.isLoginNow(SellerDetialActivity.this)) {
                        callNetGetSellerCarList();
                    } else {
                        startActivityForResult(new Intent(SellerDetialActivity.this, LoginActivity.class), BSSMConfigtor.LOGIN_FOR_RQUEST);
                    }
                }
            }
        });
        sellerDetialLL.setOnClickListener(this);
    }

    @Override
    public void initData() {
        sellerId = getIntent().getStringExtra("sellerId");
        headView.setLeft(this);
        headView.setTitle("商家詳情");

        mRequestSecondCarModel = new RequestSecondCarModel();
        mRequestSecondCarModel.setCarGears(-1);
        mRequestSecondCarModel.setCarBrand("全部");
        mRequestSecondCarModel.setCarType(-1);
        mRequestSecondCarModel.setStartPrice(-1);
        mRequestSecondCarModel.setEndPrice(-1);
        mRequestSecondCarModel.setSellerId(Integer.parseInt(sellerId));
        mCarBrandModelList = new ArrayList<>();
        mSecondCarList = new ArrayList<>();
        mSmartSecondCarListRefreshAdapter = new SmartSecondCarListRefreshAdapter(this, mSecondCarList, AliyunOSSUtils.initOSS(this));
        FullyLinearLayoutManager linearLayoutManager = new FullyLinearLayoutManager(this);
        mRecycleView.setNestedScrollingEnabled(false);
        //设置布局管理器
        mRecycleView.setLayoutManager(linearLayoutManager);
        mRecycleView.setAdapter(mSmartSecondCarListRefreshAdapter);
        mSmartSecondCarListRefreshAdapter.setOnItemClickListenr(new SmartSecondCarListRefreshAdapter.OnItemClickListener() {
            @Override
            public void onItemLongClick(View view, int position) {
                Intent intent = new Intent(SellerDetialActivity.this, SecondCarDetailActivity.class);
                intent.putExtra("seoncdeCarId", String.valueOf(mSecondCarList.get(position).getId()));
                startActivity(intent);
            }
        });

        // 获取商家详情
        callNetGetSellerDetial();

        // 初始化菜单列表
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
                        }

                        switch (position) {
                            case 0:
                                if (mCarBrandModelList.size() > 0) {
                                    LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                    View contentView = inflater.inflate(R.layout.pop_second_car_option_list, null);
                                    popMenu = new PopupWindow(contentView, LinearLayout.LayoutParams.MATCH_PARENT,
                                            LinearLayout.LayoutParams.WRAP_CONTENT);
                                    popMenu.setFocusable(true);
                                    popMenu.setOutsideTouchable(false);
                                    //popMenu.setAnimationStyle(R.style.AnimTopMiddle);
                                    popMenu.setOnDismissListener(new PopupWindow.OnDismissListener() {
                                        public void onDismiss() {
                                            pageNo = 1;
                                        }
                                    });
                                    ListView lv = contentView.findViewById(R.id.pop_second_car_option_lv);
                                    SecondCarOptionListAdapter adapter = new SecondCarOptionListAdapter(SellerDetialActivity.this, getData());
                                    lv.setAdapter(adapter);
                                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                            if (position == 0) {
                                                mRequestSecondCarModel.setCarBrand(null);
                                            } else {
                                                mRequestSecondCarModel.setCarBrand(mCarBrandModelList.get(position).getName());
                                            }
                                            mRefreshLayout.autoRefresh();
                                            popMenu.dismiss();
                                        }
                                    });
                                    //popMenu.showAsDropDown(mTabLayout, 0, 0);

                                    View windowContentViewRoot = contentView;
                                    int windowPos[] = BSSMCommonUtils.calculatePopWindowPos(view, windowContentViewRoot);
                                    int xOff = 20;// 可以自己调整偏移
                                    windowPos[0] -= xOff;
                                    popMenu.showAtLocation(view, Gravity.TOP | Gravity.START, windowPos[0], windowPos[1]);
                                } else {
                                    callNetGetCarBrand();
                                }
                                break;
                            case 1:
                                LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                View contentView0 = inflater.inflate(R.layout.pop_second_car_option_list, null);
                                popMenu = new PopupWindow(contentView0, LinearLayout.LayoutParams.MATCH_PARENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT);
                                popMenu.setFocusable(true);
                                popMenu.setOutsideTouchable(false);
                                //popMenu.setAnimationStyle(R.style.AnimTopMiddle);
                                ListView lv = contentView0.findViewById(R.id.pop_second_car_option_lv);
                                final ArrayList<String> list = new ArrayList<>();
                                list.add("全部");
                                list.add("私家車");
                                list.add("客貨車");
                                list.add("貨車");
                                list.add("電單車");
                                list.add("房車");
                                list.add("房跑車");
                                list.add("跑車");
                                list.add("敞篷車");
                                list.add("越野車");
                                list.add("旅行車");
                                list.add("小型車");
                                list.add("7/8人車");
                                list.add("其他");
                                SecondCarOptionListAdapter adapter = new SecondCarOptionListAdapter(SellerDetialActivity.this, list);
                                lv.setAdapter(adapter);
                                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        if (position == 0) {
                                            mRequestSecondCarModel.setType(null);
                                        } else {
                                            mRequestSecondCarModel.setType(list.get(position));
                                        }
                                        mRefreshLayout.autoRefresh();
                                        popMenu.dismiss();
                                    }
                                });
                                //popMenu.showAsDropDown(mTabLayout, 0, 0);

                                View windowContentViewRoot0 = contentView0;
                                int windowPos0[] = BSSMCommonUtils.calculatePopWindowPos(view, windowContentViewRoot0);
                                int xOff0 = 20;// 可以自己调整偏移
                                windowPos0[0] -= xOff0;
                                popMenu.showAtLocation(view, Gravity.TOP | Gravity.START, windowPos0[0], windowPos0[1]);
                                break;
                            case 2:
                                LayoutInflater inflater1 = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                View contentView1 = inflater1.inflate(R.layout.pop_second_car_option_list, null);
                                popMenu = new PopupWindow(contentView1, LinearLayout.LayoutParams.MATCH_PARENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT);
                                popMenu.setFocusable(true);
                                popMenu.setOutsideTouchable(false);
                                //popMenu.setAnimationStyle(R.style.AnimTopMiddle);
                                ListView lv1 = contentView1.findViewById(R.id.pop_second_car_option_lv);
                                final ArrayList<String> list1 = new ArrayList<>();
                                list1.add("全部");
                                list1.add("手動擋");
                                list1.add("自動擋");
                                SecondCarOptionListAdapter adapter1 = new SecondCarOptionListAdapter(SellerDetialActivity.this, list1);
                                lv1.setAdapter(adapter1);
                                lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        switch (position) {
                                            case 0:
                                                mRequestSecondCarModel.setCarGears(-1);
                                                break;
                                            case 1:
                                                mRequestSecondCarModel.setCarGears(0);
                                                break;
                                            case 2:
                                                mRequestSecondCarModel.setCarGears(1);
                                                break;
                                        }
                                        mRefreshLayout.autoRefresh();
                                        popMenu.dismiss();
                                    }
                                });
                                //popMenu.showAsDropDown(mTabLayout, 0, 0);

                                View windowContentViewRoot1 = contentView1;
                                int windowPos1[] = BSSMCommonUtils.calculatePopWindowPos(view, windowContentViewRoot1);
                                int xOff1 = 20;// 可以自己调整偏移
                                windowPos1[0] -= xOff1;
                                popMenu.showAtLocation(view, Gravity.TOP | Gravity.START, windowPos1[0], windowPos1[1]);
                                break;
                            case 3:
                                LayoutInflater inflater2 = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                View contentView2 = inflater2.inflate(R.layout.pop_second_car_option_list, null);
                                popMenu = new PopupWindow(contentView2, LinearLayout.LayoutParams.MATCH_PARENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT);
                                popMenu.setFocusable(true);
                                popMenu.setOutsideTouchable(false);
                                //popMenu.setAnimationStyle(R.style.AnimTopMiddle);
                                ListView lv2 = contentView2.findViewById(R.id.pop_second_car_option_lv);
                                final ArrayList<String> list2 = new ArrayList<>();
                                list2.add("全部");
                                list2.add("2018");
                                list2.add("2017");
                                list2.add("2016");
                                list2.add("2015");
                                list2.add("2014");
                                list2.add("2013");
                                list2.add("2012");
                                list2.add("2011");
                                list2.add("2010");
                                list2.add("2009");
                                list2.add("2008");
                                list2.add("2007");
                                list2.add("2006");
                                list2.add("2005");
                                list2.add("2004");
                                list2.add("2003");
                                list2.add("2002");
                                list2.add("2001");
                                list2.add("2000");
                                SecondCarOptionListAdapter adapter2 = new SecondCarOptionListAdapter(SellerDetialActivity.this, list2);
                                lv2.setAdapter(adapter2);
                                lv2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        if (position == 0) {
                                            mRequestSecondCarModel.setYear(0);
                                        } else {
                                            mRequestSecondCarModel.setYear(Integer.parseInt(list2.get(position)));
                                        }
                                        mRefreshLayout.autoRefresh();
                                        popMenu.dismiss();
                                    }
                                });
                                //popMenu.showAsDropDown(mTabLayout, 0, 0);

                                View windowContentViewRoot2 = contentView2;
                                int windowPos2[] = BSSMCommonUtils.calculatePopWindowPos(view, windowContentViewRoot2);
                                int xOff2 = 20;// 可以自己调整偏移
                                windowPos2[0] -= xOff2;
                                popMenu.showAtLocation(view, Gravity.TOP | Gravity.START, windowPos2[0], windowPos2[1]);
                                break;
                            case 4:
                                LayoutInflater inflater3 = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                View contentView3 = inflater3.inflate(R.layout.dialog_car_choose_money, null);
                                popMenu = new PopupWindow(contentView3, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                popMenu.setFocusable(true);
                                popMenu.setOutsideTouchable(true);
                                //popMenu.setAnimationStyle(R.style.AnimTopMiddle);
                                CrystalRangeSeekbar sb = contentView3.findViewById(R.id.car_choose_sb);
                                final TextView minTv = contentView3.findViewById(R.id.car_choose_min_money);
                                final TextView maxTv = contentView3.findViewById(R.id.car_choose_max_money);
                                TextView submitTv = contentView3.findViewById(R.id.car_choose_money_submit);
                                sb.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
                                    @Override
                                    public void valueChanged(Number minValue, Number maxValue) {
                                        minTv.setText(String.valueOf(minValue) + "萬");
                                        maxTv.setText(String.valueOf(maxValue) + "萬");
                                    }
                                });

                                // set final value listener
                                sb.setOnRangeSeekbarFinalValueListener(new OnRangeSeekbarFinalValueListener() {
                                    @Override
                                    public void finalValue(Number minValue, Number maxValue) {
                                        mRequestSecondCarModel.setStartPrice(minValue.doubleValue());
                                        mRequestSecondCarModel.setEndPrice(maxValue.doubleValue());
                                    }
                                });

                                submitTv.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        mRefreshLayout.autoRefresh();
                                        popMenu.dismiss();
                                    }
                                });

                                View windowContentViewRoot3 = contentView3;
                                int windowPos3[] = BSSMCommonUtils.calculatePopWindowPos(view, windowContentViewRoot3);
                                int xOff3= 20;// 可以自己调整偏移
                                windowPos3[0] -= xOff3;
                                popMenu.showAtLocation(view, Gravity.TOP | Gravity.START, windowPos3[0], windowPos3[1]);
                                break;
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        mRefreshLayout.autoRefresh();
    }


    private ArrayList<String> getData() {
        ArrayList<String> list = new ArrayList<>();
        for (CarBrandOutModel.CarBrandModel model : mCarBrandModelList) {
            list.add(model.getName());
        }
        return list;
    }

    /**
     * 获取商家二手车列表
     */
    private void callNetGetSellerCarList() {
        RequestParams params = new RequestParams(BSSMConfigtor.BASE_URL + BSSMConfigtor.GET_OLD_CAR_LIST + SPUtils.get(this, "UserToken", ""));
        params.setAsJsonContent(true);
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("pageSize", pageSize);
            jsonObj.put("pageNo", pageNo);
            if (mRequestSecondCarModel.getSellerId() != -1) {
                jsonObj.put("sellerId", mRequestSecondCarModel.getSellerId());
            }
            if (mRequestSecondCarModel.getCarBrand() != null) {
                if(!mRequestSecondCarModel.getCarBrand().equals("全部")) {
                    jsonObj.put("carBrand",mRequestSecondCarModel.getCarBrand());
                }
            }
            if (mRequestSecondCarModel.getCarType() != -1) {
                jsonObj.put("carType", mRequestSecondCarModel.getType());
            }
            if (mRequestSecondCarModel.getType() != null) {
                jsonObj.put("type", mRequestSecondCarModel.getCarType());
            }
            if (mRequestSecondCarModel.getYear() != 0) {
                jsonObj.put("year", mRequestSecondCarModel.getYear());
            } else {
                jsonObj.put("year", "2010");
            }
            if (mRequestSecondCarModel.getCarGears() != -1) {
                jsonObj.put("carGears", mRequestSecondCarModel.getCarGears());
            }
            if (mRequestSecondCarModel.getStartPrice() != -1) {
                jsonObj.put("startPrice", mRequestSecondCarModel.getStartPrice());
            }
            if (mRequestSecondCarModel.getEndPrice() != -1) {
                jsonObj.put("endPrice", mRequestSecondCarModel.getEndPrice());
            }
            jsonObj.put("ifperson", mRequestSecondCarModel.getIfperson());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String urlJson = jsonObj.toString();
        params.setBodyContent(urlJson);
        String uri = params.getUri();
        params.setConnectTimeout(30 * 1000);
        x.http().request(HttpMethod.POST ,params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                SecondCarOutModel model = new Gson().fromJson(result.toString(), SecondCarOutModel.class);
                if (model.getCode() == 200) {
                    totolCarCount = model.getData().getTotalCount();
                    mSecondCarList.addAll(model.getData().getData());
                    if (totolCarCount == 0){
                        Toast.makeText(SellerDetialActivity.this,  "沒有搵到該類數據！", Toast.LENGTH_SHORT).show();
                    }
                } else if (model.getCode() == 10000){
                    SPUtils.put(SellerDetialActivity.this, "UserToken", "");
                    Toast.makeText(SellerDetialActivity.this,  String.valueOf(model.getMsg()), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SellerDetialActivity.this, String.valueOf(model.getMsg()), Toast.LENGTH_SHORT).show();
                }
            }
            //请求异常后的回调方法
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if (ex instanceof SocketTimeoutException) {
                    Toast.makeText(SellerDetialActivity.this, getString(R.string.timeout), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SellerDetialActivity.this, getString(R.string.no_net), Toast.LENGTH_SHORT).show();
                }
            }
            //主动调用取消请求的回调方法
            @Override
            public void onCancelled(CancelledException cex) {
            }
            @Override
            public void onFinished() {
                mSmartSecondCarListRefreshAdapter.refreshListView(mSecondCarList);
                mRefreshLayout.finishRefresh();
                mRefreshLayout.finishLoadmore();
            }
        });
    }

    /**
     * 獲取商家详情列表
     */
    private void callNetGetSellerDetial() {
        RequestParams params = new RequestParams(BSSMConfigtor.BASE_URL + BSSMConfigtor.GET_SELLER_DETIAL);
        params.setAsJsonContent(true);
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("id", Integer.parseInt(sellerId));
            if (mLocation != null) {
                jsonObj.put("longitude",mLocation.getLongitude());
                jsonObj.put("latitude",mLocation.getLatitude());
            } else {
                jsonObj.put("longitude","113.548331");
                jsonObj.put("latitude","22.205702");
            }
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
                SellerDetialOutModel model = new Gson().fromJson(result.toString(), SellerDetialOutModel.class);
                if (model.getCode() == 200) {
                    mSellerDetialModel = model.getData();
                    refreshUI();
                } else if (model.getCode() == 10000){
                    SPUtils.put(SellerDetialActivity.this, "UserToken", "");
                    Toast.makeText(SellerDetialActivity.this,  String.valueOf(model.getMsg()), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SellerDetialActivity.this, String.valueOf(model.getMsg()), Toast.LENGTH_SHORT).show();
                }
            }
            //请求异常后的回调方法
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if (ex instanceof SocketTimeoutException) {
                    Toast.makeText(SellerDetialActivity.this, getString(R.string.timeout), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SellerDetialActivity.this, getString(R.string.no_net), Toast.LENGTH_SHORT).show();
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
        x.image().bind(sellerIv, mSellerDetialModel.getSellerLogo(), options1);
        nameTv.setText(mSellerDetialModel.getSellerName());
        distanceTv.setText(mSellerDetialModel.getMeter());
        addressTv.setText(mSellerDetialModel.getAddress());
    }

    /**
     * 獲取車輛品牌列表
     */
    private void callNetGetCarBrand() {
        RequestParams params = new RequestParams(BSSMConfigtor.BASE_URL + BSSMConfigtor.GET_CAR_BRAND_LIST + SPUtils.get(this, "UserToken", ""));
        params.setAsJsonContent(true);
        params.setConnectTimeout(30 * 1000);
        x.http().request(HttpMethod.POST ,params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                CarBrandOutModel model = new Gson().fromJson(result.toString(), CarBrandOutModel.class);
                if (model.getCode() == 200) {
                    CarBrandOutModel.CarBrandModel allModel = new CarBrandOutModel.CarBrandModel();
                    allModel.setId(-1);
                    allModel.setName("全部");
                    allModel.setEngName("All");
                    mCarBrandModelList.addAll(model.getData());
                    mCarBrandModelList.add(0, allModel);
                } else if (model.getCode() == 10000){
                    SPUtils.put(SellerDetialActivity.this, "UserToken", "");
                    Toast.makeText(SellerDetialActivity.this,  String.valueOf(model.getMsg()), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SellerDetialActivity.this, String.valueOf(model.getMsg()), Toast.LENGTH_SHORT).show();
                }
            }
            //请求异常后的回调方法
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if (ex instanceof SocketTimeoutException) {
                    Toast.makeText(SellerDetialActivity.this, getString(R.string.timeout), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SellerDetialActivity.this, getString(R.string.no_net), Toast.LENGTH_SHORT).show();
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.head_left:
                finish();
                break;
            case R.id.seller_detial_ll:
                Intent intent = new Intent(SellerDetialActivity.this, SellerDesActivity.class);
                intent.putExtra("SellerDetialModel", new Gson().toJson(mSellerDetialModel));
                startActivity(intent);
                break;
            case R.id.merchart_tel_ll:
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
