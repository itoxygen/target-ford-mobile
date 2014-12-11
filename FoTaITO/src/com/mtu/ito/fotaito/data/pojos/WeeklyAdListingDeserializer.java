package com.mtu.ito.fotaito.data.pojos;

import com.google.gson.*;

import java.lang.reflect.Type;

/**
 * Created by Kyle on 12/4/2014.
 */
public class WeeklyAdListingDeserializer implements JsonDeserializer<WeeklyAdListing> {
    @Override
    public WeeklyAdListing deserialize(final JsonElement jsonElement, final Type type,
            final JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        final JsonObject jsonObject = jsonElement.getAsJsonObject();

        final String price = jsonObject.get("price").getAsString();
        final String title = jsonObject.get("title").getAsString();
        final String image = jsonObject.get("image").getAsString();
        final String slug = jsonObject.get("slug").getAsString();
        final long listingId = jsonObject.get("listingid").getAsLong();

        return new WeeklyAdListing(price, title, image, slug, listingId);
    }
}
