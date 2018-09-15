package com.chuntingyu.darkskyclient.activities.login;

import com.chuntingyu.darkskyclient.applications.BaseMvpView;

/**
 * Created by Kevin on 2018/3/14.
 */

public interface LoginMvpView extends BaseMvpView {

    void openMainActivity();

    void onLoginButtonClick();
}