package com.mtu.ito.fotaito.data.pojos;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kyle on 12/3/2014.
 */
public class NearbyStoresResponseDeserializer implements JsonDeserializer<List> {
    @Override
    public List deserialize(final JsonElement jsonElement, final Type type,
            final JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        final JsonArray locationsArray = jsonElement.getAsJsonObject()
                .getAsJsonObject("Locations").getAsJsonArray("Location");

        final ArrayList<TargetStore> list = new ArrayList<TargetStore>();
        if (locationsArray != null) {
            for (JsonElement elem : locationsArray) {
                final TargetStore store = jsonDeserializationContext.deserialize(elem, TargetStore.class);
                list.add(store);
            }
        }

        return list;
    }
}
