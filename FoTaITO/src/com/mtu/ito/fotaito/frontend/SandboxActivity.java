package com.mtu.ito.fotaito.frontend;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Toast;
import com.mtu.ito.fotaito.R;

/**
 * Created by Keagan on 11/9/14.
 */
public class SandboxActivity extends Activity{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.sandbox_activity);

        // Set click listeners
        findViewById(R.id.button_runservice).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startScenService();
            }
        });

        findViewById(R.id.button_startservice).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startServiceTimer();
            }
        });

        findViewById(R.id.button_stopservice).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopServiceTimer();
            }
        });

    }

    /**
     * Start a timer to run the ScenarioService every x amount of time
     */
    public void startServiceTimer() {
        Toast.makeText(getApplicationContext(), "Started Timer svc", Toast.LENGTH_SHORT).show();
        Intent svcIntent = new Intent(this, ScenarioService.class);
        PendingIntent alarmIntent = PendingIntent.getService(this, 0, svcIntent, 0);
        AlarmManager alarmMgr = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);

        alarmMgr.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime(),
                5 * 1000,
                alarmIntent);

    }

    /**
     * Cancel the timer that runs ScenarioService
     */
    public void stopServiceTimer() {
        Toast.makeText(getApplicationContext(), "Stopped Timer svc", Toast.LENGTH_SHORT).show();
        Intent svcIntent = new Intent(this, ScenarioService.class);
        PendingIntent alarmIntent = PendingIntent.getService(this, 0, svcIntent, 0);
        AlarmManager alarmMgr = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);

        alarmMgr.cancel(alarmIntent);

    }

    /**
     * Manually run the scenario service once
     */
    public void startScenService() {
        Intent serviceIntent = new Intent(this, ScenarioService.class);
        startService(serviceIntent);
    }
}