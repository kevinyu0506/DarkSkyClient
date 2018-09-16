package com.chuntingyu.weather.activities.main;

import com.chuntingyu.weather.applications.BasePresenterBase;
import com.chuntingyu.weather.tools.coredata.DataManager;

/**
 * Created by Kevin on 2018/3/14.
 */

public class MainPresenterBase<V extends MainMvpView> extends BasePresenterBase<V> implements MainBaseMvpPresenter<V> {

    public MainPresenterBase(DataManager dataManager) {
        super(dataManager);
    }

    @Override
    public String getEmailId() {
        return getDataManager().getEmailId();
    }

    @Override
    public void setUserLoggedOut() {
        getDataManager().clear();
        getMvpView().openSplashActivity();
    }

    @Override
    public void getWeather() {

    }

    @Override
    public void showUserLocation() {

    }

    //    @Override
//    public void decideToRequestPermission() {
//        if (!getMvpView().checkPermission()){
//            getMvpView().requestPermission();
//        } else {
//            getMvpView().showUserLocation();
//        }
//    }
}