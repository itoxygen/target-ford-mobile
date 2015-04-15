package com.mtu.ito.fotaito.frontend;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mtu.ito.fotaito.R;
import com.mtu.ito.fotaito.backend.Car;
import com.mtu.ito.fotaito.data.AzureDatabaseManager;
import com.mtu.ito.fotaito.data.TargetConnection;
import com.mtu.ito.fotaito.data.pojos.TargetStore;
import com.mtu.ito.fotaito.data.pojos.WeeklyAdListing;

import org.w3c.dom.Text;

import java.lang.annotation.Target;
import java.util.List;

/**
 * Target Ford Mobile Team
 * MTU ITOxygen
 *
 * Settings Page
 */
public class SettingsActivity extends MyActivity {
    private static final String TAG = SettingsActivity.class.getSimpleName();


    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_fragment);

        // network hack
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        updateValues();

        // register the button to redirect user to sandbox (testing) activity
//        final Button sandboxButton = (Button) findViewById(R.id.button_tosandbox);
//        sandboxButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(final View v) {
//                Intent intent = new Intent(getBaseContext(), SandboxActivity.class);
//                startActivity(intent);
//            }
//        });

        final Button updateButton = (Button) findViewById(R.id.button_foreceupdate);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Log.d("settingsactivity", "updating");
                updateValues();
            }
        });
    }

    private void updateValues() {
        // vehicle status
        TextView vehicleStatus = (TextView) findViewById(R.id.textView_vstatus);
        if (checkVehicleStatus()) {
            vehicleStatus.setText("Connected");
            vehicleStatus.setTextColor(Color.parseColor("#ff99cc00"));
        } else {
            vehicleStatus.setText("Not Connected");
            vehicleStatus.setTextColor(Color.parseColor("#ffff4444"));
        }

        // target api
        TextView targetStatus = (TextView) findViewById(R.id.textView_targetStatus);
        if (checkTargetCon()) {
            targetStatus.setText("Responsive");
            targetStatus.setTextColor(Color.parseColor("#ff99cc00"));
        } else {
            targetStatus.setText("Unresponsive");
            targetStatus.setTextColor(Color.parseColor("#ffff4444"));
        }

        // mobile db
        TextView dbConnection = (TextView) findViewById(R.id.textView_dbConnection);
        AzureDatabaseManager db = AzureDatabaseManager.getInstance(this);
        if (db.isLoggedIn()) {
            dbConnection.setText("Connected");
            dbConnection.setTextColor(Color.parseColor("#ff99cc00"));
        } else {
            dbConnection.setText("Not Connected");
            dbConnection.setTextColor(Color.parseColor("#ffff4444"));
        }


        // closest store value
        Car car = Car.getInstance();
        TextView lat = (TextView) findViewById(R.id.textView_lat);
        TextView lng = (TextView) findViewById(R.id.textView_lng);
        lat.setText(Double.toString(car.lat));
        lat.setTextColor(Color.parseColor("#ff99cc00"));
        lng.setText(Double.toString(car.lng));
        lng.setTextColor(Color.parseColor("#ff99cc00"));


        // vehicle speed
        TextView speed = (TextView) findViewById(R.id.textView_speed);
        speed.setText(Double.toString(car.getSpeed()));

        // wiper status
        TextView wiper = (TextView) findViewById(R.id.textView_wiperStatus);
        wiper.setText(Boolean.toString(car.getWhiperStatus()));

    }

    private Boolean checkVehicleStatus() {
        return Car.getInstance().connected;
    }

    private boolean checkTargetCon() {
        Log.d(TAG, "checking connection to target api's");
        Car car = Car.getInstance();
        TargetConnection tc = new TargetConnection();
        List<TargetStore> stores = null;
        List<WeeklyAdListing> ads = null;
        try {
            stores = tc.queryNearbyStores(car.lat, car.lng, 500);

            // make sure a nearby store is found
           if (stores != null && stores.size() > 0) {

               // if a store is found check that querying ads returns something
                ads = tc.queryWeeklyAd(stores.get(0), "a");
               if (ads != null && ads.size() > 0) {
                   return true;
               } else {
                   Log.d(TAG, "stores found no ads returned");
               }

            } else {
               Log.d(TAG, "no stores in proximity found");
           }
            return false;
        } catch (Exception e) {
            // do something with the exception i guess
            Log.e(TAG, "Exception while checking Target API connection");
            e.printStackTrace();
        }
        return false;
    }
}
