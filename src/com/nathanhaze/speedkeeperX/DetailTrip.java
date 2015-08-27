package com.nathanhaze.speedkeeperX;

import org.json.JSONException;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nathanhaze.speedkeeperX.R;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import android.support.v4.app.FragmentActivity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class DetailTrip extends FragmentActivity {

	int currentIndex;
	GoogleMap mMap;
	
	static DatabaseHandler db;

	Trip currentTrip;
	
	SharedPreferences sharedPrefs;
    public static final String PREFS_NAME = "MyPrefsFile";
    
	
	static ProgressDialog pd;
    static  Handler handle = new Handler();
        
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail_trip);
		
        db = new DatabaseHandler(this);      
     //   ArrayList<Trip> Trips = (ArrayList<Trip>) db.getAllTrips();
        
      	/***** Setup Map ****/	
      	mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
        setMap();
        
        Bundle b = getIntent().getExtras();  
        if(b != null){
      	  currentIndex = b.getInt("index");
      	  currentTrip = db.getTrip(currentIndex);
      	 // currentTrip = Trips.get(currentIndex);
      	  ((TextView)findViewById(R.id.date)).setText(currentTrip.getDate());
      	  ((TextView)findViewById(R.id.maxspeed)).setText("Max Speed: " + currentTrip.getMaxspeed());
      	  ((TextView)findViewById(R.id.avgspeed)).setText("Average Speed: " +currentTrip.get_averageSpeed());
      	  ((TextView)findViewById(R.id.alt)).setText("Altitude Change: " +  currentTrip.getAltitude());
          addMarkers();
        }
        else{ // There was a problem getting the index
        	  ((TextView)findViewById(R.id.maxspeed)).setText("There was a problem getting the trip");
        }
        
        try{
        	sharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(this);
        } catch (NullPointerException e) {
        	sharedPrefs = null;
        }
        
	}
	
	/*
	 * Share the data with an application
	 */
    public void share(){		
    	Intent shareIntent = new Intent();
    	shareIntent.setAction(Intent.ACTION_SEND);	    	
    	shareIntent.setType("text/plain");
        shareIntent.putExtra(android.content.Intent.EXTRA_TEXT,
        "My Trip:  "	+ 	"\n" +
        "Date: " + db.getTrip(currentIndex)._date + "\n" +
        "Altitude Change: "	+ db.getTrip(currentIndex)._altitude + "\n" +
        "Distance: " + db.getTrip(currentIndex)._distance + "\n" +
        "Max Speed: " + db.getTrip(currentIndex)._maxspeed +"\n" +
        "Location: " +
        "http://maps.google.com/maps?&z=10&q=" 
    	+ db.getTrip(currentIndex)._latitude + "," + db.getTrip(currentIndex)._longitude + "&ll=" + db.getTrip(currentIndex)._latitude  + "," 
    	+ db.getTrip(currentIndex)._longitude	
        		);
    	startActivity(Intent.createChooser(shareIntent, "Share Data"));
    }
    
    
    /*** Upload the data to site ***/
    
    /*
     * Upload the data
     */
    public void upload(final int index, final Boolean location, final String text){
        pd = ProgressDialog.show(this, "Uploading" , "");
	      Thread t = new Thread() {
	          public void run() {
			        try {	
			        	if(sharedPrefs.getBoolean("units", true)){
				            Service.postData(db.getTrip(index), "metric", location, text);
			        	}
			        	else{
				            Service.postData(db.getTrip(index), "us", location, text);
			        	}
			        } catch (JSONException e) {
			            // TODO Auto-generated catch block
			            e.printStackTrace();
			       }
	          }
	        };
	       t.start();
    }
    
    /*
     * Confirm that the user wants to upload the data
     */
	public void confirmUpload(){
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
   				this);
	   final EditText message = new EditText(this);
		message.setHint("Comment");
	    alertDialogBuilder.setTitle("Disclaimer:");
 
			// set dialog message
			alertDialogBuilder
				.setMessage("This selected data will be uploaded."
						+ "Nothing that will id you will be uploaded. You cannot reverse this. You can use the menu to visit the site.")
				.setCancelable(false)
				.setView(message)
				.setPositiveButton("Upload WITH location",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						upload(currentIndex, true, message.getText().toString());
					}
				  })
				  .setNeutralButton("Upload WITHOUT location", new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int id) {
						upload(currentIndex, false, message.getText().toString());
	               }   
	            })
				.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						// if this button is clicked, just close
						// the dialog box and do nothing
						dialog.cancel();
					}
				});
 
		AlertDialog alertDialog = alertDialogBuilder.create();
 
				// show it
		alertDialog.show();
	}
    
	
	  private void setMap() {
	    	int type = 0;
	    	/*
	    	if(sharedPrefs == null){
	    		sharedPrefs = PreferenceManager
		                .getDefaultSharedPreferences(this);
	    	}
	    	try{
	    	type = Integer.parseInt(sharedPrefs.getString("maptype", "0"));  //null
	        } catch (NullPointerException e) {
	        type = 0;
	        }
	    	*/
	    	 if (mMap != null) {
		    	 mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
	    		    if (mMap != null) {
	    		    	switch(type){
	    		    	case(0):
	    		    		mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
	    		    		break;
	    		    	case(1):
	    		    		mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
	    		    		break;
	    		    	case(2):
	    		    		mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
	    		    		break;
	    		    	case(3):
	    		    		mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
	    		    		break;
	    		    	}
	    		    	mMap.setMyLocationEnabled(true); // null
	    		    }
	    		}
	    }

	public void addMarkers(){
        mMap.addMarker(new MarkerOptions()
        .position(new LatLng(currentTrip.get_startLat() , currentTrip.get_startLog()))
        .title("Start Location")
     //   .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_green))
        );	
        
        mMap.addMarker(new MarkerOptions()
        .position(new LatLng(currentTrip.getLatitude(), currentTrip.getLongitude()))
        .title("End Location") // AM
       // .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_green))
        );	
        
        mMap.moveCamera( CameraUpdateFactory.newLatLngZoom(new LatLng(currentTrip.getLatitude(), currentTrip.getLongitude()) , 10) );		
	}
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.detail_trip, menu);
        return super.onCreateOptionsMenu(menu);
    }

	@Override
	    public boolean onOptionsItemSelected(MenuItem item) {
	    	switch (item.getItemId()) {
		    	case R.id.other:
		    		String url = "http://nathanhaze.com/speedUploads/userSpeeds.php";
		    		Intent i = new Intent(Intent.ACTION_VIEW);
		    		i.setData(Uri.parse(url));
		    		startActivity(i);
		        break;
		    	case R.id.upload:
		    		confirmUpload();
		        break;
		    	case R.id.share:
		    		share();
		        break;
	    	}
	       return(true);  
	    }
	
	
	public void deleteTrip(View v){
	   	  db.deleteTrip(currentIndex);
	   	Crouton.makeText(
	               this, 
	               "The trip was deleted", 
	               Style.INFO)
	            .show();
		  Intent intent = new Intent(this, ShowList.class);
          startActivity(intent);    
	}
	   
	/**Used for Multithreading **/
    final static Runnable Success = new Runnable() {
	    public void run() {
				   
	    }
   };
		     
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
}
