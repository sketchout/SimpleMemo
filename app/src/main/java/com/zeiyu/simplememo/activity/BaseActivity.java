package com.zeiyu.simplememo.activity;

import android.app.ProgressDialog;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.zeiyu.simplememo.R;
import com.zeiyu.simplememo.model.Memo;

/**
 * Created by admin on 2016-07-15.
 */
public class BaseActivity extends AppCompatActivity {

    private static final String TAG= BaseActivity.class.getSimpleName();


    // dialog progress
    private ProgressDialog mProgressDialog;

    protected void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this, R.style.AppTheme_Dark_Dialog);
            //mProgressDialog.setCancelable(false);
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setMessage("Loading...");
        }
        mProgressDialog.show();
    }

    protected void showProgrssDialogMessage(String message) {

        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setMessage(message);
        }
        mProgressDialog.show();
    }

    protected void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    // Firebase keys commonly used with backend Servlet instances
    private static final String INBOX ="inbox";
    private static final String REQLOG ="requestLogger";

    protected void requestLogger() {
        String uidHash = "client-"
                + Integer.toString( Math.abs( getFireAuthUid().hashCode() ) );
        getFireDbRef().child(REQLOG).push().setValue(uidHash);
        Log.d(TAG,"requestLogger : " + uidHash );
    }

    // firebase auth
    protected String getFireAuthUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }
    protected FirebaseUser getFireAuthUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }
    protected void setFireAuthSignout() {
        FirebaseAuth.getInstance().signOut();
    }

    // firebase database
    protected DatabaseReference getFireDbRef() {
        return FirebaseDatabase.getInstance().getReference();
    }

    protected DatabaseReference getFireDbChild(String sChild) {
        return FirebaseDatabase.getInstance().getReference(sChild);
    }

    // public
    public DatabaseReference getMemoRef() {
        return FirebaseDatabase.getInstance().getReference(getTodoPath());
    }

//    public void saveKeyValue(String sChild, String sKey, Map<String,Object> sValue)
//    {
//        getFireDbRef().child(sChild).child(sKey).setValue(sValue);
//    }

    protected String getTodoPath() {
        return  "/"+ Memo._parent_key +"/" + getFireAuthUid() + "/";
    }

    protected void saveMemo(Memo memo) {

        getFireDbChild( getTodoPath() ).push().setValue(memo);
    }

    protected void updateMemo(Memo memo, String beforeSubject) {

        final Memo m = memo;
        Query pendingTaks = getFireDbChild( getTodoPath() )
                .orderByChild(Memo._child_key).equalTo(beforeSubject);

        pendingTaks.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot item : dataSnapshot.getChildren() ) {

                    String key = item.getKey();
                    Log.d(TAG,"updateMemo getKey :" + key );

//                    if ( key.equals("timeStamp") )
//                        item.getRef().child("timeStamp").setValue(m.getTimeStamp());
//                    if ( key.equals( Memo._child_key) )
//                        item.getRef().child(Memo._child_key).setValue(m.getSubject());
//                    if ( key.equals("content") )
//                        item.getRef().child("content").setValue(m.getTimeStamp());

                    getFireDbChild( getTodoPath() ).child(key).setValue(m.toMap());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG,"updateMemo failed :" + databaseError.getMessage() );
            }
        });


        // lost key and save in root
        //DatabaseReference r = getFireDbChild(getTodoPath())
        // .orderByChild(Memo._child_key).equalTo(beforeSubject).getRef();
        //r.updateChildren( memo.toMap() );

    }

//    public Query getQueryEqualTo(String sChild, String sValue) {
//        //return getFireDbChild( getTodoPath()).orderByChild("todoSubject").equalTo(title);
//        return getFireDbChild( getTodoPath()).orderByChild(sChild).equalTo(sValue);
//    }

    // alert
    protected void showAlert(String title,String message) {
        // Authenticated failed with error firebaseError
        AlertDialog.Builder builder =
                new AlertDialog.Builder(this);
        builder.setMessage( message )
                .setTitle(title)
                .setPositiveButton(android.R.string.ok, null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
