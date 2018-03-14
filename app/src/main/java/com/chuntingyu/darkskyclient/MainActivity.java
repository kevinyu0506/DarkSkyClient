package com.chuntingyu.darkskyclient;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chuntingyu.darkskyclient.events.ErrorEvent;
import com.chuntingyu.darkskyclient.events.WeatherEvent;
import com.chuntingyu.darkskyclient.services.WeatherServiceProvider;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import models.Currently;
import models.DataManager;
import presenters.MainPresenter;
import views.BaseActivity;
import views.MainMvpView;

public class MainActivity extends BaseActivity implements MainMvpView {

    private static final String TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.tempTextView)
    TextView tempTextView;

    @BindView(R.id.iconImageView)
    ImageView iconImageView;

    @BindView(R.id.summaryTextView)
    TextView summaryTextView;

    TextView textViewShow;
    Button buttonLogout;
    MainPresenter mainPresenter;

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestCurrentWeather(37.8267,-122.4233);

//        tempTextView = (TextView) findViewById(R.id.tempTextView);
        ButterKnife.bind(this);


        DataManager dataManager = ((MvpApp) getApplication()).getDataManager();
        mainPresenter = new MainPresenter(dataManager);
        mainPresenter.onAttach(this);

        textViewShow = (TextView) findViewById(R.id.textViewShow);

        buttonLogout = (Button) findViewById(R.id.buttonLogout);

        textViewShow.setText(mainPresenter.getEmailId());


        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainPresenter.setUserLoggedOut();
            }
        });


    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onWeatherEvent(WeatherEvent weatherEvent) {

        Currently currently = weatherEvent.getWeather().getCurrently();
        tempTextView.setText(String.valueOf(Math.round(currently.getTemperature())));

        summaryTextView.setText(currently.getSummary());

        Map<String, Integer> iconMap = new HashMap<>();
        iconMap.put("clear-day", R.drawable.ic_clear_day);
        iconMap.put("clear-night", R.drawable.ic_clear_night);
        iconMap.put("rain", R.drawable.ic_rain);
        iconMap.put("snow", R.drawable.ic_snow);
        iconMap.put("sleet", R.drawable.ic_sleet);
        iconMap.put("wind", R.drawable.ic_wind);
        iconMap.put("fog", R.drawable.ic_fog);
        iconMap.put("cloudy", R.drawable.ic_cloudy);
        iconMap.put("partly-cloudy-day", R.drawable.ic_partly_cloudy_day);
        iconMap.put("partly-cloudy-night", R.drawable.ic_partly_cloudy_night);
        iconMap.put("thunderstorm", R.drawable.ic_thunderstorm);


        iconImageView.setImageResource(iconMap.get(currently.getIcon()));

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onErrorEvent(ErrorEvent errorEvent){
        Toast.makeText(this,"Unable to connect weather server", Toast.LENGTH_SHORT).show();
    }

    private void requestCurrentWeather(double lat, double lon) {

        WeatherServiceProvider weatherServiceProvider = new WeatherServiceProvider();
        weatherServiceProvider.getWeather(lat, lon);

    }

    @Override
    public void openSplashActivity() {
        Intent intent = SplashActivity.getStartIntent(this);
        startActivity(intent);
        finish();
    }
}
