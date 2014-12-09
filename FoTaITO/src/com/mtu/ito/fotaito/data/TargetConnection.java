package com.mtu.ito.fotaito.data;

import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mtu.ito.fotaito.data.pojos.*;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Handles communication with Target APIs. Methods are thread safe.
 *
 * @author Kyle Oswald
 */
public class TargetConnection {
    private static final String TAG = TargetConnection.class.getSimpleName();

    // Key to be passed with each request for authentication
    private static final String API_KEY = "P0CrGVTiWK0jCg7Yh8WiklJmcbbd9aJq";

    private static final String GET_NEARBY_STORES_ENDPOINT = "http://api.target.com/v2/store";

    private static final String SEARCH_WEEKLY_AD_ENDPOINT = "http://api.target.com/v1/promotions/weeklyad/%s/search";

    public TargetConnection() {

    }

    /**
     * Queries Target rest api to retrieve store locations within a radius around
     * the given geo-coordinates.
     *
     * @param lat Latitude
     * @param lon Longitude
     * @param range Radius in miles
     * @return List of stores within radius, may be empty
     * @throws IOException If there is a network error
     * @see <a href="https://dev.target.com/api-documentation/get-nearby-stores">Get Nearby Stores</a>
     */
    @SuppressWarnings("unchecked")
    public List<TargetStore> queryNearbyStores(final double lat, final double lon, final int range) throws IOException {
        final Map<String, String> params = new HashMap<String, String>();
        params.put("nearby", lat + "," + lon);
        params.put("range", Integer.toString(range));
        params.put("limit", "15");

        final InputStreamReader stream = getEndpointJson(GET_NEARBY_STORES_ENDPOINT, params);

        try {
            final GsonBuilder builder = new GsonBuilder();
            final Gson gson = builder.registerTypeAdapter(Address.class, new AddressDeserializer())
                                     .registerTypeAdapter(TargetStore.class, new TargetStoreDeserializer())
                                     .registerTypeAdapter(List.class, new NearbyStoresResponseDeserializer())
                                     .create();

            return (List<TargetStore>) gson.fromJson(stream, List.class);
        } finally {
            stream.close();
        }
    }

    /**
     * Queries Target rest api to retrieve weekly ad listings for the specified
     * store.
     *
     * @param store TargetStore instance to search weekly ad
     * @param queryString Search string
     * @return List of weekly ad listings which match the query string, may be empty
     * @throws IOException If there is a network error
     * @see #queryNearbyStores(double, double, int)
     * @see <a href="https://dev.target.com/api-documentation/search-weekly-ad">Search Weekly Ad</a>
     */
    @SuppressWarnings("unchecked")
    public List<WeeklyAdListing> queryWeeklyAd(final TargetStore store, final String queryString) throws IOException {
        final Map<String, String> params = new HashMap<String, String>();
        params.put("q", queryString);

        final String url = String.format(SEARCH_WEEKLY_AD_ENDPOINT, store.getStoreSlug());
        final InputStreamReader stream = getEndpointJson(url, params);

        try {
            final GsonBuilder builder = new GsonBuilder();
            final Gson gson = builder.registerTypeAdapter(WeeklyAdListing.class, new WeeklyAdListingDeserializer())
                                     .registerTypeAdapter(List.class, new SearchWeeklyAdResponseDeserializer())
                                     .create();

            return (List<WeeklyAdListing>) gson.fromJson(stream, List.class);
        } finally {
            stream.close();
        }
    }

    private InputStreamReader getEndpointJson(final String url, final Map<String, String> params) throws IOException {
        params.put("key", API_KEY);
        final String query = encodeParams(params);

        final URL net = new URL(url + query);

        Log.d(TAG, "Hitting endpoint: " + net.toString());

        final URLConnection cnn = net.openConnection();
        cnn.setRequestProperty("Accept", "application/json");
        cnn.setDoInput(true);

        return new InputStreamReader(cnn.getInputStream(), "UTF-8");
    }

    /** Builds utf-8 url encoded query string from key-value map. */
    private String encodeParams(final Map<String, String> params) {
        final StringBuilder ret = new StringBuilder();

        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (ret.length() > 0) { // append delimiter
                ret.append('&');
            } else {
                ret.append('?');
            }

            try {
                ret.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                ret.append('=');
                ret.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                // ill commit seppuku
            }
        }

        return ret.toString();
    }
}
