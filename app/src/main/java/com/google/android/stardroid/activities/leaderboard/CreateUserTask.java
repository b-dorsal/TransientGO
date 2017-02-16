package com.google.android.stardroid.activities.leaderboard;

import android.os.AsyncTask;
import android.util.Base64;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class CreateUserTask extends AsyncTask<String, Void, User> {
  protected User doInBackground(String... args) {
    //http://transient-go.herokuapp.com/v1/user?id=brian&pass=brPass123&name=Brian%20Dorsey
    String signupURL =
        "http://transient-go.herokuapp.com/v1/user?id=" + args[0] + "&pass=" + args[1] + "&name=" +args[2];

    User user = null;
    try {
      URL url = new URL(signupURL);
      HttpURLConnection userCon = (HttpURLConnection) url.openConnection();
      userCon.setRequestMethod("POST");


      if(userCon.getResponseCode() == 200){
        String surl = "http://transient-go.herokuapp.com/v1/user/" + args[0];

        try {
          url = new URL(surl);
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
          return null;
        }
      }
      System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ code: " + userCon.getResponseCode());
      userCon.disconnect();
    } catch (IOException e) {
      e.printStackTrace();
    }


    return user;
  }
}
