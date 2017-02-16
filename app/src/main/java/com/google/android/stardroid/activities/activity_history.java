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
import com.google.android.stardroid.activities.leaderboard.User;
import com.google.android.stardroid.activities.util.CustomAdapter;
import com.google.android.stardroid.transients.SingleTransientTask;
import com.google.android.stardroid.transients.Transient;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class activity_history extends Activity implements AdapterView.OnItemClickListener {

    ArrayList<Transient> transientObjects = new ArrayList<>();

    private User me;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        me = (User) getIntent().getSerializableExtra("user");

        // Set up your ActionBar
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(R.layout.custom_action_bar);

        ListView historyListView = (ListView) findViewById( R.id.history_list );

        try {
            //System.out.println(me.getTransientIVORNs().get(0));
            for(String ivorn : me.getTransientIVORNs())
            transientObjects.add(new SingleTransientTask().execute(ivorn).get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        CustomAdapter customAdapter = new CustomAdapter(this, transientObjects);
        historyListView.setAdapter(customAdapter);
        historyListView.setOnItemClickListener(this);

        //TO-DO: Make this activity implement OnItemClickListener and implement methods to make each selection work.
        //historyListView.setOnItemClickListener(this);

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
        Intent transientDataIntent = new Intent(this, activity_transient_data.class);
        transientDataIntent.putExtra("tran", transientObjects.get(position));
        startActivity(transientDataIntent);
    }
}


