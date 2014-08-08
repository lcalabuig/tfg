package upv.locamo.tfg.smarthome.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


public class LogoutActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Delete the current user from the Shared Preferences
        updateSharedPreferences();

        Intent menuActivity = new Intent(LogoutActivity.this, MenuActivity.class);
        startActivity(menuActivity);

    }

    private void updateSharedPreferences(){
        SharedPreferences prefs = getSharedPreferences("tfg_preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("username", "user");
        editor.commit();
    }
}
