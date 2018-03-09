package com.bs.john_li.bsfslotmachine.BSSMActivity.Forum;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bs.john_li.bsfslotmachine.BSSMActivity.BaseActivity;
import com.bs.john_li.bsfslotmachine.BSSMActivity.Parking.ChooseCarActivity;
import com.bs.john_li.bsfslotmachine.BSSMActivity.Parking.ParkingOrderActivity;
import com.bs.john_li.bsfslotmachine.BSSMAdapter.PhotoAdapter;
import com.bs.john_li.bsfslotmachine.BSSMModel.CarModel;
import com.bs.john_li.bsfslotmachine.BSSMModel.ReturnContentsOutModel;
import com.bs.john_li.bsfslotmachine.BSSMUtils.BSSMCommonUtils;
import com.bs.john_li.bsfslotmachine.BSSMUtils.BSSMConfigtor;
import com.bs.john_li.bsfslotmachine.BSSMUtils.SPUtils;
import com.bs.john_li.bsfslotmachine.BSSMView.BSSMHeadView;
import com.bs.john_li.bsfslotmachine.BSSMView.NoScrollGridView;
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
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by John_Li on 5/10/2017.
 */

public class PublishForumActivity extends BaseActivity implements View.OnClickListener{
    //private ImageView publish_forum_iv;
    private BSSMHeadView publish_forum_head;
    private EditText publish_artical_et, publish_artical_title_et;

    public static final int TAKE_PHOTO = 1;
    public static final int TAKE_PHOTO_FROM_ALBUM = 2;
    private File dir; //圖片文件夾路徑
    private File file;  //照片文件
    private ImageOptions options = new ImageOptions.Builder().setSize(0, 0).setFailureDrawableId(R.mipmap.car_sample).build();
    private List<String> imgUrlList;
    private NoScrollGridView photoGv;
    private PhotoAdapter mPhotoAdapter;
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
        //publish_forum_iv = findViewById(R.id.publish_forum_iv);
        publish_forum_head = findViewById(R.id.publish_forum_head);
        publish_artical_et = findViewById(R.id.publish_artical_et);
        publish_artical_title_et = findViewById(R.id.publish_artical_title_et);
        photoGv = findViewById(R.id.publish_forum_gv);
    }

    @Override
    public void setListener() {
        photoGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == imgUrlList.size() - 1) {
                    if (imgUrlList.size() < 6) {
                        NiceDialog.init()
                                .setLayoutId(R.layout.dialog_photo)
                                .setConvertListener(new ViewConvertListener() {
                                    @Override
                                    protected void convertView(ViewHolder viewHolder, final BaseNiceDialog baseNiceDialog) {
                                        viewHolder.setOnClickListener(R.id.photo_camare, new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                if(BSSMCommonUtils.IsThereAnAppToTakePictures(PublishForumActivity.this)) {
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
                                                    Toast.makeText(PublishForumActivity.this,"您的照相機不可用哦，請檢測相機先！",Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(PublishForumActivity.this, "照片數量不可多於5張哦~", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        String startWay = intent.getStringExtra("startWay");
        publish_forum_head.setTitle("發佈帖文");
        publish_forum_head.setLeft(this);
        publish_forum_head.setRight(R.mipmap.ok, this);
        imgUrlList = new ArrayList<>();
        mPhotoAdapter = new PhotoAdapter(this, imgUrlList);
        photoGv.setAdapter(mPhotoAdapter);

        // 加入初始的添加照片的圖片
        imgUrlList.add("");
        // 判斷打開方式
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
                imgUrlList.add(0, file.getPath());
                mPhotoAdapter.refreshData(imgUrlList);
                //x.image().bind(publish_forum_iv, file.getPath(), options);
                break;
            case TAKE_PHOTO_FROM_ALBUM:
                String imagePath = BSSMCommonUtils.getRealFilePath(this, data.getData());
                file = new File(imagePath);
                imgUrlList.add(0, file.getPath());
                mPhotoAdapter.refreshData(imgUrlList);
                //x.image().bind(publish_forum_iv, file.getPath(), options);
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
                String content = publish_artical_et.getText().toString();
                String title = publish_artical_title_et.getText().toString();
                if (content != null && title != null) {
                    if (!content.equals("") && !title.equals("")) {
                        hasImage(content, title);
                    } else {
                        Toast.makeText(PublishForumActivity.this, "請填寫您要發佈的標題及內容~", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(PublishForumActivity.this, "請填寫您要發佈的標題及內容~", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    /**
     * 發佈帖文前判斷是否有照片
     * @param content
     * @param title
     */
    private void hasImage(String content, String title) {
        if (imgUrlList.size() > 1) {    // 有添加圖片
            // 提交照片到OSS
            String cover = "";
            // 提交有照片的帖文
            callNetPublishArticle(content, title, cover);
        } else {
            // 提交沒有照片的帖文
            callNetPublishArticle(content, title, "");
        }
    }

    /**
     *  發佈帖文
     * @param content
     * @param title
     * @param cover
     */
    private void callNetPublishArticle(String content, String title, String cover) {
        RequestParams params = new RequestParams(BSSMConfigtor.BASE_URL + BSSMConfigtor.PUBLISH_ARTICLE + SPUtils.get(this, "UserToken", ""));
        params.setAsJsonContent(true);
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("contents",content);
            jsonObj.put("title",title);
            if (!cover.equals("")) {
                jsonObj.put("cover", cover);
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
                ReturnContentsOutModel model = new Gson().fromJson(result.toString(), ReturnContentsOutModel.class);
                if (model.getCode() ==200) {
                    Intent intent = new Intent();
                    String contents = model.getData().getContents();
                    String contents1 = model.getData().getCreator();
                    String contents2 = model.getData().getTitle();
                    String content3s = model.getData().getCover();
                    intent.putExtra("return_contents", new Gson().toJson(model.getData()));
                    setResult(RESULT_OK, intent);
                    finish();
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
