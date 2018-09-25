package com.chuntingyu.weather.tools;

import com.chuntingyu.weather.R;

import java.util.HashMap;
import java.util.Map;

public class IconHelper {

    private static Map<String, Integer> iconMap = new HashMap<>();

    static {
        iconMap.put("clear-day", R.drawable.sun);
        iconMap.put("clear-night", R.drawable.moon);
        iconMap.put("rain", R.drawable.rain);
        iconMap.put("snow", R.drawable.snowing);
        iconMap.put("sleet", R.drawable.snowflake);
        iconMap.put("wind", R.drawable.windy);
        iconMap.put("fog", R.drawable.windy);
        iconMap.put("cloudy", R.drawable.cloud);
        iconMap.put("partly-cloudy-day", R.drawable.cloudy_sun);
        iconMap.put("partly-cloudy-night", R.drawable.cloudy_night);
        iconMap.put("thunderstorm", R.drawable.storm);
    }

    public static Integer getIconResource(String key) {
        return iconMap.get(key);
    }

}
