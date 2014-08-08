package upv.locamo.tfg.smarthome.user;

import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.ext.json.JsonRepresentation;

public class SmartHomeUser {

	private String username = "";
	private String ip = "";

	public SmartHomeUser(String uname, String ip) {
		this.username = uname;
		this.ip = ip;
	}

	public SmartHomeUser() {
		new SmartHomeUser("", "");
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

	public JSONObject getJsonUser() throws JSONException {
		JSONObject json = new JSONObject();
		json.put("userID", getUsername());
		json.put("ip", getIP());
		return json;
	}

	

}
