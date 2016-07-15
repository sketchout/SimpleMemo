package com.zeiyu.simplememo.activity;

import android.app.ProgressDialog;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.zeiyu.simplememo.R;

/**
 * Created by admin on 2016-07-15.
 */
public class BaseActivity extends AppCompatActivity {

    private ProgressDialog mProgressDialog;

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this, R.style.AppTheme_Dark_Dialog);
            //mProgressDialog.setCancelable(false);
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setMessage("Loading...");
        }
        mProgressDialog.show();
    }

    public void showProgrssDialogMessage(String message) {

        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setMessage(message);
        }
        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    public FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    public void showAlert(String title,String message) {

        // Authenticated failed with error firebaseError
        AlertDialog.Builder builder =
                new AlertDialog.Builder(this);
        builder.setMessage( message )
                .setTitle(title)
                .setPositiveButton(android.R.string.ok, null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // show
//    private boolean showMessageAlertYesNo(Context context, String title, String message) {
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        builder.setMessage(message)
//                .setTitle(title)
//                .setIcon(android.R.drawable.ic_dialog_alert)
//                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//
//                    }
//                })
//                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//
//                    }
//                });
//
//        AlertDialog dialog = builder.create();
//        dialog.show();
//
//        return false;
//    }
}
