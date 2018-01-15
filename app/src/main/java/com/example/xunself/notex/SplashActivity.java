package com.example.xunself.notex;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.util.Calendar;

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

                isFirstOpen();

                Intent intent = new Intent(SplashActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        },SPLASH_DISPLAY_LENGTH);
    }

    /**
     * 第一次打开应用操作
     */
    private void isFirstOpen(){
        if (DataSupport.isExist(Note.class)){
            Log.d("123","Note表已存在");
        }else{
            LitePal.initialize(SplashActivity.this);
            //数据库的初始化
            Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            Note note = new Note("欢迎使用NoteX","您可以通过右下角添加键进行添加数据",year,month,day);
            note.save();
            Log.d("456","Note表不存在");
        }
    }
}
