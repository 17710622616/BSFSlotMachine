package com.bs.john_li.bsfslotmachine.BSSMView;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Created by John_Li on 19/12/2017.
 */

public class ShowTiemTextView extends TextView implements Runnable {
    private EndPayTimeCallback mEndPayTimeCallback;
    private boolean run = false; //觉得是否执行run方法
    private int time;
    public ShowTiemTextView(Context context) {
        super(context);
    }

    public ShowTiemTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public void setTime(int time){  //设定初始值
        this.time = time;
    }
    public boolean isRun(){
        return run;
    }
    public void beginRun(){
        this.run = true;
        run();
    }
    public void stopRun(){
        this.run = false;
        mEndPayTimeCallback.endPayTime();
    }

    @Override
    public void run() {
        if (run){
            ComputeTime();
            DecimalFormat df1 = new DecimalFormat("00");
            this.setText(df1.format(time / 60) + ":" + df1.format(time % 60));
            postDelayed(this, 1000);
        }else{
            removeCallbacks(this);
        }
    }

    private void ComputeTime(){
        time--;
        if (time==0)
            stopRun();
    }

    public void setmEndPayTimeCallback(EndPayTimeCallback callback){
        this.mEndPayTimeCallback = callback;
    }

    /**
     * 倒計時結束仍未付款
     */
    public interface EndPayTimeCallback{
        void endPayTime();
    }
}
