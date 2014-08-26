package upv.locamo.tfg.smarthome.logic;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;

import upv.locamo.tfg.smarthome.server.ServerComponent;

public class CopyOfRules {
	
	/*
	 * Some resources of smarthome
	 */
	
	// Lightning - togglebistate functionality
	String LIGHTS_KITCHEN_CENTRAL = "DF-CUINA.IL.CENTRAL";
	String LIGHTS_KITCHEN_AUXILIARY = "DF-CUINA.IL.AUXILIAR";
	String LIGHTS_KITCHEN_HOB = "DF-CUINA.IL.BANCADA";
	String LIGHTS_DININGROOM_CENTRAL = "DF-MENJ.IL.CENTRAL";
	String LIGHTS_DININGROOM_AUXILIARY = "DF-MENJ.IL.AUXILIAR";
	String LIGHTS_ROOM1_CENTRAL = "DF-HAB1.IL.CENTRAL";
	String LIGHTS_ROOM2_CENTRAL = "DF-HAB2.IL.CENTRAL";
	String LIGHTS_ROOM3_CENTRAL = "DF-HAB3.IL.CENTRAL";

	// Blinds - movement functionality
	String BLINDS_DININGROOM_12 = "DF-MENJ.FINESTRA.PERSIANES12";
	String BLINDS_DININGROOM_34 = "DF-MENJ.FINESTRA.PERSIANES34";
	String BLINDS_DININGROOM_5 = "DF-MENJ.FINESTRA.PERSIANA5";
	String BLINDS_DININGROOM_ALL = "DF-MENJ.FINESTRA.PERSIANES";
	String BLINDS_ROOM1 = "DF-HAB1.FINESTRA.PERSIANA";
	String BLINDS_ROOM1_STOR = "DF-HAB1.FINESTRA.ESTOR";
	String BLINDS_ROOM2 = "DF-HAB2.FINESTRA.PERSIANA";
	String BLINDS_ROOM3 = "DF-HAB3.FINESTRA.PERSIANA";
	String BLINDS_ROOM3_STOR = "DF-HAB3.FINESTRA.ESTOR";

	// Heating - togglebistate functionality
	String HEATER_ROOM1 = "DF-HAB1.CL.CALEFACTOR";
	String HEATER_ROOM2 = "DF-HAB2.CL.CALEFACTOR";
	String HEATER_ROOM3 = "DF-HAB3.CL.CALEFACTOR";
	String HEATER_CBATHROOM = "DF-BC.CL.CALEFACTOR";
	String HEATER_DBATHROOM = "DF-BM.CL.CALEFACTOR";

	// Air conditioner - togglebistate functionality
	String AIRCOND_DININGROOM = "DF-MENJ.CL.AC";
	String AIRCOND_ROOM1 = "DF-HAB1.CL.AC";
	String AIRCOND_ROOM3 = "DF-HAB3.CL.AC";

	// Sensors - bitstate functionality
	String SENSOR_MOVEMENT = "DF-ENT.SE.DETMOV.SENSOR";
	String SENSOR_CLOSEDOOR = "DF-PORTA.SENSOR.TANCADA";

	/*
	 * Actions than can be made over the above resources
	 */
	
	String ENABLE = "biaON";
	String DISABLE = "biaOFF";
	String OPEN = "movaOPEN";
	String CLOSE = "movaCLOSE";
	
	/**
	 * Rule that is activated when the user moves away 200 or more meters home
	 */
	public void levelOneMovingAwayRule () {		

		sendActionToServer(LIGHTS_KITCHEN_AUXILIARY, ENABLE);
		sendActionToServer(LIGHTS_DININGROOM_AUXILIARY, ENABLE);
		
		sendActionToServer(LIGHTS_KITCHEN_CENTRAL, DISABLE);
		sendActionToServer(LIGHTS_KITCHEN_HOB, DISABLE);
		sendActionToServer(LIGHTS_DININGROOM_CENTRAL, DISABLE);
		sendActionToServer(LIGHTS_ROOM1_CENTRAL, DISABLE);
		sendActionToServer(LIGHTS_ROOM2_CENTRAL, DISABLE);
		sendActionToServer(LIGHTS_ROOM3_CENTRAL, DISABLE);
		sendActionToServer(HEATER_CBATHROOM, DISABLE);
		sendActionToServer(HEATER_DBATHROOM, DISABLE);
		
		sendActionToServer(SENSOR_MOVEMENT, ENABLE);
		sendActionToServer(SENSOR_CLOSEDOOR, ENABLE);
		
	}
	
	/**
	 * Rule that is activated when the user approaches 200 or more meters home
	 */
	public void levelOneApproachingRule () {
		
		sendActionToServer(LIGHTS_KITCHEN_AUXILIARY, ENABLE);
		sendActionToServer(LIGHTS_DININGROOM_AUXILIARY, ENABLE);
		
	}
	
	/**
	 * Rule that is activated when the user moves away 200 or more meters home
	 */
	public void levelTwoMovingAwayRule (Date date) {

		// Down blinds only if we are in the months of May to October
		if (getMonth(date) >= 5 && getMonth(date) <= 10 ){
			sendActionToServer(BLINDS_DININGROOM_12, CLOSE);
			sendActionToServer(BLINDS_DININGROOM_34, CLOSE);
			sendActionToServer(BLINDS_ROOM1, CLOSE);
			sendActionToServer(BLINDS_ROOM2, CLOSE);
			sendActionToServer(BLINDS_ROOM3, CLOSE);
			// Open that blinds if it is day
			if (getHour(date) <= 23 && getHour(date) >= 7){
				sendActionToServer(BLINDS_DININGROOM_5, OPEN);
				sendActionToServer(BLINDS_ROOM1_STOR, OPEN);
				sendActionToServer(BLINDS_ROOM3_STOR, OPEN);
			}
			// Close that blinds if it is night
			else {
				sendActionToServer(BLINDS_DININGROOM_5, CLOSE);
				sendActionToServer(BLINDS_ROOM1_STOR, CLOSE);
				sendActionToServer(BLINDS_ROOM3_STOR, CLOSE);
			}
		}
		
	}
	
	/**
	 * Rule that is activated when the user approaches 500 or more meters home
	 */
	public void levelTwoApproachingRule (Date date) {

		// Down blinds only if we are in the months of November to April
		if (getMonth(date) <= 4 && getMonth(date) >= 11 ){
			// Open that blinds if it is day
			if (getHour(date) <= 23 && getHour(date) >= 7){
				sendActionToServer(BLINDS_DININGROOM_ALL, OPEN);
				sendActionToServer(BLINDS_ROOM1_STOR, OPEN);
				sendActionToServer(BLINDS_ROOM2, OPEN);
				sendActionToServer(BLINDS_ROOM3_STOR, OPEN);
			}
		}
		
	}
	
	/**
	 * Rule that is activated when the user moves away 1000 or more meters home
	 * 
	 * or when the user is a long time away from home
	 */
	public void levelThreeMovingAwayRule (Date date) {

		// Turn off air conditioner only if we are in the months of June to September
		if (getMonth(date) >= 6 && getMonth(date) <= 9){
			sendActionToServer(AIRCOND_DININGROOM, DISABLE);
			sendActionToServer(AIRCOND_ROOM1, DISABLE);
			sendActionToServer(AIRCOND_ROOM3, DISABLE);
		}
		// Turn off heater only if we are in the months of November to February
		else if (getMonth(date) >= 11 && getMonth(date) <= 2) {
			sendActionToServer(AIRCOND_DININGROOM, DISABLE);
			sendActionToServer(HEATER_ROOM1, DISABLE);
			sendActionToServer(HEATER_ROOM2, DISABLE);
			sendActionToServer(HEATER_ROOM3, DISABLE);
		}
		
	}
	
	/**
	 * Rule that is activated when the user approaches 1000 or more meters home
	 */
	public void levelThreeApproachingRule (Date date) {

		// Turn on air conditioner only if we are in the months of June to September
		if (getMonth(date) >= 6 && getMonth(date) <= 9){
			sendActionToServer(AIRCOND_DININGROOM, ENABLE);
			sendActionToServer(AIRCOND_ROOM1, ENABLE);
			sendActionToServer(AIRCOND_ROOM3, ENABLE);
		}
		// Turn on heater only if we are in the months of November to February
		else if (getMonth(date) >= 11 && getMonth(date) <= 2) {
			sendActionToServer(AIRCOND_DININGROOM, ENABLE);
			sendActionToServer(HEATER_ROOM1, ENABLE);
			sendActionToServer(HEATER_ROOM2, ENABLE);
			sendActionToServer(HEATER_ROOM3, ENABLE);
		}
		
	}
	
	/**
	 * Sends to server an action on a given resource
	 * @param resource
	 * @param action
	 */
	private void sendActionToServer (String resource, String action) {
		ClientResource clientResource = new ClientResource(
				"http://localhost:8182/devFunc/" + resource);
		try {
			JSONObject jsonSend = new JSONObject();
			jsonSend.put("action", action);
			Representation obj = new JsonRepresentation(jsonSend); 
			clientResource.put(obj);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Returns the current state of a given resource of smarthome
	 * @param resource
	 * @return current state of the given resource (String)
	 */
	private String getResourceCurrentState (String resource){
		ClientResource clientResource = new ClientResource(
				ServerComponent.getSmartHomeURL() + "/devFunc/" + resource);
		try {
			Representation representation = clientResource.get();
			JSONObject jsonRep = (new JsonRepresentation(representation)).getJsonObject();
			return jsonRep.getString("current-state");
		} catch (JSONException | IOException e){
			e.printStackTrace();
			return null;
		}
	}
	
	
	/**
	 * Returns the month of a given date
	 * @param date
	 * @return month (int)
	 */
	private int getMonth (Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.MONTH);
	}
	
	/**
	 * Returns the hour of a given date
	 * @param date
	 * @return month (int)
	 */
	private int getHour (Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.HOUR_OF_DAY);
	}
	

}