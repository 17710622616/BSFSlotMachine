package com.bs.john_li.bsfslotmachine.BSSMActivity.CarService;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bs.john_li.bsfslotmachine.BSSMActivity.BaseActivity;
import com.bs.john_li.bsfslotmachine.BSSMActivity.LoginActivity;
import com.bs.john_li.bsfslotmachine.BSSMActivity.Mine.CarListActivity;
import com.bs.john_li.bsfslotmachine.BSSMAdapter.SecondCarOptionListAdapter;
import com.bs.john_li.bsfslotmachine.BSSMAdapter.SmartCarListRefreshAdapter;
import com.bs.john_li.bsfslotmachine.BSSMAdapter.SmartSecondCarListRefreshAdapter;
import com.bs.john_li.bsfslotmachine.BSSMModel.CarBrandOutModel;
import com.bs.john_li.bsfslotmachine.BSSMModel.CarModel;
import com.bs.john_li.bsfslotmachine.BSSMModel.HotCarOutModel;
import com.bs.john_li.bsfslotmachine.BSSMModel.RequestSecondCarModel;
import com.bs.john_li.bsfslotmachine.BSSMModel.SecondCarOutModel;
import com.bs.john_li.bsfslotmachine.BSSMModel.SideShowModel;
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
import com.othershe.nicedialog.BaseNiceDialog;
import com.othershe.nicedialog.NiceDialog;
import com.othershe.nicedialog.ViewConvertListener;
import com.othershe.nicedialog.ViewHolder;
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
 * Created by John_Li on 15/11/2018.
 */

public class SecondHandCarListActivity extends BaseActivity implements View.OnClickListener{
    private BSSMHeadView headView;
    private ImageView sellerIv1, sellerIv2, sellerIv3, sellerIv4, sellerIv5, sellerIv6, sellerIv7, sellerIv8;
    private ImageView hotCarIv1, hotCarIv2, hotCarIv3, hotCarIv4, hotCarIv5, hotCarIv6;
    private ImageView carListTv;
    private TabLayout mTabLayout;
    private PopupWindow popMenu;
    private RefreshLayout mRefreshLayout;
    private RecyclerView mRecycleView;
    private NestedScrollView mSv;
    // 廣告位商家
    private List<SideShowModel.DataBean> mSideShowList;
    // 廣告位熱門車輛
    private List<HotCarOutModel.DataBeanX.HotCarModel> mHotCarModelList;
    // 二手車列表
    private List<SecondCarOutModel.DataBeanX.SecondCarModel> mSecondCarList;
    // 請求列表的類
    private RequestSecondCarModel mRequestSecondCarModel;

    private List<CarBrandOutModel.CarBrandModel> mCarBrandModelList;
    private List<ImageView> hotSecondCarIvList;
    private List<ImageView> secondCarSellerList;
    private List<TextView> hotSecondCarTvList;
    private SmartSecondCarListRefreshAdapter mSmartSecondCarListRefreshAdapter;
    // 每頁加載數量
    private int pageSize = 10;
    // 頁數
    private int pageNo = 1;
    // 車輛總數
    private long totolCarCount;
    //private ImageOptions options = new ImageOptions.Builder().setSize(0, 0).setImageScaleType(ImageView.ScaleType.CENTER_CROP).setLoadingDrawableId(R.mipmap.second_ad).setFailureDrawableId(R.mipmap.second_ad).build();
    private ImageOptions options1 = new ImageOptions.Builder().setSize(0, 0).setPlaceholderScaleType(ImageView.ScaleType.FIT_XY).setImageScaleType(ImageView.ScaleType.FIT_XY).setLoadingDrawableId(R.mipmap.second_ad).setFailureDrawableId(R.mipmap.second_ad).build();
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
        //publishCarTv = findViewById(R.id.publish_car_tv);
        carListTv = findViewById(R.id.car_list_iv);
        mSv = findViewById(R.id.second__handle_car_sv);
        initADData();

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

        mRecycleView = findViewById(R.id.second_car_recycle_lv);
        mRefreshLayout = findViewById(R.id.second_car_srl);
        mRefreshLayout.setEnableAutoLoadmore(false);//是否启用列表惯性滑动到底部时自动加载更多
        mRefreshLayout.setDisableContentWhenRefresh(true);//是否在刷新的时候禁止列表的操作
        mRefreshLayout.setDisableContentWhenLoading(true);//是否在加载的时候禁止列表的操作
        // 设置header的高度
        mRefreshLayout.setHeaderHeightPx((int)(BSSMCommonUtils.getDeviceWitdh(this) / 4.05));//Header标准高度（显示下拉高度>=标准高度 触发刷新）
    }

    @Override
    public void setListener() {
        //publishCarTv.setOnClickListener(this);
        carListTv.setOnClickListener(this);
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
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                mSecondCarList.clear();
                pageNo = 1;
                if (BSSMCommonUtils.isLoginNow(SecondHandCarListActivity.this)) {
                    callNetGetOldeCarList();
                } else {
                    startActivityForResult(new Intent(SecondHandCarListActivity.this, LoginActivity.class), BSSMConfigtor.LOGIN_FOR_RQUEST);
                }
            }
        });
        mRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                //和最大的数据比较
                if (pageSize * (pageNo) > totolCarCount){
                    Toast.makeText(SecondHandCarListActivity.this, "沒有更多數據了誒~", Toast.LENGTH_SHORT).show();
                    mRefreshLayout.finishRefresh();
                    mRefreshLayout.finishLoadmore();
                } else {
                    pageNo ++;
                    if (BSSMCommonUtils.isLoginNow(SecondHandCarListActivity.this)) {
                        callNetGetOldeCarList();
                    } else {
                        startActivityForResult(new Intent(SecondHandCarListActivity.this, LoginActivity.class), BSSMConfigtor.LOGIN_FOR_RQUEST);
                    }
                }
            }
        });
    }

    private ArrayList<String> getData() {
        ArrayList<String> list = new ArrayList<>();
        for (CarBrandOutModel.CarBrandModel model : mCarBrandModelList) {
            list.add(model.getName());
        }
        return list;
    }

    @Override
    public void initData() {
        headView.setLeft(this);
        headView.setTitle("汽車買賣");
        mSideShowList = new ArrayList<>();
        mHotCarModelList = new ArrayList<>();
        mSecondCarList = new ArrayList<>();
        mCarBrandModelList = new ArrayList<>();
        mRequestSecondCarModel = new RequestSecondCarModel();
        mRequestSecondCarModel.setCarGears(-1);
        mRequestSecondCarModel.setCarBrand("全部");
        mRequestSecondCarModel.setCarType(-1);
        mRequestSecondCarModel.setStartPrice(-1);
        mRequestSecondCarModel.setEndPrice(-1);
        mRequestSecondCarModel.setSellerId(-1);
        mSmartSecondCarListRefreshAdapter = new SmartSecondCarListRefreshAdapter(this, mSecondCarList, AliyunOSSUtils.initOSS(this));
        FullyLinearLayoutManager linearLayoutManager = new FullyLinearLayoutManager(this);
        mRecycleView.setNestedScrollingEnabled(false);
        //设置布局管理器
        mRecycleView.setLayoutManager(linearLayoutManager);
        mRecycleView.setAdapter(mSmartSecondCarListRefreshAdapter);
        mSmartSecondCarListRefreshAdapter.setOnItemClickListenr(new SmartSecondCarListRefreshAdapter.OnItemClickListener() {
            @Override
            public void onItemLongClick(View view, int position) {
                Intent intent = new Intent(SecondHandCarListActivity.this, SecondCarDetailActivity.class);
                intent.putExtra("seoncdeCarId", String.valueOf(mSecondCarList.get(position).getId()));
                startActivity(intent);
            }
        });

        for (int i = 0; i < mTabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = mTabLayout.getTabAt(i);
            if (tab == null) return;
            //这里使用到反射，拿到Tab对象后获取Class
            Class c = tab.getClass();
            try {
                //"mView"是Tab的私有属性名称(可查看TabLayout源码),类型是 TabView,TabLayout私有内部类。
                Field field = c.getDeclaredField("mView");
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
                                mSv.scrollTo(0, 600);
                                if (mCarBrandModelList.size() > 0) {
                                    LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                    View contentView = inflater.inflate(R.layout.pop_second_car_option_list, null);
                                    popMenu = new PopupWindow(contentView, LinearLayout.LayoutParams.MATCH_PARENT,
                                            LinearLayout.LayoutParams.WRAP_CONTENT);
                                    popMenu.setFocusable(true);
                                    popMenu.setOutsideTouchable(true);
                                    //popMenu.setAnimationStyle(R.style.AnimTopMiddle);
                                    popMenu.setOnDismissListener(new PopupWindow.OnDismissListener() {
                                        public void onDismiss() {
                                            pageNo = 1;
                                        }
                                    });
                                    ListView lv = contentView.findViewById(R.id.pop_second_car_option_lv);
                                    SecondCarOptionListAdapter adapter = new SecondCarOptionListAdapter(SecondHandCarListActivity.this, getData());
                                    lv.setAdapter(adapter);
                                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                            mRequestSecondCarModel.setCarBrand(mCarBrandModelList.get(position).getName());
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
                                popMenu.setOutsideTouchable(true);
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
                                SecondCarOptionListAdapter adapter = new SecondCarOptionListAdapter(SecondHandCarListActivity.this, list);
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
                                popMenu.setOutsideTouchable(true);
                                //popMenu.setAnimationStyle(R.style.AnimTopMiddle);
                                ListView lv1 = contentView1.findViewById(R.id.pop_second_car_option_lv);
                                final ArrayList<String> list1 = new ArrayList<>();
                                list1.add("全部");
                                list1.add("手動擋");
                                list1.add("自動擋");
                                SecondCarOptionListAdapter adapter1 = new SecondCarOptionListAdapter(SecondHandCarListActivity.this, list1);
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

                                View windowContentViewRoot1 = contentView1;
                                int windowPos1[] = BSSMCommonUtils.calculatePopWindowPos(view, windowContentViewRoot1);
                                int xOff1 = 20;// 可以自己调整偏移
                                windowPos1[0] -= xOff1;
                                popMenu.showAtLocation(view, Gravity.TOP | Gravity.START, windowPos1[0], windowPos1[1]);

                                //popMenu.showAsDropDown(mTabLayout, 0, 0);
                                break;
                            case 3:
                                LayoutInflater inflater2 = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                View contentView2 = inflater2.inflate(R.layout.pop_second_car_option_list, null);
                                popMenu = new PopupWindow(contentView2, LinearLayout.LayoutParams.MATCH_PARENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT);
                                popMenu.setFocusable(true);
                                popMenu.setOutsideTouchable(true);
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
                                SecondCarOptionListAdapter adapter2 = new SecondCarOptionListAdapter(SecondHandCarListActivity.this, list2);
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
                                sb.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
                                    @Override
                                    public void valueChanged(Number minValue, Number maxValue) {
                                        /*tvMin.setText(String.valueOf(minValue));
                                        tvMax.setText(String.valueOf(maxValue));*/
                                    }
                                });

                                // set final value listener
                                sb.setOnRangeSeekbarFinalValueListener(new OnRangeSeekbarFinalValueListener() {
                                    @Override
                                    public void finalValue(Number minValue, Number maxValue) {
                                        Log.d("CRS=>", String.valueOf(minValue) + " : " + String.valueOf(maxValue));
                                    }
                                });
                                View windowContentViewRoot3 = contentView3;
                                int windowPos3[] = BSSMCommonUtils.calculatePopWindowPos(view, windowContentViewRoot3);
                                int xOff3 = 20;// 可以自己调整偏移
                                windowPos3[0] -= xOff3;
                                popMenu.showAtLocation(view, Gravity.TOP | Gravity.START, windowPos3[0], windowPos3[1]);
                                break;
                            case 5:
                                LayoutInflater inflater4 = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                View contentView4 = inflater4.inflate(R.layout.pop_second_car_option_list, null);
                                popMenu = new PopupWindow(contentView4, LinearLayout.LayoutParams.MATCH_PARENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT);
                                popMenu.setFocusable(true);
                                popMenu.setOutsideTouchable(true);
                                //popMenu.setAnimationStyle(R.style.AnimTopMiddle);
                                ListView lv4 = contentView4.findViewById(R.id.pop_second_car_option_lv);
                                final ArrayList<String> list4 = new ArrayList<>();
                                list4.add("全部");
                                list4.add("商家");
                                list4.add("個人");
                                SecondCarOptionListAdapter adapter4 = new SecondCarOptionListAdapter(SecondHandCarListActivity.this, list4);
                                lv4.setAdapter(adapter4);
                                lv4.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        switch (position) {
                                            case 0:
                                                mRequestSecondCarModel.setIfperson(0);
                                                break;
                                            case 1:
                                                mRequestSecondCarModel.setIfperson(1);
                                                break;
                                            case 2:
                                                mRequestSecondCarModel.setIfperson(2);
                                                break;
                                        }
                                        mRefreshLayout.autoRefresh();
                                        popMenu.dismiss();
                                    }
                                });
                                //popMenu.showAsDropDown(mTabLayout, 0, 0);

                                View windowContentViewRoot4 = contentView4;
                                int windowPos4[] = BSSMCommonUtils.calculatePopWindowPos(view, windowContentViewRoot4);
                                int xOff4 = 20;// 可以自己调整偏移
                                windowPos4[0] -= xOff4;
                                popMenu.showAtLocation(view, Gravity.TOP | Gravity.START, windowPos4[0], windowPos4[1]);
                                break;
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        mRefreshLayout.autoRefresh();
        callNetGetSecondCarSellerList();
        callNetGetHotCarList();
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
                    SPUtils.put(SecondHandCarListActivity.this, "UserToken", "");
                    Toast.makeText(SecondHandCarListActivity.this,  String.valueOf(model.getMsg()), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SecondHandCarListActivity.this, String.valueOf(model.getMsg()), Toast.LENGTH_SHORT).show();
                }
            }
            //请求异常后的回调方法
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if (ex instanceof SocketTimeoutException) {
                    Toast.makeText(SecondHandCarListActivity.this, getString(R.string.timeout), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SecondHandCarListActivity.this, getString(R.string.no_net), Toast.LENGTH_SHORT).show();
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

    /**
     * 初始化广告位信息
     */
    private void initADData() {
        secondCarSellerList = new ArrayList<>();
        secondCarSellerList.add((ImageView) findViewById(R.id.second_car_seller_iv1));
        secondCarSellerList.add((ImageView) findViewById(R.id.second_car_seller_iv2));
        secondCarSellerList.add((ImageView) findViewById(R.id.second_car_seller_iv3));
        secondCarSellerList.add((ImageView) findViewById(R.id.second_car_seller_iv4));
        secondCarSellerList.add((ImageView) findViewById(R.id.second_car_seller_iv5));
        secondCarSellerList.add((ImageView) findViewById(R.id.second_car_seller_iv6));
        secondCarSellerList.add((ImageView) findViewById(R.id.second_car_seller_iv7));
        secondCarSellerList.add((ImageView) findViewById(R.id.second_car_seller_iv8));
        hotSecondCarIvList = new ArrayList<>();
        hotSecondCarIvList.add((ImageView) findViewById(R.id.second_car_hot_car_iv1));
        hotSecondCarIvList.add((ImageView) findViewById(R.id.second_car_hot_car_iv2));
        hotSecondCarIvList.add((ImageView) findViewById(R.id.second_car_hot_car_iv3));
        hotSecondCarIvList.add((ImageView) findViewById(R.id.second_car_hot_car_iv4));
        hotSecondCarIvList.add((ImageView) findViewById(R.id.second_car_hot_car_iv5));
        hotSecondCarIvList.add((ImageView) findViewById(R.id.second_car_hot_car_iv6));

        hotSecondCarTvList = new ArrayList<>();
        hotSecondCarTvList.add((TextView) findViewById(R.id.second_car_hot_car_tv1));
        hotSecondCarTvList.add((TextView) findViewById(R.id.second_car_hot_car_tv2));
        hotSecondCarTvList.add((TextView) findViewById(R.id.second_car_hot_car_tv3));
        hotSecondCarTvList.add((TextView) findViewById(R.id.second_car_hot_car_tv4));
        hotSecondCarTvList.add((TextView) findViewById(R.id.second_car_hot_car_tv5));
        hotSecondCarTvList.add((TextView) findViewById(R.id.second_car_hot_car_tv6));

        for (int i = 0; i < hotSecondCarIvList.size(); i++) {
            final int finalI = i;
            hotSecondCarIvList.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mHotCarModelList.size() > 0) {
                        Intent intent = new Intent(SecondHandCarListActivity.this, SecondCarDetailActivity.class);
                        intent.putExtra("seoncdeCarId", String.valueOf(mHotCarModelList.get(finalI).getId()));
                        startActivity(intent);
                    }
                }
            });
        }

        for (int i = 0; i < secondCarSellerList.size(); i++) {
            final int finalI = i;
            secondCarSellerList.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mSideShowList.size() > 0) {
                        Intent intent = new Intent(SecondHandCarListActivity.this, SellerDetialActivity.class);
                        String.valueOf(secondCarSellerList.get(finalI).getId());
                        intent.putExtra("sellerId", String.valueOf(mSideShowList.get(finalI).getSellerId()));
                        startActivity(intent);
                    }
                }
            });
        }
    }

    /**
     * 获取广告位商家信息
     */
    private void callNetGetSecondCarSellerList() {
        RequestParams params = new RequestParams(BSSMConfigtor.BASE_URL + BSSMConfigtor.GET_SECONDE_CAR_SIDESHOW);
        params.setAsJsonContent(true);
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("type",1);
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
                SideShowModel model = new Gson().fromJson(result.toString(), SideShowModel.class);
                if (model.getCode() ==200) {
                    mSideShowList.addAll(model.getData());
                    if (mSideShowList.size() > 8) {
                        for (int i = 0; i < 8; i++) {
                            x.image().bind(secondCarSellerList.get(i), mSideShowList.get(i).getPic(), options1);
                        }
                    } else {
                        for (int i = 0; i < mSideShowList.size(); i++) {
                            x.image().bind(secondCarSellerList.get(i), mSideShowList.get(i).getPic(), options1);
                        }
                    }
                } else if (model.getCode() == 10000){
                    SPUtils.put(SecondHandCarListActivity.this, "UserToken", "");
                    Toast.makeText(SecondHandCarListActivity.this,  String.valueOf(model.getMsg()), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SecondHandCarListActivity.this, String.valueOf(model.getMsg()), Toast.LENGTH_SHORT).show();
                }
            }
            //请求异常后的回调方法
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if (ex instanceof SocketTimeoutException) {
                    Toast.makeText(SecondHandCarListActivity.this, getString(R.string.timeout), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SecondHandCarListActivity.this, getString(R.string.no_net), Toast.LENGTH_SHORT).show();
                }
            }
            //主动调用取消请求的回调方法
            @Override
            public void onCancelled(CancelledException cex) {
            }
            @Override
            public void onFinished() {
                mRefreshLayout.finishRefresh();
                mRefreshLayout.finishLoadmore();
            }
        });
    }

    /**
     * 获取熱門車信息
     */
    private void callNetGetHotCarList() {
        RequestParams params = new RequestParams(BSSMConfigtor.BASE_URL + BSSMConfigtor.GET_HOT_CAR);
        params.setAsJsonContent(true);
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("pageSize",6);
            jsonObj.put("pageNo",1);
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
                HotCarOutModel model = new Gson().fromJson(result.toString(), HotCarOutModel.class);
                if (model.getCode() ==200) {
                    mHotCarModelList.addAll(model.getData().getData());
                    if (mHotCarModelList.size() > 6) {
                        for (int i = 0; i < 6; i++) {
                            x.image().bind(hotSecondCarIvList.get(i), mHotCarModelList.get(i).getCarImg(), options1);
                            hotSecondCarTvList.get(i).setText("人氣:" + mHotCarModelList.get(i).getPageView());
                        }
                    } else {
                        for (int i = 0; i < mHotCarModelList.size(); i++) {
                            x.image().bind(hotSecondCarIvList.get(i), mHotCarModelList.get(i).getCarImg(), options1);
                            hotSecondCarTvList.get(i).setText("人氣:" + mHotCarModelList.get(i).getPageView());
                        }
                    }
                } else if (model.getCode() == 10000){
                    SPUtils.put(SecondHandCarListActivity.this, "UserToken", "");
                    Toast.makeText(SecondHandCarListActivity.this,  String.valueOf(model.getMsg()), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SecondHandCarListActivity.this, String.valueOf(model.getMsg()), Toast.LENGTH_SHORT).show();
                }
            }
            //请求异常后的回调方法
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if (ex instanceof SocketTimeoutException) {
                    Toast.makeText(SecondHandCarListActivity.this, getString(R.string.timeout), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SecondHandCarListActivity.this, getString(R.string.no_net), Toast.LENGTH_SHORT).show();
                }
            }
            //主动调用取消请求的回调方法
            @Override
            public void onCancelled(CancelledException cex) {
            }
            @Override
            public void onFinished() {
                mRefreshLayout.finishRefresh();
                mRefreshLayout.finishLoadmore();
            }
        });
    }


    /**
     * 获取二手車車列表
     */
    private void callNetGetOldeCarList() {
        RequestParams params = new RequestParams(BSSMConfigtor.BASE_URL + BSSMConfigtor.GET_OLD_CAR_LIST + SPUtils.get(this, "UserToken", ""));
        params.setAsJsonContent(true);
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("pageSize", pageSize);
            jsonObj.put("pageNo", pageNo);
            if (mRequestSecondCarModel.getCarType() != -1) {
                jsonObj.put("carType", mRequestSecondCarModel.getCarType());
            }
            if (!mRequestSecondCarModel.getCarBrand().equals("全部")) {
                jsonObj.put("carBrand",mRequestSecondCarModel.getCarBrand());
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
                        Toast.makeText(SecondHandCarListActivity.this,  "沒有搵到該類數據！", Toast.LENGTH_SHORT).show();
                    }
                } else if (model.getCode() == 10000){
                    SPUtils.put(SecondHandCarListActivity.this, "UserToken", "");
                    Toast.makeText(SecondHandCarListActivity.this,  String.valueOf(model.getMsg()), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SecondHandCarListActivity.this, String.valueOf(model.getMsg()), Toast.LENGTH_SHORT).show();
                }
            }
            //请求异常后的回调方法
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if (ex instanceof SocketTimeoutException) {
                    Toast.makeText(SecondHandCarListActivity.this, getString(R.string.timeout), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SecondHandCarListActivity.this, getString(R.string.no_net), Toast.LENGTH_SHORT).show();
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_left:
                finish();
                break;
            /*case R.id.publish_car_tv:
                startActivityForResult(new Intent(SecondHandCarListActivity.this, PublishOwnSecondCarActivity.class), 1);
                break;
            case R.id.car_list_tv:
                startActivity(new Intent(SecondHandCarListActivity.this, OwnCarListActivity.class));
                break;*/
            case R.id.car_list_iv:
                startActivity(new Intent(SecondHandCarListActivity.this, OwnCarListActivity.class));
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                mRefreshLayout.autoRefresh();
            }
        }
    }
}
