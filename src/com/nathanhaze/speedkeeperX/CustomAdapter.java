package com.nathanhaze.speedkeeperX;


import java.util.ArrayList;

import com.nathanhaze.speedkeeperX.R;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

//import com.nathanhaze.speedkeeper.R.layout;
//import com.nathanhaze.speedkeeper.R.menu;

 
public class CustomAdapter extends ArrayAdapter<Trip>{
    private ArrayList<Trip> entries;
    private Activity activity;
 
    public CustomAdapter(Activity a, int textViewResourceId, ArrayList<Trip> entries) {
        super(a, textViewResourceId, entries);
        this.entries = entries;
        this.activity = a;
    }
 
    public static class ViewHolder{
        public TextView item1;
        public TextView item2;
        public TextView item3;
        public TextView item4;
    }
 
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        ViewHolder holder;
        if (v == null) {
            LayoutInflater vi =
                (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.grid_item, null);
            holder = new ViewHolder();
            holder.item1 = (TextView) v.findViewById(R.id.date);
            holder.item2 = (TextView) v.findViewById(R.id.speed);
            holder.item3 = (TextView) v.findViewById(R.id.distance);
            holder.item4 = (TextView) v.findViewById(R.id.altitude);
            v.setTag(holder);
        }
        else
            holder=(ViewHolder)v.getTag();
 
        final Trip custom = entries.get(position);
        if (custom != null) {
        	holder.item1.setText(custom._date);
            holder.item2.setText(Float.toString(custom._maxspeed));
            holder.item3.setText(Float.toString(custom._distance));
            holder.item4.setText(Integer.toString(custom._altitude));        
        }
        return v;
    }
 
}