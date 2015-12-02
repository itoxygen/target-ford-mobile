package itoxygen.mtu.fotaitov2.backend;

/**
 * Vehicle manager is responsible for interfacing with the OpenXC vehicle service.
 *
 * Requests a bind to the service and queries it for needed data
 *
 * Created by keagan on 10/23/15.
 */
public class VehicleManager {

    private double outsideTemperature;

    public VehicleManager() {
        // init data for est
        outsideTemperature = 92.0;
    }

    public double getOutsideTemperature() {
        return outsideTemperature;
    }

}
