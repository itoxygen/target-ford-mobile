package itoxygen.mtu.fotaitov2.backend;

import java.lang.annotation.Target;
import java.util.ArrayList;

import itoxygen.mtu.fotaitov2.data.Scenario;
import itoxygen.mtu.fotaitov2.data.scenarios.HotWeatherScenario;

/**
 * Scenario Manager holds references to all scenarios we check.
 * It is responsible for iterating over the scenarios to see if any have been matched
 *
 * Created by keagan on 10/23/15.
 */
public class ScenarioManager {

    private VehicleManager vehicle;
    private TargetConnectionManager tgt;
    private ArrayList<Scenario> scenarios;

    /**
      * Constructor. Initialize the list of scenarios.
     *
      * @param vehicle reference to vehicle - must be passed to each scenario.
     */
    public ScenarioManager(VehicleManager vehicle, TargetConnectionManager tgt) {

        this.vehicle = vehicle;
        this.tgt = tgt;

        // create list of all implemented scenarios
        scenarios = new ArrayList<>();
        scenarios.add(new HotWeatherScenario(vehicle, tgt));

    }

    /**
     * Check all scenarios to see if any have matched
     *
     * @return the scenario that passed, null if no matches
     */
    public Scenario check() {
        for (Scenario scenario : scenarios) {
            if (scenario.check())
                return scenario;
        }
        return null;
    }
}
