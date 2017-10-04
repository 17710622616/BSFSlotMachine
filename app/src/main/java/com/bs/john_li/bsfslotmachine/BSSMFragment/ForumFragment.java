package com.bs.john_li.bsfslotmachine.BSSMFragment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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

import com.bs.john_li.bsfslotmachine.BSSMActivity.Forum.PublishForumActivity;
import com.bs.john_li.bsfslotmachine.BSSMActivity.Parking.ParkingOrderActivity;
import com.bs.john_li.bsfslotmachine.BSSMActivity.Parking.SearchSlotMachineActivity;
import com.bs.john_li.bsfslotmachine.BSSMUtils.BSSMCommonUtils;
import com.bs.john_li.bsfslotmachine.BSSMUtils.BSSMConfigtor;
import com.bs.john_li.bsfslotmachine.BSSMView.BSSMHeadView;
import com.bs.john_li.bsfslotmachine.BSSMView.PublishPopWindow;
import com.bs.john_li.bsfslotmachine.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.xutils.x;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.app.Activity.RESULT_OK;

/**
 * 首页停车的碎片
 * Created by John_Li on 28/7/2017.
 */

public class ForumFragment extends BaseFragment implements View.OnClickListener, PublishPopWindow.WindowClickCallBack {
    public static String TAG = ForumFragment.class.getName();
    private View forumView;
    private BSSMHeadView forumHeadView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        forumView = inflater.inflate(R.layout.fragment_forum, null);
        initView();
        setListenter();
        initData();
        return forumView;
    }

    @Override
    public void initView() {
        forumHeadView = forumView.findViewById(R.id.forum_head);
    }

    @Override
    public void setListenter() {
    }

    @Override
    public void initData() {
        forumHeadView.setTitle("社區");
        forumHeadView.setLeft(R.mipmap.operation_invitation, this);
        forumHeadView.setRight(R.mipmap.push_invitation, this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.head_left:
                Toast.makeText(getActivity(), "管理自己的帖子", Toast.LENGTH_SHORT).show();
                break;
            case R.id.head_right:
                PublishPopWindow popWindow = new PublishPopWindow(getActivity(), this);
                popWindow.showMoreWindow(view);
                break;
        }
    }

    @Override
    public void camareCallBack() {
        Intent intent = new Intent(getActivity(), PublishForumActivity.class);
        intent.putExtra("startWay","camare");
        startActivity(intent);
    }

    @Override
    public void textCallBack() {
        Intent intent = new Intent(getActivity(), PublishForumActivity.class);
        intent.putExtra("startWay","text");
        startActivity(intent);
    }

    @Override
    public void albumCallBack() {
        Intent intent = new Intent(getActivity(), PublishForumActivity.class);
        intent.putExtra("startWay","album");
        startActivity(intent);
    }
}
