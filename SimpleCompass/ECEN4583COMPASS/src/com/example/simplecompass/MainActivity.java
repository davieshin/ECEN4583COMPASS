package com.example.simplecompass;

import java.math.BigDecimal;
import java.math.RoundingMode;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;

public class MainActivity extends Activity {

	public boolean APP_CAN_UPDATE = true;
	public int APP_COMPASS_UPDATE_RATE_MS = 1;

	public static boolean COMPASS_DISPLAY_RADIANS;

	public boolean APP_ERROR_GPS_ACK = false;
	public boolean APP_ERROR_COMPASS_ACK = false;

	private getGPS gps;
	private getBearing compass;
	
	private ImageView image;
	private float currentDegree = 0f;
	



	////////////////////////////////////////
	//void onCreate()
	//Inputs: Bundle savedInstanceState
	//Outputs: None
	//
	//Run when the app is opened by the Android OS
	//for the first time. Sets the view to the
	//main activity and starts the update loop.
	//Preconditions: The MainActivity is started for the first time
	//Postconditions: The app is running.
	////////////////////////////////////////

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		PreferenceManager.setDefaultValues(this, R.xml.pref_general, false);
		image = (ImageView) findViewById(R.id.graphic_view_temp);
		
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
	//Preconditions: the app has been started for the first time
	//Postconditions: The app now displays the Action Menu
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
	//Preconditions: The app has just started for the first time
	//Postconditions: none (runs as long as the app does)
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

				//Get settings values
				display_getSettings();

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
	//Preconditions: The MainActivity has focus
	//Postconditions: The GPS fields in the UI
	//are updated with current values.
	///////////////////////////////
	void display_GPSUpdate()
	{

		//Error checking
		//Get GPS values
		if(gps.canGetLocation())
		{
			APP_ERROR_GPS_ACK = false;
			
		double latitude = round(gps.getLatitude(), 2);
		double longitude = round(gps.getLongitude(), 2);
		
		int rlatitude = (int) latitude;
		int rlongitude = (int) longitude;
		int latminute = minutes(latitude);
		int longminute = minutes(longitude);
		int latsecond = seconds(latitude);
		int longsecond = seconds(longitude);

		//Declare TextView elements so we
		//can manipulate them in code
		TextView lat_view = (TextView) findViewById(R.id.gps_lat_value);
		TextView long_view = (TextView) findViewById(R.id.gps_long_value);

		//Update screen elements.
		lat_view.setText(Integer.toString(rlatitude) + (char) 0x00B0 + Integer.toString(latminute) + "'" + Integer.toString(latsecond) + "''");
		long_view.setText(Integer.toString(rlongitude) + (char) 0x00B0 + Integer.toString(longminute) + "'" + Integer.toString(longsecond) + "''");
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
	//Reads in data from the compass class
	//and updates the appropriate elements
	//in the main activity.
	//
	//Preconditions: The MainActivity has focus.
	//Postconditions: The bearing field in the UI is
	//updated with the current value and
	//the compass graphic spins to match.
	///////////////////////////////
	void display_CompassUpdate()
	{

		String str;
		TextView compass_view = (TextView) findViewById(R.id.bearing_value);

		//Check for errors... eventually.

		if(compass.isValid())
		{
			APP_ERROR_COMPASS_ACK = false;
			//Get compass value
			double bearing = compass.Bearing();
			//Declare the TextView element so
			//we can manipulate it in the code
			
			//round
			int roundedbearing = (int) bearing;
			
			//rotate compass
			rotateCompass(roundedbearing);

			if(COMPASS_DISPLAY_RADIANS)
			{
				roundedbearing = (int) degreesToRadians(bearing);
			}


			str = Integer.toString(roundedbearing);
			if(COMPASS_DISPLAY_RADIANS)
			{
				str = str + " mrad";
			}
			else
			{
				str = str + (char) 0x00B0; //00B0 ~ degree symbol
			}
		}
		else
		{
			str = "INVALID";
			if(!APP_ERROR_COMPASS_ACK)
			{
				//Display a toast pop-up
				CharSequence text = "There is a problem with the compass hardware. Try moving the device away from any magnetic or metallic objects.";
				Toast.makeText(MainActivity.this,text,Toast.LENGTH_LONG).show();

				APP_ERROR_COMPASS_ACK = true;
			}
		}

		//Update screen element.

		compass_view.setText(str);

		return;	
	}
	////////////////////////
	//degreesToRadians
	//
	//Input: degrees (double)
	//Output: radians (double)
	//Preconditions: COMPASS_DISPLAY_RADIANS
	//must be TRUE (user has chosen to display milliradians
	//instead of degrees)
	//
	//Postconditions: The main activity will
	//display the output in milliradians instead of degrees.
	//////////////////////////
	double degreesToRadians(double degrees)
	{
		//Convert to radians and multiply by 1000
		double returnval = (degrees/360) * 2 * 3.14 * 1000;
		return returnval;
	}
	
	////////////////////////////
	//display_getSettings()
	//
	//Preconditions: The GPS and Compass fields
	//have been updated with current values.
	//
	//Postconditions: User-chosen settings will
	//be applied the next time the app updates.
	//////////////////////////////
	void display_getSettings()
	{
		//Get recent preferences
		//Show radians?
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		COMPASS_DISPLAY_RADIANS = sharedPref.getBoolean("checkbox_use_radians", false);

		//Compass Update rate
		APP_COMPASS_UPDATE_RATE_MS = Integer.parseInt(sharedPref.getString("text_compass_pref", "1"));

		//GPS Min distance
		gps.MIN_DISTANCE_CHANGE_FOR_UPDATES = Integer.parseInt(sharedPref.getString("text_gps_min_distance", "10"));

		//GPS Min time between updates
		gps.MIN_TIME_BW_UPDATES = Integer.parseInt(sharedPref.getString("text_gps_update_rate", "30"));

		return;

	}

	///////////////////////////
	//onOptionsItemSelected
	//Input: MenuItem item
	//Output: bool (true)
	//
	//Android-provided function that handles
	//Action Menu selections.
	//
	//Preconditions: App is currently displaying the MainActivity.
	//Postconditions: MainActivity is paused and the SettingsActivity is run.
	////////////////////////////
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{	    		
		if(item.getItemId() == R.id.action_settings)
		{
			openSettings();
	    }
		return true;
	}

	///////////////////////
	//openSettings()
	//
	//Starts the SettingsActivity
	//
	//Preconditions: App is on MainActivity and user has selected
	//"Settings" in the Action Menu
	//Postconditions: MainActivity is paused and SettingsActivity is run.
	///////////////////////
	public void openSettings()
	{
		Intent intent = new Intent(this, SettingsActivity.class);
		startActivity(intent);		
	}
	
	//////////////////////////
	//rotateCompass()
	//Inputs: int bearing
	//
	//Rotates the compass graphic to match the
	//given bearing
	//
	//Preconditions: App can get valid bearing value, MainActivity is
	//current activity.
	//Postconditions: Compass graphic moves to match the given
	//bearing.
	/////////////////////////
	public void rotateCompass(int bearing) {
		// get the angle around the z-axis rotated
        float degree = (float) bearing;
 
        // create a rotation animation (reverse turn degree degrees)
        RotateAnimation ra = new RotateAnimation(
                currentDegree, 
                -degree,
                Animation.RELATIVE_TO_SELF, 0.5f, 
                Animation.RELATIVE_TO_SELF,
                0.5f);
 
        // how long the animation will take place
        ra.setDuration(10);
 
        // set the animation after the end of the reservation status
        ra.setFillAfter(true);
 
        // Start the animation
        image.startAnimation(ra);
        currentDegree = -degree;
	}
	
	//////////////////////
	//round()
	// rounds doubles to given number of decimal places
	//
	//Inputs: double value, int places
	//Outputs: value (rounded to places decimal points)
	//
	//Preconditions: None
	//Postconditions: None
	//////////////////////
	public static double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    BigDecimal bd = new BigDecimal(value);
	    bd = bd.setScale(places, RoundingMode.HALF_UP);
	    return bd.doubleValue();
	}
	
	////////////////////
	//minutes()
	//Inputs: double value
	//Outputs: int minutes
	//Takes a decimal latitude/longitude value
	//and calculates the minutes from it.
	//Preconditions: none
	//Postconditions: none
	////////////////////
	public static int minutes(double value) {
		value = Math.abs(value);
		int temp = (int) value;
		int hold = (int) ((value % temp)*100);
		int Minute = ((hold*60)/100);
		return Minute;
	}
	
	////////////////////
	//seconds()
	//Inputs: double value
	//Outputs: int seconds
	//Takes a decimal latitude/longitude value
	//and calculates the seconds from it.
	//Preconditions: none
	//Postconditions: none
	////////////////////
	public static int seconds(double value) {
		value = Math.abs(value);
		int temp = (int) value;
		int hold = (int) ((value % temp)*100);
		int Minute = ((hold*60) % 100);
		int Second = (Minute*60)/100;
		return Second;
	}
	
	///////////////////
	//onResume()
	//Android-provided function called
	//when the MainActivity activity is
	//resumed from pause.
	//
	//Preconditions: The MainActivity activity
	//has been paused by the Android OS
	//PostConditions: The MainActivity activity
	//is unpaused and GPS/Compass updates resume.
	///////////////////
	 protected void onResume() {
		    super.onResume();
		    gps.getLocation();
		    compass.startUsingCompass();		   
		  }
		 
	 ///////////////////
	 //onPause()
	 //Android-provided function called
	 //when the MainActivity activity does
	 //not have focus, i.e. when the Settings menu
	 //is activated or the user runs another app.
	 //
	 //Preconditions: The MainActivity is the currently running
	 //activity.
	 //PostConditions: The MainActivity activity
	 //is paused and GPS/Compass updates are stopped.
	 ///////////////////
	 protected void onPause() {
		    super.onPause();
		    gps.stopUsingGPS();
		    compass.stopUsingCompass();	    
		  }
	

}