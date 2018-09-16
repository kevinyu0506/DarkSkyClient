package com.chuntingyu.weather.applications;

import com.chuntingyu.weather.tools.coredata.DataManager;

/**
 * Created by Kevin on 2018/3/14.
 */
public class BasePresenterBase<V extends BaseMvpView> implements BaseMvpPresenter<V> {
    private V mvpView;
    private DataManager dataManager;

    public BasePresenterBase(DataManager dataManager){
        this.dataManager = dataManager;
    }

    @Override
    public void onAttach(V mvpView) {
        this.mvpView = mvpView;
    }

    public V getMvpView() {
        return mvpView;
    }

    public DataManager getDataManager() {
        return dataManager;
    }
}