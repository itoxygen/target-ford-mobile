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

    @Override
    public Intent satisfied(final CarState state, final TargetConnection cnn,
            final List<TargetStore> stores, final Context context) throws IOException {
        for (TargetStore store : stores) {
            final List<WeeklyAdListing> listings = cnn.queryWeeklyAd(store, "energy drink");

            if (!listings.isEmpty()) {
                final WeeklyAdListing offer = listings.get(0);
                final Intent intent = ScenarioUtil.createWeeklyAdListingIntent(context, offer, store, MSG);

                Log.d(TAG, "Found an offer for " + offer.getTitle());

                return intent;
            }
        }

        return null;
    }
}
