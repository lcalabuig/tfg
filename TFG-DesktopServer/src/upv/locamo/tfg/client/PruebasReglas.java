package upv.locamo.tfg.client;

import java.util.ArrayList;
import java.util.Date;

import upv.locamo.tfg.smarthome.logic.Distance;

public class PruebasReglas {

	public static void main (String[] args) throws Exception{
		//CopyOfRules rules = new CopyOfRules();
		
		//rules.levelOneMovingAwayRule();
		Distance dist1 = new Distance(50, 60, new Date(System.currentTimeMillis()));
		Distance dist2 = new Distance(1000, 60, new Date(System.currentTimeMillis()+30000));
		
		Distance.addDistance(dist1);
		Distance.addDistance(dist2);
		
		int i = Distance.getDistancesList().indexOf(dist2);
		
		ArrayList<String> actions = new ArrayList<String>();
		if (actions.size() == 0)
			
			System.out.println(actions.get(0));
		//System.out.println(Distance.getDistancesList().);
	}
	
}
