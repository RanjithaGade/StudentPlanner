package planner.school.com.myschoolplanner.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by Ranjitha on 11/30/2015.
 */
public class AppSharedPreference {

    public static String SETTINGS = "AppSettings";
    public static String CLASSES_KEY = "classes";
    public static SharedPreferences sSharedPreferences = null;


    public static void setClassesArray(Context context, String value) {
        sSharedPreferences = getPrefs(context);
        SharedPreferences.Editor editor = sSharedPreferences.edit();
        editor.putString(CLASSES_KEY, value);
        editor.commit();
    }

    public static String getClassesArray(Context context) {
        String access_token = "";
        // Uh... what??? how about "return getPrefs(context).getString(ACCESS_TOKEN, "")"?
        try {
            sSharedPreferences = getPrefs(context);
            if (sSharedPreferences != null) {
                access_token = sSharedPreferences.getString(CLASSES_KEY, "[]");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            return access_token;
        }
    }

    public static SharedPreferences getPrefs(Context context) {
        if(sSharedPreferences == null) {
            sSharedPreferences = context.getSharedPreferences(SETTINGS, 0);

        }

        return sSharedPreferences;
    }

}
