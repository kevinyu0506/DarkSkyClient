package com.chuntingyu.darkskyclient.activities.login;

import com.chuntingyu.darkskyclient.applications.BasePresenterBase;
import com.chuntingyu.darkskyclient.models.DataManager;

/**
 * Created by Kevin on 2018/3/14.
 */

public class LoginPresenterBase<V extends LoginMvpView> extends BasePresenterBase<V> implements LoginBaseMvpPresenter<V> {

    public LoginPresenterBase(DataManager dataManager) {
        super(dataManager);
    }

    @Override
    public void startLogin(String emailId) {
        getDataManager().saveEmailId(emailId);
        getDataManager().setLoggedIn();
        getMvpView().openMainActivity();
    }

}