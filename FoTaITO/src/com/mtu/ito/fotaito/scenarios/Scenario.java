package com.mtu.ito.fotaito.scenarios;

import android.content.Intent;
import com.mtu.ito.fotaito.data.TargetConnection;
import com.mtu.ito.fotaito.service.CarState;

/**
 * Created by Kyle on 11/1/2014.
 */
public interface Scenario {
    public Intent satisfied(CarState state, TargetConnection cnn);
}
