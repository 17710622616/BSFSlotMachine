package com.cocacola.john_li.bsfmerchantsversionapp.Utils;

import android.app.Application;

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
        //AidlUtil.getInstance().connectPrinterService(this);
    }
}
