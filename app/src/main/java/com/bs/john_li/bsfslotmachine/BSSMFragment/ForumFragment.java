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
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.bs.john_li.bsfslotmachine.BSSMActivity.Forum.ArticleDetialActivity;
import com.bs.john_li.bsfslotmachine.BSSMActivity.Forum.OwnArticalListACtivty;
import com.bs.john_li.bsfslotmachine.BSSMActivity.Forum.PublishForumActivity;
import com.bs.john_li.bsfslotmachine.BSSMActivity.Mine.CarListActivity;
import com.bs.john_li.bsfslotmachine.BSSMActivity.Parking.ParkingOrderActivity;
import com.bs.john_li.bsfslotmachine.BSSMActivity.Parking.SearchSlotMachineActivity;
import com.bs.john_li.bsfslotmachine.BSSMAdapter.ContentsAdapter;
import com.bs.john_li.bsfslotmachine.BSSMModel.ContentsListModel;
import com.bs.john_li.bsfslotmachine.BSSMUtils.BSSMCommonUtils;
import com.bs.john_li.bsfslotmachine.BSSMUtils.BSSMConfigtor;
import com.bs.john_li.bsfslotmachine.BSSMUtils.SPUtils;
import com.bs.john_li.bsfslotmachine.BSSMView.BSSMHeadView;
import com.bs.john_li.bsfslotmachine.BSSMView.ExpandSwipeRefreshLayout;
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
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * 首页停车的碎片
 * Created by John_Li on 28/7/2017.
 */

public class ForumFragment extends BaseFragment implements View.OnClickListener, PublishPopWindow.WindowClickCallBack {
    public static String TAG = ForumFragment.class.getName();
    private View forumView;
    private BSSMHeadView forumHeadView;
    private ListView forumLv;
    private ExpandSwipeRefreshLayout mExpandSwipeRefreshLayout;

    private List<ContentsListModel.DataBean.ContentsModel> contentsList;
    private ContentsAdapter mContentsAdapter;

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
        forumLv = forumView.findViewById(R.id.forum_lv);
        mExpandSwipeRefreshLayout = forumView.findViewById(R.id.expandswiperefreshlayout_forum);
    }

    @Override
    public void setListenter() {
        mExpandSwipeRefreshLayout.setOnLoadListener(new ExpandSwipeRefreshLayout.OnLoadListener() {
            @Override
            public void onLoad() {
                mExpandSwipeRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() { //和最大的数据比较
                        if (contentsList.size() > 0) {
                            callNetGetContentsList(Integer.toString(contentsList.get(contentsList.size() - 1).getId()));
                        } else {
                            callNetGetContentsList("");
                        }
                    }
                }, 500);
            }
        });

        mExpandSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                contentsList.clear();
                callNetGetContentsList("");
            }
        });

        forumLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), ArticleDetialActivity.class);
                intent.putExtra("ContentsModel", new Gson().toJson(contentsList.get(i)));
                startActivity(intent);
            }
        });
    }

    @Override
    public void initData() {
        forumHeadView.setTitle("社區");
        forumHeadView.setLeft(R.mipmap.operation_invitation, this);
        forumHeadView.setRight(R.mipmap.push_invitation, this);

        contentsList = new ArrayList<>();
        mContentsAdapter = new ContentsAdapter(getActivity(), contentsList);
        forumLv.setAdapter(mContentsAdapter);
        mExpandSwipeRefreshLayout.setRefreshing(true);
        callNetGetContentsList("");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.head_left:
                if (!SPUtils.get(getActivity(), "UserToken", "").equals("")) {
                    startActivity(new Intent(getActivity(), OwnArticalListACtivty.class));
                } else {
                    Toast.makeText(getActivity(), "您尚未登錄，請登錄先!(｡･_･)!", Toast.LENGTH_SHORT).show();
                }
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

    private void callNetGetContentsList(String count) {
        RequestParams params = new RequestParams(BSSMConfigtor.BASE_URL + BSSMConfigtor.GET_CONTENTS + count);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                ContentsListModel model = new Gson().fromJson(result, ContentsListModel.class);
                if (model.getCode() == 200) {
                    List<ContentsListModel.DataBean.ContentsModel> list = model.getData().getContents();
                    contentsList.addAll(list);
                } else {
                    Toast.makeText(getActivity(), "帖文列表獲取失敗╮(╯▽╰)╭", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(getActivity(), "帖文列表獲取失敗╮(╯▽╰)╭", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                refreshContentsList();
            }
        });
    }

    /**
     * 刷新帖文界面
     */
    private void refreshContentsList() {
        mContentsAdapter.refreshListView(contentsList);
        if (mExpandSwipeRefreshLayout.isRefreshing()) {
            mExpandSwipeRefreshLayout.setRefreshing(false);
        }
        if (mExpandSwipeRefreshLayout.isLoading()) {
            mExpandSwipeRefreshLayout.setLoading(false);
        }
    }
}
