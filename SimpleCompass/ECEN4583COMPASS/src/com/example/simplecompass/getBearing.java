package com.example.simplecompass;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class getBearing implements SensorEventListener
{
	
	public double bearing; // View to draw a compass
	public boolean reliable = true;
	private SensorManager mSensorManager;
	 Sensor accelerometer;
	 Sensor magnetometer;
	 Sensor compass;
	 
	 private final Context mContext;
	 
	 static final float ALPHA = 0.2f;
	 

	 getBearing(Context context)
	 {
		 this.mContext = context;

		 mSensorManager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
		 accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		 magnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

		 mSensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_FASTEST);
		 mSensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_FASTEST);
//		 mSensorManager.registerListener(this, compass, SensorManager.SENSOR_DELAY_FASTEST);
		 
	 }

	 /*
protected void onResume() {
super.onResume();
mSensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
mSensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_UI);
}
*/

	 /*
protected void onPause() {
super.onPause();
mSensorManager.unregisterListener(this);
}
*/

	 public void onAccuracyChanged(Sensor sensor, int accuracy) { }
	 float[] mGravity;
	 float[] mGeomagnetic;
	 public void onSensorChanged(SensorEvent event)
	 {
		 if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
		 {
			 if (event.accuracy == SensorManager.SENSOR_STATUS_UNRELIABLE)
			 {
				 reliable = false; 
			 }
			 else 
			 {
				 reliable = true;
				 mGravity = lowPass(event.values.clone(), mGravity);
			 }
		 }
		 if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
		 {
			 if (event.accuracy == SensorManager.SENSOR_STATUS_UNRELIABLE)
			 {
				 reliable = false; 
			 }
			 else 
			 {
				 reliable = true;
				 mGeomagnetic = lowPass(event.values.clone(), mGeomagnetic);
			 }
		 }
		 if (mGravity != null && mGeomagnetic != null)
		 {
			 float R[] = new float[9];
			 float I[] = new float[9];
			 boolean success = SensorManager.getRotationMatrix(R, I, mGravity, mGeomagnetic);
			 if (success)
			 {
				 float orientation[] = new float[3];
				 SensorManager.getOrientation(R, orientation);
				 bearing = (orientation[0])*360/(2*Math.PI); 
					if (bearing < 0)
					{
						bearing = bearing + 360;
					}
			 }
		 }
	 }
	 public double Bearing() {
		 return bearing;
	 }
	 
	 protected float[] lowPass(float[] input, float[] output)
	 {
	     if (output == null)
	         return input;

	     for (int i = 0; i < input.length; i++)
	     {
	         output[i] = output[i] + ALPHA * (input[i] - output[i]);
	     }
	     return output;
	 }
	 
	 public void startUsingCompass(){
		 mSensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
		 mSensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_UI);
	}

	public void stopUsingCompass(){
		if (accelerometer != null)
		{
		mSensorManager.unregisterListener(this, accelerometer);
		}
		if (magnetometer != null)
		{
		mSensorManager.unregisterListener(this, magnetometer); 
		}
	}

	public boolean isValid()
	{
		if (bearing < 0 || bearing > 360)
		{
			return false;
		}	
		else if (accelerometer == null)
		{
			return false;
		}
		else if (magnetometer == null)
		{
			return false;
		}
		else 
		{
		return reliable;
		}
		}

}
