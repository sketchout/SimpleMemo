package com.zeiyu.simplememo.activity;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

public class SignupActivity extends BaseActivity {

    private static final String TAG = SignupActivity.class.getSimpleName();

    // http://sourcey.com/beautiful-android-login-and-signup-screens-with-material-design/
    @InjectView(R.id.input_name) EditText _nameText;
    @InjectView(R.id.input_email) EditText _emailText;
    @InjectView(R.id.input_password) EditText _passwordText;
    @InjectView(R.id.btn_signup)    Button _signupButton;
    @InjectView(R.id.link_login )    TextView _loginLink;
    private FirebaseAuth fireAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        fireAuth = FirebaseAuth.getInstance();

        setInitialize();

    }

    private void setInitialize() {
        ButterKnife.inject(this);

        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signup();
            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void signup() {
        Log.d(TAG, "Signup");

        if ( !validate() ) {

            //onSignupFailed();
            return;
        }

        _signupButton.setEnabled(false);

        showProgrssDialogMessage("Creating Account...");

        String name = _nameText.getText().toString();
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        // TODO : Implements your own signup logic here
        fireAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if ( !task.isSuccessful() ) {

                            Exception ex = task.getException();
                            Log.d(TAG,"signIn failed, exception: " + ex.getMessage() );

                            onSignupFailed( ex.getMessage() );
                        } else {
                            onSignupSuccess();
                        }

                    }
                });

        // wait for result
        new android.os.Handler().postDelayed(
                new Runnable() {
                    @Override
                    public void run() {

                        if ( ! _signupButton.isEnabled() ) {
                            hideProgressDialog();
                            //onLoginFailed("Server is busy or etc,.  Please try again later");
                            showAlert("Infomation",
                                    "Currently server or network is poor condition. Please try again later" );
                        }
                    }
                } , 3000);
    }

    private void onSignupSuccess() {
        _signupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }

    private void onSignupFailed(String message) {

        Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
        //showAlert("Error", "Login Failed");
        _signupButton.setEnabled(true);

    }

    private boolean validate() {
        boolean valid = true;
        String name = _nameText.getText().toString();
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();
        if ( name.isEmpty() || name.length() < 3)  {
            _nameText.setError("at least 3 characters");
            valid = false;
        } else {
            _nameText.setError(null);
        }
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
