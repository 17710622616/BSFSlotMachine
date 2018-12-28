package com.bs.john_li.gmaptest;

import android.app.Application;

import org.xutils.x;

/**
 * Created by John_Li on 2/8/2017.
 */

public class BSSMApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
    }
}
