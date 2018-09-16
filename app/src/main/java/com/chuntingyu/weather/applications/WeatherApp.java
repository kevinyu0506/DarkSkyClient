package com.chuntingyu.weather.applications;

import android.app.Application;
import android.content.Context;

import com.chuntingyu.weather.tools.coredata.DataManager;
import com.chuntingyu.weather.tools.coredata.SharedPreferencesHelper;

/**
 * Created by Kevin on 2018/3/14.
 */

public class WeatherApp extends Application {

    private static Context context;
    DataManager dataManager;

    @Override
    public void onCreate() {
        super.onCreate();
        WeatherApp.context = getApplicationContext();
        SharedPreferencesHelper sharedPreferencesHelper = new SharedPreferencesHelper(getApplicationContext());
        dataManager = new DataManager(sharedPreferencesHelper);
    }

    public DataManager getDataManager() {
        return dataManager;
    }

    public static Context getContext() {
        return WeatherApp.context;
    }
}