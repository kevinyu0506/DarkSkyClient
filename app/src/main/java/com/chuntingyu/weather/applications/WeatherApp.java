package com.chuntingyu.weather.applications;

import android.app.Application;
import android.content.Context;

import com.chuntingyu.weather.tools.coredata.DataManager;
import com.chuntingyu.weather.tools.coredata.LocationHelper;
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
        LocationHelper locationHelper = new LocationHelper(getContext());
        dataManager = new DataManager(sharedPreferencesHelper, locationHelper);
    }

    public DataManager getDataManager() {
        return dataManager;
    }

    public static Context getContext() {
        return WeatherApp.context;
    }
}