package com.example.simplecompass;

public class getGPS {
	
	public double latitude;
	public double longitude;
	public boolean update;
	
	public double getLong() {
		// get longitude
		longitude = 0;
		return longitude;
	}
	public double getLat() {
		// get latitude
		latitude = 0;
		return latitude;
	}
	public void updateGPS(boolean isTrue) {
		// if true, update every time GPS coordinate changes, else don't update
		update = isTrue;
	}
}