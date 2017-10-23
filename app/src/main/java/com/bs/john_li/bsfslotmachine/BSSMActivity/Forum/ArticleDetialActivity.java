package com.bs.john_li.bsfslotmachine.BSSMActivity.Forum;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.telecom.Call;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bs.john_li.bsfslotmachine.BSSMActivity.BaseActivity;
import com.bs.john_li.bsfslotmachine.BSSMAdapter.CommentsAdapter;
import com.bs.john_li.bsfslotmachine.BSSMModel.CommentListModel;
import com.bs.john_li.bsfslotmachine.BSSMModel.ContentsListModel;
import com.bs.john_li.bsfslotmachine.BSSMModel.ReturnCommentsOutModel;
import com.bs.john_li.bsfslotmachine.BSSMUtils.BSSMCommonUtils;
import com.bs.john_li.bsfslotmachine.BSSMUtils.BSSMConfigtor;
import com.bs.john_li.bsfslotmachine.BSSMUtils.SPUtils;
import com.bs.john_li.bsfslotmachine.BSSMView.BSSMHeadView;
import com.bs.john_li.bsfslotmachine.R;
import com.google.gson.Gson;
import com.othershe.nicedialog.BaseNiceDialog;
import com.othershe.nicedialog.NiceDialog;
import com.othershe.nicedialog.ViewConvertListener;
import com.othershe.nicedialog.ViewHolder;

import org.json.JSONException;
import org.json.JSONObject;
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
    private ImageView postCommentIv;
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
        postCommentIv = findViewById(R.id.articel_share);
    }

    @Override
    public void setListener() {
        postCommentIv.setOnClickListener(this);
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
        String url = BSSMConfigtor.BASE_URL + BSSMConfigtor.GET_COMMENTS + mContentsModel.getId() + "&nextId=";
        if(mCommentsModelList.size() > 0){
            url = url + mCommentsModelList.get(mCommentsModelList.size() - 1);
        }
        RequestParams params = new RequestParams(url);
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
            case R.id.articel_share: // 发表评论
                showCommentDialog(0);
                break;
        }
    }

    /**
     * 評論的dialog
     * @param way   // 打開方式0：評論帖文，1：回復評論
     */
    private void showCommentDialog(final int way) {
        NiceDialog.init()
                .setLayoutId(R.layout.dialog_car_edit)
                .setConvertListener(new ViewConvertListener() {
                    @Override
                    public void convertView(ViewHolder holder, final BaseNiceDialog dialog) {
                        final EditText editText = holder.getView(R.id.car_edit);
                        editText.setHint("請填回復");
                        BSSMCommonUtils.showKeyboard(editText);
                        holder.setOnClickListener(R.id.car_edit_submit, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                callNetSubmitComment(editText.getText().toString(), way);
                                dialog.dismiss();
                            }
                        });
                    }
                })
                .setShowBottom(true)
                .show(getSupportFragmentManager());
    }

    /**
     * 評論帖子
     * @param content
     */
    private void callNetSubmitComment(String content, int way) {
        RequestParams params = null;
        if (way == 0) { // 評論
            params = new RequestParams(BSSMConfigtor.BASE_URL + BSSMConfigtor.SUBMITE_COMMENT_LOGIN + SPUtils.get(this, "UserToken", ""));
        } else {    // 回復
            params = new RequestParams(BSSMConfigtor.BASE_URL + BSSMConfigtor.SUBMITE_REPLY_LOGIN + SPUtils.get(this, "UserToken", ""));
        }
        params.setAsJsonContent(true);
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("contentid", mContentsModel.getId());
            jsonObj.put("content", content);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String urlJson = jsonObj.toString();
        params.setBodyContent(urlJson);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                ReturnCommentsOutModel model = new Gson().fromJson(result, ReturnCommentsOutModel.class);
                if (model.getCode() == 200) {
                    CommentListModel.CommentsArrayModel.CommentsModel commentsModel = new CommentListModel.CommentsArrayModel.CommentsModel();
                    commentsModel.setId(model.getData().getId());
                    commentsModel.setContent(model.getData().getContent());
                    commentsModel.setContentid(model.getData().getContentid());
                    commentsModel.setCreatetime(model.getData().getCreatetime());
                    commentsModel.setCreator(model.getData().getCreator());
                    commentsModel.setCreatorid(model.getData().getCreatorid());
                    mCommentsModelList.add(commentsModel);
                } else {
                    Toast.makeText(ArticleDetialActivity.this, "評論失敗╮(╯▽╰)╭", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(ArticleDetialActivity.this, "評論失敗╮(╯▽╰)╭", Toast.LENGTH_SHORT).show();
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
}
