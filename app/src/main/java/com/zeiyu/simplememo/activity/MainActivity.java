package com.zeiyu.simplememo.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.zeiyu.simplememo.R;
import com.zeiyu.simplememo.model.Memo;
import com.zeiyu.simplememo.util.StrUtil;
import com.zeiyu.simplememo.adapter.RecyclerViewAdapter;

import java.util.ArrayList;

public class MainActivity extends BaseActivity {

    private static final String TAG= MainActivity.class.getSimpleName();
    static final int MAX_CHAT_MESSAGES_TO_SHOW = 500;
    private ArrayList<Memo> listMemo;

    // view
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private RecyclerViewAdapter recyclerViewAdatper;

    // firebase
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

        if ( getFireAuthUser() != null ) {
            Log.d(TAG, "onAuthStateChanged:uid() :" + this.getFireAuthUid() );
            requestLogger();

            setInitialize();


        } else {
            Log.d(TAG, "onAuthStateChanged:signed_out");
            loadLoginActivity();
        }
    }

    // setInitialize
    private void setInitialize() {

        showProgrssDialogMessage("Loading...");

        enableListAdapter();
        // listener
        enableDbEventListener();

        enableAddToolbar();
        enableAddFloating();

        //enableAddButton();

//        new android.os.Handler().postDelayed(
//                new Runnable() {
//                    @Override
//                    public void run() {
//
//                        if ( ! loadingDone ) {
//                            //onLoginFailed("Server is busy or etc,.  Please try again later");
//                            showAlert("Infomation",
//                                    "Currently server or network is poor condition. Please try again later" );
//
//                        } else {
//                            enableAddToolbar();
//                            enableAddFloating();
//
//                            hideProgressDialog();
//                        }
//                    }
//                } , 3000);
    }

    // enable
    private void enableDbEventListener() {

        //dbRef = FirebaseDatabase.getInstance().getReference("todo");
        //dbRef = getReferenceChild("todo");

        dbRef = getMemoRef();
        dbRef.limitToLast(MAX_CHAT_MESSAGES_TO_SHOW);

        //dbRef.orderByChild("timeStampReverse"); // ????

        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG,"Loading done." + dataSnapshot.getChildrenCount());
                hideProgressDialog();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        dbRef.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                getListSingleItem(dataSnapshot);
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                getListSingleItem(dataSnapshot);
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
            case R.id.action_signout:
                //loadEmptyActivity();
                confirmSignOut();
                return true;
        }
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

    public void loadEditActivity() {
        Intent i = new Intent(this, EditActivity.class);
        //i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // clears the activity stack --
        // This prevents the user going back to the main activity
        // when they press the Back button from the login view
        //i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        this.startActivity(i);
    }

    public void loadEditActivity(String subject,String memo) {
        Intent i = new Intent(this, EditActivity.class);
        //i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // clears the activity stack --
        // This prevents the user going back to the main activity
        // when they press the Back button from the login view
        //i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

        Bundle bundle = new Bundle();
        bundle.putString( Memo._child_key, subject );
        bundle.putString("memo", memo );
        i.putExtras(bundle);

        this.startActivity(i);
    }


    // delete
    private void removeUpdateView(DataSnapshot dataSnapshot) {

        for(DataSnapshot item : dataSnapshot.getChildren() ) {

            String key = item.getKey();

            Log.d(TAG,"removeUpdateView getKey :" + key );


            if ( key.equals( Memo._child_key) ) {

                String title = item.getValue(String.class);
                for( int i=0; i < listMemo.size(); i++ ) {
                    if( listMemo.get(i).getSubject().equals(title) ) {
                        Log.d(TAG,"removeUpdateView remove :" + i );
                        listMemo.remove(i);
                    }
                }
                Log.d(TAG,"removeUpdateView subjectTextView :" + title );
            }
            recyclerViewAdatper.notifyDataSetChanged();
            //recyclerViewAdatper = new RecyclerViewAdapter(MainActivity.this, listTodo);
            recyclerViewAdatper = new RecyclerViewAdapter( MainActivity.this,listMemo
//                    ,
//                    new OnItemTodoClickListner() {
//
//                        @Override
//                        public void onItemClick(Memo todo) {
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

        Memo memo = new Memo( );

        if( enteredTitle.length() > 12 ) {
            memo.setSubject( enteredTitle.substring(0,12)+"..." );
        } else {
            memo.setSubject( enteredTitle );
        }
        memo.setContent(enteredTitle);
        memo.setAlive(true);
        String timeString = StrUtil.timestampToString( memo.getTimeStamp()  ) ;
        Log.d(TAG,"save time :" + timeString );
        if ( memo.validation() ) {
            //dbRef.push().setValue(todo);
            saveMemo(memo);
        }
        //dbRef.push().child("todo").setValue(taskObject);
        //dbRef.child("todo").child(userid).setValue(taskObject);
        //dbRef.child("todo").setValue(taskObject);
        //_todoText.setText(""); // clear
    }
    // get
    private void getListSingleItem(DataSnapshot dataSnapshot) {

        Long tsLong = null;
        String sSubject = null;
        String sContent = null;

        for(DataSnapshot item : dataSnapshot.getChildren() ) {

            String key = item.getKey();
            Log.d(TAG,"getListSingleItem getKey :" + key );

            if ( key.equals("timeStamp") ) tsLong = item.getValue(Long.class);
            if ( key.equals( Memo._child_key) ) sSubject = item.getValue(String.class);
            if ( key.equals("content") ) sContent = item.getValue(String.class);
        }
        Log.d(TAG,"getListSingleItem / subject :" + sSubject + ", tsLong :" + tsLong);

        Memo todo = new Memo(sSubject, tsLong);
        todo.setContent( sContent );

        listMemo.add( todo );

        recyclerViewAdatper = new RecyclerViewAdapter( MainActivity.this,listMemo
//                ,
//                new OnItemTodoClickListner() {
//
//                    @Override
//                    public void onItemClick(Memo todo) {
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
        listMemo = new ArrayList<Memo>();
        recyclerView = (RecyclerView)findViewById(R.id.list_view);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);


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
//    private void enableAddButton() {
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
//    }

}
