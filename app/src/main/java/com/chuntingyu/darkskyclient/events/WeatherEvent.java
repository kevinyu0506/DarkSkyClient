package com.chuntingyu.darkskyclient.events;

import models.Weather;

/**
 * Created by Kevin on 2018/3/12.
 */

public class WeatherEvent {

    private final Weather weather;

    public WeatherEvent(Weather weather) {
        this.weather = weather;
    }

    public Weather getWeather() {
        return weather;
    }
}
