package com.zeiyu.simplememo.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ZeiYu on 7/6/2016.
 */
public class Memo {

    public Memo() {
        alive = true;
        Long t = System.currentTimeMillis();
        setTimeStamp(t);
    }

    public Memo(String subject) {

        this.subject = subject;
        alive = true;
        Long t = System.currentTimeMillis();
        setTimeStamp(t);
    }

    public Memo(String subject, Long timeStamp) {
        this.subject = subject;
        setTimeStamp(timeStamp);
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Long getTimeStamp() {
        return timeStamp;
    }
    public void setTimeStamp(Long todoTimeStamp) {
        Long r =0L;
        this.timeStamp = todoTimeStamp;

        try {
            r = todoTimeStamp * -1 ;
        } catch (Exception e ) {
            // ToDo :
            e.printStackTrace();
        }
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

        if ( subject.isEmpty()) return false;
        if ( content.isEmpty() ) return false;

        return true;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("subject", subject);
        map.put("content", content);
        map.put("alive", alive);
        map.put("timeStamp", timeStamp);
        map.put("timeStampReverse", timeStampReverse);
        return map;
    }

    // private
    private String subject;
    private String content;
    private Long timeStamp;
    private Long timeStampReverse;
    private Boolean alive;

    public static final String _child_key ="subject";
    public static final String _parent_key ="memo";
}
