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
import android.widget.TextView;

import java.util.ArrayList;
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
        Note note;
        for (int i = 0; i < 2; i++){
            note = new Note("123","123",2018,1,13);
            noteList.add(note);
        }
        note = new Note("插入","插入的数据",2018,1,13);
        noteList.add(1,note);
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
            holder.noteTitle.setText(note.getTitle());
            holder.noteContent.setText(note.getContent());
            holder.noteTime.setText((note.getMonth() + 1) + "-" + note.getDay());
        }

        @Override
        public int getItemCount() {
            return noteList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder{
            TextView noteYear;
            TextView noteTitle;
            TextView noteTime;
            TextView noteContent;
            public ViewHolder(View itemView) {
                super(itemView);
                noteYear = (TextView) itemView.findViewById(R.id.note_year);
                noteTitle = (TextView) itemView.findViewById(R.id.note_title);
                noteTime = (TextView) itemView.findViewById(R.id.note_time);
                noteContent = (TextView) itemView.findViewById(R.id.note_content);
            }
        }
    }
}
