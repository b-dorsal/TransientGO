// Copyright 2010 Google Inc.
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

import android.app.ActionBar;
import android.app.FragmentManager;
import android.app.SearchManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.stardroid.ApplicationConstants;
import com.google.android.stardroid.R;
import com.google.android.stardroid.activities.dialogs.MultipleSearchResultsDialogFragment;
import com.google.android.stardroid.activities.dialogs.NoSearchResultsDialogFragment;
import com.google.android.stardroid.activities.leaderboard.PutUserTask;
import com.google.android.stardroid.activities.leaderboard.User;
import com.google.android.stardroid.activities.leaderboard.UserTask;
import com.google.android.stardroid.activities.util.GooglePlayServicesChecker;
import com.google.android.stardroid.control.AstronomerModel;
import com.google.android.stardroid.control.AstronomerModel.Pointing;
import com.google.android.stardroid.control.ControllerGroup;
import com.google.android.stardroid.control.MagneticDeclinationCalculatorSwitcher;
import com.google.android.stardroid.inject.HasComponent;
import com.google.android.stardroid.layers.LayerManager;
import com.google.android.stardroid.renderer.RendererController;
import com.google.android.stardroid.renderer.SkyRenderer;
import com.google.android.stardroid.renderer.util.AbstractUpdateClosure;
import com.google.android.stardroid.search.SearchResult;
import com.google.android.stardroid.touch.DragRotateZoomGestureDetector;
import com.google.android.stardroid.touch.MapMover;
import com.google.android.stardroid.transients.Transient;
import com.google.android.stardroid.transients.TransientPopulateTask;
import com.google.android.stardroid.units.GeocentricCoordinates;
import com.google.android.stardroid.units.RaDec;
import com.google.android.stardroid.units.Vector3;
import com.google.android.stardroid.util.Analytics;
import com.google.android.stardroid.util.Geometry;
import com.google.android.stardroid.util.MathUtil;
import com.google.android.stardroid.util.MiscUtil;
import com.google.android.stardroid.util.SensorAccuracyMonitor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;

/**
 * The main map-rendering Activity.
 */
public class DynamicStarMapActivity extends InjectableActivity
    implements OnSharedPreferenceChangeListener, HasComponent<DynamicStarMapComponent>,
    AdapterView.OnItemClickListener {
  /**
   * ATTENTION: This was auto-generated to implement the App Indexing API.
   * See https://g.co/AppIndexing/AndroidStudio for more information.
   */
  private GoogleApiClient client;

  /**
   * ATTENTION: This was auto-generated to implement the App Indexing API.
   * See https://g.co/AppIndexing/AndroidStudio for more information.
   */
  // private GoogleApiClient client;

  /**
   * ATTENTION: This was auto-generated to implement the App Indexing API.
   * See https://g.co/AppIndexing/AndroidStudio for more information.
   */
  //private GoogleApiClient client;
  @Override
  public DynamicStarMapComponent getComponent() {
    return daggerComponent;
  }

  /**
   * ATTENTION: This was auto-generated to implement the App Indexing API.
   * See https://g.co/AppIndexing/AndroidStudio for more information.
   */
  public Action getIndexApiAction() {
    Thing object = new Thing.Builder()
        .setName("DynamicStarMap Page") // TODO: Define a title for the content shown.
        // TODO: Make sure this auto-generated URL is correct.
        .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
        .build();
    return new Action.Builder(Action.TYPE_VIEW)
        .setObject(object)
        .setActionStatus(Action.STATUS_TYPE_COMPLETED)
        .build();
  }

  /**
   * ATTENTION: This was auto-generated to implement the App Indexing API.
   * See https://g.co/AppIndexing/AndroidStudio for more information.
   */
  public Action getIndexApiAction0() {
    Thing object = new Thing.Builder()
        .setName("DynamicStarMap Page") // TODO: Define a title for the content shown.
        // TODO: Make sure this auto-generated URL is correct.
        .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
        .build();
    return new Action.Builder(Action.TYPE_VIEW)
        .setObject(object)
        .setActionStatus(Action.STATUS_TYPE_COMPLETED)
        .build();
  }

  /**
   * ATTENTION: This was auto-generated to implement the App Indexing API.
   * See https://g.co/AppIndexing/AndroidStudio for more information.
   */
//  public Action getIndexApiAction() {
//    Thing object = new Thing.Builder()
//            .setName("DynamicStarMap Page") // TODO: Define a title for the content shown.
//            // TODO: Make sure this auto-generated URL is correct.
//            .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
//            .build();
//    return new Action.Builder(Action.TYPE_VIEW)
//            .setObject(object)
//            .setActionStatus(Action.STATUS_TYPE_COMPLETED)
//            .build();
//  }

  /**
   * Passed to the renderer to get per-frame updates from the model.
   *
   * @author John Taylor
   */
  private static final class RendererModelUpdateClosure extends AbstractUpdateClosure {
    private RendererController rendererController;
    private AstronomerModel model;

    public RendererModelUpdateClosure(AstronomerModel model,
                                      RendererController rendererController) {
      this.model = model;
      this.rendererController = rendererController;
    }

    @Override
    public void run() {
      Pointing pointing = model.getPointing();
      float directionX = pointing.getLineOfSightX();
      float directionY = pointing.getLineOfSightY();
      float directionZ = pointing.getLineOfSightZ();
      float upX = pointing.getPerpendicularX();
      float upY = pointing.getPerpendicularY();
      float upZ = pointing.getPerpendicularZ();
      rendererController.queueSetViewOrientation(directionX, directionY, directionZ, upX, upY, upZ);
      Vector3 up = model.getPhoneUpDirection();
      rendererController.queueTextAngle(MathUtil.atan2(up.x, up.y));
      rendererController.queueViewerUpDirection(model.getZenith().copy());
      float fieldOfView = model.getFieldOfView();
      rendererController.queueFieldOfView(fieldOfView);
    }
  }

  // Activity for result Ids
  public static final int GOOGLE_PLAY_SERVICES_REQUEST_CODE = 1;
  public static final int GOOGLE_PLAY_SERVICES_REQUEST_LOCATION_PERMISSION_CODE = 2;
  public static final int LOGIN_REQUEST_CODE = 3;
  // End Activity for result Ids
  private static final float ROTATION_SPEED = 10;
  private static final String TAG = MiscUtil.getTag(DynamicStarMapActivity.class);
  private ImageButton cancelSearchButton;
  @Inject ControllerGroup controller;
  private GestureDetector gestureDetector;
  @Inject AstronomerModel model;
  private RendererController rendererController;
  private boolean nightMode = false;
  private boolean searchMode = false;
  private GeocentricCoordinates searchTarget = GeocentricCoordinates.getInstance(0, 0);
  @Inject SharedPreferences sharedPreferences;
  private GLSurfaceView skyView;
  private String searchTargetName;
  @Inject LayerManager layerManager;
  // TODO(widdows): Figure out if we should break out the
  // time dialog and time player into separate activities.
  private View timePlayerUI;
  private DynamicStarMapComponent daggerComponent;
  @Inject
  @Named("timetravel")
  Provider<MediaPlayer> timeTravelNoiseProvider;
  @Inject
  @Named("timetravelback")
  Provider<MediaPlayer> timeTravelBackNoiseProvider;
  private MediaPlayer timeTravelNoise;
  private MediaPlayer timeTravelBackNoise;
  @Inject Handler handler;
  @Inject Analytics analytics;
  @Inject GooglePlayServicesChecker playServicesChecker;
  @Inject FragmentManager fragmentManager;
  @Inject NoSearchResultsDialogFragment noSearchResultsDialogFragment;
  @Inject MultipleSearchResultsDialogFragment multipleSearchResultsDialogFragment;
  @Inject SensorAccuracyMonitor sensorAccuracyMonitor;
  // A list of runnables to post on the handler when we resume.
  private List<Runnable> onResumeRunnables = new ArrayList<>();
  // We need to maintain references to these objects to keep them from
  // getting gc'd.
  @SuppressWarnings("unused")
  @Inject
  MagneticDeclinationCalculatorSwitcher magneticSwitcher;
  private DragRotateZoomGestureDetector dragZoomRotateDetector;
  @Inject Animation flashAnimation;
  private DrawerLayout drawerLayout;
  private Button btnCapture;

  private Transient CURRENTtransient;
  private final int TRANSIENTS_COUNT = 20;
  private SkyRenderer renderer;

  private User ME;
  private int LOGGED_IN = 1;

  private TextView username;

  @Override
  public void onCreate(Bundle icicle) {
    super.onCreate(icicle);

    daggerComponent = DaggerDynamicStarMapComponent.builder()
        .applicationComponent(getApplicationComponent())
        .dynamicStarMapModule(new DynamicStarMapModule(this)).build();
    daggerComponent.inject(this);

    getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
        WindowManager.LayoutParams.FLAG_FULLSCREEN);

    // Set up your ActionBar
    ActionBar actionBar = getActionBar();
    actionBar.setDisplayShowHomeEnabled(false);
    actionBar.setDisplayShowTitleEnabled(false);
    actionBar.setDisplayShowCustomEnabled(true);
    actionBar.setCustomView(R.layout.custom_action_bar);

    initializeModelViewController();
    checkForSensorsAndMaybeWarn();

    LinearLayout leftRL = (LinearLayout) findViewById(R.id.leftDrawer);
    drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
    ListView mainListView = (ListView) findViewById(R.id.drawer_list);
//
    String[] itemsArray = new String[]{"My Account", "History", "Leaderboard", "About"};
    ArrayList<String> drawerList = new ArrayList<String>();
    drawerList.addAll(Arrays.asList(itemsArray));
    ArrayAdapter listAdapter = new ArrayAdapter<>(this, R.layout.menurow, drawerList);
    mainListView.setAdapter(listAdapter);
    mainListView.setOnItemClickListener(this);


    this.username = (TextView) findViewById(R.id.lblAccountName);

    btnCapture = (Button) findViewById(R.id.btnCapture);

    if (ME == null || LOGGED_IN == 0) {
      Intent userLoginIntent = new Intent(this, activity_user_login.class);
      startActivityForResult(userLoginIntent, LOGGED_IN);
    } else {
      try {
        setNewTransient();
      } catch (ExecutionException e) {
        e.printStackTrace();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }

    btnCapture.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        //btnCapture.setBackground("");
        if (renderer.inCircle()) {
          try {
            captureTransient(view);
          } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
          }
        }
      }
    });

    // ATTENTION: This was auto-generated to implement the App Indexing API.
    // See https://g.co/AppIndexing/AndroidStudio for more information.
    client =
        new GoogleApiClient.Builder(this).addApi(AppIndex.API)
            .build();
  }

  private void forceRefresh() {
    daggerComponent = DaggerDynamicStarMapComponent.builder()
        .applicationComponent(getApplicationComponent())
        .dynamicStarMapModule(new DynamicStarMapModule(this)).build();
    daggerComponent.inject(this);

    getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
        WindowManager.LayoutParams.FLAG_FULLSCREEN);
  }


  private void setNewTransient() throws ExecutionException, InterruptedException {
    CURRENTtransient = null;

    ArrayList<Transient> next = new TransientPopulateTask().execute(10).get();


    if(ME == null) {

      CURRENTtransient = next.get(0);
    }else{
      ArrayList<String> seenlist = ME.getTransientIVORNs();
      for(Transient t : next){
        if(!seenlist.contains(t.getIvorn())){
          CURRENTtransient = t;
        }
      }
    }
    GeocentricCoordinates coords = Geometry
        .getXYZ(
            new RaDec(CURRENTtransient.getRightAscension(), CURRENTtransient.getDeclination()));
    activateSearchTarget(coords, "stuff");

  }


  private void captureTransient(View view) throws ExecutionException, InterruptedException {



    Toast.makeText(this, "Capturing", Toast.LENGTH_LONG).show();

    Intent transientDataIntent = new Intent(view.getContext(), activity_transient_data.class);
    transientDataIntent.putExtra("tran", CURRENTtransient);
    startActivity(transientDataIntent);


    PutUserTask netPutter = new PutUserTask();
    netPutter.execute(ME.getUserId(), ME.getPass(), CURRENTtransient.getIvorn()).get();

    rendererController.queueDisableSearchOverlay();
    forceRefresh();
    setNewTransient();
    ME = new UserTask().execute(ME.getUserId(),ME.getPass()).get();
  }


  @Override
  public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    if (position == 0) {

      if (ME == null) {
        System.out.println("\n\nUSER LOGIN\n\n");
        Intent userLoginIntent = new Intent(this, activity_user_login.class);
        startActivityForResult(userLoginIntent, LOGGED_IN);
        drawerLayout.closeDrawers();

        //ME WONT be null anymore
      } else {
        Intent UserDataIntent = new Intent(this, activity_userdata.class);
        UserDataIntent.putExtra("user", ME);
        startActivity(UserDataIntent);
        //DISPLAY USER ACCOUNT INFO
        //I will do the user info page at home. its basic.
      }


    } else if (position == 1) {
      Toast.makeText(this, "Loading History...", Toast.LENGTH_LONG).show();
      System.out.println("\n\nHISTORY\n\n");
      Intent historyIntent = new Intent(this, activity_history.class);
      historyIntent.putExtra("user", ME);
      startActivity(historyIntent);
      drawerLayout.closeDrawers();

    } else if (position == 2) {
      Toast.makeText(this, "Loading Leaderboard...", Toast.LENGTH_LONG).show();
      System.out.println("\n\nLeaderboard\n\n");
      Intent leaderboardIntent = new Intent(this, activity_leaderboard.class);
      startActivity(leaderboardIntent);
      drawerLayout.closeDrawers();

    }
  }


  private void checkForSensorsAndMaybeWarn() {
    SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
    if (sensorManager != null && sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null
        && sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD) != null) {
      Log.i(TAG, "Minimum sensors present");
      // We want to reset to auto mode on every restart, as users seem to get
      // stuck in manual mode and can't find their way out.
      // TODO(johntaylor): this is a bit of an abuse of the prefs system, but
      // the button we use is wired into the preferences system.  Should probably
      // change this to a use a different mechanism.
      sharedPreferences.edit().putBoolean(ApplicationConstants.AUTO_MODE_PREF_KEY, true).apply();
      setAutoMode(true);
      return;
    }
    // Missing at least one sensor.  Warn the user.

  }

  @Override
  protected void onPostCreate(Bundle savedInstanceState) {
    super.onPostCreate(savedInstanceState);

  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    super.onCreateOptionsMenu(menu);
//    MenuInflater inflater = getMenuInflater();
//    inflater.inflate(R.menu.main, menu);
    return true;
  }

  @Override
  public void onDestroy() {
    Log.d(TAG, "DynamicStarMap onDestroy");
    super.onDestroy();
  }

  @Override
  public boolean onKeyDown(int keyCode, KeyEvent event) {
    switch (keyCode) {
      case (KeyEvent.KEYCODE_DPAD_LEFT):
        Log.d(TAG, "Key left");
        controller.rotate(-10.0f);
        break;
      case (KeyEvent.KEYCODE_DPAD_RIGHT):
        Log.d(TAG, "Key right");
        controller.rotate(10.0f);
        break;
      case (KeyEvent.KEYCODE_BACK):
        // If we're in search mode when the user presses 'back' the natural
        // thing is to back out of search.
        Log.d(TAG, "In search mode " + searchMode);
        if (searchMode) {
          cancelSearch();
          break;
        }
      default:
        Log.d(TAG, "Key: " + event);
        return super.onKeyDown(keyCode, event);
    }
    return true;
  }

  @Override
  public void onStart() {
    super.onStart();// ATTENTION: This was auto-generated to implement the App Indexing API.
    // ATTENTION: This was auto-generated to implement the App Indexing API.
    // See https://g.co/AppIndexing/AndroidStudio for more information.
    client.connect();
    // ATTENTION: This was auto-generated to implement the App Indexing API.
    //forceRefresh();
    // ATTENTION: This was auto-generated to implement the App Indexing API.
    // See https://g.co/AppIndexing/AndroidStudio for more information.
    AppIndex.AppIndexApi.start(client, getIndexApiAction0());
  }

  @Override
  public void onStop() {
    super.onStop();// ATTENTION: This was auto-generated to implement the App Indexing API.
    // ATTENTION: This was auto-generated to implement the App Indexing API.
    // See https://g.co/AppIndexing/AndroidStudio for more information.
    AppIndex.AppIndexApi.end(client, getIndexApiAction0());
// See https://g.co/AppIndexing/AndroidStudio for more information.
//    AppIndex.AppIndexApi.end(client, getIndexApiAction());
//    // ATTENTION: This was auto-generated to implement the App Indexing API.
//    // See https://g.co/AppIndexing/AndroidStudio for more information.
//    client.disconnect();
    // ATTENTION: This was auto-generated to implement the App Indexing API.
    // See https://g.co/AppIndexing/AndroidStudio for more information.
    client.disconnect();
  }

  @Override
  public void onResume() {
    //forceRefresh();
    Log.d(TAG, "onResume at " + System.currentTimeMillis());
    super.onResume();
    Log.i(TAG, "Resuming");
    timeTravelNoise = timeTravelNoiseProvider.get();
    timeTravelBackNoise = timeTravelBackNoiseProvider.get();

    Log.i(TAG, "Starting view");
    skyView.onResume();
    Log.i(TAG, "Starting controller");
    controller.start();

    if (controller.isAutoMode()) {
      sensorAccuracyMonitor.start();
    }
    for (Runnable runnable : onResumeRunnables) {
      handler.post(runnable);
    }
    Log.d(TAG, "-onResume at " + System.currentTimeMillis());

  }

  @Override
  public void onPause() {
    Log.d(TAG, "DynamicStarMap onPause");
    super.onPause();
    sensorAccuracyMonitor.stop();
    if (timeTravelNoise != null) {
      timeTravelNoise.release();
      timeTravelNoise = null;
    }
    if (timeTravelBackNoise != null) {
      timeTravelBackNoise.release();
      timeTravelBackNoise = null;
    }
    for (Runnable runnable : onResumeRunnables) {
      handler.removeCallbacks(runnable);
    }
    //finish();
    controller.stop();
    skyView.onPause();
    // Debug.stopMethodTracing();
    Log.d(TAG, "DynamicStarMap -onPause");
  }

  @Override
  public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
    Log.d(TAG, "Preferences changed: key=" + key);
    switch (key) {
      case ApplicationConstants.AUTO_MODE_PREF_KEY:
        //boolean autoMode = sharedPreferences.getBoolean(key, true);
        boolean autoMode = false;
        Log.d(TAG, "Automode is set to " + autoMode);
        if (!autoMode) {
          Log.d(TAG, "Switching to manual control");
          Toast.makeText(DynamicStarMapActivity.this, R.string.set_manual, Toast.LENGTH_SHORT)
              .show();
        } else {
          Log.d(TAG, "Switching to sensor control");
          Toast.makeText(DynamicStarMapActivity.this, R.string.set_auto, Toast.LENGTH_SHORT).show();
        }
        setAutoMode(false);
        break;
      default:
        return;
    }
  }

  @Override
  public boolean onTouchEvent(MotionEvent event) {

    boolean eventAbsorbed = true;
    return eventAbsorbed;
  }

  private void doSearchWithIntent(Intent searchIntent) {
    // If we're already in search mode, cancel it.
    if (searchMode) {
      cancelSearch();
    }
    Log.d(TAG, "Performing Search");
    final String queryString = searchIntent.getStringExtra(SearchManager.QUERY);
    searchMode = true;
    Log.d(TAG, "Query string " + queryString);
    List<SearchResult> results = layerManager.searchByObjectName(queryString);
    // Log the search, with value "1" for successful searches
    analytics.trackEvent(
        Analytics.USER_ACTION_CATEGORY, Analytics.SEARCH, "search:" + queryString,
        results.size() > 0 ? 1 : 0);
    if (results.size() == 0) {
      Log.d(TAG, "No results returned");
      noSearchResultsDialogFragment.show(fragmentManager, "No Search Results");
    } else if (results.size() > 1) {
      Log.d(TAG, "Multiple results returned");
      showUserChooseResultDialog(results);
    } else {
      Log.d(TAG, "One result returned.");
      final SearchResult result = results.get(0);
      //activateSearchTarget(result.coords, result.capitalizedName);
    }
  }

  private void showUserChooseResultDialog(List<SearchResult> results) {
    multipleSearchResultsDialogFragment.clearResults();
    for (SearchResult result : results) {
      multipleSearchResultsDialogFragment.add(result);
    }
    multipleSearchResultsDialogFragment.show(fragmentManager, "Multiple Search Results");
  }

  private void initializeModelViewController() {
    Log.i(TAG, "Initializing Model, View and Controller @ " + System.currentTimeMillis());
    setContentView(R.layout.skyrenderer);
    skyView = (GLSurfaceView) findViewById(R.id.skyrenderer_view);
    // We don't want a depth buffer.
    skyView.setEGLConfigChooser(false);
    renderer = new SkyRenderer(getResources());
    skyView.setRenderer(renderer);
    rendererController = new RendererController(renderer, skyView);
    // The renderer will now call back every frame to get model updates.
    rendererController.addUpdateClosure(
        new RendererModelUpdateClosure(model, rendererController));
    Log.i(TAG, "Setting layers @ " + System.currentTimeMillis());
    layerManager.registerWithRenderer(rendererController);
    Log.i(TAG, "Set up controllers @ " + System.currentTimeMillis());
    controller.setModel(model);
    wireUpScreenControls(); // TODO(johntaylor) move these?
    wireUpTimePlayer();  // TODO(widdows) move these?
  }

  private void setAutoMode(boolean auto) {
    analytics.trackEvent(Analytics.USER_ACTION_CATEGORY,
        Analytics.MENU_ITEM, Analytics.TOGGLED_MANUAL_MODE_LABEL, auto ? 0 : 1);
//    controller.setAutoMode(auto);
    controller.setAutoMode(true);
    if (auto) {
      sensorAccuracyMonitor.start();
    } else {
      sensorAccuracyMonitor.stop();
    }
  }

  private void wireUpScreenControls() {

    MapMover mapMover = new MapMover(model, controller, this);

//    gestureDetector = new GestureDetector(this, new GestureInterpreter(
//        fullscreenControlsManager, mapMover));
    dragZoomRotateDetector = new DragRotateZoomGestureDetector(mapMover);
  }

  private void cancelSearch() {
//    View searchControlBar = findViewById(R.id.search_control_bar);
//    searchControlBar.setVisibility(View.INVISIBLE);
//    rendererController.queueDisableSearchOverlay();
//    searchMode = false;
  }

  @Override
  protected void onNewIntent(Intent intent) {
    super.onNewIntent(intent);
    Log.d(TAG, "New Intent received " + intent);
    if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
      doSearchWithIntent(intent);
    }
  }

  @Override
  protected void onRestoreInstanceState(Bundle icicle) {
    Log.d(TAG, "DynamicStarMap onRestoreInstanceState");
    super.onRestoreInstanceState(icicle);
    if (icicle == null) return;
    searchMode = icicle.getBoolean(ApplicationConstants.BUNDLE_SEARCH_MODE);
    float x = icicle.getFloat(ApplicationConstants.BUNDLE_X_TARGET);
    float y = icicle.getFloat(ApplicationConstants.BUNDLE_Y_TARGET);
    float z = icicle.getFloat(ApplicationConstants.BUNDLE_Z_TARGET);
    searchTarget = new GeocentricCoordinates(x, y, z);
    searchTargetName = icicle.getString(ApplicationConstants.BUNDLE_TARGET_NAME);
    if (searchMode) {
      Log.d(TAG, "Searching for target " + searchTargetName + " at target=" + searchTarget);
      rendererController.queueEnableSearchOverlay(searchTarget, searchTargetName);
      cancelSearchButton.setVisibility(View.VISIBLE);
    }
    nightMode = icicle.getBoolean(ApplicationConstants.BUNDLE_NIGHT_MODE, false);
  }

  @Override
  protected void onSaveInstanceState(Bundle icicle) {
    Log.d(TAG, "DynamicStarMap onSaveInstanceState");
    icicle.putBoolean(ApplicationConstants.BUNDLE_SEARCH_MODE, searchMode);
    icicle.putFloat(ApplicationConstants.BUNDLE_X_TARGET, searchTarget.x);
    icicle.putFloat(ApplicationConstants.BUNDLE_Y_TARGET, searchTarget.y);
    icicle.putFloat(ApplicationConstants.BUNDLE_Z_TARGET, searchTarget.z);
    icicle.putString(ApplicationConstants.BUNDLE_TARGET_NAME, searchTargetName);
    icicle.putBoolean(ApplicationConstants.BUNDLE_NIGHT_MODE, nightMode);
    super.onSaveInstanceState(icicle);
  }

  public void activateSearchTarget(GeocentricCoordinates target, final String searchTerm) {
    rendererController.queueViewerUpDirection(model.getZenith().copy());
    rendererController.queueEnableSearchOverlay(target.copy(), searchTerm);
  }

  private void wireUpTimePlayer() {
  }

  public AstronomerModel getModel() {
    return model;
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    this.LOGGED_IN = resultCode;
    ME = (User) data.getExtras().getSerializable("userobj");
    if (ME != null) {
      System.out.println("LOGIN RESULT: " + ME.getUserId());
      this.username.setText(ME.getUserId());
    }
    try {
      setNewTransient();
    } catch (ExecutionException e) {
      e.printStackTrace();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void onRequestPermissionsResult(int requestCode,
                                         String[] permissions,
                                         int[] grantResults) {
    if (requestCode == GOOGLE_PLAY_SERVICES_REQUEST_LOCATION_PERMISSION_CODE) {
      playServicesChecker.runAfterPermissionsCheck(requestCode, permissions, grantResults);
      return;
    }
    Log.w(TAG, "Unhandled request permissions result");
  }
}