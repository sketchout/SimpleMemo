package com.zeiyu.simplememo.model;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by ZeiYu on 7/6/2016.
 */
public class Todo {

    public Todo() {
        alive = true;
        Long t = System.currentTimeMillis();
        setTimeStamp(t);
    }

    public Todo(String title) {

        this.title = title;
        alive = true;
        Long t = System.currentTimeMillis();
        setTimeStamp(t);
    }

    public Todo(String title, Long timeStamp) {
        this.title = title;
        setTimeStamp(timeStamp);
    }


    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }

    public Long getTimeStamp() {
        return timeStamp;
    }
    public void setTimeStamp(Long timeStamp) {

        this.timeStamp = timeStamp;

        Long r = timeStamp * -1 ;
        setTimeStampReverse(r);

    }

    public Long getTimeStampReverse() {
        return timeStampReverse;
    }
    public void setTimeStampReverse(Long timeStampReverse) {
        this.timeStampReverse = timeStampReverse;
    }

    public Boolean getAlive() {
        return alive;
    }
    public void setAlive(Boolean alive) {
        this.alive = alive;
    }

    public Boolean validation() {

        if ( title.isEmpty()) return false;
        if ( content.isEmpty() ) return false;

        return true;
    }

    private String title;
    private String content;
    private Long timeStamp;
    private Long timeStampReverse;
    private Boolean alive;

}
