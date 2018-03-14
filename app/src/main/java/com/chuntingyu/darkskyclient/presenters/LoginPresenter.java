package com.chuntingyu.darkskyclient.presenters;

import com.chuntingyu.darkskyclient.models.DataManager;
import com.chuntingyu.darkskyclient.views.LoginMvpView;

/**
 * Created by Kevin on 2018/3/14.
 */

public class LoginPresenter<V extends LoginMvpView> extends BasePresenter<V> implements LoginMvpPresenter<V> {

    public LoginPresenter(DataManager dataManager) {
        super(dataManager);
    }

    @Override
    public void startLogin(String emailId) {
        getDataManager().saveEmailId(emailId);
        getDataManager().setLoggedIn();
        getMvpView().openMainActivity();
    }

}