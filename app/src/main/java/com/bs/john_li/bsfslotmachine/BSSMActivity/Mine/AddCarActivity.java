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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import com.bs.john_li.bsfslotmachine.BSSMModel.CarModel;
import com.bs.john_li.bsfslotmachine.BSSMModel.CommonModel;
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
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.io.File;
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 添加車輛
 * Created by John_Li on 27/9/2017.
 */

public class AddCarActivity extends BaseActivity implements View.OnClickListener{
    private BSSMHeadView headView;
    private LinearLayout carPhotoLL, carTypeLL, carNoLL, carModelLL, carBrandLL, carStyleLL;
    private ImageView carPhotoIv;
    private TextView carTypeTv, carNoTv, carModelTv, carBrandTv, carStyleTv,outPutTv, deleteCarTV;
    private ProgressBar ivProgress;

    public static final int TAKE_PHOTO = 1;
    public static final int TAKE_PHOTO_FROM_ALBUM = 2;
    private File dir; //圖片文件夾路徑
    private File file;  //照片文件
    private ImageOptions options = new ImageOptions.Builder().setSize(0, 0).setLoadingDrawableId(R.mipmap.img_loading).setFailureDrawableId(R.mipmap.load_img_fail_list).build();
    private CarModel.CarCountAndListModel.CarInsideModel carInsideModel;
    private String carType, carNo, carModel, carBrand, carStyle;
    private String startWay;
    //负责所有的界面更新
    private OSSClient oss;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:     // 車輛圖片提交至OSS成功
                    if (startWay.equals("update")) {
                        // 修改車輛信息
                        callNetUpdateCar();
                    } else {
                        // 提交車輛信息
                        callNetSubmiteCar();
                    }
                    break;
                case -1:    // 車輛圖片提交至OSS失敗
                    Toast.makeText(AddCarActivity.this, "車輛圖片上傳失敗，請重試！", Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_car);
        initView();
        setListener();
        initData();
    }

    @Override
    public void initView() {
        headView = findViewById(R.id.add_car_head);
        carPhotoLL = findViewById(R.id.add_car_photo_ll);
        carTypeLL = findViewById(R.id.add_car_type_ll);
        carNoLL = findViewById(R.id.add_car_no_ll);
        carModelLL = findViewById(R.id.add_car_model_ll);
        carBrandLL = findViewById(R.id.add_car_brand_ll);
        carStyleLL = findViewById(R.id.add_car_style_ll);
        carPhotoIv = findViewById(R.id.add_car_photo_iv);
        carTypeTv = findViewById(R.id.add_car_type_tv);
        carNoTv = findViewById(R.id.add_car_no_tv);
        carModelTv = findViewById(R.id.add_car_model_tv);
        carBrandTv = findViewById(R.id.add_car_brand_tv);
        carStyleTv = findViewById(R.id.add_car_style_tv);
        outPutTv = findViewById(R.id.add_car_photo_output_tv);
        deleteCarTV = findViewById(R.id.delete_car_tv);
        ivProgress = findViewById(R.id.add_car_photo_bar);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            headView.setHeadHight();
        }
    }

    @Override
    public void setListener() {
        carModelLL.setOnClickListener(this);
        carBrandLL.setOnClickListener(this);
        carStyleLL.setOnClickListener(this);
        deleteCarTV.setOnClickListener(this);
    }

    @Override
    public void initData() {
        // 初始化OSS
        oss = AliyunOSSUtils.initOSS(this);
        // 初始化數據
        headView.setLeft(this);
        Intent intent = getIntent();
        startWay = intent.getStringExtra("startWay");
        // 判斷打開方式
        if (startWay.equals("update")){
            headView.setTitle("修改車輛");
            carPhotoIv.setImageResource(R.mipmap.img_loading);
            deleteCarTV.setVisibility(View.VISIBLE);
            carInsideModel = new Gson().fromJson(intent.getStringExtra("updateModel"), CarModel.CarCountAndListModel.CarInsideModel.class);
            switch (carInsideModel.getIfPerson()) {
                case 1:
                    carTypeTv.setText("車輛類型：" + "輕重型摩托車");
                    break;
                case 2:
                    carTypeTv.setText("車輛類型：" + "私家車");
                    break;
                case 3:
                    carTypeTv.setText("車輛類型：" + "重型汽車");
                    break;
            }
            x.image().bind(carPhotoIv, carInsideModel.getImgUrl(), options);
            carNoTv.setText("車牌號碼：" + carInsideModel.getCarNo());
            carModelTv.setText("車      型：" + carInsideModel.getModelForCar());
            carBrandTv.setText("車輛品牌：" + carInsideModel.getCarBrand());
            carStyleTv.setText("車輛型號：" + carInsideModel.getCarStyle());
        } else {
            headView.setRight(R.mipmap.ok, this);
            carPhotoLL.setOnClickListener(this);
            carTypeLL.setOnClickListener(this);
            carNoLL.setOnClickListener(this);

            headView.setTitle("添加車輛");
            carPhotoIv.setImageResource(R.mipmap.car_zhanwei);
            carInsideModel = new CarModel.CarCountAndListModel.CarInsideModel();
            carInsideModel.setId(-1);
            carInsideModel.setUserId(1);
            carInsideModel.setImgUrl("objectNam1");
            carInsideModel.setCarNo("");
            carInsideModel.setModelForCar("");
            carInsideModel.setCarBrand("");
            carInsideModel.setCarStyle("");
            carInsideModel.setIfPerson(0);
            carInsideModel.setIfPay(0);
            carInsideModel.setIsDelete(null);
            carInsideModel.setCreateTime(null);
            carInsideModel.setUpdateTime(null);
        }
    }

    @Override
    public void onClick(View view) {
        if(BSSMCommonUtils.isFastDoubleClick()) {
            return;
        }

        switch (view.getId()) {
            case R.id.head_left:
                finish();
                break;
            case R.id.head_right:
                submitCarData();
                break;
            case R.id.add_car_photo_ll:
                chooseCarPhoto();
                break;
            case R.id.add_car_type_ll:
                chooseCarType();
                break;
            case R.id.add_car_no_ll:
                chooseCarNo();
                break;
            case R.id.add_car_model_ll:
                chooseCarModel();
                break;
            case R.id.add_car_brand_ll:
                chooseCarBrand();
                break;
            case R.id.add_car_style_ll:
                chooseCarStyle();
                break;
            case R.id.delete_car_tv:
                showCarDeleteDialog();
                break;
        }
    }

    /**
     * 提交車輛信息
     */
    private void submitCarData() {
        if (startWay.equals("update")) {
            if (!carInsideModel.getCarNo().equals("")) {
                if (carInsideModel.getIfPerson() != 0) {
                    if (file != null) {
                        putImg();
                    } else {
                        // 修改車輛信息
                        callNetUpdateCar();
                    }
                } else {
                    Toast.makeText(this, "您還沒選擇車輛類型呢，快去填寫吧", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "您還沒填寫車牌號碼呢，快去填寫吧", Toast.LENGTH_SHORT).show();
            }
        } else {
            if (file != null) {
                if (!file.getPath().equals("")) {
                    if (!carInsideModel.getCarNo().equals("")) {
                        if (carInsideModel.getIfPerson() != 0) {
                            //uploadImage();
                            //mOssService.asyncPutImage(file.getName(), file.getPath());
                            putImg();
                        } else {
                            Toast.makeText(this, "您還沒選擇車輛類型呢，快去填寫吧", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "您還沒填寫車牌號碼呢，快去填寫吧", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "您還沒給您的愛車選照片呢，快去選一張吧", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "您還沒給您的愛車選照片呢，快去選一張吧", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 修改車輛信息
     */
    private void callNetUpdateCar() {
        RequestParams params = new RequestParams(BSSMConfigtor.BASE_URL + BSSMConfigtor.UPDATE_CAR + SPUtils.get(this, "UserToken", ""));
        params.setAsJsonContent(true);
        JSONObject jsonObj = new JSONObject();
        try {
            if (file != null) {
                carInsideModel.setImgUrl("http://test-pic-666.oss-cn-hongkong.aliyuncs.com/" + file.getName());
            }
            jsonObj.put("id", String.valueOf(carInsideModel.getId()));
            jsonObj.put("imgUrl", carInsideModel.getImgUrl());
            jsonObj.put("ifPerson", carInsideModel.getIfPerson());
            jsonObj.put("carNo",carInsideModel.getCarNo());
            jsonObj.put("modelForCar",carInsideModel.getModelForCar());
            jsonObj.put("carBrand",carInsideModel.getCarBrand());
            jsonObj.put("carStyle",carInsideModel.getCarStyle());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        params.setBodyContent(jsonObj.toString());
        params.setConnectTimeout(30 * 1000);
        String url = params.getUri();
        x.http().request(HttpMethod.POST, params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                CommonModel model = new Gson().fromJson(result.toString(), CommonModel.class);
                if (model.getCode().equals("200")) {
                    if (model.getData().equals("true")) {
                        Intent intent = new Intent();
                        intent.putExtra("resultWay", "CAR_FROM_UPDATE");
                        intent.putExtra("CAR_FROM_UPDATE", new Gson().toJson(carInsideModel));
                        setResult(RESULT_OK, intent);
                        finish();
                    } else {
                        Toast.makeText(AddCarActivity.this, " 修改車輛信息失敗！" + String.valueOf(model.getMsg()), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(AddCarActivity.this, " 修改車輛信息！" + String.valueOf(model.getMsg()), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if (ex instanceof SocketTimeoutException) {
                    Toast.makeText(AddCarActivity.this, "添加車輛" + getString(R.string.timeout), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AddCarActivity.this, getString(R.string.no_net), Toast.LENGTH_SHORT).show();
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
     * 提交車輛信息
     */
    private void callNetSubmiteCar() {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setTitle("提示");
        dialog.setMessage("正在刪除車輛......");
        dialog.setCancelable(false);
        dialog.show();
        RequestParams params = new RequestParams(BSSMConfigtor.BASE_URL + BSSMConfigtor.ADD_CAR + SPUtils.get(this, "UserToken", ""));
        params.setAsJsonContent(true);
        JSONObject jsonObj = new JSONObject();
        try {
            carInsideModel.setImgUrl("http://test-pic-666.oss-cn-hongkong.aliyuncs.com/" + file.getName());
            jsonObj.put("imgUrl", carInsideModel.getImgUrl());
            /*switch (carType) {
                case "私家車":
                    jsonObj.put("ifPerson",0);
                    break;
                case "輕型摩托車":
                    jsonObj.put("ifPerson",1);
                    break;
                case "重型摩托車":
                    jsonObj.put("ifPerson",2);
                    break;
                case "重型汽車":
                    jsonObj.put("ifPerson",3);
                    break;
            }*/
            jsonObj.put("ifPerson",carInsideModel.getIfPerson());
            jsonObj.put("carNo",carInsideModel.getCarNo());
            jsonObj.put("modelForCar",carInsideModel.getModelForCar());
            jsonObj.put("carBrand",carInsideModel.getCarBrand());
            jsonObj.put("carStyle",carInsideModel.getCarStyle());
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
                    carInsideModel.setId(Integer.parseInt(model.getData()));
                    carInsideModel.setIfPay(0);
                    carInsideModel.setUserId(996);  //new Gson().fromJson((String)SPUtils.get(AddCarActivity.this, "UserInfo", ""), UserInfoOutsideModel.class).getData().getMobile()
                    Intent intent = new Intent();
                    intent.putExtra("NEW_CAR_FROM_ADD", new Gson().toJson(carInsideModel));
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    Toast.makeText(AddCarActivity.this, "添加車輛失敗╮(╯▽╰)╭請重新添加" + String.valueOf(model.getMsg()), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if (ex instanceof SocketTimeoutException) {
                    Toast.makeText(AddCarActivity.this, "添加cheliang" + getString(R.string.timeout), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AddCarActivity.this, getString(R.string.no_net), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                dialog.dismiss();
            }
        });
    }

    /**
     * 選擇車輛照片
     */
    private void chooseCarPhoto() {
        NiceDialog.init()
                .setLayoutId(R.layout.dialog_photo)
                .setConvertListener(new ViewConvertListener() {
                    @Override
                    protected void convertView(ViewHolder viewHolder, final BaseNiceDialog baseNiceDialog) {
                        viewHolder.setOnClickListener(R.id.photo_camare, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(BSSMCommonUtils.IsThereAnAppToTakePictures(AddCarActivity.this)) {
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
                                    Toast.makeText(AddCarActivity.this,"您的照相機不可用哦，請檢測相機先！",Toast.LENGTH_SHORT).show();
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
    }

    /**
     * 選擇車輛類型
     */
    private void chooseCarType() {
        NiceDialog.init()
                .setLayoutId(R.layout.dialog_car_type)
                .setConvertListener(new ViewConvertListener() {
                    @Override
                    protected void convertView(ViewHolder viewHolder, final BaseNiceDialog baseNiceDialog) {
                        viewHolder.setOnClickListener(R.id.car_type_light, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                carInsideModel.setIfPerson(1);
                                carTypeTv.setText("車輛類型：輕型摩托車");
                                baseNiceDialog.dismiss();
                            }
                        });
                        viewHolder.setOnClickListener(R.id.car_type_private, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                carInsideModel.setIfPerson(2);
                                carTypeTv.setText("車輛類型：私家車");
                                baseNiceDialog.dismiss();
                            }
                        });
                        /*viewHolder.setOnClickListener(R.id.car_type_heavy, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                carInsideModel.setIfPay(1);
                                carTypeTv.setText("車輛類型：重型摩托車");
                                baseNiceDialog.dismiss();
                            }
                        });*/
                        viewHolder.setOnClickListener(R.id.car_type_heavy_car, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                carInsideModel.setIfPerson(3);
                                carTypeTv.setText("車輛類型：重型汽車");
                                baseNiceDialog.dismiss();
                            }
                        });
                    }
                })
                .setShowBottom(true)
                .show(getSupportFragmentManager());
    }

    /**
     * 填寫車輛品牌
     */
    private void chooseCarBrand() {
        NiceDialog.init()
                .setLayoutId(R.layout.dialog_car_edit)
                .setConvertListener(new ViewConvertListener() {
                    @Override
                    public void convertView(ViewHolder holder, final BaseNiceDialog dialog) {
                        final EditText editText = holder.getView(R.id.car_edit);
                        editText.setHint("請填寫車輛品牌");
                        BSSMCommonUtils.showKeyboard(editText);
                        holder.setOnClickListener(R.id.car_edit_submit, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                carInsideModel.setCarBrand(editText.getText().toString());
                                carBrandTv.setText("車牌品牌：" + carInsideModel.getCarBrand());
                                dialog.dismiss();
                            }
                        });
                    }
                })
                .setShowBottom(true)
                .show(getSupportFragmentManager());
    }

    /**
     * 選擇車型
     */
    private void chooseCarModel() {
        NiceDialog.init()
                .setLayoutId(R.layout.dialog_car_model)
                .setConvertListener(new ViewConvertListener() {
                    @Override
                    protected void convertView(ViewHolder viewHolder, final BaseNiceDialog baseNiceDialog) {
                        viewHolder.setOnClickListener(R.id.car_model_suv, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                carInsideModel.setModelForCar("SUV");
                                carModelTv.setText("車      型：" + carInsideModel.getModelForCar());
                                baseNiceDialog.dismiss();
                            }
                        });
                        viewHolder.setOnClickListener(R.id.car_model_limousine, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                carInsideModel.setModelForCar("轎車");
                                carModelTv.setText("車      型：" + carInsideModel.getModelForCar());
                                baseNiceDialog.dismiss();
                            }
                        });
                        viewHolder.setOnClickListener(R.id.car_type_mpv, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                carInsideModel.setModelForCar("MPV");
                                carModelTv.setText("車      型：" + carInsideModel.getModelForCar());
                                baseNiceDialog.dismiss();
                            }
                        });
                        viewHolder.setOnClickListener(R.id.car_type_sport, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                carInsideModel.setModelForCar("跑車");
                                carModelTv.setText("車      型：" + carInsideModel.getModelForCar());
                                baseNiceDialog.dismiss();
                            }
                        });
                        viewHolder.setOnClickListener(R.id.car_type_trucks, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                carInsideModel.setModelForCar("貨車");
                                carModelTv.setText("車      型：" + carInsideModel.getModelForCar());
                                baseNiceDialog.dismiss();
                            }
                        });
                        viewHolder.setOnClickListener(R.id.car_type_others, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                carInsideModel.setModelForCar("其他");
                                carModelTv.setText("車      型：" + carInsideModel.getModelForCar());
                                baseNiceDialog.dismiss();
                            }
                        });
                    }
                })
                .setShowBottom(true)
                .show(getSupportFragmentManager());
    }

    /**
     * 填寫車牌號碼
     */
    private void chooseCarNo() {
        NiceDialog.init()
                .setLayoutId(R.layout.dialog_car_edit)
                .setConvertListener(new ViewConvertListener() {
                    @Override
                    public void convertView(ViewHolder holder, final BaseNiceDialog dialog) {
                        final EditText editText = holder.getView(R.id.car_edit);
                        editText.setHint("請填寫車牌號碼");
                        BSSMCommonUtils.showKeyboard(editText);
                        holder.setOnClickListener(R.id.car_edit_submit, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                carInsideModel.setCarNo(editText.getText().toString());
                                carNoTv.setText("車牌號碼：" + carInsideModel.getCarNo());
                                dialog.dismiss();
                            }
                        });
                    }
                })
                .setShowBottom(true)
                .show(getSupportFragmentManager());
    }

    /**
     * 填寫車輛型號
     */
    private void chooseCarStyle() {
        NiceDialog.init()
                .setLayoutId(R.layout.dialog_car_edit)
                .setConvertListener(new ViewConvertListener() {
                    @Override
                    public void convertView(ViewHolder holder, final BaseNiceDialog dialog) {
                        final EditText editText = holder.getView(R.id.car_edit);
                        editText.setHint("請填寫車牌型號");
                        BSSMCommonUtils.showKeyboard(editText);
                        holder.setOnClickListener(R.id.car_edit_submit, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                carInsideModel.setCarStyle(editText.getText().toString());
                                carStyleTv.setText("車牌型號：" + carInsideModel.getCarStyle());
                                dialog.dismiss();
                            }
                        });
                    }
                })
                .setShowBottom(true)
                .show(getSupportFragmentManager());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            Toast.makeText(AddCarActivity.this, "影相失敗！", Toast.LENGTH_SHORT).show();
            return;
        }
        switch(requestCode) {
            case TAKE_PHOTO:
                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                Uri contentUri = Uri.fromFile(file);
                mediaScanIntent.setData(contentUri);
                sendBroadcast(mediaScanIntent);
                x.image().bind(carPhotoIv, file.getPath(), options);
                break;
            case TAKE_PHOTO_FROM_ALBUM:
                String imagePath = BSSMCommonUtils.getRealFilePath(this, data.getData());
                file = new File(imagePath);
                x.image().bind(carPhotoIv, file.getPath(), options);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (file != null){
            outState.putString("file_path", file.getPath());
        }

        if (carType != null){
            outState.putString("carType", carType);
        } else {
            outState.putString("carType", "");
        }
        if (carNo != null){
            outState.putString("carNo", carNo);
        } else {
            outState.putString("carNo", "");
        }
        if (carModel != null){
            outState.putString("carModel", carModel.toString());
        } else {
            outState.putString("carModel", "");
        }
        if (carBrand != null){
            outState.putString("carBrand", carBrand.toString());
        } else {
            outState.putString("carBrand", "");
        }
        if (carStyle != null){
            outState.putString("carStyle", carStyle.toString());
        } else {
            outState.putString("carStyle", "");
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

        x.image().bind(carPhotoIv, file.getPath(), options);

        carType = savedInstanceState.get("carType").toString();
        carNo = savedInstanceState.get("carNo").toString();
        carModel = savedInstanceState.get("carModel").toString();
        carBrand = savedInstanceState.get("carBrand").toString();
        carStyle = savedInstanceState.get("carStyle").toString();
        carTypeTv.setText("車輛類型：" + carType);
        carBrandTv.setText("車牌品牌：" + carBrand);
        carModelTv.setText("車      型：" + carModel);
        carStyleTv.setText("車牌型號：" + carStyle);
        carNoTv.setText("車牌號碼：" + carNo);
    }

    /**
     * 刪除車輛的dialog
     */
    private void showCarDeleteDialog() {
        NiceDialog.init()
                .setLayoutId(R.layout.dialog_normal)
                .setConvertListener(new ViewConvertListener() {
                    @Override
                    protected void convertView(ViewHolder viewHolder, final BaseNiceDialog baseNiceDialog) {
                        TextView msgTv = viewHolder.getView(R.id.dialog_normal_msg);
                        msgTv.setText("是否要刪除該車輛，刪除后車輛的會員費不會退還的哦！");
                        viewHolder.setOnClickListener(R.id.dialog_normal_no, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                baseNiceDialog.dismiss();
                            }
                        });
                        viewHolder.setOnClickListener(R.id.dialog_normal_yes, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                callNetDeleteCar();
                                baseNiceDialog.dismiss();
                            }
                        });
                    }
                })
                .setWidth(210)
                .show(getSupportFragmentManager());
    }

    /**
     * 請求刪除車輛
     */
    private void callNetDeleteCar() {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setTitle("提示");
        dialog.setMessage("正在刪除車輛......");
        dialog.setCancelable(false);
        dialog.show();
        RequestParams params = new RequestParams(BSSMConfigtor.BASE_URL + BSSMConfigtor.DELETE_CAR + SPUtils.get(this, "UserToken", ""));
        params.setAsJsonContent(true);
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("id",carInsideModel.getId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String urlJson = jsonObj.toString();
        params.setBodyContent(urlJson);
        params.setConnectTimeout(30 * 1000);
        x.http().request(HttpMethod.POST ,params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                CommonModel model = new Gson().fromJson(result.toString(), CommonModel.class);
                if (model.getCode().equals("200")) {
                    Intent intent = new Intent();
                    intent.putExtra("resultWay", "DELETE_CAR_FROM_ADD");
                    intent.putExtra("DELETE_CAR_FROM_ADD", new Gson().toJson(carInsideModel));
                    setResult(RESULT_OK, intent);
                    finish();
                    Toast.makeText(AddCarActivity.this, "刪除成功！", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AddCarActivity.this, "刪除失敗！" + String.valueOf(model.getMsg()), Toast.LENGTH_SHORT).show();
                }
            }
            //请求异常后的回调方法
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if (ex instanceof SocketTimeoutException) {
                    Toast.makeText(AddCarActivity.this, "刪除超時，請重試！", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AddCarActivity.this, "刪除失敗！", Toast.LENGTH_SHORT).show();
                }
            }
            //主动调用取消请求的回调方法
            @Override
            public void onCancelled(CancelledException cex) {
            }
            @Override
            public void onFinished() {
                dialog.dismiss();
            }
        });
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
