package com.chuntingyu.weather.applications;

import com.chuntingyu.weather.tools.coredata.DataManager;

/**
 * Created by Kevin on 2018/3/14.
 */

public class SplashPresenterBase<V extends SplashMvpView> extends BasePresenterBase<V> implements SplashBaseMvpPresenter<V> {

    public SplashPresenterBase(DataManager dataManager) {
        super(dataManager);
    }

    @Override
    public void decideNextActivity() {
        if (getDataManager().getLoggedInMode()) {
            getMvpView().openMainActivity();
        } else {
            getMvpView().openLoginActivity();
        }
    }
}