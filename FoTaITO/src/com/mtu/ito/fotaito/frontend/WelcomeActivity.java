package com.mtu.ito.fotaito.frontend;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.app.ActionBar;

import com.mtu.ito.fotaito.R;

/**
 * Target Ford Mobile Team
 * MTU ITOxygen
 *
 * Introduction page. Brief description of the app & how to use
 */
public class WelcomeActivity extends MyActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.welcome_activity);

    }

}
