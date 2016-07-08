package com.zeiyu.simplememo.model;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by ZeiYu on 7/6/2016.
 */
public class Todo {

    private String title;
    private Long timeStamp;

    public Todo() {}

    public Todo(String title) {

        this.title = title;

        Long tsLong = System.currentTimeMillis() ;
        //Long tsLong = Calendar.getInstance().getTime() / 1000L;
        //Long tsLong = Calendar.getInstance().getTime().getTimeInMillis()/1000L;

        setTimeStamp(tsLong);
    }
    public Todo(String title, Long timeStamp) {
        this.title = title;
        this.timeStamp = timeStamp;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Long timeStamp) {
        this.timeStamp = timeStamp;
    }


}
