package com.chuntingyu.weather.activities.login;

import com.chuntingyu.weather.applications.BasePresenterBase;
import com.chuntingyu.weather.tools.coredata.DataManager;

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
        getDataManager().setLoggedIn(true);
        getMvpView().openMainActivity();
    }

}