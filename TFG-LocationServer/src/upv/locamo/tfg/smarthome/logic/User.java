package upv.locamo.tfg.smarthome.logic;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class User {

	private String username = "";
	private ArrayList<Location> locationList = null;

	/**
	 * Constructor
	 * @param uname
	 * @param locationList
	 */
	public User(String uname, ArrayList<Location> locationList) {
		this.username = uname;
		this.locationList = locationList;
	}

	// Getters and setters
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public ArrayList<Location> getLocationList() {
		return locationList;
	}

	public void setLocationList(ArrayList<Location> locationList) {
		this.locationList = locationList;
	}

	/**
	 * Adds a specific location to list
	 * @param location
	 */
	public void addLocation(Location location) {
		locationList.add(location);
	}

	/**
	 * Deletes a specific location from list
	 * @param location
	 */
	public void deleteLocation(Location location) {
		for (Location l : locationList) {
			if (l == location)
				locationList.remove(l);
		}
	}
	
	/**
	 * This method creates a JSONObject with the name of the user and
	 * his locations
	 * 
	 * @return JSONObject with name and a JSONArray with positions
	 * @throws JSONException
	 */
	public JSONObject getJSONUser() throws JSONException {
		JSONObject jsonResult = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		jsonResult.put("userID", username);
		if (locationList != null){
			for (Location l : locationList) {
				jsonArray.put(l.getJSONPosition());
			}
		}
		jsonResult.put("location", jsonArray);

		return jsonResult;
	}

}
