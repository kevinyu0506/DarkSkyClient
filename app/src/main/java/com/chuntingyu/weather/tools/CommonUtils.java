package com.chuntingyu.weather.tools;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Kevin on 2018/3/14.
 */

public class CommonUtils {

    public static boolean isEmailValid(String email) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN =
                "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static int tempConverter(Double temp) {
        return (int) Math.round((temp - 32) * (5.0 / 9.0));
    }

}