package com.chuntingyu.darkskyclient.activities.login;

import com.chuntingyu.darkskyclient.applications.BaseMvpPresenter;

/**
 * Created by Kevin on 2018/3/14.
 */

public interface LoginBaseMvpPresenter<V extends LoginMvpView> extends BaseMvpPresenter<V> {

    void startLogin(String emailId);

}