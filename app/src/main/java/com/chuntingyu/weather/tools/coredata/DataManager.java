package com.chuntingyu.weather.tools.coredata;

/**
 * Created by Kevin on 2018/3/14.
 */

public class DataManager {

    private SharedPreferencesHelper sharedPreferencesHelper;

    public DataManager(SharedPreferencesHelper sharedPreferencesHelper) {
        this.sharedPreferencesHelper = sharedPreferencesHelper;
    }

    public void clear() {
        sharedPreferencesHelper.clear();
    }

    public void saveEmailId(String email) {
        sharedPreferencesHelper.putEmail(email);
    }

    public String getEmailId() {
        return sharedPreferencesHelper.getEmail();
    }

    public void setLoggedIn(boolean b) {
        sharedPreferencesHelper.putLoggedInMode(b);
    }

    public Boolean getLoggedInMode() {
        return sharedPreferencesHelper.getLoggedInMode();
    }
}