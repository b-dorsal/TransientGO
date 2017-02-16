package com.google.android.stardroid.activities.leaderboard;

/**
 * Created by admin on 12/5/16.
 */

import android.os.AsyncTask;
import android.util.Base64;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
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

public class PutUserTask extends AsyncTask<String, Void, Void> {
  protected Void doInBackground(String... args) {
    String transientURL = null;
    try {
      transientURL =
          "http://transient-go.herokuapp.com/v1/user/" + args[0] + "/transient/" + URLEncoder.encode(args[2], "UTF-8");
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }

    try {
      URL url = new URL(transientURL);
      HttpURLConnection tranCon = (HttpURLConnection) url.openConnection();
      tranCon.setRequestMethod("PUT");
      String userCredentials = args[0]+":"+args[1];
      String basicAuth = "Basic " + new String(Base64.encode(userCredentials.getBytes(), Base64.DEFAULT));
      tranCon.setRequestProperty("Authorization", basicAuth);
      System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ code: " + tranCon.getResponseCode() + " : " + transientURL);
    } catch (IOException e) {
      e.printStackTrace();
    }

    return null;
  }
}
