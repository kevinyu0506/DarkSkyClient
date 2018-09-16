package com.chuntingyu.weather.applications;

import com.chuntingyu.weather.applications.BaseMvpView;

/**
 * Created by Kevin on 2018/3/14.
 */

public interface BaseMvpPresenter<V extends BaseMvpView> {

    void onAttach(V mvpView);

}