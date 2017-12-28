package com.bs.john_li.bsfslotmachine.BSSMUtils;

import android.content.Context;
import android.util.Log;

import com.alibaba.sdk.android.oss.common.auth.OSSFederationCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSFederationToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2015/12/9 0009.
 * 重载OSSFederationCredentialProvider生成自己的获取STS的功能
 */
public class STSGetter extends OSSFederationCredentialProvider {

    private String stsServer = BSSMConfigtor.BASE_URL + "/oss/stsAuthorize";
    private Context mContext;

    public STSGetter(Context context) {
        stsServer = BSSMConfigtor.BASE_URL + "/oss/stsAuthorize";
        mContext = context;
    }

    public STSGetter(Context context, String stsServer) {
        this.stsServer = stsServer;
        mContext = context;
    }

    public OSSFederationToken getFederationToken() {
        String stsJson;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(stsServer+"?token=" + SPUtils.get(mContext.getApplicationContext(), "UserToken", "")).build();
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                stsJson = response.body().string();
            } else {
                throw new IOException("Unexpected code " + response);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
            Log.e("GetSTSTokenFail", e.toString());
            return null;
        }

        try {
            JSONObject jsonObjs = new JSONObject(stsJson);
            String ak = jsonObjs.getString("AccessKeyId");
            String sk = jsonObjs.getString("AccessKeySecret");
            String token = jsonObjs.getString("SecurityToken");
            String expiration = jsonObjs.getString("Expiration");
            return new OSSFederationToken(ak, sk, token, expiration);
        }
        catch (JSONException e) {
            Log.e("GetSTSTokenFail", e.toString());
            e.printStackTrace();
            return null;
        }
    }

}
