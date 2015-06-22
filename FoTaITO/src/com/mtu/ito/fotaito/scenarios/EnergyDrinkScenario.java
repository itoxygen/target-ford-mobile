package com.mtu.ito.fotaito.scenarios;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.mtu.ito.fotaito.data.TargetConnection;
import com.mtu.ito.fotaito.data.pojos.TargetStore;
import com.mtu.ito.fotaito.data.pojos.WeeklyAdListing;
import com.mtu.ito.fotaito.service.CarState;

import java.io.IOException;
import java.util.List;

/**
 * Created by Kyle on 12/8/2014.
 */
public class EnergyDrinkScenario implements Scenario {
    private static final String TAG = EnergyDrinkScenario.class.getSimpleName();

    private static final String MSG = "Feeling tired? How about a pick-me-up?";
    private static final String DESC = "You're quickly coming up on a Target store, Make a quick pit-stop for a discounted " +
            "energy drink!";

    @Override
    public Intent satisfied(final CarState state, final TargetConnection cnn,
            final List<TargetStore> stores, final Context context) throws IOException {
        for (TargetStore store : stores) {
            Log.d(TAG, "Querying store: " + store.getStoreSlug());

            final List<WeeklyAdListing> listings = cnn.queryWeeklyAd(store, "coca");

            if (!listings.isEmpty()) {
                final WeeklyAdListing offer = listings.get(0);
                final Intent intent = ScenarioUtil.createWeeklyAdListingIntent(context, offer, store, MSG);

                Log.d(TAG, "Found an offer for " + offer.getTitle());

                return intent;
            }
        }

        return null;
    }

    /** Getters for notification content */
    public String getMsg() { return MSG; }
    public String getDesc() { return DESC; }
}
