package upv.locamo.tfg.smarthome.app;

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.ExecutionException;


public class ExamplePlacesActivity extends ActionBarActivity {

    TextView tv_result1;
    TextView tv_result2;

    String latitude = Double.toString(38.717372);
    String longitude = Double.toString(-0.6590808);

    JSONObject jsonResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example_places);

        tv_result1 = (TextView) findViewById(R.id.tv_result1);
        tv_result2 = (TextView) findViewById(R.id.tv_result2);

        WebServiceTask webServiceTask = new WebServiceTask();
        try {
            jsonResult = webServiceTask.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        try {
            JSONArray jsonArray = jsonResult.getJSONArray("results");
            //for (int i = 0; i < jsonArray.length(); i++) {
            //for (int i = 0; i < 3; i++) {
                JSONObject result = (JSONObject) jsonArray.get(0);
                JSONObject geometry = result.getJSONObject("geometry");
                JSONObject location = geometry.getJSONObject("location");
                double lat = location.getDouble("lat");
                double lng = location.getDouble("lng");
                String name = result.getString("name");
                tv_result1.setText("Lat: " + lat + " Lng: " + lng + "Name: " + name);

            JSONObject result1 = (JSONObject) jsonArray.get(1);
            JSONObject geometry1 = result1.getJSONObject("geometry");
            JSONObject location1 = geometry1.getJSONObject("location");
            double lat1 = location1.getDouble("lat");
            double lng1 = location1.getDouble("lng");
            String name1 = result1.getString("name");
            tv_result1.setText("Lat: " + lat1 + " Lng: " + lng1 + "Name: " + name1);

            //}
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.example_places, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
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
                Log.e("!!!!INFO", "Antes de hacer la llamada");
                String url = "https://maps.googleapis.com/maps/api/place/search/json" +
                        "?types=grocery_or_supermarket" +
                        "&location=38.717372,-0.6590808" +
                        "&radius=5000" +
                        "&sensor=false" +
                        "&key=AIzaSyBwHLscORtSkd_ypEQctkZGizj_rsMNe5U";

                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(url);
                HttpResponse httpResponse = httpClient.execute(httpGet);
                Log.e("!!!!INFO", "Después de hacer la llamada");
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

    private void jsonToPlace(JSONObject jsonResult){
        try {
            JSONObject geometry = jsonResult.getJSONObject("geometry");
            JSONObject location = geometry.getJSONObject("location");
            double lat = location.getDouble("lat");
            double lng = location.getDouble("lng");
            String name = jsonResult.getString("name");

        } catch (JSONException e){
            e.printStackTrace();
        }
    }

}
