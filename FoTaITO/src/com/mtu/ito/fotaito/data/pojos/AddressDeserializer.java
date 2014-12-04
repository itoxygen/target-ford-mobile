package com.mtu.ito.fotaito.data.pojos;

import com.google.gson.*;

import java.lang.reflect.Type;

/**
 * Created by Kyle on 12/3/2014.
 */
public class AddressDeserializer implements JsonDeserializer<Address> {
    @Override
    public Address deserialize(final JsonElement jsonElement, final Type type,
            final JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        final JsonObject obj = jsonElement.getAsJsonObject();

        final double latitude = obj.get("Latitude").getAsDouble();
        final double longitude = obj.get("Longitude").getAsDouble();
        final String line = obj.get("AddressLine1").getAsString();
        final String city = obj.get("City").getAsString();
        final String country = obj.get("Country").getAsString();
        final String subdivision = obj.get("Subdivision").getAsString();
        final String postalCode = obj.get("PostalCode").getAsString();
        final String countryName = obj.get("CountryName").getAsString();
        final String formattedAddr = obj.get("FormattedAddress").getAsString();

        return new Address(latitude, longitude, line, city,
                country, subdivision, postalCode, countryName, formattedAddr);
    }
}
