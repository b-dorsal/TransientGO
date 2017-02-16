package com.google.android.stardroid.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.stardroid.R;
import com.google.android.stardroid.activities.leaderboard.CreateUserTask;
import com.google.android.stardroid.activities.leaderboard.User;

import java.util.concurrent.ExecutionException;


public class activity_createuser extends Activity {


  private User ME = null;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.createuserdialog);

    // Set up your ActionBar
    ActionBar actionBar = getActionBar();
    actionBar.setDisplayShowHomeEnabled(false);
    actionBar.setDisplayShowCustomEnabled(true);
    actionBar.setTitle("Sign Up");




    Button btnCancel = (Button) findViewById(R.id.btnCancel);
    btnCancel.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        finishWithResult(0);
        System.out.println("BACK\n\n");
      }
    });
    Button btnSignup = (Button) findViewById(R.id.btnSignup);
    btnSignup.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        EditText name = (EditText) findViewById(R.id.txtNewName);
        EditText username = (EditText) findViewById(R.id.txtNewUsername);
        EditText pass = (EditText) findViewById(R.id.txtnewPassword);
        EditText pass2 = (EditText) findViewById(R.id.txtPassagain);

        String fullname = String.valueOf(name.getText());
        String uname = String.valueOf(username.getText());
        String password = String.valueOf(pass.getText());
        String passagain= String.valueOf(pass2.getText());
        System.out.println(fullname);
        System.out.println(uname);
        System.out.println(password);
        System.out.println(passagain);

        if(!password.equals(passagain)){
          pass2.setText("");
          pass2.setError("try again");
        }else{
          try {
            ME = new CreateUserTask().execute(uname, password, fullname).get();
          } catch (InterruptedException e) {
            e.printStackTrace();
          } catch (ExecutionException e) {
            e.printStackTrace();
          }
          if(ME == null){
            username.setText("");
            username.setError("already taken");
          }
        }
        if(ME != null){
          finishWithResult(1);
        }







        //finishWithResult(1);
        System.out.println("SIGNUP\n\n");
      }
    });




  }
  private void finishWithResult(int RESULT)
  {
    Intent intent = new Intent();
    finish();
  }


}