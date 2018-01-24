package com.example.xunself.notex;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.litepal.crud.DataSupport;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.util.ArrayList;
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
    private LinearLayout openAlbumLayout;
    //打开相册
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

    private List<NoteImage> noteImageList;

    private RecyclerView noteImageRecyclerView;
    private NoteImageAdapter noteImageAdapter;

    //参数 -- 打开相册
    public static final int CHOOSE_PHOTO = 1;

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
        openAlbumLayout = (LinearLayout) findViewById(R.id.open_album_layout);
        returnButton = (ImageView) findViewById(R.id.return_button);
        selectTimeLayout = (LinearLayout) findViewById(R.id.select_time);
        timeText = (TextView) findViewById(R.id.note_time);
        titleEdit = (EditText) findViewById(R.id.note_title);
        contentEdit = (EditText) findViewById(R.id.note_content);
        submitButton = (LinearLayout) findViewById(R.id.submit_button);
        addNoteLayout = (LinearLayout) findViewById(R.id.add_note_layout);
        noteImageRecyclerView = (RecyclerView) findViewById(R.id.noteImage_recyclerview);
        noteImageAdapter = new NoteImageAdapter();
        noteImageRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        noteImageRecyclerView.setAdapter(noteImageAdapter);
        selectTimeLayout.setOnClickListener(this);
        returnButton.setOnClickListener(this);
        addNoteLayout.setOnClickListener(this);
        openAlbumLayout.setOnClickListener(this);
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
     * 获取图片数据
     */
    private void getNoteImageData(){
        if (bNote != null){
            noteImageList = DataSupport.where("noteId = ?",noteId + "").find(NoteImage.class);
        }else{
            noteImageList = new ArrayList<>();
        }
        noteImageAdapter.notifyDataSetChanged();
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
            getNoteImageData();
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
            case R.id.open_album_layout:
                //打开相册
                //SD卡权限询问
                if(ContextCompat.checkSelfPermission(AddNoteActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                    //如果没有开启该权限时，将弹窗让用户选择权限允许
                    ActivityCompat.requestPermissions(AddNoteActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE },1);
                }else{
                    //当已开启该权限时，将默认打开相册
                    openAlbum();
                }
                break;
            default:
                break;
        }
    }

    /**
     * 第一次权限开启后的回调方法
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode){
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    //当选择权限打开后执行
                    openAlbum();
                }else{
                    //当没有打开权限
                    Toast.makeText(AddNoteActivity.this,"您没有开启该权限",Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    /**
     * 打开相册
     */
    private void openAlbum(){
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent,CHOOSE_PHOTO);
    }

    /**
     * 对选择图片后的处理
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case CHOOSE_PHOTO:
                if (resultCode == RESULT_OK){
                    //判断手机系统版本号
                    if (Build.VERSION.SDK_INT >= 19){
                        //4.4以上进行处理
                        handleImageOnKitKat(data);
                    }else{
                        //4.4以下对图片的处理
                        handleImageBeforeKitKat(data);
                    }
                }
                break;
        }
    }

    /**
     * 4.4以上图片处理
     * @param data
     */
    @TargetApi(19)
    private void handleImageOnKitKat(Intent data){
        String imagePath = null;
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(this,uri)){
            //如果是Document类型的Uri，则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())){
                String id = docId.split(":")[1];    //解析出数字格式的id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,selection);
            }else if ("com.android.provides.downloads.documents".equals(uri.getAuthority())){
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),Long.valueOf(docId));
                imagePath = getImagePath(contentUri,null);
            }
        }else if ("content".equalsIgnoreCase(uri.getScheme())){
            //如果是content类型的uri，则使用普通的方式
            imagePath = getImagePath(uri,null);
        }else if ("file".equalsIgnoreCase(uri.getScheme())){
            //如果是file类型的uri，直接获取图片路径即可
            imagePath = uri.getPath();
        }
        //根据图片路径显示图片
        displayImage(imagePath);
    }

    /**
     * 4.4以下处理图片
     */
    private void handleImageBeforeKitKat(Intent data){
        Uri uri = data.getData();
        String imagePath = getImagePath(uri,null);
        displayImage(imagePath);
    }

    /**
     * 获取图片路径
     */
    private String getImagePath(Uri uri,String selection){
        String path = null;
        //通过Uri和selection来获取真实的图片路径
        Cursor cursor = getContentResolver().query(uri,null,selection,null,null);
        if (cursor != null){
            if (cursor.moveToFirst()){
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    /**
     * 显示图片
     */
    private void displayImage(String imagePath){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        if (imagePath != null){
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            bitmap.compress(Bitmap.CompressFormat.PNG,100,baos);
            byte[] image = baos.toByteArray();
            NoteImage noteImage = new NoteImage(noteId,image);
            noteImageList.add(noteImage);
            noteImageAdapter.notifyDataSetChanged();
        }else{
            Toast.makeText(AddNoteActivity.this,"错误图片显示",Toast.LENGTH_SHORT).show();
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




    class NoteImageAdapter extends RecyclerView.Adapter<NoteImageAdapter.ViewHolder>{

        class ViewHolder extends RecyclerView.ViewHolder{
            ImageView noteImage;
            LinearLayout imageItemLayout;

            public ViewHolder(View itemView) {
                super(itemView);
                noteImage = (ImageView) itemView.findViewById(R.id.note_image);
                imageItemLayout = (LinearLayout) itemView.findViewById(R.id.image_item_layout);
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(AddNoteActivity.this).inflate(R.layout.noteimage_item,parent,false);
            ViewHolder holder = new ViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            final NoteImage noteImage = noteImageList.get(position);
            Glide.with(AddNoteActivity.this)
                    .load(noteImage.getImage())
                    .into(holder.noteImage);
            holder.imageItemLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    PopupMenu popupMenu = new PopupMenu(AddNoteActivity.this,holder.imageItemLayout);
                    popupMenu.getMenuInflater().inflate(R.menu.image_item_menu,popupMenu.getMenu());
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            switch (menuItem.getItemId()){
                                case R.id.action_image_delete:
                                    noteImageList.remove(noteImage);
                                    noteImageAdapter.notifyDataSetChanged();
                                    break;
                                case R.id.action_image_inquire:
                                    noteImage.save();
                                    Intent intent = new Intent(AddNoteActivity.this,InquireImageActivity.class);
                                    intent.putExtra("extra_image_id",noteImage.getId());
                                    startActivity(intent);
                                    break;
                            }
                            return true;
                        }
                    });
                    popupMenu.show();
                    return true;
                }
            });
        }

        @Override
        public int getItemCount() {
            return noteImageList.size();
        }

        /**
         * blob转byte[]
         */
        private byte[] blobToBytes(Blob blob){
            InputStream is = null;
            byte[] b = null;
            try{
                is = blob.getBinaryStream();
                b = new byte[(int) blob.length()];
                is.read(b);
                return b;
            }catch(Exception e){
                e.printStackTrace();
            }finally{
                try{
                    is.close();
                    is = null;
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
            return b;
        }
    }
}
