package com.mtu.ito.fotaito.data.pojos;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

/**
 * Created by Kyle on 12/10/2014.
 */
public class SavedListingSerializer implements JsonSerializer<SavedListing> {
    @Override
    public JsonElement serialize(final SavedListing savedListing, final Type type,
            final JsonSerializationContext jsonSerializationContext) {
        final JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("id", savedListing.getId());
        jsonObject.addProperty("store_id", savedListing.getStoreId());
        jsonObject.addProperty("message", savedListing.getMessage());

        final WeeklyAdListing listing = savedListing.getListing();
        jsonObject.addProperty("ad_price", listing.getPrice());
        jsonObject.addProperty("ad_title", listing.getTitle());
        jsonObject.addProperty("ad_image", listing.getImageUrl());
        jsonObject.addProperty("ad_slug", listing.getSlug());
        jsonObject.addProperty("ad_id", listing.getListingId());

        return jsonObject;
    }
}
