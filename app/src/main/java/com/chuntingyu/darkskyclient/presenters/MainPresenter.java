package com.chuntingyu.darkskyclient.presenters;

import com.chuntingyu.darkskyclient.models.DataManager;
import com.chuntingyu.darkskyclient.views.MainMvpView;

/**
 * Created by Kevin on 2018/3/14.
 */

public class MainPresenter<V extends MainMvpView> extends BasePresenter<V> implements MainMvpPresenter<V> {

    public MainPresenter(DataManager dataManager) {
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

}