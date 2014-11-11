package com.mtu.ito.fotaito.frontend;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import com.mtu.ito.fotaito.R;
import com.mtu.ito.fotaito.data.AzureDatabaseManager;

import java.util.HashMap;

/**
 * Created by Kyle on 11/2/2014.
 */
public class MainActivity extends DrawerActivity {
    // Always start a new activity/fragment/service with this line (replace class name)
    /* Android logging tag */
    private static final String TAG = MainActivity.class.getSimpleName();

    // Key values recognized in received intents
    public static final String KEY_FRAGMENT = "MainActivity.KEY_FRAGMENT";
    public static final String KEY_ARGS     = "MainActivity.KEY_ARGS";
    public static final String KEY_TAB      = "MainActivity.KEY_TAB";

    public static final String TAB_OFFERS   = "MainActivity.TAB_OFFERS";
    public static final String TAB_SETTINGS = "MainActivity.TAB_SETTINGS";

    // Results to be propagated back to StarterActivity
    public static final int RESULT_EXIT   = 0;
    public static final int RESULT_LOGOUT = 1;

    private final HashMap<String, DrawerItem> _tabMap = new HashMap<String, DrawerItem>();

    private String _tab;

    private AzureDatabaseManager _db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        _db = AzureDatabaseManager.getInstance(this);

        setDrawerTitle(_db.getLoggedInUserId());

        // My Offers Item
        addDrawerItem(new DrawerItem() {
            @Override
            public void onClicked() {
                openTab(TAB_OFFERS);
            }

            @Override
            public String toString() {
                return "My Offers";
            }
        });

        // Settings Item
        addDrawerItem(new DrawerItem() {
            @Override
            public void onClicked() {
                openTab(TAB_SETTINGS);
            }

            @Override
            public String toString() {
                return "Settings";
            }
        });

        // Logout Item
        addDrawerItem(new DrawerItem() {
            @Override
            public void onClicked() {
                _db.logout();
                setResult(RESULT_LOGOUT); // Let the starter know to re-login
                finish();
            }

            @Override
            public String toString() {
                return "Logout";
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private void openTab(final String tab) {
        Fragment fragment = null;
        String title = null;

        if (_tab != null && _tab.equals(tab)) {
            return;
        } else if (TAB_OFFERS.equals(tab)) {
            fragment = new OffersFragment();
            title = "My Offers";
        } else if (TAB_SETTINGS.equals(tab)) {
            fragment = new SettingsFragment();
            title = "Settings";
        }

        final FragmentManager fm = getFragmentManager();

        if (fm.getBackStackEntryCount() > 0) { // Pop non-root fragments
            popFragment();
        }

        // Insert the fragment by replacing any existing fragment
        fm.beginTransaction()
          .replace(R.id.content_frame, fragment)
          .commit();

        _tab = tab;

        getActionBar().setTitle(title);
    }

    @Override
    public void onResume() {
        super.onResume();

        // Get the intent which started the activity
        final Intent intent = this.getIntent();

        final Bundle args = intent.getExtras();
        final String fragment = args == null ? null : args.getString(KEY_FRAGMENT);
        final String tab = args == null ? null : args.getString(KEY_TAB);

        if (fragment != null) { // If the intent specifies a fragment, open it
            try {
                final Class<? extends Fragment> clazz = Class.forName(fragment).asSubclass(Fragment.class);
                openFragment(clazz, args.getBundle(KEY_ARGS));
            } catch (ClassNotFoundException e) {
                Log.e(TAG, "Failed to locate class for " + fragment, e);
            } catch (ClassCastException e) {
                Log.e(TAG, "Specified class does not extend FoTaITOFragment.", e);
            } catch (InstantiationException e) {
                Log.e(TAG, "Failed to instantiate fragment for "  + fragment, e);
            }
        } else if (tab != null) { // Open specified tab
            openTab(tab);
        } else if (_tab == null) { // Open default tab on creation
            openTab(TAB_OFFERS);
        }
    }

    @Override
    public void onNewIntent(final Intent intent) {
        // onResume gets called after this to handle the intent
        setIntent(intent);
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_EXIT); // Let the starter know not to re-login
        finish();
    }
}