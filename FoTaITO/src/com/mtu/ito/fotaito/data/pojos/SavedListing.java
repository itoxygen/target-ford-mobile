package com.mtu.ito.fotaito.data.pojos;

import android.view.View;

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
    private View expandableView;

    public SavedListing(final String message, final WeeklyAdListing listing,
            final TargetStore store) {
        id = UUID.randomUUID().toString();
        _message = message;
        _listing = listing;
        _storeId = store.getStoreId();
        expandableView = null;
    }

    public SavedListing(final String id, final String message,
            final WeeklyAdListing listing, final String storeId) {
        this.id = id;
        _message = message;
        _listing = listing;
        _storeId = storeId;
        expandableView = null;
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

    public void setExpandableView(View v) { expandableView = v; }

    public View getExpandableView() { return expandableView; }
}
