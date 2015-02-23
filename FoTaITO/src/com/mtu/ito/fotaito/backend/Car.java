package com.mtu.ito.fotaito.backend;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.app.Activity;
import com.openxc.*;
import android.util.Log;
import com.openxc.measurements.*;

/**
 * Created by dshull on 2/15/15.
 */
public class Car extends Activity {

    /* Car info */


    private static final String TAG = "StarterActivity";
    private VehicleManager mVehicleManager;

    /*
    Class Level variables
     */
    private boolean whiperFluidStatus;
    private double ambientTemperature; /* not sure what class to use for this */

    private double steeringFluid;/* not sure what class to use for this */

    /*Fuel level */
    private boolean isElectric; /* used to allow for both electric and gas powered cars*/
    private double batteryLevel;
    private double fuelLevel;
    private double speed;
    private double odometer;

    /*
    Location - where is the car currently
     */
    private double lat;
    private double lng;

    /*
    * LISTENERS
    * */

    /*
    Sets the speed of the car (may be in RPM, if so we'll need to look at how we convert it).
     */
    EngineSpeed.Listener mSpeedListener = new EngineSpeed.Listener() {
        @Override
        public void receive(Measurement measurement) {
            /*
            Get the value for speed
             */
            final EngineSpeed carSpeed = (EngineSpeed) measurement;
            speed = carSpeed.getValue().doubleValue();
        }
    };
    /*
        Set the latitude of the car
     */
    Latitude.Listener mLatListener = new Latitude.Listener() {
        @Override
        public void receive(Measurement measurement) {
            /*
            Get the value for Lattitude
             */
            final Latitude latVal = (Latitude) measurement;
            lat = latVal.getValue().doubleValue();
        }
    };
    /*
       Set the longitude of the car
    */
    Longitude.Listener mLngListener = new Latitude.Listener() {
        @Override
        public void receive(Measurement measurement) {
            /*
            Get the value for longitude
             */
            final Longitude lngVal = (Longitude) measurement;
            lng = lngVal.getValue().doubleValue();
        }
    };
    /*
      Set the longitude of the car
   */
    Odometer.Listener mOdomListener = new Odometer.Listener() {
        @Override
        public void receive(Measurement measurement) {
            /*
            Get the value for odometer
             */
            final Odometer odomValue = (Odometer) measurement;
            odometer = odomValue.getValue().doubleValue();
        }
    };
    /*
      Set the longitude of the car
   */
    WindshieldWiperStatus.Listener mWhiperListener = new WindshieldWiperStatus.Listener() {
        @Override
        public void receive(Measurement measurement) {
            /*
            Get the value for windhield wiper status
             */
            final WindshieldWiperStatus whiperVal = (WindshieldWiperStatus) measurement;
            whiperFluidStatus = whiperVal.getValue().booleanValue();
        }
    };
    /*
         Set the longitude of the car
      */
    FuelLevel.Listener mFuelListener = new FuelLevel.Listener() {
        @Override
        public void receive(Measurement measurement) {
            /*
            Get the value for fuel level
             */
            final FuelLevel fuelVal = (FuelLevel) measurement;

            if(isElectric){
                batteryLevel = fuelVal.getValue().doubleValue();
            }
            else{
                fuelLevel = fuelVal.getValue().doubleValue();
            }
        }
    };

     /*
    * OPENXC Management
    * */

    private ServiceConnection mConnection = new ServiceConnection() {
        // Called when the connection with the VehicleManager service is
        // established, i.e. bound.
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            Log.i(TAG, "Bound to VehicleManager");

            // When the VehicleManager starts up, we store a reference to it
            // here in "mVehicleManager" so we can call functions on it
            // elsewhere in our code.
            mVehicleManager = ((VehicleManager.VehicleBinder) service)
                    .getService();

            // We want to receive updates whenever the EngineSpeed changes. We
            // have an EngineSpeed.Listener (see above, mSpeedListener) and here
            // we request that the VehicleManager call its receive() method
            // whenever the EngineSpeed changes
            mVehicleManager.addListener(EngineSpeed.class, mSpeedListener);
        }

        // Called when the connection with the service disconnects unexpectedly
        public void onServiceDisconnected(ComponentName className) {
            Log.w(TAG, "VehicleManager Service  disconnected unexpectedly");
            mVehicleManager = null;
        }
    };

    /*
     * These values should never be set by anything but the car object
     * So they've been set to be private accessible only through these getters
     */
    public boolean getWhiperFluidStatus() {
        return whiperFluidStatus;
    }

    public double getAmbientTemperature() {
        return ambientTemperature;
    }

    public boolean isElectric() {
        return isElectric;
    }

    public double getBatteryLevel() {
        return batteryLevel;
    }

    public double getFuelLevel() {
        return fuelLevel;
    }

    public double getSpeed() {
        return speed;
    }

    public double getOdometer() {
        return odometer;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public double getSteeringFluid() {
        return steeringFluid;
    }


}
