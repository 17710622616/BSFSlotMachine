package com.bs.john_li.bsfslotmachine.BSSMActivity.Mine;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.JsPromptResult;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.bs.john_li.bsfslotmachine.BSSMActivity.BaseActivity;
import com.bs.john_li.bsfslotmachine.BSSMModel.ShareModel;
import com.bs.john_li.bsfslotmachine.BSSMUtils.BSSMCommonUtils;
import com.bs.john_li.bsfslotmachine.BSSMUtils.BSSMConfigtor;
import com.bs.john_li.bsfslotmachine.BSSMUtils.SPUtils;
import com.bs.john_li.bsfslotmachine.BSSMView.BSSMHeadView;
import com.bs.john_li.bsfslotmachine.R;
import com.github.lzyzsd.jsbridge.BridgeHandler;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.BridgeWebViewClient;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.util.HashMap;
import java.util.Set;


/**
 * Created by John_Li on 12/12/2018.
 */

public class ShareActivity extends BaseActivity implements View.OnClickListener{
    private BSSMHeadView headView;
    private BridgeWebView mWebView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        initView();
        setListener();
        initData();
    }

    @Override
    public void initView() {
        mWebView = (BridgeWebView) findViewById(R.id.share_wv);
        headView = (BSSMHeadView) findViewById(R.id.share_head);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            headView.setHeadHight();
        }
    }

    @Override
    public void setListener() {
        headView.setLeft(this);
        headView.setTitle("分享有獎");
        //支持javascript
        WebSettings webSettings = mWebView.getSettings();
        //设置与Js交互的权限
        webSettings.setJavaScriptEnabled(true);
        // 设置允许JS弹窗
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        mWebView.loadUrl(BSSMConfigtor.BASE_URL + BSSMConfigtor.GET_SHARE_WEB + SPUtils.get(ShareActivity.this, "UserToken", ""));
        mWebView.registerHandler("shareAction", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                final ShareModel model = new Gson().fromJson(data, ShareModel.class);
                String fileName = "IMG_5002.PNG";
                String dirPath = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "BSSMPictures").getPath();
                File filesDir = getExternalFilesDir(null);
                //存到本地的绝对路径
                final String filePath = dirPath + "/" + fileName;
                File file = new File(filePath);
                //如果不存在
                if (!file.exists()) {
                    File dirFile = new File(dirPath);
                    if (dirFile.exists()) {
                        //创建
                        dirFile.mkdirs();
                    }
                    RequestParams entity = new RequestParams("https://test-pic-666.oss-cn-hongkong.aliyuncs.com/" + fileName);
                    entity.setSaveFilePath(filePath);
                    x.http().get(entity, new Callback.CommonCallback<File>() {
                        @Override
                        public void onSuccess(File result) {
                            //BSSMCommonUtils.openShare(getActivity(), "掌泊寶", "http://www.bsmaco.icoc.bz/", "掌泊寶官網", filePath);
                        }

                        @Override
                        public void onError(Throwable ex, boolean isOnCallback) {
                        }

                        @Override
                        public void onCancelled(CancelledException cex) {
                        }

                        @Override
                        public void onFinished() {
                            BSSMCommonUtils.openShare(ShareActivity.this, model.getTitle(), model.getPictureUrl(), model.getContent(), filePath);
                        }
                    });
                } else {
                    BSSMCommonUtils.openShare(ShareActivity.this, model.getTitle(), model.getPictureUrl(), model.getContent(), filePath);
                }
            }
        });
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_left:
                finish();
                break;
        }
    }
}
