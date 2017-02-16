package com.google.android.stardroid.activities.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.stardroid.R;
import com.google.android.stardroid.transients.Transient;

import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private ArrayList<Transient> objects;

    private class ViewHolder {
        TextView txtname;
        TextView txtradec;
        TextView txtmag;
        TextView txtdate;
        ImageView imgImage;
    }

    public CustomAdapter(Context context, ArrayList<Transient> objects) {
        inflater = LayoutInflater.from(context);
        this.objects = objects;
    }

    public int getCount() {
        return objects.size();
    }

    public Transient getItem(int position) {
        return objects.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.historyrow, null);
            holder.txtmag = (TextView) convertView.findViewById(R.id.txtMagnitude);
            holder.txtname = (TextView) convertView.findViewById(R.id.txtTranID);
            holder.txtradec = (TextView) convertView.findViewById(R.id.txtRADEC);
            holder.txtdate = (TextView) convertView.findViewById(R.id.txtTimeDate);
            holder.imgImage = (ImageView) convertView.findViewById(R.id.imgImage);


            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.txtmag.setText("Mag: " + objects.get(position).getMagnitude());
        holder.txtname.setText("ID: " + objects.get(position).getId());
        holder.txtradec.setText("RA: " + objects.get(position).getRightAscension()+ "  Dec:" + objects.get(position).getDeclination());
        holder.txtdate.setText("Date: " + objects.get(position).getDateTime());

        holder.imgImage.setImageBitmap((objects.get(position).getImageBMP()));

        return convertView;
    }
}
