package com.vaibhav.varun.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
//import android.preference.PreferenceManager;

public class SharedPreferenceHelper {

    private static SharedPreferenceHelper sharedPreferenceHelper;
    private static final String PREFS_NAME = "to_do_prefs";
    public static final String IS_FIRST_RUN = "is_first_run";

    public static SharedPreferenceHelper getInstance() {
        if (sharedPreferenceHelper == null) {
            sharedPreferenceHelper = new SharedPreferenceHelper();
        }
        return sharedPreferenceHelper;
    }

    private SharedPreferenceHelper() {
        super();
    }

    public void saveStringValue(Context context, String text, String Key) {
        SharedPreferences settings;
        Editor editor;
        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE); //1
        editor = settings.edit(); //2
        editor.putString(Key, text); //3
        editor.apply(); //4
    }

    public String getStringValue(Context context, String Key) {
        SharedPreferences settings;
        String text = "";
        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        text = settings.getString(Key, "");
        return text;
    }

    public void saveBooleanValue(Context context, boolean text, String Key) {
        SharedPreferences settings;
        Editor editor;
        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE); //1
        editor = settings.edit(); //2
        editor.putBoolean(Key, text); //3
        editor.apply(); //4
    }

    public boolean getBooleanValue(Context context, String Key) {
        SharedPreferences settings;
        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return settings.getBoolean(Key, true);
    }

    public void clearSharedPreference(Context context) {
        SharedPreferences settings;
        Editor editor;
        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = settings.edit();
        editor.clear();
        editor.apply();
    }

    public void removeValue(Context context, String value) {
        SharedPreferences settings;
        Editor editor;
        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = settings.edit();
        editor.remove(value);
        editor.apply();
    }
}