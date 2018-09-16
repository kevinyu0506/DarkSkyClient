package com.chuntingyu.weather.applications;

/**
 * Created by Kevin on 2018/3/14.
 */

public interface SplashBaseMvpPresenter<V extends SplashMvpView> extends BaseMvpPresenter<V> {

    void decideNextActivity();

}
