package com.chuntingyu.darkskyclient.views;

/**
 * Created by Kevin on 2018/3/14.
 */

public interface MainMvpView extends MvpView {

    void openSplashActivity();

    boolean checkPermission();

    void requestPermission();

    void showUserLocation();
}