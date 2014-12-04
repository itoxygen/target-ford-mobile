package com.mtu.ito.fotaito.data.pojos;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kyle on 12/4/2014.
 */
public class SearchWeeklyAdResponseDeserializer implements JsonDeserializer<List> {
    @Override
    public List deserialize(final JsonElement jsonElement, final Type type,
            final JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        final JsonObject jsonObject = jsonElement.getAsJsonObject();
        final JsonArray listingsArray = jsonObject.getAsJsonArray("listings");

        final ArrayList<WeeklyAdListing> list = new ArrayList<WeeklyAdListing>();
        for (JsonElement elem : listingsArray) {
            final WeeklyAdListing listing = jsonDeserializationContext.deserialize(elem, WeeklyAdListing.class);
            list.add(listing);
        }

        return list;
    }
}
