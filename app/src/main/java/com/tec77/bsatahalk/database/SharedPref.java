package com.tec77.bsatahalk.database;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Dell-pc on 12/6/2015.
 */
public class SharedPref {

    public static SharedPreferences sharedPreferences;
    private static SharedPref pref;
    public static SharedPreferences.Editor editor;

    public static SharedPref getInstance(Context context) {
        if (pref == null) {
            pref = new SharedPref(context);
        }
        return pref;
    }

    public void clearAllData() {
        editor.clear();
        editor.commit();
    }

    public SharedPref(Context context) {
        sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void putString(String key, String value) {
        editor.putString(key, value);
        editor.commit();
    }

    public void putBoolean(String key, boolean value) {
        editor.putBoolean(key, value);
        editor.commit();
    }

    public void setLoggedin(boolean loggedin) {
        editor.putBoolean("loggedInMode", loggedin);
        editor.commit();
    }

    public Boolean loggeedIn() {
        return sharedPreferences.getBoolean("loggedInMode", false);
    }

    public String getString(String key) {
        return sharedPreferences.getString(key, "");
    }

    public boolean getBoolean(String key) {
        return sharedPreferences.getBoolean(key, false);
    }

    public void putLong(String key, Long value) {
        editor.putLong(key, value);
        editor.commit();
    }

    public Long getLong(String key) {
        return sharedPreferences.getLong(key, 999);
    }

    public void putInt(String key, int value) {
        editor.putInt(key, value);
        editor.commit();
    }

    public int getInt(String key) {
        return sharedPreferences.getInt(key, 0);
    }

}
