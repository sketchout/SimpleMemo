package com.zeiyu.simplememo.view;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.zeiyu.simplememo.R;
import com.zeiyu.simplememo.model.Todo;
import com.zeiyu.simplememo.util.DateUtils;

import java.util.List;
import java.util.Random;

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
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_todo,parent, false);
        viewHolder = new RecyclerViewHolders(layoutView, todo);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolders holder, int position) {

        holder.title.setText( todo.get(position).getTitle() );
        holder.content.setText( todo.get(position).getContent());

        String timeString = DateUtils.timestampToTime( todo.get(position).getTimeStamp()  ) ;
        String dateString = DateUtils.timestampToDate( todo.get(position).getTimeStamp()  ) ;
        holder.timeStamp.setText( timeString  );
        holder.dateStamp.setText( dateString  );

        // icon
        String iconString = todo.get(position).getTitle().substring(0,1);
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
        return this.todo.size();
    }


}
