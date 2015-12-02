package itoxygen.mtu.fotaitov2.data.scenarios;

import itoxygen.mtu.fotaitov2.backend.OpenXCManager;
import itoxygen.mtu.fotaitov2.backend.TargetConnectionManager;
import itoxygen.mtu.fotaitov2.data.Product;
import itoxygen.mtu.fotaitov2.data.Scenario;

/**
 * Checking for hot weather and pushing relevant ads
 *
 * Created by keagan on 10/23/15.
 */
public class HotWeatherScenario extends Scenario {

    public HotWeatherScenario(OpenXCManager vehicle, TargetConnectionManager tgt) {
        super(vehicle, tgt);
    }

    @Override
    protected boolean checkVehicle() {
//        if (vehicle.getOutsideTemperature() > 87.0)
//            return true;
        return true;
    }

    @Override
    protected Product checkTargetItems() {

        Product prod = tgt.searchWeeklyAds("Drink", "colma-ca-94014");

        return prod;
    }
}
