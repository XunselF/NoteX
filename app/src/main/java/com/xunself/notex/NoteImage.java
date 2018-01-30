package com.xunself.notex;

import org.litepal.crud.DataSupport;

import java.io.Serializable;
import java.sql.Blob;

/**
 * Created by XunselF on 2018/1/25.
 */

public class NoteImage extends DataSupport implements Serializable {
    private int id;
    private int noteId;
    private byte[] image;
    public NoteImage(int noteId,byte[] image){
        this.noteId = noteId;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public int getNoteId() {
        return noteId;
    }

    public byte[] getImage() {
        return image;
    }
}
