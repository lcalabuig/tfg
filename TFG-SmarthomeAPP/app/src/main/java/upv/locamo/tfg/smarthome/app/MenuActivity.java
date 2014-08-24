package upv.locamo.tfg.smarthome.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import upv.locamo.tfg.smarthome.app.utils.Utils;


public class MenuActivity extends ActionBarActivity {

    SharedPreferences appPreferences;
    boolean isAppInstalled = false;

    private Button btn_start;
    private Button btn_exit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        addShortcut();

        btn_start = (Button) findViewById(R.id.btn_start);
        btn_exit = (Button) findViewById(R.id.btn_exit);

        // Start application
        btn_start.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // If the user is not logged in to the application
                if (Utils.getUser(getApplicationContext()).equals("user")) {
                    Intent loginActivity = new Intent(MenuActivity.this, LoginActivity.class);
                    startActivity(loginActivity);
                }
                else {
                    Intent mainActivity = new Intent(MenuActivity.this, MainActivity.class);
                    startActivity(mainActivity);
                }
                /*
                Intent placesActivity = new Intent(MenuActivity.this, ExamplePlacesActivity.class);
                startActivity(placesActivity);*/
            }
        });

        // Exit
        btn_exit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
                System.exit(0);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    /**
     * Adds shortcut for the application on home screen
     */
    private void addShortcut() {

        appPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        isAppInstalled = appPreferences.getBoolean("isAppInstalled",false);
        if(isAppInstalled==false) {
            Intent shortcutIntent = new Intent(getApplicationContext(),
                    MenuActivity.class);

            shortcutIntent.setAction(Intent.ACTION_MAIN);

            Intent addIntent = new Intent();
            addIntent
                    .putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
            addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "My SmartHome");
            addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
                    Intent.ShortcutIconResource.fromContext(getApplicationContext(),
                            R.drawable.ic_launcher)
            );

            addIntent
                    .setAction("com.android.launcher.action.INSTALL_SHORTCUT");
            getApplicationContext().sendBroadcast(addIntent);

            SharedPreferences.Editor editor = appPreferences.edit();
            editor.putBoolean("isAppInstalled", true);
            editor.commit();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.about) {
            Intent aboutActivity = new Intent(MenuActivity.this, AboutUsActivity.class);
            startActivity(aboutActivity);
        }
        return super.onOptionsItemSelected(item);
    }

}
