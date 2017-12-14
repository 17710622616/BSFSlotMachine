package com.bs.john_li.bsfslotmachine.BSSMAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bs.john_li.bsfslotmachine.BSSMModel.ContentsListModel;
import com.bs.john_li.bsfslotmachine.R;

import java.util.List;

/**
 * 个人博文列表刷新
 * Created by John_Li on 28/11/2017.
 */

public class SmartOwnRefreshAdapter extends RecyclerView.Adapter implements View.OnClickListener {
    private List<ContentsListModel.DataBean.ContentsModel> list;
    private final Context mContext;
    private OnItemClickListener mOnitemClickListener = null;

    public SmartOwnRefreshAdapter(Context context, List<ContentsListModel.DataBean.ContentsModel> list) {
        this.list = list;
        mContext = context;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_contents, parent, false);
        SmartRefreshViewHolder vh = new SmartRefreshViewHolder(view);
        view.setOnClickListener(this);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((SmartRefreshViewHolder)holder).contentsTitle.setText(list.get(position).getTitle());
        ((SmartRefreshViewHolder)holder).contentsPostID.setText("發佈者：" + list.get(position).getCreator());
        ((SmartRefreshViewHolder)holder).contentsComment.setText(Integer.toString(list.get(position).getCommentcount()) + "條評論");
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onClick(View v) {
        if (mOnitemClickListener != null) {
            mOnitemClickListener.onItemClick(v, (int)v.getTag());
        }
    }

    public class SmartRefreshViewHolder extends RecyclerView.ViewHolder {
        public ImageView contentsIv;
        public TextView contentsTitle;
        public TextView contentsPostID;
        public TextView contentsComment;
        public SmartRefreshViewHolder(View view){
            super(view);
            contentsIv = (ImageView) view.findViewById(R.id.item_contents_iv);
            contentsTitle = (TextView) view.findViewById(R.id.item_contents_title);
            contentsPostID = (TextView) view.findViewById(R.id.item_contents_postid);
            contentsComment = (TextView) view.findViewById(R.id.item_contents_comments);
        }
    }

    public void setOnItemClickListenr(OnItemClickListener listenr) {
        this.mOnitemClickListener = listenr;
    }

    public static interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
}
