package com.bs.john_li.bsfslotmachine;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.bs.john_li.bsfslotmachine.BSSMActivity.MainActivity;
import com.bs.john_li.bsfslotmachine.BSSMUtils.StatusBarUtil;
import com.bs.john_li.bsfslotmachine.BSSMView.BSSMHeadView;

public class SplashActivity extends AppCompatActivity implements View.OnClickListener{
    private BSSMHeadView splash_head;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        setContentView(R.layout.activity_splash);

        splash_head = (BSSMHeadView) findViewById(R.id.splash_head);
        TextView tv = (TextView) findViewById(R.id.splash_hello);

        splash_head.setTitle("欢迎页");
        splash_head.setLeft(this);
        splash_head.setRight(R.mipmap.wallet,this);

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
            }
        });
    }

    @Override
    public void onClick(View view) {

    }
}
