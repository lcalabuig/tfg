package upv.locamo.tfg.ip;

import org.json.JSONException;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import upv.locamo.tfg.smarthome.user.SmartHomeUser;


public class RootServerResource extends ServerResource {

	public RootServerResource(){
		setNegotiated(false);
	}
	
	@Override
	protected Representation get() throws ResourceException{
		System.out.println("The GET method of root resource was invoked.");
		SmartHomeUser user = new SmartHomeUser("locamo", "158.42.51.2");
		try {
			return new JsonRepresentation(user.getJsonUser());
		} catch (JSONException e) {
			e.printStackTrace();
			return new StringRepresentation("Cannot create the JSONOject for the user");			
		}
	}
	
	@Override
	protected Representation options() throws ResourceException{
		System.out.println("The OPTIONS method of root resource was invoked.");
		throw new RuntimeException("Not yet implemented");
	}
	

}
