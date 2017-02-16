package com.google.android.stardroid.activities.leaderboard;
/*
 * Copyright (C) 2016 Shakhar Dasgupta <sdasgupt@oswego.edu>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

    import java.io.Serializable;
    import java.util.ArrayList;

/**
 *
 * @author Shakhar Dasgupta <sdasgupt@oswego.edu>
 */
public class User implements Serializable{

  private final String userId;
  private final String name;
  private final int score;
  private String password;
  private final ArrayList<String> transientIVORNs;

  public User(String userId, String name, int score, ArrayList<String> transientIVORNs) {
    this.userId = userId;
    this.name = name;
    this.score = score;
    this.transientIVORNs = transientIVORNs;
  }

  public void setPass(String pass){
    this.password = pass;
  }

  public String getPass(){
    return this.password;
  }
  public String getUserId() {
    return userId;
  }

  public String getName() {
    return name;
  }

  public int getScore() {
    return score;
  }

  public ArrayList<String> getTransientIVORNs() {
    return transientIVORNs;
  }

}
