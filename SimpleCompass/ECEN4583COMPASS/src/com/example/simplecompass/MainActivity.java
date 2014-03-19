package com.example.simplecompass;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	public boolean APP_CAN_UPDATE = true;
	public int APP_UPDATE_RATE_MS = 500;

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
		
		//Update screen element.
		compass_view.setText(Double.toString(bearing));
		
		return;	
	}
	
}
