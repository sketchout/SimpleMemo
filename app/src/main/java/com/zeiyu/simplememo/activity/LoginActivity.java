package com.zeiyu.simplememo.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.zeiyu.simplememo.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class LoginActivity extends BaseActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();
    private static final int REQUEST_SIGNUP = 0;

    // http://sourcey.com/beautiful-android-login-and-signup-screens-with-material-design/

    @InjectView(R.id.btn_login) Button _loginButton;
    @InjectView(R.id.input_email) EditText _emailText;
    @InjectView(R.id.input_password) EditText _passwordText;
    @InjectView(R.id.link_singup) TextView _signupLink;
    private FirebaseAuth fireAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        fireAuth = FirebaseAuth.getInstance();
        setInitialize();
    }

    private void setInitialize() {

        ButterKnife.inject(this);

        _loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // Start the Signup activity
                Intent intent = new Intent( getApplicationContext(), SignupActivity.class );
                startActivityForResult(intent, REQUEST_SIGNUP);
            }
        });

    }

    private void login() {

        Log.d(TAG, "Login");

        if ( !validate() ) {

            //onLoginFailed();
            return;
        }

        if (fireAuth == null) {
            showAlert("Error", "Temporarily can't connect to server.");
            return;
        }

        _loginButton.setEnabled(false);

//        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
//                R.style.AppTheme_Dark_Dialog);
//        progressDialog.setIndeterminate(true);
//        progressDialog.setMessage("Authenticating...");
//        progressDialog.show();

        showProgrssDialogMessage("Authenticating...");

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();
        //final boolean[] bResult = {false};

        // TODO : Implement your own authentication logic here.

        fireAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful() );

                        hideProgressDialog();

                        if ( !task.isSuccessful() ) {

                            Exception ex = task.getException();
                            Log.d(TAG,"signIn failed, exception: " + ex.getMessage() );

                            onLoginFailed( ex.getMessage() );
                            //bResult[0] = false;
                            //onLoginFailed("Authentication was failed.");
                            //progressDialog.dismiss();

                        } else {
                            // On complete call either onLoginSuccess or onLoginFailed
                            onLoginSuccess();
                            //bResult[0] = true;
                        }
                    }
        });
        // 
        new android.os.Handler().postDelayed(
                new Runnable() {
                    @Override
                    public void run() {

                        if ( ! _loginButton.isEnabled() ) {
                            hideProgressDialog();
                            //onLoginFailed("Server is busy or etc,.  Please try again later");
                            showAlert("Infomation",
                                    "Currently server or network is poor condition. Please try again later" );
                        }
                    }
                } , 3000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        if ( requestCode ==  REQUEST_SIGNUP )
        {
            if ( resultCode == RESULT_OK ) {
                // TODO :  Implement successful signup logic here
                // By default we just finish the activity and log them in automatically
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        //        super.onBackPressed();
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }

    private void onLoginSuccess() {
        _loginButton.setEnabled(true);
        finish();
    }

    private void onLoginFailed(String message) {
        //Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
        showAlert("Login Alert", message );
        _loginButton.setEnabled(true);
    }

    private boolean validate() {
        boolean valid = true;
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if ( email.isEmpty()
                || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() ) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }
        if ( password.isEmpty() || password.length() < 6 || password.length() > 10 ) {
            _passwordText.setError("between 6 and 10 characters ");
            valid = false;
        } else {
            _passwordText.setError(null);
        }
        return valid;
    }

}
