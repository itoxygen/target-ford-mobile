package com.mtu.ito.fotaito.data.pojos;

/**
 * Created by Kyle on 12/3/2014.
 */
public class Address {
    private double _latitude;
    private double _longitude;
    private String _line;
    private String _city;
    private String _county;
    private String _subdivision;
    private String _postalCode;
    private String _countryName;
    private String _formattedAddress;

    public Address(final double latitude, final double longitude, final String line, final String city,
            final String county, final String subdivision, final String postalCode, final String countryName,
            final String formattedAddress) {
        _latitude = latitude;
        _longitude = longitude;
        _line = line;
        _city = city;
        _county = county;
        _subdivision = subdivision;
        _postalCode = postalCode;
        _countryName = countryName;
        _formattedAddress = formattedAddress;
    }

    public double getLatitude() {
        return _latitude;
    }

    public double getLongitude() {
        return _longitude;
    }

    public String getLine() {
        return _line;
    }

    public String getCity() {
        return _city;
    }

    public String getCounty() {
        return _county;
    }

    public String getSubdivision() {
        return _subdivision;
    }

    public String getPostalCode() {
        return _postalCode;
    }

    public String getCountryName() {
        return _countryName;
    }

    public String getFormattedAddress() {
        return _formattedAddress;
    }
}
