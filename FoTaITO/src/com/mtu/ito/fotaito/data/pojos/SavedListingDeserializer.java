package com.mtu.ito.fotaito.data.pojos;

import com.google.gson.*;

import java.lang.reflect.Type;

/**
 * Created by Kyle on 12/9/2014.
 */
public class SavedListingDeserializer implements JsonDeserializer<SavedListing> {
    @Override
    public SavedListing deserialize(final JsonElement jsonElement, final Type type,
            final JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        final JsonObject jsonObject = jsonElement.getAsJsonObject();

        final WeeklyAdListing listing = deserializeWeeklyAdListing(jsonObject);
        final String storeId = jsonObject.get("store_id").getAsString();
        final String id = jsonObject.get("id").getAsString();
        final String message = jsonObject.get("message").getAsString();

        return new SavedListing(id, message, listing, storeId);
    }

    private WeeklyAdListing deserializeWeeklyAdListing(final JsonObject jsonObject) {
        final String price = jsonObject.get("ad_price").getAsString();
        final String title = jsonObject.get("ad_title").getAsString();
        final String image = jsonObject.get("ad_image").getAsString();
        final String slug = jsonObject.get("ad_slug").getAsString();
        final long id = jsonObject.get("ad_id").getAsLong();

        return new WeeklyAdListing(price, title, image, slug, id);
    }
}
