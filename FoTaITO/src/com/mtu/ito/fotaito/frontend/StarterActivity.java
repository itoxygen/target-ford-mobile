package com.mtu.ito.fotaito.frontend;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import com.mtu.ito.fotaito.data.AzureDatabaseManager;

/**
 * Created by Kyle on 11/2/2014.
 */
public class StarterActivity extends Activity {
    private static final String TAG = StarterActivity.class.getSimpleName();

    private AzureDatabaseManager _db;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _db = AzureDatabaseManager.getInstance(this);
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

    // Called before onResume() on MainActivity exit. If MainActivity didn't respond
    // with a RESULT_LOGOUT result code then exit, else continue to onResume() to
    // perform login.
    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        Log.d(TAG, "Received result code " + resultCode + " from MainActivity.");

        if (resultCode != MainActivity.RESULT_LOGOUT) { // Exit the app
            finish();
        } else {
            //_db.logout();
        }
    }

    private void startMainActivity() {
        final Intent intent = new Intent(this, MainActivity.class);
        intent.putExtras(getIntent()); // Forward extras to MainActivity
        startActivityForResult(intent, 0, null);
    }
}