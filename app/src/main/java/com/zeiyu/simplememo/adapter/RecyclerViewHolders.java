package com.zeiyu.simplememo.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.zeiyu.simplememo.R;
import com.zeiyu.simplememo.model.Todo;

import java.util.List;

/**
 * Created by ZeiYu on 7/6/2016.
 */
public class RecyclerViewHolders extends RecyclerView.ViewHolder {

    private static final String TAG = RecyclerViewHolders.class.getSimpleName();

    public TextView content;
    public ImageView markIcon;
    public TextView title;
    public TextView dateStamp;
    public TextView timeStamp;
    public ImageView deleteIcon;
    private List<Todo> todoObject;

    //
    public RecyclerViewHolders(final View itemView,
                   final List<Todo> todoObject ) {

        super(itemView);

        // set
        this.todoObject = todoObject;

        title = (TextView)itemView.findViewById(R.id.todo_title);
        content = (TextView)itemView.findViewById(R.id.todo_content);
        dateStamp = (TextView)itemView.findViewById(R.id.todo_date);
        timeStamp = (TextView)itemView.findViewById(R.id.todo_time);
        markIcon = (ImageView)itemView.findViewById(R.id.todo_icon);
        deleteIcon =(ImageView)itemView.findViewById(R.id.todo_delete);

        // listen for delete
        deleteIcon.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {

            // Toast
            String title = todoObject.get(getAdapterPosition()).getTitle();
            Log.d(TAG,"deleteIcon - Todo Title:"+ title);

            // Lod.d
            DatabaseReference dbr = FirebaseDatabase.getInstance().getReference("todo");
            Query qry = dbr.orderByChild("title").equalTo(title);

            qry.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for( DataSnapshot item : dataSnapshot.getChildren() ) {
                        item.getRef().removeValue();
                        //item.getRef().setValue("alive",false);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.e(TAG,"onCancelled", databaseError.toException() );
                }
            });

            }
        });

    }
}
