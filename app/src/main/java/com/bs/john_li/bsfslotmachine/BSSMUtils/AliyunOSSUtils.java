package com.bs.john_li.bsfslotmachine.BSSMUtils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.GetObjectRequest;
import com.alibaba.sdk.android.oss.model.GetObjectResult;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.bs.john_li.bsfslotmachine.R;

import java.io.File;
import java.io.InputStream;

/**
 * Created by John_Li on 12/3/2018.
 */

public class AliyunOSSUtils {
    /**
     *  初始化一个OssService用来上传
     * @param context
     */
    public static OSSClient initOSS(Context context) {
        //负责所有的界面更新
        OSSClient oss;
        OSSCredentialProvider credentialProvider;
        //使用自己的获取STSToken的类
        //STSGetter类，封装如何跟从应用服务器取数据，必须继承于OSSFederationCredentialProvider这个类。 取Token这个取决于您所写的APP跟应用服务器数据的协议设计。
        if (BSSMConfigtor.OSS_TOKEN .equals("")) {
            credentialProvider = new STSGetter(context);
        }else {
            credentialProvider = new STSGetter(context, BSSMConfigtor.BASE_URL + BSSMConfigtor.OSS_TOKEN);
        }
        //初始化OSSClient
        ClientConfiguration conf = new ClientConfiguration();
        conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒
        conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒
        conf.setMaxConcurrentRequest(5); // 最大并发请求数，默认5个
        conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次
        oss = new OSSClient(context.getApplicationContext(), BSSMConfigtor.END_POINT, credentialProvider, conf);
        return oss;
    }

    private void putImg(final Handler mHandler, OSSClient oss, File file) {
        Bitmap bitmap = BSSMCommonUtils.compressImageFromFile(file.getPath(), 1024f);// 按尺寸压缩图片
        File filePut = BSSMCommonUtils.compressImage(bitmap, file.getPath());  //按质量压缩图片

        String fileName = filePut.getName();
        String filePath = filePut.getPath();
        // 构造上传请求
        PutObjectRequest put = new PutObjectRequest(BSSMConfigtor.BucketName, fileName, filePath);

        // 異步請求
        OSSAsyncTask task = oss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                Message msg = new Message();
                msg.what  = 0;
                mHandler.sendMessage(msg);
            }

            @Override
            public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                // 请求异常
                if (clientExcepion != null) {
                    // 本地异常如网络异常等
                    clientExcepion.printStackTrace();
                }
                if (serviceException != null) {
                    // 服务异常
                    Log.e("ErrorCode", serviceException.getErrorCode());
                    Log.e("RequestId", serviceException.getRequestId());
                    Log.e("HostId", serviceException.getHostId());
                    Log.e("RawMessage", serviceException.getRawMessage());
                }

                Message msg = new Message();
                msg.what  = -1;
                mHandler.sendMessage(msg);
            }
        });
    }

    /**
     *  从OSS上下载图片
     * @param object    圖片名稱
     * @param oss   OSS對象
     * @param iv    綁定的ImageView
     * @param context   上下文，用來做UI線程的執行操作
     */
    public static void downloadImg(String object, OSSClient oss, final ImageView iv, final Context context, final int fialImgRes) {
        final Activity activity = ((Activity)context);
        if ((object == null)) {
            Log.w("AsyncGetImage", "ObjectNull");
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    iv.setImageResource(fialImgRes);
                }
            });
            return;
        }

        if (object.equals("")){
            Log.w("AsyncGetImage", "ObjectNull");
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    iv.setImageResource(fialImgRes);
                }
            });
            return;
        }

        GetObjectRequest get = new GetObjectRequest(BSSMConfigtor.BucketName, object);

        OSSAsyncTask task = oss.asyncGetObject(get, new OSSCompletedCallback<GetObjectRequest, GetObjectResult>() {
            @Override
            public void onSuccess(GetObjectRequest request, GetObjectResult result) {
                // 请求成功
                InputStream inputStream = result.getObjectContent();
                // 直接对stream直接用不转byte
                /*InputStream inputStreamCache = inputStream;
                BitmapFactory.Options opt = new BitmapFactory.Options();
                opt.inJustDecodeBounds = true;
                //获取图片参数
                BitmapFactory.decodeStream(inputStreamCache,null, opt);
                opt.inSampleSize= BSSMCommonUtils.calculateInSampleSize(opt,carPhotoIv.getWidth(), carPhotoIv.getHeight());
                opt.inPreferredConfig = Bitmap.Config.RGB_565;
                opt.inPurgeable = true;
                opt.inInputShareable = true;
                opt.inJustDecodeBounds = false;
                //先关闭InputStream 流
                try {
                    inputStreamCache.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //生成Bitmap对象
                Bitmap bm = BitmapFactory.decodeStream(inputStream,null, opt);*//*

                //  阿里的方法不处理
                *//*try {
                    //Bitmap bm = BitmapFactory.decodeStream(inputStream);
                    //需要根据对应的View大小来自适应缩放
                    Bitmap bm = BSSMCommonUtils.autoResizeFromStream(inputStream, carPhotoIv);
                    inputStream.close();
                    Message msg = mHandler.obtainMessage(2, bm);
                    msg.sendToTarget();
                    System.gc();
                }catch (Exception e) {
                    e.printStackTrace();
                }*//*

                //  阿里的方法做图片处理
                //重载InputStream来获取读取进度信息
                *//*try {
                    //需要根据对应的View大小来自适应缩放
                    Bitmap bm = BSSMCommonUtils.autoResizeFromStream(inputStream, carPhotoIv);
                    Message msg = mHandler.obtainMessage(2, bm);
                    msg.sendToTarget();
                } catch (Exception e) {
                    e.printStackTrace();
                }*/
                try {
                    byte[] date = new byte[0];
                    date = BSSMCommonUtils.readStream(inputStream);
                    //获取bitmap
                    final Bitmap bm = BitmapFactory.decodeByteArray(date,0,date.length);
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            iv.setImageBitmap(bm);
                            System.gc();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(GetObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                // 请求异常
                if (clientExcepion != null) {
                    // 本地异常如网络异常等
                    clientExcepion.printStackTrace();
                }
                if (serviceException != null) {
                    // 服务异常
                    Log.e("ErrorCode", serviceException.getErrorCode());
                    Log.e("RequestId", serviceException.getRequestId());
                    Log.e("HostId", serviceException.getHostId());
                    Log.e("RawMessage", serviceException.getRawMessage());
                }

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        iv.setImageResource(fialImgRes);
                    }
                });
            }
        });
    }


}
