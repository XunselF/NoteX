package com.example.xunself.notex;

import org.litepal.crud.DataSupport;

/**
 * Created by XunselF on 2018/1/13.
 */

public class Note extends DataSupport{
    private int id;
    private String title;
    private String content;
    private int year;
    private int month;
    private int day;

    public Note(String title,String content,int year,int month,int day){
        this.title = title;
        this.content = content;
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getDay() {
        return day;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    public String getContent() {
        return content;
    }
}
