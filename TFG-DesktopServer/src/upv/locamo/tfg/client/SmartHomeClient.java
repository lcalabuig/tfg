package upv.locamo.tfg.client;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.data.Status;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;


public class SmartHomeClient {
	


	public static void main(String args[]) throws Exception {
		/*
		ClientResource resource = new ClientResource("http://locamo.no-ip.org/iplist/locamo");
				
		JSONObject jsonSend = new JSONObject();
		jsonSend.put("ip", "152.33.33.54");
		Representation obj = new JsonRepresentation(jsonSend); 
		resource.put(obj);
		*/
		ClientResource resource = new ClientResource("http://locamo.no-ip.org/iplist/");
		System.out.println(resource.getResponse());
		System.out.println(resource.getStatus().isSuccess());
		System.out.println(resource.getStatus().equals(Status.CLIENT_ERROR_NOT_FOUND));
		/*
		Representation objectRepresentation = resource.get(MediaType.APPLICATION_JSON);
		JSONObject json = (new JsonRepresentation(objectRepresentation)).getJsonObject();
		//String as = objectRepresentation.getText();
		//System.out.println(json.getString("ip"));
		*/
		JSONObject jsonSend = new JSONObject();
		jsonSend.put("ip", "152.33.33.54");
		jsonSend.put("userID", "asñlkdfj");
		jsonSend.put("pass", "60");
		Representation obj = new JsonRepresentation(jsonSend); 
		resource.put(obj);
		System.out.println(resource.getStatus().isSuccess());
		
		/*
		ClientResource clientResource = new ClientResource(Method.PUT, "http://locamo.no-ip.org/iplist/locamo");
		JSONObject jsonSend = new JSONObject();
		jsonSend.put("ip", "152.33.33.54");
		Representation obj = new JsonRepresentation(jsonSend); 
		clientResource.put(obj);
		*/
	}
	
	
	/*
	public static void delete() throws ResourceException{
		String ip = UsersServerResource.getIPByUser("locamo");
		SmartHomeUser userToDelete = new SmartHomeUser("locamo", ip);
		for (SmartHomeUser a : UsersServerResource.getUsers()){
			if (a.getUsername().equals(userToDelete.getUsername()))
				UsersServerResource.getUsers().remove(a);
		}
		
		//boolean removed = UsersServerResource.getUsers().remove(user);
		//System.out.println(UsersServerResource.getUsers().size());
		//UsersServerResource.getUsers().add(new SmartHomeUser("locmao", ip));
			
	}
*/
	
}
