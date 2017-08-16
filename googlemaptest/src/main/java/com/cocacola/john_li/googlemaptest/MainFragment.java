package com.cocacola.john_li.googlemaptest;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
import java.util.Locale;

/**
 * Created by John_Li on 15/8/2017.
 */

public class MainFragment extends Fragment implements OnMapReadyCallback ,GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private View fgView;
    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;

    private LatLng latLng;
    private GoogleMap mGoogleMap;
    private SupportMapFragment mMapFragment;
    private Marker mCurrLocation;

    private static final int REQUESTCODE = 6001;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fgView = inflater.inflate(R.layout.fragment_main, null);
        mMapFragment = (SupportMapFragment)getChildFragmentManager().findFragmentById(R.id.map_view);
        mMapFragment.getMapAsync(this);
        return fgView;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        // 允许获取我的位置
        try {
            mGoogleMap.setMyLocationEnabled(true);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        buildGoogleApiClient();
        mGoogleApiClient.connect();
    }

    @Override
    public void onPause() {
        super.onPause();
        //Unregister for location callbacks:
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    super.onLocationResult(locationResult);
                }
            });
        }
    }

    protected synchronized void buildGoogleApiClient() {
        Toast.makeText(getActivity(), "buildGoogleApiClient", Toast.LENGTH_SHORT).show();
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Toast.makeText(getActivity(), "onConnected", Toast.LENGTH_SHORT).show();
        Location mLastLocation = null;
        try {
            Log.i("位置", LocationServices.FusedLocationApi.getLocationAvailability(mGoogleApiClient) + "");
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        } catch (SecurityException e) {
            e.printStackTrace();
        }


        if (mLastLocation != null) {
            //place marker at current position
            mGoogleMap.clear();
            latLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title("Current Position");
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()), 15));
            // 添加marker，但是这里我们特意把marker弄成透明的
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.location));

            mCurrLocation = mGoogleMap.addMarker(markerOptions);

            Log.i("位置", mLastLocation + "1111111");
            Log.i("位置", "最新的位置 getProvider " + mLastLocation.getProvider());
            Log.i("位置", "最新的位置 getAccuracy " + mLastLocation.getAccuracy());
            Log.i("位置", "最新的位置 getAltitude " + mLastLocation.getAltitude());
            Log.i("位置", "最新的位置 Bearing() " + mLastLocation.getBearing());
            Log.i("位置", "最新的位置 Extras() " + mLastLocation.getExtras());
            Log.i("位置", "最新的位置 Latitude() " + mLastLocation.getLatitude());
            Log.i("位置", "最新的位置 Longitude()() " + mLastLocation.getLongitude());
            Log.i("位置", " =============  ");
            TextView mTvAddress = (TextView) fgView.findViewById(R.id.main_tv);
            String address = getAddress(getActivity(), mLastLocation.getLatitude(), mLastLocation.getLongitude());
            mTvAddress.setText(address);
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
        });
    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(getActivity(), "onConnectionSuspended", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(getActivity(), "onConnectionFailed", Toast.LENGTH_SHORT).show();
    }

    /**
     * 逆地理编码 得到地址
     * @param context
     * @param latitude
     * @param longitude
     * @return
     */
    public static String getAddress(Context context, double latitude, double longitude) {
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
            return address.get(0).getAddressLine(0) + "  " + address.get(0).getLocality() + " " + address.get(0).getCountryName();
        } catch (Exception e) {
            e.printStackTrace();
            return "未知";
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUESTCODE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    buildGoogleApiClient();
                    mGoogleApiClient.connect();
                } else {
                }
                return;
            }
        }
    }
}
