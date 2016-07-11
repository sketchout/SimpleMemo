package com.zeiyu.simplememo.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.zeiyu.simplememo.R;
import com.zeiyu.simplememo.model.Todo;
import com.zeiyu.simplememo.util.DateUtils;
import com.zeiyu.simplememo.view.RecyclerViewAdapter;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG= MainActivity.class.getSimpleName();

    private DatabaseReference dbr;
    private ArrayList<Todo> allTodo;
    private EditText addTaskBox;
    private RecyclerView recyclerView;
    private Button addTaskButton;
    private LinearLayoutManager linearLayoutManager;
    private RecyclerViewAdapter recyclerViewAdatper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        allTodo = new ArrayList<Todo>();
        dbr = FirebaseDatabase.getInstance().getReference("todo");

        addTaskBox = (EditText)findViewById(R.id.add_task_box);

        recyclerView = (RecyclerView)findViewById(R.id.task_list);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        addTaskButton =(Button)findViewById(R.id.add_task_button);

        /*
        // toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        */

        /*
        // floataction
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        */
        addTaskButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {

                final String enteredTitle = addTaskBox.getText().toString();
                if (TextUtils.isEmpty( enteredTitle)) {
                    Toast.makeText(MainActivity.this,
                            R.string.empty_string_msg,Toast.LENGTH_LONG).show();
                    return;
                }
                if ( enteredTitle.length() < 6 ) {

//                    Toast.makeText(MainActivity.this,
//                            R.string.short_string_msg,Toast.LENGTH_LONG).show();
                    showMessageAlertOk(MainActivity.this,"Information", getString(R.string.short_string_msg) );
                    return;
                }
                saveNewTodo(enteredTitle);
            }
        });

        dbr.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                getAllTodo(dataSnapshot);
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                getAllTodo(dataSnapshot);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

                todoDeletion(dataSnapshot);
            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void saveNewTodo(String enteredTitle) {

        Todo todo = new Todo( enteredTitle  );

        String timeString = DateUtils.timestampToString( todo.getTimeStamp()  ) ;
        Log.d(TAG,"save time :" + timeString );

        dbr.push().setValue(todo);
        //dbr.push().child("todo").setValue(taskObject);
        //dbr.child("todo").child(userid).setValue(taskObject);
        //dbr.child("todo").setValue(taskObject);
        addTaskBox.setText(""); // clear
    }

    private void todoDeletion(DataSnapshot dataSnapshot) {
        for(DataSnapshot item : dataSnapshot.getChildren() ) {
            String key = item.getKey();
            Log.d(TAG,"getKey :" + key );
            if ( key.equals("title") ) {
                String title = item.getValue(String.class);
                for( int i=0; i < allTodo.size(); i++ ) {
                    if( allTodo.get(i).getTitle().equals(title) ) {
                        allTodo.remove(i);
                    }
                }
                Log.d(TAG,"delettion title :" + title );
            }
            recyclerViewAdatper.notifyDataSetChanged();
            recyclerViewAdatper = new RecyclerViewAdapter(MainActivity.this, allTodo);

            recyclerView.setAdapter(recyclerViewAdatper);
            recyclerView.scrollToPosition(allTodo.size());
        }
    }

    private void getAllTodo(DataSnapshot dataSnapshot) {

        Long tsLong = null;
        String sTitle = null;
        for(DataSnapshot item : dataSnapshot.getChildren() ) {

            String key = item.getKey();
            Log.d(TAG,"getKey :" + key );
            //Todo todo = item.getValue(Todo.class);
            //Todo todo = item.child(key).getValue(Todo.class);
            if ( key.equals("timeStamp") ) tsLong = item.getValue(Long.class);
            if ( key.equals("title") ) sTitle = item.getValue(String.class);
        }
        Log.d(TAG,"sTitle :" + sTitle + ", tsLong :" + tsLong);
        allTodo.add( new Todo(sTitle, tsLong) );
        recyclerViewAdatper = new RecyclerViewAdapter(MainActivity.this, allTodo);
        recyclerView.setAdapter(recyclerViewAdatper);
        recyclerView.scrollToPosition(allTodo.size());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showMessageAlertOk(Context context, String title, String message) {
        AlertDialog.Builder builder =
                new AlertDialog.Builder(context);
        builder.setMessage(message)
                .setTitle(title)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.ok, null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private boolean showMessageAlertYesNo(Context context, String title, String message) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message)
                .setTitle(title)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();

        return false;
    }
}
