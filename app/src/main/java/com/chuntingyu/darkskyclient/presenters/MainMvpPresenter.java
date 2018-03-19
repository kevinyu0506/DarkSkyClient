package com.chuntingyu.darkskyclient.presenters;

import com.chuntingyu.darkskyclient.views.MainMvpView;

/**
 * Created by Kevin on 2018/3/14.
 */

public interface MainMvpPresenter<V extends MainMvpView> extends MvpPresenter<V> {

    String getEmailId();

    void setUserLoggedOut();

    void decideToRequestPermission();

}