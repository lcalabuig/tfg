package upv.locamo.tfg.smarthome.user;

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

public class UsersServerResource extends ServerResource {

	private static ArrayList<SmartHomeUser> users = new ArrayList<SmartHomeUser>(){
		private static final long serialVersionUID = 1L;

	{add(new SmartHomeUser("locamo", "158.42.63.55"));add(new SmartHomeUser("temocas", "158.42.78.302"));}};

	private String password = "tfg-smarthome";
	
	public static ArrayList<SmartHomeUser> getUsers() {
		return users;
	}
	
	@Override
	protected Representation get() throws ResourceException {
		//addUsers();

		JSONArray jsonArray = new JSONArray();		
		try {
			for (int i = 0; i < getUsers().size(); i++) {
				jsonArray.put(getUsers().get(i).getJsonUser());
			}
			return new JsonRepresentation(jsonArray);
		} catch (JSONException e) {
			e.printStackTrace();
			return new StringRepresentation("Cannot create the list");
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
				SmartHomeUser user = new SmartHomeUser(username, ip);
				getUsers().add(user);
				return new JsonRepresentation(json);
			}
			else {
				return new StringRepresentation("Cannot add the user"); 
			}
		} catch (JSONException | IOException e) {
			e.printStackTrace();
			return new StringRepresentation("Cannot add the user");
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
	
	public static void setIPByUser (String uname, String ip){
		for (SmartHomeUser user : users){
			if (user.getUsername().equals(uname))
				user.setIP(ip);
		}
	}
	
	public static boolean contains (String uname){
		for (SmartHomeUser user : users){
			if (user.getUsername().equals(uname))
				return true;
		}
		return false;
	}
}
