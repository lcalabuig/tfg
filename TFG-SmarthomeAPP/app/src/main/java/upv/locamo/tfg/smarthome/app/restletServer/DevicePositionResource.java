package upv.locamo.tfg.smarthome.app.restletServer;

//JSON object
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
//Restlet
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import upv.locamo.tfg.smarthome.app.MainActivity;
import upv.locamo.tfg.smarthome.app.utils.Utils;

public class DevicePositionResource extends ServerResource {

    LocationManager locationManager;
    LocationListener locationListener;
    private boolean gps_enabled = false;
    private boolean network_enabled = false;
    private double longitude;
    private double latitude;
    private float accuracy;

    @Override
    protected Representation get() throws ResourceException {
        Log.i("Restlet", "Acceding to Device Position Resource.");
        try {
            getLocation();
            JSONArray jsonLocation = new JSONArray();
            JSONObject jsonObjLocation = new JSONObject();
            jsonObjLocation.put("longitude", longitude);
            jsonObjLocation.put("latitude", latitude);
            jsonObjLocation.put("accuracy", accuracy);
            jsonLocation.put(jsonObjLocation);

            JSONObject jsonResult = new JSONObject();
            jsonResult.put("userID", Utils.getUser());
            jsonResult.put("location", jsonLocation);

            return new JsonRepresentation(jsonResult);
        } catch (JSONException e) {
            e.printStackTrace();
            return new StringRepresentation("Cannot create the JSONOject for the position");
        }
    }

    private void getLocation() {
        locationManager = (LocationManager) AndroidServerApplication.getContextApplication().getSystemService(Context.LOCATION_SERVICE);
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                longitude = location.getLongitude();
                latitude = location.getLatitude();
                accuracy = location.getAccuracy();
            }

            public void onProviderDisabled(String provider) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }
        };

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 20, 0, locationListener);

    }

}


