package com.chuntingyu.weather.activities.main;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chuntingyu.weather.applications.BaseActivity;
import com.chuntingyu.weather.applications.SplashActivity;
import com.chuntingyu.weather.applications.WeatherApp;
import com.chuntingyu.weather.R;
import com.chuntingyu.weather.models.Data;
import com.chuntingyu.weather.models.Hourly;
import com.chuntingyu.weather.models.Weather;
import com.chuntingyu.weather.network.WeatherNao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;
import retrofit2.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

import com.chuntingyu.weather.models.Currently;
import com.chuntingyu.weather.tools.IconHelper;
import com.chuntingyu.weather.tools.KYMath;
import com.chuntingyu.weather.tools.KYTime;
import com.chuntingyu.weather.tools.KYTouchListener;
import com.chuntingyu.weather.tools.acplibrary.ACProgressConstant;
import com.chuntingyu.weather.tools.acplibrary.ACProgressFlower;
import com.chuntingyu.weather.tools.coredata.DataManager;
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
    private ACProgressFlower pd;
    private Animation updateAnim;

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
    @BindView(R.id.hourlySummary)
    TextView hourlySummary;
    @BindView(R.id.center_recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.update_button)
    ImageView updateBtn;

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
        TextViewCompat.setAutoSizeTextTypeWithDefaults(hourlySummary, TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM);

        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainPresenter.setUserLoggedOut();
            }
        });

        MainActivityPermissionsDispatcher.showUserLocationWithPermissionCheck(this);
        MainActivityPermissionsDispatcher.getWeatherWithPermissionCheck(this);

        updateBtn.setOnClickListener(updateButtonClickListener);
        updateAnim = AnimationUtils.loadAnimation(this, R.anim.update_rotate_anim);
        updateBtn.startAnimation(updateAnim);

        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        recyclerView.setAdapter(dailyReportAdapter);
        recyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    }

    private View.OnClickListener updateButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            updateBtn.startAnimation(updateAnim);
//            MainActivityPermissionsDispatcher.showUserLocationWithPermissionCheck(MainActivity.this);
            MainActivityPermissionsDispatcher.getWeatherWithPermissionCheck(MainActivity.this);
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // NOTE: delegate the permission handling to generated method
        MainActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @NeedsPermission(Manifest.permission.INTERNET)
    public void getWeather() {
        pd = new ACProgressFlower.Builder(this).direction(ACProgressConstant.DIRECT_CLOCKWISE).themeColor(Color.WHITE).build();
        pd.show();
//        if (updateBtn != null) {
//            updateBtn.startAnimation(updateAnim);
//        }

        WeatherNao.getWeather(lat, lon).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Response<Weather>>() {
            @Override
            public void onNext(Response<Weather> response) {
                unsubscribe();
                if (response.isSuccessful()) {
                    Weather weather = response.body();
                    if (weather != null) {
                        Currently currently = weather.getCurrently();
                        Log.e(TAG, "Temperature = " + currently.getTemperature());

                        tempTextView.setText(String.valueOf(KYMath.tempConverterF2C(currently.getTemperature())) + "\u00b0C");
                        summaryTextView.setText(currently.getSummary());

                        Hourly hourly = weather.getHourly();
                        hourlySummary.setText(hourly.getSummary());

                        dailyDatas = weather.getDaily().getData();
                        dailyReportAdapter.notifyDataSetChanged();

                        Integer iconResource = IconHelper.getIconResource(currently.getIcon());
                        iconImageView.setImageResource(iconResource);
                        Log.e(TAG, "Lat = " + lat + "Lon = " + lon);
                    } else {
                        Log.e(TAG, "No response, check your key");
                    }
                    pd.dismiss();
                    updateBtn.clearAnimation();
                }
            }

            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "onFailure, unable to get weather data");
                pd.dismiss();
                updateBtn.clearAnimation();
//                Toast.makeText("Unable to connect weather server", Toast.LENGTH_SHORT).show();
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
                        if (location != null) {
                            lat = location.getLatitude();
                            lon = location.getLongitude();
                            Geocoder gc = new Geocoder(MainActivity.this);
                            try {
//                                Log.e(TAG, "Lat = " + location.getLatitude() + ", Lon = " + location.getLongitude());
                                List<Address> lstAddress = gc.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                String returnAddress = lstAddress.get(0).getAdminArea().toUpperCase();
                                userLocation.setText(returnAddress);
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
            int tempHigh = KYMath.tempConverterF2C(dailyData.getTemperatureHigh());
            holder.setTempHigh(String.valueOf(tempHigh));
            int tempLow = KYMath.tempConverterF2C(dailyData.getTemperatureLow());
            holder.setTempLow(String.valueOf(tempLow));
            String day = KYTime.getDayOfWeek(dailyData.getTime());
            holder.setWeekday(day);
            String date = KYTime.getDate(dailyData.getTime());
            holder.setDate(date);
            Log.e("=====", day + " :" + dailyData.getTime());
        }

        @Override
        public int getItemCount() {
            return dailyDatas.size();
        }
    }

    public class DailyReportViewHolder extends RecyclerView.ViewHolder {
        View root;
        ImageView icon;
        TextView summary, tempHigh, tempLow, weekday, date;
        KYTouchListener touchListener = new KYTouchListener();

        DailyReportViewHolder(View view) {
            super(view);
            root = view.findViewById(R.id.cell_daily_report_root);
            icon = view.findViewById(R.id.cell_daily_report_icon);
            summary = view.findViewById(R.id.cell_daily_report_summary);
            tempHigh = view.findViewById(R.id.cell_daily_report_temp_high);
            tempLow = view.findViewById(R.id.cell_daily_report_temp_low);
            weekday = view.findViewById(R.id.cell_daily_report_weekday);
            date = view.findViewById(R.id.cell_daily_report_date);

            root.getLayoutParams().height = KYMath.screenSize().y * 280 / 667;
            root.getLayoutParams().width = KYMath.screenSize().x * 120 / 375;

            root.setOnTouchListener(touchListener);
//            icon.setOnTouchListener(touchListener);
        }

        public void setIcon(String resource) {
            Integer imageResource = IconHelper.getIconResource(resource);
            this.icon.setImageResource(Integer.valueOf(imageResource));
        }

        public void setSummary(String summary) {
            this.summary.setText(summary);
        }

        public void setTempHigh(String temp) {
            this.tempHigh.setText(temp + "\u00b0C");
        }

        public void setTempLow(String temp) {
            this.tempLow.setText(temp + "\u00b0C");
        }

        public void setWeekday(String weekday) {
            this.weekday.setText(weekday);
        }

        public void setDate(String date) {
            this.date.setText(date);
        }
    }

}
