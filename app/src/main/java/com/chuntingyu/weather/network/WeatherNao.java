package com.chuntingyu.weather.network;

import com.chuntingyu.weather.models.Weather;
import com.chuntingyu.weather.network.config.RetrofitConfig;

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
