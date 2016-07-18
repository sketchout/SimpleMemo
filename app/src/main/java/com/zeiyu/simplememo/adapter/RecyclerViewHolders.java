package com.zeiyu.simplememo.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.zeiyu.simplememo.R;
import com.zeiyu.simplememo.model.Todo;

import java.util.List;

/**
 * Created by ZeiYu on 7/6/2016.
 * https://github.com/habitissimo/vespapp/blob/master/app/src/main/java/com/habitissimo/vespapp/fotos/RecyclerViewHolders.java
 */
public class RecyclerViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener {

    // Set a click listener to a RecyclerView
    // http://antonioleiva.com/recyclerview-listener/
    private static final String TAG = RecyclerViewHolders.class.getSimpleName();
    private Callback callback;
    private Context context;

    public TextView memoTextView;
    public ImageView markIcon;
    public TextView subjectTextView;
    public TextView dateTextView;
    public TextView timeTextView;
    public ImageView deleteIcon;

    private List<Todo> todoList;

    //
    public RecyclerViewHolders(final View itemView,Context context,
                               final List<Todo> todoList, Callback callback) {
        super(itemView);
        this.context = context;
        this.callback = callback;

        // set
        this.todoList = todoList;

        subjectTextView = (TextView)itemView.findViewById(R.id.todo_title);
        memoTextView = (TextView)itemView.findViewById(R.id.todo_content);
        dateTextView = (TextView)itemView.findViewById(R.id.todo_date);
        timeTextView = (TextView)itemView.findViewById(R.id.todo_time);
        markIcon = (ImageView)itemView.findViewById(R.id.todo_icon);

        // Click Listener
        deleteIcon =(ImageView)itemView.findViewById(R.id.todo_delete);
        deleteIcon.setOnClickListener(this);

    }

//    public void bind(final Todo todoItem, final OnItemTodoClickListner listener) {
//
//        // subject & memo
//        subjectTextView.setText ( todoItem.getTodoSubject() );
//        memoTextView.setText( todoItem.getTodoMemo() );
//
//        // date & time
//        String timeString = StrUtil.timestampToString( todoItem.getTodoTimeStamp() );
//        String dateString = StrUtil.timestampToDate( todoItem.getTodoTimeStamp() ) ;
//
//        timeTextView.setText(timeString);
//        dateTextView.setText(dateString);
//
//        String iconString = todoItem.getTodoSubject().substring(0,1);
//        if ( iconString.isEmpty() ) iconString = "-";
//        int color = getColor(iconString);
//
//        TextDrawable drawableIcon = TextDrawable.builder()
//                //.buildRect(iconString,color);
//                .buildRoundRect(iconString,color,10);
//
//        itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                listener.onItemClick(todoItem);
//            }
//        });
//
//    }
    private int getColor(String iconString) {
        //Random rnd = new Random();
        //int num = Integer.parseInt(iconString);
        //int color = Color.argb(255,rnd.nextInt(256),rnd.nextInt(256),rnd.nextInt(256));
        ColorGenerator generater = ColorGenerator.MATERIAL;
        int color = generater.getColor(iconString);
        return color;
    }

    @Override
    public void onClick(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle(R.string.confirm_delete_title)
                .setMessage(R.string.confirm_delete_message)
                .setNegativeButton(R.string.confirm_delete_cancel,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                .setPositiveButton(R.string.confirm_delete_accept,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                callback.onDeleteProcess(getAdapterPosition());
                            }
                });

        builder.create().show();
    }
    public interface Callback {
        void onDeleteProcess(int index);
    }
}
