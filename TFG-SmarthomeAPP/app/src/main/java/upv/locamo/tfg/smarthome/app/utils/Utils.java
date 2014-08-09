package upv.locamo.tfg.smarthome.app.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class Utils {

    private static SharedPreferences prefs;
    private static SharedPreferences.Editor editor;
    private static String user;

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

}
