package com.bs.john_li.bsfslotmachine.BSSMActivity.Forum;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.bs.john_li.bsfslotmachine.BSSMActivity.BaseActivity;
import com.bs.john_li.bsfslotmachine.BSSMActivity.Parking.PaymentAcvtivity;
import com.bs.john_li.bsfslotmachine.BSSMAdapter.ContentsAdapter;
import com.bs.john_li.bsfslotmachine.BSSMModel.ContentsListModel;
import com.bs.john_li.bsfslotmachine.BSSMUtils.BSSMConfigtor;
import com.bs.john_li.bsfslotmachine.BSSMUtils.SPUtils;
import com.bs.john_li.bsfslotmachine.BSSMView.BSSMHeadView;
import com.bs.john_li.bsfslotmachine.BSSMView.ExpandSwipeRefreshLayout;
import com.bs.john_li.bsfslotmachine.BSSMView.PublishPopWindow;
import com.bs.john_li.bsfslotmachine.R;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by John_Li on 28/10/2017.
 */

public class OwnArticalListACtivty extends BaseActivity implements View.OnClickListener{
    private BSSMHeadView headView;
    private ListView ownForumLv;
    private LinearLayout own_no_artical_ll;
    private ExpandSwipeRefreshLayout mExpandSwipeRefreshLayout;

    private List<ContentsListModel.DataBean.ContentsModel> contentsList;
    private ContentsAdapter mContentsAdapter;

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
        ownForumLv = findViewById(R.id.own_forum_lv);
        mExpandSwipeRefreshLayout = findViewById(R.id.own_forum_list);
        own_no_artical_ll = findViewById(R.id.own_no_artical_ll);
    }

    @Override
    public void setListener() {
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

        ownForumLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(OwnArticalListACtivty.this, ArticleDetialActivity.class);
                intent.putExtra("startway", 1);
                intent.putExtra("ContentsModel", new Gson().toJson(contentsList.get(i)));
                startActivityForResult(intent, 6);
            }
        });
    }

    @Override
    public void initData() {
        headView.setTitle("我的帖文");
        headView.setLeft(this);
        headView.setRight(R.mipmap.push_invitation, this);

        contentsList = new ArrayList<>();
        mContentsAdapter = new ContentsAdapter(OwnArticalListACtivty.this, contentsList);
        ownForumLv.setAdapter(mContentsAdapter);
        mExpandSwipeRefreshLayout.setRefreshing(true);
        callNetGetContentsList("");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.head_left:
                finish();
                break;
            case R.id.head_right:

                break;
        }
    }

    /**
     * 獲取我的帖文列表
     * @param count
     */
    private void callNetGetContentsList(String count) {
        RequestParams params = new RequestParams(BSSMConfigtor.BASE_URL + BSSMConfigtor.GET_CONTENTS + count + "&token=" + SPUtils.get(this, "UserToken", ""));
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
        mContentsAdapter.refreshListView(contentsList);
        if (mExpandSwipeRefreshLayout.isRefreshing()) {
            mExpandSwipeRefreshLayout.setRefreshing(false);
        }
        if (mExpandSwipeRefreshLayout.isLoading()) {
            mExpandSwipeRefreshLayout.setLoading(false);
        }
        if (contentsList.size() > 0) {
            own_no_artical_ll.setVisibility(View.GONE);
            mExpandSwipeRefreshLayout.setVisibility(View.VISIBLE);
        } else {
            own_no_artical_ll.setVisibility(View.VISIBLE);
            mExpandSwipeRefreshLayout.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 6){
                contentsList.clear();
                mExpandSwipeRefreshLayout.setRefreshing(true);
                callNetGetContentsList("");
            }
        }
    }
}
