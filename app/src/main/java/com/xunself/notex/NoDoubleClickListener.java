package com.xunself.notex;

import android.view.View;

import java.util.Calendar;

/**
 * Created by XunselF on 2018/1/15.
 */

public abstract class NoDoubleClickListener implements View.OnClickListener {
    public static final int MIN_CLICK_DELAY_TIME = 1000;
    private long lastClickTime = 0;

    @Override
    public void onClick(View view) {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME){
            lastClickTime = currentTime;
        }
    }
}
