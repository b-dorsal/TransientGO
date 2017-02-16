package com.google.android.stardroid.activities.leaderboard;

import android.os.AsyncTask;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/*
 * Copyright 2016 Shakhar Dasgupta <sdasgupt@oswego.edu>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/**
 * @author Shakhar Dasgupta <sdasgupt@oswego.edu>
 */
public class LeaderboardTask extends AsyncTask<Void, Void, Leaderboard> {
  private String surl = "http://transient-go.herokuapp.com/v1/leaderboard";
  protected Leaderboard doInBackground(Void... x) {
    Leaderboard lboard = null;
    try {
      URL url = new URL(surl);
      Gson gson = new Gson();
      HttpURLConnection con = (HttpURLConnection) url.openConnection();
      con.setRequestMethod("GET");
      lboard = gson.fromJson(new InputStreamReader(con.getInputStream()), Leaderboard.class);
      con.disconnect();

    } catch (MalformedURLException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return lboard;
  }
}
