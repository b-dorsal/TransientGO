package com.google.android.stardroid.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.stardroid.R;
import com.google.android.stardroid.transients.Transient;

/**
 * Created by admin on 11/30/16.
 */

public class activity_transient_data extends Activity{


  private Transient myTransient;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_transient_data);

    myTransient = (Transient) getIntent().getSerializableExtra("tran");

    // Set up your ActionBar
    ActionBar actionBar = getActionBar();
    actionBar.setDisplayShowHomeEnabled(false);
    actionBar.setDisplayShowCustomEnabled(true);


    TextView id = (TextView) findViewById(R.id.txtTranID);
    TextView mag = (TextView) findViewById(R.id.txtMagnitude);
    TextView ra = (TextView) findViewById(R.id.txtRa);
    TextView dec = (TextView) findViewById(R.id.txtDec);
    ImageView image = (ImageView) findViewById(R.id.imgImage);
    ImageView graph = (ImageView) findViewById(R.id.imgGraph);
    TextView ivorn = (TextView) findViewById(R.id.txtivorn);


    id.setText( " ID: " + myTransient.getId());
    mag.setText(" Magnitude:  " + myTransient.getMagnitude());
    ra.setText( " Right Ascension:  " + myTransient.getRightAscension());
    dec.setText(" Declination:  " + myTransient.getDeclination());
    ivorn.setText(" IVORN:" + myTransient.getIvorn());

    image.setImageBitmap(myTransient.getImageBMP());
    graph.setImageBitmap(myTransient.getLightCurveBMP());

    actionBar.setTitle("Transient Data");

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



