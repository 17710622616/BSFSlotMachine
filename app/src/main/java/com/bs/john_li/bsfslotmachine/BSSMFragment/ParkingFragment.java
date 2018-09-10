package com.bs.john_li.bsfslotmachine.BSSMFragment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.AnimationDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bs.john_li.bsfslotmachine.BSSMActivity.LoginActivity;
import com.bs.john_li.bsfslotmachine.BSSMActivity.Mine.WalletActivity;
import com.bs.john_li.bsfslotmachine.BSSMActivity.Parking.SearchSlotMachineActivity;
import com.bs.john_li.bsfslotmachine.BSSMActivity.Parking.SlotMachineListActivity;
import com.bs.john_li.bsfslotmachine.BSSMUtils.BSSMCommonUtils;
import com.bs.john_li.bsfslotmachine.BSSMUtils.BSSMConfigtor;
import com.bs.john_li.bsfslotmachine.BSSMView.BSSMHeadView;
import com.bs.john_li.bsfslotmachine.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 首页停车的碎片
 * Created by John_Li on 28/7/2017.
 */
@SuppressWarnings("ResourceType")
public class ParkingFragment extends BaseFragment implements View.OnClickListener, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    public static String TAG = ParkingFragment.class.getName();
    private View parkingView;
    private BSSMHeadView headView;
    private LinearLayout goParlingLL;
    private LinearLayout loadLL;
    private ImageView loadIv;
    private ImageView loadFailIv;
    private TextView loadTv;
    private TextView mTvAddress;

    private AnimationDrawable animationDrawable = null;
    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;

    private LatLng latLng;
    private GoogleMap mGoogleMap;
    private SupportMapFragment mMapFragment;
    private Marker mCurrLocation;
    private LocationManager mLocationManager;
    private Location mLastLocation = null;
    private String mAddress;

    private static final int REQUESTCODE = 6001;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        parkingView = inflater.inflate(R.layout.fragment_parking, null);
        initView();
        setListenter();
        initData();
        return parkingView;
    }

    @Override
    public void initView() {
        headView = (BSSMHeadView) parkingView.findViewById(R.id.parking_head);
        loadLL = parkingView.findViewById(R.id.parking_load_ll);
        loadIv = parkingView.findViewById(R.id.parking_load_iv);
        loadFailIv = parkingView.findViewById(R.id.parking_load_fail);
        loadTv = parkingView.findViewById(R.id.parking_load_tv);
        mTvAddress = (TextView) parkingView.findViewById(R.id.parking_location_info);
        mMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_view);
        goParlingLL = parkingView.findViewById(R.id.go_parling_ll);
        mMapFragment.getMapAsync(this);
        View mapView = mMapFragment.getView();
        // 調整按鈕位置
        if (mapView != null && mapView.findViewById(1) != null) {
            // Get the button view
            View locationButton = ((View) mapView.findViewById(1).getParent()).findViewById(2);
            // and next place it, on bottom right (as Google Maps app)
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
            // position on right bottom
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
            layoutParams.setMargins(0, 90, 30, 0);
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            headView.setHeadHight();
        }
    }

    @Override
    public void setListenter() {
        goParlingLL.setOnClickListener(this);
        loadLL.setOnClickListener(null);
    }

    @Override
    public void initData() {//2
        headView.setTitle("停車");
        headView.setLeft(R.mipmap.search, this);
        headView.setRight(R.mipmap.wallet, this);
        loadIv.setBackgroundResource(R.drawable.load_anim);
        animationDrawable = (AnimationDrawable) loadIv.getBackground();
        animationDrawable.start();
        mLocationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.head_left:
                getActivity().startActivity(new Intent(getActivity(), SearchSlotMachineActivity.class));
                break;
            case R.id.head_right:
                if (BSSMCommonUtils.isLoginNow(getActivity())) {
                    getActivity().startActivity(new Intent(getActivity(), WalletActivity.class));
                } else {
                    startActivityForResult(new Intent(getActivity(), LoginActivity.class), BSSMConfigtor.LOGIN_FOR_RQUEST);
                }
                break;
            case R.id.go_parling_ll:
                if (mLastLocation != null && mAddress != null) {
                    if(!mAddress.equals("未知")){
                        Intent intent = new Intent(getActivity(), SlotMachineListActivity.class);
                        intent.putExtra("Latitude", String.valueOf(mLastLocation.getLatitude()));
                        intent.putExtra("Longitude", String.valueOf(mLastLocation.getLongitude()));
                        intent.putExtra("Address", mAddress);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getActivity(), "您的地址信息錯誤，請重開網絡或重新打開設備~", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "您還沒定位呢，請先定位~", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.parking_load_ll:
                animationDrawable.start();
                loadIv.setVisibility(View.VISIBLE);
                loadFailIv.setVisibility(View.GONE);
                loadTv.setText("加載中......");
                //onConnected(null);
                if (mGoogleMap != null) {
                    mGoogleMap.clear();
                }
                onMapReady(mGoogleMap);
                break;
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {//3
        mGoogleMap = googleMap;
        if (mGoogleMap != null) {
            // 允许获取我的位置
            try {
                mGoogleMap.getUiSettings().setMapToolbarEnabled(false);
                mGoogleMap.setMyLocationEnabled(true);
            } catch (SecurityException e) {
                e.printStackTrace();
            }
            buildGoogleApiClient();
            mGoogleApiClient.connect();

            // 定位按鈕觸發事件：重新定位
            mGoogleMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
                @Override
                public boolean onMyLocationButtonClick() {
                    //onConnected(null);
                    if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                        if (mGoogleMap != null) {
                            mGoogleMap.clear();
                        }
                        onMapReady(mGoogleMap);
                    } else {
                        Toast.makeText(getActivity(), "親，要打開GPS或網絡才可以定位的哦~", Toast.LENGTH_SHORT).show();
                    }
                    return false;
                }
            });
        }
    }


    @Override
    public void onPause() {//返回
        super.onPause();
        //Unregister for location callbacks:
        if (mGoogleApiClient != null) {
            if (mGoogleApiClient.isConnected()) {
                LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        super.onLocationResult(locationResult);
                    }
                });
            }
        }
    }

    protected synchronized void buildGoogleApiClient() {//4
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {//5
        boolean isGPSEnabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean isNetworkEnabled = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (isGPSEnabled || isNetworkEnabled) {
            if (!isGPSEnabled) {
                Toast.makeText(getActivity(), "親，要打開GPS定位更準哦~", Toast.LENGTH_SHORT).show();
                loadMapFail();
            }

            if (!isNetworkEnabled) {
                Toast.makeText(getActivity(), "親，要打開網絡定位更準哦~", Toast.LENGTH_SHORT).show();
                loadMapFail();
            }
        } else {
            Toast.makeText(getActivity(), "親，要打開GPS和網絡才可以定位的哦~", Toast.LENGTH_SHORT).show();
            loadMapFail();
        }

        if (isGPSEnabled && isNetworkEnabled){
            mTimer = new Timer();
            mTimer.schedule(new WaitTask(), 1000, 3000);
        }

        /*try {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        } catch (SecurityException e) {
            e.printStackTrace();
        }

        if (mLastLocation != null) {
            //place marker at current position

            mTvAddress.setVisibility(View.VISIBLE);
            mAddress = getAddress(getActivity(), mLastLocation.getLatitude(), mLastLocation.getLongitude());
            String address = "當前位置：" + mAddress;

            mGoogleMap.clear();
            latLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            //MarkerOptions markerOptions = new MarkerOptions();
            //markerOptions.position(latLng);
            //markerOptions.title(address);
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()), 18));
            loadLL.setVisibility(View.GONE);
            // 添加marker，但是这里我们特意把marker弄成透明的
            //markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.location_mark));

            //mCurrLocation = mGoogleMap.addMarker(markerOptions);
            mTvAddress.setText(address);

            *//*Log.i("位置", mLastLocation + "1111111");
            Log.i("位置", "最新的位置 getProvider " + mLastLocation.getProvider());
            Log.i("位置", "最新的位置 getAccuracy " + mLastLocation.getAccuracy());
            Log.i("位置", "最新的位置 getAltitude " + mLastLocation.getAltitude());
            Log.i("位置", "最新的位置 Bearing() " + mLastLocation.getBearing());
            Log.i("位置", "最新的位置 Extras() " + mLastLocation.getExtras());
            Log.i("位置", "最新的位置 Latitude() " + mLastLocation.getLatitude());
            Log.i("位置", "最新的位置 Longitude()() " + mLastLocation.getLongitude());
            Log.i("位置", " =============  ");*//*
        } else {
            loadMapFail();
        }

        mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(5000); //5 seconds
        mLocationRequest.setFastestInterval(3000); //3 seconds
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        //mLocationRequest.setSmallestDisplacement(0.1F); //1/10 meter

        //LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
            }
        });*/
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        loadMapFail();
    }

    private void loadMapFail() {
        loadLL.setVisibility(View.VISIBLE);
        loadLL.setOnClickListener(this);
        //loadIv.setImageResource(R.mipmap.head_boy);
        loadIv.setVisibility(View.GONE);
        animationDrawable.stop();
        loadFailIv.setVisibility(View.VISIBLE);
        loadTv.setText("加載失敗，點我重新加載\n如果未打開系統定位服務請開啟先!");
        mTvAddress.setText("定位失敗，請重試");
    }

    /**
     * 逆地理编码 得到地址
     * @param context
     * @param latitude
     * @param longitude
     * @return
     */
    public static String getAddress(Context context, double latitude, double longitude) {//6
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> address = geocoder.getFromLocation(latitude, longitude, 1);
            Log.i("位置", "得到位置当前" + address + "'\n"
                    + "经度：" + String.valueOf(address.get(0).getLongitude()) + "\n"
                    + "纬度：" + String.valueOf(address.get(0).getLatitude()) + "\n"
                    + "纬度：" + "国家：" + address.get(0).getCountryName() + "\n"
                    + "城市：" + address.get(0).getLocality() + "\n"
                    + "名称：" + address.get(0).getAddressLine(1) + "\n"
                    + "街道：" + address.get(0).getAddressLine(0)
            );
            //return address.get(0).getCountryName() + " " + address.get(0).getAddressLine(0);
            return address.get(0).getAddressLine(0);
        } catch (Exception e) {
            e.printStackTrace();
            return "未知";
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUESTCODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    buildGoogleApiClient();
                    mGoogleApiClient.connect();
                } else {
                }
                return;
            }
        }
    }

    /**
     * 等待線程，讓主線程等待子線程，每隔一秒拿一次，直至拿到，最多拿五次
     */
    public class WaitTask extends TimerTask {
        int times = 5;
        @Override
        public void run() {
            Message msg = new Message();
            if (times > 0 && mLastLocation == null) {   // 请求次数在五次内，且没获取到定位坐标
                try {
                    mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                } catch (SecurityException e) {
                    e.printStackTrace();
                }
                times--;
            } else {
                if (mLastLocation != null){     // 获取到定位坐标
                    msg.what = 1;
                    mHandler.sendMessage(msg);
                } else {    // 获取次数已经超过五次，且没获取到，判定获取失败
                    msg.what = 2;
                    mHandler.sendMessage(msg);
                }
                mTimer.cancel();
            }
        }
    }

    private Timer mTimer;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    mTvAddress.setVisibility(View.VISIBLE);
                    mAddress = getAddress(getActivity(), mLastLocation.getLatitude(), mLastLocation.getLongitude());
                    String address = "當前位置：" + mAddress;
                    mGoogleMap.clear();
                    latLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                    mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()), 18));
                    loadLL.setVisibility(View.GONE);
                    loadLL.setOnClickListener(null);
                    mTvAddress.setText(address);

                    mLocationRequest = LocationRequest.create();
                    mLocationRequest.setInterval(5000); //5 seconds
                    mLocationRequest.setFastestInterval(3000); //3 seconds
                    mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
                    //mLocationRequest.setSmallestDisplacement(0.1F); //1/10 meter

                    //LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
                    LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, new LocationCallback() {
                        @Override
                        public void onLocationResult(LocationResult locationResult) {
                            super.onLocationResult(locationResult);
                        }
                    });
                    break;
                case 2:
                    loadMapFail();
                    break;
            }
        }
    };
}
