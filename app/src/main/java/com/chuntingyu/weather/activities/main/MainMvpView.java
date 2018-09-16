package com.chuntingyu.weather.activities.main;

import com.chuntingyu.weather.applications.BaseMvpView;

/**
 * Created by Kevin on 2018/3/14.
 */

public interface MainMvpView extends BaseMvpView {

    void openSplashActivity();

//    boolean checkPermission();

//    void requestPermission();

    void showUserLocation();
}