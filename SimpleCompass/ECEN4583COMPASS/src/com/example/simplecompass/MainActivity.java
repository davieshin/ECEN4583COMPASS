package com.example.simplecompass;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	public boolean APP_CAN_UPDATE = true;
	public int APP_COMPASS_UPDATE_RATE_MS = 500;


	public static boolean COMPASS_DISPLAY_RADIANS;

	public boolean APP_ERROR_GPS_ACK = false;
	public boolean APP_ERROR_COMPASS_ACK = false;

	private getGPS gps;
	private getBearing compass;



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

		Context context = getApplicationContext();
		gps = new getGPS(context);
		compass = new getBearing(context);

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
		final Handler handler = new Handler();
		//handler.postDelayed(new Runnable()
		handler.post(new Runnable()
		{
			@Override
			public void run()
			{

				//Update GPS
				display_GPSUpdate();

				//Update compass
				display_CompassUpdate();

				handler.postDelayed(this,APP_COMPASS_UPDATE_RATE_MS);

			}
		//}, APP_COMPASS_UPDATE_RATE_MS);
		});

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

		//Check for errors... eventually.

		//Get GPS values
		if(gps.canGetLocation())
		{
		double latitude = gps.getLatitude();
		double longitude = gps.getLongitude();

		//Declare TextView elements so we
		//can manipulate them in code
		TextView lat_view = (TextView) findViewById(R.id.gps_lat_value);
		TextView long_view = (TextView) findViewById(R.id.gps_long_value);

		//Update screen elements.
		lat_view.setText(Double.toString(latitude));
		long_view.setText(Double.toString(longitude));
		}
		else
		{
			TextView lat_view = (TextView) findViewById(R.id.gps_lat_value);
			TextView long_view = (TextView) findViewById(R.id.gps_long_value);

			lat_view.setText("INVALID");
			long_view.setText("INVALID");

			if(!APP_ERROR_GPS_ACK)
			{
				//Display a toast pop-up
				final String text = "A problem has occured with the GPS hardware. Ensure that you have a signal and that the location service is enabled.";			
				Toast.makeText(MainActivity.this,text,Toast.LENGTH_LONG).show();

				APP_ERROR_GPS_ACK = true;
			}
		}

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

		String str;
		TextView compass_view = (TextView) findViewById(R.id.bearing_value);

		//Check for errors... eventually.

		if(compass.isValid())
		{
			//Get compass value
			double bearing = compass.Bearing();
			bearing = bearing*360/(2*3.14); //Returns radians by default, convert to degrees
			
			if (bearing < 0)
			{
				bearing = bearing + 360;
			}
			
			//Get the setting value(s) so we know what we need to display.
			SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
			COMPASS_DISPLAY_RADIANS = sharedPref.getBoolean("example_checkbox", false);

			if(COMPASS_DISPLAY_RADIANS)
			{
				bearing = degreesToRadians(bearing);
			}


			//str = Double.toString(bearing);
			str = String.format("%.0f", bearing);
			if(COMPASS_DISPLAY_RADIANS)
			{
				str = str + " mrad";
			}
			else
			{
				str = str + " deg";
			}
		}
		else
		{
			str = "INVALID";
			if(!APP_ERROR_COMPASS_ACK)
			{
				Context context = getApplicationContext();
				CharSequence text = "There is a problem with the compass hardware. Try moving the device away from any magnetic or metallic objects.";
				Toast.makeText(MainActivity.this,text,Toast.LENGTH_LONG).show();

				APP_ERROR_COMPASS_ACK = true;
			}
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