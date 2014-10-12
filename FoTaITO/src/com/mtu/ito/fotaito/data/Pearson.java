package com.mtu.ito.fotaito.data;

import android.util.Base64;
import android.util.Log;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Kyle on 9/27/2014.
 */
public class Pearson {
    private static final String TAG = Pearson.class.getSimpleName();

    private static final String BASE_URL = "https://sandbox.youracclaim.com/api/v1";

    private static final String ORGANIZATION_ID = "41dd84f5-e826-4e17-a819-14b6af8bdb1f";
    private static final String ORGANIZATION_AUTH_TOKEN = "59kyows7O22yDDvz_g40:";

    public static Badge[] getUserBadges(final String id) throws Exception {
        final String url = "/organizations/" + ORGANIZATION_ID + "/badges?filter=user_id::" + id;

        Log.i(TAG, url);

        final InputStream stream = doHTTP(url, "GET");
        final ObjectMapper mapper = new ObjectMapper();

        final JsonNode root = mapper.readTree(stream);

        final ArrayNode data = (ArrayNode) root.path("data");
        final Badge[] ret = new Badge[data.size()];
        for (int i = 0; i < data.size(); i++) {
            final JsonNode node = data.get(i).path("badge_template");
            ret[i] = new Badge();
            ret[i].setId(node.path("id").getTextValue());
            ret[i].setDescription(node.path("description").getTextValue());
        }

        return ret;
    }

    private static InputStream doHTTP(String url, String method)
            throws Exception {
        URL obj = new URL(BASE_URL + url);
        Log.i(TAG, obj.toString());
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod(method);

        final String base64 = Base64.encodeToString(ORGANIZATION_AUTH_TOKEN.getBytes(), 0);
        con.setRequestProperty("Authorization", "Basic " + base64);

        int responseCode = con.getResponseCode();

        if (responseCode != 200) {
            throw new RuntimeException("Bad response code: " + responseCode);
        }
        //System.out.println("\nSending 'GET' request to URL : " + url);
        //System.out.println("Response Code : " + responseCode);

        return new BufferedInputStream(con.getInputStream());
    }
}
