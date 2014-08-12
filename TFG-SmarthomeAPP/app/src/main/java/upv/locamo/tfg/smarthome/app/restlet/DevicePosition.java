package upv.locamo.tfg.smarthome.app.restlet;

//JSON object
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
//Restlet
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;

import upv.locamo.tfg.smarthome.app.MainActivity;
import upv.locamo.tfg.smarthome.app.utils.Utils;

public class DevicePosition {

    private static LocationManager locationManager;
    private static LocationListener locationListener;
    public static Location location = null;
    public static Location locationOld = null;
    public static double longitude;
    public static double latitude;
    public static float accuracy;
    public static long time;

    // flag for GPS status
    static boolean isGPSEnabled = false;
    // flag for network status
    static boolean isNetworkEnabled = false;

    private static String user = Utils.getUser(MainActivity.getContext());



    private static JSONObject createJSONLocation() {
        Log.e("Restlet", "Acceding to Device Position Resource.");
        try {
            JSONArray jsonLocation = new JSONArray();
            JSONObject jsonObjLocation = new JSONObject();
            jsonObjLocation.put("longitude", longitude);
            jsonObjLocation.put("latitude", latitude);
            jsonObjLocation.put("accuracy", accuracy);
            jsonObjLocation.put("time", time);
            jsonLocation.put(jsonObjLocation);

            JSONObject jsonResult = new JSONObject();
            jsonResult.put("location", jsonLocation);

            return jsonResult;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void getCurrentLocation(long minTime) {
        locationManager = (LocationManager) MainActivity.getContext().getSystemService(Context.LOCATION_SERVICE);
        Log.e("!!!!!INFO", "Entro en getCurrentLocation");
        locationListener = new LocationListener() {
            public void onLocationChanged(Location l) {
                location = l;
                longitude = l.getLongitude();
                latitude = l.getLatitude();
                accuracy = l.getAccuracy();
                time = l.getTime();
                //showLocation();
                String loc = Double.toString(longitude) + " - " + Double.toString(latitude) + " - " + Float.toString(accuracy) + " - " + Long.toString(time);
                Log.e("!!!INFO", loc);
            }

            public void onProviderDisabled(String provider) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }
        };
        Log.e("!!!INFO", "El valor de refresco es " + Long.toString(minTime));

        //If GPS is enabled
        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (isGPSEnabled)
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime, 0, locationListener);
        //If Network is enabled
        isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (isNetworkEnabled)
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime, 0, locationListener);
        Log.e("!!!INFO", "GPS enabled? " + Boolean.toString(isGPSEnabled) + " Network enabled? " + Boolean.toString(isNetworkEnabled));

    }

    private static class SendPositionToServerTask extends AsyncTask<JSONObject, Void, Void> {
        @Override
        protected Void doInBackground(JSONObject... jsonObjs) {
            Log.d("!!!INFO", "Entro por SendPositionToServer task");
            ClientResource resource = new ClientResource("http://locamo.no-ip.org:8284/users/" + user);
            Representation obj = new JsonRepresentation(jsonObjs[0]);
            resource.put(obj);

            return null;
        }
    }

    public static void showLocation(){
        MainActivity.tv_longitude.setText("Longitude: " + longitude);
        MainActivity.tv_latitude.setText("Latitude: " + latitude);
        MainActivity.tv_accuracy.setText("Accuracy: " + accuracy);
        MainActivity.tv_date.setText("Date: " + MainActivity.getTimeFormatted(time));
    }
}


