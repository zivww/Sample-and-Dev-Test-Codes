package com.galagala.findchinaapp;

import android.app.Application;
import android.content.SharedPreferences;
import com.galagala.findchinaapp.dbhandler.DataBaseHelper;

public class AppController extends Application {
    private static DataBaseHelper dataBaseHelper;
    private static AppController instace;
    private final String DEFAULT_PREF = "default_pref";
    private SharedPreferences default_prefs;

    public static AppController getInstace() {
        return instace;
    }

    public void onCreate() {
        super.onCreate();
        instace = this;
        this.default_prefs = getSharedPreferences("default_pref", 0);
        dataBaseHelper = new DataBaseHelper(this);
    }

    public static DataBaseHelper getDbHelper() {
        return dataBaseHelper;
    }

    public SharedPreferences getDefaultPreference() {
        return this.default_prefs;
    }
}
