package com.example.simplecompass;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	public boolean APP_CAN_UPDATE = true;
	public int APP_UPDATE_RATE_MS = 500;
	
	public static boolean COMPASS_DISPLAY_RADIANS;

	////////////////////////////////////////
	//void onCreate()
	//Inputs: Bundle savedInstanceState
	//Outputs: None
	//
	//Run when the app is opened by the Android OS
	//for the first time. Sets the view to the
	//main activity and starts the update loop.
	////////////////////////////////////////
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		PreferenceManager.setDefaultValues(this, R.xml.pref_general, false);
		
		startUpdateLoop();

	}
	
	//////////////////////////////////////////
	//boolean onCreateOptionsMenu()
	//Inputs: Menu menu
	//Outputs: Boolean, true
	//
	//Inflates the action menu and adds items to it
	////////////////////////////////////////
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	////////////////////////////////////
	//void startUpdateLoop()
	//Inputs: none
	//Outputs: none
	//
	//The applications's main update loop.
	//Calls the functions that update the
	//display, then pauses for a short amount of
	//time before updating again.
	////////////////////////////////////
	void startUpdateLoop()
	{
		Handler handler = new Handler();
		handler.postDelayed(new Runnable()
		{
			public void run()
			{
				//Update GPS
				display_GPSUpdate();
				
				//Update compass
				display_CompassUpdate();
			}
		}, APP_UPDATE_RATE_MS);
		
		return;
	}
	
	//////////////////////////////
	//void display_GPSUpdate()
	//Inputs: none
	//Outputs: none
	//
	//Reads in data from the GPS class
	//and updates the appropriate elements
	//in the main activity.
	///////////////////////////////
	void display_GPSUpdate()
	{
		getGPS gps = new getGPS();
		
		//Check for errors... eventually.
		
		//Get GPS values
		double latitude = gps.getLat();
		double longitude = gps.getLong();
		
		//Declare TextView elements so we
		//can manipulate them in code
		TextView lat_view = (TextView) findViewById(R.id.gps_lat_value);
		TextView long_view = (TextView) findViewById(R.id.gps_long_value);
		
		//Update screen elements.
		lat_view.setText(Double.toString(latitude));
		long_view.setText(Double.toString(longitude));
		
		return;
		
		
	}
	
	//////////////////////////////
	//void display_CompassUpdate()
	//Inputs: none
	//Outputs: none
	//
	//Reads in data from the GPS class
	//and updates the appropriate elements
	//in the main activity.
	///////////////////////////////
	void display_CompassUpdate()
	{
		getBearing compass = new getBearing();
		
		//Check for errors... eventually.
		
		//Get compass value
		double bearing = compass.Bearing();
		
		//Declare the TextView element so
		//we can manipulate it in the code
		
		TextView compass_view = (TextView) findViewById(R.id.bearing_value);
		
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
		COMPASS_DISPLAY_RADIANS = sharedPref.getBoolean("example_checkbox", false);
		
		if(COMPASS_DISPLAY_RADIANS)
		{
			bearing = degreesToRadians(bearing);
		}
		
		
		String str = Double.toString(bearing);
		if(COMPASS_DISPLAY_RADIANS)
		{
			str = str + " mrad";
		}
		else
		{
			str = str + " deg";
		}
		
		//Update screen element.
		
		compass_view.setText(str);
		
		return;	
	}
	
	double degreesToRadians(double degrees)
	{
		//Convert to radians and multiply by 1000
		double returnval = (degrees/360) * 2 * 3.14 * 1000;
		return returnval;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{	    		
		if(item.getItemId() == R.id.action_settings)
		{
			openSettings();
	    }
		return true;
	}
	
	public void openSettings()
	{
		Intent intent = new Intent(this, SettingsActivity.class);
		startActivity(intent);		
	}
	
}
