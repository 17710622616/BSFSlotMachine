package com.bs.john_li.bsfslotmachine.BSSMAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.bs.john_li.bsfslotmachine.BSSMModel.CommentListModel;
import com.bs.john_li.bsfslotmachine.BSSMUtils.BSSMCommonUtils;
import com.bs.john_li.bsfslotmachine.R;

import java.util.List;

/**
 * Created by John_Li on 26/10/2017.
 */

public class CommentsExpandAdapter extends BaseExpandableListAdapter {
    public Context mContext;
    private List<CommentListModel.CommentsArrayModel.CommentsModel> commnettsList;

    public  CommentsExpandAdapter (Context context, List<CommentListModel.CommentsArrayModel.CommentsModel> commnettsList) {
        this.commnettsList = commnettsList;
        this.mContext = context;
    }

    @Override
    public int getGroupCount() {
        return commnettsList.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return commnettsList.get(i).getReplies().size();
    }

    @Override
    public Object getGroup(int i) {
        return null;
    }

    @Override
    public Object getChild(int i, int i1) {
        return commnettsList.get(i).getReplies().get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        GroupHolder holder = null;
        if (view == null) {
            view = View.inflate(mContext, R.layout.item_comments, null);
            holder = new GroupHolder();
            holder.item_comments_time = view.findViewById(R.id.item_comments_time);
            holder.item_commentor_tv = view.findViewById(R.id.item_commentor_tv);
            holder.item_comments_tv = view.findViewById(R.id.item_comments_tv);
            view.setTag(holder);
        } else {
            holder = (GroupHolder) view.getTag();
        }

         holder.item_commentor_tv.setText(commnettsList.get(i).getCreator() + ":");
        holder.item_comments_tv.setText(commnettsList.get(i).getContent());
        holder.item_comments_time.setText(BSSMCommonUtils.stampToDate(String.valueOf(commnettsList.get(i).getCreatetime())));
        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        ChildHolder holder = null;
        if (view == null) {
            view = View.inflate(mContext, R.layout.item_comment_replys, null);
            holder = new ChildHolder();
            holder.item_comments_time = view.findViewById(R.id.item_relpy_comments_time);
            holder.item_commentor_tv = view.findViewById(R.id.item_relpy_commentor_tv);
            holder.item_comments_tv = view.findViewById(R.id.item_relpy_comments_tv);
            view.setTag(holder);
        } else {
            holder = (ChildHolder) view.getTag();
        }

        holder.item_commentor_tv.setText(commnettsList.get(i).getReplies().get(i1).getCreator() + ":");
        holder.item_comments_tv.setText(commnettsList.get(i).getReplies().get(i1).getContent());
        holder.item_comments_time.setText(BSSMCommonUtils.stampToDate(String.valueOf(commnettsList.get(i).getReplies().get(i1).getCreatetime())));
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    public class GroupHolder {
        public TextView item_comments_time;
        public TextView item_commentor_tv;
        public TextView item_comments_tv;
    }

    public class ChildHolder {
        public TextView item_comments_time;
        public TextView item_commentor_tv;
        public TextView item_comments_tv;
    }

    public void refreshData(List<CommentListModel.CommentsArrayModel.CommentsModel> commnettsList) {
        this.commnettsList = commnettsList;
        notifyDataSetChanged();
    }
}
