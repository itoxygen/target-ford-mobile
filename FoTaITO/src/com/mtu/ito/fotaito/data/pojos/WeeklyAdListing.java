package com.mtu.ito.fotaito.data.pojos;

import java.io.Serializable;

/**
 * Created by Kyle on 12/4/2014.
 */
public class WeeklyAdListing implements Serializable {
    private String _price;
    private String _title;
    private String _imageUrl;
    private String _slug;
    private long _listingId;

    public WeeklyAdListing(final String price, final String title,
            final String imageUrl, final String slug, final long listingId) {
        _price = price;
        _title = title;
        _imageUrl = imageUrl;
        _slug = slug;
        _listingId = listingId;
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
