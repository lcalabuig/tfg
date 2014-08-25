package upv.locamo.tfg.smarthome.app.restlet;

//JSON object
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
//Restlet
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;
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
    private JSONObject jsonResult;
    private int notificationID = 1;

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

                SmarthomeShoppingList shoppingList = new SmarthomeShoppingList();
                if (shoppingList.getShoppingList().size() != 0) {
                    showSupermarkets();
                }
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

    private void showSupermarkets(){
        GooglePlacesTask webServiceTask = new GooglePlacesTask();
        try {
            jsonResult = webServiceTask.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        try {
            JSONObject result, geometry, location;
            if (jsonResult != null) {
                JSONArray jsonArray = jsonResult.getJSONArray("results");
                for (int i = 0; i < jsonArray.length(); i++) {
                    result = (JSONObject) jsonArray.get(i);
                    geometry = result.getJSONObject("geometry");
                    location = geometry.getJSONObject("location");
                    Marker supermarket = MainActivity.map.addMarker(new MarkerOptions()
                            .position(new LatLng(location.getDouble("lat"), location.getDouble("lng")))
                            .title(result.getString("name"))
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.shopping_cart)));
                }
                createNotification();
            }

        } catch (JSONException e){
            e.printStackTrace();
        }

    }


    /**
     * AsyncTask for obtain the nearby supermarkets
     */
    public class GooglePlacesTask extends AsyncTask<Void, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(Void... params) {
            InputStream is = null;
            JSONObject jObj = null;
            String json = "";
            // Making HTTP request
            try {
                String url = "https://maps.googleapis.com/maps/api/place/search/json" +
                        "?types=grocery_or_supermarket" +
                        "&location=" + location.getLatitude() + "," + location.getLongitude() +
                        "&radius=300" +
                        "&sensor=false" +
                        "&key=AIzaSyBwHLscORtSkd_ypEQctkZGizj_rsMNe5U";

                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(url);
                HttpResponse httpResponse = httpClient.execute(httpGet);
                HttpEntity httpEntity = httpResponse.getEntity();
                is = httpEntity.getContent();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                is.close();
                json = sb.toString();
            } catch (Exception e) {
                Log.e("Buffer Error", "Error converting result " + e.toString());
            }
            try {
                if (new JSONObject(json).getString("status").equals("OK"))
                    jObj = new JSONObject(json);
            } catch (JSONException e) {
                Log.e("JSON Parser", "Error parsing data " + e.toString());
            }
            return jObj;
        }

    }

    private void createNotification(){
        Vibrator v = (Vibrator) MainActivity.getContext().getSystemService(Context.VIBRATOR_SERVICE);
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(MainActivity.getContext())
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle(MainActivity.getContext().getResources().getString(R.string.app_name))
                        .setContentText(MainActivity.getContext().getResources().getString(R.string.notification));
        //mBuilder.setVibrate(new long[] {300}); // Doesn't work
        v.vibrate(300);
        mBuilder.setLights(Color.WHITE, 800, 800);
        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(MainActivity.getContext(), MainActivity.class);

        // The stack builder object will contain an artificial back stack for the started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(MainActivity.getContext());
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(MainActivity.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) MainActivity.getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
        mNotificationManager.notify(notificationID, mBuilder.build());

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


