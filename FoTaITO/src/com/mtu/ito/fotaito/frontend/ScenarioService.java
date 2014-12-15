package com.mtu.ito.fotaito.frontend;

import android.app.*;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import com.mtu.ito.fotaito.R;

/**
 * Created by Keagan on 11/9/14.
 */



public class ScenarioService extends Service{
    int mStartMode; // indicates behavior when service is killed
    IBinder mBinder; // interface that clients bind to

    @Override
    public void onCreate() {}

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        createNotification();
        return mStartMode;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onDestroy() {}


    /**
     * Send a notification to the device
     */
    public void createNotification() {

        // basic notification builder
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setContentTitle("Test Notificaiton")
                .setContentText("This is a fotaito notification")
                .setSmallIcon(R.drawable.ic_launcher)
                .setAutoCancel(true);

        // activity to call on launch
        final Intent scenIntent = new Intent(this, ScenarioActivity.class);

        // TODO: Pass a bundle along with intent, general process below...
        // make new Bundle
        // b.putString(KEY_FRAGMENT, ProductFragment.class.getName());
        // new bundle offerDetails
        // b.putString(KEY_ARGS something);

        // Create artificial stack to ensure moving back from activity leads to home screen
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(ScenarioActivity.class);
        // add intent that starts activity to top of stack
        stackBuilder.addNextIntent(scenIntent);

        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(
                0,
                PendingIntent.FLAG_UPDATE_CURRENT
        );

        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        int mId = 1; // used to update the notification later
        mNotificationManager.notify(mId, mBuilder.build());

    }

}