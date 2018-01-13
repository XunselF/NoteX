package com.example.xunself.notex;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private FloatingActionButton add_note_button;
    //添加事件按钮

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    /**
     * 初始化控件
     */
    private void init(){
        add_note_button = (FloatingActionButton) findViewById(R.id.add_note_button);
        add_note_button.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){
            case R.id.add_note_button:
                intent = new Intent(MainActivity.this,AddNoteActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
