package com.bs.john_li.bsfslotmachine.BSSMActivity.Mine;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bs.john_li.bsfslotmachine.BSSMActivity.BaseActivity;
import com.bs.john_li.bsfslotmachine.BSSMModel.VersionOutModel;
import com.bs.john_li.bsfslotmachine.BSSMUtils.BSSMCommonUtils;
import com.bs.john_li.bsfslotmachine.BSSMUtils.BSSMConfigtor;
import com.bs.john_li.bsfslotmachine.BSSMUtils.SPUtils;
import com.bs.john_li.bsfslotmachine.BSSMView.BSSMHeadView;
import com.bs.john_li.bsfslotmachine.R;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.xutils.common.Callback;
import org.xutils.common.task.PriorityExecutor;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;

/**
 * 系統設置
 * Created by John_Li on 4/8/2017.
 */

public class SettingActivity extends BaseActivity implements View.OnClickListener {
    private BSSMHeadView settingHead;
    private LinearLayout clearLL, updateVersion, aboutLL;
    private TextView clearTv, loginOutTv;


    private String m_newVerCode; //最新版的版本号
    private String m_newVerName; //最新版的版本名
    private String m_newApkUrl;//新的apk下载地址
    private String m_appNameStr; //下载到本地要给这个APP命的名字
    private String m_versionRemark; //新版本的備註
    private Callback.Cancelable cancelable;// 短點續傳的回調
    private ProgressDialog m_progressDlg;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activty_setting);
        initView();
        setListener();
        initData();
    }

    @Override
    public void initView() {
        settingHead = (BSSMHeadView) findViewById(R.id.setting_head);
        clearLL = findViewById(R.id.setting_clean_cache);
        aboutLL = findViewById(R.id.setting_about);
        updateVersion = findViewById(R.id.version_update);
        clearTv = findViewById(R.id.setting_clean_cache_tv);
        loginOutTv = findViewById(R.id.login_out_tv);
    }

    @Override
    public void setListener() {
        clearLL.setOnClickListener(this);
        aboutLL.setOnClickListener(this);
        updateVersion.setOnClickListener(this);
        loginOutTv.setOnClickListener(this);
    }

    @Override
    public void initData() {
        settingHead.setTitle("設置");
        settingHead.setLeft(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.head_left:
                finish();
                break;
            case R.id.setting_clean_cache:
                clearTv.setText("0.0MB");
                break;
            case R.id.setting_about:
                startActivity(new Intent(this, AboutUsActivity.class));
                break;
            case R.id.version_update:
                checkAPPVersion();
                break;
            case R.id.login_out_tv:
                SPUtils.put(this, "UserToken", "");
                SPUtils.put(this, "UserInfo", "");
                SPUtils.put(this, "HasPayPw", "");
                EventBus.getDefault().post("LOGIN_OUT");
                finish();
                break;
        }
    }

    /**
     * 从服务器获取当前最新版本号
     */
    private void checkAPPVersion() {
        m_progressDlg = new ProgressDialog(this);
        m_progressDlg.setTitle("提示");
        m_progressDlg.setMessage("檢查版本號中......");
        m_progressDlg.show();
        m_appNameStr = "haha.apk";
        RequestParams params = new RequestParams(BSSMConfigtor.BASE_URL + BSSMConfigtor.CHECK_VERSION);
        String url = params.getUri();
        params.setConnectTimeout(30 * 1000);
        x.http().request(HttpMethod.POST, params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                VersionOutModel model = new Gson().fromJson(result.toString(), VersionOutModel.class);
                if (model.getCode() == 200) {
                    m_newVerCode = model.getData().getVersion();
                    m_newApkUrl = model.getData().getRdUrl();
                    m_appNameStr = model.getData().getAppName();
                    m_versionRemark = model.getData().getExt();
                } else {
                    m_newVerCode = "-1";
                    Toast.makeText(SettingActivity.this, model.getMsg().toString(), Toast.LENGTH_SHORT).show();
                }
            }

            //请求异常后的回调方法
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                m_newVerCode = "-1";
            }

            //主动调用取消请求的回调方法
            @Override
            public void onCancelled(CancelledException cex) {
            }

            @Override
            public void onFinished() {
                m_progressDlg.dismiss();
                int vercode = BSSMCommonUtils.getVerCode(SettingActivity.this.getApplicationContext());
                if (Integer.parseInt(m_newVerCode) > vercode) {
                    doNewVersionUpdate(); // 更新新版本
                } else {
                    notNewVersionDlgShow(); // 提示当前为最新版本
                }
            }
        });
    }

    private void doNewVersionUpdate() {
        int verCode = BSSMCommonUtils.getVerCode(SettingActivity.this.getApplicationContext());
        String verName = BSSMCommonUtils.getVerName(SettingActivity.this.getApplicationContext());

        String str= "當前版本："+verName+" Code:"+verCode+" ,發現新版本："+m_newVerName+
                " Code:"+m_newVerCode+" ,是否更新？";
        Dialog dialog = new AlertDialog.Builder(SettingActivity.this).setTitle("軟件更新").setMessage(str)
                // 设置内容
                .setPositiveButton("更新",// 设置确定按钮
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                m_progressDlg.setTitle("正在下載");
                                m_progressDlg.setMessage("請稍後...");
                                downFile(m_newApkUrl);  //开始下载
                            }
                        })
                .setNegativeButton("暫不更新",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                // 点击"取消"按钮之后退出程序
                            }
                        }).create();// 创建
        // 显示对话框
        dialog.show();
    }

    /**
         *  提示当前为最新版本
         */
    private void notNewVersionDlgShow() {
        int verCode = BSSMCommonUtils.getVerCode(SettingActivity.this.getApplicationContext());
        String verName = BSSMCommonUtils.getVerName(SettingActivity.this.getApplicationContext());
        if(m_newVerCode.equals("-1")) {
            Toast.makeText(SettingActivity.this, "獲取版本號失敗，請重試！", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(SettingActivity.this, "當前版本:"+verName+" Code:"+verCode+",已經是最新版本!", Toast.LENGTH_LONG).show();
        }
    }

    private void downFile(String m_newApkUrl) {
        initProgressDialog();
        // 開始下載
        //设置请求参数
        RequestParams params = new RequestParams(m_newApkUrl);
        params.setAutoResume(true);//设置是否在下载是自动断点续传
        params.setAutoRename(false);//设置是否根据头信息自动命名文件
        params.setSaveFilePath("/sdcard/xutils/xUtils_1.avi");
        params.setExecutor(new PriorityExecutor(2, true));//自定义线程池,有效的值范围[1, 3], 设置为3时, 可能阻塞图片加载.
        params.setCancelFast(true);//是否可以被立即停止.
        //下面的回调都是在主线程中运行的,这里设置的带进度的回调
        /**
                 * 可取消的任务
                 */
        cancelable = x.http().get(params, new Callback.ProgressCallback<File>() {
            @Override
            public void onCancelled(CancelledException arg0) {
                Log.i("tag", "取消"+Thread.currentThread().getName());
            }

            @Override
            public void onError(Throwable arg0, boolean arg1) {
                Log.i("tag", "onError: 失败"+Thread.currentThread().getName());
                m_progressDlg.dismiss();
            }

            @Override
            public void onFinished() {
                Log.i("tag", "完成,每次取消下载也会执行该方法"+Thread.currentThread().getName());
                m_progressDlg.dismiss();
            }

            @Override
            public void onSuccess(File arg0) {
                Log.i("tag", "下载成功的时候执行"+Thread.currentThread().getName());
                // 下載完成
                down();
            }

            @Override
            public void onLoading(long total, long current, boolean isDownloading) {
                if (isDownloading) {
                    m_progressDlg.setProgress((int) (current*100/total));
                    Log.i("tag", "下载中,会不断的进行回调:"+Thread.currentThread().getName());
                }
            }

            @Override
            public void onStarted() {
                Log.i("tag", "开始下载的时候执行"+Thread.currentThread().getName());
                m_progressDlg.show();
            }

            @Override
            public void onWaiting() {
                Log.i("tag", "等待,在onStarted方法之前执行"+Thread.currentThread().getName());
            }

        });
    }

    /**
     * 下載完成關閉進度條
     */
    private void down() {
        m_progressDlg.dismiss();
        update();
    }

    /**
     * 更新APP
     */
    private void update() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory(), m_appNameStr)),
                "application/vnd.android.package-archive");
        this.startActivity(intent);
    }

    /*初始化短點續傳的对话框*/
    private void initProgressDialog() {
        //创建进度条对话框
        m_progressDlg = new ProgressDialog(this);
        //设置标题
        m_progressDlg.setTitle("下载文件");
        //设置信息
        m_progressDlg.setMessage("玩命下载中...");
        //设置显示的格式
        m_progressDlg.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        //设置按钮
        m_progressDlg.setButton(ProgressDialog.BUTTON_NEGATIVE, "暂停",new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //点击取消正在下载的操作
                cancelable.cancel();
            }});

        m_progressDlg.show();
    }
}
