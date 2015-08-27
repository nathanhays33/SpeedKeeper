package com.nathanhaze.speedkeeperX;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;




public class Service  extends DetailTrip{

	/** Use your own parameters **/
	public static void postData(Trip trip, String units, Boolean location, String message) throws JSONException{  
        // Create a new HttpClient and Post Header
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("http://nathanhaze.com/speedUploads/post.php");
        JSONObject json = new JSONObject();
 
        try {
            // JSON data:	
            json.put("date", trip._date);
            if(location == true){
	            json.put("lat", trip._latitude);
	            json.put("log", trip._longitude);
            }
            else{
            	json.put("lat", "");
	            json.put("log", "");
            }
            json.put("message", message);
            json.put("speed", trip._maxspeed);
            json.put("distance", trip._distance);
            json.put("alt", trip._altitude);
            json.put("unit", units);
            
            JSONArray postjson=new JSONArray();
            postjson.put(json);
 
            // Post the data:
            httppost.setHeader("json",json.toString());
            httppost.getParams().setParameter("jsonpost",postjson);
 
            // Execute HTTP Post Request
            System.out.print(json);
            HttpResponse response = httpclient.execute(httppost);
 
            // for JSON:
            if(response != null)
            {
                InputStream is = response.getEntity().getContent();
 
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                StringBuilder sb = new StringBuilder();
 
                String line = null;
                try {
                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            
            /** Most below is related to multithreading **/
            pd.dismiss(); //Threading
		    handle.post(Success); //Threading 
		    
		    
        }catch (ClientProtocolException e) {
        	pd.dismiss();
		    handle.post(Success); // Threading
            // TODO Auto-generated catch block
        } catch (IOException e) {
        	pd.dismiss();
		    handle.post(Success); //Threading 

            // TODO Auto-generated catch block
        }
    }
    
    /**Used for Multithreading **/
	final static Runnable Success = new Runnable() {
		   public void run() {
		   }
	     };
	     
}
