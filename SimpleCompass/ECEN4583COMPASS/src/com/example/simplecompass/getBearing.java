package com.example.simplecompass;

public class getBearing {
	
	public double bearing;
	public boolean update;
	
	public double Bearing() {
		// get bearing
		bearing = 0;
		return bearing;
	}
	public void updateBearing(boolean isTrue) {
		// if true, update every time bearings changes, else don't update
		update = isTrue;
	}
}