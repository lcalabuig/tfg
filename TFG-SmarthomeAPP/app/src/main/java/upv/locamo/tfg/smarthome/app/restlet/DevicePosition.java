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

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

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
import upv.locamo.tfg.smarthome.app.R;
import upv.locamo.tfg.smarthome.app.utils.Utils;

public class DevicePosition {

    private static LocationManager locationManager;
    private static LocationListener locationListener;
    public static Location location = null;
    public static double longitude;
    public static double latitude;
    public static float accuracy;
    public static long time;
    private LatLng CURRENT_LOCATION;
    private Marker currentPositionMarker = null;

    private long minTime = TimeUnit.MINUTES.toMillis(2);

    // flag for GPS status
    static boolean isGPSEnabled = false;
    // flag for network status
    static boolean isNetworkEnabled = false;


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
        if (isGPSEnabled)
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime, 0, locationListener);
        else if (isNetworkEnabled)
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, minTime, 0, locationListener);
        else
            Toast.makeText(MainActivity.getContext(), "Please enable GPS", Toast.LENGTH_SHORT).show();

    }

    private class SendPositionToServerTask extends AsyncTask<JSONObject, Void, Void> {
        @Override
        protected Void doInBackground(JSONObject... jsonObjs) {
            Log.e("!!!INFO", "SendPositionToServer task");
            ClientResource resource = new ClientResource("http://locamo.no-ip.org:8284/users/" + MainActivity.getUser());
            Representation obj = new JsonRepresentation(jsonObjs[0]);
            resource.put(obj);
            Log.e("!!!INFO", "PUT sent with the position");
            return null;
        }
    }

    private void showLocation(){
        if (location != null){
            CURRENT_LOCATION = new LatLng(location.getLatitude(), location.getLongitude());

            CameraPosition cameraCurrentPosition = new CameraPosition.Builder()
                    .target(CURRENT_LOCATION)   // Center map at home
                    .zoom(17)                   // Zoom
                    .build();
            CameraUpdate cameraUpdateCurrentPosition =
                    CameraUpdateFactory.newCameraPosition(cameraCurrentPosition);

            MainActivity.map.animateCamera(cameraUpdateCurrentPosition);

            if (currentPositionMarker != null)
                currentPositionMarker.remove();
            currentPositionMarker = MainActivity.map.addMarker(new MarkerOptions()
                    .position(CURRENT_LOCATION)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.position)));
        }
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


