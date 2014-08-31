package upv.locamo.tfg.smarthome.server;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;

import upv.locamo.tfg.smarthome.server.resources.DistancesServerResource;
import upv.locamo.tfg.smarthome.server.resources.ShoppingItemServerResource;
import upv.locamo.tfg.smarthome.server.resources.ShoppingListServerResource;
import upv.locamo.tfg.smarthome.server.resources.UserServerResource;
import upv.locamo.tfg.smarthome.server.resources.UsersServerResource;

public class ServerApplication extends Application{
	
	/**
	 * Constructor for change the settings
	 */
	public ServerApplication() {
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
		router.attach("/users", UsersServerResource.class);
		router.attach("/users/", UsersServerResource.class);
		router.attach("/users/{userID}", UserServerResource.class);
		router.attach("/distances", DistancesServerResource.class);
		router.attach("/shoppingList", ShoppingListServerResource.class);
		router.attach("/shoppingList/{item}", ShoppingItemServerResource.class);
		
		return router;
	}

}