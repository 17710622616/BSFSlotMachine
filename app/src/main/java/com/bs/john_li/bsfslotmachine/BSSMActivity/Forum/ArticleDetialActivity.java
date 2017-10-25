package com.bs.john_li.bsfslotmachine.BSSMActivity.Forum;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.telecom.Call;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bs.john_li.bsfslotmachine.BSSMActivity.BaseActivity;
import com.bs.john_li.bsfslotmachine.BSSMActivity.Mine.AddCarActivity;
import com.bs.john_li.bsfslotmachine.BSSMAdapter.CommentsAdapter;
import com.bs.john_li.bsfslotmachine.BSSMAdapter.CommentsExpandAdapter;
import com.bs.john_li.bsfslotmachine.BSSMModel.CommentListModel;
import com.bs.john_li.bsfslotmachine.BSSMModel.CommonModel;
import com.bs.john_li.bsfslotmachine.BSSMModel.ContentsListModel;
import com.bs.john_li.bsfslotmachine.BSSMModel.ReturnCommentsOutModel;
import com.bs.john_li.bsfslotmachine.BSSMUtils.BSSMCommonUtils;
import com.bs.john_li.bsfslotmachine.BSSMUtils.BSSMConfigtor;
import com.bs.john_li.bsfslotmachine.BSSMUtils.SPUtils;
import com.bs.john_li.bsfslotmachine.BSSMView.BSSMHeadView;
import com.bs.john_li.bsfslotmachine.BSSMView.CustomExpandableListView;
import com.bs.john_li.bsfslotmachine.BSSMView.NoScrollListView;
import com.bs.john_li.bsfslotmachine.R;
import com.google.gson.Gson;
import com.othershe.nicedialog.BaseNiceDialog;
import com.othershe.nicedialog.NiceDialog;
import com.othershe.nicedialog.ViewConvertListener;
import com.othershe.nicedialog.ViewHolder;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.HttpMethod;
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
    private CustomExpandableListView contentsLv;

    private ContentsListModel.DataBean.ContentsModel mContentsModel;
    private List<CommentListModel.CommentsArrayModel.CommentsModel> mCommentsModelList;
    private CommentsAdapter mCommentsAdapter;
    private CommentsExpandAdapter mCommentsExpandAdapter;
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
        contentsLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
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
                                        if(BSSMCommonUtils.isLoginNow(ArticleDetialActivity.this)) {    // 已经登录
                                            callNetSubmitReply(editText.getText().toString(), 0, i);
                                        } else {    // 未登录，游客
                                            callNetSubmitReply(editText.getText().toString(), 1, i);
                                        }
                                        dialog.dismiss();
                                    }
                                });
                            }
                        })
                        .setShowBottom(true)
                        .show(getSupportFragmentManager());
            }
        });

        contentsLv.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, final int i, long l) {
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
                                        if(BSSMCommonUtils.isLoginNow(ArticleDetialActivity.this)) {    // 已经登录
                                            callNetSubmitReply(editText.getText().toString(), 0, i);
                                        } else {    // 未登录，游客
                                            callNetSubmitReply(editText.getText().toString(), 1, i);
                                        }
                                        dialog.dismiss();
                                    }
                                });
                            }
                        })
                        .setShowBottom(true)
                        .show(getSupportFragmentManager());
                return true;
            }
        });

        contentsLv.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, final int i, int i1, long l) {
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
                                        if(BSSMCommonUtils.isLoginNow(ArticleDetialActivity.this)) {    // 已经登录
                                            callNetSubmitReply(editText.getText().toString(), 0, i);
                                        } else {    // 未登录，游客
                                            callNetSubmitReply(editText.getText().toString(), 1, i);
                                        }
                                        dialog.dismiss();
                                    }
                                });
                            }
                        })
                        .setShowBottom(true)
                        .show(getSupportFragmentManager());
                return false;
            }
        });
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
        mCommentsExpandAdapter = new CommentsExpandAdapter(this, mCommentsModelList);
        mCommentsAdapter = new CommentsAdapter(this, mCommentsModelList);
        contentsLv.setAdapter(mCommentsExpandAdapter);
        //contentsLv.setGroupIndicator(null);

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
        //mCommentsAdapter.refreshListView(mCommentsModelList);
        mCommentsExpandAdapter.notifyDataSetChanged();
        expandListView();
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
                showCommentDialog();
                break;
        }
    }

    /**
     * 张开所有List
     */
    private void expandListView() {
        for (int i = 0; i < mCommentsModelList.size(); i++) {
            contentsLv.expandGroup(i);
        }
    }

    /**
     * 評論的dialog
     */
    private void showCommentDialog() {
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
                                if(BSSMCommonUtils.isLoginNow(ArticleDetialActivity.this)) {    // 已经登录
                                    callNetSubmitComment(editText.getText().toString(), 0);
                                } else {    // 未登录，游客
                                    callNetSubmitComment(editText.getText().toString(), 1);
                                }
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
        if (way == 0) {
            params = new RequestParams(BSSMConfigtor.BASE_URL + BSSMConfigtor.SUBMITE_COMMENT_LOGIN + SPUtils.get(this, "UserToken", ""));
        } else {
            params = new RequestParams(BSSMConfigtor.BASE_URL + BSSMConfigtor.SUBMITE_COMMENT_VISITOR);
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
        x.http().request(HttpMethod.POST, params, new Callback.CommonCallback<String>() {
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
                    //mCommentsAdapter.refreshListView(mCommentsModelList);
                    mCommentsExpandAdapter.notifyDataSetChanged();
                    expandListView();
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

            }
        });
    }

    /**
     * 回复評論
     * @param content
     * @param way
     * @param position
     */
    private void callNetSubmitReply(String content, int way, final int position) {
        RequestParams params = null;
        if (way == 0) {
            params = new RequestParams(BSSMConfigtor.BASE_URL + BSSMConfigtor.SUBMITE_REPLY_LOGIN + SPUtils.get(this, "UserToken", ""));
        } else {
            params = new RequestParams(BSSMConfigtor.BASE_URL + BSSMConfigtor.SUBMITE_REPLY_VISITOR);
        }
        params.setAsJsonContent(true);
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("commentid", mCommentsModelList.get(position).getId());
            jsonObj.put("content", content);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String urlJson = jsonObj.toString();
        params.setBodyContent(urlJson);
        x.http().request(HttpMethod.POST, params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                ReturnCommentsOutModel model = new Gson().fromJson(result, ReturnCommentsOutModel.class);
                if (model.getCode() == 200) {
                    CommentListModel.CommentsArrayModel.CommentsModel.RepliesBean repliesModel = new CommentListModel.CommentsArrayModel.CommentsModel.RepliesBean();
                    repliesModel.setId(model.getData().getId());
                    repliesModel.setContent(model.getData().getContent());
                    repliesModel.setCommentid(model.getData().getContentid());
                    repliesModel.setCreatetime(model.getData().getCreatetime());
                    repliesModel.setCreator(model.getData().getCreator());
                    repliesModel.setCreatorid(model.getData().getCreatorid());
                    List<CommentListModel.CommentsArrayModel.CommentsModel.RepliesBean> repliesList = mCommentsModelList.get(position).getReplies();
                    if (repliesList == null) {
                        repliesList = new ArrayList<CommentListModel.CommentsArrayModel.CommentsModel.RepliesBean>();
                    }
                    repliesList.add(repliesModel);
                    mCommentsModelList.get(position).setReplies(repliesList);
                    //mCommentsAdapter.refreshListView(mCommentsModelList);
                    mCommentsExpandAdapter.notifyDataSetChanged();
                    expandListView();
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

            }
        });
    }
}
