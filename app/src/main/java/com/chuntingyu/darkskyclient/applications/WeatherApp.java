package com.chuntingyu.darkskyclient.applications;

import android.app.Application;

import com.chuntingyu.darkskyclient.tools.coredata.DataManager;
import com.chuntingyu.darkskyclient.tools.coredata.SharedPreferencesHelper;

/**
 * Created by Kevin on 2018/3/14.
 */

public class WeatherApp extends Application {

    DataManager dataManager;

    @Override
    public void onCreate() {
        super.onCreate();

        SharedPreferencesHelper sharedPreferencesHelper = new SharedPreferencesHelper(getApplicationContext());
        dataManager = new DataManager(sharedPreferencesHelper);
    }

    public DataManager getDataManager() {
        return dataManager;
    }
}