package com.bs.john_li.bsfslotmachine.BSSMFragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bs.john_li.bsfslotmachine.BSSMView.BSSMHeadView;
import com.bs.john_li.bsfslotmachine.R;

/**
 * 首页停车的碎片
 * Created by John_Li on 28/7/2017.
 */

public class ForumFragment extends BaseFragment implements View.OnClickListener {
    public static String TAG = ForumFragment.class.getName();
    /*private Activity mActivity;
    private AppCompatActivity mAppCompatActivity;
    private Toolbar forumToolBar;*/
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
        //mActivity = getActivity();
        initView();
        setListenter();
        initData();
        return forumView;
    }

    @Override
    public void initView() {
        /*forumToolBar = (Toolbar)forumView.findViewById(R.id.forum_toolbar);
        forumToolBar.inflateMenu(R.menu.menu_forum);*/
        forumHeadView = forumView.findViewById(R.id.forum_head);
    }

    @Override
    public void setListenter() {
        /*forumToolBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return false;
            }
        });*/
    }

    @Override
    public void initData() {
        forumHeadView.setTitle("社區");
        forumHeadView.setLeft(R.mipmap.operation_invitation, this);
        forumHeadView.setRight(R.mipmap.push_invitation, this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.head_left:
                Toast.makeText(getActivity(),"管理自己的帖子",Toast.LENGTH_SHORT).show();
                break;
            case R.id.head_right:
                Toast.makeText(getActivity(),"發佈帖子",Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
