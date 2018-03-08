package com.bs.john_li.bsfslotmachine.BSSMActivity.Mine;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.bs.john_li.bsfslotmachine.BSSMActivity.BaseActivity;
import com.bs.john_li.bsfslotmachine.BSSMAdapter.OpinionPhotoAdapter;
import com.bs.john_li.bsfslotmachine.BSSMView.BSSMHeadView;
import com.bs.john_li.bsfslotmachine.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 意见反馈
 * Created by John_Li on 9/8/2017.
 */

public class OpinionActivity extends BaseActivity implements View.OnClickListener{
    private BSSMHeadView headView;
    private GridView opinionGv;
    private List<Integer> photoList;
    private OpinionPhotoAdapter mOpinionPhotoAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opinion);
        initView();
        setListener();
        initData();
    }

    @Override
    public void initView() {
        headView = findViewById(R.id.opinion_head);
        opinionGv = findViewById(R.id.opinion_gv);
    }

    @Override
    public void setListener() {
        opinionGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0){
                    Toast.makeText(OpinionActivity.this.getApplicationContext(), "拍照", Toast.LENGTH_SHORT).show();
                } else {

                }
            }
        });
    }

    @Override
    public void initData() {
        headView.setTitle("意見反饋");
        headView.setLeft(this);
        headView.setRight(R.mipmap.ok,this);

        photoList = new ArrayList<Integer>();
        photoList.add(R.mipmap.camera);
        mOpinionPhotoAdapter = new OpinionPhotoAdapter(this, photoList);
        opinionGv.setAdapter(mOpinionPhotoAdapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.head_left:
                finish();
                break;
            case R.id.head_right:
                break;
        }
    }
}
