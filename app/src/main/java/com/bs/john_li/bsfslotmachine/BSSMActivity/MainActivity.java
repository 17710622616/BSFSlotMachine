package com.bs.john_li.bsfslotmachine.BSSMActivity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.bs.john_li.bsfslotmachine.BSSMFragment.ForumFragment;
import com.bs.john_li.bsfslotmachine.BSSMFragment.MineFragment;
import com.bs.john_li.bsfslotmachine.BSSMFragment.ParkingFragment;
import com.bs.john_li.bsfslotmachine.BSSMModel.VersionOutModel;
import com.bs.john_li.bsfslotmachine.BSSMUtils.BSSMCommonUtils;
import com.bs.john_li.bsfslotmachine.BSSMUtils.BSSMConfigtor;
import com.bs.john_li.bsfslotmachine.BSSMUtils.SPUtils;
import com.bs.john_li.bsfslotmachine.BSSMUtils.StatusBarUtil;
import com.bs.john_li.bsfslotmachine.R;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.common.task.PriorityExecutor;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;

/**
 * Created by John_Li on 27/7/2017.
 */

public class MainActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {
    private RadioButton park_rb,forum_rb,mine_rb;
    private RadioGroup bottom_group;
    private FragmentManager fm;
    private Fragment cacheFragment;
    private ImageView iv;


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
        setContentView(R.layout.activity_main);
        //StatusBarUtil.setColor(this,getResources().getColor(R.color.colorPrimary),0);
        checkRuntimePermission();
        // 判斷是否登錄
        isLoginNow();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        initView();
        setListener();
        initData();
        checkAPPVersion();
    }

    @Override
    public void initView() {
        bottom_group = (RadioGroup)findViewById(R.id.bottom_main_group);
        park_rb = (RadioButton) findViewById(R.id.bottom_main_parking);
        forum_rb = (RadioButton) findViewById(R.id.bottom_main_forum);
        mine_rb = (RadioButton) findViewById(R.id.bottom_main_mine);

        //iv = findViewById(R.id.order_icon);
    }

    @Override
    public void setListener() {
        bottom_group.setOnCheckedChangeListener(this);
    }

    @Override
    public void initData() {
        fm = getSupportFragmentManager();
        FragmentTransaction traslation = fm.beginTransaction();
        cacheFragment = new ParkingFragment();
        traslation.add(R.id.main_containor,cacheFragment,ParkingFragment.TAG);
        traslation.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * 检查运行时权限
     *      需要3个权限(都是危险权限):
     *      1. 读取位置权限;
     *      2. 读取外部存储器权限;
     *      3. 写入外部存储器权限
     */
    private void checkRuntimePermission() {
        //第 1 步: 检查是否有相应的权限
        boolean isAllGranted = checkPermissionAllGranted(new String[] {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE});
        // 如果这3个权限全都拥有, 则直接执行备份代码
        if (isAllGranted) {
            return;
        }
        //第 2 步: 请求权限
        // 一次请求多个权限, 如果其他有权限是已经授予的将会自动忽略掉
        ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE},BSSMConfigtor.MY_PERMISSION_REQUEST_CODE);
    }

    /**
     * 检查是否拥有指定的所有权限
     */
    private boolean checkPermissionAllGranted(String[] permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                // 只要有一个权限没有被授予, 则直接返回 false
                return false;
            }
        }
        return true;
    }

    /**
     * 第 3 步: 申请权限结果返回处理
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == BSSMConfigtor.MY_PERMISSION_REQUEST_CODE) {
            boolean isAllGranted = true;

            // 判断是否所有的权限都已经授予了
            for (int grant : grantResults) {
                if (grant != PackageManager.PERMISSION_GRANTED) {
                    isAllGranted = false;
                    break;
                }
            }

            if (isAllGranted) {
                // 如果所有的权限都授予了, 则执行备份代码
                return;
            } else {
                // 弹出对话框告诉用户需要权限的原因, 并引导用户去应用权限管理中手动打开权限按钮
                openAppDetails();
            }
        }
    }

    /**
     * 打开 APP 的详情设置
     */
    private void openAppDetails() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("親，要打開定位及讀寫文檔的權限，我們才能為您更好的定位下單哦！");
        builder.setPositiveButton("去手動授權", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                intent.setData(Uri.parse("package:" + getPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("取消", null);
        builder.show();
    }

    /**
     * 判斷是否登錄
     */
    private void isLoginNow() {
        String userToken = (String) SPUtils.get(this, "UserToken", "");
        if (userToken == null) {
            if (userToken.equals("")){
                Toast.makeText(this, getString(R.string.not_login), Toast.LENGTH_SHORT).show();
            }
            Toast.makeText(this,  getString(R.string.not_login), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
        switch (i){
            case R.id.bottom_main_parking:
                park_rb.setTextColor(getResources().getColor(R.color.colorSkyBlue));
                forum_rb.setTextColor(getResources().getColor(R.color.colorDrakGray));
                mine_rb.setTextColor(getResources().getColor(R.color.colorDrakGray));
                switchPages(ParkingFragment.class,ParkingFragment.TAG);
                break;
            case R.id.bottom_main_forum:
                park_rb.setTextColor(getResources().getColor(R.color.colorDrakGray));
                forum_rb.setTextColor(getResources().getColor(R.color.colorSkyBlue));
                mine_rb.setTextColor(getResources().getColor(R.color.colorDrakGray));
                switchPages(ForumFragment.class,ForumFragment.TAG);
                break;
            case R.id.bottom_main_mine:
                park_rb.setTextColor(getResources().getColor(R.color.colorDrakGray));
                forum_rb.setTextColor(getResources().getColor(R.color.colorDrakGray));
                mine_rb.setTextColor(getResources().getColor(R.color.colorSkyBlue));
                switchPages(MineFragment.class,MineFragment.TAG);
                break;
        }
    }

    private void switchPages(Class<?> cls, String tag){
        FragmentTransaction transaction = fm.beginTransaction();
        if (cacheFragment != null){
            transaction.hide(cacheFragment);
        }
        cacheFragment = fm.findFragmentByTag(tag);
        if (cacheFragment != null){
            transaction.show(cacheFragment);
        } else {
            try{
               cacheFragment = (Fragment) cls.getConstructor().newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (Exception e){
                e.printStackTrace();
            }
            transaction.add(R.id.main_containor, cacheFragment, tag);
        }
        transaction.commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 从服务器获取当前最新版本号
     */
    private void checkAPPVersion() {
        m_progressDlg = new ProgressDialog(this);
        m_progressDlg.setTitle("提示");
        m_progressDlg.setMessage("檢查版本號中......");
        m_progressDlg.setCancelable(false);
        m_progressDlg.setCanceledOnTouchOutside(false);
        m_progressDlg.show();
        m_appNameStr = "掌泊寶.apk";
        RequestParams params = new RequestParams(BSSMConfigtor.BASE_URL + BSSMConfigtor.CHECK_VERSION);
        String url = params.getUri();
        params.setConnectTimeout(30 * 1000);
        x.http().request(HttpMethod.GET, params, new Callback.CommonCallback<String>() {
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
                    Toast.makeText(MainActivity.this, model.getMsg().toString(), Toast.LENGTH_SHORT).show();
                }
            }

            //请求异常后的回调方法
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(MainActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
                m_newVerCode = "-1";
            }

            //主动调用取消请求的回调方法
            @Override
            public void onCancelled(CancelledException cex) {
            }

            @Override
            public void onFinished() {
                m_progressDlg.dismiss();
                int vercode = BSSMCommonUtils.getVerCode(MainActivity.this.getApplicationContext());
                if (Integer.parseInt(m_newVerCode) > vercode) {
                    doNewVersionUpdate(); // 更新新版本
                } else {
                    notNewVersionDlgShow(); // 提示当前为最新版本
                }
            }
        });
    }

    private void doNewVersionUpdate() {
        int verCode = BSSMCommonUtils.getVerCode(MainActivity.this.getApplicationContext());
        String verName = BSSMCommonUtils.getVerName(MainActivity.this.getApplicationContext());

        String str= "當前版本："+verName+" Code:"+verCode+" ,發現新版本："+
                " Code:"+m_newVerCode+" ,是否更新？";
        Dialog dialog = new AlertDialog.Builder(MainActivity.this).setTitle("軟件更新").setMessage(str)
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
                                System.exit(0);
                            }
                        }).create();// 创建

        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        // 显示对话框
        dialog.show();
    }

    /**
     *  提示当前为最新版本
     */
    private void notNewVersionDlgShow() {
        int verCode = BSSMCommonUtils.getVerCode(MainActivity.this.getApplicationContext());
        String verName = BSSMCommonUtils.getVerName(MainActivity.this.getApplicationContext());
        if(m_newVerCode.equals("-1")) {
            //Toast.makeText(MainActivity.this, "獲取版本號失敗，請重試！", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(MainActivity.this, "當前版本:"+verName+" Code:"+verCode+",已經是最新版本!", Toast.LENGTH_LONG).show();
        }
    }

    private void downFile(String m_newApkUrl) {
        initProgressDialog();
        // 開始下載
        //设置请求参数
        RequestParams params = new RequestParams(m_newApkUrl);
        params.setAutoResume(true);//设置是否在下载是自动断点续传
        params.setAutoRename(false);//设置是否根据头信息自动命名文件
        params.setSaveFilePath(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getPath() + "/BSSMPictures/" + m_appNameStr);
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
                Log.i("tag", "onError: 失败"+Thread.currentThread().getName() + "------Throwable:" + arg0.getMessage());
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
        //update();
        openAPKFile(this, new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/BSSMPictures", m_appNameStr).getPath());
    }

    /**
     * 打开安装包
     *
     * @param mContext
     * @param fileUri
     */
    public void openAPKFile(Activity mContext, String fileUri) {
        //DataEmbeddingUtil.dataEmbeddingAPPUpdate(fileUri);
        // 核心是下面几句代码
        if (null != fileUri) {
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                File apkFile = new File(fileUri);
                //兼容7.0
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    Uri contentUri = FileProvider.getUriForFile(MainActivity.this, "com.bs.john_li.bsfslotmachine" + ".fileprovider", apkFile);
                    intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
                    //兼容8.0
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        boolean hasInstallPermission = getPackageManager().canRequestPackageInstalls();
                        if (!hasInstallPermission) {
                            Toast.makeText(MainActivity.this, "hasInstallPermission=" + hasInstallPermission, Toast.LENGTH_LONG);
                            startInstallPermissionMainActivity();
                            return;
                        }
                    }
                } else {
                    intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                }
                if (mContext.getPackageManager().queryIntentActivities(intent, 0).size() > 0) {
                    mContext.startActivity(intent);
                }
            } catch (Throwable e) {
                e.printStackTrace();
                //DataEmbeddingUtil.dataEmbeddingAPPUpdate(e.toString());
                //CommonUtils.makeEventToast(MyApplication.getContext(), MyApplication.getContext().getString(R.string.download_hint), false);
                Toast.makeText(MainActivity.this, "版本更新失敗，您亦可以嘗試去Google Play搜索(掌泊寶)更新最新版，謝謝！", Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * 跳转到设置-允许安装未知来源-页面
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startInstallPermissionMainActivity() {
        //注意这个是8.0新API
        Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    /**
     * 更新APP
     */
    private void update() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.fromFile(new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/BSSMPictures", m_appNameStr)), "application/vnd.android.package-archive");
        this.startActivity(intent);
    }

    /*初始化短點續傳的对话框*/
    private void initProgressDialog() {
        //创建进度条对话框
        m_progressDlg = new ProgressDialog(this);
        //设置标题
        m_progressDlg.setTitle("下載安裝包");
        //设置信息
        m_progressDlg.setMessage("玩命下載中...");
        //设置显示的格式
        m_progressDlg.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        //设置按钮
        m_progressDlg.setButton(ProgressDialog.BUTTON_NEGATIVE, "暫停",new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //点击取消正在下载的操作
                cancelable.cancel();
            }});

        m_progressDlg.show();
    }
}
