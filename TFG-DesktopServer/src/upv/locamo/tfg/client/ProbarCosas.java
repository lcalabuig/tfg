package upv.locamo.tfg.client;

import java.util.Calendar;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



public class ProbarCosas {
	
	static String urlComplete = "www.something.com 8812";
	static String url;
	static String port;
	static float time;


	public static void main (String[] args) throws Exception{
		
		Date date = new Date(System.currentTimeMillis());
		//System.out.println(date.get);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		System.out.println(calendar.get(Calendar.DAY_OF_MONTH));
		System.out.println(calendar.get(Calendar.MONTH));
		System.out.println(calendar.get(Calendar.YEAR));
		
		
		
		/*
		System.out.println(urlComplete);
		doSomething();
		System.out.println(url);
		System.out.println(port);
		
		System.out.println(Distance.home.getLongitude());
		System.out.println(Distance.home.getLatitude());
		*/
		/*
		JSONObject json = createJSONArray();

		ClientResource resource = new ClientResource("http://localhost:8284/users/locamo");
		Representation representation = new JsonRepresentation(json);
		resource.put(representation);
		*/
/*
		ClientResource clientResource = new ClientResource("http://localhost:8284/shoppingList");
		Representation rep = clientResource.get();
		JSONObject jsonRep = (new JsonRepresentation(rep)).getJsonObject();
		JSONArray jsonArray = new JSONArray();
		jsonArray = jsonRep.getJSONArray("shopping_list");
		System.out.println("El tamño del array es: " + jsonArray.length());
		
		JSONObject jsonRep2 = new JSONObject();
        JSONArray jsonArray2 = new JSONArray();
        jsonArray2.put("pollo");
        jsonArray2.put("champiñones");
        jsonRep2.put("shopping_list", jsonArray2);
        Representation rep2 = new JsonRepresentation(jsonRep2);
        clientResource.put(rep2);
        */
        //ClientResource clientResource2 = new ClientResource("http://localhost:8284/shoppingList/perejil");
        //clientResource2.delete();
        
	}
	
	public static void doSomething(){
		String[] parts = urlComplete.split(" ");
		url = parts[0];
		port = parts[1];
		
	}
	
	public static JSONObject createJSONArray(){
		try{
		JSONArray jsonLocation = new JSONArray();
        JSONObject jsonObjLocation = new JSONObject();
        jsonObjLocation.put("longitude", -0.658936969);
        jsonObjLocation.put("latitude", 38.717133336);
        jsonObjLocation.put("accuracy", 60);
        time = System.currentTimeMillis();
        jsonObjLocation.put("time", time);
        jsonLocation.put(jsonObjLocation);

        JSONObject jsonResult = new JSONObject();
        jsonResult.put("location", jsonLocation);

        return jsonResult;
		} catch(JSONException e){
			e.printStackTrace();
			return null;
		}
	}
}
