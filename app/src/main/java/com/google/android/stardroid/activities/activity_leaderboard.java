package com.google.android.stardroid.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.stardroid.R;
import com.google.android.stardroid.activities.leaderboard.Leader;
import com.google.android.stardroid.activities.leaderboard.LeaderboardTask;
import com.google.android.stardroid.activities.util.CustomUserAdapter;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by admin on 12/5/16.
 */




  public class activity_leaderboard extends Activity implements AdapterView.OnItemClickListener {

    private ArrayList<Leader> userObjects = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_leaderboard);


      // Set up your ActionBar
      ActionBar actionBar = getActionBar();
      actionBar.setDisplayShowHomeEnabled(false);
      actionBar.setDisplayShowCustomEnabled(true);
      actionBar.setTitle("Leaderboard");

      ListView leaderListView = (ListView) findViewById(R.id.leaderboard_list);


      try {
        userObjects.addAll(new LeaderboardTask().execute().get().getLeaders());
      } catch (InterruptedException e) {
        e.printStackTrace();
      } catch (ExecutionException e) {
        e.printStackTrace();
      }
      //populate leaders


      CustomUserAdapter customUserAdapter = new CustomUserAdapter(this, userObjects);
      leaderListView.setAdapter(customUserAdapter);
      leaderListView.setOnItemClickListener(this);


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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//      Intent transientDataIntent = new Intent(this, activity_transient_data.class);
//      transientDataIntent.putExtra("tran", transientObjects.get(position));
//      startActivity(transientDataIntent);
    }
  }



