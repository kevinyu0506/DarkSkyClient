package com.chuntingyu.darkskyclient.activities.main;

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
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chuntingyu.darkskyclient.applications.BaseActivity;
import com.chuntingyu.darkskyclient.applications.WeatherApp;
import com.chuntingyu.darkskyclient.R;
import com.chuntingyu.darkskyclient.applications.SplashActivity;
import com.chuntingyu.darkskyclient.events.ErrorEvent;
import com.chuntingyu.darkskyclient.events.WeatherEvent;
import com.chuntingyu.darkskyclient.models.Hourly;
import com.chuntingyu.darkskyclient.models.Weather;
import com.chuntingyu.darkskyclient.network.WeatherNao;
//import com.chuntingyu.darkskyclient.services.WeatherServiceProvider;

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
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;
import retrofit2.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

import com.chuntingyu.darkskyclient.models.Currently;
import com.chuntingyu.darkskyclient.models.DataManager;
import com.gc.materialdesign.views.ButtonRectangle;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

@RuntimePermissions
public class MainActivity extends BaseActivity implements MainMvpView {

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

    @BindView(R.id.mSwipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @BindView(R.id.hourlySummary)
    TextView hourlySummary;

    MainPresenterBase mainPresenter;

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        DataManager dataManager = ((WeatherApp) getApplication()).getDataManager();
        mainPresenter = new MainPresenterBase(dataManager);
        mainPresenter.onAttach(this);

        textViewShow.setText("Welcome " + mainPresenter.getEmailId() + "!");

        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainPresenter.setUserLoggedOut();
            }
        });

        /*
         * Sets up a SwipeRefreshLayout.OnRefreshListener that is invoked when the user
         * performs a swipe-to-refresh gesture.
         */
        mSwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
//                        Log.i(LOG_TAG, "onRefresh called from SwipeRefreshLayout");

                        // This method performs the actual data-refresh operation.
                        // The method calls setRefreshing(false) when it's finished.

                        MainActivityPermissionsDispatcher.showUserLocationWithPermissionCheck(MainActivity.this);
//                        showUserLocation();
                    }
                }
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // NOTE: delegate the permission handling to generated method
        MainActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @NeedsPermission(Manifest.permission.ACCESS_FINE_LOCATION)
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

//                                requestCurrentWeather(location.getLatitude(),location.getLongitude());

                                double lat = location.getLatitude();
                                double lon = location.getLongitude();

                                WeatherNao.getWeather(lat, lon).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Response<Weather>>() {
                                    @Override
                                    public void onNext(Response<Weather> response) {
                                        unsubscribe();
                                        if (response.isSuccessful()) {
                                            Weather weather = response.body();
                                            if (weather != null) {
                                                Currently currently = weather.getCurrently();
                                                Log.e(TAG, "Temperature = " + currently.getTemperature());
//                                                EventBus.getDefault().post(new WeatherEvent(weather));

                                                tempTextView.setText(String.valueOf(tempConverter(currently.getTemperature())) + "\u00b0C");
                                                summaryTextView.setText(currently.getSummary());

                                                Hourly hourly = weather.getHourly();
                                                hourlySummary.setText(hourly.getSummary());

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

                                            } else {
                                                Log.e(TAG, "No response, check your key");
//                                                EventBus.getDefault().post(new ErrorEvent("Unable to connect weather server"));
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCompleted() {

                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        Log.e(TAG, "onFailure, unable to get weather data");
//                                        Toast.makeText("Unable to connect weather server", Toast.LENGTH_SHORT).show();
//                                        EventBus.getDefault().post(new ErrorEvent("Unable to connect weather server"));
                                    }
                                });

                                if (mSwipeRefreshLayout.isRefreshing()) {
                                    mSwipeRefreshLayout.setRefreshing(false);
                                }


                            } catch (IOException e) {

                            }
                        }
                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();
//        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void openSplashActivity() {
        Intent intent = SplashActivity.getStartIntent(this);
        startActivity(intent);
        finish();
    }

    private int tempConverter(Double temp) {
        return (int) Math.round((temp - 32) * (5.0 / 9.0));
    }

}
