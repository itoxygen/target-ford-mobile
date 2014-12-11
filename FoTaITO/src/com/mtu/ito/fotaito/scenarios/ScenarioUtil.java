package com.mtu.ito.fotaito.scenarios;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.mtu.ito.fotaito.data.pojos.TargetStore;
import com.mtu.ito.fotaito.data.pojos.WeeklyAdListing;
import com.mtu.ito.fotaito.frontend.MainActivity;
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
        intent.putExtra(MainActivity.KEY_FRAGMENT, ProductFragment.class.getName());

        final Bundle fragmentArgs = new Bundle();
        fragmentArgs.putSerializable(ProductFragment.KEY_STORE, store);
        fragmentArgs.putSerializable(ProductFragment.KEY_LISTING, listing);
        fragmentArgs.putString(ProductFragment.KEY_MESSAGE, message);
        intent.putExtra(MainActivity.KEY_ARGS, fragmentArgs);

        return intent;
    }
}
