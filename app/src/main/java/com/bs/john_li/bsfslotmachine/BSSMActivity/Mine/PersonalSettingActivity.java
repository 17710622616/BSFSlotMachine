package com.bs.john_li.bsfslotmachine.BSSMActivity.Mine;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.bs.john_li.bsfslotmachine.BSSMActivity.BaseActivity;
import com.bs.john_li.bsfslotmachine.BSSMActivity.ForgetPwActivity;
import com.bs.john_li.bsfslotmachine.BSSMActivity.LoginActivity;
import com.bs.john_li.bsfslotmachine.BSSMModel.CommonModel;
import com.bs.john_li.bsfslotmachine.BSSMModel.UserInfoOutsideModel;
import com.bs.john_li.bsfslotmachine.BSSMUtils.AliyunOSSUtils;
import com.bs.john_li.bsfslotmachine.BSSMUtils.BSSMCommonUtils;
import com.bs.john_li.bsfslotmachine.BSSMUtils.BSSMConfigtor;
import com.bs.john_li.bsfslotmachine.BSSMUtils.DigestUtils;
import com.bs.john_li.bsfslotmachine.BSSMUtils.SPUtils;
import com.bs.john_li.bsfslotmachine.BSSMView.BSSMHeadView;
import com.bs.john_li.bsfslotmachine.R;
import com.google.gson.Gson;
import com.othershe.nicedialog.BaseNiceDialog;
import com.othershe.nicedialog.NiceDialog;
import com.othershe.nicedialog.ViewConvertListener;
import com.othershe.nicedialog.ViewHolder;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.io.File;
import java.io.FileOutputStream;
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 個人設置，賬戶信息
 * Created by John_Li on 4/8/2017.
 */

public class PersonalSettingActivity extends BaseActivity implements View.OnClickListener{
    private BSSMHeadView personalHead;
    private LinearLayout headPortraitLL, usernameLL, phoneLL, pwLL, payPwLL;
    private TextView nickNameTv, phoneNumTv, payPwTv;
    private ImageView headIv;
    private String nickName, phoneNum, loginPw;
    private UserInfoOutsideModel.UserInfoModel mUserInfoModel;

    public static final int TAKE_PHOTO = 1;
    public static final int TAKE_PHOTO_FROM_ALBUM = 2;
    private static final int REQUEST_SMALL_IMAGE_CUTTING = 3;
    private File dir; //圖片文件夾路徑
    private File file;  //照片文件
    private ImageOptions options = new ImageOptions.Builder().setSize(0, 0).setLoadingDrawableId(R.mipmap.head_boy).setFailureDrawableId(R.mipmap.head_boy).build();
    private OSSClient oss;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:     // 頭像提交至OSS成功
                    mUserInfoModel.setHeadimg(file.getName());
                    updateUserHeadImg();
                    break;
                case -1:    // 頭像提交至OSS失敗
                    Toast.makeText(PersonalSettingActivity.this, "上傳頭像失敗，請重試！", Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_setting);
        initView();
        setListener();
        initData();
    }

    @Override
    public void initView() {
        personalHead = (BSSMHeadView) findViewById(R.id.personal_head);
        headPortraitLL = findViewById(R.id.personal_head_portrait);
        usernameLL = findViewById(R.id.personal_nickname);
        phoneLL = findViewById(R.id.personal_phone);
        pwLL = findViewById(R.id.personal_pw);
        payPwLL = findViewById(R.id.personal_pay_pw);
        nickNameTv = findViewById(R.id.personal_nickname_tv);
        phoneNumTv = findViewById(R.id.personal_phone_tv);
        payPwTv = findViewById(R.id.personal_pay_pw_tv);
        headIv = findViewById(R.id.personal_head_portrait_iv);
    }

    @Override
    public void setListener() {
        headPortraitLL.setOnClickListener(this);
        usernameLL.setOnClickListener(this);
        //phoneLL.setOnClickListener(this);
        pwLL.setOnClickListener(this);
        payPwLL.setOnClickListener(this);
    }

    @Override
    public void initData() {
        personalHead.setTitle("賬戶信息");
        personalHead.setLeft(this);
        oss = AliyunOSSUtils.initOSS(this);

        String userInfoJson = String.valueOf(SPUtils.get(this, "UserInfo", "").toString());
        String hasPayPw = String.valueOf(SPUtils.get(this, "HasPayPw", "0")).toString();
        getHasPayPw();
        if (!userInfoJson.equals("")) {
            mUserInfoModel = new Gson().fromJson(userInfoJson, UserInfoOutsideModel.UserInfoModel.class);
            nickNameTv.setText(mUserInfoModel.getNickname());
            phoneNumTv.setText(BSSMCommonUtils.change3to6ByStar(mUserInfoModel.getMobile()));
            //AliyunOSSUtils.downloadImg(mUserInfoModel.getHeadimg(), oss, headIv, this, R.mipmap.head_boy);
            x.image().bind(headIv, mUserInfoModel.getHeadimg(), options);
        }

        if (hasPayPw.equals("1")) {
            payPwTv.setText("修改支付密碼");
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.head_left:
                finish();
                break;
            case R.id.personal_head_portrait:   // 頭像
                //startActivityForResult(new Intent(this, HeadViewActivity.class), 7);
                NiceDialog.init()
                        .setLayoutId(R.layout.dialog_photo)
                        .setConvertListener(new ViewConvertListener() {
                            @Override
                            protected void convertView(ViewHolder viewHolder, final BaseNiceDialog baseNiceDialog) {
                                viewHolder.setOnClickListener(R.id.photo_camare, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        if(BSSMCommonUtils.IsThereAnAppToTakePictures(PersonalSettingActivity.this)) {
                                            dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "BSSMPictures");
                                            if (!dir.exists()) {
                                                dir.mkdir();
                                            }

                                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                            SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
                                            Date date = new Date(System.currentTimeMillis());
                                            file = new File(dir, "head" + format.format(date) + ".jpg");
                                            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                                            startActivityForResult(intent, TAKE_PHOTO);
                                        } else {
                                            Toast.makeText(PersonalSettingActivity.this,"您的照相機不可用哦，請檢測相機先！",Toast.LENGTH_SHORT).show();
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
                                        //startActivityForResult(getAlbum, TAKE_PHOTO_FROM_ALBUM);
                                        startActivityForResult(getAlbum, REQUEST_SMALL_IMAGE_CUTTING);
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
                break;
            case R.id.personal_nickname:    // 暱稱1
                NiceDialog.init()
                        .setLayoutId(R.layout.dialog_car_edit)
                        .setConvertListener(new ViewConvertListener() {
                            @Override
                            public void convertView(ViewHolder holder, final BaseNiceDialog dialog) {
                                final EditText editText = holder.getView(R.id.car_edit);
                                editText.setHint("請輸入新的暱稱");
                                BSSMCommonUtils.showKeyboard(editText);
                                holder.setOnClickListener(R.id.car_edit_submit, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        if (!editText.getText().toString().equals("")) {
                                            mUserInfoModel.setNickname(editText.getText().toString());
                                            updateUserInfo(1);
                                            dialog.dismiss();
                                        } else {
                                            Toast.makeText(PersonalSettingActivity.this, "請輸入要修改的新的暱稱~", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        })
                        .setShowBottom(true)
                        .show(getSupportFragmentManager());
                break;
            case R.id.personal_phone://修改手機號碼
                /*NiceDialog.init()
                        .setLayoutId(R.layout.dialog_car_edit)
                        .setConvertListener(new ViewConvertListener() {
                            @Override
                            public void convertView(ViewHolder holder, final BaseNiceDialog dialog) {
                                final EditText editText = holder.getView(R.id.car_edit);
                                editText.setInputType(InputType.TYPE_CLASS_PHONE);
                                editText.setHint("請輸入新的手機號碼");
                                BSSMCommonUtils.showKeyboard(editText);
                                holder.setOnClickListener(R.id.car_edit_submit, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        if (!editText.getText().toString().equals("")) {
                                            mUserInfoModel.setMobile(editText.getText().toString());
                                            phoneNumTv.setText(mUserInfoModel.getMobile());
                                            dialog.dismiss();
                                        } else {
                                            Toast.makeText(PersonalSettingActivity.this, "請輸入要修改的手機號碼~", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        })
                        .setShowBottom(true)
                        .show(getSupportFragmentManager());
                break;*/
            case R.id.personal_pw://修改登錄密碼
                Intent intent = new Intent(this, ForgetPwActivity.class);
                intent.putExtra("updatePw", "updatePw");
                startActivityForResult(intent, BSSMConfigtor.REQUEST_CODE);
                break;
            case R.id.personal_pay_pw:  // 修改支付密碼
                if (SPUtils.get(this, "HasPayPw", "").equals("1")) {
                    NiceDialog.init()
                            .setLayoutId(R.layout.dialog_update_pw)
                            .setConvertListener(new ViewConvertListener() {
                                @Override
                                public void convertView(ViewHolder holder, final BaseNiceDialog dialog) {
                                    final EditText oldPwEt = holder.getView(R.id.old_pw);
                                    final EditText newPwEt = holder.getView(R.id.new_pw);
                                    final EditText newPwAffirm = holder.getView(R.id.new_pw_affirm);
                                    final LinearLayout loadingLL = holder.getView(R.id.dialog_pw_loading_ll);
                                    BSSMCommonUtils.showKeyboard(oldPwEt);
                                    oldPwEt.setHint("請輸入舊密碼");
                                    newPwEt.setHint("請輸入新密碼");
                                    newPwAffirm.setHint("請再次輸入新密碼");
                                    holder.setOnClickListener(R.id.update_pw_submit, new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            if (!oldPwEt.getText().toString().equals("") && !newPwEt.getText().toString().equals("") && !newPwAffirm.getText().toString().equals("")) {
                                                if (newPwEt.getText().toString().equals(newPwAffirm.getText().toString())) {
                                                    String oldPayPw = oldPwEt.getText().toString();
                                                    String payPw = newPwEt.getText().toString();
                                                    loadingLL.setVisibility(View.VISIBLE);
                                                    callNetUpdatePayPw(oldPayPw, payPw, loadingLL, dialog);
                                                } else {
                                                    Toast.makeText(PersonalSettingActivity.this, "新舊支付密碼不一致", Toast.LENGTH_LONG).show();
                                                }
                                            } else {
                                                Toast.makeText(PersonalSettingActivity.this, "新舊密碼及確認密碼都不可以為空哦~", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                                }
                            })
                            .setShowBottom(true)
                            .show(getSupportFragmentManager());
                } else {
                    NiceDialog.init()
                            .setLayoutId(R.layout.dialog_update_pw)
                            .setConvertListener(new ViewConvertListener() {
                                @Override
                                public void convertView(ViewHolder holder, final BaseNiceDialog dialog) {
                                    final EditText oldPwEt = holder.getView(R.id.old_pw);
                                    final EditText newPwEt = holder.getView(R.id.new_pw);
                                    final EditText newPwAffirm = holder.getView(R.id.new_pw_affirm);
                                    final LinearLayout loadingLL = holder.getView(R.id.dialog_pw_loading_ll);
                                    oldPwEt.setVisibility(View.GONE);
                                    BSSMCommonUtils.showKeyboard(oldPwEt);
                                    newPwEt.setHint("請輸入密碼");
                                    newPwAffirm.setHint("請再次輸入密碼");
                                    holder.setOnClickListener(R.id.update_pw_submit, new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            if (!newPwEt.getText().toString().equals("") && !newPwAffirm.getText().toString().equals("")) {
                                                if (newPwEt.getText().toString().equals(newPwAffirm.getText().toString())) {
                                                    String payPw = newPwEt.getText().toString();
                                                    loadingLL.setVisibility(View.VISIBLE);
                                                    callNetCreatePayPw(payPw, loadingLL, dialog);
                                                } else {
                                                    Toast.makeText(PersonalSettingActivity.this, "新支付密碼與確認新支付密碼不一致", Toast.LENGTH_LONG).show();
                                                }
                                            } else {
                                                Toast.makeText(PersonalSettingActivity.this, "新舊密碼及確認密碼都不可以為空哦~", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            })
                            .setShowBottom(true)
                            .show(getSupportFragmentManager());
                }
                break;
        }
    }

    /**
     * 修改支付密碼
     */
    private void callNetUpdatePayPw(String oldPayPw, String payPw, final LinearLayout loadingLL, final BaseNiceDialog dialog) {
        RequestParams params = new RequestParams(BSSMConfigtor.BASE_URL + BSSMConfigtor.CHANGE_USER_PAY_PW + SPUtils.get(this, "UserToken", ""));
        params.setAsJsonContent(true);
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("oldpwd", DigestUtils.getMD5Str(oldPayPw));
            jsonObj.put("paypwd", DigestUtils.getMD5Str(payPw));
            jsonObj.put("timestamp", System.currentTimeMillis());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String urlJson = jsonObj.toString();
        params.setBodyContent(urlJson);
        params.setConnectTimeout(30 * 1000);
        x.http().request(HttpMethod.POST, params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                CommonModel model = new Gson().fromJson(result.toString(), CommonModel.class);
                if (model.getCode().equals("200")) {
                    if (model.getData().equals("true")) {
                        Toast.makeText(PersonalSettingActivity.this, "修改密碼成功！", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    } else {
                        Toast.makeText(PersonalSettingActivity.this, "修改密碼失敗，請重試或聯繫客服！", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(PersonalSettingActivity.this, String.valueOf(model.getMsg()), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if (ex instanceof SocketTimeoutException) {
                    Toast.makeText(PersonalSettingActivity.this, "修改密碼超時，請重試！", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(PersonalSettingActivity.this, "修改密碼失敗，請重試或聯繫客服！", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                loadingLL.setVisibility(View.GONE);
            }
        });
    }

    /**
     * 創建支付密碼
     */
    private void callNetCreatePayPw(String payPw, final LinearLayout loadingLL, final BaseNiceDialog dialog) {
        RequestParams params = new RequestParams(BSSMConfigtor.BASE_URL + BSSMConfigtor.CREATE_USER_PAY_PW + SPUtils.get(this, "UserToken", ""));
        params.setAsJsonContent(true);
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("paypwd", DigestUtils.getMD5Str(payPw));
            jsonObj.put("timestamp", System.currentTimeMillis());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String urlJson = jsonObj.toString();
        params.setBodyContent(urlJson);
        params.setConnectTimeout(30 * 1000);
        x.http().request(HttpMethod.POST, params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                CommonModel model = new Gson().fromJson(result.toString(), CommonModel.class);
                if (model.getCode().equals("200")) {
                    if (model.getData().equals("true")) {
                        SPUtils.put(PersonalSettingActivity.this, "HasPayPw", "1");
                    } else {
                        Toast.makeText(PersonalSettingActivity.this, "創建密碼失敗，請重試或聯繫客服！", Toast.LENGTH_SHORT).show();
                    }
                } else if(model.getCode().equals("10001")) {
                    payPwTv.setText("修改支付密碼");
                    SPUtils.put(PersonalSettingActivity.this, "HasPayPw", "1");
                    dialog.dismiss();
                } else {
                    Toast.makeText(PersonalSettingActivity.this, String.valueOf(model.getMsg()), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if (ex instanceof SocketTimeoutException) {
                    Toast.makeText(PersonalSettingActivity.this, "創建密碼超時，請重試！", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(PersonalSettingActivity.this, "創建密碼失敗，請重試或聯繫客服！", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                loadingLL.setVisibility(View.GONE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 100:     // 修改密碼返回
                if (data != null) {
                    doLogin(data.getStringExtra("mobile"), data.getStringExtra("password"));
                }
                //finish();
                break;
            case TAKE_PHOTO:
                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                Uri contentUri = Uri.fromFile(file);
                mediaScanIntent.setData(contentUri);
                sendBroadcast(mediaScanIntent);
                // 上傳頭像
                putImg();
                x.image().bind(headIv, file.getPath(), options);
                break;
            case TAKE_PHOTO_FROM_ALBUM:
                /*String imagePath = BSSMCommonUtils.getRealFilePath(this, data.getData());
                file = new File(imagePath);
                headIv.setImageBitmap(BitmapFactory.decodeFile(file.getAbsolutePath()));*/
                if (data != null) {
                    Bundle extras = data.getExtras();
                    if (extras != null){
                        Bitmap photo = extras.getParcelable("data"); // 直接获得内存中保存的 bitmap
                        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
                        Date date = new Date(System.currentTimeMillis());
                        file = new File(dir, "head" + format.format(date) + ".jpg");
                        // 保存图片
                        FileOutputStream outputStream = null;
                        try {
                            outputStream = new FileOutputStream(file);
                            photo.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                            outputStream.flush();
                            outputStream.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        // 上傳頭像
                        putImg();
                        // 在视图中显示图片
                        headIv.setImageBitmap(photo);
                    } else {
                        Toast.makeText(this, "圖片保存失敗", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(this, "圖片保存失敗", Toast.LENGTH_LONG).show();
                }
                break;
            case REQUEST_SMALL_IMAGE_CUTTING:
                try {
                    Intent intent = new Intent("com.android.camera.action.CROP");
                    intent.setDataAndType(data.getData(), "image/*");
                    intent.putExtra("crop", "true");
                    intent.putExtra("aspectX", 1); // 裁剪框比例
                    intent.putExtra("aspectY", 1);
                    intent.putExtra("outputX", 300); // 输出图片大小
                    intent.putExtra("outputY", 300);
                    intent.putExtra("scale", true);
                    intent.putExtra("return-data", true);
                    startActivityForResult(intent, TAKE_PHOTO_FROM_ALBUM);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    /**
     * 更改用戶信息
     */
    public void updateUserInfo(final int type) {
        RequestParams params = new RequestParams(BSSMConfigtor.BASE_URL + BSSMConfigtor.UPDATE_USER_INFO + SPUtils.get(this, "UserToken", ""));
        params.setAsJsonContent(true);
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("nickname",mUserInfoModel.getNickname());
            jsonObj.put("birthday",mUserInfoModel.getBirthday());
            jsonObj.put("realname",mUserInfoModel.getRealname());
            jsonObj.put("descx",mUserInfoModel.getDescx());
            jsonObj.put("sex",mUserInfoModel.getSex());
            jsonObj.put("idcardno",mUserInfoModel.getIdcardno());
            jsonObj.put("address",mUserInfoModel.getAddress());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String url = jsonObj.toString();
        params.setBodyContent(url);
        params.setConnectTimeout(30 * 1000);
        x.http().request(HttpMethod.POST ,params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                UserInfoOutsideModel model = new Gson().fromJson(result.toString(), UserInfoOutsideModel.class);
                if (model.getCode() == 200) {
                    mUserInfoModel.setNickname(model.getData().getNickname());
                    mUserInfoModel.setAddress(model.getData().getAddress());
                    mUserInfoModel.setMobile(model.getData().getMobile());
                    mUserInfoModel.setBirthday(model.getData().getBirthday());
                    mUserInfoModel.setDescx(model.getData().getDescx());
                    mUserInfoModel.setIdcardno(model.getData().getIdcardno());
                    mUserInfoModel.setRealname(model.getData().getRealname());
                    mUserInfoModel.setSex(model.getData().getSex());
                    mUserInfoModel.setHeadimg(model.getData().getHeadimg());
                    String userInfoJson = new Gson().toJson(mUserInfoModel);
                    SPUtils.put(PersonalSettingActivity.this, "UserInfo", userInfoJson);
                    EventBus.getDefault().post("LOGIN");
                    Toast.makeText(PersonalSettingActivity.this, "用戶信息更新成功~", Toast.LENGTH_SHORT).show();
                    switch (type) {
                        case 1:
                            nickNameTv.setText(mUserInfoModel.getNickname());
                            break;
                    }
                } else {
                    Toast.makeText(PersonalSettingActivity.this, "用戶信息更新失敗╮(╯▽╰)╭" + String.valueOf(model.getMsg()), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if (ex instanceof SocketTimeoutException) {
                    Toast.makeText(PersonalSettingActivity.this, "用戶信息更新超時，請重試！", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(PersonalSettingActivity.this, "用戶信息更新失敗╮(╯▽╰)╭", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    /**
     * 更改用戶头像
     */
    public void updateUserHeadImg() {
        RequestParams params = new RequestParams(BSSMConfigtor.BASE_URL + BSSMConfigtor.UPDATE_USER_HEAD_IMG + mUserInfoModel.getHeadimg() + "&token=" + SPUtils.get(this, "UserToken", ""));
        params.setConnectTimeout(30 * 1000);
        x.http().request(HttpMethod.GET ,params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                UserInfoOutsideModel model = new Gson().fromJson(result.toString(), UserInfoOutsideModel.class);
                if (model.getCode() == 200) {
                    String userInfoJson = new Gson().toJson(mUserInfoModel);
                    SPUtils.put(PersonalSettingActivity.this, "UserInfo", userInfoJson);
                    EventBus.getDefault().post("LOGIN");
                    Toast.makeText(PersonalSettingActivity.this, "上傳頭像成功！", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(PersonalSettingActivity.this, "上傳頭像失敗，請重試！" + String.valueOf(model.getMsg()), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if (ex instanceof SocketTimeoutException) {
                    Toast.makeText(PersonalSettingActivity.this, "上傳頭像超時，請重試！", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(PersonalSettingActivity.this, "上傳頭像失敗，請重試！", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    /**
     * 登錄
     */
    private void doLogin(String username, String pw) {
        RequestParams params = new RequestParams(BSSMConfigtor.BASE_URL + BSSMConfigtor.USER_LOGIN);
        params.setAsJsonContent(true);
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("mobile",username);
            jsonObj.put("password",pw);
            jsonObj.put("osVersion",BSSMConfigtor.OS_TYPE);
            jsonObj.put("osType",BSSMConfigtor.OS_TYPE);
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
                    SPUtils.put(PersonalSettingActivity.this, "UserToken", model.getData().toString());
                    Toast.makeText(PersonalSettingActivity.this, getString(R.string.login_success), Toast.LENGTH_SHORT).show();
                    //setResult(BSSMConfigtor.LOGIN_FOR_RESULT);
                    getUserInfo(model.getData().toString());
                } else {
                    Toast.makeText(PersonalSettingActivity.this, getString(R.string.login_fail) + String.valueOf(model.getMsg()), Toast.LENGTH_SHORT).show();
                }
            }
            //请求异常后的回调方法
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if (ex instanceof SocketTimeoutException) {
                    Toast.makeText(PersonalSettingActivity.this, "登錄超時，請重試！", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(PersonalSettingActivity.this, getString(R.string.no_net), Toast.LENGTH_SHORT).show();
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

    /**
     * 獲取用戶信息
     * @param token
     */
    private void getUserInfo(String token) {
        RequestParams params = new RequestParams(BSSMConfigtor.BASE_URL + BSSMConfigtor.GET_USER_INFO + token);
        params.setConnectTimeout(30 * 1000);
        x.http().request(HttpMethod.GET ,params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                UserInfoOutsideModel model = new Gson().fromJson(result.toString(), UserInfoOutsideModel.class);
                if (model.getCode() == 200) {
                    if (model.getData().getNickname() == null) {
                        model.getData().setNickname("用戶");
                    }
                    if (model.getData().getAddress() == null) {
                        model.getData().setAddress("");
                    }
                    if (model.getData().getRealname() == null) {
                        model.getData().setRealname("");
                    }
                    if (model.getData().getHeadimg() == null) {
                        model.getData().setAddress("objectNam1");
                    }
                    if (model.getData().getDescx() == null) {
                        model.getData().setDescx("");
                    }
                    if (model.getData().getIdcardno() == null) {
                        model.getData().setIdcardno("");
                    }
                    if (model.getData().getBirthday() == null) {
                        model.getData().setBirthday("");
                    }
                    String userInfoJson = new Gson().toJson(model.getData());
                    SPUtils.put(PersonalSettingActivity.this, "UserInfo", userInfoJson);
                    Log.d("getUserURI", "獲取用戶信息成功");
                    EventBus.getDefault().post("LOGIN");
                    finish();
                } else {
                    Log.d("getUserURI", "獲取用戶信息失敗");
                }
            }
            //请求异常后的回调方法
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.d("getUserURI", "獲取用戶信息失敗");
                //Toast.makeText(LoginActivity.this, getString(R.string.no_net), Toast.LENGTH_SHORT).show();
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

    /**
     * 判断是否有支付密码
     */
    private void getHasPayPw() {
        RequestParams params = new RequestParams(BSSMConfigtor.BASE_URL + BSSMConfigtor.GET_USER_HAS_PAY_PW + SPUtils.get(this, "UserToken", ""));
        params.setConnectTimeout(30 * 1000);
        x.http().request(HttpMethod.GET ,params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                CommonModel model = new Gson().fromJson(result.toString(), CommonModel.class);
                if (model.getCode().equals("200")) {
                    //String hasPayPw = new Gson().toJson(model.getData()).toString();
                    String hasPayPw = model.getData().toString();
                    if (hasPayPw.equals("true")) {
                        payPwTv.setText("修改支付密碼");
                        SPUtils.put(PersonalSettingActivity.this, "HasPayPw", "1");
                    } else {
                        Log.d("HasPayPw","获取支付密码失败");
                    }
                }
            }
            //请求异常后的回调方法
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.d("HasPayPw","获取支付密码失败");
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (file != null){
            outState.putString("file_path", file.getPath());
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        String cacheFileName = savedInstanceState.get("file_path").toString();
        if (cacheFileName != null) {
            if (!cacheFileName.equals("")) {
                file = new File(cacheFileName);
            }
        }

        if (dir == null) {
            new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "BSSMPictures");
        }
    }

    /**
     * 上傳圖片到OSS
     */
    private void putImg() {
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
}
