package com.zeiyu.simplememo.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zeiyu.simplememo.R;
import com.zeiyu.simplememo.model.Todo;
import com.zeiyu.simplememo.util.DateUtils;

import java.util.List;

/**
 * Created by ZeiYu on 7/6/2016.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewHolders> {

    private Context context;
    private List<Todo> todo;

    public RecyclerViewAdapter(Context context, List<Todo> todo) {
        this.context = context;
        this.todo = todo;
    }

    @Override
    public RecyclerViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerViewHolders viewHolder = null;
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.todo_list,parent, false);
        viewHolder = new RecyclerViewHolders(layoutView, todo);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolders holder, int position) {

        holder.title.setText( todo.get(position).getTitle() );

        String timeString = DateUtils.timestampToTime( todo.get(position).getTimeStamp()  ) ;
        String dateString = DateUtils.timestampToDate( todo.get(position).getTimeStamp()  ) ;
        holder.timeStamp.setText( timeString  );
        holder.dateStamp.setText( dateString  );

    }

    @Override
    public int getItemCount() {
        return this.todo.size();
    }


}
