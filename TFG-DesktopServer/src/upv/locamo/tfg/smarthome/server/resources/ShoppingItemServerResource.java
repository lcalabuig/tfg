package upv.locamo.tfg.smarthome.server.resources;

import java.util.ArrayList;

import org.restlet.data.Status;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

public class ShoppingItemServerResource extends ServerResource {
	
	private String item;

	/**
	 * This method gets an item of shopping list by the URL
	 * Example:
	 *     URL: http://localhost:8284/shoppingList/tomatoes
	 *     item = tomatoes
	 */
	@Override
	protected void doInit() throws ResourceException {
		item = getAttribute("item");
	}
	
	/**
	 * Method DELETE
	 * Deletes a specific item of shopping list
	 */
	@Override
	protected Representation delete() throws ResourceException {
		ArrayList<String> aux = ShoppingListServerResource.getShoppingList();
		if (aux.size() != 0){
			aux.remove(item);
			//ShoppingListServerResource.removeItem(item);
			return new JsonRepresentation(ShoppingListServerResource.shoppingListToJSON());
		}
		else {
			setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
			return null;
		}

	}

}
