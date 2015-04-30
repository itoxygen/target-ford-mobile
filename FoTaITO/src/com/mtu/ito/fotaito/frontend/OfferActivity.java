package com.mtu.ito.fotaito.frontend;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.mtu.ito.fotaito.R;

/**
 * Created by Keagan on 2/4/2015.
 */
public class OfferActivity extends MyActivity {

    private static final String TAG = OfferActivity.class.getSimpleName();

    // Key values
    public static final String KEY_FRAGMENT = "MainActivity.KEY_FRAGMENT";
    public static final String KEY_ARGS     = "MainActivity.KEY_ARGS";


    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_offer_view);

        try {
            setOfferDetails();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void setOfferDetails() throws ClassNotFoundException {
        Intent intent = this.getIntent();
        Bundle args = intent.getExtras();

        if (args != null) {
            final String fragment = args == null ? null : args.getString(KEY_FRAGMENT);
            Log.d(TAG, "received fragment strings");
//            TextView text = (TextView) findViewById(R.id.fragment_string);
//            text.setText(fragment);

            // launch product fragment
            final Class<? extends Fragment> fragClass = Class.forName(fragment).asSubclass(Fragment.class);

            final Fragment f = Fragment.instantiate(this, fragClass.getName(), args.getBundle(KEY_ARGS));

            // Insert the fragment by replacing any existing fragment and adding it to the back stack
            final FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, f)
                    .commit();


        } else {
            Log.e(TAG, "Offer intent args = null");
        }
    }
}
