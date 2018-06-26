package com.bs.john_li.bsfslotmachine.BSSMActivity.Mine;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.bs.john_li.bsfslotmachine.BSSMActivity.BaseActivity;
import com.bs.john_li.bsfslotmachine.BSSMAdapter.GuoJiangLongAdapter;
import com.bs.john_li.bsfslotmachine.BSSMView.BSSMHeadView;
import com.bs.john_li.bsfslotmachine.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by John_Li on 10/8/2017.
 */

public class GuoJiangLongActivity extends BaseActivity implements View.OnClickListener {
    private BSSMHeadView gjlHead;
    private GridView gjlGv;
    private List<String> gjlList;
    private GuoJiangLongAdapter mGuoJiangLongAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);setContentView(R.layout.activity_guojianglong);
        initView();
        setListener();
        initData();
    }

    @Override
    public void initView() {
        gjlHead = findViewById(R.id.gjl_head);
        gjlGv = findViewById(R.id.gjl_gv);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            gjlHead.setHeadHight();
        }
    }

    @Override
    public void setListener() {
        gjlGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // 使用系统的电话拨号服务，必须去声明权限，在AndroidManifest.xml中进行声明
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:65999631"));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                GuoJiangLongActivity.this.startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void initData() {
        gjlHead.setTitle("過江龍服務");
        gjlHead.setLeft(this);
        gjlList = new ArrayList<String>();
        gjlList.add("花地玛堂区");
        gjlList.add("圣安多尼堂区");
        gjlList.add("大堂区");
        gjlList.add("望德堂区");
        gjlList.add("风顺堂区");
        gjlList.add("嘉模堂区");
        gjlList.add("圣方济各堂区");
        mGuoJiangLongAdapter = new GuoJiangLongAdapter(this, gjlList);
        gjlGv.setAdapter(mGuoJiangLongAdapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.head_left:
                finish();
                break;
        }
    }
}
