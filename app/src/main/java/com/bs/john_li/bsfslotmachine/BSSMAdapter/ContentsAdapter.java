package com.bs.john_li.bsfslotmachine.BSSMAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bs.john_li.bsfslotmachine.BSSMModel.CarModel;
import com.bs.john_li.bsfslotmachine.BSSMModel.ContentsListModel;
import com.bs.john_li.bsfslotmachine.R;

import java.util.List;

/**
 * Created by John_Li on 9/8/2017.
 */

public class ContentsAdapter extends BaseAdapter {
    private List<ContentsListModel.DataBean.ContentsModel> contentsList;
    private LayoutInflater inflater;
    private Context mContext;
    public ContentsAdapter(Context context, List<ContentsListModel.DataBean.ContentsModel> contentsList) {
        this.contentsList = contentsList;
        this.mContext = context;
        inflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return contentsList.size();
    }

    @Override
    public Object getItem(int i) {
        return contentsList.get(i);
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
            convertView = inflater.inflate(R.layout.item_contents, null);
            holder.contentsIv = convertView.findViewById(R.id.item_contents_iv);
            holder.contentsTitle = convertView.findViewById(R.id.item_contents_title);
            holder.contentsPostID = convertView.findViewById(R.id.item_contents_postid);
            holder.contentsComment = convertView.findViewById(R.id.item_contents_comments);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.contentsTitle.setText(contentsList.get(i).getTitle());
        holder.contentsPostID.setText("發佈者：" + contentsList.get(i).getCreator());
        holder.contentsComment.setText(Integer.toString(contentsList.get(i).getCommentcount()) + "條評論");
        return convertView;
    }

    class ViewHolder {
        public ImageView contentsIv;
        public TextView contentsTitle;
        public TextView contentsPostID;
        public TextView contentsComment;
    }

    public void refreshListView(List<ContentsListModel.DataBean.ContentsModel> newList){
        this.contentsList = newList;
        notifyDataSetChanged();
    }
}
