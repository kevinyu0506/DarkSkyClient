package com.chuntingyu.darkskyclient.tools;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class KYTime {

    private static Map<Integer, String> weekDayMap = new HashMap<>();

    static {
        weekDayMap.put(1, "MONDAY");
        weekDayMap.put(2, "TUESDAY");
        weekDayMap.put(3, "WEDNESDAY");
        weekDayMap.put(4, "THURSDAY");
        weekDayMap.put(5, "FRIDAY");
        weekDayMap.put(6, "SATURDAY");
        weekDayMap.put(7, "SUNDAY");
    }

    public static String getDayOfWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return weekDayMap.get(calendar.get(Calendar.DAY_OF_WEEK));
    }

    public static String getDayOfWeek(Integer dateInt) {
        long nowTimeLong = new Long(dateInt).longValue() * 1000;
        DateFormat ymdhmsFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String nowTimeStr = ymdhmsFormat.format(nowTimeLong);
        try {
            Date nowTimeDate = ymdhmsFormat.parse(nowTimeStr);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(nowTimeDate);
            return weekDayMap.get(calendar.get(Calendar.DAY_OF_WEEK));
        } catch (ParseException e) {

        }
        return weekDayMap.get(Calendar.DAY_OF_WEEK);
    }
}
