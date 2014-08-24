package upv.locamo.tfg.smarthome.server;

import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;


public class RootServerResource extends ServerResource {

	public RootServerResource(){
		setNegotiated(false);
	}
	
	@Override
	protected Representation get() throws ResourceException{
		System.out.println("The GET method of root resource was invoked.");
		return new StringRepresentation("Hi everyone");
	}
	
	@Override
	protected Representation options() throws ResourceException{
		System.out.println("The OPTIONS method of root resource was invoked.");
		throw new RuntimeException("Not yet implemented");
	}
	

}
