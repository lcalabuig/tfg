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
import upv.locamo.tfg.smarthome.logic.User;

public class UsersServerResource extends ServerResource {

	private static ArrayList<User> users = new ArrayList<User>();
	
	private String password = "tfg";
	
	public static ArrayList<User> getUsers() {
		return users;
	}
	
	/**
	 * Method GET: gets the list with all users registered and their locations
	 */
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
	
	/**
	 * Method PUT: inserts a new user
	 */
	@Override
	protected Representation put(Representation representation) throws ResourceException {
		
		try {
			JSONObject json = (new JsonRepresentation(representation)).getJsonObject();
			String username = json.getString("userID");
			String pass = json.getString("pass");
			// Adds a user only if he/she introduced the correct password
			if (pass.equals(password)){
				User user = new User(username, new ArrayList<Location>());
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
	
	/**
	 * Obtains the list with the locations of a specific user
	 * 
	 * @param uname
	 * @return list with locations
	 */
	public static ArrayList<Location> getLocationListByUser (String uname){
		ArrayList<Location> list = new ArrayList<Location>();
		for (User user : users){
			if (user.getUsername().equals(uname))
				list = user.getLocationList();
		}
		return list;
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
	/**
	 * Check if an user is already inserted in the list
	 * 
	 * @param uname
	 * @return
	 */
	public static boolean contains (String uname){
		for (User user : users){
			if (user.getUsername().equals(uname))
				return true;
		}
		return false;
	}
	
	/**
	 * Adds a Location in the list only if the location received is not
	 * the same as an already inserted
	 * 
	 * @param uname
	 * @param location
	 * @return false if location not exists in the list
	 */
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
