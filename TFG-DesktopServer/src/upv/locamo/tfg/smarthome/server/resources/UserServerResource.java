package upv.locamo.tfg.smarthome.server.resources;

import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.data.Status;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import upv.locamo.tfg.smarthome.logic.Distance;
import upv.locamo.tfg.smarthome.logic.Location;
import upv.locamo.tfg.smarthome.logic.SmartHomeUser;

public class UserServerResource extends ServerResource {

	private String userID;

	/**
	 * This method gets the user by the URL
	 * Example:
	 *     URL: http://localhost/users/locamo
	 *     user = locamo
	 */
	@Override
	protected void doInit() throws ResourceException {
		userID = getAttribute("userID");

	}

	/**
	 * Method GET
	 * Returns information of a determinate user
	 */
	@Override
	protected Representation get() throws ResourceException {
		if (UsersServerResource.contains(userID)) {
			String ip = UsersServerResource.getIPByUser(userID);
			ArrayList<Location> arrayList = UsersServerResource
					.getLocationListByUser(userID);
			SmartHomeUser user = new SmartHomeUser(userID, ip, arrayList);
			try {
				return new JsonRepresentation(user.getJSONUser());
			} catch (JSONException e) {
				e.printStackTrace();
				return new StringRepresentation(
						"Cannot create the JSONOject for the user");
			}
		} else {
			setStatus(Status.CLIENT_ERROR_NOT_FOUND, "Invalid username");
			return null;
		}
	}

	/**
	 * Method PUT
	 * Option 1: Insert/Change the IP of a determinate user
	 * Option 2: Insert the location of a determinate user
	 */
	@Override
	protected Representation put(Representation representation)
			throws ResourceException {

		try {
			JSONObject json = (new JsonRepresentation(representation))
					.getJsonObject();
			// If the user introduce the IP
			if (json.has("ip")) {
				String ip = json.getString("ip");
				System.out.println(ip);
				UsersServerResource.setIPByUser(userID, ip);
				ArrayList<Location> list = UsersServerResource
						.getLocationListByUser(userID);
				return new JsonRepresentation(new SmartHomeUser(userID, ip,
						list).getJSONUser());
			}
			// If user introduce the position
			else {
				JSONArray jsonArray = new JSONArray();
				jsonArray = json.getJSONArray("location");
				String ip = UsersServerResource.getIPByUser(userID);
				// jsonArray = json.getJSONArray("location");
				SmartHomeUser user = new SmartHomeUser(userID, ip,
						UsersServerResource.getLocationListByUser(userID));
				
				// Add the location sent to the locations list and calculate distance
				addLocationToList(user, jsonArray);			

				return new JsonRepresentation(new SmartHomeUser(userID, ip,
						user.getLocationList()).getJSONUser());
			}

		} catch (JSONException | IOException e) {
			setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
			return null;
		}

	}

	/**
	 * Method DELETE
	 * Deletes a determinate user
	 */
	@Override
	protected Representation delete() throws ResourceException {
		String ip = UsersServerResource.getIPByUser(userID);
		ArrayList<Location> locationList = UsersServerResource
				.getLocationListByUser(userID);
		SmartHomeUser userToDelete = new SmartHomeUser(userID, ip, locationList);
		try {
			for (SmartHomeUser a : UsersServerResource.getUsers()) {
				if (a.getUsername().equals(userID))
					UsersServerResource.getUsers().remove(a);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			return new JsonRepresentation(userToDelete.getJSONUser());
		} catch (JSONException e) {
			setStatus(Status.CLIENT_ERROR_NOT_FOUND, "Invalid username");
			return null;
		}

	}

	/**
	 * Insert location received into location list
	 * @param jsonArray
	 * @return location added
	 */
	public void addLocationToList(SmartHomeUser user, JSONArray jsonArray) {
		Location loc = null;
		try {
			if (jsonArray != null) {
				for (int i = 0; i < jsonArray.length(); i++) {
					loc = jsonToLocation((JSONObject) jsonArray.get(i));
					if (!UsersServerResource.ifUserHaveLocation(userID, loc)) {
						user.addLocation(loc);
						System.out.println(loc.getAccuracy());
						// Calculate distance between location received and home
						calculateDistance(loc);
					}
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Creates a new Location object from a JSONObject
	 * @param JSONObject
	 * @return location
	 */
	public static Location jsonToLocation(JSONObject json) {
		try {
			Location l = new Location(json.getDouble("longitude"),
					json.getDouble("latitude"),
					json.getLong("time"),
					json.getInt("accuracy"));
			return l;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}

	}
	
	/**
	 * Calculates and adds to list the distance between home and the location
	 * @param location
	 */
	public void calculateDistance(Location loc){
			double distance = Distance.calculateDistance(
					loc.getLongitude(), loc.getLatitude());
			Distance dist = new Distance(distance, loc.getAccuracy(), loc.getDate());
			System.out.println(dist.getDistance());
			Distance.addDistance(dist);		
	}
}
