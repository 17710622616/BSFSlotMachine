package com.bs.john_li.bsfslotmachine.BSSMView;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bs.john_li.bsfslotmachine.R;

/**
 * 头部组合控件
 * Created by John_Li on 28/7/2017.
 */

public class BSSMHeadView extends LinearLayout {
    private ImageView leftIv,rightIv;
    private TextView leftTv,titleTv,rightTv;
    private Context mContext;
    public BSSMHeadView(Context context) {
        super(context);
        init(context);
    }

    public BSSMHeadView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BSSMHeadView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.layout_head, this);
        leftIv = (ImageView) this.findViewById(R.id.head_left);
        titleTv = (TextView) this.findViewById(R.id.head_title);
        rightIv = (ImageView) this.findViewById(R.id.head_right);
        rightTv = (TextView) this.findViewById(R.id.head_right_tv);
        leftIv.setVisibility(INVISIBLE);
        titleTv.setVisibility(INVISIBLE);
        rightIv.setVisibility(INVISIBLE);
        rightTv.setVisibility(GONE);
    }

    /**
         * 标题
         */
    public void setTitle(String title){
        titleTv.setVisibility(VISIBLE);
        titleTv.setText(title);
    }

    /**
         * 左边按钮
         */
    public void setLeft(OnClickListener listener){
        leftIv.setVisibility(VISIBLE);
        leftIv.setOnClickListener(listener);
    }

    /**
         * 左边按钮有图片
         */
    public void setLeft(int resId,OnClickListener listener){
        leftIv.setVisibility(VISIBLE);
        //leftIv.setBackgroundResource(resId);
        leftIv.setImageResource(resId);
        leftIv.setOnClickListener(listener);
    }

    /**
         * 右边按钮有图片
         */
    public void setRight(int resId,OnClickListener listener){
        rightIv.setVisibility(VISIBLE);
        rightIv.setImageResource(resId);
        rightIv.setOnClickListener(listener);
    }

    /**
         * 右边按钮文字
         */
    public void setRightText(String str,OnClickListener listener){
        rightIv.setVisibility(GONE);
        rightTv.setVisibility(VISIBLE);
        rightTv.setText(str);
        rightTv.setOnClickListener(listener);
    }
}
