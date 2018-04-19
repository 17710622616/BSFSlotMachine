package com.bs.john_li.bsfslotmachine.BSSMActivity.Forum;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telecom.Call;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.sdk.android.oss.OSSClient;
import com.bs.john_li.bsfslotmachine.BSSMActivity.BaseActivity;
import com.bs.john_li.bsfslotmachine.BSSMActivity.Mine.AddCarActivity;
import com.bs.john_li.bsfslotmachine.BSSMAdapter.CollapsingAdapter;
import com.bs.john_li.bsfslotmachine.BSSMAdapter.CommentsAdapter;
import com.bs.john_li.bsfslotmachine.BSSMAdapter.CommentsExpandAdapter;
import com.bs.john_li.bsfslotmachine.BSSMModel.ArticalLikeOutModel;
import com.bs.john_li.bsfslotmachine.BSSMModel.CommentListModel;
import com.bs.john_li.bsfslotmachine.BSSMModel.CommonModel;
import com.bs.john_li.bsfslotmachine.BSSMModel.ContentsListModel;
import com.bs.john_li.bsfslotmachine.BSSMModel.ReturnCommentsOutModel;
import com.bs.john_li.bsfslotmachine.BSSMUtils.AliyunOSSUtils;
import com.bs.john_li.bsfslotmachine.BSSMUtils.BSSMCommonUtils;
import com.bs.john_li.bsfslotmachine.BSSMUtils.BSSMConfigtor;
import com.bs.john_li.bsfslotmachine.BSSMUtils.SPUtils;
import com.bs.john_li.bsfslotmachine.BSSMUtils.StatusBarUtil;
import com.bs.john_li.bsfslotmachine.BSSMView.BSSMHeadView;
import com.bs.john_li.bsfslotmachine.BSSMView.CustomExpandableListView;
import com.bs.john_li.bsfslotmachine.BSSMView.FloatingTestButton;
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
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 帖文詳情
 * Created by John_Li on 18/10/2017.
 */

public class ArticleDetialActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView postNameTv, contentsTv, noCommentsTv, moreTv;
    private ImageView postCommentIv, shareIv, deleteIv;
    private CheckBox favoriteCb;
    private AppBarLayout appbar;
    private Toolbar articalToolbar;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private ViewPager mViewPager;
    private CollapsingAdapter mCollapsingAdapter;
    private FloatingTestButton mFab;
    private NestedScrollView mNestedScrollView;
    private CustomExpandableListView contentsLv;
    private List<ImageView> imgList;
    private ContentsListModel.DataBean.ContentsModel mContentsModel;
    private List<CommentListModel.CommentsArrayModel.CommentsModel> mCommentsModelList;
    private CommentsExpandAdapter mCommentsExpandAdapter;
    // 打開方式，0：帖文列表打開，1：個人列表打開
    private int startway = 0;
    // 提交中的dialog
    private AlertDialog mSubmitDialog;
    //提交中的dialog中的動畫
    private RotateAnimation mRotateAnimation;
    //private ImageView mSubmitDialogIv;
    private OSSClient oss;
    private ImageOptions options = new ImageOptions.Builder().setSize(0, 0).setLoadingDrawableId(R.mipmap.img_loading).setFailureDrawableId(R.mipmap.load_img_fail_list).build();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //StatusBarUtil.setColor(this,getResources().getColor(R.color.colorAlpha));
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        setContentView(R.layout.activity_articel_detial);
        initView();
        setListener();
        initData();
    }

    public void initView() {
        contentsTv = (TextView) findViewById(R.id.articel_detial_contents);
        postNameTv = (TextView) findViewById(R.id.articel_creator);
        moreTv = (TextView) findViewById(R.id.article_more_tv);
        postCommentIv = (ImageView) findViewById(R.id.articel_post_comment);
        shareIv = (ImageView) findViewById(R.id.articel_share);
        deleteIv = (ImageView) findViewById(R.id.articel_delete);
        favoriteCb = (CheckBox) findViewById(R.id.articel_favorite);
        appbar = (AppBarLayout) findViewById(R.id.atical_detial_appbar);
        articalToolbar = (Toolbar) findViewById(R.id.atical_detial_toolbar);
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.atical_detial_collapsing_toolbar);
        mViewPager = (ViewPager) findViewById(R.id.atical_detial_vp);
        mNestedScrollView = (NestedScrollView) findViewById(R.id.collapsing_comment_sv);
        contentsLv = (CustomExpandableListView) findViewById(R.id.atical_detial_lv);
        mFab = (FloatingTestButton) findViewById(R.id.atical_detial_fab);
        noCommentsTv = (TextView) findViewById(R.id.atical_detial_no_comments);

        contentsLv.setFocusable(false);
        favoriteCb.setFocusable(true);
        favoriteCb.setFocusableInTouchMode(true);
        favoriteCb.requestFocus();
    }

    public void setListener() {
        postCommentIv.setOnClickListener(this);
        moreTv.setOnClickListener(this);
        shareIv.setOnClickListener(this);
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
                                        if(BSSMCommonUtils.isLoginNow(ArticleDetialActivity.this)) {    // 已经登录
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

        deleteIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(ArticleDetialActivity.this).setTitle("提醒")
                        .setIconAttribute(android.R.attr.alertDialogIcon)
                        .setMessage("確定要刪除這條評論么?")
                        .setPositiveButton("確認", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                callNetDeleteArticle();
                            }})
                        .setNegativeButton("取消", null)
                        .create().show();
            }
        });

        favoriteCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                callNetSubmitLike(isChecked);
            }
        });
    }

    private void callNetSubmitLike(boolean isChecked) {
        RequestParams params = null;
        if (isChecked) {
            params = new RequestParams(BSSMConfigtor.BASE_URL + BSSMConfigtor.ARTICAL_LIKE + "id=" + mContentsModel.getId() + "&token=" + SPUtils.get(this, "UserToken", ""));
        } else {
            params = new RequestParams(BSSMConfigtor.BASE_URL + BSSMConfigtor.ARTICAL_UNLIKE + "id=" + mContentsModel.getId() + "&token=" + SPUtils.get(this, "UserToken", ""));
        }
        params.setAsJsonContent(true);
        params.setConnectTimeout(30 * 1000);
        x.http().request(HttpMethod.POST, params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                ArticalLikeOutModel model = new Gson().fromJson(result.toString(), ArticalLikeOutModel.class);
                if (model.getCode() == 200) {
                    Toast.makeText(ArticleDetialActivity.this, "點讚成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ArticleDetialActivity.this, "點讚失敗," + String.valueOf(model.getMsg()), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if (ex instanceof SocketTimeoutException) {
                    Toast.makeText(ArticleDetialActivity.this, "點讚超時，請重試！", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ArticleDetialActivity.this, "點讚失敗╮(╯▽╰)╭", Toast.LENGTH_SHORT).show();
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

    public void initData() {
        // 獲取帖文資料
        Intent intent = getIntent();
        mContentsModel = new Gson().fromJson(intent.getStringExtra("ContentsModel"), ContentsListModel.DataBean.ContentsModel.class);
        oss = AliyunOSSUtils.initOSS(this);

        startway = intent.getIntExtra("startway", 0);
        // 刪除是否可見
        if (startway != 0) {
            deleteIv.setVisibility(View.VISIBLE);
        }

        // toolbar
        setSupportActionBar(articalToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //  設置標題
        mCollapsingToolbarLayout.setTitle(mContentsModel.getTitle());
        // 設置標題及內容
        contentsTv.setText(mContentsModel.getContents());
        // 發佈者
        postNameTv.setText(mContentsModel.getCreator());
        // 頭部的圖片列表
        imgList = new ArrayList<>();
        // 如果有圖片則顯示，無則不設置
        if (mContentsModel.getCover() != null) {
            if (!mContentsModel.getCover().equals("")) {
                try {
                    //Map<String, String> coverMap = new Gson().fromJson(mContentsModel.getCover(), HashMap.class);
                    String[] coverArr = new Gson().fromJson(mContentsModel.getCover(), String[].class);
                    for (String cover : coverArr) {
                        ImageView iv = new ImageView(this);
                        iv.setBackgroundColor(getResources().getColor(R.color.colorMineGray));
                        iv.setImageResource(R.mipmap.img_loading_list);
                        //AliyunOSSUtils.downloadImg(entry.getValue(), oss, iv, this, R.mipmap.load_img_fail);
                        x.image().bind(iv, cover, options);
                        imgList.add(iv);
                    }

                    mCollapsingAdapter = new CollapsingAdapter(imgList);
                    mViewPager.setAdapter(mCollapsingAdapter);
                } catch (Exception e) {
                    ViewGroup.LayoutParams params = mViewPager.getLayoutParams();
                    params.height = 270;
                    mViewPager.setLayoutParams(params);
                    appbar.setExpanded(false);
                    articalToolbar.setCollapsible(false);
                }
            } else {
                ViewGroup.LayoutParams params = mViewPager.getLayoutParams();
                params.height = 270;
                mViewPager.setLayoutParams(params);
                appbar.setExpanded(false);
                articalToolbar.setCollapsible(false);
            }
        } else {
            ViewGroup.LayoutParams params = mViewPager.getLayoutParams();
            params.height = 270;
            mViewPager.setLayoutParams(params);
            appbar.setExpanded(false);
            articalToolbar.setCollapsible(false);
        }

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ArticleDetialActivity.this, "头像", Toast.LENGTH_SHORT).show();
            }
        });

        // 評論列表
        mCommentsModelList = new ArrayList<>();
        mCommentsExpandAdapter = new CommentsExpandAdapter(this, mCommentsModelList);
        contentsLv.setAdapter(mCommentsExpandAdapter);

        // 獲取評論列表
        callNetGetCommentList();
    }

    /**
     * 獲取評論列表
     */
    private void callNetGetCommentList() {
        String url = BSSMConfigtor.BASE_URL + BSSMConfigtor.GET_COMMENTS + mContentsModel.getId() + "&nextId=";
        if(mCommentsModelList.size() > 0){
            url = url + mCommentsModelList.get(mCommentsModelList.size() - 1).getId();
        }
        RequestParams params = new RequestParams(url);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                CommentListModel model = new Gson().fromJson(result, CommentListModel.class);
                if (model.getCode() == 200) {
                    mCommentsModelList.addAll(model.getData().getComments());
                } else {
                    Toast.makeText(ArticleDetialActivity.this, "獲取評論失敗" + String.valueOf(model.getMsg()), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if (ex instanceof SocketTimeoutException) {
                    Toast.makeText(ArticleDetialActivity.this, "獲取評論超時，請重試", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ArticleDetialActivity.this, "獲取評論失敗╮(╯▽╰)╭", Toast.LENGTH_SHORT).show();
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
        mCommentsExpandAdapter.notifyDataSetChanged();
        expandListView();
        if (mCommentsModelList.size() > 0) {
            contentsLv.setVisibility(View.VISIBLE);
            moreTv.setVisibility(View.VISIBLE);
            noCommentsTv.setVisibility(View.GONE);
        } else {
            contentsLv.setVisibility(View.GONE);
            moreTv.setVisibility(View.GONE);
            noCommentsTv.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.head_left:
                finish();
                break;
            case R.id.articel_post_comment: // 发表评论
                showCommentDialog();
                break;
            case R.id.article_more_tv: // 獲取更多評論
                Intent intent = new Intent(this, CommentsListActivity.class);
                intent.putExtra("ContentsModel", new Gson().toJson(mContentsModel));
                startActivity(intent);
                break;
            case R.id.articel_share: // 獲取更多評論
                Toast.makeText(this, "分享", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    /**
     * 张开所有List
     */
    private void expandListView() {
        for (int i = 0; i < mCommentsModelList.size(); i++) {
            String content = mCommentsModelList.get(i).getContent();
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
        showLoadingDialog();
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
        params.setConnectTimeout(30 * 1000);
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
                    List<CommentListModel.CommentsArrayModel.CommentsModel.RepliesBean> replisList = new ArrayList<CommentListModel.CommentsArrayModel.CommentsModel.RepliesBean>();
                    commentsModel.setReplies(replisList);
                    if (mCommentsModelList.size() != 0) {
                        mCommentsModelList.add(0, commentsModel);
                    } else {
                        mCommentsModelList.add(commentsModel);
                    }
                    mCommentsExpandAdapter.notifyDataSetChanged();
                    expandListView();
                } else {
                    Toast.makeText(ArticleDetialActivity.this, "評論失敗╮(╯▽╰)╭" + String.valueOf(model.getMsg()), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if (ex instanceof SocketTimeoutException) {
                    Toast.makeText(ArticleDetialActivity.this, "評論失敗超時，請重試！", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ArticleDetialActivity.this, "評論失敗╮(╯▽╰)╭", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(ArticleDetialActivity.this, "回復失敗╮(╯▽╰)╭", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(ArticleDetialActivity.this, "回復失敗╮(╯▽╰)╭", Toast.LENGTH_SHORT).show();
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
     * 刪除帖文
     */
    private void callNetDeleteArticle() {
        showLoadingDialog();
        RequestParams params = new RequestParams(BSSMConfigtor.BASE_URL + BSSMConfigtor.DELETE_COTENTS + mContentsModel.getId() + "&token=" + SPUtils.get(this, "UserToken", ""));
        params.setConnectTimeout(30 * 1000);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                ContentsListModel model = new Gson().fromJson(result, ContentsListModel.class);
                if (model.getCode() == 200) {
                    setResult(RESULT_OK);
                    finish();
                } else {
                    Toast.makeText(ArticleDetialActivity.this, "帖文刪除失敗╮(╯▽╰)╭", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(ArticleDetialActivity.this, "帖文刪除失敗╮(╯▽╰)╭", Toast.LENGTH_SHORT).show();
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
        //rotateAnimation.setDuration(2000);
        mSubmitDialogIv.startAnimation(mRotateAnimation);
        mSubmitDialog.show();
    }

    /**
     * 關閉加載中的dialog
     */
    private void closeLoadingDialog() {
        //mSubmitDialogIv.clearAnimation();
        mRotateAnimation.cancel();
        mSubmitDialog.dismiss();
    }
}
