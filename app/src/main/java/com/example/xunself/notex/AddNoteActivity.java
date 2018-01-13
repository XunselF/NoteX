package com.example.xunself.notex;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;

public class AddNoteActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView returnButton;
    //返回键
    private LinearLayout selectTimeLayout;
    //设置时间
    private TextView timeText;
    //显示时间


    private Date selectedDate;
    //所选时间

    private int mYear;
    //年
    private int mMonth;
    //月
    private int mDay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        init();
    }

    @Override
    protected void onPause() {
        super.onPause();
        getTime();
    }


    /**
     * 初始化
     */
    private void init(){
        returnButton = (ImageView) findViewById(R.id.return_button);
        selectTimeLayout = (LinearLayout) findViewById(R.id.select_time);
        timeText = (TextView) findViewById(R.id.note_time);
        selectTimeLayout.setOnClickListener(this);
        returnButton.setOnClickListener(this);

        getCurrentTime();
    }

    /**
     * 获取当前时间
     */
    private void getCurrentTime(){
        Calendar calendar = Calendar.getInstance();
        if (selectedDate == null){
            selectedDate = new Date();
        }
        calendar.setTime(selectedDate);
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);
    }

    /**
     *  将时间加载
     */
    private void getTime(){
        String time = mYear + "年" + (mMonth + 1) + "月" + mDay + "日";
        timeText.setText(time);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.select_time:
                new DatePickerDialog(AddNoteActivity.this,onDateSetListener,mYear,mMonth,mDay).show();
                break;
            case R.id.return_button:
                finish();
                break;
            default:
                break;
        }
    }

    /**
     * 选择日期弹窗监听
     */
    private DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener(){
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            mYear = year;
            mMonth = month;
            mDay = day;
            getTime();
        }
    };
}
