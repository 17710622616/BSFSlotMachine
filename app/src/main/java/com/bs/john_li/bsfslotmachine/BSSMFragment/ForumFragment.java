package com.bs.john_li.bsfslotmachine.BSSMFragment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bs.john_li.bsfslotmachine.BSSMView.BSSMHeadView;
import com.bs.john_li.bsfslotmachine.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * 首页停车的碎片
 * Created by John_Li on 28/7/2017.
 */

public class ForumFragment extends BaseFragment implements View.OnClickListener, OnMapReadyCallback {
    public static String TAG = ForumFragment.class.getName();
    public static final int LOCATION_UPDATE_MIN_DISTANCE = 10;
    public static final int LOCATION_UPDATE_MIN_TIME = 5000;
    /*private Activity mActivity;
    private AppCompatActivity mAppCompatActivity;
    private Toolbar forumToolBar;*/
    private View forumView;
    private BSSMHeadView forumHeadView;
    private MapView mMapView;
    private GoogleMap mGoogleMap;
    private LocationManager mLocationManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        forumView = inflater.inflate(R.layout.fragment_forum, null);
        //mActivity = getActivity();
        initView();
        setListenter();
        initData();
        return forumView;
    }

    @Override
    public void initView() {
        /*forumToolBar = (Toolbar)forumView.findViewById(R.id.forum_toolbar);
        forumToolBar.inflateMenu(R.menu.menu_forum);*/
        forumHeadView = forumView.findViewById(R.id.forum_head);
        mMapView = forumView.findViewById(R.id.main_map_view);
    }

    @Override
    public void setListenter() {
        /*forumToolBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return false;
            }
        });*/
    }

    @Override
    public void initData() {
        forumHeadView.setTitle("社區");
        forumHeadView.setLeft(R.mipmap.operation_invitation, this);
        forumHeadView.setRight(R.mipmap.push_invitation, this);

        mMapView.getMapAsync(this);
        mLocationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.head_left:
                Toast.makeText(getActivity(), "管理自己的帖子", Toast.LENGTH_SHORT).show();
                break;
            case R.id.head_right:
                Toast.makeText(getActivity(), "發佈帖子", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        int googlePlayStatus = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity());
        if (googlePlayStatus != ConnectionResult.SUCCESS) {
            GooglePlayServicesUtil.getErrorDialog(googlePlayStatus, getActivity(), -1).show();
            getActivity().finish();
        } else {
        /* 設定地圖 */
            if (mGoogleMap != null) {
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
                mGoogleMap.setMyLocationEnabled(true);
                mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);
                mGoogleMap.getUiSettings().setAllGesturesEnabled(true);
            }
        }
    }

    private LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {
                drawMarker(location);
                mLocationManager.removeUpdates(mLocationListener);
            } else {
            }
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {
        }

        @Override
        public void onProviderEnabled(String s) {
        }

        @Override
        public void onProviderDisabled(String s) {
        }
    };

    private void getCurrentLocation() {
        boolean isGPSEnabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean isNetworkEnabled = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        Location location = null;
        if (!(isGPSEnabled || isNetworkEnabled))
            Snackbar.make(mMapView, "0.0", Snackbar.LENGTH_INDEFINITE).show();
        else {
            if (isNetworkEnabled) {
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
                mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                        LOCATION_UPDATE_MIN_TIME, LOCATION_UPDATE_MIN_DISTANCE, mLocationListener);
                location = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }

            if (isGPSEnabled) {
                mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                        LOCATION_UPDATE_MIN_TIME, LOCATION_UPDATE_MIN_DISTANCE, mLocationListener);
                location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }
        }
        if (location != null)
            drawMarker(location);
    }

    /* 在Google Map上放上目前位置的地標圖示。 */
    private void drawMarker(Location location) {
        if (mGoogleMap != null) {
            mGoogleMap.clear();
            LatLng gps = new LatLng(location.getLatitude(), location.getLongitude());
            mGoogleMap.addMarker(new MarkerOptions()
                    .position(gps)
                    .title("Current Position"));
            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(gps, 16));
        }
    }
}
