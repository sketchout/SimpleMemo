package com.zeiyu.simplememo.activity;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

import com.zeiyu.simplememo.R;

import butterknife.InjectView;

public class EditActivity extends AppCompatActivity {

    // http://www.parallelcodes.com/creating-a-notepad-application-in-android/

    @InjectView(R.id.input_subject) EditText _subjectText;
    @InjectView(R.id.input_memo) EditText _memoText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        Bundle extras = getIntent().getExtras();
        if ( extras != null ) {
            int value = extras.getInt("id");
            if ( value > 0 ) {
                snackbar = Snackbar.make(coordinatorLayout,"Memo Id : " + String.valueof(value),
                        Snackbar.LENGTH_LONG);
            }
        }
    }
}
