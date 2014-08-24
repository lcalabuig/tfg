package upv.locamo.tfg.smarthome.app.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.ArrayList;

public class Utils {

    private static SharedPreferences prefs;
    private static SharedPreferences.Editor editor;
    private static String user;
    private static ArrayList<String> shoppingList;

    public static String getUser(){
        return user;
    }

    public static String getUser(Context context){
        prefs = context.getSharedPreferences("tfg_preferences", Context.MODE_PRIVATE);
        user = prefs.getString("username", "user");
        return user;
    }

    public static void setUser(Context context, String u){
        prefs = context.getSharedPreferences("tfg_preferences", Context.MODE_PRIVATE);
        editor = prefs.edit();
        user = u;
        editor.putString("username", u);
        editor.commit();
    }

    public static void deleteCurrentUser(Context context){
        prefs = context.getSharedPreferences("tfg_preferences", Context.MODE_PRIVATE);
        editor = prefs.edit();
        user = "user";
        editor.putString("username", "user");
        editor.commit();
    }

    public static boolean checkWifiConnection(Context context) {
        ConnectivityManager conectManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conectManager != null) {
            NetworkInfo wifiConection = conectManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (wifiConection != null
                    && wifiConection.isAvailable()
                    && wifiConection.getDetailedState() == NetworkInfo.DetailedState.CONNECTED) {
                return true;
            }
        }
        return false;
    }
}
