package upv.locamo.tfg.smarthome.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import upv.locamo.tfg.smarthome.app.utils.Utils;


public class LogoutActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Delete the current user from the Shared Preferences
        Utils.deleteCurrentUser(MainActivity.getContext());

        Intent menuActivity = new Intent(this, MenuActivity.class);
        menuActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_CLEAR_TASK |
                Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(menuActivity);
        finish();

    }

}
