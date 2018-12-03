package com.bs.john_li.bsfslotmachine.BSSMActivity.CarService;

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
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.bs.john_li.bsfslotmachine.BSSMActivity.BaseActivity;
import com.bs.john_li.bsfslotmachine.BSSMAdapter.PhotoAdapter;
import com.bs.john_li.bsfslotmachine.BSSMAdapter.SecondCarOptionListAdapter;
import com.bs.john_li.bsfslotmachine.BSSMModel.CarBrandOutModel;
import com.bs.john_li.bsfslotmachine.BSSMModel.CommonJieModel;
import com.bs.john_li.bsfslotmachine.BSSMModel.CommonModel;
import com.bs.john_li.bsfslotmachine.BSSMModel.SecondCarOutModel;
import com.bs.john_li.bsfslotmachine.BSSMModel.SubmitSecondCarModel;
import com.bs.john_li.bsfslotmachine.BSSMUtils.AliyunOSSUtils;
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
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by John_Li on 22/11/2018.
 */

public class PublishOwnSecondCarActivity extends BaseActivity implements View.OnClickListener{
    private BSSMHeadView headView;
    private RadioGroup mCarTypeRg, mGearsRg;
    private LinearLayout brandLL, seriesLL, styleLL,firstTimeLL,miealgeLL,priceLL,telLL,desLL,colorLL,modelLL,exhaustLL,countryLL,deliveryTimeLL,configInfoLL,repiarStatrLL,insideBodyLL,testConclusionLL;
    private TextView brandTv,seriesTv,styleTv,firstTimeLLTv,miealgeTv,priceTv,telTv,desTv,colorTv,modelTv,exhaustTv,countryTv,deliveryTimeTv,configInfoTv,repiarStatrTv,insideBodyTv,testConclusionTv;

    private SubmitSecondCarModel mSubmitSecondCarModel;
    private List<CarBrandOutModel.CarBrandModel> mCarBrandModelList;


    public static final int TAKE_PHOTO = 1;
    public static final int TAKE_PHOTO_FROM_ALBUM = 2;
    private File dir; //圖片文件夾路徑
    private File file;  //照片文件
    private ImageOptions options = new ImageOptions.Builder().setSize(0, 0).setFailureDrawableId(R.mipmap.car_sample).build();
    private List<String> imgUrlList;
    private NoScrollGridView photoGv;
    private PhotoAdapter mPhotoAdapter;
    private OSSClient oss;
    // 提交帖文的提示窗
    private ProgressDialog dialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_own_second_car);
        initView();
        setListener();
        initData();
    }

    @Override
    public void initView() {
        headView = findViewById(R.id.publish_own_second_car_head);
        mCarTypeRg = findViewById(R.id.publish_own_second_car_type_rg);
        brandLL = findViewById(R.id.publish_own_second_car_brand_ll);
        seriesLL = findViewById(R.id.publish_own_second_car_series_ll);
        styleLL = findViewById(R.id.publish_own_second_car_style_ll);
        firstTimeLL = findViewById(R.id.publish_own_second_car_first_time_ll);
        miealgeLL = findViewById(R.id.publish_own_second_car_miealge_ll);
        mGearsRg = findViewById(R.id.publish_own_second_car_gears_rg);
        priceLL = findViewById(R.id.publish_own_second_car_price_ll);
        telLL = findViewById(R.id.publish_own_second_car_tel_ll);
        desLL = findViewById(R.id.publish_own_second_car_des_ll);
        colorLL = findViewById(R.id.publish_own_second_car_color_ll);
        modelLL = findViewById(R.id.publish_own_second_car_model_ll);
        exhaustLL = findViewById(R.id.publish_own_second_car_exhaust_ll);
        countryLL = findViewById(R.id.publish_own_second_car_country_ll);
        deliveryTimeLL = findViewById(R.id.publish_own_second_car_delivery_time_ll);
        configInfoLL = findViewById(R.id.publish_own_second_car_config_info_ll);
        repiarStatrLL = findViewById(R.id.publish_own_second_car_repiar_statr_ll);
        insideBodyLL = findViewById(R.id.publish_own_second_car_inside_body_ll);
        testConclusionLL = findViewById(R.id.publish_own_second_car_test_conclusion_ll);

        brandTv = findViewById(R.id.publish_own_second_car_brand_tv);
        seriesTv = findViewById(R.id.publish_own_second_car_series_tv);
        styleTv = findViewById(R.id.publish_own_second_car_style_tv);
        firstTimeLLTv = findViewById(R.id.publish_own_second_car_first_time_tv);
        miealgeTv = findViewById(R.id.publish_own_second_car_miealge_tv);
        priceTv = findViewById(R.id.publish_own_second_car_price_tv);
        telTv = findViewById(R.id.publish_own_second_car_tel_tv);
        desTv = findViewById(R.id.publish_own_second_car_des_tv);
        colorTv = findViewById(R.id.publish_own_second_car_color_tv);
        modelTv = findViewById(R.id.publish_own_second_car_model_tv);
        exhaustTv = findViewById(R.id.publish_own_second_car_exhaust_tv);
        countryTv = findViewById(R.id.publish_own_second_car_country_tv);
        deliveryTimeTv = findViewById(R.id.publish_own_second_car_delivery_time_tv);
        configInfoTv = findViewById(R.id.publish_own_second_car_config_info_tv);
        repiarStatrTv = findViewById(R.id.publish_own_second_car_repiar_statr_tv);
        insideBodyTv = findViewById(R.id.publish_own_second_car_inside_body_tv);
        testConclusionTv = findViewById(R.id.publish_own_second_car_test_conclusion_tv);


        photoGv = findViewById(R.id.publish_forum_gv);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            headView.setHeadHight();
        }
    }

    @Override
    public void setListener() {
        brandLL.setOnClickListener(this);
        seriesLL.setOnClickListener(this);
        styleLL.setOnClickListener(this);
        firstTimeLL.setOnClickListener(this);
        miealgeLL.setOnClickListener(this);
        priceLL.setOnClickListener(this);
        telLL.setOnClickListener(this);
        desLL.setOnClickListener(this);
        colorLL.setOnClickListener(this);
        modelLL.setOnClickListener(this);
        exhaustLL.setOnClickListener(this);
        countryLL.setOnClickListener(this);
        deliveryTimeLL.setOnClickListener(this);
        configInfoLL.setOnClickListener(this);
        repiarStatrLL.setOnClickListener(this);
        insideBodyLL.setOnClickListener(this);
        testConclusionLL.setOnClickListener(this);

        mCarTypeRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                if (i == R.id.publish_own_second_scooter_rb) {
                    mSubmitSecondCarModel.setCarType(1);
                } else if (i == R.id.publish_own_second_private_car_rb){
                    mSubmitSecondCarModel.setCarType(0);
                } else {
                    mSubmitSecondCarModel.setCarType(-1);
                }
            }
        });
        mGearsRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                if (i == R.id.publish_own_second_car_gears_auto_rb) {
                    mSubmitSecondCarModel.setCarGears("自動");
                } else if (i == R.id.publish_own_second_car_gears_handle_rb){
                    mSubmitSecondCarModel.setCarGears("手動");
                } else {
                    mSubmitSecondCarModel.setCarGears(null);
                }
            }
        });


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
                                                if(BSSMCommonUtils.IsThereAnAppToTakePictures(PublishOwnSecondCarActivity.this)) {
                                                    dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "BSSMPictures");
                                                    if (!dir.exists()) {
                                                        dir.mkdir();
                                                    }

                                                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                                    SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
                                                    Date date = new Date(System.currentTimeMillis());
                                                    file = new File(dir, "charge_car" + format.format(date) + ".jpg");
                                                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                                                    startActivityForResult(intent, TAKE_PHOTO);
                                                } else {
                                                    Toast.makeText(PublishOwnSecondCarActivity.this,"您的照相機不可用哦，請檢測相機先！",Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(PublishOwnSecondCarActivity.this, "照片數量不可多於5張哦~", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    @Override
    public void initData() {
        headView.setLeft(this);
        headView.setTitle("發佈車輛");
        headView.setRight(R.mipmap.ok, this);

        mSubmitSecondCarModel = new SubmitSecondCarModel();
        mCarBrandModelList = new ArrayList<>();

        oss = AliyunOSSUtils.initOSS(this);

        imgUrlList = new ArrayList<>();
        mPhotoAdapter = new PhotoAdapter(this, imgUrlList);
        photoGv.setAdapter(mPhotoAdapter);
        // 加入初始的添加照片的圖片
        imgUrlList.add("");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_left:
                finish();
                break;
            case R.id.head_right:
                dialog = new ProgressDialog(this);
                dialog.setTitle("提示");
                dialog.setMessage("提交車輛中......");
                dialog.setCancelable(false);
                dialog.show();
                String s = mSubmitSecondCarModel.getCarType() + "/" + mSubmitSecondCarModel.getCarBrand()
                        + "/" + mSubmitSecondCarModel.getCarSeries() + "/" + mSubmitSecondCarModel.getType()
                        + "/" + mSubmitSecondCarModel.getCarStyle() + "/" + mSubmitSecondCarModel.getFirstRegisterationTime()
                        + "/" + mSubmitSecondCarModel.getDriverMileage() + "/" + mSubmitSecondCarModel.getCarGears()
                        + "/" + mSubmitSecondCarModel.getCarPrices()  + "/" + mSubmitSecondCarModel.getTel()
                        + "/" + mSubmitSecondCarModel.getCarDescription()  ;
                if (mSubmitSecondCarModel.getCarType() != -1 && mSubmitSecondCarModel.getCarBrand() != null
                        && mSubmitSecondCarModel.getCarSeries() != null && mSubmitSecondCarModel.getType() != null
                        && mSubmitSecondCarModel.getCarStyle() != null && mSubmitSecondCarModel.getFirstRegisterationTime() != null
                        && mSubmitSecondCarModel.getDriverMileage() != null && mSubmitSecondCarModel.getCarGears() != null
                        && mSubmitSecondCarModel.getCarPrices() != null && mSubmitSecondCarModel.getTel() != null
                        && mSubmitSecondCarModel.getCarDescription() != null) {
                    hasImage();
                } else {
                    dialog.dismiss();
                    Toast.makeText(PublishOwnSecondCarActivity.this, "請填寫全車輛信息~", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.publish_own_second_car_brand_ll:
                if (mCarBrandModelList.size() > 0) {
                    NiceDialog.init()
                            .setLayoutId(R.layout.dialog_car_brand_list)
                            .setConvertListener(new ViewConvertListener() {
                                @Override
                                protected void convertView(ViewHolder viewHolder, final BaseNiceDialog baseNiceDialog) {
                                    ListView lv = viewHolder.getView(R.id.dialog_car_brand_lv);
                                    final List<String> list = new ArrayList<String>();
                                    for (CarBrandOutModel.CarBrandModel carBrandModel : mCarBrandModelList) {
                                        list.add(carBrandModel.getEngName());
                                    }
                                    lv.setAdapter(new SecondCarOptionListAdapter(PublishOwnSecondCarActivity.this, list));
                                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                            mSubmitSecondCarModel.setCarBrand(list.get(i));
                                            brandTv.setText(mSubmitSecondCarModel.getCarBrand());
                                            baseNiceDialog.dismiss();
                                        }
                                    });
                                }
                            })
                            .setShowBottom(true)
                            .show(getSupportFragmentManager());
                } else {
                    callNetGetCarBrand();
                }
                break;
            case R.id.publish_own_second_car_series_ll:
                NiceDialog.init()
                        .setLayoutId(R.layout.dialog_car_edit)
                        .setConvertListener(new ViewConvertListener() {
                            @Override
                            protected void convertView(ViewHolder viewHolder, final BaseNiceDialog baseNiceDialog) {
                                final EditText editText = viewHolder.getView(R.id.car_edit);
                                editText.setHint("請填寫車系(例如：C-Class)");
                                BSSMCommonUtils.showKeyboard(editText);
                                viewHolder.setOnClickListener(R.id.car_edit_submit, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        if (editText.getText().toString().length()> 0) {
                                            mSubmitSecondCarModel.setCarSeries(editText.getText().toString());
                                            seriesTv.setText(editText.getText().toString());
                                            baseNiceDialog.dismiss();
                                        } else {
                                            Toast.makeText(PublishOwnSecondCarActivity.this, "請填寫車系(例如：C-Class)", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                            }
                        })
                        .setShowBottom(true)
                        .show(getSupportFragmentManager());
                break;
            case R.id.publish_own_second_car_style_ll:
                NiceDialog.init()
                        .setLayoutId(R.layout.dialog_car_edit)
                        .setConvertListener(new ViewConvertListener() {
                            @Override
                            protected void convertView(ViewHolder viewHolder, final BaseNiceDialog baseNiceDialog) {
                                final EditText editText = viewHolder.getView(R.id.car_edit);
                                editText.setHint("請填寫車型(例如：C250)");
                                BSSMCommonUtils.showKeyboard(editText);
                                viewHolder.setOnClickListener(R.id.car_edit_submit, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        if (editText.getText().toString().length()> 0) {
                                            mSubmitSecondCarModel.setCarStyle(editText.getText().toString());
                                            styleTv.setText(mSubmitSecondCarModel.getCarStyle());
                                            baseNiceDialog.dismiss();
                                        } else {
                                            Toast.makeText(PublishOwnSecondCarActivity.this, "請填寫車型(例如：C250)", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                            }
                        })
                        .setShowBottom(true)
                        .show(getSupportFragmentManager());
                break;
            case R.id.publish_own_second_car_first_time_ll:
                NiceDialog.init()
                        .setLayoutId(R.layout.dialog_time_picker)
                        .setConvertListener(new ViewConvertListener() {
                            @Override
                            protected void convertView(ViewHolder viewHolder, final BaseNiceDialog baseNiceDialog) {
                                final DatePicker datePicker = viewHolder.getView(R.id.dialog_date_picker);
                                datePicker.init(BSSMCommonUtils.getYear(), BSSMCommonUtils.getMonth(), BSSMCommonUtils.getDayOfMonth(), new DatePicker.OnDateChangedListener() {
                                    @RequiresApi(api = Build.VERSION_CODES.N)
                                    @Override
                                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                        // 获取一个日历对象，并初始化为当前选中的时间
                                        Calendar calendar = Calendar.getInstance();
                                        calendar.set(year, monthOfYear, dayOfMonth);
                                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd ");
                                        mSubmitSecondCarModel.setFirstRegisterationTime(format.format(calendar.getTime()).toString() + " 00:00:00");
                                        firstTimeLLTv.setText(mSubmitSecondCarModel.getFirstRegisterationTime());
                                    }
                                });
                                viewHolder.setOnClickListener(R.id.dialog_dp_submit, new View.OnClickListener() {
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
            case R.id.publish_own_second_car_miealge_ll:
                NiceDialog.init()
                        .setLayoutId(R.layout.dialog_car_edit)
                        .setConvertListener(new ViewConvertListener() {
                            @Override
                            protected void convertView(ViewHolder viewHolder, final BaseNiceDialog baseNiceDialog) {
                                final EditText editText = viewHolder.getView(R.id.car_edit);
                                editText.setHint("請填寫里程(單位：萬公里)");
                                BSSMCommonUtils.showKeyboard(editText);
                                viewHolder.setOnClickListener(R.id.car_edit_submit, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        if (editText.getText().toString().length()> 0) {
                                            mSubmitSecondCarModel.setDriverMileage(editText.getText().toString());
                                            miealgeTv.setText(mSubmitSecondCarModel.getDriverMileage() + "萬公里");
                                            baseNiceDialog.dismiss();
                                        } else {
                                            Toast.makeText(PublishOwnSecondCarActivity.this, "請填寫里程(單位：萬公里)", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                            }
                        })
                        .setShowBottom(true)
                        .show(getSupportFragmentManager());
                break;
            case R.id.publish_own_second_car_price_ll:
                NiceDialog.init()
                        .setLayoutId(R.layout.dialog_car_edit)
                        .setConvertListener(new ViewConvertListener() {
                            @Override
                            protected void convertView(ViewHolder viewHolder, final BaseNiceDialog baseNiceDialog) {
                                final EditText editText = viewHolder.getView(R.id.car_edit);
                                editText.setHint("請填寫價格(單位：萬)");
                                BSSMCommonUtils.showKeyboard(editText);
                                viewHolder.setOnClickListener(R.id.car_edit_submit, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        if (editText.getText().toString().length()> 0) {
                                            mSubmitSecondCarModel.setCarPrices(editText.getText().toString());
                                            priceTv.setText(mSubmitSecondCarModel.getCarPrices() + "萬");
                                            baseNiceDialog.dismiss();
                                        } else {
                                            Toast.makeText(PublishOwnSecondCarActivity.this, "請填寫價格(單位：萬)", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                            }
                        })
                        .setShowBottom(true)
                        .show(getSupportFragmentManager());
                break;
            case R.id.publish_own_second_car_tel_ll:
                NiceDialog.init()
                        .setLayoutId(R.layout.dialog_car_edit)
                        .setConvertListener(new ViewConvertListener() {
                            @Override
                            protected void convertView(ViewHolder viewHolder, final BaseNiceDialog baseNiceDialog) {
                                final EditText editText = viewHolder.getView(R.id.car_edit);
                                editText.setHint("請填寫聯絡電話(例如：655***39)");
                                BSSMCommonUtils.showKeyboard(editText);
                                viewHolder.setOnClickListener(R.id.car_edit_submit, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        if (editText.getText().toString().length()> 0) {
                                            mSubmitSecondCarModel.setTel(editText.getText().toString());
                                            telTv.setText(mSubmitSecondCarModel.getTel());
                                            baseNiceDialog.dismiss();
                                        } else {
                                            Toast.makeText(PublishOwnSecondCarActivity.this, "請填寫聯絡電話(例如：655***39)", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                            }
                        })
                        .setShowBottom(true)
                        .show(getSupportFragmentManager());
                break;
            case R.id.publish_own_second_car_des_ll:
                NiceDialog.init()
                        .setLayoutId(R.layout.dialog_car_edit)
                        .setConvertListener(new ViewConvertListener() {
                            @Override
                            protected void convertView(ViewHolder viewHolder, final BaseNiceDialog baseNiceDialog) {
                                final EditText editText = viewHolder.getView(R.id.car_edit);
                                editText.setHint("請填寫車輛狀態");
                                BSSMCommonUtils.showKeyboard(editText);
                                viewHolder.setOnClickListener(R.id.car_edit_submit, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        if (editText.getText().toString().length()> 0) {
                                            mSubmitSecondCarModel.setCarDescription(editText.getText().toString());
                                            desTv.setText(mSubmitSecondCarModel.getCarDescription());
                                            baseNiceDialog.dismiss();
                                        } else {
                                            Toast.makeText(PublishOwnSecondCarActivity.this, "請填寫車輛狀態", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                            }
                        })
                        .setShowBottom(true)
                        .show(getSupportFragmentManager());
                break;
            case R.id.publish_own_second_car_color_ll:
                NiceDialog.init()
                        .setLayoutId(R.layout.dialog_car_brand_list)
                        .setConvertListener(new ViewConvertListener() {
                            @Override
                            protected void convertView(ViewHolder viewHolder, final BaseNiceDialog baseNiceDialog) {
                                ListView lv = viewHolder.getView(R.id.dialog_car_brand_lv);
                                final List<String> list = new ArrayList<String>();
                                list.add("棕色");
                                list.add("橙色");
                                list.add("紫色");
                                list.add("紅色");
                                list.add("黑色");
                                list.add("白色");
                                list.add("銀灰色");
                                list.add("深灰色");
                                list.add("藍色");
                                list.add("綠色");
                                list.add("黃色");
                                list.add("香檳色");
                                list.add("其他");
                                lv.setAdapter(new SecondCarOptionListAdapter(PublishOwnSecondCarActivity.this, list));
                                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                        mSubmitSecondCarModel.setCarColor(list.get(i));
                                        colorTv.setText(mSubmitSecondCarModel.getCarColor());
                                        baseNiceDialog.dismiss();
                                    }
                                });
                            }
                        })
                        .setShowBottom(true)
                        .show(getSupportFragmentManager());
                break;
            case R.id.publish_own_second_car_model_ll:
                NiceDialog.init()
                        .setLayoutId(R.layout.dialog_car_brand_list)
                        .setConvertListener(new ViewConvertListener() {
                            @Override
                            protected void convertView(ViewHolder viewHolder, final BaseNiceDialog baseNiceDialog) {
                                ListView lv = viewHolder.getView(R.id.dialog_car_brand_lv);
                                final List<String> list = new ArrayList<String>();
                                list.add("私家車");
                                list.add("客貨車");
                                list.add("貨車");
                                list.add("電單車");
                                list.add("房車");
                                list.add("房跑車");
                                list.add("跑車");
                                list.add("敞篷車");
                                list.add("越野車");
                                list.add("旅行車");
                                list.add("小型車");
                                list.add("7/8人車");
                                list.add("其他");
                                lv.setAdapter(new SecondCarOptionListAdapter(PublishOwnSecondCarActivity.this, list));
                                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                        mSubmitSecondCarModel.setType(list.get(i));
                                        modelTv.setText(mSubmitSecondCarModel.getType());
                                        baseNiceDialog.dismiss();
                                    }
                                });
                            }
                        })
                        .setShowBottom(true)
                        .show(getSupportFragmentManager());
                break;
            case R.id.publish_own_second_car_exhaust_ll:
                NiceDialog.init()
                        .setLayoutId(R.layout.dialog_car_edit)
                        .setConvertListener(new ViewConvertListener() {
                            @Override
                            protected void convertView(ViewHolder viewHolder, final BaseNiceDialog baseNiceDialog) {
                                final EditText editText = viewHolder.getView(R.id.car_edit);
                                editText.setHint("請填寫排量(單位：C.C.)");
                                BSSMCommonUtils.showKeyboard(editText);
                                viewHolder.setOnClickListener(R.id.car_edit_submit, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        if (editText.getText().toString().length()> 0) {
                                            mSubmitSecondCarModel.setExhaust(editText.getText().toString());
                                            exhaustTv.setText(mSubmitSecondCarModel.getExhaust() + "C.C.");
                                            baseNiceDialog.dismiss();
                                        } else {
                                            Toast.makeText(PublishOwnSecondCarActivity.this, "請填寫排量(單位：C.C.)", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                            }
                        })
                        .setShowBottom(true)
                        .show(getSupportFragmentManager());
                break;
            case R.id.publish_own_second_car_country_ll:
                NiceDialog.init()
                        .setLayoutId(R.layout.dialog_car_edit)
                        .setConvertListener(new ViewConvertListener() {
                            @Override
                            protected void convertView(ViewHolder viewHolder, final BaseNiceDialog baseNiceDialog) {
                                final EditText editText = viewHolder.getView(R.id.car_edit);
                                editText.setHint("請填寫出廠國家");
                                BSSMCommonUtils.showKeyboard(editText);
                                viewHolder.setOnClickListener(R.id.car_edit_submit, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        if (editText.getText().toString().length()> 0) {
                                            mSubmitSecondCarModel.setCountryOfOrigin(editText.getText().toString());
                                            countryTv.setText(mSubmitSecondCarModel.getCountryOfOrigin());
                                            baseNiceDialog.dismiss();
                                        } else {
                                            Toast.makeText(PublishOwnSecondCarActivity.this, "請填寫出廠國家", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                            }
                        })
                        .setShowBottom(true)
                        .show(getSupportFragmentManager());
                break;
            case R.id.publish_own_second_car_delivery_time_ll:
                NiceDialog.init()
                        .setLayoutId(R.layout.dialog_time_picker)
                        .setConvertListener(new ViewConvertListener() {
                            @Override
                            protected void convertView(ViewHolder viewHolder, final BaseNiceDialog baseNiceDialog) {
                                final DatePicker datePicker = viewHolder.getView(R.id.dialog_date_picker);
                                datePicker.init(BSSMCommonUtils.getYear(), BSSMCommonUtils.getMonth(), BSSMCommonUtils.getDayOfMonth(), new DatePicker.OnDateChangedListener() {
                                    @RequiresApi(api = Build.VERSION_CODES.N)
                                    @Override
                                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                        // 获取一个日历对象，并初始化为当前选中的时间
                                        Calendar calendar = Calendar.getInstance();
                                        calendar.set(year, monthOfYear, dayOfMonth);
                                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd ");
                                        mSubmitSecondCarModel.setDeliveryTime(format.format(calendar.getTime()).toString() + " 00:00:00");
                                        deliveryTimeTv.setText(mSubmitSecondCarModel.getDeliveryTime());
                                    }
                                });

                                viewHolder.setOnClickListener(R.id.dialog_dp_submit, new View.OnClickListener() {
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
            case R.id.publish_own_second_car_config_info_ll:
                NiceDialog.init()
                        .setLayoutId(R.layout.dialog_car_edit)
                        .setConvertListener(new ViewConvertListener() {
                            @Override
                            protected void convertView(ViewHolder viewHolder, final BaseNiceDialog baseNiceDialog) {
                                final EditText editText = viewHolder.getView(R.id.car_edit);
                                editText.setHint("請填寫配置信息");
                                BSSMCommonUtils.showKeyboard(editText);
                                viewHolder.setOnClickListener(R.id.car_edit_submit, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        if (editText.getText().toString().length()> 0) {
                                            mSubmitSecondCarModel.setConfigInfo(editText.getText().toString());
                                            configInfoTv.setText(mSubmitSecondCarModel.getConfigInfo());
                                            baseNiceDialog.dismiss();
                                        } else {
                                            Toast.makeText(PublishOwnSecondCarActivity.this, "請選擇配置信息", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                            }
                        })
                        .setShowBottom(true)
                        .show(getSupportFragmentManager());
                break;
            case R.id.publish_own_second_car_repiar_statr_ll:
                NiceDialog.init()
                        .setLayoutId(R.layout.dialog_car_edit)
                        .setConvertListener(new ViewConvertListener() {
                            @Override
                            protected void convertView(ViewHolder viewHolder, final BaseNiceDialog baseNiceDialog) {
                                final EditText editText = viewHolder.getView(R.id.car_edit);
                                editText.setHint("請填寫維修狀態");
                                BSSMCommonUtils.showKeyboard(editText);
                                viewHolder.setOnClickListener(R.id.car_edit_submit, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        if (editText.getText().toString().length()> 0) {
                                            mSubmitSecondCarModel.setStateOfRepiar(editText.getText().toString());
                                            repiarStatrTv.setText(mSubmitSecondCarModel.getStateOfRepiar());
                                            baseNiceDialog.dismiss();
                                        } else {
                                            Toast.makeText(PublishOwnSecondCarActivity.this, "請填寫維修狀態", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                            }
                        })
                        .setShowBottom(true)
                        .show(getSupportFragmentManager());
                break;
            /*case R.id.publish_own_second_car_inside_body_ll:
                NiceDialog.init()
                        .setLayoutId(R.layout.dialog_car_edit)
                        .setConvertListener(new ViewConvertListener() {
                            @Override
                            protected void convertView(ViewHolder viewHolder, final BaseNiceDialog baseNiceDialog) {
                                final EditText editText = viewHolder.getView(R.id.car_edit);
                                editText.setHint("請填寫車身內部描述");
                                BSSMCommonUtils.showKeyboard(editText);

                                viewHolder.setOnClickListener(R.id.car_edit_submit, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        if (editText.getText().toString().length()> 0) {
                                            mSubmitSecondCarModel.setInsideBody(editText.getText().toString());
                                            insideBodyTv.setText(mSubmitSecondCarModel.getInsideBody());
                                            baseNiceDialog.dismiss();
                                        } else {
                                            Toast.makeText(PublishOwnSecondCarActivity.this, "請填寫車身內部描述", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                            }
                        })
                        .setShowBottom(true)
                        .show(getSupportFragmentManager());
                break;*/
            case R.id.publish_own_second_car_inside_body_ll:
                NiceDialog.init()
                        .setLayoutId(R.layout.dialog_car_edit)
                        .setConvertListener(new ViewConvertListener() {
                            @Override
                            protected void convertView(ViewHolder viewHolder, final BaseNiceDialog baseNiceDialog) {
                                final EditText editText = viewHolder.getView(R.id.car_edit);
                                editText.setHint("請填寫車身內部信息");
                                BSSMCommonUtils.showKeyboard(editText);
                                viewHolder.setOnClickListener(R.id.car_edit_submit, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        if (editText.getText().toString().length()> 0) {
                                            mSubmitSecondCarModel.setInsideBody(editText.getText().toString());
                                            insideBodyTv.setText(mSubmitSecondCarModel.getInsideBody());
                                            baseNiceDialog.dismiss();
                                        } else {
                                            Toast.makeText(PublishOwnSecondCarActivity.this, "請填寫車身內部信息", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                            }
                        })
                        .setShowBottom(true)
                        .show(getSupportFragmentManager());
                break;
            case R.id.publish_own_second_car_test_conclusion_ll:
                NiceDialog.init()
                        .setLayoutId(R.layout.dialog_car_edit)
                        .setConvertListener(new ViewConvertListener() {
                            @Override
                            protected void convertView(ViewHolder viewHolder, final BaseNiceDialog baseNiceDialog) {
                                final EditText editText = viewHolder.getView(R.id.car_edit);
                                editText.setHint("請填寫試駕結論");
                                BSSMCommonUtils.showKeyboard(editText);
                                viewHolder.setOnClickListener(R.id.car_edit_submit, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        if (editText.getText().toString().length()> 0) {
                                            mSubmitSecondCarModel.setTestConclusion(editText.getText().toString());
                                            testConclusionTv.setText(mSubmitSecondCarModel.getTestConclusion());
                                            baseNiceDialog.dismiss();
                                        } else {
                                            Toast.makeText(PublishOwnSecondCarActivity.this, "請填寫試駕結論", Toast.LENGTH_LONG).show();
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

    /**
     * 獲取車輛品牌列表
     */
    private void callNetGetCarBrand() {
        RequestParams params = new RequestParams(BSSMConfigtor.BASE_URL + BSSMConfigtor.GET_CAR_BRAND_LIST + SPUtils.get(this, "UserToken", ""));
        params.setAsJsonContent(true);
        params.setConnectTimeout(30 * 1000);
        x.http().request(HttpMethod.POST ,params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                CarBrandOutModel model = new Gson().fromJson(result.toString(), CarBrandOutModel.class);
                if (model.getCode() == 200) {
                    mCarBrandModelList.addAll(model.getData());
                    NiceDialog.init()
                            .setLayoutId(R.layout.dialog_car_brand_list)
                            .setConvertListener(new ViewConvertListener() {
                                @Override
                                protected void convertView(ViewHolder viewHolder, final BaseNiceDialog baseNiceDialog) {
                                    ListView lv = viewHolder.getView(R.id.dialog_car_brand_lv);
                                    final List<String> list = new ArrayList<String>();
                                    for (CarBrandOutModel.CarBrandModel carBrandModel : mCarBrandModelList) {
                                        list.add(carBrandModel.getEngName());
                                    }
                                    lv.setAdapter(new SecondCarOptionListAdapter(PublishOwnSecondCarActivity.this, list));
                                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                            mSubmitSecondCarModel.setCarBrand(list.get(i));
                                            brandTv.setText(mSubmitSecondCarModel.getCarBrand());
                                            baseNiceDialog.dismiss();
                                        }
                                    });
                                }
                            })
                            .setShowBottom(true)
                            .show(getSupportFragmentManager());
                } else if (model.getCode() == 10000){
                    SPUtils.put(PublishOwnSecondCarActivity.this, "UserToken", "");
                    Toast.makeText(PublishOwnSecondCarActivity.this,  String.valueOf(model.getMsg()), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(PublishOwnSecondCarActivity.this, String.valueOf(model.getMsg()), Toast.LENGTH_SHORT).show();
                }
            }
            //请求异常后的回调方法
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if (ex instanceof SocketTimeoutException) {
                    Toast.makeText(PublishOwnSecondCarActivity.this, getString(R.string.timeout), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(PublishOwnSecondCarActivity.this, getString(R.string.no_net), Toast.LENGTH_SHORT).show();
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


    /**
     * 發佈帖文前判斷是否有照片
     */
    private void hasImage() {
        if (imgUrlList.size() > 1) {    // 有添加圖片
            // 提交照片到OSS
            submitImgToOss();
        } else {
            // 提示沒有照片
            Toast.makeText(this, "您未影像，請先影像再提交！", Toast.LENGTH_SHORT).show();
        }
    }

    private void submitImgToOss() {
        // 提交成功的集合
        // 照片數組
        final String[] imgArr = new String[imgUrlList.size() - 1];
        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 1:
                        putNum = 0;
                        // 提交有照片的帖文
                        String cover = "";
                        for (String s : imgArr) {
                            cover = cover + s + ",";
                        }
                        cover = cover.substring(0, cover.length() - 1);
                        Log.d("cover", cover);
                        submitSecondCar(cover);
                        break;
                }
            }
        };
        putImg(imgArr, handler);
    }


    /**
     * 提交二手車
     * @param cover
     */
    private void submitSecondCar(String cover) {
        RequestParams params = new RequestParams(BSSMConfigtor.BASE_URL + BSSMConfigtor.SUBMIT_OLD_CAR + SPUtils.get(this, "UserToken", ""));
        params.setAsJsonContent(true);
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("carNo","MO6593");
            jsonObj.put("carType",mSubmitSecondCarModel.getCarType());
            jsonObj.put("carBrand",mSubmitSecondCarModel.getCarBrand());
            jsonObj.put("carSeries",mSubmitSecondCarModel.getCarSeries());
            jsonObj.put("firstRegisterationTime",mSubmitSecondCarModel.getFirstRegisterationTime());
            jsonObj.put("driverMileage",mSubmitSecondCarModel.getDriverMileage());
            if (mSubmitSecondCarModel.getCarGears().equals("手動")) {
                jsonObj.put("carGears", 1);
            } else {
                jsonObj.put("carGears", 0);
            }
            jsonObj.put("carImg",cover);
            jsonObj.put("carPrices",mSubmitSecondCarModel.getCarPrices());
            jsonObj.put("tel",mSubmitSecondCarModel.getTel());
            jsonObj.put("isTelDisplayed", 0);
            jsonObj.put("carDescription",mSubmitSecondCarModel.getCarDescription());
            jsonObj.put("carColor",mSubmitSecondCarModel.getCarColor());
            jsonObj.put("type",mSubmitSecondCarModel.getType());
            jsonObj.put("exhaust",mSubmitSecondCarModel.getExhaust());
            jsonObj.put("countryOfOrigin",mSubmitSecondCarModel.getCountryOfOrigin());
            jsonObj.put("deliveryTime",mSubmitSecondCarModel.getDeliveryTime());
            jsonObj.put("releaseDate", BSSMCommonUtils.getTimeNoW());
            jsonObj.put("periodValidity", BSSMCommonUtils.getHalfYearTime());
            jsonObj.put("configInfo",mSubmitSecondCarModel.getConfigInfo());
            jsonObj.put("stateOfRepiar",mSubmitSecondCarModel.getStateOfRepiar());
            jsonObj.put("insideBody",mSubmitSecondCarModel.getInsideBody());
            jsonObj.put("testConclusion",mSubmitSecondCarModel.getTestConclusion());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String urlJson = jsonObj.toString();
        params.setBodyContent(urlJson);
        params.setConnectTimeout(30 * 1000);
        x.http().request(HttpMethod.POST ,params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                CommonJieModel model = new Gson().fromJson(result.toString(), CommonJieModel.class);
                if (model.getCode() == 200) {
                    setResult(RESULT_OK);
                    Toast.makeText(PublishOwnSecondCarActivity.this,  "車輛添加成功！", Toast.LENGTH_SHORT).show();
                    finish();
                } else if (model.getCode() == 10000){
                    SPUtils.put(PublishOwnSecondCarActivity.this, "UserToken", "");
                    Toast.makeText(PublishOwnSecondCarActivity.this,  String.valueOf(model.getMsg()), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(PublishOwnSecondCarActivity.this, String.valueOf(model.getMsg()), Toast.LENGTH_SHORT).show();
                }
            }
            //请求异常后的回调方法
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if (ex instanceof SocketTimeoutException) {
                    Toast.makeText(PublishOwnSecondCarActivity.this, getString(R.string.timeout), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(PublishOwnSecondCarActivity.this, getString(R.string.no_net), Toast.LENGTH_SHORT).show();
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

    // 提交的照片數量
    private int putNum;

    /**
     * 上傳圖片到OSS
     */
    private void putImg(final String[] imgArr, final Handler handler) {
        putNum ++;
        if (putNum == imgUrlList.size() || imgUrlList.get(putNum - 1).equals("")) {
            // 结束的处理逻辑，并退出该方法
            return;
        }

        Bitmap bitmap = BSSMCommonUtils.compressImageFromFile(imgUrlList.get(putNum - 1), 1024f);// 按尺寸压缩图片
        File filePut = BSSMCommonUtils.compressImage(bitmap, imgUrlList.get(putNum - 1));  //按质量压缩图片

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
                if (putNum <= imgUrlList.size() - 2) {
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
}
