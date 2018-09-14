package com.chuntingyu.darkskyclient.network;

import com.chuntingyu.darkskyclient.models.Weather;
import com.chuntingyu.darkskyclient.network.config.RetrofitConfig;

import retrofit2.Response;
import rx.Observable;
import rx.schedulers.Schedulers;

public class WeatherNao {
    private static final WeatherService weatherService;

    static {
        weatherService = RetrofitConfig.retrofit().create(WeatherService.class);
    }

    public static Observable<Response<Weather>> getWeather(double lat, double lon) {
        return weatherService.getWeather(lat, lon)
                .subscribeOn(Schedulers.io());
    }
}
