package com.example.simplecompass;


import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

//public class getBearing {
//
//   public double bearing;
//   SensorEvent event;
//   SensorManager mSensorManager;
//   
//public double Bearing() {
//	// TODO Auto-generated method stub
//	mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
//	mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR),
//           SensorManager.SENSOR_DELAY_GAME);
//	
//	switch (event.sensor.getType()) {
//    case Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR:
//    	bearing = Math.round(event.values[0]);	
//        break;                      
//    default: 
//        break;
//	}
//	return bearing;
//}
//}

/*
public class getBearing extends Activity implements SensorEventListener {
	public double bearing; // View to draw a compass

private SensorManager mSensorManager;
 Sensor accelerometer;
 Sensor magnetometer;

protected void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);
mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
magnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
}

protected void onResume() {
super.onResume();
mSensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
mSensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_UI);
}

protected void onPause() {
super.onPause();
mSensorManager.unregisterListener(this);
}

public void onAccuracyChanged(Sensor sensor, int accuracy) { }
float[] mGravity;
float[] mGeomagnetic;
public void onSensorChanged(SensorEvent event) {
if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
	mGravity = event.values;
if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
	mGeomagnetic = event.values;
if (mGravity != null && mGeomagnetic != null) {
float R[] = new float[9];
float I[] = new float[9];
boolean success = SensorManager.getRotationMatrix(R, I, mGravity, mGeomagnetic);
if (success) {
	float orientation[] = new float[3];
SensorManager.getOrientation(R, orientation);
bearing = orientation[0]; 
}
}
}
public double Bearing() {
	return bearing;
}

public boolean isValid()
{
	return true;
}


}

*/

public class getBearing implements SensorEventListener
{
	public double bearing; // View to draw a compass

	private SensorManager mSensorManager;
	 Sensor accelerometer;
	 Sensor magnetometer;


	 private final Context mContext;

	 getBearing(Context context)
	 {
		 this.mContext = context;

		 mSensorManager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
		 accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		 magnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

		 mSensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
		 mSensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_UI);

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
			mGravity = event.values;
		 if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
			 mGeomagnetic = event.values;
		 if (mGravity != null && mGeomagnetic != null)
		 {
			 float R[] = new float[9];
			 float I[] = new float[9];
			 boolean success = SensorManager.getRotationMatrix(R, I, mGravity, mGeomagnetic);
			 if (success)
			 {
				 float orientation[] = new float[3];
				 SensorManager.getOrientation(R, orientation);
				 bearing = orientation[0]; 
			 }
		 }
	 }
public double Bearing() {
	return bearing;
}

public boolean isValid()
{
	return true;
}


}