package com.bs.john_li.bsfslotmachine.BSSMActivity.Mine;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.BoolRes;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bs.john_li.bsfslotmachine.BSSMActivity.BaseActivity;
import com.bs.john_li.bsfslotmachine.BSSMActivity.Parking.ParkingOrderActivity;
import com.bs.john_li.bsfslotmachine.BSSMActivity.Parking.SearchSlotMachineActivity;
import com.bs.john_li.bsfslotmachine.BSSMModel.CarModel;
import com.bs.john_li.bsfslotmachine.BSSMModel.CommonModel;
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
import org.xutils.common.util.FileUtil;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 添加車輛
 * Created by John_Li on 27/9/2017.
 */

public class AddCarActivity extends BaseActivity implements View.OnClickListener{
    private BSSMHeadView headView;
    private LinearLayout carPhotoLL, carTypeLL, carNoLL, carModelLL, carBrandLL, carStyleLL;
    private ImageView carPhotoIv;
    private TextView carTypeTv, carNoTv, carModelTv, carBrandTv, carStyleTv;

    public static final int TAKE_PHOTO = 1;
    public static final int TAKE_PHOTO_FROM_ALBUM = 2;
    private File dir; //圖片文件夾路徑
    private File file;  //照片文件
    private ImageOptions options = new ImageOptions.Builder().setSize(0, 0).setFailureDrawableId(R.mipmap.car_sample).build();
    private CarModel.CarCountAndListModel.CarInsideModel carInsideModel;
    private String carType, carNo, carModel, carBrand, carStyle;
    private String startWay;
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
    }

    @Override
    public void setListener() {
        carPhotoLL.setOnClickListener(this);
        carTypeLL.setOnClickListener(this);
        carNoLL.setOnClickListener(this);
        carModelLL.setOnClickListener(this);
        carBrandLL.setOnClickListener(this);
        carStyleLL.setOnClickListener(this);
    }

    @Override
    public void initData() {
        headView.setLeft(this);
        headView.setRight(R.mipmap.ok, this);
        Intent intent = getIntent();
        startWay = intent.getStringExtra("startWay");
        if (startWay.equals("update")){
            headView.setTitle("修改車輛");
            carInsideModel = new Gson().fromJson("updateModel", CarModel.CarCountAndListModel.CarInsideModel.class);
        } else {
            headView.setTitle("添加車輛");
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
        }
    }

    /**
     * 提交車輛信息
     */
    private void submitCarData() {
        if (file != null) {
            if (!file.getPath().equals("")) {
                if (!carInsideModel.getCarNo().equals("")) {
                    if (!carInsideModel.getModelForCar().equals("")) {
                        if (!carInsideModel.getCarBrand().equals("")) {
                            if (!carInsideModel.getCarStyle().equals("")) {
                                // 提交車輛信息
                                callNetSubmiteCar();
                            } else {
                                Toast.makeText(this, "您還沒填寫車牌型號呢，快去填寫吧", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(this, "您還沒填寫車牌品牌呢，快去填寫吧", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "您還沒填寫車型呢，快去填寫吧", Toast.LENGTH_SHORT).show();
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

    /**
     * 提交車輛信息
     */
    private void callNetSubmiteCar() {
        RequestParams params = new RequestParams(BSSMConfigtor.BASE_URL + BSSMConfigtor.ADD_CAR + SPUtils.get(this, "UserToken", ""));
        params.setAsJsonContent(true);
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("imgUrl","objectNam1");
            switch (carType) {
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
            }
            jsonObj.put("carNo",carNo);
            jsonObj.put("modelForCar",carModel);
            jsonObj.put("carBrand",carBrand);
            jsonObj.put("carStyle",carStyle);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String urlJson = jsonObj.toString();
        params.setBodyContent(urlJson);
        x.http().request(HttpMethod.POST, params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                CommonModel model = new Gson().fromJson(result.toString(), CommonModel.class);
                if (model.getCode().equals("200")) {
                    carInsideModel.setId(Integer.parseInt(model.getData()));
                    Intent intent = new Intent();
                    intent.putExtra("NEW_CAR_FROM_ADD", new Gson().toJson(carInsideModel));
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    Toast.makeText(AddCarActivity.this, "添加車輛失敗╮(╯▽╰)╭請重新添加", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(AddCarActivity.this, getString(R.string.no_net), Toast.LENGTH_SHORT).show();
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
     * 检查相机是否可以打开
     */
    private boolean IsThereAnAppToTakePictures() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        List<ResolveInfo> availableActivities = getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return availableActivities != null && availableActivities.size() > 0;
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
                                if(IsThereAnAppToTakePictures()) {
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
                        viewHolder.setOnClickListener(R.id.car_type_private, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                carInsideModel.setIfPay(0);
                                carTypeTv.setText("車輛類型：私家車");
                                baseNiceDialog.dismiss();
                            }
                        });
                        viewHolder.setOnClickListener(R.id.car_type_light, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                carInsideModel.setIfPay(1);
                                carTypeTv.setText("車輛類型：輕型摩托車");
                                baseNiceDialog.dismiss();
                            }
                        });
                        viewHolder.setOnClickListener(R.id.car_type_heavy, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                carInsideModel.setIfPay(1);
                                carTypeTv.setText("車輛類型：重型摩托車");
                                baseNiceDialog.dismiss();
                            }
                        });
                        viewHolder.setOnClickListener(R.id.car_type_heavy_car, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                carInsideModel.setIfPay(1);
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
}
