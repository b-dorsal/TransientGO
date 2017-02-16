package com.google.android.stardroid.activities;

import android.app.Activity;
import android.app.FragmentManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.google.android.stardroid.R;
import com.google.android.stardroid.inject.PerActivity;

import dagger.Module;
import dagger.Provides;

/**
 * Created by johntaylor on 4/2/16.
 */
@Module
public class SplashScreenModule {
  private SplashScreenActivity activity;

  public SplashScreenModule(SplashScreenActivity activity) {
    this.activity = activity;
  }


  @Provides
  @PerActivity
  Activity provideActivity() {
    return activity;
  }

  @Provides
  @PerActivity
  Animation provideFadeoutAnimation() {
    return AnimationUtils.loadAnimation(activity, R.anim.fadeout);
  }

  @Provides
  @PerActivity
  FragmentManager provideFragmentManager() {
    return activity.getFragmentManager();
  }
}
