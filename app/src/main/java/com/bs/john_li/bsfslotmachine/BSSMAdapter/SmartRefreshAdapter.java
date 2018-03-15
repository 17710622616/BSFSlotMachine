package com.bs.john_li.bsfslotmachine.BSSMAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.sdk.android.oss.OSSClient;
import com.bs.john_li.bsfslotmachine.BSSMModel.ContentsListModel;
import com.bs.john_li.bsfslotmachine.BSSMUtils.AliyunOSSUtils;
import com.bs.john_li.bsfslotmachine.R;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by John_Li on 28/11/2017.
 */

public class SmartRefreshAdapter extends RecyclerView.Adapter implements View.OnClickListener {
    private List<ContentsListModel.DataBean.ContentsModel> list;
    private final Context mContext;
    private OSSClient oss;
    private OnItemClickListener mOnitemClickListener = null;

    public SmartRefreshAdapter(Context context, List<ContentsListModel.DataBean.ContentsModel> list, OSSClient oss) {
        this.list = list;
        mContext = context;
        this.oss = oss;
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
        String cover = list.get(position).getCover();
        try {
            Map<String, String> coverMap = new Gson().fromJson(cover, HashMap.class);
            cover = coverMap.get("mainPic");
        } catch (Exception e) {
            cover = "";
        }
        AliyunOSSUtils.downloadImg(cover, oss, ((SmartRefreshViewHolder)holder).contentsIv, mContext, R.mipmap.load_img_fail_list);
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
