package com.google.android.stardroid.activities.leaderboard;

import android.os.AsyncTask;
import android.util.Base64;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
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

public class UserTask extends AsyncTask<String, Void, User> {
  protected User doInBackground(String... args) {
    String surl = "http://transient-go.herokuapp.com/v1/user/" + args[0];
    User user = null;
    try {
      URL url = new URL(surl);
      Gson gson = new Gson();
      HttpURLConnection con = (HttpURLConnection) url.openConnection();
      con.setRequestMethod("GET");

      String userCredentials = args[0]+":"+args[1];
      String basicAuth = "Basic " + new String(Base64.encode(userCredentials.getBytes(), Base64.DEFAULT));
      con.setRequestProperty("Authorization", basicAuth);

      user = gson.fromJson(new InputStreamReader(con.getInputStream()), User.class);
      user.setPass(args[1]);
      con.disconnect();

      //System.out.println(user.getName() + " : " + user.getScore() + " : " + user.getUserId());
    } catch (IOException e) {
      e.printStackTrace();
    }

    return user;
  }
}
