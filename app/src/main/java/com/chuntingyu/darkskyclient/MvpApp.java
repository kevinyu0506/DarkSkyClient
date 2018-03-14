package com.chuntingyu.darkskyclient;

import android.app.Application;

import com.chuntingyu.darkskyclient.models.DataManager;
import com.chuntingyu.darkskyclient.models.SharedPrefsHelper;

/**
 * Created by Kevin on 2018/3/14.
 */

public class MvpApp extends Application {

    DataManager dataManager;

    @Override
    public void onCreate() {
        super.onCreate();

        SharedPrefsHelper sharedPrefsHelper = new SharedPrefsHelper(getApplicationContext());
        dataManager = new DataManager(sharedPrefsHelper);

    }

    public DataManager getDataManager() {
        return dataManager;
    }

}