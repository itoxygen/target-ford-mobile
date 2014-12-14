package com.mtu.ito.fotaito.data.pojos;

import java.io.Serializable;
import java.util.UUID;

/**
 * @author Kyle Oswald
 */
public class SavedListing implements Serializable {
    // Has to be named "id" verbatim or azure freaks out
    // and i dont feel like changing the src code
    private final String id;

    private final String _message;
    private final WeeklyAdListing _listing;
    private final String _storeId;

    public SavedListing(final String message, final WeeklyAdListing listing,
            final TargetStore store) {
        id = UUID.randomUUID().toString();
        _message = message;
        _listing = listing;
        _storeId = store.getStoreId();
    }

    public SavedListing(final String id, final String message,
            final WeeklyAdListing listing, final String storeId) {
        this.id = id;
        _message = message;
        _listing = listing;
        _storeId = storeId;
    }

    public String getId() {
        return id;
    }

    public String getMessage() {
        return _message;
    }

    public WeeklyAdListing getListing() {
        return _listing;
    }

    public String getStoreId() {
        return _storeId;
    }
}
