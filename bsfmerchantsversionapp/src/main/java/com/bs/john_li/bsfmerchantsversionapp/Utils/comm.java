package com.bs.john_li.bsfmerchantsversionapp.Utils;

import android.os.Environment;

import java.io.File;

/**
 * Created by John_Li on 17/1/2019.
 */

public class comm {
    public static File getPathFile(String path){
        String apkName = path.substring(path.lastIndexOf("/"));
        File outputFile = new File(Environment.getExternalStoragePublicDirectory (Environment.DIRECTORY_DOWNLOADS), apkName);
        return outputFile;
    }

    public static void rmoveFile(String path){
        File file = getPathFile(path);
        file.delete();
    }
}
