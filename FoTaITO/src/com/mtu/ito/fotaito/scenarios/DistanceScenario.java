package com.mtu.ito.fotaito.scenarios;

import android.content.Intent;
import com.mtu.ito.fotaito.data.CartwheelConnection;
import com.mtu.ito.fotaito.service.CarState;

/**
 * Created by Keagan on 11/30/14.
 * Push notification after car has traveled a certain distance
 */
public class DistanceScenario implements Scenario{

    public Intent satisfied(CarState state, CartwheelConnection cnn) {
        return null;
    }


}
