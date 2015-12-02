package itoxygen.mtu.fotaitov2.backend;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import com.openxc.VehicleManager;
import com.openxc.measurements.EngineSpeed;
import com.openxc.measurements.Measurement;
import com.openxc.measurements.BrakePedalStatus;
import com.openxc.measurements.VehicleSpeed;
import com.openxc.measurements.Latitude;
import com.openxc.measurements.Longitude;

/**
 * Vehicle manager is responsible for interfacing with the OpenXC vehicle service.
 *
 * Requests a bind to the service and queries it for needed data
 *
 * Created by keagan on 10/23/15.
 */
public class OpenXCManager {

    private static final String TAG = "FordVehicleManager";

    private VehicleManager mVehicleManager;

    private Activity ref;

    private double mEngineSpeed;
    private boolean mBrakePedalStatus;
    private double mVehicleSpeed;
    private double mLatitude;
    private double mLongitude;

    public OpenXCManager() {}

    public void init(Activity ref) {
        this.ref = ref;
        Log.d(TAG, "init ford vehicle manager");
    }

    public void bind() {
        Log.d(TAG, "Attemptint to initialize openXC connection");
        if(mVehicleManager == null) {
            Intent intent = new Intent(ref, VehicleManager.class);
            ref.bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        }
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        // Called when the connection with the VehicleManager service is established
        public void onServiceConnected(ComponentName className, IBinder service) {
            Log.d(TAG, "Bound to VehicleManager");

            // store vehicle connection
            mVehicleManager = ((VehicleManager.VehicleBinder) service)
                    .getService();

            // Add listeneres
            mVehicleManager.addListener(EngineSpeed.class, mSpeedListener);
            mVehicleManager.addListener(BrakePedalStatus.class, mBrakePedalStatusListener);
            mVehicleManager.addListener(VehicleSpeed.class, mVehicleSpeedListener);
            mVehicleManager.addListener(Latitude.class, mLatitudeListener);
            mVehicleManager.addListener(Longitude.class, mLongitudeListener);
        }

        // Called when the connection with the service disconnects unexpectedly
        public void onServiceDisconnected(ComponentName className) {
            Log.w("openxc", "VehicleManager Service  disconnected unexpectedly");
            mVehicleManager = null;
        }
    };

    // ENGINE RPMS
    EngineSpeed.Listener mSpeedListener = new EngineSpeed.Listener() {
        public void receive(Measurement measurement) {
            // cast generic measurement back to the type we know it to be, an EngineSpeed.
            final EngineSpeed speed = (EngineSpeed) measurement;
            mEngineSpeed = speed.getValue().doubleValue();
        }
    };

    // BRAKE STATUS
    BrakePedalStatus.Listener mBrakePedalStatusListener = new BrakePedalStatus.Listener() {
        public void receive(Measurement measurement) {
            final BrakePedalStatus status = (BrakePedalStatus) measurement;
            mBrakePedalStatus = status.getValue().booleanValue();
        }
    };

    // VEHICLE SPEED
    VehicleSpeed.Listener mVehicleSpeedListener = new VehicleSpeed.Listener() {
        public void receive(Measurement measurement) {
            final VehicleSpeed speed = (VehicleSpeed) measurement;
            mVehicleSpeed = speed.getValue().doubleValue();
        }
    };

    // LATITUDE
    Latitude.Listener mLatitudeListener = new Latitude.Listener() {
        public void receive(Measurement measurement) {
            final Latitude lat = (Latitude) measurement;
            mLatitude = lat.getValue().doubleValue();
        }
    };

    // LATITUDE
    Longitude.Listener mLongitudeListener = new Longitude.Listener() {
        public void receive(Measurement measurement) {
            final Longitude longitude = (Longitude) measurement;
            mLongitude = longitude.getValue().doubleValue();
        }
    };


    // public getters
    public double getEngineStatus() { return mEngineSpeed; }
    public boolean getBrakePedalStatus() { return mBrakePedalStatus; }
    public double getVehicleSpeed() { return mVehicleSpeed; }
    public double getLatitude() { return mLatitude; }
    public double getLongitude() { return mLongitude; }
}
