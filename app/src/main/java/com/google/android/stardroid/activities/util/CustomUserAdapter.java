package com.google.android.stardroid.activities.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.android.stardroid.R;
import com.google.android.stardroid.activities.leaderboard.Leader;

import java.util.ArrayList;

//Brian Dorsey 2016
public class CustomUserAdapter extends BaseAdapter {

  private LayoutInflater inflater;
  private ArrayList<Leader> leaders;

  private class ViewHolder {
    TextView txtname;
    TextView txtScore;
  }

  public CustomUserAdapter(Context context, ArrayList<Leader> leaders) {
    inflater = LayoutInflater.from(context);
    this.leaders = leaders;
  }

  public int getCount() {
    return leaders.size();
  }

  public Leader getItem(int position) {
    return leaders.get(position);
  }

  public long getItemId(int position) {
    return position;
  }

  public View getView(int position, View convertView, ViewGroup parent) {
    ViewHolder holder = null;
    if(convertView == null) {
      holder = new ViewHolder();
      convertView = inflater.inflate(R.layout.leaderboardrow, null);
      holder.txtname = (TextView) convertView.findViewById(R.id.txtName);
      holder.txtScore = (TextView) convertView.findViewById(R.id.txtmyScore);

      convertView.setTag(holder);
    } else {
      holder = (ViewHolder) convertView.getTag();
    }
    holder.txtname.setText(leaders.get(position).getName());
    String score = Integer.toString(leaders.get(position).getScore());
    holder.txtScore.setText(score);

    return convertView;
  }
}
