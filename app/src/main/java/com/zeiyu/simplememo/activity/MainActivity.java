package com.zeiyu.simplememo.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

    // add
    private EditText addTaskBox;
    private Button addTaskButton;
    // view
    private RecyclerView recyclerView;
    private ArrayList<Todo> listTodo;
    private LinearLayoutManager linearLayoutManager;
    private RecyclerViewAdapter recyclerViewAdatper;
    static final int MAX_CHAT_MESSAGES_TO_SHOW = 500;
    // firebase
    private FirebaseAuth fAuth;
    private FirebaseAuth.AuthStateListener fAuthListener;
    private DatabaseReference dbr;
    private FirebaseUser fUser;


    // onCreate
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // http://yookn.tistory.com/244
        checkFirebaseAuth();
        //setInitialize();
    }

    //FirebaseUser checkFirebaseAuth
    private void checkFirebaseAuth() {

        fUser = FirebaseAuth.getInstance().getCurrentUser();
        if ( fUser != null ) {
            Log.d(TAG, "onAuthStateChanged:uid() :" + fUser.getUid() );
            setInitialize();

        } else {
            Log.d(TAG, "onAuthStateChanged:signed_out");
            loadLoginActivity();
        }
    }

    // setInitialize
    private void setInitialize() {
        enableListAdapter();
        enableAddButton();
        enableAddToolbar();
        enableAddFloating();
        enableDbEventListen();
    }

    // enable
    private void enableListAdapter() {
        listTodo = new ArrayList<Todo>();
        recyclerView = (RecyclerView)findViewById(R.id.list_view);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
    }
    // enable
    private void enableDbEventListen() {
        dbr = FirebaseDatabase.getInstance().getReference("todo");
        dbr.limitToLast(MAX_CHAT_MESSAGES_TO_SHOW);
        dbr.orderByChild("timeStampReverse");

        dbr.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                getListTodo(dataSnapshot);
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                getListTodo(dataSnapshot);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

                deleteTodo(dataSnapshot);
            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
    // enable
    private void enableAddFloating() {
        // floataction
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG )
//                        .setAction("Action", null).show();
                loadEmptyActivity();
            }
        });
    }

    // enable
    private void enableAddToolbar() {
        // toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
    }

    // enable
    private void enableAddButton() {
        addTaskBox = (EditText)findViewById(R.id.add_task_box);

        addTaskButton =(Button)findViewById(R.id.add_task_button);
        addTaskButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {

                final String enteredTitle = addTaskBox.getText().toString();
                if (TextUtils.isEmpty( enteredTitle)) {
                    Toast.makeText(MainActivity.this,
                            R.string.empty_string_msg, Toast.LENGTH_LONG).show();
                    return;
                }
                if ( enteredTitle.length() < 6 ) {

//                    Toast.makeText(MainActivity.this,
//                            R.string.short_string_msg,Toast.LENGTH_LONG).show();
                    showMessageAlertOk(MainActivity.this,"Information",
                            getString(R.string.short_string_msg) );
                    return;
                }
                saveNewTodo(enteredTitle);
            }
        });
    }

    // onCreate
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    // onOptions
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            loadEmptyActivity();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // show
    private void showLoginAlert() {
        AlertDialog.Builder builder =
                new AlertDialog.Builder(MainActivity.this);
        builder.setMessage(R.string.login_notice_text)
                .setTitle(R.string.login_notice_title)
                .setPositiveButton(android.R.string.ok, null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // load
    private void loadLoginActivity()
    {
        //showLoginAlert();
        Intent intent = new Intent(this, DefaultLoginActivity.class);
        // New Task
        intent.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK );
        intent.addFlags( Intent.FLAG_ACTIVITY_CLEAR_TASK );

        this.startActivity(intent);
    }

    // load
    private void loadEmptyActivity()
    {
        Intent i = new Intent(this, EmptyActivity.class);
        //i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // clears the activity stack --
        // This prevents the user going back to the main activity
        // when they press the Back button from the login view
        //i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

        this.startActivity(i);
    }

    // save
    private void saveNewTodo(String enteredTitle) {

        Todo todo = new Todo( );
        if( enteredTitle.length() > 12 ) {
            todo.setTitle( enteredTitle.substring(0,12)+"..." );
        } else {
            todo.setTitle( enteredTitle );
        }
        todo.setContent(enteredTitle);
        todo.setAlive(true);

        String timeString = DateUtils.timestampToString( todo.getTimeStamp()  ) ;
        Log.d(TAG,"save time :" + timeString );

        if ( todo.validation() )
            dbr.push().setValue(todo);
        //dbr.push().child("todo").setValue(taskObject);
        //dbr.child("todo").child(userid).setValue(taskObject);
        //dbr.child("todo").setValue(taskObject);
        addTaskBox.setText(""); // clear
    }

    // delete
    private void deleteTodo(DataSnapshot dataSnapshot) {
        for(DataSnapshot item : dataSnapshot.getChildren() ) {
            String key = item.getKey();
            Log.d(TAG,"getKey :" + key );
            if ( key.equals("title") ) {
                String title = item.getValue(String.class);
                for( int i=0; i < listTodo.size(); i++ ) {
                    if( listTodo.get(i).getTitle().equals(title) ) {
                        listTodo.remove(i);
                    }
                }
                Log.d(TAG,"delettion title :" + title );
            }
            recyclerViewAdatper.notifyDataSetChanged();
            recyclerViewAdatper = new RecyclerViewAdapter(MainActivity.this, listTodo);
            recyclerView.setAdapter(recyclerViewAdatper);
            //recyclerView.scrollTo(0,0);
            recyclerView.scrollToPosition(recyclerViewAdatper.getItemCount()-1);
        }
    }

    // get
    private void getListTodo(DataSnapshot dataSnapshot) {

        Long tsLong = null;
        String sTitle = null;
        String sContent = null;

        for(DataSnapshot item : dataSnapshot.getChildren() ) {

            String key = item.getKey();
            Log.d(TAG,"getKey :" + key );
            //Todo todo = item.getValue(Todo.class);
            //Todo todo = item.child(key).getValue(Todo.class);
            if ( key.equals("timeStamp") ) tsLong = item.getValue(Long.class);
            if ( key.equals("title") ) sTitle = item.getValue(String.class);
            if ( key.equals("content") ) sContent = item.getValue(String.class);
        }
        Log.d(TAG,"sTitle :" + sTitle + ", tsLong :" + tsLong);
        Todo todo = new Todo(sTitle, tsLong);
        todo.setContent( sContent );
        listTodo.add( todo );

        recyclerViewAdatper = new RecyclerViewAdapter(MainActivity.this,listTodo );
        recyclerView.setAdapter(recyclerViewAdatper);
        //recyclerView.scrollTo(0,0);
        recyclerView.scrollToPosition(recyclerViewAdatper.getItemCount()-1);
    }

    // show
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
    // show
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

    // on
    @Override
    protected void onResume() {
        super.onResume();
    }

    // on
    @Override
    protected void onRestart() {
        super.onRestart();
    }
}
