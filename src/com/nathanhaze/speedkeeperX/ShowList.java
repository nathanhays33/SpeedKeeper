package com.nathanhaze.speedkeeperX;

import java.util.ArrayList;

import com.google.analytics.tracking.android.EasyTracker;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.google.analytics.tracking.android.EasyTracker;
import com.nathanhaze.speedkeeperX.R;



public class ShowList extends Activity {

	private CustomAdapter adapter;
	
	
	static ProgressDialog pd;
    static  Handler handle = new Handler();
		
	DatabaseHandler dh; 
	ListView lv;
	ArrayList<Trip> Trips;
	
    static boolean metric = false;
	
    
    public static EditText message;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		 super.onCreate(savedInstanceState);
         setContentView(R.layout.list);
         
         dh = new DatabaseHandler(this);  
         Trips = (ArrayList<Trip>) dh.getAllTrips();
         
         lv = (ListView) findViewById(R.id.listview);
         adapter = new CustomAdapter(this,
                 R.id.listview,
                 Trips);
         lv.setAdapter(adapter);
         
          
          if (Build.VERSION.SDK_INT >= 9) {
              setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
          }
          
          lv.setClickable(true);
          lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {  
				
				viewDetails(Trips.get(arg2).getID());
			}
          });   
          
          Bundle b = getIntent().getExtras();  
          if(b != null){
        	  metric = b.getBoolean("metric");
          }
	}

	public void viewDetails(int i){
	    Intent list_intent = new Intent(this, DetailTrip.class);
	    list_intent.putExtra("index", i);
        startActivity(list_intent);
	}
	
	public void showDialog(String title, String message){
        AlertDialog a = new AlertDialog.Builder(this).create();
        a.setTitle(title);
        a.setMessage(message);

        a.setButton("Ok", new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int which) {
            return;
          }
        });
        a.show();
	}
	
	

    

	    @Override
	    public boolean onCreateOptionsMenu(Menu menu) {
	        // Inflate the menu items for use in the action bar
	        MenuInflater inflater = getMenuInflater();
	      //  inflater.inflate(R.menu.detail_trip, menu);
	        return super.onCreateOptionsMenu(menu);
	    }
	    
		
	    @Override
	    public void onStart() {
	      super.onStart();
	      EasyTracker.getInstance(this).activityStart(this);  // Add this method.
	    }

	    @Override
	    public void onStop() {
	      super.onStop();
	      EasyTracker.getInstance(this).activityStop(this);  // Add this method.
	    }
	    
	 // 2.0 and above
	    @Override
	    public void onBackPressed() {
	    	Intent intent = new Intent(this, MainActivity.class);
	        startActivity(intent);
	    }

	    // Before 2.0
	    @Override
	    public boolean onKeyDown(int keyCode, KeyEvent event) {
	        if (keyCode == KeyEvent.KEYCODE_BACK) {
		    	Intent intent = new Intent(this, MainActivity.class);
	            startActivity(intent);
	            return true;
	        }
	        return super.onKeyDown(keyCode, event);
	    }
}
