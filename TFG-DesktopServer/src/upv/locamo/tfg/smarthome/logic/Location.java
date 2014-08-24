package upv.locamo.tfg.smarthome.logic;

import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

public class Location {
	
	private double longitude;
	private double latitude;
	private float accuracy;	
	private long time;

	/**
	 * Constructor
	 * @param longitude
	 * @param latitude
	 * @param time
	 * @param accuracy
	 */
	public Location(double longitude, double latitude,long time, float accuracy) {
		this.longitude = longitude;
		this.latitude = latitude;
		this.time = time;
		this.accuracy = accuracy;
	}
	
	/**
	 * Generic constructor for Location
	 */
	public Location(){
		new Location(0, 0, 0L, 0);
	}
	/**
	 * Constructor
	 * @param longitude
	 * @param latitude
	 */
	public Location(double longitude, double latitude){
		this.longitude = longitude;
		this.latitude = latitude;
	}
	
	/*
	 * Getters and setters for Location attributes
	 */
	
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(long longitude) {
		this.longitude = longitude;
	}
	public double getLatitude() {
		return latitude;
	}
	
	public void setLatitude(long latitude) {
		this.latitude = latitude;
	}
	
	public Date getDate() {
		Date d = new Date(time);
		return d;
	}
	
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	public float getAccuracy() {
		return accuracy;
	}
	public void setAccuracy(float accuracy) {
		this.accuracy = accuracy;
	}
	
	/**
	 * Creates a JSON object of Location
	 * @return JSONObject
	 */
	public JSONObject getJSONPosition(){
        JSONObject jsonObjLocation = new JSONObject();
        try {
        	jsonObjLocation.put("longitude", longitude);
			jsonObjLocation.put("latitude", latitude);
			jsonObjLocation.put("accuracy", accuracy);
	        jsonObjLocation.put("date", getDate());
	        return jsonObjLocation;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

}
