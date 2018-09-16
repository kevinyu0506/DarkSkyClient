package com.chuntingyu.weather.events;

/**
 * Created by Kevin on 2018/3/12.
 */

public class ErrorEvent {
    private final String errorEvent;

    public ErrorEvent(String errorEvent) {

        this.errorEvent = errorEvent;

    }

    public String getErrorEvent() {
        return errorEvent;
    }
}
