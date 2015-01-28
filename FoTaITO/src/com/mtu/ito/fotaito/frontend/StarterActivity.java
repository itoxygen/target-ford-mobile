package com.mtu.ito.fotaito.frontend;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.microsoft.windowsazure.mobileservices.authentication.MobileServiceAuthenticationProvider;
import com.mtu.ito.fotaito.R;
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

        setContentView(R.layout.starter_activity);

        final Button googleButton = (Button) findViewById(R.id.button_google_sign_in);
        googleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                _db.setIdentityProvider(MobileServiceAuthenticationProvider.Google);
                login();
            }
        });

        final Button facebookButton = (Button) findViewById(R.id.button_facebook_sign_in);
        facebookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                _db.setIdentityProvider(MobileServiceAuthenticationProvider.Facebook);
                login();
            }
        });

        final Button twitterButton = (Button) findViewById(R.id.button_twitter_sign_in);
        twitterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                _db.setIdentityProvider(MobileServiceAuthenticationProvider.Twitter);
                login();
            }
        });

        //_db = AzureDatabaseManager.getInstance(this);
        _db = AzureDatabaseManager.getInstance(getApplicationContext()); // Experimental
    }

    private void login() {
        Log.d(TAG, "User not logged in, performing authentication.");

        _db.setContext(StarterActivity.this);
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
                        Toast.makeText(StarterActivity.this, "Failed to authenticate", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        if (_db.isLoggedIn() || _db.loadUserTokenCache()) { // User is already logged in
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
        final Intent intent = new Intent(this, WelcomeActivity.class);
        intent.putExtras(getIntent()); // Forward extras to MainActivity
        startActivityForResult(intent, 0, null);
    }
}