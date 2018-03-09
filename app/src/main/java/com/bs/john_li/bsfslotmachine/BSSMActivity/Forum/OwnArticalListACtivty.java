package com.bs.john_li.bsfslotmachine.BSSMActivity.Forum;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bs.john_li.bsfslotmachine.BSSMActivity.BaseActivity;
import com.bs.john_li.bsfslotmachine.BSSMAdapter.SmartOwnRefreshAdapter;
import com.bs.john_li.bsfslotmachine.BSSMModel.ContentsListModel;
import com.bs.john_li.bsfslotmachine.BSSMUtils.BSSMCommonUtils;
import com.bs.john_li.bsfslotmachine.BSSMUtils.BSSMConfigtor;
import com.bs.john_li.bsfslotmachine.BSSMUtils.SPUtils;
import com.bs.john_li.bsfslotmachine.BSSMUtils.StatusBarUtil;
import com.bs.john_li.bsfslotmachine.BSSMView.BSSMHeadView;
import com.bs.john_li.bsfslotmachine.BSSMView.PublishPopWindow;
import com.bs.john_li.bsfslotmachine.R;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by John_Li on 28/10/2017.
 */

public class OwnArticalListACtivty extends BaseActivity implements View.OnClickListener, PublishPopWindow.WindowClickCallBack {
    private BSSMHeadView headView;
    private LinearLayout own_no_artical_ll;
    private RefreshLayout mRefreshLayout;
    private RecyclerView mRecycleView;
    private List<ContentsListModel.DataBean.ContentsModel> contentsList;
    private SmartOwnRefreshAdapter mSmartOwnRefreshAdapter;
    private PublishPopWindow popWindow;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_own_ratical_list);
        initView();
        setListener();
        initData();
    }

    @Override
    public void initView() {
        headView = findViewById(R.id.own_head);
        //ownForumLv = findViewById(R.id.own_forum_lv);
        //mExpandSwipeRefreshLayout = findViewById(R.id.own_forum_list);
        mRecycleView = findViewById(R.id.own_forum_lv);
        mRefreshLayout = findViewById(R.id.own_forum_list);
        own_no_artical_ll = findViewById(R.id.own_no_artical_ll);

        // 设置header的高度
        mRefreshLayout.setHeaderHeightPx((int)(BSSMCommonUtils.getDeviceWitdh(this) / 4.05));//Header标准高度（显示下拉高度>=标准高度 触发刷新）
    }

    @Override
    public void setListener() {
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
        headView.setTitle("我的帖文");
        headView.setLeft(this);
        headView.setRight(R.mipmap.push_invitation, this);

        contentsList = new ArrayList<>();
        mSmartOwnRefreshAdapter = new SmartOwnRefreshAdapter(this, contentsList);
        mRecycleView.setLayoutManager(new LinearLayoutManager(this));
        mRecycleView.setAdapter(mSmartOwnRefreshAdapter);
        mSmartOwnRefreshAdapter.setOnItemClickListenr(new SmartOwnRefreshAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(OwnArticalListACtivty.this, ArticleDetialActivity.class);
                intent.putExtra("startway", 1);
                intent.putExtra("ContentsModel", new Gson().toJson(contentsList.get(position)));
                startActivityForResult(intent, 6);
            }
        });
        mRefreshLayout.autoRefresh();
        callNetGetContentsList("");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.head_left:
                finish();
                break;
            case R.id.head_right:
                if (!SPUtils.get(this, "UserToken", "").equals("")) {
                    popWindow = new PublishPopWindow(this, null);
                    popWindow.showMoreWindow(view);
                    StatusBarUtil.setColor(this,getResources().getColor(R.color.colorWight));
                } else {
                    Toast.makeText(this, "您尚未登錄，請登錄先!(｡･_･)!", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    /**
     * 獲取我的帖文列表
     * @param count
     */
    private void callNetGetContentsList(String count) {
        RequestParams params = new RequestParams(BSSMConfigtor.BASE_URL + BSSMConfigtor.GET_CONTENTS + count + "&token=" + SPUtils.get(this, "UserToken", ""));
        params.setConnectTimeout(30 * 1000);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                ContentsListModel model = new Gson().fromJson(result, ContentsListModel.class);
                if (model.getCode() == 200) {
                    List<ContentsListModel.DataBean.ContentsModel> list = model.getData().getContents();
                    contentsList.addAll(list);
                } else {
                    Toast.makeText(OwnArticalListACtivty.this, "帖文列表獲取失敗╮(╯▽╰)╭", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(OwnArticalListACtivty.this, "帖文列表獲取失敗╮(╯▽╰)╭", Toast.LENGTH_SHORT).show();
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
        if (contentsList.size() > 0) {
            own_no_artical_ll.setVisibility(View.GONE);
        } else {
            own_no_artical_ll.setVisibility(View.VISIBLE);
        }
        mSmartOwnRefreshAdapter.notifyDataSetChanged();
        mRefreshLayout.finishRefresh();
        mRefreshLayout.finishLoadmore();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 1:
                    ContentsListModel.DataBean.ContentsModel returnContentsModel1 = new Gson().fromJson(data.getStringExtra("return_contents"), ContentsListModel.DataBean.ContentsModel.class);
                    contentsList.add(0, returnContentsModel1);
                    refreshContentsList();
                    break;
                case 2:
                    ContentsListModel.DataBean.ContentsModel returnContentsModel2 = new Gson().fromJson(data.getStringExtra("return_contents"), ContentsListModel.DataBean.ContentsModel.class);
                    contentsList.add(0, returnContentsModel2);
                    refreshContentsList();
                    break;
                case 3:
                    ContentsListModel.DataBean.ContentsModel returnContentsModel3 = new Gson().fromJson(data.getStringExtra("return_contents"), ContentsListModel.DataBean.ContentsModel.class);
                    contentsList.add(0, returnContentsModel3);
                    refreshContentsList();
                    break;
                case 6:
                    contentsList.clear();
                    mRefreshLayout.setEnableRefresh(true);
                    callNetGetContentsList("");
                    break;
            }
        }
    }

    @Override
    public void camareCallBack() {
        popWindow.closePopupWindow();
        Intent intent = new Intent(this, PublishForumActivity.class);
        intent.putExtra("startWay","camare");
        startActivityForResult(intent, 1);
    }

    @Override
    public void textCallBack() {
        popWindow.closePopupWindow();
        Intent intent = new Intent(this, PublishForumActivity.class);
        intent.putExtra("startWay","text");
        startActivityForResult(intent, 2);
    }

    @Override
    public void albumCallBack() {
        popWindow.closePopupWindow();
        Intent intent = new Intent(this, PublishForumActivity.class);
        intent.putExtra("startWay","album");
        startActivityForResult(intent, 3);
    }
}
