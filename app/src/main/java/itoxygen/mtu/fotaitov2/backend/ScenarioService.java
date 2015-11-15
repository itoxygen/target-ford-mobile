package itoxygen.mtu.fotaitov2.backend;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import itoxygen.mtu.fotaitov2.MainActivity;
import itoxygen.mtu.fotaitov2.R;
import itoxygen.mtu.fotaitov2.data.Product;
import itoxygen.mtu.fotaitov2.data.Scenario;

import com.google.gson.*;

/**
 * Created by keagan on 10/15/15.
 *
 * Initialized by MainActivity and set to run on an interval
 * Service runs through scenario manager to see if a match is available.
 */
public class ScenarioService extends IntentService {

    private final String TAG = "ScenarioService";
    final Gson gson;

    public ScenarioService() {
        super("ScenarioService");

        gson = new Gson();
    }

    /**
     * ScenarioService is signaled ot run when it receives an intent passed to this method.
     * Check target listings and vehicle status to attempt to find a product to push
     * @param intent
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "ScenarioService starting");

        // create vehicle object from openXC library and Target API connection manager
        VehicleManager veh = new VehicleManager();
        TargetConnectionManager tgt = new TargetConnectionManager();

        // create scenario manager object and pass it vehicle
        ScenarioManager scenarioManager = new ScenarioManager(veh, tgt);

        // use scenario manager to check all scenarios and send notification if a match is found
        Scenario matchedScenario = scenarioManager.check();

        if (matchedScenario != null)
            sendNotification(matchedScenario.getMatchedProduct());

    }

    /**
     * Send a notification to the phone.
     * @param product Product to be bundled with the notification
     */
    private void sendNotification(Product product) {

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle("Test Notification")
                .setContentText("Click Me");

        // intent to be launched within app
        Intent resultIntent = new Intent(this, MainActivity.class);
        resultIntent.putExtra("launchType", "notification");
        resultIntent.putExtra("product", gson.toJson(product));

        // ensure that navigating backwards leads to home page
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);

        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        notificationBuilder.setContentIntent(resultPendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notificationBuilder.build());
    }
}
