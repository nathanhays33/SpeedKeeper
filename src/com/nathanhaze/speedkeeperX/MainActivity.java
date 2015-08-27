package com.nathanhaze.speedkeeperX;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;

import com.google.analytics.tracking.android.EasyTracker;
import com.nathanhaze.speedkeeperX.R;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;


public class MainActivity extends Activity implements LocationListener, OnMenuItemClickListener {
    
	  private TextView AccuValue, speedvalue, distancevalue, maxspeedView, totalmax;
	 
	  static LocationManager locationManager;
	  static Location location;
	  static LocationListener locationListener;
	  static Boolean isGPSEnabled = false;
	  
	  static DatabaseHandler db;
	  
      private static double previous_log;
      private static double previous_lat;
      
      static float distance= 0;
      static int maxspeed = 0;
      private int  totalmaxvalue =0;
      static int speed = 0 ;
      private static double startAltitude;
      private static double altitude;
      
      public static List<Trip> Trips;
      static boolean recording;
      
      /* Preference */
      private static final int RESULT_SETTINGS = 1;
  	  SharedPreferences sharedPrefs;
      public static final String PREFS_NAME = "MyPrefsFile";
      static boolean metric = false;
      
      /*Average*/
      static long totalspeed;
      static int dataPoints;

      /*Initial Location*/
      private static double start_log;
      private static double start_lat;
      
      
      
      /*
       * (non-Javadoc)
       * @see android.app.Activity#onCreate(android.os.Bundle)
       */
    @Override
    public void onCreate(Bundle temp) {
        super.onCreate(temp);
        setContentView(R.layout.main);
      
        if (temp != null){
            maxspeed = temp.getInt("maxspeed");
            distance = temp.getFloat("distance");
         }
        
        speedvalue = (TextView)findViewById(R.id.speedvalue);
        distancevalue = (TextView)findViewById(R.id.distancevalue);
        maxspeedView = (TextView)findViewById(R.id.maxspeedvalue);
        totalmax =  (TextView)findViewById(R.id.totalmaxvalue);
        AccuValue = (TextView)findViewById(R.id.accuValue);

        db = new DatabaseHandler(this);      
        // Reading all Trips
        Trips = db.getAllTrips();       
 
        for (Trip cn : Trips) {
        	if(totalmaxvalue < cn._maxspeed){
        		totalmaxvalue =  cn._maxspeed;
        	}
        }
       // float test = Math.round(totalmaxvalue *100.0f)/100.0f;
        totalmax.setText(Float.toString(Math.round(totalmaxvalue *100.0f)/100.0f));

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
     // Register the listener with the Location Manager to receive location updates
        
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        isGPSEnabled = locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER);
        
        location = getLastKnownLocation();
	    // getting GPS status

        if(!isGPSEnabled){
        	showSettingsAlert();
        }
        if(location != null && isGPSEnabled){
	  	    AccuValue.setText(Integer.toString((int)location.getAccuracy() ));
	  	    previous_lat = location.getLatitude();
	        previous_log = location.getLongitude();   
        }
        
        try{
        	sharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(this);
        } catch (NullPointerException e) {
        	sharedPrefs = null;
        }
        
        maxspeed =0;
    }


       /*
       // US Units
        mphConversion = 2.23694f;
        distamceConversion =  0.000621371f;
        altitudeConversion =  3.28084f;        
        // Metric
        mphConversion = 3.6f;
        distamceConversion =  0.001f;
        altitudeConversion =  1f;
        */
        
	@Override
	  public void onLocationChanged(Location location) {
	      int accuracy = (int) location.getAccuracy();
	      AccuValue.setText(Integer.toString(accuracy));
	      if(accuracy < 20){
	          findViewById(R.id.button).setVisibility(View.VISIBLE);
	    	  double lat = (double) (location.getLatitude());
		      double lng = (double) (location.getLongitude());
		      
		      if(metric){
		         speed = (int) (3.6f * location.getSpeed()); // METERS
		      }
		      else{
		          speed = (int) (2.23694f * location.getSpeed()); //US
		      }    
			  speedvalue.setText(Integer.toString(speed));
		      float[] results = new float[4];
		      if(previous_lat !=0){
		    	  location.distanceBetween(lat, lng, previous_lat, previous_log, results);
		      }
		      
		      TextView alt = (TextView)findViewById(R.id.altValue);
		      
		      altitude = round(location.getAltitude());
		      
              if(metric){
                  alt.setText(Double.toString(altitude) + " meters"); //METERS
              }
              else{
                  alt.setText(Double.toString(altitude) + " feet");  //US
              }
              
              /*Average*/
              totalspeed += speed;
              dataPoints ++;
              
              if(accuracy < 13){
				
				  if(metric){
			     	  distance += results[0] * 0.001f;	//METERS
				  }	  
				  else{
					  distance += results[0] * 0.000621371f; //US
				  }
				  
				  distance = Math.round(distance * 100.00)/100.00f;	
				  
				  if(metric){
					  distancevalue.setText(distance + " km"); //METERS
				  }
				  else{
					  distancevalue.setText(distance + " miles"); //US
				  }
              }
			  
		      if(maxspeed < speed){
			          maxspeed = speed;	 
			    	  maxspeedView.setText(Integer.toString(maxspeed));
			      if(maxspeed > totalmaxvalue){
			    	  totalmaxvalue = maxspeed;
			    	  totalmax.setText(Integer.toString(totalmaxvalue));
			      }
		      }
		      previous_lat = lat;
		      previous_log = lng;
			  ((LinearLayout)findViewById(R.id.accuracyLayout)).setBackgroundResource(R.color.light_blue);
	      }
	      
	      else{
	    	  if(!recording){
		          findViewById(R.id.button).setVisibility(View.INVISIBLE);
	    	  }
			  distancevalue.setText("");
			  speedvalue.setText("");
	    	 // accuLayout.setBackgroundResource(R.color.red);
			  ((LinearLayout)findViewById(R.id.accuracyLayout)).setBackgroundResource(R.color.red);
	      }
	    }

    @Override
    public void onProviderDisabled(String provider) {
      Toast.makeText(this, "GPS was Disabled",
          Toast.LENGTH_SHORT).show();
    }

    public void action(View v){
    	if(recording){
    		stopTrip();
    	}
    	else{
    		startTrip();
       	}
    }
    public void startTrip (){
        ((Button)findViewById(R.id.button)).setText("Stop");
    	if(recording){
    		Crouton.makeText(
	  	               this, 
	  	              "Already Recording", 
	  	               Style.ALERT)
	  	            .show();
    	}
    	else{
    		Crouton.makeText(
	  	               this, 
	  	               "Recording Trip", 
	  	               Style.INFO)
	  	            .show();
    	recording = true;
    	previous_lat  = 0;
        previous_log = 0;
        maxspeed = 0;
        distance = 0;
        startAltitude = altitude;
        distancevalue.setText("0");
        maxspeedView.setText("0");
        
        /*Average*/
        totalspeed =0;
        dataPoints =0;
        
        start_log = location.getLongitude();
        start_lat = location.getLatitude();
    	}
        
    }
    
    public void stopTrip (){
            ((Button)findViewById(R.id.button)).setText("Start");
            
    		int altitudeDifference = (int) (altitude - startAltitude);
    		
    		int averageSpeed = 000;
    		if(dataPoints != 0){
    		   averageSpeed = (int)totalspeed/dataPoints;
    		}
    		
		    db.addTrip(new Trip(distance, maxspeed, getTimeStamp(), altitudeDifference, previous_lat, previous_log,
		    		start_lat, start_log, averageSpeed));
		    Crouton.makeText(
	  	               this, 
	  	               "The Trip was inserted in the database", 
	  	               Style.INFO)
	  	            .show();
		    recording = false;
    }

    private double round(Double num){
    	if(metric){
    	   return Math.round(num * 100.0) / 100.0; // METERS
    	}
    	else{
        	return Math.round(3.28084f * num * 100.0) / 100.0; //US
    	}
    }

	public String getTimeStamp() {
	     String timeStamp= new SimpleDateFormat("MM-dd' 'HH:mm").format(Calendar.getInstance().getTime());
		 return timeStamp;	
	}
	
	@Override
	public void onProviderEnabled(String provider) {
	    Toast.makeText(this, "Enabled new provider " + provider,
	            Toast.LENGTH_SHORT).show();
	        previous_lat = (float) (location.getLatitude());
	        previous_log = (float) (location.getLongitude());
	}


	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}
	
	 public void showSettingsAlert(){
	        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
	      
	        // Setting Dialog Title
	        alertDialog.setTitle("GPS is settings");
	  
	        // Setting Dialog Message
	        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");
	  
	        // Setting Icon to Dialog
	        //alertDialog.setIcon(R.drawable.delete);
	  
	        // On pressing Settings button
	        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog,int which) {
	                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
	                startActivity(intent);
	            }
	        });
	  
	        // on pressing cancel button
	        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int which) {
	            dialog.cancel();
	            }
	        });
	  
	        // Showing Alert Message
	        alertDialog.show();
	    }
 /*
	 public static List<Trip> getTrips(){
		 Trips = db.getAllTrips();
		 return Trips;
	 }
	 */
	 
		protected void onPause(){
			super.onPause();
		}
		protected void onStop() {
		    super.onStop();  
		    EasyTracker.getInstance(this).activityStop(this);  // Add this method.
		}
		
	    @Override
	    public void onStart() {
	      super.onStart();
	      EasyTracker.getInstance(this).activityStart(this);  // Add this method.
	      boolean temp = metric;
	      try{	
		     metric = sharedPrefs.getBoolean("units", false);
		   } catch (NullPointerException e) {
		     metric = false;	
		  }       
  
	      if(temp != metric){
	    	  maxspeed = 0; 
	      }

	      if(metric){
	          ((TextView)findViewById(R.id.speedlabel)).setText("Speed(Kph");
	          ((TextView)findViewById(R.id.distancelabel)).setText("Distance(Km) ");
	      }
	      else{
	          ((TextView)findViewById(R.id.speedlabel)).setText("Speed(Mph)");
	          ((TextView)findViewById(R.id.distancelabel)).setText("Distance(miles) ");
	      }
	      
	    }
	    
		
		private Location getLastKnownLocation() {
		    List<String> providers = locationManager.getProviders(true);
		    Location bestLocation = null;
		    for (String provider : providers) {
		        Location l = locationManager.getLastKnownLocation(provider);
		        if (l == null) {
		            continue;
		        }
		        if (bestLocation == null
		                || l.getAccuracy() < bestLocation.getAccuracy()) {
		            bestLocation = l;
		        }
		    }
		    if (bestLocation == null) {
		    	if(isGPSEnabled){
		    		bestLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		    	}
		    	else{
		    	   bestLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		    	}
		   
		    }
		    return bestLocation;
		}
		
		
		 protected void onSaveInstanceState(Bundle temp) {
		      temp.putFloat("distance", distance);
		      temp.putInt("maxspeed", maxspeed);
		      super.onSaveInstanceState(temp);
		    }



		    public void onToggleClicked(View view) {
		        // Is the toggle on?
		        boolean on = ((ToggleButton) view).isChecked();
		        LinearLayout background  = (LinearLayout) findViewById(R.id.holder);
		        if (on) {
		            background.setBackgroundResource(R.color.white);
		        } else {
		            background.setBackgroundResource(R.color.yellow);
		        }
		    }


			   public void menu(View v){
				    	PopupMenu popup = new PopupMenu(this, v);
				        MenuInflater inflater = popup.getMenuInflater();
				        inflater.inflate(R.menu.list, popup.getMenu());
				        popup.setOnMenuItemClickListener(this);
				        popup.show();
			    }
				
		   
				
				@Override
				public boolean onMenuItemClick(MenuItem item) {
			    	switch (item.getItemId()) {
			    	case R.id.listview:
			    	    Intent list_intent = new Intent(this, ShowList.class);
			    	    list_intent.putExtra("metric", metric);
			            startActivity(list_intent);
			        break;
			    	case R.id.uploads:
			    		String url = "http://nathanhaze.com/speedUploads/userSpeeds.php";
			    		Intent i = new Intent(Intent.ACTION_VIEW);
			    		i.setData(Uri.parse(url));
			    		startActivity(i);
			        break;
			        case R.id.pref:
			            Intent pref = new Intent(this, Preference.class);
			            startActivityForResult(pref, RESULT_SETTINGS);
		            break;      
		    	}
		                return(true);  
				}
				
				
				   @Override
				    public boolean onOptionsItemSelected(MenuItem item) {
				    	switch (item.getItemId()) {
					    	case R.id.results:
					    	    Intent list_intent = new Intent(this, ShowList.class);
					    	    list_intent.putExtra("metric", metric);
					            startActivity(list_intent);
					        break;
					    	case R.id.other:
					    		String url = "http://nathanhaze.com/speedUploads/userSpeeds.php";
					    		Intent i = new Intent(Intent.ACTION_VIEW);
					    		i.setData(Uri.parse(url));
					    		startActivity(i);
					        break;
					        case R.id.setting:
					            Intent pref = new Intent(this, Preference.class);
					            startActivityForResult(pref, RESULT_SETTINGS);
				            break;
				    	}
				                return(true);  
				    }
					
			    @Override
			    public boolean onCreateOptionsMenu(Menu menu) {
			        // Inflate the menu items for use in the action bar
			        MenuInflater inflater = getMenuInflater();
			        inflater.inflate(R.menu.main, menu);
			        return super.onCreateOptionsMenu(menu);
			    }
		    
}