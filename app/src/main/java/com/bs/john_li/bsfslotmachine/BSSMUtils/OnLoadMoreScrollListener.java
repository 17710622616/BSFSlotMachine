package com.bs.john_li.bsfslotmachine.BSSMUtils;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import com.bs.john_li.bsfslotmachine.BSSMAdapter.ContentsAdapter;

/**
 * Created by John_Li on 7/11/2017.
 */

public class OnLoadMoreScrollListener implements AbsListView.OnScrollListener {
    private int lastItemIndex = Integer.MIN_VALUE;// 当前ListView中最后一个Item的索引

    private final ListView mListView;
    private final View mFootView;

    private boolean isEnd = false;
    //private final Object mLock = new Object();

    private OnLoadMoreStateListener mOnLoadMoreStateListener;

    public OnLoadMoreScrollListener(ListView listView, View footView) {
        this.mListView = listView;
        this.mFootView = footView;
        listView.removeFooterView(footView);
        listView.addFooterView(footView);
    }

    public void setOnLoadMoreStateListener(OnLoadMoreStateListener onLoadMoreStateListener) {
        mOnLoadMoreStateListener = onLoadMoreStateListener;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (mFootView == null) return;
        if (!(view instanceof ListView)) return;
        final ListView listView = (ListView) view;

        // &&mFooter!=null&&mFooter.footView!=null&&mFooter.footView.hasWindowFocus()
//        Log.d("getItemCount():", "" + getItemCount(listView));
//        Log.d("lastItemIndex:", "" + lastItemIndex);
        //synchronized (mLock) {
        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
                && lastItemIndex == getItemCount(listView) - 1) {
            if (mOnLoadMoreStateListener != null) {
                mOnLoadMoreStateListener.onLoading(mFootView);
            }
        }
        //}
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
        if (mFootView == null) return;
        if (!(view instanceof ListView)) return;
        final ListView listView = (ListView) view;

        final int footerViewsCount = listView.getFooterViewsCount();
        final int headerViewsCount = listView.getHeaderViewsCount();

        lastItemIndex = firstVisibleItem + visibleItemCount - 1
                - footerViewsCount - headerViewsCount;
        if (footerViewsCount != 0) {
            final int visibleListItemCount = visibleItemCount - footerViewsCount;

            if ((visibleListItemCount == 0 && isFooterViewVisible(listView, mFootView))
                    || (visibleListItemCount >= getItemCount(listView)
                    && isLastItemVisible(listView, mFootView) && isFirstItemVisible(listView))) {
                lastItemIndex = Integer.MIN_VALUE;
                if (visibleListItemCount > 0) {
                    mListView.removeFooterView(mFootView);
                }
            }
        } else {
            if (visibleItemCount != 0 && visibleItemCount <= listView.getCount()
                    && !isLastItemVisible(listView, mFootView)) {
                mListView.addFooterView(mFootView);
            }
        }

        if (mOnLoadMoreStateListener != null) {
            // 判断footerview是否显示
            if (!isFooterViewVisible(listView, mFootView)) {
                if (isEnd) {
                    mOnLoadMoreStateListener.onEnd(mFootView);
                } else {
                    mOnLoadMoreStateListener.onNormal(mFootView);
                }
            }
        }
    }

    private int getItemCount(ListView listView) {
        return listView.getCount() - listView.getFooterViewsCount() - listView.getHeaderViewsCount();
    }

    private boolean isFooterViewVisible(ListView listView, View footView) {
        return (!(listView.getFooterViewsCount() == 0 || footView == null
                || footView.getTop() == 0)) && footView.getTop() <= listView.getBottom();
    }

    private boolean isLastItemVisible(ListView listView, View footView) {
        final int childCount = listView.getChildCount();
        if (childCount == 0) {
            return true;
        }
        View lastVisibleChild = listView.getChildAt(childCount - 1);
        if (lastVisibleChild == null) {
            return true;
        }
        if (lastVisibleChild.equals(footView)) {
            lastVisibleChild = listView.getChildAt(childCount - 2);
        }
        return lastVisibleChild == null || lastVisibleChild.getBottom() <= listView.getBottom();
    }

    private boolean isFirstItemVisible(ListView listView) {
        if (listView.getChildCount() == 0) {
            return true;
        }
        View firstVisibleChild = listView.getChildAt(0);
        return firstVisibleChild == null || firstVisibleChild.getTop() >= listView.getTop();
    }

    public void setEnd(boolean isEnd) {
        this.isEnd = isEnd;
        if (mOnLoadMoreStateListener != null) {
            if (isEnd) {
                mOnLoadMoreStateListener.onEnd(mFootView);
            } else {
                mOnLoadMoreStateListener.onNormal(mFootView);
            }
        }
    }

    public interface OnLoadMoreStateListener {
        void onNormal(View footView);

        void onLoading(View footView);

        void onEnd(View footView);
    }
}