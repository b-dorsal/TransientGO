package com.google.android.stardroid.activities;


//


import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.stardroid.R;
import com.google.android.stardroid.activities.leaderboard.User;
import com.google.android.stardroid.activities.leaderboard.UserTask;

import java.util.concurrent.ExecutionException;

public class activity_user_login extends Activity {

    private EditText mEmailView;
    private EditText mPasswordView;
    private User ME;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);

        // Set up your ActionBar
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setTitle(R.string.login_title);

        mEmailView = (EditText) findViewById(R.id.tbxUsername);
        mPasswordView = (EditText) findViewById(R.id.tbxPassword);
        Button mEmailSignInButton = (Button) findViewById(R.id.btnSignIn);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    attemptLogin();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });


        TextView txtCreate = (TextView) findViewById(R.id.btnCreateUser);

        SpannableString content = new SpannableString("Sign Up for Transient Go");
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        txtCreate.setText(content);


        txtCreate.setOnClickListener(new OnClickListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {

                Intent CreateUserIntent = new Intent(activity_user_login.this, activity_createuser.class);
                startActivity(CreateUserIntent);

            }
        });


        Button btnCancel = (Button) findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                finishWithResult(1);
                System.out.println("BACK\n\n");
            }
        });
    }
    private void finishWithResult(int RESULT)
    {
        Intent intent = new Intent();
        setResult(RESULT, intent);
        intent.putExtra("userobj", this.ME);
        setResult(RESULT, intent);
        finish();
    }
    private void attemptLogin() throws ExecutionException, InterruptedException {
        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);
        // Store values at the time of the login attempt.
        String username = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError("Invalid Password");
        }else {
            // Check for a valid username address.
            if (TextUtils.isEmpty(username)) {
                mEmailView.setError("Missing Fields");
            } else if (!isEmailValid(username)) {
                mEmailView.setError("Invalid Username");
            } else {
                finishWithResult(authenticate(username, password));
            }
        }
    }


    private int authenticate(String name, String pass)
        throws ExecutionException, InterruptedException {
        //return 1 if successful
        //return 0 if failed

        ME = new UserTask().execute(name,pass).get();
        if(ME != null) {
            return 1;
        }else{
            ME = null;
            return 0;
        }
    }

    private boolean isEmailValid(String username) {
        //TODO: Replace this with your own logic
        return username.length() >= 4;
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() >=4;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 1){
            mEmailView.setError("Sign in with your new account");
        }
    }



}