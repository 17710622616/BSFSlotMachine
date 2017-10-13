package com.bs.john_li.bsfslotmachine.BSSMView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.GridView;

/**
 * Created by John_Li on 13/10/2017.
 */

public class NoScrollGridView extends GridView {
    public NoScrollGridView(Context context, AttributeSet attrs, int defStyle) {

        super(context, attrs, defStyle);

        // TODO Auto-generated constructor stub

    }


    public NoScrollGridView(Context context, AttributeSet attrs) {

        super(context, attrs);

        // TODO Auto-generated constructor stub

    }


    public NoScrollGridView(Context context) {

        super(context);

        // TODO Auto-generated constructor stub

    }


//    @Override
//
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//
//        //下面这句话是关键
//
//        if (ev.getAction()==MotionEvent.ACTION_MOVE) {
//
//            return true;
//
//        }
//
//        return super.dispatchTouchEvent(ev);//也有所不同哦
//
//    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int expandSpec = View.MeasureSpec.makeMeasureSpec(
                Integer.MAX_VALUE >> 2, View.MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
