// Copyright 2008 Google Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.android.stardroid.activities;

import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

import com.google.android.stardroid.R;
import com.google.android.stardroid.StardroidApplication;
import com.google.android.stardroid.activities.util.ConstraintsChecker;
import com.google.android.stardroid.inject.HasComponent;
import com.google.android.stardroid.util.Analytics;
import com.google.android.stardroid.util.MiscUtil;

import javax.inject.Inject;

/**
 * Shows a splash screen, then launch the next activity.
 */
public class SplashScreenActivity extends InjectableActivity
    implements
    HasComponent<SplashScreenComponent> {
  private final static String TAG = MiscUtil.getTag(SplashScreenActivity.class);

  @Inject StardroidApplication app;
  @Inject Analytics analytics;
  @Inject SharedPreferences sharedPreferences;
  @Inject Animation fadeAnimation;
  @Inject FragmentManager fragmentManager;

  @Inject ConstraintsChecker cc;
  private View graphic;
  private SplashScreenComponent daggerComponent;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    Log.d(TAG, "onCreate");
    super.onCreate(savedInstanceState);
    setContentView(R.layout.splash);
    daggerComponent = DaggerSplashScreenComponent.builder()
        .applicationComponent(getApplicationComponent())
        .splashScreenModule(new SplashScreenModule(this)).build();
    daggerComponent.inject(this);

    graphic = findViewById(R.id.splash);

    fadeAnimation.setAnimationListener(new AnimationListener() {
      public void onAnimationEnd(Animation arg0) {
        Log.d(TAG, "onAnimationEnd");
        graphic.setVisibility(View.INVISIBLE);
        maybeShowWhatsNewAndEnd();
      }

      public void onAnimationRepeat(Animation arg0) {
      }

      public void onAnimationStart(Animation arg0) {
        Log.d(TAG, "SplashScreen.Animcation onAnimationStart");
      }
    });
  }

  @Override
  public void onResume() {
    super.onResume();

  }

  @Override
  public void onStart() {
    super.onStart();
    new CountDownTimer(2000, 1000) {
      @Override
      public void onTick(long millisUntilFinished) {
      }
      public void onFinish() {
        launchSkyMap();
      }
    }.start();




  }

  @Override
  public void onPause() {
    Log.d(TAG, "onPause");
    super.onPause();
  }

  @Override
  public void onDestroy() {
    Log.d(TAG, "onDestroy");
    super.onDestroy();
  }


  private void maybeShowWhatsNewAndEnd() {
      launchSkyMap();
  }

  // What's new dialog closed.

  private void launchSkyMap() {
    Intent intent = new Intent(SplashScreenActivity.this, DynamicStarMapActivity.class);
    cc.check();
    startActivity(intent);
    finish();
  }

  @Override
  public SplashScreenComponent getComponent() {
    return daggerComponent;
  }
}
