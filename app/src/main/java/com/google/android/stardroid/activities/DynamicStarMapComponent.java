package com.google.android.stardroid.activities;

import com.google.android.stardroid.ApplicationComponent;

import com.google.android.stardroid.activities.dialogs.MultipleSearchResultsDialogFragment;
import com.google.android.stardroid.activities.dialogs.NoSearchResultsDialogFragment;
import com.google.android.stardroid.inject.PerActivity;

import dagger.Component;

/**
 * Created by johntaylor on 3/29/16.
 */
@PerActivity
@Component(modules={DynamicStarMapModule.class}, dependencies={ApplicationComponent.class})
public interface DynamicStarMapComponent extends
    NoSearchResultsDialogFragment.ActivityComponent,
    MultipleSearchResultsDialogFragment.ActivityComponent{
  void inject(DynamicStarMapActivity activity);
}

