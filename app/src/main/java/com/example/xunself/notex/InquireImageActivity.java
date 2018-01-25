package com.example.xunself.notex;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import org.litepal.crud.DataSupport;

import java.util.List;

public class InquireImageActivity extends AppCompatActivity {

    private ImageView noteImage;

    private NoteImage image;

    private byte[] imageByte;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inquire_image);
        init();
    }

    /**
     * 初始化
     */
    private void init(){
        int imageId = getIntent().getIntExtra("extra_image_id",0);
        List<NoteImage> images = DataSupport.findAll(NoteImage.class);
        image = DataSupport.where("id = ?",imageId + "").find(NoteImage.class).get(0);
        imageByte = image.getImage();

        noteImage = (ImageView) findViewById(R.id.note_image);
        Glide.with(InquireImageActivity.this)
                .load(imageByte)
                .into(noteImage);
        if (!getIntent().getBooleanExtra("extra_isExist",false)){
            image.delete();
        }
        noteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
