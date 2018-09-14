//package com.chuntingyu.darkskyclient.services;
//
//import android.util.Log;
//
//import com.chuntingyu.darkskyclient.events.ErrorEvent;
//import com.chuntingyu.darkskyclient.events.WeatherEvent;
//
//import org.greenrobot.eventbus.EventBus;
//
//import com.chuntingyu.darkskyclient.models.Currently;
//import com.chuntingyu.darkskyclient.models.Weather;
//import com.chuntingyu.darkskyclient.network.WeatherService;
//
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//import retrofit2.Retrofit;
//import retrofit2.converter.gson.GsonConverterFactory;
//
///**
// * Created by Kevin on 2018/3/12.
// */
//
//public class WeatherServiceProvider {
//
//    private static final String BASE_URL = "https://api.darksky.net/forecast/7f21b16d01ea1f181a0774a474dc6236/";
//    private static final String TAG = WeatherServiceProvider.class.getSimpleName();
//    private Retrofit retrofit;
//
//    private Retrofit getRetrofit(){
//
//        if (this.retrofit == null){
//            this.retrofit = new Retrofit.Builder()
//                    .baseUrl(BASE_URL)
//                    .addConverterFactory(GsonConverterFactory.create())
//                    .build();
//        }
//        return this.retrofit;
//    }
//
//    public void getWeather(double lat, double lon){
//        WeatherService weatherService = getRetrofit().create(WeatherService.class);
//        Call<Weather> weatherData = weatherService.getWeather(lat, lon);
//        weatherData.enqueue(new Callback<Weather>() {
//            @Override
//            public void onResponse(Call<Weather> call, Response<Weather> response) {
//
//                Weather weather = response.body();
//
//                if (weather != null){
//                    Currently currently = weather.getCurrently();
//                    Log.e(TAG, "Temperature = " + currently.getTemperature());
//                    EventBus.getDefault().post(new WeatherEvent(weather));
//                } else {
//                    Log.e(TAG, "No response, check your key");
//                    EventBus.getDefault().post(new ErrorEvent("Unable to connect weather server"));
//                }
//
//
//            }
//
//            @Override
//            public void onFailure(Call<Weather> call, Throwable t) {
//
//                Log.e(TAG, "onFailure, unable to get weather data");
////                Toast.makeText("Unable to connect weather server", Toast.LENGTH_SHORT).show();
//                EventBus.getDefault().post(new ErrorEvent("Unable to connect weather server"));
//
//            }
//        });
//    }
//
//}
