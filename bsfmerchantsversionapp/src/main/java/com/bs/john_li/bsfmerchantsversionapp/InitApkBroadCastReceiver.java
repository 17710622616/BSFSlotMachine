package com.bs.john_li.bsfmerchantsversionapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.bs.john_li.bsfmerchantsversionapp.Utils.comm;

/**
 * Created by John_Li on 17/1/2019.
 */

public class InitApkBroadCastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_PACKAGE_ADDED.equals(intent.getAction())) {
            comm.rmoveFile("/data/user/0/com.android.providers.downloads/cache/MerchantSide-1.apk");
            //Toast.makeText(context , "监听到系统广播添加" , Toast.LENGTH_LONG).show();
        }

        if (Intent.ACTION_PACKAGE_REMOVED.equals(intent.getAction())) {
            comm.rmoveFile("/data/user/0/com.android.providers.downloads/cache/MerchantSide-1.apk");
            //Toast.makeText(context , "监听到系统广播移除" , Toast.LENGTH_LONG).show();
            //System.out.println("");
        }

        if (Intent.ACTION_PACKAGE_REPLACED.equals(intent.getAction())) {
            comm.rmoveFile("/data/user/0/com.android.providers.downloads/cache/MerchantSide-1.apk");
            //Toast.makeText(context , "监听到系统广播替换" , Toast.LENGTH_LONG).show();
        }
    }
}
