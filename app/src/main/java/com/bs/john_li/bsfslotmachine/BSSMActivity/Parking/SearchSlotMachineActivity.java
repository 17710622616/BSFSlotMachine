package com.bs.john_li.bsfslotmachine.BSSMActivity.Parking;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bs.john_li.bsfslotmachine.BSSMAdapter.SearchSlotMachineAdapter;
import com.bs.john_li.bsfslotmachine.BSSMModel.SlotMachineListOutsideModel;
import com.bs.john_li.bsfslotmachine.BSSMUtils.BSSMCommonUtils;
import com.bs.john_li.bsfslotmachine.BSSMUtils.BSSMConfigtor;
import com.bs.john_li.bsfslotmachine.BSSMUtils.PhotoUtils;
import com.bs.john_li.bsfslotmachine.BSSMUtils.SPUtils;
import com.bs.john_li.bsfslotmachine.BSSMUtils.StatusBarUtil;
import com.bs.john_li.bsfslotmachine.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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
import java.lang.reflect.Type;
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 搜索界面
 * Created by John_Li on 21/9/2017.
 */

public class SearchSlotMachineActivity extends AppCompatActivity {
    private SearchView mSearchView;
    private ImageView backIv;
    private LinearLayout noresultLl;
    private Toolbar mToolbar;
    private SearchSlotMachineAdapter mSlotMachineAdapter;
    private AutoCompleteTextView mCompleteText;
    // 顯示在搜索結果上的名稱集合
    private List<String> slotMachineList;
    // 搜索結果集合
    private List<SlotMachineListOutsideModel.SlotMachineListModel.SlotMachineModel> smList;

    private File fileUri;//照片文件路徑
    private Uri imageUri;//照片文件路徑
    private static final int CODE_GALLERY_REQUEST = 2;   //0xa0
    private static final int CODE_CAMERA_REQUEST = 1;    //0xa1
    private static final int CAMERA_PERMISSIONS_REQUEST_CODE = 0x03;
    private static final int STORAGE_PERMISSIONS_REQUEST_CODE = 0x04;
    private File dir; //圖片文件夾路徑
    //private File fileUri;  //照片文件
    private int pageSize = 10;
    private int pageNo = 1;
    private long totalCount;
    private String textChange = "";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_slot_machine);
        StatusBarUtil.setColor(this,getResources().getColor(R.color.colorSkyBlue));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        initView();
        setListener();
        initData();
    }

    public void initView() {
        mSearchView = (SearchView) findViewById(R.id.search_sm_sv);
        backIv = (ImageView) findViewById(R.id.search_sm_iv);
        noresultLl = (LinearLayout) findViewById(R.id.search_noresult_ll);
    }

    public void setListener() {
        backIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        noresultLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*NiceDialog.init()
                        .setLayoutId(R.layout.dialog_photo)
                        .setConvertListener(new ViewConvertListener() {
                            @Override
                            protected void convertView(ViewHolder viewHolder, final BaseNiceDialog baseNiceDialog) {
                                viewHolder.setOnClickListener(R.id.photo_camare, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        autoObtainCameraPermission();
                                        baseNiceDialog.dismiss();
                                    }
                                });
                                viewHolder.setOnClickListener(R.id.photo_album, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        autoObtainStoragePermission();
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
                        .show(getSupportFragmentManager());*/
            startActivity(new Intent(SearchSlotMachineActivity.this, InputUnknowSlotMachineActivity.class));
            }
        });
    }

    public void initData() {
        // 獲取歷史數據
        smList = new ArrayList<>();
        slotMachineList = new ArrayList<>();
        getHistorySearchFromSP();

        // searview
        //设置搜索框直接展开显示。左侧有放大镜(在搜索框中) 右侧有叉叉 可以关闭搜索框
        //mSearchView.setIconified(false);
        //设置搜索框直接展开显示。左侧有放大镜(在搜索框外) 右侧无叉叉 有输入内容后有叉叉 不能关闭搜索框
        //mSearchView.setIconifiedByDefault(false);
        //设置搜索框直接展开显示。左侧有无放大镜(在搜索框中) 右侧无叉叉 有输入内容后有叉叉 不能关闭搜索框
        mSearchView.onActionViewExpanded();
        mSearchView.setQueryHint("請輸入咪錶編號");

        // 自動編輯框
        int completeTextId = mSearchView.getResources().getIdentifier("android:id/search_src_text", null, null);
        mCompleteText = mSearchView.findViewById(R.id.search_src_text);
        mCompleteText.setTextColor(Color.WHITE);
        mSlotMachineAdapter = new SearchSlotMachineAdapter(this,slotMachineList);
        mCompleteText.setAdapter(mSlotMachineAdapter);
        mCompleteText.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String testname = slotMachineList.get(position);
                if (position == (slotMachineList.size() - 1) && testname.equals("查看更多 ")) {
                    //Toast.makeText(SearchSlotMachineActivity.this, "查看更多", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SearchSlotMachineActivity.this, SearchSlotMachineListActivity.class);
                    intent.putExtra("smList", new Gson().toJson(smList));
                    intent.putExtra("totalCount", String.valueOf(totalCount));
                    intent.putExtra("textChange", textChange);
                    //intent.putExtra("", "");
                    startActivity(intent);
                } else {
                    String chooseStr = slotMachineList.get(position);
                    mSearchView.setQuery(chooseStr,true);
                    // 將搜索寫入搜索歷史
                    putInHistorySearch(position);
                    Intent intent = null;
                    if (smList.get(position).getParkingSpaces() != null) {// 有子列表
                        intent = new Intent(SearchSlotMachineActivity.this, SlotMachineChildListActivity.class);
                        intent.putExtra("SlotMachineModel", new Gson().toJson(smList.get(position)));
                    } else {// 無子列表
                        intent = new Intent(SearchSlotMachineActivity.this, ParkingOrderActivity.class);
                        intent.putExtra("way", BSSMConfigtor.SLOT_MACHINE_EXIST);
                        intent.putExtra("SlotMachine", new Gson().toJson(smList.get(position)));
                    }
                    startActivity(intent);
                    finish();
                }
            }
        });
        mCompleteText.setThreshold(0);

        // 修改搜索框控件间的间隔
        LinearLayout search_edit_frame = mSearchView.findViewById(R.id.search_edit_frame);
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) search_edit_frame.getLayoutParams();
        params.leftMargin = 0;
        params.rightMargin = 10;
        search_edit_frame.setLayoutParams(params);

        // searchview字變化的監聽
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Toast.makeText(SearchSlotMachineActivity.this,query,Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                checkInputText(newText);
                return false;
            }
        });

        dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "BSSMPictures");
        if (!dir.exists()) {
            dir.mkdir();
        }
    }

    /**
     * 自动获取相机权限
     */
    private void autoObtainCameraPermission() {
        try {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                    Toast.makeText(SearchSlotMachineActivity.this, "您已经拒绝过一次", Toast.LENGTH_SHORT).show();
                }
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, CAMERA_PERMISSIONS_REQUEST_CODE);
            } else {//有权限直接调用系统相机拍照
                SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
                Date date = new Date(System.currentTimeMillis());
                fileUri = new File(dir.getPath() + "/order" + format.format(date) + ".jpg");
                imageUri = Uri.fromFile(fileUri);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                    imageUri = FileProvider.getUriForFile(SearchSlotMachineActivity.this, "com.bs.john_li.bsfslotmachine" + ".fileprovider", fileUri);//通过FileProvider创建一个content类型的Uri
                }

                PhotoUtils.takePicture(this, imageUri, CODE_CAMERA_REQUEST);
            }
        }catch (Exception e) {

        }
    }

    /**
     * 自动获取相冊权限
     */
    private void autoObtainStoragePermission() {
        // 使用意图直接调用手机相册  
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // 打开手机相册,设置请求码  
        startActivityForResult(intent, CODE_GALLERY_REQUEST);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case CAMERA_PERMISSIONS_REQUEST_CODE: {//调用系统相机申请拍照权限回调
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
                    Date date = new Date(System.currentTimeMillis());
                    fileUri = new File(dir.getPath() + "/order" + format.format(date) + ".jpg");
                    imageUri = Uri.fromFile(fileUri);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                        imageUri = FileProvider.getUriForFile(SearchSlotMachineActivity.this, "com.bs.john_li.bsfslotmachine" + ".fileprovider", fileUri);//通过FileProvider创建一个content类型的Uri
                    PhotoUtils.takePicture(this, imageUri, CODE_CAMERA_REQUEST);
                } else {
                    Toast.makeText(SearchSlotMachineActivity.this, "请允许打开相机", Toast.LENGTH_SHORT).show();
                }
                break;


            }
            case STORAGE_PERMISSIONS_REQUEST_CODE://调用系统相册申请Sdcard权限回调
                /*if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    PhotoUtils.openPic(this, CODE_GALLERY_REQUEST);
                } else {
                    Toast.makeText(SearchSlotMachineActivity.this, "请允许打操作SDCard", Toast.LENGTH_SHORT).show();
                }*/

                // 使用意图直接调用手机相册  
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                // 打开手机相册,设置请求码  
                startActivityForResult(intent, CODE_GALLERY_REQUEST);
                break;
        }
    }

    /**
     * 檢查用戶輸入的內容
     * @param newText
     */
    private void checkInputText(String newText) {
        if (!newText.equals("")) {
            if(!(newText.length() > 8)) {   // 當長度不大於八的時候進行搜索，本身咪錶編碼長度不大於八，其次防止最後選中是為了效果會自動把選中的填充導致的再次搜索
                callNetChangeData(newText);
            }
        } else {
            getHistorySearchFromSP();
            mSlotMachineAdapter.refreshData(slotMachineList);
        }
    }

    /**
     * 請求網絡搜索咪錶號
     * @param newText
     */
    private void callNetChangeData(final String newText) {
        RequestParams params = new RequestParams(BSSMConfigtor.BASE_URL + BSSMConfigtor.SEARCH_SLOT_MACHINE);
        params.setAsJsonContent(true);
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("key",newText);
            jsonObj.put("pageSize",pageSize);
            jsonObj.put("pageNo",pageNo);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        params.setBodyContent(jsonObj.toString());
        String uri = params.getUri();
        params.setConnectTimeout(30 * 1000);
        x.http().request(HttpMethod.POST, params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                textChange = newText;
                SlotMachineListOutsideModel model = new Gson().fromJson(result.toString(), SlotMachineListOutsideModel.class);
                if (model.getCode() == 200) {
                    if (model.getData() != null) {
                        totalCount = model.getData().getTotalCount();
                        if (model.getData().getData() != null) {
                            smList = model.getData().getData();
                            SlotMachineListOutsideModel.SlotMachineListModel.SlotMachineModel moreModel = new SlotMachineListOutsideModel.SlotMachineListModel.SlotMachineModel();
                            moreModel.setAddress("");
                            moreModel.setAreaCode("");
                            moreModel.setCarType(-1);
                            moreModel.setDistance(-1);
                            moreModel.setId(-1);
                            moreModel.setLatitude(-1);
                            moreModel.setLongitude(-1);
                            moreModel.setMachineNo("查看更多");
                            moreModel.setParkingSpaces(null);
                            moreModel.setAreaCode("");
                            moreModel.setPillarColor("");
                            smList.add(moreModel);
                            updateAdapterData();
                        } else {
                            SlotMachineListOutsideModel.SlotMachineListModel.SlotMachineModel moreModel = new SlotMachineListOutsideModel.SlotMachineListModel.SlotMachineModel();
                            moreModel.setAddress("");
                            moreModel.setAreaCode("");
                            moreModel.setCarType(-1);
                            moreModel.setDistance(-1);
                            moreModel.setId(-1);
                            moreModel.setLatitude(-1);
                            moreModel.setLongitude(-1);
                            moreModel.setMachineNo("暫無數據");
                            moreModel.setParkingSpaces(null);
                            moreModel.setAreaCode("");
                            moreModel.setPillarColor("");
                            smList.add(moreModel);
                            updateAdapterData();
                        }
                    } else {
                        SlotMachineListOutsideModel.SlotMachineListModel.SlotMachineModel moreModel = new SlotMachineListOutsideModel.SlotMachineListModel.SlotMachineModel();
                        moreModel.setAddress("");
                        moreModel.setAreaCode("");
                        moreModel.setCarType(-1);
                        moreModel.setDistance(-1);
                        moreModel.setId(-1);
                        moreModel.setLatitude(-1);
                        moreModel.setLongitude(-1);
                        moreModel.setMachineNo("暫無數據");
                        moreModel.setParkingSpaces(null);
                        moreModel.setAreaCode("");
                        moreModel.setPillarColor("");
                        smList.add(moreModel);
                        updateAdapterData();
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if (ex instanceof SocketTimeoutException) {
                    Toast.makeText(SearchSlotMachineActivity.this, "查詢超時，請重試！", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SearchSlotMachineActivity.this, "查詢出錯，請重試！", Toast.LENGTH_SHORT).show();
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
     * 獲得結果更新數據
     */
    private void updateAdapterData() {
        slotMachineList.clear();
        for (int i = 0; i < smList.size(); i++) {
            slotMachineList.add(smList.get(i).getMachineNo() + " " + smList.get(i).getAddress());
        }

        for (String str : slotMachineList) {
            Log.d("列表内容", str);
        }
        mSlotMachineAdapter.refreshData(slotMachineList);
    }

    /**
     * 獲取歷史搜索記錄
     */
    private void getHistorySearchFromSP() {
        String historySearch = (String) SPUtils.get(this, "SlotMachine", "");
        // 清空列表及顯示的列表
        if (smList != null && slotMachineList != null) {
            smList.clear();
            slotMachineList.clear();
            // 插入歷史數據到列表中
            if (!historySearch.equals("")) {
                Type type = new TypeToken<ArrayList<SlotMachineListOutsideModel.SlotMachineListModel.SlotMachineModel>>() {}.getType();
                List<SlotMachineListOutsideModel.SlotMachineListModel.SlotMachineModel> historyList = new Gson().fromJson(historySearch, type);
                for (SlotMachineListOutsideModel.SlotMachineListModel.SlotMachineModel model: historyList){
                    smList.add(model);
                }
            }

            // 插入新的數據到顯示的List
            for (SlotMachineListOutsideModel.SlotMachineListModel.SlotMachineModel model : smList) {
                slotMachineList.add(model.getMachineNo() + " " + model.getAddress());
            }
        }
    }

    /**
     * 將搜索寫入搜索歷史
     */
    private void putInHistorySearch(int position) {
        String historySearch = (String) SPUtils.get(this, "SlotMachine", "");
        Type type = new TypeToken<ArrayList<SlotMachineListOutsideModel.SlotMachineListModel.SlotMachineModel>>() {}.getType();
        List<SlotMachineListOutsideModel.SlotMachineListModel.SlotMachineModel> historyList = null;
        if (!historySearch.equals("")) {
            historyList = new Gson().fromJson(historySearch, type);
            boolean hasSearch = false;
            for (SlotMachineListOutsideModel.SlotMachineListModel.SlotMachineModel model: historyList){
                if (model.getMachineNo().equals(smList.get(position).getMachineNo())){
                    hasSearch = true;
                }
            }

            if (!hasSearch) {
                historyList.add(smList.get(position));
            }
        } else {
            historyList = new ArrayList<>();
            historyList.add(smList.get(position));
        }
        SPUtils.put(this, "SlotMachine", new Gson().toJson(historyList));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            Toast.makeText(SearchSlotMachineActivity.this, "影相失敗！", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent1 = new Intent(this, ParkingOrderActivity.class);
        switch(requestCode) {
            case CODE_CAMERA_REQUEST:
                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                Uri contentUri = Uri.fromFile(fileUri);
                mediaScanIntent.setData(contentUri);
                sendBroadcast(mediaScanIntent);
                intent1.putExtra("way", BSSMConfigtor.SLOT_MACHINE_NOT_EXIST);
                intent1.putExtra("imageUri", fileUri.getPath());
                startActivity(intent1);
                finish();
                break;
            case CODE_GALLERY_REQUEST:
                String imagePath = BSSMCommonUtils.getRealFilePath(this, data.getData());
                fileUri = new File(imagePath);
                intent1.putExtra("way", BSSMConfigtor.SLOT_MACHINE_NOT_EXIST);
                intent1.putExtra("imageUri", fileUri.getPath());
                startActivity(intent1);
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (fileUri != null){
            outState.putString("file_path", fileUri.getPath());
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        String cacheFileName = savedInstanceState.get("file_path").toString();
        if (cacheFileName != null) {
            if (!cacheFileName.equals("")) {
                fileUri = new File(cacheFileName);
            }
        }
    }
}
