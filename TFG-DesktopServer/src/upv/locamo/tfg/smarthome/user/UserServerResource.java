package upv.locamo.tfg.smarthome.user;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.data.Status;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

public class UserServerResource extends ServerResource {

	private String userID;

	@Override
	protected void doInit() throws ResourceException {
		userID = getAttribute("userID");

	}

	@Override
	protected Representation get() throws ResourceException {
		if (UsersServerResource.contains(userID)) {
			String ip = UsersServerResource.getIPByUser(userID);
			SmartHomeUser user = new SmartHomeUser(userID, ip);
			try {
				return new JsonRepresentation(user.getJsonUser());
			} catch (JSONException e) {
				e.printStackTrace();
				return new StringRepresentation(
						"Cannot create the JSONOject for the user");
			}
		} else {
			setStatus(Status.CLIENT_ERROR_NOT_FOUND, "Username not valid"); 
			return null; 
		}
	}

	@Override
	protected Representation put(Representation representation)
			throws ResourceException {

		try {
			JSONObject json = (new JsonRepresentation(representation))
					.getJsonObject();
			String ip = json.getString("ip");
			System.out.println(ip);
			UsersServerResource.setIPByUser(userID, ip);
			return new JsonRepresentation(
					new SmartHomeUser(userID, ip).getJsonUser());
		} catch (JSONException | IOException e) {
			return new StringRepresentation("Cannot update the user info");
		}

	}

	@Override
	protected Representation delete() throws ResourceException {
		String ip = UsersServerResource.getIPByUser(userID);
		SmartHomeUser userToDelete = new SmartHomeUser(userID, ip);

		for (SmartHomeUser a : UsersServerResource.getUsers()) {
			if (a.getUsername().equals(userToDelete.getUsername()))
				UsersServerResource.getUsers().remove(a);
		}

		try {
			return new JsonRepresentation(userToDelete.getJsonUser());
		} catch (JSONException e) {
			e.printStackTrace();
			return new StringRepresentation(
					"Cannot delete the JSONOject for the user");
		}

	}

	public SmartHomeUser getUserFromJson(JsonRepresentation json)
			throws JSONException {
		JSONObject jsonUser = json.getJsonObject();
		System.out.println(jsonUser.getString("ip"));
		SmartHomeUser user = new SmartHomeUser(userID, jsonUser.getString("ip"));
		return user;
	}

}
