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

    private ProgressDialog mProgressDialog;

    // dialog progress
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

    // firebase auth
    public String getFireAuthUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }
    public FirebaseUser getFireAuthUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }
    public void setFireAuthSignout() {
        FirebaseAuth.getInstance().signOut();
    }

    // firebase database
    public DatabaseReference getFireDbRef() {
        return FirebaseDatabase.getInstance().getReference();
    }

    public DatabaseReference getFireDbChild(String sChild) {
        return FirebaseDatabase.getInstance().getReference(sChild);
    }

    public DatabaseReference getTodoReferenceChild() {
        return FirebaseDatabase.getInstance().getReference(getTodoPath());
    }

//    public void saveKeyValue(String sChild, String sKey, Map<String,Object> sValue)
//    {
//        getFireDbRef().child(sChild).child(sKey).setValue(sValue);
//    }

    public String getTodoPath() {
        return  "/"+ Memo._parent_key +"/" + getFireAuthUid() + "/";
    }

    public void saveMemo(Memo memo) {

        getFireDbChild( getTodoPath() ).push().setValue(memo);
    }

    public void updateMemo(Memo memo, String beforeSubject) {

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

}
