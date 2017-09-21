package com.bs.john_li.bsfslotmachine.BSSMActivity.Parking;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
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
import com.bs.john_li.bsfslotmachine.BSSMAdapter.SearchSlotMachineAdapter;
import com.bs.john_li.bsfslotmachine.BSSMUtils.StatusBarUtil;
import com.bs.john_li.bsfslotmachine.R;

import java.util.ArrayList;
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
                Toast.makeText(SearchSlotMachineActivity.this,"拍照停車",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void initData() {
        slotMachineList = new ArrayList<>();
        for (int i=0;i<3;i++) {
            slotMachineList.add("#" + i + "0000");
        }

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
            }
        });
        mCompleteText.setThreshold(1);

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
                callNetChangeData(newText);
                return false;
            }
        });
    }

    /**
     * 請求網絡搜索咪錶號
     * @param newText
     */
    private void callNetChangeData(String newText) {
        if (!newText.equals("")) {
            for (int i=0;i<3;i++) {
                slotMachineList.add("#0000" + i);
            }
        } else {
            slotMachineList.clear();
            for (int i=0;i<3;i++) {
                slotMachineList.add("#" + i + "0000");
            }
        }

        mSlotMachineAdapter.refreshData(slotMachineList);
    }
}
