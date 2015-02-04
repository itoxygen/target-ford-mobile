package com.mtu.ito.fotaito.frontend;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.mtu.ito.fotaito.R;

/**
 * Target Ford Mobile Team
 * MTU ITOxygen
 *
 * Introduction page. Brief description of the app & how to use
 */
public class WelcomeActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.welcome_activity);

    }

    /**
     * Create the action bar menu
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_settings:
                openSettings();
                return true;
            case R.id.action_offers:
                openOffers();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void openSettings() {
        final Intent intent = new Intent(this, SettingsActivity.class);
        intent.putExtras(getIntent()); // Forward extras to MainActivity
        startActivityForResult(intent, 0, null);
    }

    public void openOffers() {
        final Intent intent = new Intent(this, SavedListingsActivity.class);
        intent.putExtras(getIntent()); // Forward extras to MainActivity
        startActivityForResult(intent, 0, null);
    }
}
