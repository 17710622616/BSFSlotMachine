package com.bs.john_li.bsfslotmachine.BSSMActivity.Mine;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.bs.john_li.bsfslotmachine.BSSMActivity.BaseActivity;
import com.bs.john_li.bsfslotmachine.BSSMAdapter.OpinionPhotoAdapter;
import com.bs.john_li.bsfslotmachine.BSSMModel.CommonModel;
import com.bs.john_li.bsfslotmachine.BSSMModel.ReturnContentsOutModel;
import com.bs.john_li.bsfslotmachine.BSSMUtils.AliyunOSSUtils;
import com.bs.john_li.bsfslotmachine.BSSMUtils.BSSMCommonUtils;
import com.bs.john_li.bsfslotmachine.BSSMUtils.BSSMConfigtor;
import com.bs.john_li.bsfslotmachine.BSSMUtils.SPUtils;
import com.bs.john_li.bsfslotmachine.BSSMView.BSSMHeadView;
import com.bs.john_li.bsfslotmachine.R;
import com.google.gson.Gson;
import com.othershe.nicedialog.BaseNiceDialog;
import com.othershe.nicedialog.NiceDialog;
import com.othershe.nicedialog.ViewConvertListener;
import com.othershe.nicedialog.ViewHolder;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 意见反馈
 * Created by John_Li on 9/8/2017.
 */

public class OpinionActivity extends BaseActivity implements View.OnClickListener{
    private BSSMHeadView headView;
    private GridView opinionGv;
    private List<String> photoList;
    private OpinionPhotoAdapter mOpinionPhotoAdapter;
    // 提交帖文的提示窗
    private ProgressDialog dialog;

    private EditText contentTv, nameTv, telTv;
    public static final int TAKE_PHOTO = 1;
    public static final int TAKE_PHOTO_FROM_ALBUM = 2;
    private File dir; //圖片文件夾路徑
    private File file;  //照片文件
    private OSSClient oss;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opinion);
        initView();
        setListener();
        initData();
    }

    @Override
    public void initView() {
        headView = findViewById(R.id.opinion_head);
        contentTv = findViewById(R.id.opinion_et);
        nameTv = findViewById(R.id.opinion_name);
        telTv = findViewById(R.id.opinion_phone);
        opinionGv = findViewById(R.id.opinion_gv);
    }

    @Override
    public void setListener() {
        opinionGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if (position == photoList.size() - 1) {
                    if (photoList.size() < 6) {
                        NiceDialog.init()
                                .setLayoutId(R.layout.dialog_photo)
                                .setConvertListener(new ViewConvertListener() {
                                    @Override
                                    protected void convertView(ViewHolder viewHolder, final BaseNiceDialog baseNiceDialog) {
                                        viewHolder.setOnClickListener(R.id.photo_camare, new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                if(BSSMCommonUtils.IsThereAnAppToTakePictures(OpinionActivity.this)) {
                                                    dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "BSSMPictures");
                                                    if (!dir.exists()) {
                                                        dir.mkdir();
                                                    }

                                                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                                    SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
                                                    Date date = new Date(System.currentTimeMillis());
                                                    file = new File(dir, "location" + format.format(date) + ".jpg");
                                                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                                                    startActivityForResult(intent, TAKE_PHOTO);
                                                } else {
                                                    Toast.makeText(OpinionActivity.this,"您的照相機不可用哦，請檢測相機先！",Toast.LENGTH_SHORT).show();
                                                }
                                                baseNiceDialog.dismiss();
                                            }
                                        });
                                        viewHolder.setOnClickListener(R.id.photo_album, new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "BSSMPictures");
                                                if (!dir.exists()) {
                                                    dir.mkdir();
                                                }

                                                Intent getAlbum;
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                                                    getAlbum = new Intent(Intent.ACTION_PICK);
                                                } else {
                                                    getAlbum = new Intent(Intent.ACTION_GET_CONTENT);
                                                }
                                                getAlbum.setType("image/*");
                                                startActivityForResult(getAlbum, TAKE_PHOTO_FROM_ALBUM);
                                                baseNiceDialog.dismiss();
                                            }
                                        });
                                        viewHolder.setOnClickListener(R.id.photo_cancel, new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                baseNiceDialog.dismiss();
                                            }
                                        });
                                    }
                                })
                                .setShowBottom(true)
                                .show(getSupportFragmentManager());
                    } else {
                        Toast.makeText(OpinionActivity.this, "照片數量不可多於5張哦~", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    @Override
    public void initData() {
        headView.setTitle("意見反饋");
        headView.setLeft(this);
        headView.setRight(R.mipmap.ok,this);

        oss = AliyunOSSUtils.initOSS(this);
        photoList = new ArrayList<String>();
        // 加入初始的添加照片的圖片
        photoList.add("");
        mOpinionPhotoAdapter = new OpinionPhotoAdapter(this, photoList);
        opinionGv.setAdapter(mOpinionPhotoAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            finish();
            return;
        }
        switch(requestCode) {
            case TAKE_PHOTO:
                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                Uri contentUri = Uri.fromFile(file);
                mediaScanIntent.setData(contentUri);
                sendBroadcast(mediaScanIntent);
                photoList.add(0, file.getPath());
                mOpinionPhotoAdapter.refreshData(photoList);
                //x.image().bind(publish_forum_iv, file.getPath(), options);
                break;
            case TAKE_PHOTO_FROM_ALBUM:
                String imagePath = BSSMCommonUtils.getRealFilePath(this, data.getData());
                file = new File(imagePath);
                photoList.add(0, file.getPath());
                mOpinionPhotoAdapter.refreshData(photoList);
                //x.image().bind(publish_forum_iv, file.getPath(), options);
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.head_left:
                finish();
                break;
            case R.id.head_right:
                dialog = new ProgressDialog(this);
                dialog.setTitle("提示");
                dialog.setMessage("提交貼文中......");
                dialog.setCancelable(false);
                dialog.show();
                String content = String.valueOf(contentTv.getText());
                String name = String.valueOf(nameTv.getText().toString());
                String tel = String.valueOf(telTv.getText().toString());
                if (!content.equals("null") && !name.equals("null")&& !tel.equals("null")) {
                    hasImage(content, name, tel);
                } else {
                    dialog.dismiss();
                    Toast.makeText(OpinionActivity.this, "請填寫您要發佈的標題及內容~", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    /**
     * 發佈帖文前判斷是否有照片
     * @param content
     * @param name
     * @param tel
     */
    private void hasImage(String content, String name, String tel) {
        if (photoList.size() > 1) {    // 有添加圖片
            // 提交照片到OSS
            submitImgToOss(content, name, tel);
        } else {
            // 提交沒有照片的帖文
            callNetPublishOpinion(content, name, tel, "");
        }
    }

    private void submitImgToOss(final String content, final String name, final String tel) {
        // 提交成功的集合
        // 照片數組
        final String[] imgArr = new String[photoList.size() - 1];
        final Map<String, String> imgStatusMaps = new HashMap<>();
        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 1:
                        putNum = 0;
                        // 提交有照片的帖文
                        String cutpic = new Gson().toJson(imgArr);
                        Log.d("cutpic", cutpic);
                        callNetPublishOpinion(content, name, tel, cutpic);
                        break;
                }
            }
        };
        putImg(imgArr, handler);
    }

    // 提交的照片數量
    private int putNum;
    /**
     * 上傳圖片到OSS
     */
    private void putImg(final String[] imgArr, final Handler handler) {
        putNum ++;
        if (putNum == photoList.size() || photoList.get(putNum - 1).equals("")) {
            // 结束的处理逻辑，并退出该方法
            return;
        }

        Bitmap bitmap = BSSMCommonUtils.compressImageFromFile(photoList.get(putNum - 1), 1024f);// 按尺寸压缩图片
        File filePut = BSSMCommonUtils.compressImage(bitmap, photoList.get(putNum - 1));  //按质量压缩图片

        final String fileName = filePut.getName();
        String filePath = filePut.getPath();
        // 构造上传请求
        final PutObjectRequest put = new PutObjectRequest(BSSMConfigtor.BucketName, fileName, filePath);

        // 異步請求
        OSSAsyncTask task = oss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                Message msg = new Message();
                msg.what  = 0;
                //mHandler.sendMessage(msg);
                /*if (putNum == 1) {
                    imgStatusMaps.put("mainPic", fileName);  // 把当前上传图片成功的阿里云路径添加到集合
                } else {
                    imgStatusMaps.put("commonPic", fileName);  // 把当前上传图片成功的阿里云路径添加到集合
                }*/
                imgArr[putNum - 1] = "http://test-pic-666.oss-cn-hongkong.aliyuncs.com/" + fileName;

                // 这里进行递归单张图片上传，在外面判断是否进行跳出， 最後一張的添加圖片的空路徑所以-2
                if (putNum <= photoList.size() - 2) {
                    putImg(imgArr, handler);
                } else {
                    Message message = new Message();
                    message.what = 1;
                    handler.sendMessage(message);
                }
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

                putNum = 0;
            }
        });
    }

    /**
     *  發佈帖文
     * @param content
     * @param mobile
     * @param cutpic
     * @param name
     */
    private void callNetPublishOpinion(String content, String name, String mobile, String cutpic) {
        RequestParams params = new RequestParams(BSSMConfigtor.BASE_URL + BSSMConfigtor.SUBMIT_OPINION + SPUtils.get(this, "UserToken", ""));
        params.setAsJsonContent(true);
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("content",content);
            jsonObj.put("mobile",mobile);
            jsonObj.put("name",name);
            jsonObj.put("cutpic", cutpic);
            String token = String.valueOf(SPUtils.get(this, "UserToken", ""));
            if (token.equals("null")) {
                jsonObj.put("token", token);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String urlJson = jsonObj.toString();
        params.setBodyContent(urlJson);
        String uri = params.getUri();
        params.setConnectTimeout(30 * 1000);
        x.http().request(HttpMethod.POST ,params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                CommonModel model = new Gson().fromJson(result.toString(), CommonModel.class);
                if (model.getCode().equals("200")) {
                    Toast.makeText(OpinionActivity.this, "提交意見成功！感謝您的寶貴意見，我們會繼續努力的！", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    finish();
                } else {
                    dialog.dismiss();
                    Toast.makeText(OpinionActivity.this, "提交意見失敗," + String.valueOf(model.getMsg()), Toast.LENGTH_SHORT).show();
                }
            }
            //请求异常后的回调方法
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                dialog.dismiss();
                if (ex instanceof java.net.SocketTimeoutException) {
                    Toast.makeText(OpinionActivity.this, "請求超時，請重試", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(OpinionActivity.this, "請求錯誤，請重新提交", Toast.LENGTH_SHORT).show();
                }
            }
            //主动调用取消请求的回调方法
            @Override
            public void onCancelled(CancelledException cex) {
            }
            @Override
            public void onFinished() {
            }
        });
    }
}
