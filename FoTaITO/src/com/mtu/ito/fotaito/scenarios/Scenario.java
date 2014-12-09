package com.mtu.ito.fotaito.scenarios;

import android.content.Context;
import android.content.Intent;
import com.mtu.ito.fotaito.data.TargetConnection;
import com.mtu.ito.fotaito.data.pojos.TargetStore;
import com.mtu.ito.fotaito.service.CarState;

import java.io.IOException;
import java.util.List;

/**
 * Encapsulates the logic necessary to determine if a possible offer scenario
 * is applicable and to determine what action to take.
 *
 * @author Kyle Oswald
 */
public interface Scenario {
    /**
     * Checks to see if the conditions required to satisfy the scenario are
     * met. An Intent should be created using the supplied context with the
     * action to be taken if the scenario is satisfied. Otherwise null should
     * be returned.
     *
     * @param state A snapshot of the vehicle's current parameters
     * @param cnn Target API connection
     * @param stores List of valid stores to use in searches
     * @param context Application context to use when creating intent
     * @return An Intent or null
     * @throws IOException
     */
    public Intent satisfied(CarState state, TargetConnection cnn,
                            List<TargetStore> stores, Context context) throws IOException;
}
