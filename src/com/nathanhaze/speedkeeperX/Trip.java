package com.nathanhaze.speedkeeperX;

public class Trip {
	
	//private variables
	int _id;
	public float _distance;
	public int _maxspeed;
	public String _date;
	public int _altitude;
	public double _latitude;
	public double _longitude;
	
	/*Start of the trip */
	public double _startLat;
	public double _startLog;
	
	public int _averageSpeed;
	
	// Empty constructor
	public Trip(){
		
	}
	
	public Trip(int id, float distance, int maxspeed, String date, int altitude, 
			double latitude, double longitude, double startlat, double startlog, int avgspeed){
		this._id = id;
		this._distance = distance;
		this._maxspeed = maxspeed;
		this._date = date;
		this._altitude = altitude;
		this._latitude = latitude;
		this._longitude = longitude;
		this._startLat = startlat;
		this._startLog = startlog;
		this._averageSpeed = avgspeed;
	}
	
	public Trip(float distance, int maxspeed, String date, int altitude, 
			double latitude, double longitude, double startlat, double startlog, int avgspeed){
		this._distance = distance;
		this._maxspeed = maxspeed;
		this._date = date;
		this._altitude = altitude;
		this._latitude = latitude;
		this._longitude = longitude;
		this._startLat = startlat;
		this._startLog = startlog;
		this._averageSpeed = avgspeed;
	}
	
	
	// constructor
	public Trip(int id, float distance, int maxspeed, String date, int altitude, 
			double latitude, double longitude){
		this._id = id;
		this._distance = distance;
		this._maxspeed = maxspeed;
		this._date = date;
		this._altitude = altitude;
		this._latitude = latitude;
		this._longitude = longitude;
	}
	
	// constructor
	public Trip(float distance, int maxspeed, String date, int altitude, 
			double latitude, double longitude){
		this._distance = distance;
		this._maxspeed = maxspeed;
		this._date = date;
		this._altitude = altitude;
		this._latitude = latitude;
		this._longitude = longitude;
	}
	
	
	// constructor
	public Trip(int id, float distance, int maxspeed, String date, int altitude){
		this._id = id;
		this._distance = distance;
		this._maxspeed = maxspeed;
		this._date = date;
		this._altitude = altitude;
	}
	
	// constructor
	public Trip(float distance, int maxspeed, String date, int altitude){
		this._distance = distance;
		this._maxspeed = maxspeed;
		this._date = date;
		this._altitude = altitude;
	}
	
	// constructor
	public Trip(int id, float distance, int maxspeed, String date){
		this._id = id;
		this._distance = distance;
		this._maxspeed = maxspeed;
		this._date = date;
	}
	
	// constructor
	public Trip(float distance, int maxspeed, String date){
		this._distance = distance;
		this._maxspeed = maxspeed;
		this._date = date;
	}
	// getting ID
	public int getID(){
		return this._id;
	}
	
	public String getDate(){
		return _date;
	}
	
	public void setDate(String date){
		this._date = date;
	}
	
	// setting id
	public void setID(int id){
		this._id = id;
	}
	
	public float getDistance(){
		return this._distance;
	}
	
	public void setDistance(float distance){
		this._distance = distance;
	}
	
	public int getMaxspeed(){
		return this._maxspeed;
	}
	
	public void setMaxspeed(int maxspeed){
		this._maxspeed = maxspeed;
	}
	
	public double getLatitude(){
		return  _latitude;
	}
	
	public void setLatitude (double latitude){
		this._latitude= latitude;
	}
	
	public double getLongitude(){
		return  _longitude;
	}
	
	public void setLongitude (double longitude){
		this._longitude= longitude;
	}
	
	public int getAltitude (){
		return  _altitude;
	}
	
	public void setAltitude (int altitude){
		this._altitude= altitude;
	}
	
	public double get_startLat() {
		return _startLat;
	}
	public void set_startLat(double _startLat) {
		this._startLat = _startLat;
	}
	public double get_startLog() {
		return _startLog;
	}
	public void set_startLog(double _startLog) {
		this._startLog = _startLog;
	}
	public int get_averageSpeed() {
		return _averageSpeed;
	}
	public void set_averageSpeed(int _averageSpeed) {
		this._averageSpeed = _averageSpeed;
	}
}
