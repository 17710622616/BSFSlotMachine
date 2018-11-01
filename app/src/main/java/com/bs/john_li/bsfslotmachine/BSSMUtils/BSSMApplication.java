package com.bs.john_li.bsfslotmachine.BSSMUtils;

import android.app.Application;
import android.support.multidex.MultiDexApplication;

import com.mob.MobSDK;

import org.xutils.x;

/**
 * Created by John on 12/9/2017.
 */

public class BSSMApplication extends MultiDexApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        // 初始化xutils
        x.Ext.init(this);
        // 初始化sharesdk
        //MobSDK.init(this, BSSMConfigtor.SHARESDK_APP_KEY, BSSMConfigtor.SHARESDK_APP_SECRET);
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(getApplicationContext());
    }
}
