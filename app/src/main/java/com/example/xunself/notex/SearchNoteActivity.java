package com.example.xunself.notex;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.litepal.crud.DataSupport;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class SearchNoteActivity extends AppCompatActivity {

    private SearchView noteSearchView;

    private RecyclerView noteRecyclerView;

    private SearchNoteActivity.NoteAdapter noteAdapter;

    private List<Note> noteList;

    private LinearLayout selectTimeLayout;

    private int mYear;

    private int mMonth;

    private int mDay;

    private TextView selectedTime;

    private String inputSearchText;

    private TextView clearButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_note);
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    /**
     * 初始化
     */
    private void init() {
        noteList = new ArrayList<>();
        selectTimeLayout = (LinearLayout) findViewById(R.id.select_time_layout);
        noteSearchView = (SearchView) findViewById(R.id.note_searchView);
        selectedTime = (TextView) findViewById(R.id.selected_time);
        clearButton = (TextView) findViewById(R.id.clear_button);
        noteRecyclerView = (RecyclerView) findViewById(R.id.note_recyclerview);
        noteRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        noteAdapter = new NoteAdapter();
        noteRecyclerView.setAdapter(noteAdapter);


        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mYear = 0;
                mMonth = 0;
                mDay = 0;
                selectedTime.setText("无限制");
                getSearchData(inputSearchText,getTimeData());
            }
        });

        noteSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                inputSearchText = newText;
                getSearchData(newText,getTimeData());
                return true;
            }
        });
        selectTimeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mYear == 0 && mMonth == 0 && mDay == 0){
                    Calendar c = Calendar.getInstance();
                    mYear = c.get(Calendar.YEAR);
                    mMonth = c.get(Calendar.MONTH);
                    mDay = c.get(Calendar.DAY_OF_MONTH);
                }
                new DatePickerDialog(SearchNoteActivity.this,onDateSetListener,mYear,mMonth,mDay).show();
            }
        });
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
            selectedTime.setText(mYear + "-" + (mMonth + 1) + "-" + mDay + "    最近五天");
            getSearchData(inputSearchText,getTimeData());
        }
    };
    /**
     * 进行搜索
     */
    private void getSearchData(String newText,List<Note> notes){
        noteList.clear();
        if (newText == null){
            noteList = notes;
        }else{
            for (int i = 0; i < notes.size(); i++){
                Note note = notes.get(i);
                if (note.getTitle().toUpperCase().indexOf(newText.toUpperCase()) != -1){
                    noteList.add(note);
                }
            }
        }
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

    /**
     * 时间搜索
     */
    private List<Note> getTimeData(){
        List<Note> notes = new ArrayList<>();
        if (mYear != 0 || mMonth != 0 || mDay != 0){

            try{
                String time = mYear + "-" + (mMonth + 1) + "-" + mDay;
                SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd");
                Date date =sdf.parse(time);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                calendar.add(Calendar.DAY_OF_MONTH,3);

                Date laterDate = calendar.getTime();

                calendar.add(Calendar.DAY_OF_MONTH, -6);

                Date currentDate = calendar.getTime();

                List<Note> noteList = DataSupport.findAll(Note.class);
                for (Note note : noteList){

                    String noteTime = note.getYear() + "-" + (note.getMonth() + 1) + "-" + note.getDay();
                    Date noteDate = sdf.parse(noteTime);
                    if (noteDate.compareTo(currentDate) > 0 && laterDate.compareTo(noteDate)> 0){
                        notes.add(note);
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }

        }else{
            notes = DataSupport.findAll(Note.class);
        }
        return notes;
    }

    /**
     * 获取数据
     */
    private void getData(){
        if (noteList.size() == 0){
            noteList =  DataSupport.findAll(Note.class);
        }else{
            getSearchData(inputSearchText,getTimeData());
        }
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


    class NoteAdapter extends RecyclerView.Adapter<SearchNoteActivity.NoteAdapter.ViewHolder>{

        @Override
        public NoteAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(SearchNoteActivity.this).inflate(R.layout.note_layout,parent,false);
            ViewHolder holder = new ViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(final SearchNoteActivity.NoteAdapter.ViewHolder holder, int position) {
            final Note note = noteList.get(position);
            holder.noteItemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(SearchNoteActivity.this,AddNoteActivity.class);
                    intent.putExtra("extra_note",note);
                    startActivity(intent);
                }
            });
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
            holder.noteTime.setText(note.getYear() + "-" + (note.getMonth() + 1) + "-" + note.getDay());
            holder.displayYearLayout.setVisibility(View.GONE);
        }

        @Override
        public int getItemCount() {
            return noteList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder{
            LinearLayout displayYearLayout;
            LinearLayout noteItemLayout;
            TextView noteYear;
            TextView noteTitle;
            TextView noteTime;
            TextView noteContent;
            public ViewHolder(View itemView) {
                super(itemView);
                displayYearLayout = (LinearLayout) itemView.findViewById(R.id.display_year_layout);
                noteItemLayout = (LinearLayout) itemView.findViewById(R.id.note_item_layout);
                noteYear = (TextView) itemView.findViewById(R.id.note_year);
                noteTitle = (TextView) itemView.findViewById(R.id.note_title);
                noteTime = (TextView) itemView.findViewById(R.id.note_time);
                noteContent = (TextView) itemView.findViewById(R.id.note_content);
            }
        }
    }
}
