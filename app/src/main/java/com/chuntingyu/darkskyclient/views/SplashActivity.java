package com.chuntingyu.darkskyclient.views;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.chuntingyu.darkskyclient.MvpApp;
import com.chuntingyu.darkskyclient.R;
import com.chuntingyu.darkskyclient.models.DataManager;
import com.chuntingyu.darkskyclient.presenters.SplashPresenter;

public class SplashActivity extends BaseActivity implements SplashMvpView {

    private static final int MY_PERMISSIONS_FINE_LOCATION = 0;
    SplashPresenter mSplashPresenter;

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, SplashActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        DataManager dataManager = ((MvpApp) getApplication()).getDataManager();

        mSplashPresenter = new SplashPresenter(dataManager);

        mSplashPresenter.onAttach(this);

        mSplashPresenter.decideNextActivity();

    }

    @Override
    public void openMainActivity() {
        Intent intent = MainActivity.getStartIntent(this);
        startActivity(intent);
        finish();
    }

    @Override
    public void openLoginActivity() {
        Intent intent = LoginActivity.getStartIntent(this);
        startActivity(intent);
        finish();
    }
}