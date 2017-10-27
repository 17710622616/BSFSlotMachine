package com.bs.john_li.bsfslotmachine.BSSMActivity.Forum;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bs.john_li.bsfslotmachine.BSSMActivity.BaseActivity;
import com.bs.john_li.bsfslotmachine.BSSMActivity.Parking.ChooseCarActivity;
import com.bs.john_li.bsfslotmachine.BSSMActivity.Parking.ParkingOrderActivity;
import com.bs.john_li.bsfslotmachine.BSSMModel.CarModel;
import com.bs.john_li.bsfslotmachine.BSSMModel.ReturnContentsOutModel;
import com.bs.john_li.bsfslotmachine.BSSMUtils.BSSMCommonUtils;
import com.bs.john_li.bsfslotmachine.BSSMUtils.BSSMConfigtor;
import com.bs.john_li.bsfslotmachine.BSSMUtils.SPUtils;
import com.bs.john_li.bsfslotmachine.BSSMView.BSSMHeadView;
import com.bs.john_li.bsfslotmachine.R;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by John_Li on 5/10/2017.
 */

public class PublishForumActivity extends BaseActivity implements View.OnClickListener{
    private ImageView publish_forum_iv;
    private BSSMHeadView publish_forum_head;
    private EditText publish_artical_et, publish_artical_title_et;

    public static final int TAKE_PHOTO = 1;
    public static final int TAKE_PHOTO_FROM_ALBUM = 2;
    private File dir; //圖片文件夾路徑
    private File file;  //照片文件
    private ImageOptions options = new ImageOptions.Builder().setSize(0, 0).setFailureDrawableId(R.mipmap.car_sample).build();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_forum);
        initView();
        setListener();
        initData();
    }

    @Override
    public void initView() {
        publish_forum_iv = findViewById(R.id.publish_forum_iv);
        publish_forum_head = findViewById(R.id.publish_forum_head);
        publish_artical_et = findViewById(R.id.publish_artical_et);
        publish_artical_title_et = findViewById(R.id.publish_artical_title_et);
    }

    @Override
    public void setListener() {

    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        String startWay = intent.getStringExtra("startWay");
        publish_forum_head.setTitle("發佈帖文");
        publish_forum_head.setLeft(this);
        publish_forum_head.setRight(R.mipmap.ok, this);
        switch (startWay) {
            case "camare":
                callCamare();
                break;
            case "text":
                callText();
                break;
            case "album":
                callAlbum();
                break;
        }
    }

    private void callAlbum() {
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
    }

    private void callText() {

    }

    private void callCamare() {
        if(BSSMCommonUtils.IsThereAnAppToTakePictures(this)) {
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
            Toast.makeText(this,"您的照相機不可用哦，請檢測相機先！",Toast.LENGTH_SHORT).show();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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
                x.image().bind(publish_forum_iv, file.getPath(), options);
                break;
            case TAKE_PHOTO_FROM_ALBUM:
                String imagePath = BSSMCommonUtils.getRealFilePath(this, data.getData());
                file = new File(imagePath);
                x.image().bind(publish_forum_iv, file.getPath(), options);
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (file != null){
            outState.putString("file_path", file.getPath());
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            String cacheFileName = savedInstanceState.get("file_path").toString();
            if (cacheFileName != null) {
                if (!cacheFileName.equals("")) {
                    file = new File(cacheFileName);
                }
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.head_left:
                finish();
                break;
            case R.id.head_right:
                String content = publish_artical_title_et.getText().toString();
                String title = publish_artical_title_et.getText().toString();
                if (content != null && title != null) {
                    if (!content.equals("") && !title.equals("")) {
                        callNetPublishArticle(content, title);
                    } else {
                        Toast.makeText(PublishForumActivity.this, getString(R.string.no_net), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(PublishForumActivity.this, getString(R.string.no_net), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    /**
     * 發佈帖文
     */
    private void callNetPublishArticle(String content, String title) {
        RequestParams params = new RequestParams(BSSMConfigtor.BASE_URL + BSSMConfigtor.GET_CAR_LIST + SPUtils.get(this, "UserToken", ""));
        params.setAsJsonContent(true);
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("title",title);
            jsonObj.put("cover", "objectName1");
            jsonObj.put("contents",content);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String urlJson = jsonObj.toString();
        params.setBodyContent(urlJson);
        String uri = params.getUri();
        x.http().request(HttpMethod.POST ,params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                ReturnContentsOutModel model = new Gson().fromJson(result.toString(), ReturnContentsOutModel.class);
                if (model.getCode() ==200) {
                    
                } else {
                    Toast.makeText(PublishForumActivity.this, "提交失敗╮(╯▽╰)╭", Toast.LENGTH_SHORT).show();
                }
            }
            //请求异常后的回调方法
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(PublishForumActivity.this, getString(R.string.no_net), Toast.LENGTH_SHORT).show();
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
