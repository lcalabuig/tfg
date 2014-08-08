package upv.locamo.tfg.smarthome.app.restletClient;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.ExecutionException;



public class DeviceIP {
    private String localIP;
    private String user;

    public DeviceIP(){
        localIP = null;
    }

    public String getLocalIP(){
        return localIP;
    }

    public void setUser(String u){
        user = u;
    }

    /**
     * Method called on onCreate() that calls the corresponding AsyncTask to obtain the public IP
     */
    public void obtainIPAddress() {
        if (localIP == null) {
            WebServiceTask webServiceTask = new WebServiceTask();
            try {
                JSONObject result = webServiceTask.execute().get();
                localIP = result.getString("ip");

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * AsyncTask for obtain the public IP of the device
     */
    public class WebServiceTask extends AsyncTask<Void, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(Void... params) {
            InputStream is = null;
            JSONObject jObj = null;
            String json = "";
            // Making HTTP request
            try {
                String url = "http://ip2country.sourceforge.net/ip2c.php?format=JSON";
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
                jObj = new JSONObject(json);
            } catch (JSONException e) {
                Log.e("JSON Parser", "Error parsing data " + e.toString());
            }
            return jObj;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            try {
                localIP = json.getString("ip");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Method called on onCreate() that calls the corresponding AsyncTask to send the public IP
     */
    public void sendIPToServer(){
        SendIPtoServerTask sendIPtoServerTask = new SendIPtoServerTask();
        sendIPtoServerTask.execute();
    }

    /**
     * AsyncTask for send to server the IP obtained
     */
    private class SendIPtoServerTask extends AsyncTask<Void, Void, Void>{
        @Override
        protected Void doInBackground(Void... params) {

            ClientResource resource = new ClientResource("http://locamo.no-ip.org/iplist/" + user);
            try {
                JSONObject jsonSend = new JSONObject();
                jsonSend.put("ip", localIP);
                Representation obj = new JsonRepresentation(jsonSend);
                resource.put(obj);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    /*
    public boolean isWifiConnected() {
        boolean retval = false;
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiNetwork != null && wifiNetwork.isConnected()) {
            Log.i("NETWORK INFO", "Connection by WiFi");
            retval = true;
        }
        NetworkInfo mobileNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (mobileNetwork != null && mobileNetwork.isConnected()){
            Log.i("NETWORK INFO", "Connection by data");
            retval = false;
        }
        return retval;
    }
    */
}
