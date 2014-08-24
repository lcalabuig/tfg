package upv.locamo.tfg.smarthome.server.resources;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import upv.locamo.tfg.smarthome.logic.Distance;


public class DistancesServerResource extends ServerResource{
	
	@Override
	protected Representation get() throws ResourceException {

		JSONArray jsonArray = new JSONArray();
		 
		try {
			for (int i = 0; i < Distance.getDistancesList().size(); i++) {
				JSONObject jsonObj = new JSONObject();
				Distance dist = Distance.getDistancesList().get(i);
				jsonObj.put("time", dist.getDate());
				jsonObj.put("distance", dist.getDistance());
				jsonObj.put("accuracy", dist.getAccuracy());
				jsonArray.put(jsonObj);
			}
			return new JsonRepresentation(jsonArray);
		} catch (JSONException e) {
			e.printStackTrace();
			return new StringRepresentation("Cannot create the list");
		}		
	}

}
