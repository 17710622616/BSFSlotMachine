package com.bs.john_li.bsfslotmachine.BSSMActivity.Mine;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
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

import com.bs.john_li.bsfslotmachine.BSSMActivity.BaseActivity;
import com.bs.john_li.bsfslotmachine.BSSMActivity.ForgetPwActivity;
import com.bs.john_li.bsfslotmachine.BSSMActivity.LoginActivity;
import com.bs.john_li.bsfslotmachine.BSSMModel.CommonModel;
import com.bs.john_li.bsfslotmachine.BSSMModel.UserInfoOutsideModel;
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

import org.greenrobot.eventbus.EventBus;
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
import java.util.Timer;
import java.util.TimerTask;

/**
 * 個人設置，賬戶信息
 * Created by John_Li on 4/8/2017.
 */

public class PersonalSettingActivity extends BaseActivity implements View.OnClickListener{
    private BSSMHeadView personalHead;
    private LinearLayout headPortraitLL, usernameLL, phoneLL, pwLL, payPwLL;
    private TextView nickNameTv,phoneNumTv;
    private ImageView headIv;
    private String nickName, phoneNum, loginPw, payPw;
    private UserInfoOutsideModel.UserInfoModel mUserInfoModel;

    public static final int TAKE_PHOTO = 1;
    public static final int TAKE_PHOTO_FROM_ALBUM = 0;
    private static final int REQUEST_SMALL_IMAGE_CUTTING = 2;
    private File dir; //圖片文件夾路徑
    private File file;  //照片文件
    private ImageOptions options = new ImageOptions.Builder().setSize(0, 0).setFailureDrawableId(R.mipmap.head_boy).build();
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

        String userInfoJson = (String) SPUtils.get(this, "UserInfo", "");
        if (!userInfoJson.equals("")) {
            mUserInfoModel = new Gson().fromJson(userInfoJson, UserInfoOutsideModel.UserInfoModel.class);
            nickNameTv.setText(mUserInfoModel.getNickname());
            phoneNumTv.setText(BSSMCommonUtils.change3to6ByStar(mUserInfoModel.getMobile()));
            AliyunOSSUtils.downloadImg(mUserInfoModel.getHeadimg(), AliyunOSSUtils.initOSS(this), headIv, this);
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
                                            file = new File(dir, "car" + format.format(date) + ".jpg");
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
                NiceDialog.init()
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
                break;
            case R.id.personal_pw://修改登錄密碼
                startActivityForResult(new Intent(this, ForgetPwActivity.class), BSSMConfigtor.REQUEST_CODE);
                break;
            case R.id.personal_pay_pw:  // 修改支付密碼
                NiceDialog.init()
                        .setLayoutId(R.layout.dialog_update_pw)
                        .setConvertListener(new ViewConvertListener() {
                            @Override
                            public void convertView(ViewHolder holder, final BaseNiceDialog dialog) {
                                final EditText oldPwEt = holder.getView(R.id.old_pw);
                                final EditText newPwEt = holder.getView(R.id.new_pw);
                                final EditText newPwAffirm = holder.getView(R.id.new_pw_affirm);
                                BSSMCommonUtils.showKeyboard(oldPwEt);
                                oldPwEt.setHint("請輸入舊密碼");
                                newPwEt.setHint("請輸入新密碼");
                                newPwAffirm.setHint("請再次輸入新密碼");
                                holder.setOnClickListener(R.id.update_pw_submit, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        if (!oldPwEt.getText().toString().equals("") && !oldPwEt.getText().toString().equals("") && !oldPwEt.getText().toString().equals("")) {
                                            payPw = newPwEt.getText().toString();
                                            dialog.dismiss();
                                        } else {
                                            Toast.makeText(PersonalSettingActivity.this, "新舊密碼及確認密碼都不可以為空哦~", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        })
                        .setShowBottom(true)
                        .show(getSupportFragmentManager());
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 100:     // 修改密碼返回
                doLogin(data.getStringExtra("mobile"), data.getStringExtra("password"));
                //finish();
                break;
            case TAKE_PHOTO:
                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                Uri contentUri = Uri.fromFile(file);
                mediaScanIntent.setData(contentUri);
                sendBroadcast(mediaScanIntent);
                x.image().bind(headIv, file.getPath(), options);
                break;
            case TAKE_PHOTO_FROM_ALBUM:
                String imagePath = BSSMCommonUtils.getRealFilePath(this, data.getData());
                file = new File(imagePath);
                headIv.setImageBitmap(BitmapFactory.decodeFile(file.getAbsolutePath()));
                break;
            case REQUEST_SMALL_IMAGE_CUTTING:
                String imagePath1 = BSSMCommonUtils.getRealFilePath(this, data.getData());
                file = new File(imagePath1);
                x.image().bind(headIv, file.getPath(), options);
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
                    Toast.makeText(PersonalSettingActivity.this, "用戶信息更新失敗╮(╯▽╰)╭", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(PersonalSettingActivity.this, "用戶信息更新失敗╮(╯▽╰)╭", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(PersonalSettingActivity.this, getString(R.string.login_fail), Toast.LENGTH_SHORT).show();
                }
            }
            //请求异常后的回调方法
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(PersonalSettingActivity.this, getString(R.string.no_net), Toast.LENGTH_SHORT).show();
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
    }
}
