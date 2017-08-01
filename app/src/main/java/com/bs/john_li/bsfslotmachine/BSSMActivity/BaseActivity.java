package com.bs.john_li.bsfslotmachine.BSSMActivity;

import android.support.annotation.LayoutRes;
import android.support.v4.app.FragmentActivity;

import com.bs.john_li.bsfslotmachine.BSSMUtils.StatusBarUtil;
import com.bs.john_li.bsfslotmachine.R;

/**
 * Created by John_Li on 27/7/2017.
 */

public abstract class BaseActivity extends FragmentActivity {
    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        StatusBarUtil.setColor(this,getResources().getColor(R.color.colorSkyBlue));
    }

    public abstract void initView();
    public abstract void setListener();
    public abstract void initData();
}
