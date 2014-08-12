package upv.locamo.tfg.smarthome.app;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
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
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.ExecutionException;

import upv.locamo.tfg.smarthome.app.utils.Utils;


public class LoginActivity extends ActionBarActivity {

    Button btn_register;
    EditText et_user;
    EditText et_pass;
    String user;
    String pass;
    String url = "http://locamo.no-ip.org:8284/users/";

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
                //VERIRY INTERNET CONNECTION
                //if ()
                if (!user.matches("") && !pass.matches("")) {
                    sendUserToServer();
                    if (ifUserIsAdded()) {
                        updateSharedPreferences();
                        goToMainActivity();
                    } else {
                        Toast.makeText(getApplicationContext(), "Please enter a valid username/password", Toast.LENGTH_SHORT).show();
                    }
                } else {
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


    private void goToMainActivity() {
        Intent mainActivity = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(mainActivity);
    }

    private void updateSharedPreferences() {
        Utils.setUser(getApplicationContext(), user);
    }

    /**
     * Method called on onCreate() that calls the corresponding AsyncTask to send the public IP
     */
    public void sendUserToServer() {
        if (!userExists()) {
            SendUserToServerTask sendUsertoServerTask = new SendUserToServerTask();
            sendUsertoServerTask.execute();
        }
    }

    /**
     * AsyncTask for send the new user to server
     */
    private class SendUserToServerTask extends AsyncTask<Void, Void, Void> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /*progressDialog = new ProgressDialog(LoginActivity.this);
            progressDialog.setTitle("Connecting with server");
            progressDialog.setMessage("Please wait");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(false);
            progressDialog.show();*/
        }

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

        @Override
        protected void onPostExecute(Void result) {
            //progressDialog.dismiss();
        }
    }

    private boolean userExists() {
        boolean retval = false;
        try {
            CheckIfUserExistsTask checkIfUserExistsTask = new CheckIfUserExistsTask();
            Log.e("!!!INFO", "La url es: " + url + user);
            retval = checkIfUserExistsTask.execute(url + user).get();
            Log.e("!!!INFO", "El usuario existe? " + Boolean.toString(retval));
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return retval;
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
                } else {
                    return true;
                }
            } catch (IOException e) {
                return false;
            }

        }

    }

    private boolean ifUserIsAdded() {
        return userExists();
    }

}
