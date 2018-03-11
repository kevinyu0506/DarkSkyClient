package com.chuntingyu.darkskyclient.services;

import models.Weather;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Kevin on 2018/3/11.
 */

public interface WeatherService {

    @GET(".")
    Call<Weather> getWeather();

}
