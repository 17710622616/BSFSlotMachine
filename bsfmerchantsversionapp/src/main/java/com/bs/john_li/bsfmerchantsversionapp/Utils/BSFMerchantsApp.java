package com.bs.john_li.bsfmerchantsversionapp.Utils;

import android.app.Application;

import org.xutils.x;

/**
 * Created by John_Li on 17/11/2018.
 */

public class BSFMerchantsApp extends Application {
    private boolean isAidl;

    public boolean isAidl() {
        return isAidl;
    }

    public void setAidl(boolean aidl) {
        isAidl = aidl;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        isAidl = true;
        x.Ext.init(this);
        //AidlUtil.getInstance().connectPrinterService(this);
    }
}
