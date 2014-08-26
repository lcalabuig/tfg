package upv.locamo.tfg.smarthome.logic;

import java.util.ArrayList;
import java.util.Date;

public class Distance {

	// Location of home
	public static Location home = new Location(-0.6591402085289246,
			38.71733000907843);

	private static ArrayList<Distance> distances = new ArrayList<Distance>();
	private static ArrayList<String> actions = new ArrayList<String>();

	private double distance;
	private float accuracy;
	private Date date;

	/**
	 * Constructor
	 * 
	 * @param distance
	 * @param accuracy
	 * @param date
	 */
	public Distance(double distance, float accuracy, Date date) {
		this.distance = distance;
		this.accuracy = accuracy;
		this.date = date;
	}

	public double getDistance() {
		return distance;
	}

	public float getAccuracy() {
		return accuracy;
	}

	public Date getDate() {
		return date;
	}

	/**
	 * Gets the list of distances
	 * 
	 * @return distances ArrayList
	 */
	public static ArrayList<Distance> getDistancesList() {
		return distances;
	}

	/**
	 * Adds a new distance to list
	 * 
	 * @param distance
	 */
	public static void addDistance(Distance distance) {
		distances.add(distance);
	}

	/**
	 * Haversine formula to calculate distance between a location and home
	 * 
	 * @param longitude
	 * @param latitude
	 * @return distance (in meters)
	 */
	public static double calculateDistance(double lon, double lat) {
		double earthRadius = 6371000; // meters
		double dLat = Math.toRadians(lat - home.getLatitude());
		double dLng = Math.toRadians(lon - home.getLongitude());
		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
				+ Math.cos(Math.toRadians(home.getLatitude()))
				* Math.cos(Math.toRadians(lat)) * Math.sin(dLng / 2)
				* Math.sin(dLng / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		float dist = (float) (earthRadius * c);
		return dist;
	}

	public static void applyRulesAccordingDistance(Distance dist) {

		Rules rule = new Rules();

		// This array contains this numbers:
		// -1: distances.get(i) > distances.get(i + 1) - indicates that user is
		// approaching
		// 0: distances.get(i) = distances.get(i + 1) - indicates that user is
		// at the same location
		// 1: distances.get(i) < distances.get(i + 1) - indicates that user is
		// moving away
		int sizeAux = 4;
		int[] aux = new int[sizeAux];
		int index = Distance.getDistancesList().indexOf(dist);

		if (distances.size() != 0 && index > distances.size()) {
			for (int i = 4; i >= 0; i--) {
				if (distances.get(index - i).getDistance() < distances.get(
						index - i + 1).getDistance())
					aux[sizeAux - i] = 1;
				else if (distances.get(index - i).getDistance() > distances
						.get(index - i + 1).getDistance())
					aux[sizeAux - i] = -1;
				else
					aux[sizeAux - i] = 0;
			}
			// User is approaching to home
			if (countEqualNumbers(aux, -1) + countEqualNumbers(aux, 0) > countEqualNumbers(
					aux, 1) + countEqualNumbers(aux, 0)) {

				// If user is 200 to 500 meters home, activate
				// "twoHundredMetersApproachingRule"
				if (dist.getDistance() >= 200 && dist.getDistance() < 500) {
					if ((actions.size() != 0 && !actions.get(actions.size())
							.equals("200mApproaching")) || actions.size() == 0) {
						rule.twoHundredMetersApproachingRule();
						actions.add("200mApproaching");
					}
				}
				// If user is 500 to 1000 meters home, activate
				// "fiveHundredMetersApproachingRule"
				else if (dist.getDistance() >= 500 && dist.getDistance() < 1000) {
					if ((actions.size() != 0 && !actions.get(actions.size())
							.equals("500mApproaching")) || actions.size() == 0) {
						rule.fiveHundredMetersApproachingRule(dist.getDate());
						actions.add("500mApproaching");
					}
				}
				// If the is 1000 or more meters home, activate
				// "oneThousandMetersApproachingRule"
				else if (dist.getDistance() >= 1000){
					if ((actions.size() != 0 && !actions.get(actions.size())
							.equals("1000mApproaching")) || actions.size() == 0) {
						rule.oneThousandMetersApproachingRule(dist.getDate());
						actions.add("1000mApproaching");
					}
				}

			}
			// User is moving away from home
			else if (countEqualNumbers(aux, -1) + countEqualNumbers(aux, 0) < countEqualNumbers(
					aux, 1) + countEqualNumbers(aux, 0)) {

				// If user is 200 to 500 meters home, activate
				// "twoHundredMetersApproachingRule"
				if (dist.getDistance() >= 200 && dist.getDistance() < 500) {
					if ((actions.size() != 0 && !actions.get(actions.size())
							.equals("200mMovingAway")) || actions.size() == 0) {
						rule.twoHundredMetersApproachingRule();
						actions.add("200mMovingAway");
					}
				}
				// If user is 500 to 1000 meters home, activate
				// "fiveHundredMetersApproachingRule"
				else if (dist.getDistance() >= 500 && dist.getDistance() < 1000) {
					if ((actions.size() != 0 && !actions.get(actions.size())
							.equals("500mMovingAway")) || actions.size() == 0) {
						rule.fiveHundredMetersApproachingRule(dist.getDate());
						actions.add("500mMovingAway");
					}
					else if (!actions.get(actions.size()).equals("200mMovingAway"))
						rule.twoHundredMetersMovingAwayRule();
						actions.add("200mMovingAway");
						rule.fiveHundredMetersMovingAwayRule(dist.getDate());
						actions.add("500mMovingAway");
				}
				// If the is 1000 or more meters home, activate
				// "oneThousandMetersApproachingRule"
				else if (dist.getDistance() >= 1000)
					if ((actions.size() != 0 && !actions.get(actions.size())
							.equals("1000mMovingAway")) || actions.size() == 0) {
						rule.oneThousandMetersApproachingRule(dist.getDate());
						actions.add("1000mMovingAway");
					}
					else if (!actions.get(actions.size()).equals("500mMovingAway")){
						rule.fiveHundredMetersApproachingRule(dist.getDate());
						actions.add("500mMovingAway");
						rule.oneThousandMetersApproachingRule(dist.getDate());
						actions.add("1000mMovingAway");
					}
					else if (!actions.get(actions.size()).equals("200mMovingAway")){
						rule.twoHundredMetersMovingAwayRule();
						actions.add("200mMovingAway");
						rule.fiveHundredMetersApproachingRule(dist.getDate());
						actions.add("500mMovingAway");
						rule.oneThousandMetersApproachingRule(dist.getDate());
						actions.add("1000mMovingAway");
					}
			}
		}

	}

	private static int countEqualNumbers(int[] array, int number) {
		int count = 0;
		for (int i = 0; i < array.length; i++)
			if (array[i] == number)
				count++;
		return count;
	}

}
