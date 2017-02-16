package com.google.android.stardroid.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.stardroid.R;
import com.google.android.stardroid.activities.leaderboard.User;

/**
 * Created by Brian Dorsey on 12/6/16.
 */


public class activity_userdata extends Activity{

  private User me;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_userdata);

    me = (User) getIntent().getSerializableExtra("user");

    // Set up your ActionBar
    ActionBar actionBar = getActionBar();
    actionBar.setDisplayShowHomeEnabled(false);
    actionBar.setDisplayShowCustomEnabled(true);
    actionBar.setTitle("My Account");

    TextView username = (TextView) findViewById(R.id.txtUsername);
    TextView name = (TextView) findViewById(R.id.txtName);
    TextView score = (TextView) findViewById(R.id.txtmyScore);
    TextView caught = (TextView) findViewById(R.id.txtNumCaught);


    username.setText(me.getUserId());
    name.setText(me.getName());
    score.setText(me.getScore() + "pts");
    caught.setText(me.getTransientIVORNs().size() + " transients captured");

    Button btnCancel = (Button) findViewById(R.id.btnCancel);
    btnCancel.setOnClickListener(new View.OnClickListener() {
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
    finish();
  }

}
