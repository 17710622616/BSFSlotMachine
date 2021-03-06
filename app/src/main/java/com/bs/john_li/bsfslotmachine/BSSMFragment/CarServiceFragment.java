package com.bs.john_li.bsfslotmachine.BSSMFragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Toast;

import com.bs.john_li.bsfslotmachine.BSSMActivity.CarService.AdvertiseActivity;
import com.bs.john_li.bsfslotmachine.BSSMActivity.CarService.CarPartsSetListActivity;
import com.bs.john_li.bsfslotmachine.BSSMActivity.CarService.CarWashActivity;
import com.bs.john_li.bsfslotmachine.BSSMActivity.CarService.CarWashOrderListActivity;
import com.bs.john_li.bsfslotmachine.BSSMActivity.CarService.SecondHandCarListActivity;
import com.bs.john_li.bsfslotmachine.BSSMActivity.CarService.SellerDetialActivity;
import com.bs.john_li.bsfslotmachine.BSSMActivity.Parking.CourseActivity;
import com.bs.john_li.bsfslotmachine.BSSMAdapter.CarServiceAdapter;
import com.bs.john_li.bsfslotmachine.BSSMAdapter.CollapsingAdapter;
import com.bs.john_li.bsfslotmachine.BSSMAdapter.SmartHotSellerRefreshAdapter;
import com.bs.john_li.bsfslotmachine.BSSMModel.HotSellerOutModel;
import com.bs.john_li.bsfslotmachine.BSSMModel.SideShowModel;
import com.bs.john_li.bsfslotmachine.BSSMUtils.AliyunOSSUtils;
import com.bs.john_li.bsfslotmachine.BSSMUtils.BSSMCommonUtils;
import com.bs.john_li.bsfslotmachine.BSSMUtils.BSSMConfigtor;
import com.bs.john_li.bsfslotmachine.BSSMUtils.SPUtils;
import com.bs.john_li.bsfslotmachine.BSSMView.BSSMHeadView;
import com.bs.john_li.bsfslotmachine.BSSMView.FullyLinearLayoutManager;
import com.bs.john_li.bsfslotmachine.BSSMView.NoScrollGridView;
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
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by John_Li on 9/11/2018.
 */

public class CarServiceFragment extends BaseFragment {
    public static String TAG = CarServiceFragment.class.getName();
    private View carServiceView;
    private BSSMHeadView headView;
    private RefreshLayout mRefreshLayout;
    private NoScrollGridView mGv;
    private RecyclerView mLv;
    private ViewPager mViewPager;

    // 輪播圖圖片集合
    private List<ImageView> imgList;
    // 輪播圖適配器
    private CollapsingAdapter mCollapsingAdapter;
    //
    private CarServiceAdapter mCarServiceAdapter;
    //定位都要通过LocationManager这个类实现
    private LocationManager locationManager;
    private Location mLocation;
    private String provider;
    // 每頁加載數量
    private int pageSize = 5;
    // 頁數
    private int pageNo = 1;
    // 車輛總數
    private long totolCarCount = 0;
    private List<HotSellerOutModel.HotSellerModel.DataBean> mHotSellerList;
    private SmartHotSellerRefreshAdapter mSmartHotSellerRefreshAdapter;
    private ImageOptions options1 = new ImageOptions.Builder().setSize(0, 0).setPlaceholderScaleType(ImageView.ScaleType.FIT_XY).setImageScaleType(ImageView.ScaleType.FIT_XY).setLoadingDrawableId(R.mipmap.second_ad).setFailureDrawableId(R.mipmap.second_ad).build();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        carServiceView = inflater.inflate(R.layout.fragment_car_service, null);
        getLocation();
        initView();
        setListenter();
        initData();
        return carServiceView;
    }

    private void getLocation() {
        //获取定位服务
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        //获取当前可用的位置控制器
        List<String> list = locationManager.getProviders(true);

        if (list.contains(LocationManager.GPS_PROVIDER)) {
            //是否为GPS位置控制器
            provider = LocationManager.GPS_PROVIDER;
        } else if (list.contains(LocationManager.NETWORK_PROVIDER)) {
            //是否为网络位置控制器
            provider = LocationManager.NETWORK_PROVIDER;

        } else {
            Toast.makeText(getActivity(), "请检查网络或GPS是否打开", Toast.LENGTH_LONG).show();
            return;
        }
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
        headView = carServiceView.findViewById(R.id.car_service_head);
        mRefreshLayout = (RefreshLayout) carServiceView.findViewById(R.id.car_service_srl);
        mViewPager = carServiceView.findViewById(R.id.car_service_vp);
        mGv = carServiceView.findViewById(R.id.car_service_gv);
        mLv = carServiceView.findViewById(R.id.car_service_lv);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            headView.setHeadHight();
        }
        mRefreshLayout.setEnableAutoLoadmore(false);//是否启用列表惯性滑动到底部时自动加载更多
        mRefreshLayout.setDisableContentWhenRefresh(true);//是否在刷新的时候禁止列表的操作
        mRefreshLayout.setDisableContentWhenLoading(true);//是否在加载的时候禁止列表的操作
        // 设置header的高度
        mRefreshLayout.setHeaderHeightPx((int)(BSSMCommonUtils.getDeviceWitdh(getActivity()) / 4.05));//Header标准高度（显示下拉高度>=标准高度 触发刷新）
    }

    @Override
    public void setListenter() {
        mGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                        getActivity().startActivity(new Intent(getActivity(), CarWashActivity.class));
                        break;
                    case 1:
                        getActivity().startActivity(new Intent(getActivity(), SecondHandCarListActivity.class));
                        break;
                    case 2:
                        getActivity().startActivity(new Intent(getActivity(), CarPartsSetListActivity.class));
                        break;
                    case 3:
                        getActivity().startActivity(new Intent(getActivity(), CarWashOrderListActivity.class));
                        break;
                    case 4:
                        Toast.makeText(getActivity(), "暫未開放，敬請期待！", Toast.LENGTH_SHORT).show();
                        break;
                    case 5:
                        Toast.makeText(getActivity(), "暫未開放，敬請期待！", Toast.LENGTH_SHORT).show();
                        break;
                    case 6:
                        Toast.makeText(getActivity(), "暫未開放，敬請期待！", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                mHotSellerList.clear();
                pageNo = 1;
                callNetGetRecommend();
            }
        });
        mRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                //和最大的数据比较
                if (pageSize * (pageNo + 1) > totolCarCount){
                    Toast.makeText(getActivity(), "沒有更多數據了誒~", Toast.LENGTH_SHORT).show();
                    mRefreshLayout.finishRefresh();
                    mRefreshLayout.finishLoadmore();
                } else {
                    pageNo ++;
                    callNetGetRecommend();
                }
            }
        });
    }

    @Override
    public void initData() {
        headView.setTitle("汽車服務");

        // 初始化橫幅
        imgList = new ArrayList<>();
        mCollapsingAdapter = new CollapsingAdapter(imgList);
        mViewPager.setAdapter(mCollapsingAdapter);
        callNetGetAd();

        // 初始化車輛服務的菜單GridView
        mCarServiceAdapter = new CarServiceAdapter(getActivity());
        mGv.setAdapter(mCarServiceAdapter);

        mHotSellerList = new ArrayList<>();
        mSmartHotSellerRefreshAdapter = new SmartHotSellerRefreshAdapter(getActivity(), mHotSellerList, AliyunOSSUtils.initOSS(getActivity()));
        mSmartHotSellerRefreshAdapter.setOnItemClickListenr(new SmartHotSellerRefreshAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (mHotSellerList.get(position).getUrl() != null) {
                    if (!mHotSellerList.get(position).equals("")) {
                        Intent intent = new Intent(getActivity(), AdvertiseActivity.class);
                        intent.putExtra("advertiseUrl", mHotSellerList.get(position).getUrl());
                        getActivity().startActivity(intent);
                    } else {
                        Intent intent = new Intent(getActivity(), SellerDetialActivity.class);
                        intent.putExtra("sellerId", String.valueOf(mHotSellerList.get(position).getId()));
                        startActivity(intent);
                    }
                } else {
                    Intent intent = new Intent(getActivity(), SellerDetialActivity.class);
                    intent.putExtra("sellerId", String.valueOf(mHotSellerList.get(position).getId()));
                    startActivity(intent);
                }
            }
        });

        FullyLinearLayoutManager linearLayoutManager = new FullyLinearLayoutManager(getActivity());
        mLv.setNestedScrollingEnabled(false);
        //设置布局管理器
        mLv.setLayoutManager(linearLayoutManager);
        mLv.setAdapter(mSmartHotSellerRefreshAdapter);

        mRefreshLayout.autoRefresh();
    }

    private void callNetGetAd() {
        RequestParams params = new RequestParams(BSSMConfigtor.BASE_URL + BSSMConfigtor.GET_SECONDE_CAR_SIDESHOW);
        params.setAsJsonContent(true);
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("type",3);
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
                final SideShowModel model = new Gson().fromJson(result.toString(), SideShowModel.class);
                if (model.getCode() ==200) {
                    for (int i = 0; i < model.getData().size(); i ++) {
                        ImageView iv = new ImageView(getActivity());
                        iv.setBackgroundColor(getResources().getColor(R.color.colorMineGray));
                        iv.setImageResource(R.mipmap.img_loading_list);
                        //AliyunOSSUtils.downloadImg(entry.getValue(), oss, iv, this, R.mipmap.load_img_fail);
                        x.image().bind(iv, model.getData().get(i).getPic(), options1);
                        if (model.getData().get(i).getUrl() != null){
                            if (!model.getData().get(i).getUrl().equals("")) {
                                final int j = i;
                                iv.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent = new Intent(getActivity(), AdvertiseActivity.class);
                                        intent.putExtra("advertiseUrl", model.getData().get(j).getUrl());
                                        getActivity().startActivity(intent);
                                    }
                                });
                            }
                        }
                        imgList.add(iv);
                        mCollapsingAdapter.notifyDataSetChanged();
                    }
                } else if (model.getCode() == 10000){
                    SPUtils.put(getActivity(), "UserToken", "");
                    Toast.makeText(getActivity(),  String.valueOf(model.getMsg()), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(),  String.valueOf(model.getMsg()), Toast.LENGTH_SHORT).show();
                }
            }
            //请求异常后的回调方法
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if (ex instanceof SocketTimeoutException) {
                    Toast.makeText(getActivity(), "請求超時，請重試！", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), getString(R.string.no_net), Toast.LENGTH_SHORT).show();
                }
            }
            //主动调用取消请求的回调方法
            @Override
            public void onCancelled(CancelledException cex) {
            }
            @Override
            public void onFinished() {
                mSmartHotSellerRefreshAdapter.refreshListView(mHotSellerList);
                mRefreshLayout.finishRefresh();
                mRefreshLayout.finishLoadmore();
            }
        });
    }

    private void callNetGetRecommend() {
        RequestParams params = new RequestParams(BSSMConfigtor.BASE_URL + BSSMConfigtor.GET_HOT_SELLER_LIST);
        params.setAsJsonContent(true);
        JSONObject jsonObj = new JSONObject();
        try {
            if (mLocation != null) {
                jsonObj.put("longitude",mLocation.getLongitude());
                jsonObj.put("latitude",mLocation.getLatitude());
            } else {
                jsonObj.put("longitude","113.548331");
                jsonObj.put("latitude","22.205702");
            }
            jsonObj.put("pageSize",pageSize);
            jsonObj.put("pageNo",pageNo);
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
                HotSellerOutModel model = new Gson().fromJson(result.toString(), HotSellerOutModel.class);
                if (model.getCode() ==200) {
                    totolCarCount = model.getData().getTotalCount();
                    List<HotSellerOutModel.HotSellerModel.DataBean> hotSellerModels = model.getData().getData();
                    mHotSellerList.addAll(hotSellerModels);
                } else if (model.getCode() == 10000){
                    SPUtils.put(getActivity(), "UserToken", "");
                    Toast.makeText(getActivity(),  String.valueOf(model.getMsg()), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(),  String.valueOf(model.getMsg()), Toast.LENGTH_SHORT).show();
                }
            }
            //请求异常后的回调方法
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if (ex instanceof SocketTimeoutException) {
                    Toast.makeText(getActivity(), "請求超時，請重試！", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), getString(R.string.no_net), Toast.LENGTH_SHORT).show();
                }
            }
            //主动调用取消请求的回调方法
            @Override
            public void onCancelled(CancelledException cex) {
            }
            @Override
            public void onFinished() {
                mSmartHotSellerRefreshAdapter.refreshListView(mHotSellerList);
                mRefreshLayout.finishRefresh();
                mRefreshLayout.finishLoadmore();
            }
        });
    }
}
