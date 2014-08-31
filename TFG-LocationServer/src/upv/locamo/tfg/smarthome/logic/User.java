package upv.locamo.tfg.smarthome.logic;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class User {

	private String username = "";
	private ArrayList<Location> locationList = null;

	public User(String uname, ArrayList<Location> locationList) {
		this.username = uname;
		this.locationList = locationList;
	}

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

	public void addLocation(Location location) {
		locationList.add(location);
	}

	public void deleteLocation(Location location) {
		for (Location l : locationList) {
			if (l == location)
				locationList.remove(l);
		}
	}

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
