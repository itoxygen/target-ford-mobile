package com.mtu.ito.fotaito.service;

/**
 * Created by Kyle on 11/1/2014.
 */
public class CarState {
    private final double _lat;
    private final double _lon;

    public CarState(final double lat, final double lon) {
        _lat = lat;
        _lon = lon;
    }

    public double getLatitude() {
        return _lat;
    }

    public double getLongitude() {
        return _lon;
    }
}
