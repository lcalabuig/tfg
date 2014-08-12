package upv.locamo.tfg.smarthome.app;


import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import upv.locamo.tfg.smarthome.app.restlet.DeviceIP;
import upv.locamo.tfg.smarthome.app.restlet.DevicePosition;
import upv.locamo.tfg.smarthome.app.utils.Utils;


public class MainActivity extends ActionBarActivity {
    private String ip;
    private String user;
    private static Context context;

    //GPS status
    boolean isGPSEnabled = false;
    //Network status
    boolean isNetworkEnabled = false;

    private LocationManager locationManager;
    private LocationListener locationListener;
    private Location location = null;
    private double longitude;
    private double latitude;
    private float accuracy;
    private long time;

    private TextView tv_ip;
    public static TextView tv_longitude;
    public static TextView tv_latitude;
    public static TextView tv_accuracy;
    public static TextView tv_date;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_ip = (TextView) findViewById(R.id.tv_ip);
        tv_longitude = (TextView) findViewById(R.id.tv_longitude);
        tv_latitude = (TextView) findViewById(R.id.tv_latitude);
        tv_accuracy = (TextView) findViewById(R.id.tv_accuracy);
        tv_date = (TextView) findViewById(R.id.tv_date);

        context = getApplicationContext();
        System.setProperty("java.net.preferIPv6Addresses","false");
        //
        // getConnectionsEnabled();

        //Obtain the IP and send it to the Rest Server
        sendIPtoServer();
        tv_ip.setText(ip);

        getCurrentLocation();


    }

    public static Context getContext(){
        return context;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.about) {
            Intent aboutActivity = new Intent(MainActivity.this, AboutUsActivity.class);
            startActivity(aboutActivity);
        }
        if (id == R.id.logout) {
            Intent logoutActivity = new Intent(MainActivity.this, LogoutActivity.class);
            startActivity(logoutActivity);
        }
        return super.onOptionsItemSelected(item);

    }

    private void sendIPtoServer(){
        DeviceIP deviceIP = new DeviceIP();
        deviceIP.obtainIPAddress();
        ip = deviceIP.getLocalIP();
        user = Utils.getUser(getApplicationContext());
        deviceIP.setUser(user);
        deviceIP.sendIPToServer();
    }

    public void getCurrentLocation() {
        locationManager = (LocationManager) MainActivity.getContext().getSystemService(Context.LOCATION_SERVICE);
        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        Log.e("!!!!!INFO", "Entro en getCurrentLocation");
        Log.e("!!!!!INFO", "La hora en la que entra es " + getTimeFormatted(System.currentTimeMillis()));

        locationListener = new LocationListener() {
            public void onLocationChanged(Location l) {
                location = l;
                longitude = l.getLongitude();
                latitude = l.getLatitude();
                accuracy = l.getAccuracy();
                time = l.getTime();
                tv_longitude.setText("Longitude: " + longitude);
                tv_latitude.setText("Latitude: " + latitude);
                tv_accuracy.setText("Accuracy: " + accuracy);
                tv_date.setText("Date: " + getTimeFormatted(time));

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
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 30000, 0, locationListener);

        if (isNetworkEnabled)
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 30000, 0, locationListener);

    }

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

    public class SendPositionToServerTask extends AsyncTask<JSONObject, Void, Void> {
        @Override
        protected Void doInBackground(JSONObject... jsonObjs) {
            Log.d("!!!INFO", "Entro por SendPositionToServer task");
            ClientResource resource = new ClientResource("http://locamo.no-ip.org:8284/users/" + user);
            Representation obj = new JsonRepresentation(jsonObjs[0]);
            resource.put(obj);

            return null;
        }
    }

    public static String getTimeFormatted(long x){
        Date d = new Date(x);
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return format.format(d);
    }

}
