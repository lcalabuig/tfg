package upv.locamo.tfg.smarthome.app;


import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import upv.locamo.tfg.smarthome.app.restlet.DeviceIP;
import upv.locamo.tfg.smarthome.app.restlet.DevicePosition;
import upv.locamo.tfg.smarthome.app.utils.Utils;


public class MainActivity extends FragmentActivity {

    private String ip;
    private static String user;
    private static Context context;

    private Location location;

    public static GoogleMap map;
    private LatLng HOME;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = getApplicationContext();
        System.setProperty("java.net.preferIPv6Addresses","false");

        user = Utils.getUser(context);
        //Obtain the IP and send it to the Rest Server
        //sendIPtoServer();
        //Log.i("INFO", "IP: " + ip);

        getCurrentLocation();

        HOME = new LatLng(38.717338,-0.6591331);

        // Get the map
        map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();

        // Move camera to home location
        CameraPosition cameraPositionHome = new CameraPosition.Builder()
                .target(HOME)   // Center map at home
                .zoom(17)       // Zoom
                .build();
        CameraUpdate cameraUpdateHome =
                CameraUpdateFactory.newCameraPosition(cameraPositionHome);
        map.animateCamera(cameraUpdateHome);

        // Add a marker to map that indicates home location
        map.addMarker(new MarkerOptions()
                .position(HOME)
                .title(getApplicationContext().getString(R.string.home)));

    }

    public static Context getContext(){
        return context;
    }

    public static String getUser(){ return user; }

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
            Intent logoutActivity = new Intent(this, LogoutActivity.class);
            logoutActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                    Intent.FLAG_ACTIVITY_CLEAR_TASK |
                    Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(logoutActivity);
            finish();
        }
        if (id == R.id.shoppingList) {
            Intent shoppingListActivity = new Intent(MainActivity.this, ShoppingListActivity.class);
            startActivity(shoppingListActivity);
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

    private void getCurrentLocation(){
        DevicePosition devicePosition = new DevicePosition();
        devicePosition.getCurrentLocation();
        location = devicePosition.getLocation();
    }

}
