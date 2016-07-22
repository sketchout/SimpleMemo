package com.zeiyu.simplememo.activity;

import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.NavUtils;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.zeiyu.simplememo.R;
import com.zeiyu.simplememo.model.Memo;
import com.zeiyu.simplememo.opensrc.CharacterCounterErrorWatcher;
import com.zeiyu.simplememo.util.StrUtil;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class EditActivity extends BaseActivity {

    private static final String TAG= EditActivity.class.getSimpleName();

    // http://www.parallelcodes.com/creating-a-notepad-application-in-android/

    @InjectView(R.id.input_subject) EditText _subjectText;
    @InjectView(R.id.input_memo) EditText _memoText;
    @InjectView(R.id.coordinator_edit) CoordinatorLayout _editCoordinator;

    private Snackbar snackbar;
    private boolean isUpdate = false;
    private String beforeSubject = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        enableHome();
        ButterKnife.inject(this);

        Bundle extras = getIntent().getExtras();
        if ( extras != null ) {
            String subject = extras.getString( Memo._child_key);
            String memo  = extras.getString("memo");
            if ( ! subject.isEmpty() ) {
//                snackbar = Snackbar.make(_editCoordinator,"Subject : "
//                        + String.valueOf(subject), Snackbar.LENGTH_LONG);

                isUpdate = true;

                Log.d(TAG,"isUpdate  :" + isUpdate );

                beforeSubject=subject;
                loadMemo(subject, memo);
            }
        }
    }
    private void loadMemo(String subject,String memo) {
        //DatabaseReference dbr = getMemoRef();
        //Query qry = dbr.orderByChild(Memo._child_key).equalTo(subject);
        _subjectText.setText(subject);
        _memoText.setText(memo);

    }

    private void enableHome() {

        TextInputLayout input_layout_subject =
                (TextInputLayout)findViewById(R.id.input_layout_subject);
        TextInputLayout input_layout_memo =
                (TextInputLayout)findViewById(R.id.input_playout_memo);

        input_layout_subject.getEditText().addTextChangedListener(new CharacterCounterErrorWatcher(input_layout_subject,0,10));
        input_layout_memo.getEditText().addTextChangedListener(new CharacterCounterErrorWatcher(input_layout_memo,0,200));


        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar_edit);
        setSupportActionBar(toolbar);
        if ( getSupportActionBar() != null ) {

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);  // the left caret
            //getSupportActionBar().setDisplayShowHomeEnabled(true); // the icon
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_edit, menu);

        return super.onCreateOptionsMenu(menu);
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
            case R.id.menu_save:
                saveNewMemo();
                return true;
            case R.id.menu_delete:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // save
    private void saveNewMemo() {

        String subject = _subjectText.getText().toString();
        String content = _memoText.getText().toString();

        if ( ! validateInput(subject) ) return ;

        Memo memo = new Memo( );
        if( subject.length() > 12 ) {
            memo.setSubject( subject.substring(0,12)+"..." );

        } else {
            memo.setSubject( subject );
        }
        memo.setContent(content);
        memo.setAlive(true);
        String timeString = StrUtil.timestampToString( memo.getTimeStamp()  ) ;
        Log.d(TAG,"saveNewMemo save time :" + timeString );
        if ( memo.validation() ) {
            //dbRef.push().setValue(todo);
            if ( isUpdate ) {
                Log.d(TAG,"updateMemo  :" + beforeSubject );
                updateMemo(memo, beforeSubject);
            }
            else {
                Log.d(TAG,"saveMemo  :" + subject );
                saveMemo(memo);
            }
        }
        //addTaskBox.setText(""); // clear
        NavUtils.navigateUpFromSameTask(this);
    }

    private boolean validateInput(String subject) {
        if (TextUtils.isEmpty(subject)) {
            Toast.makeText(EditActivity.this,
                    R.string.empty_string_msg, Toast.LENGTH_LONG).show();
            return false;
        } else if ( subject.length() < 6 ) {
            showAlert("Information", getString(R.string.short_string_msg));
            return false;
        }
        return true;
    }
}
