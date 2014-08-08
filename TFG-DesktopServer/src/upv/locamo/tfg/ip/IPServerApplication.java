package upv.locamo.tfg.ip;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.resource.ServerResource;
import org.restlet.routing.Router;

import upv.locamo.tfg.smarthome.user.*;

public class IPServerApplication extends Application{
	
	/**
	 * Constructor for change the settings
	 */
	public IPServerApplication() {
		setName("RESTful Server");
		setDescription("Example for a basic server for Android IPs");
		setOwner("Lorena Calabuig");
		setAuthor("locamo");
	}

	/**
	 * Factory method called by the framework when the application starts 
	 * @return Restlet - a string formatted
	 */
	@Override
	public Restlet createInboundRoot() {
		
		Router router = new Router(getContext());
		router.attach("/", RootServerResource.class);
		router.attach("/iplist/", UsersServerResource.class);
		router.attach("/iplist/{userID}", UserServerResource.class);
		
		return router;
	}

}