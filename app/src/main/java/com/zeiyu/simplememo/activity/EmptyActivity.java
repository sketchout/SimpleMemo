package com.zeiyu.simplememo.activity;

import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.zeiyu.simplememo.R;

public class EmptyActivity extends AppCompatActivity {

    // http://www.parallelcodes.com/creating-a-notepad-application-in-android/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty);


        setInitialize();
    }

    private void setInitialize() {
        enableHome();
    }

    private void enableHome() {
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar_empty);
        setSupportActionBar(toolbar);
        if ( getSupportActionBar() != null ) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);  // the left caret
            //getSupportActionBar().setDisplayShowHomeEnabled(true); // the icon
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
