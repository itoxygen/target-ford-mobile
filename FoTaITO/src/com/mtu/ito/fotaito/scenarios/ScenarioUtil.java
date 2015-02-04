package com.mtu.ito.fotaito.scenarios;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.mtu.ito.fotaito.data.pojos.SavedListing;
import com.mtu.ito.fotaito.data.pojos.TargetStore;
import com.mtu.ito.fotaito.data.pojos.WeeklyAdListing;
import com.mtu.ito.fotaito.frontend.OfferActivity;
import com.mtu.ito.fotaito.frontend.ProductFragment;
import com.mtu.ito.fotaito.frontend.StarterActivity;

/**
 * @author Kyle Oswald
 */
public final class ScenarioUtil {
    private ScenarioUtil() { }

    public static Intent createWeeklyAdListingIntent(final Context context,
            final WeeklyAdListing listing, final TargetStore store, final String message) {
        final Intent intent = new Intent(context, StarterActivity.class);
        intent.putExtra(OfferActivity.KEY_FRAGMENT, ProductFragment.class.getName());

        final SavedListing sl = new SavedListing(message, listing, store);

        final Bundle fragmentArgs = new Bundle();
        fragmentArgs.putSerializable(ProductFragment.KEY_LISTING, sl);
        fragmentArgs.putBoolean(ProductFragment.KEY_SAVED, false);

        intent.putExtra(OfferActivity.KEY_ARGS, fragmentArgs);

        return intent;
    }
}
