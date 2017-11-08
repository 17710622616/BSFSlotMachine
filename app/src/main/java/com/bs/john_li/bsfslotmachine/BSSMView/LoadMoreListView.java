package com.bs.john_li.bsfslotmachine.BSSMView;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListView;

import com.bs.john_li.bsfslotmachine.BSSMAdapter.ContentsAdapter;
import com.bs.john_li.bsfslotmachine.BSSMUtils.OnLoadMoreScrollListener;

/**
 * Created by John_Li on 7/11/2017.
 */

public class LoadMoreListView extends ListView {
    private OnLoadMoreScrollListener mScrollListener;

    public LoadMoreListView(Context context) {
        super(context);
    }

    public LoadMoreListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LoadMoreListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setFooterView(View footerView) {
        this.mScrollListener = new OnLoadMoreScrollListener(this, footerView);
        setOnScrollListener(mScrollListener);
    }

    public void setFooterView(View footerView, OnLoadMoreScrollListener.OnLoadMoreStateListener onLoadMoreStateListener) {
        setFooterView(footerView);
        this.mScrollListener.setOnLoadMoreStateListener(onLoadMoreStateListener);
    }

    public void setOnLoadMoreStateListener(OnLoadMoreScrollListener.OnLoadMoreStateListener onLoadMoreStateListener) {
        if (mScrollListener != null) {
            this.mScrollListener.setOnLoadMoreStateListener(onLoadMoreStateListener);
        }
    }

    public void setEnd(boolean isEnd) {
        if (mScrollListener != null) {
            mScrollListener.setEnd(isEnd);
        }
    }
}
