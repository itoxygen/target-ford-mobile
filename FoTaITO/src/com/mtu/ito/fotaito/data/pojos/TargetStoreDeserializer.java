package com.mtu.ito.fotaito.data.pojos;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kyle on 12/3/2014.
 */
public class TargetStoreDeserializer implements JsonDeserializer<TargetStore> {
    @Override
    public TargetStore deserialize(final JsonElement jsonElement, final Type type,
            final JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        final JsonObject obj = jsonElement.getAsJsonObject();

        final String storeId = obj.get("ID").getAsString();
        final String storeName = obj.get("Name").getAsString();
        final Address address = jsonDeserializationContext.deserialize(obj.get("Address"), Address.class);

        final List<String> capabilities = new ArrayList<String>();
        final JsonArray jsonCapabilities = obj.getAsJsonArray("Capability");
        for (JsonElement elem : jsonCapabilities) {
            capabilities.add(elem.getAsJsonObject().get("CapabilityName").getAsString());
        }

        return new TargetStore(storeId, storeName, address, capabilities);
    }
}
