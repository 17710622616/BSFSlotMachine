package com.bs.john_li.bsfslotmachine.BSSMUtils;

import android.content.Context;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;

/**
 * Created by John_Li on 22/12/2017.
 */

public class OSSTools {
    public static void uploadImg(final Context context, String fileName, String filePath) {
        OSSClient oss;
        OSSCredentialProvider credentialProvider = new OSSPlainTextAKSKCredentialProvider(BSSMConfigtor.accessKey, BSSMConfigtor.screctKey);
//        从服务器获取token
//        OSSCredentialProvider credentialProvider1 = new STSGetter(endpoint);
        ClientConfiguration conf = new ClientConfiguration();
        conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒
        conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒
        conf.setMaxConcurrentRequest(5); // 最大并发请求书，默认5个
        conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次
        oss = new OSSClient(context.getApplicationContext(), BSSMConfigtor.END_POINT, credentialProvider, conf);
        // 构造上传请求
        PutObjectRequest put = new PutObjectRequest(BSSMConfigtor.BucketName, fileName, filePath);
//        //设置上传时header的值
//        ObjectMetadata objectMetadata = new ObjectMetadata();
//        objectMetadata.setContentType("在Web服务中定义文件的类型，决定以什么形式、什么编码读取这个文件");
//        objectMetadata.setCacheControl("");
//        objectMetadata.setContentLength(1024);
//        //md5 加密
//        objectMetadata.setContentMD5("");
//        put.setMetadata(objectMetadata);
        // 异步上传时可以设置进度回调
        put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
            @Override
            public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
                /*final int progress = (int) (100 * currentSize / totalSize);
                pb_progress.setProgress(progress);
                Log.d("PutObject", "currentSize: " + currentSize + " totalSize: " + totalSize);*/
            }
        });
        //异步上传 可在主线程中更新UI可在主线程
        OSSAsyncTask task = oss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                Log.d("PutObject", "UploadSuccess");
                Toast.makeText(context, "上传成功", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                Toast.makeText(context, "上传失败", Toast.LENGTH_LONG).show();

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
            }
        });
    }
}
