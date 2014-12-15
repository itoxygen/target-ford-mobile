package com.mtu.ito.fotaito.backend;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;
import com.mtu.ito.fotaito.R;

/**
 * Created by Keagan on 11/9/14.
 */
public class ScenarioActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scenario_activity);

        // Bundled information will be used to launch the a fragment with the correct content
        Bundle extras = getIntent().getExtras();

        String fragment = extras.getString("KEY_FRAGMENT");
        String args = extras.getString("KEY_ARGS");

    }
}