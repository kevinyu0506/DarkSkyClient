package com.chuntingyu.darkskyclient.activities.main;

import com.chuntingyu.darkskyclient.applications.BaseMvpView;

/**
 * Created by Kevin on 2018/3/14.
 */

public interface MainMvpView extends BaseMvpView {

    void openSplashActivity();

//    boolean checkPermission();

//    void requestPermission();

    void showUserLocation();
}