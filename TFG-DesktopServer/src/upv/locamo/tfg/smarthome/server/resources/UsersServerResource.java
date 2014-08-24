package upv.locamo.tfg.smarthome.server.resources;

import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.data.Status;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import upv.locamo.tfg.smarthome.logic.Location;
import upv.locamo.tfg.smarthome.logic.SmartHomeUser;

public class UsersServerResource extends ServerResource {
/*
	private static ArrayList<SmartHomeUser> users = new ArrayList<SmartHomeUser>(){
		private static final long serialVersionUID = 1L;

	{add(new SmartHomeUser("locamo", null, new ArrayList<Location>()));
	 add(new SmartHomeUser("temocas", null, new ArrayList<Location>()));}};
*/
	private static ArrayList<SmartHomeUser> users = new ArrayList<SmartHomeUser>();
	
	private String password = "tfg";
	
	public static ArrayList<SmartHomeUser> getUsers() {
		return users;
	}
	
	@Override
	protected Representation get() throws ResourceException {

		JSONArray jsonArray = new JSONArray();		
		try {
			for (int i = 0; i < getUsers().size(); i++) {
				jsonArray.put(getUsers().get(i).getJSONUser());
			}
			return new JsonRepresentation(jsonArray);
		} catch (JSONException e) {
			setStatus(Status.CLIENT_ERROR_NOT_FOUND);
			return null;
		}		
	}
	
	@Override
	protected Representation put(Representation representation) throws ResourceException {
		
		try {
			JSONObject json = (new JsonRepresentation(representation)).getJsonObject();
			String username = json.getString("userID");
			String ip = json.getString("ip");
			String pass = json.getString("pass");
			if (pass.equals(password)){
				SmartHomeUser user = new SmartHomeUser(username, ip, new ArrayList<Location>());
				getUsers().add(user);
				return new JsonRepresentation(json);
			}
			else {
				setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
				return null; 
			}
		} catch (JSONException | IOException e) {
			setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
			return null;
		}
				
	}
	
	public static String getIPByUser (String uname){
		String ip = "";
		for (SmartHomeUser user : users){
			if (user.getUsername().equals(uname))
				ip = user.getIP();
		}
		return ip;
	}
	
	public static ArrayList<Location> getLocationListByUser (String uname){
		ArrayList<Location> list = new ArrayList<Location>();
		for (SmartHomeUser user : users){
			if (user.getUsername().equals(uname))
				list = user.getLocationList();
		}
		return list;
	}
	
	public static void setIPByUser (String uname, String ip){
		for (SmartHomeUser user : users){
			if (user.getUsername().equals(uname))
				user.setIP(ip);
		}
	}
	/*
	public static void setLocationListByUser (String uname, ArrayList<Location> list){
		for (SmartHomeUser user : users){
			if (user.getUsername().equals(uname)){
				user.setLocationList(list);
			}
		}
	}
	*/
	public static boolean contains (String uname){
		for (SmartHomeUser user : users){
			if (user.getUsername().equals(uname))
				return true;
		}
		return false;
	}
	
	public static boolean ifUserHaveLocation(String uname, Location l){
		ArrayList<Location> list = getLocationListByUser(uname);
		for (Location loc: list){
			if (loc.getTime() == l.getTime()){
				return true;
			}
		}
		return false;
	}
}
