package com.mtu.ito.fotaito.data.pojos;

import java.io.Serializable;

/**
 * Created by Kyle on 12/4/2014.
 */
public class WeeklyAdListing implements Serializable {
    private String _id; // only used by azure records

    private String _price;
    private String _title;
    private String _imageUrl;
    private String _slug;
    private long _listingId;

    public WeeklyAdListing(final String id, final String price, final String title,
                           final String imageUrl, final String slug, final long listingId) {
        this(price, title, imageUrl, slug, listingId);
        _id = id;
    }

    public WeeklyAdListing(final String price, final String title,
            final String imageUrl, final String slug, final long listingId) {
        _id = ""; // Empty string is default expected by azure (i think)

        _price = price;
        _title = title;
        _imageUrl = imageUrl;
        _slug = slug;
        _listingId = listingId;
    }

    public String getAzureId() {
        return _id;
    }

    public void setAzureId(final String id) {
        _id = id;
    }

    public String getPrice() {
        return _price;
    }

    public String getTitle() {
        return _title;
    }

    public String getImageUrl() {
        return _imageUrl;
    }

    public String getSlug() {
        return _slug;
    }

    public long getListingId() {
        return _listingId;
    }
}
