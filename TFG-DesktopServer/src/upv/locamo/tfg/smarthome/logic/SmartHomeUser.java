package upv.locamo.tfg.smarthome.logic;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SmartHomeUser {

	private String username = "";
	private String ip = "";
	private ArrayList<Location> locationList = null;

	public SmartHomeUser(String uname, String ip, ArrayList<Location> locationList) {
		this.username = uname;
		this.ip = ip;
		this.locationList = locationList;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getIP() {
		return ip;
	}

	public void setIP(String ip) {
		this.ip = ip;
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

	public JSONObject getJsonUser() throws JSONException {
		JSONObject json = new JSONObject();
		json.put("userID", username);
		json.put("ip", ip);
		return json;
	}

	public JSONObject getJSONUser() throws JSONException {
		JSONObject jsonResult = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		jsonResult.put("userID", username);
		jsonResult.put("ip", ip);
		if (locationList != null){
			for (Location l : locationList) {
				jsonArray.put(l.getJSONPosition());
			}
		}
		jsonResult.put("location", jsonArray);

		return jsonResult;
	}

}
