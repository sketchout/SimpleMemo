package com.zeiyu.simplememo.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
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
import com.zeiyu.simplememo.R;
import com.zeiyu.simplememo.model.Todo;
import com.zeiyu.simplememo.util.StrUtil;
import com.zeiyu.simplememo.adapter.RecyclerViewAdapter;

import java.util.ArrayList;

public class MainActivity extends BaseActivity {

    private static final String TAG= MainActivity.class.getSimpleName();

    // add
    private EditText _todoText;
    private Button _addButton;
    // view
    private RecyclerView recyclerView;
    private ArrayList<Todo> listTodo;
    private LinearLayoutManager linearLayoutManager;
    private RecyclerViewAdapter recyclerViewAdatper;
    static final int MAX_CHAT_MESSAGES_TO_SHOW = 500;
    // firebase
    private FirebaseAuth fAuth;
    private FirebaseAuth.AuthStateListener fAuthListener;
    private FirebaseUser fUser;
    private DatabaseReference dbRef;


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

        //fUser = FirebaseAuth.getInstance().getFireAuthUser();
        fUser = getFireAuthUser();
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
        // listener
        enableDbEventListener();
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
        switch ( id ) {
            case R.id.action_settings:
                return true;
            case R.id.action_signout:
                //loadEmptyActivity();
                confirmSignOut();
                return true;
        }
//        if (id == R.id.action_settings) {
//            loadEmptyActivity();
//            return true;
//        }
        return super.onOptionsItemSelected(item);
    }

    private void confirmSignOut() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(R.string.confirm_signout_title)
                .setMessage(R.string.confirm_signout_message)
                .setNegativeButton(R.string.confirm_signout_cancel,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                .setPositiveButton(R.string.confirm_signout_accept,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                // ToDo :
                                // callback.onDeleteProcess(getAdapterPosition());
                                signout();
                            }
                        });

        builder.create().show();
    }

    private void signout() {
        this.setFireAuthSignout();
        this.checkFirebaseAuth();
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

    // load
    private void loadLoginActivity()
    {
        Intent intent = new Intent(this, LoginActivity.class);
        // New Task
        //intent.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK );
        //intent.addFlags( Intent.FLAG_ACTIVITY_CLEAR_TASK );
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

    private void loadEditActivity() {
        Intent i = new Intent(this, EditActivity.class);
        //i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // clears the activity stack --
        // This prevents the user going back to the main activity
        // when they press the Back button from the login view
        //i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        this.startActivity(i);
    }



    // delete
    private void removeUpdateView(DataSnapshot dataSnapshot) {

        for(DataSnapshot item : dataSnapshot.getChildren() ) {
            String key = item.getKey();
            Log.d(TAG,"removeUpdateView getKey :" + key );

            if ( key.equals("todoSubject") ) {
                String title = item.getValue(String.class);
                for( int i=0; i < listTodo.size(); i++ ) {
                    if( listTodo.get(i).getTodoSubject().equals(title) ) {
                        Log.d(TAG,"removeUpdateView remove :" + i );
                        listTodo.remove(i);
                    }
                }
                Log.d(TAG,"removeUpdateView subjectTextView :" + title );
            }
            recyclerViewAdatper.notifyDataSetChanged();
            //recyclerViewAdatper = new RecyclerViewAdapter(MainActivity.this, listTodo);
            recyclerViewAdatper = new RecyclerViewAdapter( MainActivity.this,listTodo
//                    ,
//                    new OnItemTodoClickListner() {
//
//                        @Override
//                        public void onItemClick(Todo todo) {
//                            Toast.makeText(getBaseContext(), "Item Clieck", Toast.LENGTH_LONG).show();
//                        }
//                    }
            );

            recyclerView.setAdapter(recyclerViewAdatper);
            //recyclerView.scrollTo(0,0);
            recyclerView.scrollToPosition(recyclerViewAdatper.getItemCount()-1);
        }
    }

    // save
    private void saveNewTodo(String enteredTitle) {

        Todo todo = new Todo( );

        if( enteredTitle.length() > 12 ) {
            todo.setTodoSubject( enteredTitle.substring(0,12)+"..." );
        } else {
            todo.setTodoSubject( enteredTitle );
        }
        todo.setTodoMemo(enteredTitle);
        todo.setTodoAlive(true);
        String timeString = StrUtil.timestampToString( todo.getTodoTimeStamp()  ) ;
        Log.d(TAG,"save time :" + timeString );
        if ( todo.validation() ) {
            //dbRef.push().setValue(todo);
            saveTodo(todo);
        }
        //dbRef.push().child("todo").setValue(taskObject);
        //dbRef.child("todo").child(userid).setValue(taskObject);
        //dbRef.child("todo").setValue(taskObject);
        _todoText.setText(""); // clear
    }
    // get
    private void getListTodo(DataSnapshot dataSnapshot) {

        Long tsLong = null;
        String sSubject = null;
        String sMemo = null;

        for(DataSnapshot item : dataSnapshot.getChildren() ) {

            String key = item.getKey();
            Log.d(TAG,"getKey :" + key );

            //Todo todo = item.getValue(Todo.class);
            //Todo todo = item.child(key).getValue(Todo.class);
            if ( key.equals("todoTimeStamp") ) tsLong = item.getValue(Long.class);
            if ( key.equals("todoSubject") ) sSubject = item.getValue(String.class);
            if ( key.equals("todoMemo") ) sMemo = item.getValue(String.class);
        }
        Log.d(TAG,"getListTodo / sTitle :" + sSubject + ", tsLong :" + tsLong);

        Todo todo = new Todo(sSubject, tsLong);
        todo.setTodoMemo( sMemo );

        listTodo.add( todo );

        recyclerViewAdatper = new RecyclerViewAdapter( MainActivity.this,listTodo
//                ,
//                new OnItemTodoClickListner() {
//
//                    @Override
//                    public void onItemClick(Todo todo) {
//                        Toast.makeText(getBaseContext(), "Item Clieck", Toast.LENGTH_LONG).show();
//                    }
//                }
        );
        recyclerView.setAdapter(recyclerViewAdatper );
        //recyclerView.scrollTo(0,0);
        recyclerView.scrollToPosition(recyclerViewAdatper.getItemCount()-1);
    }

    // enable
    private void enableListAdapter() {
        listTodo = new ArrayList<Todo>();
        recyclerView = (RecyclerView)findViewById(R.id.list_view);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);


    }
    // enable
    private void enableDbEventListener() {

        //dbRef = FirebaseDatabase.getInstance().getReference("todo");
        //dbRef = getReferenceChild("todo");

        dbRef = getTodoReferenceChild();
        dbRef.limitToLast(MAX_CHAT_MESSAGES_TO_SHOW);
        dbRef.orderByChild("timeStampReverse"); // ????

        dbRef.addChildEventListener(new ChildEventListener() {
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

                removeUpdateView(dataSnapshot);
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
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_main);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG )
//                        .setAction("Action", null).show();
                loadEditActivity();
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
//        _todoText = (EditText)findViewById(R.id.input_todo_in_list);
//
//        _addButton =(Button)findViewById(R.id.btn_add_in_list);
//        _addButton.setOnClickListener(new View.OnClickListener(){
//
//            @Override
//            public void onClick(View view) {
//
//            final String enteredTitle = _todoText.getText().toString();
//            if (TextUtils.isEmpty( enteredTitle)) {
//                Toast.makeText(MainActivity.this,
//                        R.string.empty_string_msg, Toast.LENGTH_LONG).show();
//                return;
//            }
//            if ( enteredTitle.length() < 6 ) {
////                    Toast.makeText(MainActivity.this,
////                            R.string.short_string_msg,Toast.LENGTH_LONG).show();
//                showAlert("Information", getString(R.string.short_string_msg));
//
//                return;
//            }
//            saveNewTodo(enteredTitle);
//            }
//        });
    }

}
