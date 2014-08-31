package upv.locamo.tfg.smarthome.logic;

import java.util.ArrayList;

public class UserBehaviour {

	private static ArrayList<Distance> distances = new ArrayList<Distance>();

	private static ArrayList<String> actions = new ArrayList<String>();
	
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
		int index = distances.indexOf(dist);

		if (distances.size() != 0 && index >= 4) {
			for (int i = 4; i >= 0; i--) {
				int j = index - i;
				if (j < 4){
					if (distances.get(index).getDistance() > distances.get(j).getDistance())
						aux[sizeAux - i] = 1;
					else if (distances.get(index).getDistance() < distances.get(j).getDistance())
						aux[sizeAux - i] = -1;
					else
						aux[sizeAux - i] = 0;
				}
			}
			System.out.println("El número de -1 es: " + countEqualNumbers(aux, -1));
			System.out.println("El número de 0 es: " + countEqualNumbers(aux, 0));
			System.out.println("El número de 1 es: " + countEqualNumbers(aux, 1));
			// User is approaching to home
			if (countEqualNumbers(aux, -1) + countEqualNumbers(aux, 0) > countEqualNumbers(
					aux, 1) + countEqualNumbers(aux, 0)) {

				// If user is 200 to 500 meters home, activate
				// "twoHundredMetersApproachingRule"
				if (dist.getDistance() >= 200 && dist.getDistance() < 500) {
					if ((actions.size() != 0 && !actions.get(actions.size() - 1)
							.equals("200mApproaching"))) {
						rule.twoHundredMetersApproachingRule();
						actions.add("200mApproaching");
					}
					else if (!actions.get(actions.size() - 1).equals("500mApproaching")
							 || actions.size() == 0){
						rule.fiveHundredMetersApproachingRule(dist.getDate());
						actions.add("500mApproaching");
						rule.twoHundredMetersApproachingRule();
						actions.add("200mApproaching");
					}
					else if (!actions.get(actions.size() - 1).equals("1000mApproaching")
							 || actions.size() == 0){
						rule.oneThousandMetersApproachingRule(dist.getDate());
						actions.add("1000mApproaching");
						rule.fiveHundredMetersApproachingRule(dist.getDate());
						actions.add("500mApproaching");
						rule.twoHundredMetersApproachingRule();
						actions.add("200mApproaching");
					}
				}
				// If user is 500 to 1000 meters home, activate
				// "fiveHundredMetersApproachingRule"
				else if (dist.getDistance() >= 500 && dist.getDistance() < 1000) {
					if ((actions.size() != 0 && !actions.get(actions.size() - 1)
							.equals("500mApproaching"))) {
						rule.fiveHundredMetersApproachingRule(dist.getDate());
						actions.add("500mApproaching");
					}
					else if (!actions.get(actions.size() - 1).equals("1000mApproaching")
							 || actions.size() == 0){
						rule.oneThousandMetersApproachingRule(dist.getDate());
						actions.add("1000mApproaching");
						rule.fiveHundredMetersApproachingRule(dist.getDate());
						actions.add("500mApproaching");
					}
				}
				// If the is 1000 or more meters home, activate
				// "oneThousandMetersApproachingRule"
				else if (dist.getDistance() >= 1000){
					if ((actions.size() != 0 && !actions.get(actions.size() - 1)
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
					if ((actions.size() != 0 && !actions.get(actions.size() - 1)
							.equals("200mMovingAway")) || actions.size() == 0) {
						rule.twoHundredMetersMovingAwayRule();
						actions.add("200mMovingAway");
					}
				}
				// If user is 500 to 1000 meters home, activate
				// "fiveHundredMetersApproachingRule"
				else if (dist.getDistance() >= 500 && dist.getDistance() < 1000) {
					if ((actions.size() != 0 && !actions.get(actions.size() - 1)
							.equals("500mMovingAway"))) {
						rule.fiveHundredMetersMovingAwayRule(dist.getDate());
						actions.add("500mMovingAway");
					}
					else if (!actions.get(actions.size() - 1).equals("200mMovingAway")
							 || actions.size() == 0)
						rule.twoHundredMetersMovingAwayRule();
						actions.add("200mMovingAway");
						rule.fiveHundredMetersMovingAwayRule(dist.getDate());
						actions.add("500mMovingAway");
				}
				// If the is 1000 or more meters home, activate
				// "oneThousandMetersApproachingRule"
				else if (dist.getDistance() >= 1000)
					if ((actions.size() != 0 && !actions.get(actions.size() - 1)
							.equals("1000mMovingAway"))) {
						rule.oneThousandMetersMovingAwayRule(dist.getDate());
						actions.add("1000mMovingAway");
					}
					else if (!actions.get(actions.size() - 1).equals("500mMovingAway")){
						rule.fiveHundredMetersMovingAwayRule(dist.getDate());
						actions.add("500mMovingAway");
						rule.oneThousandMetersMovingAwayRule(dist.getDate());
						actions.add("1000mMovingAway");
					}
					else if (!actions.get(actions.size() - 1).equals("200mMovingAway")  
							 || actions.size() == 0){
						rule.twoHundredMetersMovingAwayRule();
						actions.add("200mMovingAway");
						rule.fiveHundredMetersMovingAwayRule(dist.getDate());
						actions.add("500mMovingAway");
						rule.oneThousandMetersMovingAwayRule(dist.getDate());
						actions.add("1000mMovingAway");
					}
			}
		}
		// If user is more than 2 km home first time that initialize app,
		// apply all rules moving away from home
		else if (distances.size() != 0 && index == 0 && distances.get(index).getDistance() >= 2000) {
			rule.twoHundredMetersMovingAwayRule();
			actions.add("200mMovingAway");
			rule.fiveHundredMetersMovingAwayRule(dist.getDate());
			actions.add("500mMovingAway");
			rule.oneThousandMetersMovingAwayRule(dist.getDate());
			actions.add("1000mMovingAway");
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
