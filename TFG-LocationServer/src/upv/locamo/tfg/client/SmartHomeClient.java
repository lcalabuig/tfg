package upv.locamo.tfg.client;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;



import upv.locamo.tfg.smarthome.logic.Location;


public class SmartHomeClient {
	
	private static ArrayList<Location> locationList = new ArrayList<Location>();
	private static ArrayList<Location> locationList2 = new ArrayList<Location>();
	private static double longitude;
	private static double latitude;
	private static float accuracy;
	private static long time;
	private static Date date;

	public static void main(String args[]) throws Exception {
		/*
		ClientResource resource = new ClientResource("http://locamo.no-ip.org/iplist/locamo");
				
		JSONObject jsonSend = new JSONObject();
		jsonSend.put("ip", "152.33.33.54");
		Representation obj = new JsonRepresentation(jsonSend); 
		resource.put(obj);
		*/
		/*
		ClientResource resource = new ClientResource("http://locamo.no-ip.org/iplist/");
		System.out.println(resource.getResponse());
		System.out.println(resource.getStatus().isSuccess());
		System.out.println(resource.getStatus().equals(Status.CLIENT_ERROR_NOT_FOUND));
		*/
		/*
		Representation objectRepresentation = resource.get(MediaType.APPLICATION_JSON);
		JSONObject json = (new JsonRepresentation(objectRepresentation)).getJsonObject();
		//String as = objectRepresentation.getText();
		//System.out.println(json.getString("ip"));
		*/
		/*
		JSONObject jsonSend = new JSONObject();
		jsonSend.put("ip", "152.33.33.54");
		jsonSend.put("userID", "asñlkdfj");
		jsonSend.put("pass", "60");
		Representation obj = new JsonRepresentation(jsonSend); 
		resource.put(obj);
		System.out.println(resource.getStatus().isSuccess());
		*/
		/*
		ClientResource clientResource = new ClientResource(Method.PUT, "http://locamo.no-ip.org/iplist/locamo");
		JSONObject jsonSend = new JSONObject();
		jsonSend.put("ip", "152.33.33.54");
		Representation obj = new JsonRepresentation(jsonSend); 
		clientResource.put(obj);
		*/
		/*
		ClientResource clientResource = new ClientResource(Method.GET, "http://locamo.no-ip.org/");
		Representation objectRepresentation = clientResource.get();
		String result = objectRepresentation.getText().toString();
		System.out.println(result);
		//String as = objectRepresentation.getText();
		//System.out.println(json.getString("ip"));
		 */
		long x = System.currentTimeMillis();
		System.out.println(getTimeFormatted(x));
		double p = 9.4684358468;
		long j = System.currentTimeMillis();
		int a = 56;
		addLocation(new Location(p,p, j, 56));
		addLocation(new Location(9.0,9.0, 9L, 9));
		System.out.println(getJSONUser().toString());
		
		System.out.println("-------------------------------------------------");
		
		JSONArray jsonArray = new JSONArray();
		jsonArray = getJSONUser().getJSONArray("location");
		System.out.println(jsonArray.toString());

		ArrayList<Location> list = new ArrayList<Location>();
		if (jsonArray != null) { 
			   for (int i = 0 ; i < jsonArray.length(); i++){ 
				   list.add(jsonToLocation((JSONObject) jsonArray.get(i)));
				   System.out.println(list.get(i).getAccuracy());
			   } 
		}
		/*
		for (JSONObject data : listData){
			Location l = jsonToLocation(data);
			System.out.println(data.toString());
			System.out.println("Accuracy: "+ l.getDate());
			
		}*/
		
		System.out.println("-------------------------------------------------");
		
		ClientResource clientResource = new ClientResource(Method.PUT, "http://locamo.no-ip.org/users/temocas");
		System.out.println("Primer put");
		Representation obj = new JsonRepresentation(getJSONUser());
		System.out.println(obj.getText());
		clientResource.put(obj);
		System.out.println("Segundo put");
		addLocation2(new Location(p,p, j, 56));
		Representation obj2 = new JsonRepresentation(getJSONUser());
		System.out.println(obj2.getText());
		clientResource.put(obj2);
	}
	
	public static String getTimeFormatted(long x){
		Date d = new Date(x);
		System.out.println(d.toString());
		System.out.println(x);
		DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

		return format.format(d);
	}

	public static void addLocation(Location l){
		locationList.add(l);
	}
	
	public static void addLocation2(Location l){
		locationList2.add(l);
	}
	
	public static JSONObject getJSONUser(){
		try{
			JSONObject jsonResult = new JSONObject();
			JSONArray jsonArray = new JSONArray();
			//jsonResult.put("userID", "asfadsf");
			//jsonResult.put("ip", "192.168.1.1");
			for (Location l : locationList){
				jsonArray.put(l.getJSONPosition());
			}
			jsonResult.put("location", jsonArray);
			return jsonResult;
		} catch (JSONException e){
			e.printStackTrace();
			return null;
		}
	}
	public static JSONObject getJSONUser2(){
		try{
			JSONObject jsonResult = new JSONObject();
			JSONArray jsonArray = new JSONArray();
			//jsonResult.put("userID", "asfadsf");
			//jsonResult.put("ip", "192.168.1.1");
			for (Location l : locationList2){
				jsonArray.put(l.getJSONPosition());
			}
			jsonResult.put("location", jsonArray);
			return jsonResult;
		} catch (JSONException e){
			e.printStackTrace();
			return null;
		}
	}
	
	public static Location jsonToLocation(JSONObject json){
		try {
			longitude = json.getDouble("longitude");
			latitude = json.getDouble("latitude");
			accuracy = json.getInt("accuracy");
			time = json.getLong("time");
			date = new Date(json.getLong("time"));
			Location l = new Location(longitude, latitude, time, accuracy );
			return l;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		
	}

}
