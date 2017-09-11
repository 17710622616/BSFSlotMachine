package com.bs.john_li.bsfslotmachine.BSSMUtils;

import android.app.Application;

import org.xutils.x;

/**
 * Created by John on 12/9/2017.
 */

public class BSSMApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
    }
}
