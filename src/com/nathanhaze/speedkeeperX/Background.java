package com.nathanhaze.speedkeeperX;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;

public class Background  extends AsyncTask implements LocationListener{
    private NotificationHelper mNotificationHelper;
    public Background(Context context){
        mNotificationHelper = new NotificationHelper(context);
    }
 
    protected void onPreExecute(){
        //Create the notification in the statusbar
        mNotificationHelper.createNotification();
    }
 
    protected void onProgressUpdate(Integer... progress) {
        //This method runs on the UI thread, it receives progress updates
        //from the background thread and publishes them to the status bar
        mNotificationHelper.progressUpdate(accuracy);
    }
    protected void onPostExecute(Void result)    {
        //The task is complete, tell the status bar about it
        mNotificationHelper.completed();
    }

	@Override
	protected Object doInBackground(Object... params) {
        //This is where we would do the actual download stuff
        //for now I'm just going to loop for 10 seconds
        // publishing progress every second
        for (int i=10;i<=100;i += 10)
            {
                try {
                    Thread.sleep(1000);
                    publishProgress(accuracy);
 
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        return null;
	}

	MainActivity test = new MainActivity();
	float speedvalue;
	float distance;
	float previous_lat;
	float previous_log;
	float maxspeed;
	float accuracy; 
	
	@Override
	public void onLocationChanged(Location location) {
		 float lat = (float) (location.getLatitude());
	     float lng = (float) (location.getLongitude());
         float speed = 2.23694f * location.getSpeed();
         float[] results = new float[4];
         speed = (float) Math.round(speed * 100.0f)/100.0f;   	 
	      accuracy = (int) location.getAccuracy();
	      if(accuracy < 20){
		      location.distanceBetween(lat, lng, previous_lat, previous_log, results );
		      distance += results[0] * 0.000621371f;
		      distance = Math.round(distance/100.0f)/100.f;	
		  //    test.setDistance(distance);
		      if(maxspeed < speed){
			      maxspeed = speed;	 
			  //    test.setMaxSpeed(maxspeed);
		      }
		      previous_lat = lat;
		      previous_log = lng;
	      }
		
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

}