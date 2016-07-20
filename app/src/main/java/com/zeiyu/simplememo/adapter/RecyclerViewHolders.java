package com.zeiyu.simplememo.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.zeiyu.simplememo.R;
import com.zeiyu.simplememo.model.Memo;

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

    private LinearLayout linearLayout;
    public TextView memoTextView;
    public ImageView markIcon;
    public TextView subjectTextView;
    public TextView dateTextView;
    public TextView timeTextView;
    public ImageView deleteIcon;

    private List<Memo> todoList;
    //
    public RecyclerViewHolders(final View itemView, Context context,
                               final List<Memo> todoList, Callback callback) {
        super(itemView);

        // set
        this.context = context;
        this.callback = callback;
        this.todoList = todoList;

        // find view
        linearLayout = (LinearLayout)itemView.findViewById(R.id.linear_todo);
        subjectTextView = (TextView)itemView.findViewById(R.id.todo_title);
        memoTextView = (TextView)itemView.findViewById(R.id.todo_content);
        dateTextView = (TextView)itemView.findViewById(R.id.todo_date);
        timeTextView = (TextView)itemView.findViewById(R.id.todo_time);
        markIcon = (ImageView)itemView.findViewById(R.id.todo_icon);

        // Click Listener
        deleteIcon =(ImageView)itemView.findViewById(R.id.todo_delete);
        deleteIcon.setOnClickListener(this);

        // http://stackoverflow.com/questions/31790971/no-toast-shown-when-item-clicked-recyclerview
        itemView.setClickable(true);
        itemView.setFocusableInTouchMode(true);

        final Callback call = this.callback;
        itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
//                Toast.makeText( itemView.getContext(),"(i)Position:"
//                        +Integer.toString( getAdapterPosition() ), Toast.LENGTH_LONG ).show();
                call.onClickLinearLayout(getAdapterPosition());
            }
        });
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText( itemView.getContext(),"(L)Position:"
//                        +Integer.toString( getAdapterPosition() ), Toast.LENGTH_LONG ).show();
                call.onClickLinearLayout(getAdapterPosition());
            }
        });
    }

//    private int getColor(String iconString) {
//        //Random rnd = new Random();
//        //int num = Integer.parseInt(iconString);
//        //int color = Color.argb(255,rnd.nextInt(256),rnd.nextInt(256),rnd.nextInt(256));
//        ColorGenerator generater = ColorGenerator.MATERIAL;
//        int color = generater.getColor(iconString);
//        return color;
//    }

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
                                callback.onDeleteProcess( getAdapterPosition( ));
                            }
                });

        builder.create().show();
    }
    public interface Callback {
        void onDeleteProcess(int index);
        void onClickLinearLayout(int index);
    }
}
