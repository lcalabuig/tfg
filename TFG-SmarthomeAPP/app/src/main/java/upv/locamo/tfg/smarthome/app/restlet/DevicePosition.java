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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import upv.locamo.tfg.smarthome.app.MainActivity;
import upv.locamo.tfg.smarthome.app.utils.Utils;

public class DevicePosition {

    private static LocationManager locationManager;
    private static LocationListener locationListener;
    public static Location location = null;
    public static double longitude;
    public static double latitude;
    public static float accuracy;
    public static long time;

    private double minTime = TimeUnit.MINUTES.toMillis(2);

    // flag for GPS status
    static boolean isGPSEnabled = false;
    // flag for network status
    static boolean isNetworkEnabled = false;

    private static String user = Utils.getUser(MainActivity.getContext());



    private JSONObject createJSONLocation() {
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
    public void getCurrentLocation() {
        locationManager = (LocationManager) MainActivity.getContext().getSystemService(Context.LOCATION_SERVICE);
        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (Utils.checkWifiConnection(MainActivity.getContext())){
            isNetworkEnabled = true;
        }

        Log.e("!!!!!INFO", "Entro en getCurrentLocation");
        //Log.e("!!!!!INFO", "La hora en la que entra es " + getTimeFormatted(System.currentTimeMillis()));

        locationListener = new LocationListener() {
            public void onLocationChanged(Location l) {
                location = l;
                longitude = l.getLongitude();
                latitude = l.getLatitude();
                accuracy = l.getAccuracy();
                time = l.getTime();

                showLocation();

                String loc = Double.toString(longitude) + " - " + Double.toString(latitude) + " - " + Float.toString(accuracy) + " - " + getTimeFormatted(time);
                Log.e("!!!INFO", loc);

                SendPositionToServerTask sendPositionToServerTask = new SendPositionToServerTask();
                sendPositionToServerTask.execute(createJSONLocation());
            }

            public void onProviderDisabled(String provider) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }
        };

        if (isNetworkEnabled)
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, TimeUnit.MINUTES.toMillis(2), 0, locationListener);
        if (isGPSEnabled)
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, TimeUnit.MINUTES.toMillis(2), 0, locationListener);

    }

    private class SendPositionToServerTask extends AsyncTask<JSONObject, Void, Void> {
        @Override
        protected Void doInBackground(JSONObject... jsonObjs) {
            Log.e("!!!INFO", "SendPositionToServer task");
            ClientResource resource = new ClientResource("http://locamo.no-ip.org:8284/users/" + user);
            Representation obj = new JsonRepresentation(jsonObjs[0]);
            resource.put(obj);
            Log.e("!!!INFO", "PUT sent with the position");
            return null;
        }
    }

    private void showLocation(){
        MainActivity.tv_longitude.setText("Longitude: " + location.getLongitude());
        MainActivity.tv_latitude.setText("Latitude: " + location.getLatitude());
        MainActivity.tv_accuracy.setText("Accuracy: " + location.getAccuracy());
        MainActivity.tv_date.setText("Date: " + DevicePosition.getTimeFormatted(location.getTime()));
    }

    public static String getTimeFormatted(long x){
        Date d = new Date(x);
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return format.format(d);
    }

    public Location getLocation(){
        return location;
    }
}


