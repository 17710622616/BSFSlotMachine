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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bs.john_li.bsfslotmachine.BSSMActivity.Forum.ArticleDetialActivity;
import com.bs.john_li.bsfslotmachine.BSSMActivity.Forum.OwnArticalListACtivty;
import com.bs.john_li.bsfslotmachine.BSSMActivity.Forum.PublishForumActivity;
import com.bs.john_li.bsfslotmachine.BSSMActivity.Mine.CarListActivity;
import com.bs.john_li.bsfslotmachine.BSSMActivity.Parking.ParkingOrderActivity;
import com.bs.john_li.bsfslotmachine.BSSMActivity.Parking.SearchSlotMachineActivity;
import com.bs.john_li.bsfslotmachine.BSSMAdapter.ContentsAdapter;
import com.bs.john_li.bsfslotmachine.BSSMAdapter.SmartRefreshAdapter;
import com.bs.john_li.bsfslotmachine.BSSMModel.ContentsListModel;
import com.bs.john_li.bsfslotmachine.BSSMUtils.BSSMCommonUtils;
import com.bs.john_li.bsfslotmachine.BSSMUtils.BSSMConfigtor;
import com.bs.john_li.bsfslotmachine.BSSMUtils.OnLoadMoreScrollListener;
import com.bs.john_li.bsfslotmachine.BSSMUtils.SPUtils;
import com.bs.john_li.bsfslotmachine.BSSMUtils.StatusBarUtil;
import com.bs.john_li.bsfslotmachine.BSSMView.BSSMHeadView;
import com.bs.john_li.bsfslotmachine.BSSMView.ExpandSwipeRefreshLayout;
import com.bs.john_li.bsfslotmachine.BSSMView.LoadMoreListView;
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
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

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
    //private SwipeRefreshLayout swipeRefreshLayout;
    private RefreshLayout mRefreshLayout;
    private RecyclerView mRecycleView;
    //private LoadMoreListView mListView;
    private List<ContentsListModel.DataBean.ContentsModel> contentsList;
    private ContentsAdapter mContentsAdapter;
    private SmartRefreshAdapter mSmartRefreshAdapter;
    private PublishPopWindow popWindow;

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
        mRefreshLayout = (RefreshLayout) forumView.findViewById(R.id.forum_srl);
        mRecycleView = (RecyclerView) forumView.findViewById(R.id.forum_lv);
        /*mListView = (LoadMoreListView) forumView.findViewById(R.id.forum_lv);
        mListView.setFooterView(View.inflate(getActivity(), R.layout.layout_load_more_footer, null), new OnLoadMoreScrollListener.OnLoadMoreStateListener() {
            @Override
            public void onNormal(View footView) {
                footView.findViewById(R.id.footer_pb_loading).setVisibility(View.GONE);
                ((TextView) footView.findViewById(R.id.footer_tv_msg)).setText("上拉加載更多");
            }

            @Override
            public void onLoading(View footView) {
                footView.findViewById(R.id.footer_pb_loading).setVisibility(View.VISIBLE);
                ((TextView) footView.findViewById(R.id.footer_tv_msg)).setText("正在加載...");

                if (contentsList.size() > 0) {
                    callNetGetContentsList(Integer.toString(contentsList.get(contentsList.size() - 1).getId()));
                } else {
                    callNetGetContentsList("");
                }
            }

            @Override
            public void onEnd(View footView) {
                footView.findViewById(R.id.footer_pb_loading).setVisibility(View.GONE);
                ((TextView) footView.findViewById(R.id.footer_tv_msg)).setText("已加載全部");
            }
        });
        contentsList = new ArrayList<>();
        mContentsAdapter = new ContentsAdapter(getActivity(), contentsList);
        mListView.setAdapter(mContentsAdapter);

        swipeRefreshLayout = forumView.findViewById(R.id.forum_srl);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reFresh();
            }
        });
        reFresh();*/
    }

    @Override
    public void setListenter() {
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                contentsList.clear();
                callNetGetContentsList("");
            }
        });
        mRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                if (contentsList.size() > 0) {
                    callNetGetContentsList(Integer.toString(contentsList.get(contentsList.size() - 1).getId()));
                } else {
                    callNetGetContentsList("");
                }
            }
        });
    }

    @Override
    public void initData() {
        forumHeadView.setTitle("社區");
        forumHeadView.setLeft(R.mipmap.operation_invitation, this);
        forumHeadView.setRight(R.mipmap.push_invitation, this);

        /*swipeRefreshLayout.setRefreshing(true);*/
        contentsList = new ArrayList<>();
        mSmartRefreshAdapter = new SmartRefreshAdapter(getActivity(), contentsList);
        mRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecycleView.setAdapter(mSmartRefreshAdapter);

        mSmartRefreshAdapter.setOnItemClickListenr(new SmartRefreshAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getActivity(), ArticleDetialActivity.class);
                intent.putExtra("ContentsModel", new Gson().toJson(contentsList.get(position)));
                startActivity(intent);
            }
        });

        callNetGetContentsList("");
    }

    private void reFresh() {
        contentsList.clear();
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
                if (!SPUtils.get(getActivity(), "UserToken", "").equals("")) {
                    popWindow = new PublishPopWindow(getActivity(), this);
                    popWindow.showMoreWindow(view);
                    StatusBarUtil.setColor(getActivity(),getResources().getColor(R.color.colorWight));
                } else {
                    Toast.makeText(getActivity(), "您尚未登錄，請登錄先!(｡･_･)!", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void camareCallBack() {
        popWindow.closePopupWindow();
        Intent intent = new Intent(getActivity(), PublishForumActivity.class);
        intent.putExtra("startWay","camare");
        startActivityForResult(intent, 1);
    }

    @Override
    public void textCallBack() {
        popWindow.closePopupWindow();
        Intent intent = new Intent(getActivity(), PublishForumActivity.class);
        intent.putExtra("startWay","text");
        startActivityForResult(intent, 2);
    }

    @Override
    public void albumCallBack() {
        popWindow.closePopupWindow();
        Intent intent = new Intent(getActivity(), PublishForumActivity.class);
        intent.putExtra("startWay","album");
        startActivityForResult(intent, 3);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            ContentsListModel.DataBean.ContentsModel returnContentsModel = new Gson().fromJson(data.getStringExtra("return_contents"), ContentsListModel.DataBean.ContentsModel.class);
            switch (requestCode) {
                case 1:
                    contentsList.add(0, returnContentsModel);
                    refreshContentsList();
                    break;
                case 2:
                    contentsList.add(0, returnContentsModel);
                    refreshContentsList();
                    break;
                case 3:
                    contentsList.add(0, returnContentsModel);
                    refreshContentsList();
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void callNetGetContentsList(String count) {
        RequestParams params = null;
        if (count != null) {
            params = new RequestParams(BSSMConfigtor.BASE_URL + BSSMConfigtor.GET_CONTENTS + count);
        } else {
            params = new RequestParams(BSSMConfigtor.BASE_URL + BSSMConfigtor.GET_CONTENTS);
        }
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                ContentsListModel model = new Gson().fromJson(result, ContentsListModel.class);
                if (model.getCode() == 200) {
                    List<ContentsListModel.DataBean.ContentsModel> list = model.getData().getContents();
                    /*if (list.size() <= 0) {
                        mListView.setEnd(true);
                    } else {
                        contentsList.addAll(list);
                    }*/
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
        /*mContentsAdapter.refreshListView(contentsList);
        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }*/
        mSmartRefreshAdapter.notifyDataSetChanged();
        mRefreshLayout.finishRefresh();
        mRefreshLayout.finishLoadmore();
    }
}
