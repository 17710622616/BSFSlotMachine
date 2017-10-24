package com.bs.john_li.bsfslotmachine.BSSMAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bs.john_li.bsfslotmachine.BSSMModel.CommentListModel;
import com.bs.john_li.bsfslotmachine.BSSMModel.ContentsListModel;
import com.bs.john_li.bsfslotmachine.R;

import java.util.List;

/**
 * Created by John_Li on 9/8/2017.
 */

public class CommentsAdapter extends BaseAdapter {
    private List<CommentListModel.CommentsArrayModel.CommentsModel> commnettsList;
    private LayoutInflater inflater;
    private Context mContext;
    public CommentsAdapter(Context context, List<CommentListModel.CommentsArrayModel.CommentsModel> commnettsList) {
        this.commnettsList = commnettsList;
        this.mContext = context;
        inflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return commnettsList.size();
    }

    @Override
    public Object getItem(int i) {
        return commnettsList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View convertView = view;
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_comments, null);
            holder.item_comments_iv = convertView.findViewById(R.id.item_comments_iv);
            holder.item_commentor_tv = convertView.findViewById(R.id.item_commentor_tv);
            holder.item_comments_tv = convertView.findViewById(R.id.item_comments_tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.item_commentor_tv.setText(commnettsList.get(i).getCreator());
        holder.item_comments_tv.setText(commnettsList.get(i).getContent());
        return convertView;
    }

    class ViewHolder {
        public ImageView item_comments_iv;
        public TextView item_commentor_tv;
        public TextView item_comments_tv;
    }

    public void refreshListView(List<CommentListModel.CommentsArrayModel.CommentsModel> newList){
        this.commnettsList = newList;
        notifyDataSetChanged();
    }
}
