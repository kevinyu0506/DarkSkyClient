package com.chuntingyu.darkskyclient.network;

import com.chuntingyu.darkskyclient.models.Weather;

import rx.Observable;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Kevin on 2018/3/11.
 */

public interface WeatherService {
    @GET("{lat},{lon}")
    Observable<Response<Weather>> getWeather(
            @Path("lat") double lat,
            @Path("lon") double lon
    );
}
