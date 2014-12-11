package com.mtu.ito.fotaito.scenarios;

import java.util.ArrayList;

/**
 * Created by Keagan on 11/30/14.
 */
public class ScenarioManager {

    ArrayList<Scenario> scenarios;

    public ScenarioManager() {

        scenarios = new ArrayList<Scenario>();

        // add each scenario to the list
        scenarios.add(new DistanceScenario());
        scenarios.add(new TimeScenario());
    }

}
