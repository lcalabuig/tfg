package upv.locamo.tfg.smarthome.logic;

import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;

import upv.locamo.tfg.smarthome.server.ServerComponent;

public class Rules {

	// Lightning
	String LIGHTS_KITCHEN_CENTRAL = "DF-CUINA.IL.CENTRAL";
	String LIGHTS_KITCHEN_AUXILIARY = "DF-CUINA.IL.AUXILIAR";
	String LIGHTS_KITCHEN_HOB = "DF-CUINA.IL.BANCADA";
	String LIGHTS_DININGROOM_CENTRAL = "DF-MENJ.IL.CENTRAL";
	String LIGHTS_DININGROOM_AUXILIARY = "DF-MENJ.IL.AUXILIAR";
	String LIGHTS_ROOM1_CENTRAL = "DF-HAB1.IL.CENTRAL";
	String LIGHTS_ROOM2_CENTRAL = "DF-HAB2.IL.CENTRAL";
	String LIGHTS_ROOM3_CENTRAL = "DF-HAB3.IL.CENTRAL";

	// Blinds
	String BLINDS_DININGROOM_12 = "DF-MENJ.FINESTRA.PERSIANES12";
	String BLINDS_DININGROOM_34 = "DF-MENJ.FINESTRA.PERSIANES34";
	String BLINDS_DININGROOM_5 = "DF-MENJ.FINESTRA.PERSIANA5";
	String BLINDS_DININGROOM_ALL = "DF-MENJ.FINESTRA.PERSIANES";
	String BLINDS_ROOM1 = "DF-HAB1.FINESTRA.PERSIANA";
	String BLINDS_ROOM1_STOR = "DF-HAB1.FINESTRA.ESTOR";
	String BLINDS_ROOM2 = "DF-HAB2.FINESTRA.PERSIANA";
	String BLINDS_ROOM3 = "DF-HAB3.FINESTRA.PERSIANA";
	String BLINDS_ROOM3_STOR = "DF-HAB3.FINESTRA.ESTOR";

	// Heating
	String HEATER_ROOM1 = "DF-HAB1.CL.CALEFACTOR";
	String HEATER_ROOM2 = "DF-HAB2.CL.CALEFACTOR";
	String HEATER_ROOM3 = "DF-HAB3.CL.CALEFACTOR";
	String HEATER_CBATHROOM = "DF-BC.CL.CALEFACTOR";
	String HEATER_DBATHROOM = "DF-BM.CL.CALEFACTOR";

	// Air conditioner
	String AIRCOND_DININGROOM = "DF-MENJ.CL.AC";
	String AIRCOND_ROOM1 = "DF-HAB1.CL.AC";
	String AIRCOND_ROOM3 = "DF-HAB3.CL.AC";

	private void sendActionToServer(String action, String resource) {
		ClientResource clientResourse = new ClientResource(
				ServerComponent.getSmartHomeURL() + "/devFunc/" + resource);
		try {
			JSONObject jsonSend = new JSONObject();
			jsonSend.put("action", action);
			Representation obj = new JsonRepresentation(jsonSend); 
			clientResourse.put(obj);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	

}