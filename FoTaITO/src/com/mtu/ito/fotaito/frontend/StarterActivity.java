package com.mtu.ito.fotaito.frontend;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import com.mtu.ito.fotaito.data.AzureDatabaseManager;

/**
 * Application entry point. Handles user authentication and ensures that there
 * is database access and nothing is corrupted. All extras attached to the intent
 * which starts this activity are forwarded to {@link MainActivity} verbatim.
 * Additional functionality may be added later.
 *
 * @author Kyle Oswald
 */
public class StarterActivity extends Activity {
    private static final String TAG = StarterActivity.class.getSimpleName();

    private AzureDatabaseManager _db;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //_db = AzureDatabaseManager.getInstance(this);
        _db = AzureDatabaseManager.getInstance(getApplicationContext()); // Experimental
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!_db.isLoggedIn()) { // User must log in to access the rest of the app
            Log.d(TAG, "User not logged in, performing authentication.");

            _db.setContext(this);
            _db.login(new AzureDatabaseManager.LoginCallback() {
                @Override
                public void onSuccess(final AzureDatabaseManager sender) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.d(TAG, "Authentication success.");
                            startMainActivity();
                        }
                    });
                }

                @Override
                public void onFailure(final AzureDatabaseManager sender) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.d(TAG, "Authentication failure.");
                            finish();
                        }
                    });
                }
            });
        } else { // User is already logged in
            Log.d(TAG, "User already logged in.");
            startMainActivity();
        }
    }

    @Override
    public void onNewIntent(final Intent intent) {
        setIntent(intent);
    }

    /*
     * Called before onResume() upon MainActivity exit. If MainActivity didn't respond
     * with a RESULT_LOGOUT result code then exit, else continue to onResume() to
     * perform login.
     */
    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        Log.d(TAG, "Received result code " + resultCode + " from MainActivity.");

        if (resultCode != MainActivity.RESULT_LOGOUT) { // Exit the app
            finish();
        } else {
            //_db.logout(); Moved to MainActivity
        }
    }

    /**
     * Kick off MainActivity, onActivityResult() is called with the response
     * code when it finishes.
     */
    private void startMainActivity() {
        final Intent intent = new Intent(this, MainActivity.class);
        intent.putExtras(getIntent()); // Forward extras to MainActivity
        startActivityForResult(intent, 0, null);
    }
}