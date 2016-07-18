package com.zeiyu.simplememo.model;

/**
 * Created by ZeiYu on 7/6/2016.
 */
public class Todo {

    public Todo() {
        todoAlive = true;
        Long t = System.currentTimeMillis();
        setTodoTimeStamp(t);
    }

    public Todo(String title) {

        this.todoSubject = title;
        todoAlive = true;
        Long t = System.currentTimeMillis();
        setTodoTimeStamp(t);
    }

    public Todo(String title, Long timeStamp) {
        this.todoSubject = title;
        setTodoTimeStamp(timeStamp);
    }


    public String getTodoSubject() {
        return todoSubject;
    }
    public void setTodoSubject(String todoSubject) {
        this.todoSubject = todoSubject;
    }

    public String getTodoMemo() {
        return todoMemo;
    }
    public void setTodoMemo(String todoMemo) {
        this.todoMemo = todoMemo;
    }

    public Long getTodoTimeStamp() {
        return todoTimeStamp;
    }
    public void setTodoTimeStamp(Long todoTimeStamp) {
        Long r =0L;
        this.todoTimeStamp = todoTimeStamp;

        try {
            r = todoTimeStamp * -1 ;
        } catch (Exception e ) {
            // ToDo :
            e.printStackTrace();
        }
        setTodoTimeStampReverse(r);

    }

    public Long getTodoTimeStampReverse() {
        return todoTimeStampReverse;
    }
    public void setTodoTimeStampReverse(Long todoTimeStampReverse) {
        this.todoTimeStampReverse = todoTimeStampReverse;
    }

    public Boolean getTodoAlive() {
        return todoAlive;
    }
    public void setTodoAlive(Boolean todoAlive) {
        this.todoAlive = todoAlive;
    }

    public Boolean validation() {

        if ( todoSubject.isEmpty()) return false;
        if ( todoMemo.isEmpty() ) return false;

        return true;
    }

    private String todoSubject;
    private String todoMemo;
    private Long todoTimeStamp;
    private Long todoTimeStampReverse;
    private Boolean todoAlive;

}
