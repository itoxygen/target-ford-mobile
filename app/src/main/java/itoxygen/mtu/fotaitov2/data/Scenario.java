package itoxygen.mtu.fotaitov2.data;

import itoxygen.mtu.fotaitov2.backend.TargetConnectionManager;
import itoxygen.mtu.fotaitov2.backend.VehicleManager;

/**
 * Abstract scenario class defines what each scenario must include
 *
 * Created by keagan on 10/23/15.
 */
public abstract class Scenario {

    protected Product matchedProduct;
    protected VehicleManager vehicle;
    protected TargetConnectionManager tgt;

    public Scenario(VehicleManager vehicle, TargetConnectionManager tgt) {
        this.vehicle = vehicle;
        this.tgt = tgt;
    }

    /**
     * To be a match the scenario must have ALL of its vehicle matches pass,
     * as well as a matching product found in the Target APIs
     * @return true if the scenario passes, false if not
     */
    public boolean check() {
        Product targetProduct = checkTargetItems();
        if (checkVehicle() && (targetProduct != null)) {
            matchedProduct = targetProduct;
            return true;
        } else {
            return false;
        }
    }

    /**
     * Check all vehicle requirements (speed, temp, oil pressure, etc.)
     * @return true if ALL requirements are met, false if not
     */
    protected abstract boolean checkVehicle();

    /**
     * Check all available target APIs for search terms of products we want to push
     * @return Product match that was found, null if no matches
     */
    protected abstract Product checkTargetItems();

    public Product getMatchedProduct() {
        return matchedProduct;
    }
}


