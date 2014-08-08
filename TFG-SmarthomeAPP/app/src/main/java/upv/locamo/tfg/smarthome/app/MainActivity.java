package upv.locamo.tfg.smarthome.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


import upv.locamo.tfg.smarthome.app.restletClient.DeviceIP;


public class MainActivity extends ActionBarActivity {


    private TextView tv_ip;

    private static final int PREFERENCE_MODE_PRIVATE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        System.setProperty("java.net.preferIPv6Addresses","false");

        DeviceIP deviceIP = new DeviceIP();

        deviceIP.obtainIPAddress();
        tv_ip = (TextView) findViewById(R.id.tv_ip);
        tv_ip.setText(deviceIP.getLocalIP());
        String user = getUser();
        deviceIP.setUser(user);
        deviceIP.sendIPToServer();

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
        return super.onOptionsItemSelected(item);

    }

    private String getUser(){
        SharedPreferences prefs = getSharedPreferences("tfg_preferences", MODE_PRIVATE);
        return prefs.getString("username", "user");
    }
}
