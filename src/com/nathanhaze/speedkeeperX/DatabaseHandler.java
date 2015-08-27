package com.nathanhaze.speedkeeperX;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {

	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 2;

	// Database Name
	private static final String DATABASE_NAME = "tripsManager";

	// Contacts table name
	private static final String TABLE_CONTACTS = "trips";

	// Contacts Table Columns names
	private static final String KEY_ID = "id";
	private static final String KEY_DISTANCE = "distance";
	private static final String KEY_MAXSPEED = "maxspeed";
	private static final String KEY_DATE = "date_number";
	private static final String ALTITUDE = "altitude";
	private static final String LATITUDE = "latitude";
	private static final String LONGITUDE = "longitude";
	private static final String AVGSPEED = "average_speed";
	private static final String START_LATITUDE = "start_latitude";
	private static final String START_LONGITUDE = "start_longitude";
	
	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CONTACTS + "("
				+ KEY_ID + " INTEGER PRIMARY KEY," + KEY_DISTANCE + " INTEGER,"
				+ KEY_MAXSPEED + " INTEGER," + KEY_DATE + " TEXT," + ALTITUDE + " INTEGER," + 
				LATITUDE + " DOUBLE," +  LONGITUDE + " DOUBLE," + 
				START_LATITUDE + " DOUBLE," +  START_LONGITUDE + " DOUBLE," + 
				AVGSPEED + " INTEGER" +		
				")";
		db.execSQL(CREATE_CONTACTS_TABLE);
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);

		// Create tables again
		onCreate(db);
	}

	/**
	 * All CRUD(Create, Read, Update, Delete) Operations
	 */

	// Adding new trip
	void addTrip(Trip trip) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_DISTANCE, trip.getDistance());
		values.put(KEY_MAXSPEED, trip.getMaxspeed()); 
		values.put(KEY_DATE, trip.getDate()); 
		values.put(ALTITUDE, trip.getAltitude()); 
		values.put(LATITUDE, trip.getLatitude()); 
		values.put(LONGITUDE, trip.getLongitude()); 
		
		values.put(START_LATITUDE, trip.get_startLat()); 
		values.put(START_LONGITUDE, trip.get_startLog()); 
		values.put(AVGSPEED, trip.get_averageSpeed()); 
		
		// Inserting Row
		db.insert(TABLE_CONTACTS, null, values);
		db.close(); // Closing database connection
	}

	// Getting single trip
	public Trip getTrip(int id) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_CONTACTS, new String[] { KEY_ID,
				KEY_DISTANCE, KEY_MAXSPEED, KEY_DATE, ALTITUDE, LATITUDE, 
				LONGITUDE, START_LATITUDE, START_LONGITUDE, AVGSPEED }, 		
				KEY_ID + "=?",
				new String[] { String.valueOf(id) }, null, null, null, null);
		if (cursor != null)
			cursor.moveToFirst();

		Trip trip = new Trip(Integer.parseInt(cursor.getString(0)),
				cursor.getFloat(1), cursor.getInt(2), cursor.getString(3), 
				cursor.getInt(4), cursor.getDouble(5), cursor.getDouble(6),
				cursor.getDouble(7), cursor.getDouble(8), cursor.getInt(9));
		// return trip
		return trip;
	}
	
	
	// Getting All Trips
	public List<Trip> getAllTrips() {
		List<Trip> tripList = new ArrayList<Trip>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				Trip trip = new Trip();
				trip.setID(Integer.parseInt(cursor.getString(0)));
				trip.setDistance(cursor.getFloat(1));
				trip.setMaxspeed(cursor.getInt(2));
				trip.setDate(cursor.getString(3));
				trip.setAltitude(cursor.getInt(4));
				trip.setLatitude(cursor.getDouble(5));
				trip.setLongitude(cursor.getDouble(6));
				trip.set_startLat(cursor.getDouble(7));
				trip.set_startLog(cursor.getDouble(8));
				trip.set_averageSpeed(cursor.getInt(9));
				// Adding trip to list
				tripList.add(trip);
			} while (cursor.moveToNext());
		}

		// return trip list
		return tripList;
	}

	// Updating single trip
	public int updateTrip(Trip trip) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_DISTANCE, trip.getDistance());
		values.put(KEY_MAXSPEED, trip.getMaxspeed());
		values.put(KEY_DATE, trip.getDate());
		values.put(ALTITUDE, trip.getAltitude());
		values.put(LATITUDE, trip.getLatitude());
		values.put(LONGITUDE, trip.getLongitude());
		values.put(START_LATITUDE, trip.get_startLat());
		values.put(START_LONGITUDE, trip.get_startLog());
		values.put(AVGSPEED, trip.get_averageSpeed());
		// updating row
		return db.update(TABLE_CONTACTS, values, KEY_ID + " = ?",
				new String[] { String.valueOf(trip.getID()) });
	}

	// Deleting single trip
	public void deleteTrip(Trip trip) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_CONTACTS, KEY_ID + " = ?",
				new String[] { String.valueOf(trip.getID()) });
		db.close();
	}
	
	public void deleteTrip(int trip) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_CONTACTS, KEY_ID + " = ?",
				new String[] { String.valueOf(trip) });
		db.close();
	}

	// Getting trips Count
	public int getTripCount() {
		String countQuery = "SELECT  * FROM " + TABLE_CONTACTS;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		cursor.close();
		return cursor.getCount();
	}

}
