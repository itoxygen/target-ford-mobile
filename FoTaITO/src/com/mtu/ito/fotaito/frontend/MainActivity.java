package com.mtu.ito.fotaito.frontend;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import com.mtu.ito.fotaito.R;

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

    private String _tab;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addTab(TAB_OFFERS);
        addTab(TAB_SETTINGS);
    }

    @Override
    public void onStart() {
        super.onStart();
        openTab(TAB_OFFERS);
    }

    @Override
    protected void openTab(final String tab) {
        Fragment fragment = null;

        if (_tab != null && _tab.equals(tab)) {
            return;
        } else if (TAB_OFFERS.equals(tab)) {
            fragment = new OffersFragment();
        } else if (TAB_SETTINGS.equals(tab)) {
            fragment = new SettingsFragment();
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

        super.openTab(tab);
    }

    @Override
    public void onResume() {
        super.onResume();

        // Get the intent which started the activity
        final Intent intent = this.getIntent();

        final Bundle args = intent.getExtras();
        final String fragment = args.getString(KEY_FRAGMENT);
        final String tab = args.getString(KEY_TAB);

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
        }
    }

    @Override
    public void onNewIntent(final Intent intent) {
        // onResume gets called after this to handle the intent
        setIntent(intent);
    }
}