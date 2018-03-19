package com.chuntingyu.darkskyclient.views;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chuntingyu.darkskyclient.MvpApp;
import com.chuntingyu.darkskyclient.R;
import com.chuntingyu.darkskyclient.events.ErrorEvent;
import com.chuntingyu.darkskyclient.events.WeatherEvent;
import com.chuntingyu.darkskyclient.services.WeatherServiceProvider;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.chuntingyu.darkskyclient.models.Currently;
import com.chuntingyu.darkskyclient.models.DataManager;
import com.chuntingyu.darkskyclient.presenters.MainPresenter;
import com.gc.materialdesign.views.ButtonRectangle;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class MainActivity extends BaseActivity implements MainMvpView  {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int MY_PERMISSIONS_FINE_LOCATION = 0;
    private FusedLocationProviderClient mFusedLocationClient;

    @BindView(R.id.tempTextView)
    TextView tempTextView;

    @BindView(R.id.iconImageView)
    ImageView iconImageView;

    @BindView(R.id.summaryTextView)
    TextView summaryTextView;

    @BindView(R.id.textViewShow)
    TextView textViewShow;

    @BindView(R.id.buttonLogout)
    ButtonRectangle buttonLogout;

    @BindView(R.id.userLocation)
    TextView userLocation;

    MainPresenter mainPresenter;

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        requestCurrentWeather(37.8267,-122.4233);

        ButterKnife.bind(this);

        DataManager dataManager = ((MvpApp) getApplication()).getDataManager();
        mainPresenter = new MainPresenter(dataManager);
        mainPresenter.onAttach(this);

        textViewShow.setText("Welcome " + mainPresenter.getEmailId() + "!");

        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainPresenter.setUserLoggedOut();
            }
        });

        mainPresenter.decideToRequestPermission();


    }

    @Override
    public boolean checkPermission() {

        return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;

    }

    @Override
    public void requestPermission() {

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                MY_PERMISSIONS_FINE_LOCATION);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                    showUserLocation();

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

        }
    }

    @Override
    public void showUserLocation() {

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object

                            Geocoder gc = new Geocoder(getApplicationContext(), Locale.ENGLISH);
                            try {
                                Log.e(TAG, "Lat = " + location.getLatitude() + ", Lon = " + location.getLongitude());
                                List<Address> lstAddress = gc.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                String returnAddress = lstAddress.get(0).getAdminArea().toUpperCase();
                                userLocation.setText(returnAddress);

                                requestCurrentWeather(location.getLatitude(),location.getLongitude());


                            } catch (IOException e) {

                            }
                        }
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
        tempTextView.setText(String.valueOf(tempConverter(currently.getTemperature())));

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

    private int tempConverter(Double temp) {
        return  (int)Math.round((temp - 32) * (5.0/9.0));
    }

}
