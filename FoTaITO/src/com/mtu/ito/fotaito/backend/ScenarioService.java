package com.mtu.ito.fotaito.backend;

import android.app.*;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.os.StrictMode;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import com.mtu.ito.fotaito.R;
import com.mtu.ito.fotaito.data.TargetConnection;
import com.mtu.ito.fotaito.data.pojos.TargetStore;
import com.mtu.ito.fotaito.frontend.OfferActivity;
import com.mtu.ito.fotaito.frontend.SettingsActivity;
import com.mtu.ito.fotaito.frontend.StarterActivity;
import com.mtu.ito.fotaito.scenarios.EnergyDrinkScenario;
import com.mtu.ito.fotaito.service.CarState;
import android.os.Bundle;

import java.io.IOException;
import java.util.List;

/* Changes
 * - TODO Put the scenario checks into a background thread & removed threading policy override
 * - DONE Set up the notification to route through StarterActivity so the user can log in
 */
/**
 * @author Keagan Rasmussen
 */
public class ScenarioService extends Service {
    /* Android logging tag */
    private static final String TAG = ScenarioService.class.getSimpleName();

    int mStartMode; // indicates behavior when service is killed
    IBinder mBinder; // interface that clients bind to

    @Override
    public void onCreate() {}

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // check for any matched discount scenarios
        checkScenarios();

        return 0;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onDestroy() {}

    /**
     * Runs through the list of all possible scenarios
     * Creates a notification to the device when a match is found
     *      -Stops loop and returns true if notification is launched, only want one
     *
     * @return boolean - Whether a scenario match was found
     */
    private boolean checkScenarios() {

        // Get snapshots needed for checks
        final TargetConnection cnn = new TargetConnection();
        final CarState state = new CarState(44.9833d, -93.2667d); // Geo coordinates of Minneapolis

        try {

            // check for nearby store before carrying on
            List<TargetStore> stores = cnn.queryNearbyStores(state.getLatitude(), state.getLongitude(), 50);

            // no stores found. No point in checking scenarios.
//            if (stores.size() == 0) return false;

            // TODO: for each scenario file ( src/com.mtu.ito.fotaito/scenarios/* ) change to loop.
            final EnergyDrinkScenario energyDrinkScenario = new EnergyDrinkScenario();

            final Intent intent = energyDrinkScenario.satisfied(state, cnn, stores, ScenarioService.this);
            if (intent != null) {
                createNotification(energyDrinkScenario.getMsg(), energyDrinkScenario.getDesc(), intent);
                return true;
            }

            Log.d(TAG, "Intent returned from satisfied was null");
            return false;

        } catch (IOException e) {
            Log.e(TAG, "Error while checking scenarios");
            return false;
        }

    }

    /**
     * Send a notification to the device
     */
    private void createNotification(String notificationTitle, String notificationText,
                                   Intent intentToLaunch) {

        // basic notification builder
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setContentTitle(notificationTitle)
                .setContentText(notificationText)
                .setSmallIcon(R.drawable.ic_launcher)
                .setAutoCancel(true);

        // activity to call on launch. Routes through StarterActivity for authentication
//        Intent mainIntent = new Intent(this, MainActivity.class);
        // StarterActivity should always be the root of the stack
        Intent mainIntent = new Intent(this, OfferActivity.class);

        mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // transfer bundles to mainIntent
        mainIntent.putExtras(intentToLaunch);
        //Bundle myBundle = intentToLaunch.getExtras();
        //mainIntent.putExtras(myBundle);

        // Create artificial stack to ensure moving back from activity leads to home screen
        //TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        //stackBuilder.addParentStack(MainActivity.class);

        // add intent that starts activity to top of stack
        //stackBuilder.addNextIntent(mainIntent);

        //PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(
        //        0,
        //        PendingIntent.FLAG_UPDATE_CURRENT
        //);

        final PendingIntent resultPendingIntent = PendingIntent.getActivity(
                this, 0, mainIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder.setContentIntent(resultPendingIntent);

        // register and send the notification
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        int mId = 1; // used to update the notification later
        mNotificationManager.notify(mId, mBuilder.build());

    }

}