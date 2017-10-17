package com.bs.john_li.bsfslotmachine.BSSMActivity.Forum;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.telecom.Call;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bs.john_li.bsfslotmachine.BSSMActivity.BaseActivity;
import com.bs.john_li.bsfslotmachine.BSSMAdapter.CommentsAdapter;
import com.bs.john_li.bsfslotmachine.BSSMModel.CommentListModel;
import com.bs.john_li.bsfslotmachine.BSSMModel.ContentsListModel;
import com.bs.john_li.bsfslotmachine.BSSMUtils.BSSMConfigtor;
import com.bs.john_li.bsfslotmachine.BSSMView.BSSMHeadView;
import com.bs.john_li.bsfslotmachine.R;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * 帖文詳情
 * Created by John_Li on 18/10/2017.
 */

public class ArticleDetialActivity extends BaseActivity implements View.OnClickListener{
    private BSSMHeadView headView;
    private TextView postNameTv, titleTv, contentsTv, noCommentsTv;
    private ListView contentsLv;

    private ContentsListModel.DataBean.ContentsModel mContentsModel;
    private List<CommentListModel.CommentsArrayModel.CommentsModel> mCommentsModelList;
    private CommentsAdapter mCommentsAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_articel_detial);
        initView();
        setListener();
        initData();
    }

    @Override
    public void initView() {
        headView = findViewById(R.id.articel_detial_head);
        postNameTv = findViewById(R.id.articel_detial_name);
        titleTv = findViewById(R.id.articel_detial_title);
        contentsTv = findViewById(R.id.articel_detial_contents);
        noCommentsTv = findViewById(R.id.articel_contents_no_comments);
        contentsLv = findViewById(R.id.articel_contents_lv);
    }

    @Override
    public void setListener() {

    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        mContentsModel = new Gson().fromJson(intent.getStringExtra("ContentsModel"), ContentsListModel.DataBean.ContentsModel.class);
        headView.setTitle(mContentsModel.getTitle());
        headView.setLeft(this);

        postNameTv.setText(mContentsModel.getCreator());
        titleTv.setText(mContentsModel.getTitle());
        contentsTv.setText(mContentsModel.getContents());

        mCommentsModelList = new ArrayList<>();
        mCommentsAdapter = new CommentsAdapter(this, mCommentsModelList);
        contentsLv.setAdapter(mCommentsAdapter);

        // 獲取評論列表
        callNetGetCommentList();
    }

    /**
     * 獲取評論列表
     */
    private void callNetGetCommentList() {
        RequestParams params = new RequestParams(BSSMConfigtor.BASE_URL + BSSMConfigtor.GET_COMMENTS + mContentsModel.getId() + "&nextId=");
        String url = params.getUri();
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                CommentListModel model = new Gson().fromJson(result, CommentListModel.class);
                if (model.getCode() == 200) {
                    mCommentsModelList.addAll(model.getData().getComments());
                } else {
                    Toast.makeText(ArticleDetialActivity.this, "獲取評論失敗╮(╯▽╰)╭", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(ArticleDetialActivity.this, "獲取評論失敗╮(╯▽╰)╭", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                refreshCommentsList();
            }
        });
    }

    /**
     * 刷新評論列表
     */
    private void refreshCommentsList() {
        mCommentsAdapter.refreshListView(mCommentsModelList);
        if (mCommentsModelList.size() > 0) {
            contentsLv.setVisibility(View.VISIBLE);
            noCommentsTv.setVisibility(View.GONE);
        } else {
            contentsLv.setVisibility(View.GONE);
            noCommentsTv.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.head_left:
                finish();
                break;
        }
    }
}
