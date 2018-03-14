package com.chuntingyu.darkskyclient.presenters;

import com.chuntingyu.darkskyclient.views.SplashMvpView;

/**
 * Created by Kevin on 2018/3/14.
 */

public interface SplashMvpPresenter<V extends SplashMvpView> extends MvpPresenter<V> {

    void decideNextActivity();

}
