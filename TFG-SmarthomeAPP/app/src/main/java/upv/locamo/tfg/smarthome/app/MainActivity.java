package upv.locamo.tfg.smarthome.app;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.restlet.Component;
import org.restlet.Server;
import org.restlet.data.Method;
import org.restlet.data.Protocol;
import org.restlet.engine.Engine;
import org.restlet.ext.nio.*;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;

import java.io.IOException;
import java.util.logging.Level;

import upv.locamo.tfg.smarthome.app.restletClient.DeviceIP;
import upv.locamo.tfg.smarthome.app.restletServer.AndroidServerApplication;
import upv.locamo.tfg.smarthome.app.utils.Utils;


public class MainActivity extends ActionBarActivity {
    private Component component;
    private TextView tv_ip;
    private TextView tv_result;
    private Button btn_getResource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_ip = (TextView) findViewById(R.id.tv_ip);
        tv_result = (TextView) findViewById(R.id.tv_result);
        btn_getResource = (Button) findViewById(R.id.btn_getResource);
        System.setProperty("java.net.preferIPv6Addresses","false");

        Engine.getInstance().getRegisteredServers().add(new HttpServerHelper(null));
        Engine.setLogLevel(Level.FINEST);
        Log.e("!!!!INFO", "Modificado Engine)");

        //Obtain the IP and send it to the Rest Server
        sendIPtoServer();
        Log.e("!!!!INFO", "Después de sendIPtoServer()");

        //Create a server to send the position
        initServer();
        makeCallToServer();
        /*
        btn_getResource.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


               //Toast.makeText(getApplicationContext(), "Please enter a valid username/password", Toast.LENGTH_SHORT).show();

            }
        });
        */
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
        tv_ip.setText(deviceIP.getLocalIP());
        String user = Utils.getUser(getApplicationContext());
        deviceIP.setUser(user);
        deviceIP.sendIPToServer();
    }

    private void initServer(){
        Log.e("!!!!INFO", "Entro en InitServer");
        InitServerTask initServerTask = new InitServerTask();
        initServerTask.execute();
        Log.e("!!!!INFO", "Salgo de InitServer");
    }

    public class InitServerTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            Log.e("!!!!INFO", "Entro en InitServerTask - doInBackground");
            try {
                component = new Component();
                Log.e("!!!!INFO", "new Component()");
                final Server connector = component.getServers().add(Protocol.HTTP, 8080); //Probar Configurations.localPort
                Log.e("!!!!INFO", "Creado el connector");
                component.getDefaultHost().attach(new AndroidServerApplication(getApplicationContext()));
                Log.e("!!!!INFO", "Se añade el AndroidServerApplication");
                connector.start();
                Log.i("!!!!INFO", "Se inicia el conector");
            } catch (Exception e) {
                Log.e("!!!!ERROR", "Wystąpił błąd podczas uruchamiania usługi");
            }
            Log.e("!!!!INFO", "Salgo de InitServerTask - doInBackground");
            return null;
        }
    }

    private void makeCallToServer(){
        Log.e("!!!!INFO", "Entro en InitServer");
        MakeCallToServerTask makeCallToServerTask = new MakeCallToServerTask();
        makeCallToServerTask.execute();
        Log.e("!!!!INFO", "Salgo de InitServer");
    }

    public class MakeCallToServerTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            try {
                ClientResource clientResource = new ClientResource(Method.GET, "http://localhost:8080/");
                Representation objectRepresentation = clientResource.get();
                final String result = objectRepresentation.getText().toString();
                Log.e("!!!INFO",result);
                runOnUiThread(new Runnable(){
                    public void run(){
                        tv_result.setText(result);
                    }
                });
            } catch (IOException e) {
                Log.e("!!!ERROR", "Error producido al leer del servidor android");
            }

            return null;
        }
    }


}
