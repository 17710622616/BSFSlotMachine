package com.bs.john_li.bsfslotmachine.BSSMActivity.Forum;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bs.john_li.bsfslotmachine.BSSMActivity.BaseActivity;
import com.bs.john_li.bsfslotmachine.BSSMAdapter.CommentsExpandAdapter;
import com.bs.john_li.bsfslotmachine.BSSMModel.CommentListModel;
import com.bs.john_li.bsfslotmachine.BSSMModel.ContentsListModel;
import com.bs.john_li.bsfslotmachine.BSSMModel.ReturnCommentsOutModel;
import com.bs.john_li.bsfslotmachine.BSSMUtils.BSSMCommonUtils;
import com.bs.john_li.bsfslotmachine.BSSMUtils.BSSMConfigtor;
import com.bs.john_li.bsfslotmachine.BSSMUtils.SPUtils;
import com.bs.john_li.bsfslotmachine.BSSMView.BSSMHeadView;
import com.bs.john_li.bsfslotmachine.BSSMView.CustomExpandableListView;
import com.bs.john_li.bsfslotmachine.R;
import com.google.gson.Gson;
import com.othershe.nicedialog.BaseNiceDialog;
import com.othershe.nicedialog.NiceDialog;
import com.othershe.nicedialog.ViewConvertListener;
import com.othershe.nicedialog.ViewHolder;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by John_Li on 9/11/2017.
 */

public class CommentsListActivity extends BaseActivity implements View.OnClickListener{
    private BSSMHeadView commentListHead;
    private LinearLayout noCommentLL;
    private RefreshLayout mRefreshLayout;
    private CustomExpandableListView mExpandableListView;
    // 提交中的dialog
    private AlertDialog mSubmitDialog;
    //提交中的dialog中的動畫
    private RotateAnimation mRotateAnimation;

    private ContentsListModel.DataBean.ContentsModel mContentsModel;
    private List<CommentListModel.CommentsArrayModel.CommentsModel> mCommentsModelList;
    private CommentsExpandAdapter mCommentsExpandAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments_list);
        initView();
        setListener();
        initData();
    }

    @Override
    public void initView() {
        commentListHead = findViewById(R.id.comments_list_head);
        mExpandableListView = findViewById(R.id.atical_detial_lv);
        mRefreshLayout = findViewById(R.id.atical_detial_srl);
        noCommentLL = findViewById(R.id.comments_list_ll);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            commentListHead.setHeadHight();
        }
        mRefreshLayout.setEnableAutoLoadmore(false);//是否启用列表惯性滑动到底部时自动加载更多
        mRefreshLayout.setDisableContentWhenRefresh(true);//是否在刷新的时候禁止列表的操作
        mRefreshLayout.setDisableContentWhenLoading(true);//是否在加载的时候禁止列表的操作
        // 设置header的高度
        mRefreshLayout.setHeaderHeightPx((int)(BSSMCommonUtils.getDeviceWitdh(this) / 4.05));//Header标准高度（显示下拉高度>=标准高度 触发刷新）\
        mRefreshLayout.setLoadmoreFinished(false);
        //mExpandableListView.setFocusable(false);
    }

    @Override
    public void setListener() {
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                mCommentsModelList.clear();
                callNetGetCommentList("");
            }
        });
        mRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                if (mCommentsModelList.size() > 0) {
                    callNetGetCommentList(Integer.toString(mCommentsModelList.get(mCommentsModelList.size() - 1).getId()));
                } else {
                    callNetGetCommentList("");
                }
            }
        });

        noCommentLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callNetGetCommentList("");
            }
        });

        mExpandableListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
                                        if(BSSMCommonUtils.isLoginNow(CommentsListActivity.this)) {    // 已经登录
                                            callNetSubmitReply(String.valueOf(editText.getText()), 0, i);
                                        } else {    // 未登录，游客
                                            callNetSubmitReply(String.valueOf(editText.getText()), 1, i);
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

        mExpandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
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
                                        if(BSSMCommonUtils.isLoginNow(CommentsListActivity.this)) {    // 已经登录
                                            callNetSubmitReply(String.valueOf(editText.getText()), 0, i);
                                        } else {    // 未登录，游客
                                            callNetSubmitReply(String.valueOf(editText.getText()), 1, i);
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

        mExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, final int groupPosition, int childPosition, long l) {
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
                                        if(BSSMCommonUtils.isLoginNow(CommentsListActivity.this)) {    // 已经登录
                                            callNetSubmitReply(editText.getText().toString(), 0, groupPosition);
                                        } else {    // 未登录，游客
                                            callNetSubmitReply(editText.getText().toString(), 1, groupPosition);
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
        // 獲取帖文資料
        Intent intent = getIntent();
        mContentsModel = new Gson().fromJson(intent.getStringExtra("ContentsModel"), ContentsListModel.DataBean.ContentsModel.class);
        commentListHead.setTitle("評論列表");
        commentListHead.setLeft(CommentsListActivity.this);
        //commentListHead.setRight(R.mipmap.push_invitation, this);
        // 評論列表
        mCommentsModelList = new ArrayList<>();
        mCommentsExpandAdapter = new CommentsExpandAdapter(this, mCommentsModelList);
        mExpandableListView.setAdapter(mCommentsExpandAdapter);
        mRefreshLayout.autoRefresh();
    }

    /**
     * 獲取評論列表
     */
    private void callNetGetCommentList(String nextId) {
        String url = BSSMConfigtor.BASE_URL + BSSMConfigtor.GET_COMMENTS + mContentsModel.getId() + "&nextId=";
        if(mCommentsModelList.size() > 0){
            url = url + nextId;
        }
        RequestParams params = new RequestParams(url);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                CommentListModel model = new Gson().fromJson(result, CommentListModel.class);
                if (model.getCode() == 200) {
                    mCommentsModelList.addAll(model.getData().getComments());
                } else {
                    Toast.makeText(CommentsListActivity.this, "獲取評論失敗╮(╯▽╰)╭" + String.valueOf(model.getMsg()), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if (ex instanceof SocketTimeoutException) {
                    Toast.makeText(CommentsListActivity.this, "獲取評論失敗超時，請重試！", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(CommentsListActivity.this, "獲取評論失敗╮(╯▽╰)╭", Toast.LENGTH_SHORT).show();
                }
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
        mRefreshLayout.finishRefresh();
        mRefreshLayout.finishLoadmore();
        mCommentsExpandAdapter.notifyDataSetChanged();
        // 展开所有回复
        expandListView();
    }

    /**
     * 张开所有List
     */
    private void expandListView() {
        for (int i = 0; i < mCommentsModelList.size(); i++) {
            String content = mCommentsModelList.get(i).getContent();
            mExpandableListView.expandGroup(i);
        }
    }


    /**
     * 回复評論
     * @param content
     * @param way
     * @param position
     */
    private void callNetSubmitReply(String content, int way, final int position) {
        showLoadingDialog();
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
        params.setConnectTimeout(30 * 1000);
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
                    repliesList.add(0, repliesModel);
                    mCommentsModelList.get(position).setReplies(repliesList);
                    //mCommentsAdapter.refreshListView(mCommentsModelList);
                    mCommentsExpandAdapter.notifyDataSetChanged();
                    expandListView();
                } else {
                    Toast.makeText(CommentsListActivity.this, "評論失敗╮(╯▽╰)╭" + String.valueOf(model.getMsg()), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if (ex instanceof SocketTimeoutException) {
                    Toast.makeText(CommentsListActivity.this, "評論失敗超時，請重試!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(CommentsListActivity.this, "評論失敗╮(╯▽╰)╭", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                closeLoadingDialog();
            }
        });
    }


    /**
     *  顯示加載中的dialog
     */
    private void showLoadingDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.NoBackGroundDialog);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_loading,null);
        builder.setView(view);
        mSubmitDialog = builder.create();
        mSubmitDialog.setCancelable(false);
        ImageView mSubmitDialogIv = view.findViewById(R.id.dialog_loading_iv);
        mRotateAnimation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        mSubmitDialogIv.setAnimation(mRotateAnimation);
        mSubmitDialogIv.startAnimation(mRotateAnimation);
        mSubmitDialog.show();
    }

    /**
     * 關閉加載中的dialog
     */
    private void closeLoadingDialog() {
        mRotateAnimation.cancel();
        mSubmitDialog.dismiss();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_left:
                finish();
                break;
        }
    }
}
