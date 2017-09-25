package com.bs.john_li.bsfslotmachine.BSSMUtils;

import android.content.Context;
import android.widget.Toast;

import com.bs.john_li.bsfslotmachine.R;

/**
 * Created by John on 12/9/2017.
 */

public class BSSMCommonUtils {
    public static boolean isLoginNow(Context c){
        String userToken = (String) SPUtils.get(c, "UserToken", "");
        if (userToken == null) {
            if (userToken.equals("")){
                Toast.makeText(c, c.getString(R.string.not_login), Toast.LENGTH_SHORT).show();
            }
            Toast.makeText(c,  c.getString(R.string.not_login), Toast.LENGTH_SHORT).show();
        }
        return true;
    }
}
