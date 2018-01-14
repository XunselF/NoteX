package com.example.xunself.notex;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

import org.litepal.LitePal;

/**
 * author:XunselF (XunselF@hotmail.com)
 * 2018/1/12
 * splash
 */
public class SplashActivity extends AppCompatActivity {

    private final int SPLASH_DISPLAY_LENGTH = 3000;
    //延迟时长
    private Handler splashDelayHandler;
    //延迟线程

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //进入全屏模式
        setContentView(R.layout.activity_splash);
        splashDelay();
    }

    /**
     * 定时器用于启动主页面
     */
    private void splashDelay(){
        splashDelayHandler = new Handler();
        splashDelayHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
                //退出全屏模式

                LitePal.initialize(SplashActivity.this);
                //数据库的初始化

                Intent intent = new Intent(SplashActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        },SPLASH_DISPLAY_LENGTH);
    }
}
