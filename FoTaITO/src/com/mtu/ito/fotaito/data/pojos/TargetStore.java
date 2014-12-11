package com.mtu.ito.fotaito.data.pojos;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Kyle on 12/2/2014.
 */
public class TargetStore implements Serializable {
    private ArrayList<String> _capabilities;

    private String _storeId;

    private String _storeName;

    private Address _address;

    public TargetStore(final String storeId, final String storeName,
            final Address address, final ArrayList<String> capabilities) {
        _storeId = storeId;
        _storeName = storeName;
        _address = address;
        _capabilities = capabilities;
    }

    public Iterable<String> getCapabilities() {
        return _capabilities;
    }

    public String getStoreId() {
        return _storeId;
    }

    public String getStoreName() {
        return _storeName;
    }

    public Address getAddress() {
        return _address;
    }

    public String getStoreSlug() {
        final StringBuilder builder = new StringBuilder();

        builder.append(_address.getCity().toLowerCase());
        builder.append('-');
        builder.append(_address.getSubdivision().toLowerCase());
        builder.append('-');
        builder.append(postalPrefix());

        return builder.toString();
    }

    private String postalPrefix() {
        final String post = _address.getPostalCode();
        final int hyphen = post.indexOf('-');
        return hyphen == -1 ? post : post.substring(0, hyphen);
    }
}
