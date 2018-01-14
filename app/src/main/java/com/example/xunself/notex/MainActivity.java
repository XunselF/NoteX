package com.example.xunself.notex;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private FloatingActionButton add_note_button;
    //添加事件按钮

    private RecyclerView noteRecyclerView;

    private NoteAdapter noteAdapter;

    private List<Note> noteList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }


    /**
     * 初始化控件
     */
    private void init(){
        noteList = new ArrayList<>();
        add_note_button = (FloatingActionButton) findViewById(R.id.add_note_button);
        noteRecyclerView = (RecyclerView) findViewById(R.id.note_recyclerview);
        noteAdapter = new NoteAdapter();
        noteRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        noteRecyclerView.setAdapter(noteAdapter);
        add_note_button.setOnClickListener(this);

    }

    /**
     * 获取数据
     */
    private void getData(){
        noteList =  DataSupport.findAll(Note.class);
        Collections.sort(noteList, new Comparator<Note>() {
            @Override
            public int compare(Note n1, Note n2) {
                int year = n2.getYear() - n1.getYear();
                if (year == 0){
                    int month = n2.getMonth() - n1.getMonth();
                    if (month == 0){
                        int day = n2.getDay() - n1.getDay();
                        return day;
                    }
                    return month;
                }
                return year;
            }
        });
        noteAdapter.notifyDataSetChanged();
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

    class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder>{

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.note_layout,parent,false);
            ViewHolder holder = new ViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Note note = noteList.get(position);
            holder.noteYear.setText(note.getYear() + "");
            if (note.getTitle().equals("")){
                holder.noteTitle.setText("（为空）");
            }else{
                holder.noteTitle.setText(note.getTitle());
            }

            if (note.getContent().equals("")){
                holder.noteContent.setText("（为空）");
            }else{
                holder.noteContent.setText(note.getContent());
            }
            holder.noteTime.setText((note.getMonth() + 1) + "-" + note.getDay());
            if (position != 0 ){
                Note beforeNote = noteList.get(position - 1);
                if (beforeNote.getYear() == note.getYear()){
                    holder.displayYearLayout.setVisibility(View.GONE);
                }
            }
        }

        @Override
        public int getItemCount() {
            return noteList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder{
            LinearLayout displayYearLayout;
            TextView noteYear;
            TextView noteTitle;
            TextView noteTime;
            TextView noteContent;
            public ViewHolder(View itemView) {
                super(itemView);
                displayYearLayout = (LinearLayout) itemView.findViewById(R.id.display_year_layout);
                noteYear = (TextView) itemView.findViewById(R.id.note_year);
                noteTitle = (TextView) itemView.findViewById(R.id.note_title);
                noteTime = (TextView) itemView.findViewById(R.id.note_time);
                noteContent = (TextView) itemView.findViewById(R.id.note_content);
            }
        }
    }
}
