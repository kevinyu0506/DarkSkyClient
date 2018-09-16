package com.chuntingyu.darkskyclient.activities.main;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chuntingyu.darkskyclient.applications.BaseActivity;
import com.chuntingyu.darkskyclient.applications.SplashActivity;
import com.chuntingyu.darkskyclient.applications.WeatherApp;
import com.chuntingyu.darkskyclient.R;
import com.chuntingyu.darkskyclient.models.Daily;
import com.chuntingyu.darkskyclient.models.Data;
import com.chuntingyu.darkskyclient.models.Hourly;
import com.chuntingyu.darkskyclient.models.Weather;
import com.chuntingyu.darkskyclient.network.WeatherNao;
//import com.chuntingyu.darkskyclient.services.WeatherServiceProvider;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;
import retrofit2.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

import com.chuntingyu.darkskyclient.models.Currently;
import com.chuntingyu.darkskyclient.tools.CommonUtils;
import com.chuntingyu.darkskyclient.tools.IconHelper;
import com.chuntingyu.darkskyclient.tools.KYMath;
import com.chuntingyu.darkskyclient.tools.coredata.DataManager;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

@RuntimePermissions
public class MainActivity extends BaseActivity implements MainMvpView {
    private static final String TAG = "MainActivity";
    //    private static final int MY_PERMISSIONS_FINE_LOCATION = 0;
    private FusedLocationProviderClient fusedLocationClient;
    private double lat;
    private double lon;
    private List<Data> dailyDatas = new ArrayList<>();
    private MainPresenterBase mainPresenter;
    private LayoutInflater inflater;
    private DailyReportAdapter dailyReportAdapter = new DailyReportAdapter();

    @BindView(R.id.tempTextView)
    TextView tempTextView;
    @BindView(R.id.iconImageView)
    ImageView iconImageView;
    @BindView(R.id.summaryTextView)
    TextView summaryTextView;
    @BindView(R.id.textViewShow)
    TextView textViewShow;
    @BindView(R.id.buttonLogout)
    LinearLayout buttonLogout;
    @BindView(R.id.userLocation)
    TextView userLocation;
    @BindView(R.id.mSwipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.hourlySummary)
    TextView hourlySummary;
    @BindView(R.id.center_recycler_view)
    RecyclerView recyclerView;

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_center);

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

        MainActivityPermissionsDispatcher.showUserLocationWithPermissionCheck(this);
        MainActivityPermissionsDispatcher.getWeatherWithPermissionCheck(this);

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
                        MainActivityPermissionsDispatcher.getWeatherWithPermissionCheck(MainActivity.this);
                    }
                }
        );

        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        recyclerView.setAdapter(dailyReportAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // NOTE: delegate the permission handling to generated method
        MainActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @NeedsPermission(Manifest.permission.INTERNET)
    public void getWeather() {
        WeatherNao.getWeather(lat, lon).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Response<Weather>>() {
            @Override
            public void onNext(Response<Weather> response) {
                unsubscribe();
                if (response.isSuccessful()) {
                    Weather weather = response.body();
                    if (weather != null) {
                        Currently currently = weather.getCurrently();
                        Log.e(TAG, "Temperature = " + currently.getTemperature());

                        tempTextView.setText(String.valueOf(CommonUtils.tempConverter(currently.getTemperature())) + "\u00b0C");
                        summaryTextView.setText(currently.getSummary());

                        Hourly hourly = weather.getHourly();
                        hourlySummary.setText(hourly.getSummary());

                        dailyDatas = weather.getDaily().getData();
                        dailyReportAdapter.notifyDataSetChanged();

                        Integer iconResource = IconHelper.getIconResource(currently.getIcon());
                        iconImageView.setImageResource(iconResource);
                    } else {
                        Log.e(TAG, "No response, check your key");
                    }
                }
            }

            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "onFailure, unable to get weather data");
//                Toast.makeText("Unable to connect weather server", Toast.LENGTH_SHORT).show();
//                EventBus.getDefault().post(new ErrorEvent("Unable to connect weather server"));
            }
        });
    }

    @NeedsPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    @Override
    public void showUserLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object
                            lat = location.getLatitude();
                            lon = location.getLongitude();
                            Geocoder gc = new Geocoder(getApplicationContext(), Locale.ENGLISH);
                            try {
                                Log.e(TAG, "Lat = " + location.getLatitude() + ", Lon = " + location.getLongitude());
                                List<Address> lstAddress = gc.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                String returnAddress = lstAddress.get(0).getAdminArea().toUpperCase();
                                userLocation.setText(returnAddress);

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
    public void openSplashActivity() {
        Intent intent = SplashActivity.getStartIntent(this);
        startActivity(intent);
        finish();
    }

    private class DailyReportAdapter extends RecyclerView.Adapter<DailyReportViewHolder> {
        @Override
        public DailyReportViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = inflater.inflate(R.layout.cell_daily_report, parent, false);
            return new DailyReportViewHolder(view);
        }

        @Override
        public void onBindViewHolder(DailyReportViewHolder holder, int position) {
            Data dailyData = dailyDatas.get(position);
            holder.setIcon(dailyData.getIcon());
            holder.setSummary(dailyData.getSummary());
            int tempHigh = CommonUtils.tempConverter(dailyData.getTemperatureHigh());
            holder.setTempHigh(String.valueOf(tempHigh));
            int tempLow = CommonUtils.tempConverter(dailyData.getTemperatureLow());
            holder.setTempLow(String.valueOf(tempLow));
            holder.setWeekday("MONDAY");
        }

        @Override
        public int getItemCount() {
            return dailyDatas.size();
        }
    }

    public class DailyReportViewHolder extends RecyclerView.ViewHolder {
        View root;
        ImageView icon;
        TextView summary;
        TextView tempHigh;
        TextView tempLow;
        TextView weekday;

        public DailyReportViewHolder(View view) {
            super(view);
            root = view.findViewById(R.id.cell_daily_report_root);
            icon = view.findViewById(R.id.cell_daily_report_icon);
            summary = view.findViewById(R.id.cell_daily_report_summary);
            tempHigh = view.findViewById(R.id.cell_daily_report_temp_high);
            tempLow = view.findViewById(R.id.cell_daily_report_temp_low);
            weekday = view.findViewById(R.id.cell_daily_report_weekday);

            root.getLayoutParams().height = KYMath.screenSize().y * 280/667;
            root.getLayoutParams().width = KYMath.screenSize().x * 120/375;
        }

        public void setIcon(String resource) {
            Integer imageResource = IconHelper.getIconResource(resource);
            this.icon.setImageResource(Integer.valueOf(imageResource));
        }

        public void setSummary(String summary) {
            this.summary.setText(summary);
        }

        public void setTempHigh(String temp) {
            this.tempHigh.setText(temp+"\u00b0C");
        }

        public void setTempLow(String temp) {
            this.tempLow.setText(temp+"\u00b0C");
        }

        public void setWeekday(String weekday) {
            this.weekday.setText(weekday);
        }
    }

}
