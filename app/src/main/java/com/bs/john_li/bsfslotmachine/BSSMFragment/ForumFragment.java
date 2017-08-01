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

import com.bs.john_li.bsfslotmachine.R;

/**
 * 首页停车的碎片
 * Created by John_Li on 28/7/2017.
 */

public class ForumFragment extends BaseFragment {
    public static String TAG = ForumFragment.class.getName();
    private Activity mActivity;
    private AppCompatActivity mAppCompatActivity;
    private View forumView;
    private Toolbar forumToolBar;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        forumView = inflater.inflate(R.layout.fragment_forum, null);
        mActivity = getActivity();
        initView();
        setListenter();
        initData();
        return forumView;
    }

    @Override
    public void initView() {
        forumToolBar = (Toolbar)forumView.findViewById(R.id.forum_toolbar);
        forumToolBar.inflateMenu(R.menu.menu_forum);
    }

    @Override
    public void setListenter() {
        forumToolBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return false;
            }
        });
    }

    @Override
    public void initData() {
        forumToolBar.setTitle("社區");
        forumToolBar.setTitleTextColor(getResources().getColor(R.color.colorWight));
    }
}
