package com.bs.john_li.bsfslotmachine.BSSMActivity.Parking;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bs.john_li.bsfslotmachine.BSSMActivity.BaseActivity;
import com.bs.john_li.bsfslotmachine.BSSMActivity.LoginActivity;
import com.bs.john_li.bsfslotmachine.BSSMAdapter.SearchSlotMachineAdapter;
import com.bs.john_li.bsfslotmachine.BSSMModel.SlotMachineListModel;
import com.bs.john_li.bsfslotmachine.BSSMUtils.BSSMConfigtor;
import com.bs.john_li.bsfslotmachine.BSSMUtils.SPUtils;
import com.bs.john_li.bsfslotmachine.BSSMUtils.StatusBarUtil;
import com.bs.john_li.bsfslotmachine.R;
import com.google.android.gms.nearby.messages.internal.Update;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Type;
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
    private List<String> slotMachineList;
    private SearchSlotMachineAdapter mSlotMachineAdapter;
    private AutoCompleteTextView mCompleteText;
    private List<SlotMachineListModel.SlotMachineModel> smList;

    public static final int TAKE_PHOTO = 1;
    public static final int CROP_PHOTO = 2;
    private Uri imageUri; //图片路径
    private String filename; //图片名称
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

        /*if (savedInstanceState == null) {
            getFragmentManager().beginTransaction() .add(R.id.container, new PlaceholderFragment()) .commit();
        }*/
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
                Toast.makeText(SearchSlotMachineActivity.this,"拍照停車",Toast.LENGTH_SHORT).show();
                callCamare();
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
                String chooseStr = slotMachineList.get(position);
                mSearchView.setQuery(chooseStr,true);
                putInHistorySearch(position);
                Intent intent = new Intent(SearchSlotMachineActivity.this, ParkingOrderActivity.class);
                intent.putExtra("SlotMachine", new Gson().toJson(smList.get(position)));
                startActivity(intent);
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
                Toast.makeText(SearchSlotMachineActivity.this,query,Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                checkInputText(newText);
                return false;
            }
        });
    }

    /**
     * 檢查用戶輸入的內容
     * @param newText
     */
    private void checkInputText(String newText) {
        if (!newText.equals("")) {
            callNetChangeData(newText);
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
        } catch (JSONException e) {
            e.printStackTrace();
        }
        params.setBodyContent(jsonObj.toString());
        String uri = params.getUri();
        x.http().request(HttpMethod.POST, params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                SlotMachineListModel model = new Gson().fromJson(result.toString(), SlotMachineListModel.class);
                if (model.getCode() == 200) {
                    if (model.getData() != null) {
                        smList = model.getData();
                        updateAdapterData();
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(SearchSlotMachineActivity.this, "獲取失敗" + newText, Toast.LENGTH_SHORT).show();
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
        mSlotMachineAdapter.refreshData(slotMachineList);
    }

    /**
     * 獲取歷史搜索記錄
     */
    private void getHistorySearchFromSP() {
        String historySearch = (String) SPUtils.get(this, "SlotMachine", "");
        // 清空列表及顯示的列表
        smList.clear();
        slotMachineList.clear();
        // 插入歷史數據到列表中
        if (!historySearch.equals("")) {
            Type type = new TypeToken<ArrayList<SlotMachineListModel.SlotMachineModel>>() {}.getType();
            List<SlotMachineListModel.SlotMachineModel> historyList = new Gson().fromJson(historySearch, type);
            for (SlotMachineListModel.SlotMachineModel model: historyList){
                smList.add(model);
            }
        }

        // 插入新的數據到顯示的List
        for (SlotMachineListModel.SlotMachineModel model : smList) {
            slotMachineList.add(model.getMachineNo() + " " + model.getAddress());
        }
    }

    /**
     * 將搜索寫入搜索歷史
     */
    private void putInHistorySearch(int position) {
        String historySearch = (String) SPUtils.get(this, "SlotMachine", "");
        Type type = new TypeToken<ArrayList<SlotMachineListModel.SlotMachineModel>>() {}.getType();
        List<SlotMachineListModel.SlotMachineModel> historyList = null;
        if (!historySearch.equals("")) {
            historyList = new Gson().fromJson(historySearch, type);
            boolean hasSearch = false;
            for (SlotMachineListModel.SlotMachineModel model: historyList){
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

    private void callCamare() {
        //图片名称 时间命名
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date(System.currentTimeMillis());
        filename = format.format(date);
        //创建File对象用于存储拍照的图片 SD卡根目录
        //File outputImage = new File(Environment.getExternalStorageDirectory(),"test.jpg");
        //存储至DCIM文件夹
        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        File outputImage = new File(path,filename+".png");
        try {
            if(outputImage.exists()) {
                outputImage.delete();
            }
            outputImage.createNewFile();
        } catch(IOException e) {
            e.printStackTrace();
        }
        //将File对象转换为Uri并启动照相程序
        imageUri = Uri.fromFile(outputImage);
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE"); //照相
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri); //指定图片输出地址
        startActivityForResult(intent,TAKE_PHOTO); //启动照相
        //拍完照startActivityForResult() 结果返回onActivityResult()函数
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            Toast.makeText(SearchSlotMachineActivity.this, "影相失敗！", Toast.LENGTH_SHORT).show();
            return;
        }
        switch(requestCode) {
            case TAKE_PHOTO:
                Intent intent = new Intent(this, ParkingOrderActivity.class);
                intent.putExtra("way", BSSMConfigtor.SLOT_MACHINE_NOT_EXIST);
                intent.putExtra("imageUri", imageUri.getPath());
                startActivity(intent);
                finish();
                /*Intent intent = new Intent("com.android.camera.action.CROP"); //剪裁
                intent.setDataAndType(imageUri, "image*//*");
                intent.putExtra("scale", true);
                //设置宽高比例
                intent.putExtra("aspectX", 1);
                intent.putExtra("aspectY", 1);
                //设置裁剪图片宽高
                intent.putExtra("outputX", 340);
                intent.putExtra("outputY", 340);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                Toast.makeText(SearchSlotMachineActivity.this, "剪裁图片", Toast.LENGTH_SHORT).show();
                //广播刷新相册
                Intent intentBc = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                intentBc.setData(imageUri);
                this.sendBroadcast(intentBc);
                startActivityForResult(intent, CROP_PHOTO); //设置裁剪参数显示图片至ImageView*/
                break;
            default:
                break;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("imageUri", imageUri.getPath());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        File outputImage = new File((String) savedInstanceState.get("imageUri"));
        try {
            if(outputImage.exists()) {
                outputImage.delete();
            }
            outputImage.createNewFile();
        } catch(IOException e) {
            e.printStackTrace();
        }
        //将File对象转换为Uri并启动照相程序
        imageUri = Uri.fromFile(outputImage);
    }
}
