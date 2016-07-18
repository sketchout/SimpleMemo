package com.zeiyu.simplememo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.zeiyu.simplememo.R;
import com.zeiyu.simplememo.activity.MainActivity;
import com.zeiyu.simplememo.model.Todo;
import com.zeiyu.simplememo.util.StrUtil;

import java.util.List;

/**
 * Created by ZeiYu on 7/6/2016.
 */
public class RecyclerViewAdapter
        extends RecyclerView.Adapter<RecyclerViewHolders> {

    private static final String TAG = RecyclerViewAdapter.class.getSimpleName();

    //private final OnItemTodoClickListner listener;
    private Context context;
    private final List<Todo> todoList;

    public RecyclerViewAdapter(Context context, List<Todo> todoList
                                //,OnItemTodoClickListner listener
                               ) {
        this.context = context;
        this.todoList = todoList;
        //this.listener = listener;
    }

    @Override
    public RecyclerViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {

        // viewHolder
        RecyclerViewHolders viewHolder = null;

        // view - item
        View layoutView = LayoutInflater.from(
                parent.getContext()).inflate(R.layout.item_todo,parent, false);

        // viewHolder
        viewHolder = new RecyclerViewHolders(layoutView, context, todoList,
                new RecyclerViewHolders.Callback() {

            @Override
            public void onDeleteProcess(int index) {

                String title = todoList.get(index).getTodoSubject();
                // Lod.d
                Log.d(TAG,"deleteIcon - Todo Title:"+ title);
                // find
                //DatabaseReference dbr = FirebaseDatabase.getInstance().getReference("todo");

                DatabaseReference dbr = ((MainActivity)context).getTodoReferenceChild();
                Query qry = dbr.orderByChild("todoSubject").equalTo(title);
                //Query qry = ((MainActivity)context).getQueryEqualTo(title);

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
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolders holder, int position) {

        Log.d(TAG,"onBindViewHolder:" + position);
        Log.d(TAG,"todoList.get(position).getTodoSubject():" + todoList.get(position).getTodoSubject());
        //holder.bind(todoList.get(position), listener );

        holder.subjectTextView.setText( todoList.get(position).getTodoSubject() );
        holder.memoTextView.setText( todoList.get(position).getTodoMemo());

        String timeString = StrUtil.timestampToTime( todoList.get(position).getTodoTimeStamp()  ) ;
        String dateString = StrUtil.timestampToDate( todoList.get(position).getTodoTimeStamp()  ) ;

        holder.timeTextView.setText( timeString  );
        holder.dateTextView.setText( dateString  );

        // icon
        String iconString = todoList.get(position).getTodoSubject().substring(0,1);
        if ( iconString.isEmpty() ) iconString ="_";
        int color = getColor(iconString);

        TextDrawable drawableIcon = TextDrawable.builder()
                //.buildRect(iconString,color);
                .buildRoundRect(iconString,color,10);

        holder.markIcon.setImageDrawable(drawableIcon);

    }

    private int getColor(String iconString) {
        //Random rnd = new Random();
        //int num = Integer.parseInt(iconString);
        //int color = Color.argb(255,rnd.nextInt(256),rnd.nextInt(256),rnd.nextInt(256));
        ColorGenerator generater = ColorGenerator.MATERIAL;
        int color = generater.getColor(iconString);
        return color;
    }

    @Override
    public int getItemCount() {
        return this.todoList.size();
    }


}
