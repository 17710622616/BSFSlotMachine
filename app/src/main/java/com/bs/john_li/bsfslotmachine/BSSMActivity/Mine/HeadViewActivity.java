package com.bs.john_li.bsfslotmachine.BSSMActivity.Mine;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import com.bs.john_li.bsfslotmachine.BSSMActivity.BaseActivity;
import com.bs.john_li.bsfslotmachine.BSSMView.BSSMHeadView;
import com.bs.john_li.bsfslotmachine.R;
import com.othershe.nicedialog.BaseNiceDialog;
import com.othershe.nicedialog.NiceDialog;
import com.othershe.nicedialog.ViewConvertListener;
import com.othershe.nicedialog.ViewHolder;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by John_Li on 29/9/2017.
 */

public class HeadViewActivity extends BaseActivity implements View.OnClickListener{
    private BSSMHeadView head;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_headview);
    }

    @Override
    public void initView() {
        head = findViewById(R.id.headview_head);
    }

    @Override
    public void setListener() {

    }

    @Override
    public void initData() {
        head.setLeft(this);
        head.setTitle("頭像");
        head.setRight(R.mipmap.ok, this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.head_left:
                finish();
                break;
            case R.id.head_right:
                chooseGetImageWay();
                break;
        }
    }

    /**
     * 獲取照片的方式
     */
    private void chooseGetImageWay() {
        NiceDialog.init()
                .setLayoutId(R.layout.dialog_photo)
                .setConvertListener(new ViewConvertListener() {
                    @Override
                    protected void convertView(ViewHolder viewHolder, final BaseNiceDialog baseNiceDialog) {
                        viewHolder.setOnClickListener(R.id.photo_camare, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                baseNiceDialog.dismiss();
                            }
                        });
                        viewHolder.setOnClickListener(R.id.photo_album, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
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
}
