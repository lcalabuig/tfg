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
import upv.locamo.tfg.smarthome.logic.User;
import upv.locamo.tfg.smarthome.logic.UserBehaviour;

public class UserServerResource extends ServerResource {

	private String userID;

	/**
	 * This method gets the user by the URL Example: URL:
	 * http://localhost/users/locamo user = locamo
	 */
	@Override
	protected void doInit() throws ResourceException {
		userID = getAttribute("userID");

	}

	/**
	 * Method GET Returns information of a determinate user
	 */
	@Override
	protected Representation get() throws ResourceException {
		if (UsersServerResource.contains(userID)) {
			ArrayList<Location> arrayList = UsersServerResource
					.getLocationListByUser(userID);
			User user = new User(userID, arrayList);
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
	 * Method PUT Option 1: Insert/Change the IP of a determinate user Option 2:
	 * Insert the location of a determinate user
	 */
	@Override
	protected Representation put(Representation representation)
			throws ResourceException {

		try {
			JSONObject json = (new JsonRepresentation(representation))
					.getJsonObject();

			JSONArray jsonArray = new JSONArray();
			jsonArray = json.getJSONArray("location");
			User user = new User(userID,
					UsersServerResource.getLocationListByUser(userID));

			// Add the location sent to the locations list and calculate distance
			Distance dist = addLocationToList(user, jsonArray);
			UserBehaviour.applyRulesAccordingDistance(dist);

			return new JsonRepresentation(new User(userID,
					user.getLocationList()).getJSONUser());

		} catch (JSONException | IOException e) {
			setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
			return null;
		}

	}


	/**
	 * Inserts location received into location list and calculates distance
	 * between location and home
	 * 
	 * @param jsonArray
	 * @return calculated distance
	 */
	public Distance addLocationToList(User user, JSONArray jsonArray) {
		Location loc = null;
		Distance dist = null;
		try {
			if (jsonArray != null) {
				loc = jsonToLocation((JSONObject) jsonArray.get(0));
				if (!UsersServerResource.ifUserHaveLocation(userID, loc)) {
					user.addLocation(loc);
					System.out.println(loc.getAccuracy());
					// Calculate distance between location received and home
					double distance = Distance.calculateDistance(loc);
					dist = new Distance(distance, loc.getAccuracy(),
							loc.getDate());
					UserBehaviour.addDistance(dist);
				}

			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return dist;
	}

	/**
	 * Creates a new Location object from a JSONObject
	 * 
	 * @param JSONObject
	 * @return location
	 */
	public static Location jsonToLocation(JSONObject json) {
		try {
			Location l = new Location(json.getDouble("longitude"),
					json.getDouble("latitude"), json.getLong("time"),
					json.getInt("accuracy"));
			return l;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}

	}

}
