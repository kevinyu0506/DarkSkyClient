package com.chuntingyu.darkskyclient.activities.main;

import com.chuntingyu.darkskyclient.applications.BaseMvpPresenter;

/**
 * Created by Kevin on 2018/3/14.
 */

public interface MainBaseMvpPresenter<V extends MainMvpView> extends BaseMvpPresenter<V> {

    String getEmailId();

    void setUserLoggedOut();

    void getWeather();

    void showUserLocation();

//    void decideToRequestPermission();

}