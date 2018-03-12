package com.chuntingyu.darkskyclient.services;

import models.Weather;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Kevin on 2018/3/11.
 */

public interface WeatherService {

    @GET("{lat},{lon}")
    Call<Weather> getWeather(@Path("lat") double lat, @Path("lon") double lon);

}
