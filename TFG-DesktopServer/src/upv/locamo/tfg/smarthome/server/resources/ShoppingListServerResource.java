package upv.locamo.tfg.smarthome.server.resources;

import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.data.Status;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

public class ShoppingListServerResource extends ServerResource {

	private static ArrayList<String> shoppingList = new ArrayList<String>();

	public static ArrayList<String> getShoppingList() {
		return shoppingList;
	}
	public static void setShoppingList(ArrayList<String> newShoppingList) {
		shoppingList = newShoppingList;
	}

	/**
	 * Method GET
	 * Returns the shopping list
	 */
	@Override
	protected Representation get() throws ResourceException {

		return new JsonRepresentation(shoppingListToJSON());

	}

	/**
	 * Method PUT
	 * Adds a item to shopping list
	 */
	@Override
	protected Representation put(Representation representation)
			throws ResourceException {

		try {
			JSONObject json = (new JsonRepresentation(representation))
					.getJsonObject();
			JSONArray jsonArray = new JSONArray();
			jsonArray = json.getJSONArray("shopping_list");
			for (int i = 0; i < jsonArray.length(); i++) {
				if (!ifItemExists(jsonArray.get(i).toString()))
					shoppingList.add(jsonArray.get(i).toString());
			}

			return new JsonRepresentation(shoppingListToJSON());

		} catch (JSONException | IOException e) {
			setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
			return null;
		}

	}
	
	private boolean ifItemExists(String item){
		for (String s : shoppingList)
			if (s.toLowerCase().equals(item.toLowerCase()))
				return true;
		return false;
	}
	
	/**
	 * Adds a item to the shopping list
	 * @param item
	 */
	public static void addItem(String item) {
		shoppingList.add(item);
	}

	/**
	 * Constructs a JSONObject of shopping list
	 * @return JSONObject of shopping list
	 */
	public static JSONObject shoppingListToJSON() {
		JSONObject json = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		try {
			for (String s : shoppingList)
				jsonArray.put(s);
			json.put("shopping_list", jsonArray);
			return json;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

}
