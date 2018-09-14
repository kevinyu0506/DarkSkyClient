package com.chuntingyu.darkskyclient.applications;

import com.chuntingyu.darkskyclient.models.DataManager;

/**
 * Created by Kevin on 2018/3/14.
 */
public class BasePresenterBase<V extends BaseMvpView> implements BaseMvpPresenter<V> {

    private V mMvpView;

    DataManager mDataManager;


    public BasePresenterBase(DataManager dataManager){
        mDataManager = dataManager;
    }

    @Override
    public void onAttach(V mvpView) {
        mMvpView = mvpView;
    }

    public V getMvpView() {
        return mMvpView;
    }

    public DataManager getDataManager() {
        return mDataManager;
    }
}