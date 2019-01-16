package com.bs.john_li.bsfmerchantsversionapp;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.GetObjectRequest;
import com.alibaba.sdk.android.oss.model.GetObjectResult;
import com.bs.john_li.bsfmerchantsversionapp.Adapter.MyMenuAdapter;
import com.bs.john_li.bsfmerchantsversionapp.Model.TaskInfo;
import com.bs.john_li.bsfmerchantsversionapp.Model.VersionOutModel;
import com.bs.john_li.bsfmerchantsversionapp.Utils.AliyunOSSUtils;
import com.bs.john_li.bsfmerchantsversionapp.Utils.BSFCommonUtils;
import com.bs.john_li.bsfmerchantsversionapp.Utils.BSFMerchantConfigtor;
import com.bs.john_li.bsfmerchantsversionapp.Utils.DownloadRunnable;
import com.bs.john_li.bsfmerchantsversionapp.Utils.SPUtils;
import com.google.gson.Gson;
import com.sunmi.peripheral.printer.ICallback;
import com.sunmi.peripheral.printer.ILcdCallback;
import com.sunmi.peripheral.printer.ITax;
import com.sunmi.peripheral.printer.SunmiPrinterService;
import com.sunmi.peripheral.printer.TransBean;

import org.xutils.common.Callback;
import org.xutils.common.task.PriorityExecutor;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity {
    private GridView mGv;

    private static final int START_SCAN = 0x0001;
    private static Boolean isQuit = false;
    private String m_newVerCode; //最新版的版本号
    private String m_newVerName; //最新版的版本名
    private String m_newApkUrl;//新的apk下载地址
    private String m_appNameStr; //下载到本地要给这个APP命的名字
    private String m_versionRemark; //新版本的備註
    private Callback.Cancelable cancelable;// 短點續傳的回調
    private ProgressDialog m_progressDlg;
    //负责所有的界面更新
    private OSSClient oss;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String SellerUserToken = SPUtils.get(this, "SellerUserToken", "").toString();
        if (!SellerUserToken.equals("")) {
            initView();
            setListener();
            initData();
            checkAPPVersion();
        } else {
            startActivityForResult(new Intent(this, LoginActivity.class), 1);
        }
    }

    private void initView() {
        mGv = (GridView) findViewById(R.id.menu_gridView);
    }

    private void setListener() {
        mGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        // 为外部调用提供
                        try {
                            Intent intent = new Intent("com.summi.scan");

                            intent.setPackage("com.sunmi.sunmiqrcodescanner");
                             //扫码模块有一些功能选项，开发者可以通过传递参数控制这些参数，
                             //所有参数都有一个默认值，开发者只要在需要的时候添加这些配置就可以。
                             intent.putExtra("CURRENT_PPI", 0X0003);//当前分辨率
                             //M1和V1的最佳是800*480,PPI_1920_1080 = 0X0001;PPI_1280_720 =
                             //0X0002;PPI_BEST = 0X0003;
                             intent.putExtra("PLAY_SOUND", true);// 扫描完成声音提示  默认true
                             intent.putExtra("PLAY_VIBRATE", false);
                             //扫描完成震动,默认false，目前M1硬件支持震动可用该配置，V1不支持
                             intent.putExtra("IDENTIFY_INVERSE_QR_CODE", true);// 识别反色二维码，默认true
                             intent.putExtra("IDENTIFY_MORE_CODE", false);// 识别画面中多个二维码，默认false
                             intent.putExtra("IS_SHOW_SETTING", true);// 是否显示右上角设置按钮，默认true
                             intent.putExtra("IS_SHOW_ALBUM", true);// 是否显示从相册选择图片按钮，默认true
                            startActivityForResult(intent, START_SCAN);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case 1:
                        startActivity(new Intent(MainActivity.this, SellerOrderListActivity.class));
                        break;
                    case 2:
                        startActivity(new Intent(MainActivity.this, SellerSetListActivity.class));
                        break;
                    case 3:
                        //Toast.makeText(MainActivity.this, "此功能暫沒開放，敬請期待！", Toast.LENGTH_SHORT).show();
                        break;
                    case 4:
                        new AlertDialog.Builder(MainActivity.this).setTitle("提醒")
                                .setIconAttribute(android.R.attr.alertDialogIcon)
                                .setMessage("確認要登出賬戶？")
                                .setPositiveButton("確認", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        SPUtils.put(MainActivity.this, "qsUserToken", "");
                                        dialog.dismiss();
                                        String qsUserToken = (String) SPUtils.get(MainActivity.this, "qsUserToken", "");
                                        if (qsUserToken.equals("")) {
                                            System.exit(0);
                                        } else {
                                            Toast.makeText(MainActivity.this, "登出失敗！", Toast.LENGTH_SHORT).show();
                                        }
                                    }})
                                .setNegativeButton("取消", null)
                                .create().show();
                        break;
                }
            }
        });
    }

    private void initData() {
        mGv.setAdapter(new MyMenuAdapter(this));
        // 初始化OSS
        oss = AliyunOSSUtils.initOSS(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == START_SCAN && data != null) {
            Bundle bundle = data.getExtras();
            ArrayList<HashMap<String, String>> result = (ArrayList<HashMap<String, String>>) bundle .getSerializable("data");
            Iterator<HashMap<String, String>> it = result.iterator();
            String scanReturnStr = "";
            while (it.hasNext()) {
                HashMap<String, String> hashMap = it.next();
                scanReturnStr = scanReturnStr + hashMap.get("VALUE");
            }
            Toast.makeText(MainActivity.this, scanReturnStr, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this,OrderCheckActivity.class);
            intent.putExtra("scanReturnStr", scanReturnStr);
            startActivity(intent);
        }

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 1:
                    initView();
                    setListener();
                    initData();
                    checkAPPVersion();
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isQuit = false;
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (!isQuit) {
                isQuit = true;
                Toast.makeText(getApplicationContext(), "再按一次退出程序",
                        Toast.LENGTH_SHORT).show();
                // 利用handler延迟发送更改状态信息
                mHandler.sendEmptyMessageDelayed(0, 2000);
            } else {
                finish();
                System.exit(0);
            }
        }
        return false;
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
        RequestParams params = new RequestParams(BSFMerchantConfigtor.BASE_URL + BSFMerchantConfigtor.CHECK_VERSION);
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
                int vercode = BSFCommonUtils.getVerCode(MainActivity.this.getApplicationContext());
                if (Integer.parseInt(m_newVerCode) > vercode) {
                    doNewVersionUpdate(); // 更新新版本
                } else {
                    notNewVersionDlgShow(); // 提示当前为最新版本
                }
            }
        });
    }

    private void doNewVersionUpdate() {
        int verCode = BSFCommonUtils.getVerCode(MainActivity.this.getApplicationContext());
        String verName = BSFCommonUtils.getVerName(MainActivity.this.getApplicationContext());

        String str= "當前版本："+verName+" Code:"+verCode+" ,發現新版本："+
                " Code:"+m_newVerCode+" ,是否更新？";
        Dialog dialog = new android.app.AlertDialog.Builder(MainActivity.this).setTitle("軟件更新").setMessage(str)
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
        int verCode = BSFCommonUtils.getVerCode(MainActivity.this.getApplicationContext());
        String verName = BSFCommonUtils.getVerName(MainActivity.this.getApplicationContext());
        if(m_newVerCode.equals("-1")) {
            //Toast.makeText(MainActivity.this, "獲取版本號失敗，請重試！", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(MainActivity.this, "當前版本:"+verName+" Code:"+verCode+",已經是最新版本!", Toast.LENGTH_SHORT).show();
        }
    }

    private long mediaLength;
    private long readSize;
    private String localUrl;
    private void downFile(String m_newApkUrl) {
        // 開始下載
        initProgressDialog();
        // 构造下载文件请求。
        GetObjectRequest get = new GetObjectRequest(BSFMerchantConfigtor.BucketName, "MerchantSide.apk");

        OSSAsyncTask task = oss.asyncGetObject(get, new OSSCompletedCallback<GetObjectRequest, GetObjectResult>() {
            @Override
            public void onSuccess(GetObjectRequest request, GetObjectResult result) {
                // 请求成功。
                InputStream inputStream = result.getObjectContent();
                // 请求成功回调
                Log.d("Content-Length", "" + result.getContentLength());
                //拿到输入流和文件长度
                inputStream = result.getObjectContent();
                mediaLength = result.getContentLength();
                byte[] buffer = new byte[2*2048];
                int len;
                FileOutputStream out = null;
//              long lastReadSize = 0;
                //建立本地缓存路径，视频缓存到这个目录
                localUrl = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getPath() + "/BSSMPictures/" + m_appNameStr;
                Log.d("localUrl: " , localUrl);
                File cacheFile = new File(localUrl);
                if (!cacheFile.exists()) {
                    cacheFile.getParentFile().mkdirs();
                    try {
                        cacheFile.createNewFile();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                readSize = cacheFile.length();
                try {
                    //将缓存的视频转换为流
                    out = new FileOutputStream(cacheFile, true);
                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                if (mediaLength == -1) {
                    return;
                }
                mHandler1.sendEmptyMessage(VIDEO_STATE_UPDATE);
                try {
                    while ((len = inputStream.read(buffer)) != -1) {
                        // 处理下载的数据
                        try{
                            out.write(buffer, 0, len);
                            readSize += len;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    mHandler1.sendEmptyMessage(CACHE_VIDEO_END);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                finally {
                    if (out != null) {
                        try {
                            out.close();
                        } catch (IOException e) {
                        }
                    }
                    if (inputStream != null) {
                        try {
                            inputStream.close();
                        } catch (IOException e) {
                        }
                    }
                }
            }

            @Override
            // GetObject请求成功，将返回GetObjectResult，其持有一个输入流的实例。返回的输入流，请自行处理。
            public void onFailure(GetObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                // 请求异常。
                if (clientExcepion != null) {
                    // 本地异常，如网络异常等。
                    clientExcepion.printStackTrace();
                }
                if (serviceException != null) {
                    // 服务异常。
                    Log.e("ErrorCode", serviceException.getErrorCode());
                    Log.e("RequestId", serviceException.getRequestId());
                    Log.e("HostId", serviceException.getHostId());
                    Log.e("RawMessage", serviceException.getRawMessage());
                }
                Toast.makeText(MainActivity.this, "下載新版本失敗！", Toast.LENGTH_SHORT).show();
                m_progressDlg.dismiss();
            }
        });
    }

    private final static int VIDEO_STATE_UPDATE = 0;
    private final static int CACHE_VIDEO_END = 3;

    private final Handler mHandler1 = new Handler() {
        @RequiresApi(api = 26)
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case VIDEO_STATE_UPDATE:
                    double cachepercent = readSize * 100.00 / mediaLength * 1.0;
                    String s = String.format("已下載: [%.2f%%]", cachepercent);
//              }
                    //缓存到达100%时开始播放
                    /*if(cachepercent==100.0||cachepercent==100.00){
                        mVideoView.setVideoPath(localUrl);
                        mVideoView.start();
                        String s1 = String.format("已缓存: [%.2f%%]", cachepercent);
                        tvcache.setText(s1);
                        return;
                    }*/
                    m_progressDlg.setMessage(s);
                    mHandler.sendEmptyMessageDelayed(VIDEO_STATE_UPDATE, 1000);
                    break;

                case CACHE_VIDEO_END:
                    down();
                    break;
            }
            super.handleMessage(msg);
        }
    };

    /**
     * 下載完成關閉進度條
     */
    @RequiresApi(api = 26)
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
    @RequiresApi(api = 26)
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

    SunmiPrinterService sunmiPrinterService = new SunmiPrinterService() {
        @Override
        public void updateFirmware() throws RemoteException {

        }

        @Override
        public int getFirmwareStatus() throws RemoteException {
            return 0;
        }

        @Override
        public String getServiceVersion() throws RemoteException {
            return null;
        }

        @Override
        public void printerInit(ICallback callback) throws RemoteException {

        }

        @Override
        public void printerSelfChecking(ICallback callback) throws RemoteException {

        }

        @Override
        public String getPrinterSerialNo() throws RemoteException {
            return null;
        }

        @Override
        public String getPrinterVersion() throws RemoteException {
            return null;
        }

        @Override
        public String getPrinterModal() throws RemoteException {
            return null;
        }

        @Override
        public int getPrintedLength(ICallback callback) throws RemoteException {
            return 0;
        }

        @Override
        public void lineWrap(int n, ICallback callback) throws RemoteException {

        }

        @Override
        public void sendRAWData(byte[] data, ICallback callback) throws RemoteException {

        }

        @Override
        public void setAlignment(int alignment, ICallback callback) throws RemoteException {

        }

        @Override
        public void setFontName(String typeface, ICallback callback) throws RemoteException {

        }

        @Override
        public void setFontSize(float fontsize, ICallback callback) throws RemoteException {

        }

        @Override
        public void printText(String text, ICallback callback) throws RemoteException {

        }

        @Override
        public void printTextWithFont(String text, String typeface, float fontsize, ICallback callback) throws RemoteException {

        }

        @Override
        public void printColumnsText(String[] colsTextArr, int[] colsWidthArr, int[] colsAlign, ICallback callback) throws RemoteException {

        }

        @Override
        public void printBitmap(Bitmap bitmap, ICallback callback) throws RemoteException {

        }

        @Override
        public void printBarCode(String data, int symbology, int height, int width, int textposition, ICallback callback) throws RemoteException {

        }

        @Override
        public void printQRCode(String data, int modulesize, int errorlevel, ICallback callback) throws RemoteException {

        }

        @Override
        public void printOriginalText(String text, ICallback callback) throws RemoteException {

        }

        @Override
        public void commitPrint(TransBean[] transbean, ICallback callback) throws RemoteException {

        }

        @Override
        public void commitPrinterBuffer() throws RemoteException {

        }

        @Override
        public void cutPaper(ICallback callback) throws RemoteException {

        }

        @Override
        public int getCutPaperTimes() throws RemoteException {
            return 0;
        }

        @Override
        public void openDrawer(ICallback callback) throws RemoteException {

        }

        @Override
        public int getOpenDrawerTimes() throws RemoteException {
            return 0;
        }

        @Override
        public void enterPrinterBuffer(boolean clean) throws RemoteException {

        }

        @Override
        public void exitPrinterBuffer(boolean commit) throws RemoteException {

        }

        @Override
        public void tax(byte[] data, ITax callback) throws RemoteException {

        }

        @Override
        public void getPrinterFactory(ICallback callback) throws RemoteException {

        }

        @Override
        public void clearBuffer() throws RemoteException {

        }

        @Override
        public void commitPrinterBufferWithCallback(ICallback callback) throws RemoteException {

        }

        @Override
        public void exitPrinterBufferWithCallback(boolean commit, ICallback callback) throws RemoteException {

        }

        @Override
        public void printColumnsString(String[] colsTextArr, int[] colsWidthArr, int[] colsAlign, ICallback callback) throws RemoteException {

        }

        @Override
        public int updatePrinterState() throws RemoteException {
            return 0;
        }

        @Override
        public void sendLCDCommand(int flag) throws RemoteException {

        }

        @Override
        public void sendLCDString(String string, ILcdCallback callback) throws RemoteException {

        }

        @Override
        public void sendLCDBitmap(Bitmap bitmap, ILcdCallback callback) throws RemoteException {

        }

        @Override
        public int getPrinterMode() throws RemoteException {
            return 0;
        }

        @Override
        public int getPrinterBBMDistance() throws RemoteException {
            return 0;
        }

        @Override
        public void printBitmapCustom(Bitmap bitmap, int type, ICallback callback) throws RemoteException {

        }

        @Override
        public int getForcedDouble() throws RemoteException {
            return 0;
        }

        @Override
        public boolean isForcedAntiWhite() throws RemoteException {
            return false;
        }

        @Override
        public boolean isForcedBold() throws RemoteException {
            return false;
        }

        @Override
        public boolean isForcedUnderline() throws RemoteException {
            return false;
        }

        @Override
        public int getForcedRowHeight() throws RemoteException {
            return 0;
        }

        @Override
        public int getFontName() throws RemoteException {
            return 0;
        }

        @Override
        public void sendLCDDoubleString(String topText, String bottomText, ILcdCallback callback) throws RemoteException {

        }

        @Override
        public int getPrinterPaper() throws RemoteException {
            return 0;
        }

        @Override
        public boolean getDrawerStatus() throws RemoteException {
            return false;
        }

        @Override
        public void sendLCDFillString(String string, int size, boolean fill, ILcdCallback callback) throws RemoteException {

        }

        @Override
        public void sendLCDMultiString(String[] text, int[] align, ILcdCallback callback) throws RemoteException {

        }

        @Override
        public IBinder asBinder() {
            return null;
        }
    };

    private void Binding(){
        Intent intent=new Intent();
        intent.setPackage("woyou.aidlservice.jiuiv5");
        intent.setAction("woyou.aidlservice.jiuiv5.IWoyouService");
        bindService(intent, connService, Context.BIND_AUTO_CREATE);
    }

    private ServiceConnection connService = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            sunmiPrinterService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            sunmiPrinterService = SunmiPrinterService.Stub.asInterface(service);
        }
    };
}