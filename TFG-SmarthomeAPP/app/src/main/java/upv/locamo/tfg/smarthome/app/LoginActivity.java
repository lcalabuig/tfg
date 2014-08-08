package upv.locamo.tfg.smarthome.app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.data.MediaType;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.ExecutionException;


public class LoginActivity extends ActionBarActivity {

    Button btn_register;
    EditText et_user;
    EditText et_pass;
    String user;
    String pass;
    String url = "http://locamo.no-ip.org/iplist/";

    private static final int PREFERENCE_MODE_PRIVATE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btn_register = (Button) findViewById(R.id.btn_register);
        et_user = (EditText) findViewById(R.id.et_user);
        et_pass = (EditText) findViewById(R.id.et_password);

        btn_register.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                user = et_user.getText().toString();
                pass = et_pass.getText().toString();
                if (!user.matches("") && !pass.matches("")) {
                    sendUserToServer();
                    if (ifUserIsAdded()) {
                        updateSharedPreferences();
                        goToMainActivity();
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Please enter a valid username/password", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), "Please enter a valid username/password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.about) {
            Intent aboutActivity = new Intent(LoginActivity.this, AboutUsActivity.class);
            startActivity(aboutActivity);
        }
        return super.onOptionsItemSelected(item);
    }


    private void goToMainActivity(){
        Log.e("!!!!INFO", "Entro en goToMainActivity()");
        Intent mainActivity = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(mainActivity);
    }

    private void updateSharedPreferences(){

        Log.e("!!!!INFO", "Entro en updateSharedPreferences()");
        SharedPreferences prefs = getSharedPreferences("tfg_preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("username", user);
        editor.commit();
    }

    /**
     * Method called on onCreate() that calls the corresponding AsyncTask to send the public IP
     */
    public void sendUserToServer(){
        Log.e("!!!!INFO", "Entro en sendUserToServer()");
        if (!userExists()) {
            SendUsertoServerTask sendUsertoServerTask = new SendUsertoServerTask();
            sendUsertoServerTask.execute();
        }
    }

    /**
     * AsyncTask for send to server the new user
     */
    private class SendUsertoServerTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            ClientResource resource = new ClientResource(url);
                try {
                    JSONObject jsonSend = new JSONObject();
                    jsonSend.put("userID", user);
                    jsonSend.put("ip", "null");
                    jsonSend.put("pass", pass);
                    Representation obj = new JsonRepresentation(jsonSend);
                    resource.put(obj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            return null;
        }
    }

    /**
     * AsyncTask for send to server the new user
     */
    private class CheckIfUserExistsTask extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {
            try {
                URL url = new URL(params[0]);
                URLConnection connection = url.openConnection();
                connection.connect();
                HttpURLConnection httpConnection = (HttpURLConnection) connection;
                int code = httpConnection.getResponseCode();
                if (code != 200) {
                    return false;
                }
                else {
                    return true;
                }
            } catch (IOException e) {
                return false;
            }

        }

    }

    private boolean userExists() {
        boolean retval = false;
        try {
            CheckIfUserExistsTask checkIfUserExistsTask = new CheckIfUserExistsTask();
            retval = checkIfUserExistsTask.execute(url+user).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return retval;
    }

    private boolean ifUserIsAdded(){
        return userExists();
    }

}
