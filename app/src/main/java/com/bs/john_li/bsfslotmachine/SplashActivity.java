package com.bs.john_li.bsfslotmachine;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bs.john_li.bsfslotmachine.BSSMActivity.MainActivity;
import com.bs.john_li.bsfslotmachine.BSSMUtils.BSSMApplication;
import com.bs.john_li.bsfslotmachine.BSSMUtils.BSSMConfigtor;
import com.bs.john_li.bsfslotmachine.BSSMUtils.StatusBarUtil;
import com.bs.john_li.bsfslotmachine.BSSMView.BSSMHeadView;

public class SplashActivity extends AppCompatActivity implements View.OnClickListener{
    private BSSMHeadView splash_head;
    private ImageView splashIV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BSSMApplication app = (BSSMApplication) SplashActivity.this.getApplication();

        BSSMConfigtor.OS_TYPE = Build.VERSION.RELEASE;
        BSSMConfigtor.OS_TYPE = Build.VERSION.RELEASE;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        setContentView(R.layout.activity_splash);

        splashIV = (ImageView) findViewById(R.id.splash_iv);
        splashIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(SplashActivity.this, MainActivity.class));
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }
        }, 3000);
    }

    @Override
    public void onClick(View view) {

    }
}
