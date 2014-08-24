package upv.locamo.tfg.smarthome.logic;

import java.util.ArrayList;
import java.util.Date;

public class Distance {
	
	// Location of home
	public static Location home = new Location(-0.6591402085289246,38.71733000907843);	
	
	private static ArrayList<Distance> distances = new ArrayList<Distance>();
	
	private double distance;
	private float accuracy;
	private Date date;
	
	/**
	 * Constructor
	 * @param distance
	 * @param accuracy
	 * @param date
	 */
	public Distance (double distance, float accuracy, Date date){
		this.distance = distance;
		this.accuracy = accuracy;
		this.date = date;
	}

	public double getDistance(){
		return distance;
	}
	public float getAccuracy(){
		return accuracy;
	}
	public Date getDate(){
		return date;
	}
	
	/**
	 * Gets the list of distances
	 * @return distances ArrayList
	 */
	public static ArrayList<Distance> getDistancesList(){
		return distances;
	}
	
	public static void addDistance(Distance distance){
		distances.add(distance);
	}

	
	/**
	 * Haversine formula to calculate distance between a location and home 
	 * @param longitude
	 * @param latitude
	 * @return distance (in meters)
	 */
	public static double calculateDistance(double lon, double lat){
		double earthRadius = 6371000; //meters
	    double dLat = Math.toRadians(lat-home.getLatitude());
	    double dLng = Math.toRadians(lon-home.getLongitude());
	    double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
	               Math.cos(Math.toRadians(home.getLatitude())) * Math.cos(Math.toRadians(lat)) *
	               Math.sin(dLng/2) * Math.sin(dLng/2);
	    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
	    float dist = (float) (earthRadius * c);
	    return dist;
	}

}
