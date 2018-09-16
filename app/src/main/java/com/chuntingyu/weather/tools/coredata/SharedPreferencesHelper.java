package com.chuntingyu.weather.tools.coredata;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Kevin on 2018/3/14.
 */

public class SharedPreferencesHelper {
    private static final String TAG = "SharedPreferencesHelper";
    private static final String EmailKey = "email";
    private static final String loginKey = "login";

    private SharedPreferences sharedPreferences;

    public SharedPreferencesHelper(Context context) {
        sharedPreferences = context.getSharedPreferences(TAG, MODE_PRIVATE);
    }

    public void clear() {
        sharedPreferences.edit().clear().apply();
    }

    public void putEmail(String email) {
        sharedPreferences.edit().putString(EmailKey, email).apply();
    }

    public String getEmail() {
        return sharedPreferences.getString(EmailKey, null);
    }

    public void putLoggedInMode(boolean loggedIn) {
        sharedPreferences.edit().putBoolean(loginKey, loggedIn).apply();
    }

    public boolean getLoggedInMode() {
        return sharedPreferences.getBoolean(loginKey, false);
    }

}
