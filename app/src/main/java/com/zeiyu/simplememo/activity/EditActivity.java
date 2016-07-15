package com.zeiyu.simplememo.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.zeiyu.simplememo.R;

public class EditActivity extends AppCompatActivity {

    // http://www.parallelcodes.com/creating-a-notepad-application-in-android/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        Bundle extras = getIntent().getExtras();
        if ( extras != null ) {
            int value = extras.getInt("id");
        }
    }
}
