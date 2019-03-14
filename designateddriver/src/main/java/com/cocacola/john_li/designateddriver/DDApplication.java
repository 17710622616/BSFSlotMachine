package com.cocacola.john_li.designateddriver;

import android.app.Application;

import org.xutils.x;

/**
 * Created by John_Li on 14/3/2019.
 */

public class DDApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
    }
}
