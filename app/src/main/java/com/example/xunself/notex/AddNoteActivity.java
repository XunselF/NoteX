package com.example.xunself.notex;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.litepal.crud.DataSupport;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AddNoteActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView returnButton;
    //返回键
    private LinearLayout selectTimeLayout;
    //设置时间
    private TextView timeText;
    //显示时间
    private EditText titleEdit;
    //输入标题
    private EditText contentEdit;
    //输入内容
    private LinearLayout addNoteLayout;
    //父布局
    private LinearLayout submitButton;
    //确定键

    private int noteId;

    private Note bNote;


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
    protected void onResume() {
        super.onResume();
        getTime();
    }



    /**
     * 初始化
     */
    private void init(){
        returnButton = (ImageView) findViewById(R.id.return_button);
        selectTimeLayout = (LinearLayout) findViewById(R.id.select_time);
        timeText = (TextView) findViewById(R.id.note_time);
        titleEdit = (EditText) findViewById(R.id.note_title);
        contentEdit = (EditText) findViewById(R.id.note_content);
        submitButton = (LinearLayout) findViewById(R.id.submit_button);
        addNoteLayout = (LinearLayout) findViewById(R.id.add_note_layout);
        selectTimeLayout.setOnClickListener(this);
        returnButton.setOnClickListener(this);
        addNoteLayout.setOnClickListener(this);
        submitButton.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onClick(View view) {
                super.onClick(view);
                submitNote();
                finish();
            }
        });

        getExtraData();
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
     * 获取传过来的值
     */
    private void getExtraData(){
        Intent intent = getIntent();
        bNote = (Note) intent.getSerializableExtra("extra_note");
        if (bNote != null){
            mYear = bNote.getYear();
            mMonth = bNote.getMonth();
            mDay = bNote.getDay();
            titleEdit.setText(bNote.getTitle());
            contentEdit.setText(bNote.getContent());
            noteId = bNote.getId();
        }else{
            getCurrentTime();
        }
    }

    /**
     * 隐藏键盘
     */
    private void hideInputMethod(){
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager.isActive()){
            inputMethodManager.hideSoftInputFromWindow(addNoteLayout.getWindowToken(),0);
            addNoteLayout.clearFocus();
            addNoteLayout.setFocusable(false);
        }
    }

    /**
     *  将时间加载
     */
    private void getTime(){
        String time = mYear + "年" + (mMonth + 1) + "月" + mDay + "日";
        timeText.setText(time);
    }

    /**
     * 提交笔记数据
     */
    private void submitNote(){
        String title = titleEdit.getText().toString();
        String content = contentEdit.getText().toString();
        Note note = new Note(title,content,mYear,mMonth,mDay);
        if (noteId == 0){
            if (note.save()){
                Toast.makeText(AddNoteActivity.this,"保存成功",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(AddNoteActivity.this,"保存失败",Toast.LENGTH_SHORT).show();
            }
        }else{
            ContentValues values = new ContentValues();
            values.put("title",title);
            values.put("content",content);
            values.put("year",mYear);
            values.put("month",mMonth);
            values.put("day",mDay);
            DataSupport.update(Note.class,values,noteId);
            Toast.makeText(AddNoteActivity.this,"修改成功",Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.add_note_layout:
                hideInputMethod();
                break;
            case R.id.select_time:
                new DatePickerDialog(AddNoteActivity.this,onDateSetListener,mYear,mMonth,mDay).show();
                hideInputMethod();
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
