package com.chuntingyu.darkskyclient.presenters;

import com.chuntingyu.darkskyclient.models.DataManager;
import com.chuntingyu.darkskyclient.views.SplashMvpView;

/**
 * Created by Kevin on 2018/3/14.
 */

public class SplashPresenter<V extends SplashMvpView> extends BasePresenter<V> implements SplashMvpPresenter<V> {

    public SplashPresenter(DataManager dataManager) {
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