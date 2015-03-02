package com.mtu.ito.fotaito.frontend;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mtu.ito.fotaito.R;
import com.mtu.ito.fotaito.data.AzureDatabaseManager;
import com.mtu.ito.fotaito.data.pojos.SavedListing;

import java.util.List;

/**
 * Target Ford Mobile Team
 * MTU ITOxygen
 *
 * View all listings that have been saved to azure.
 *
 * Allow viewing of individual listing as well as removing from storage
 *
 */
public class SavedListingsActivity extends MyActivity {
    private static final String TAG = ProductFragment.class.getSimpleName();


    // Layout views
    private RelativeLayout layout_listings;
    private TextView text_listings;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.saved_listings_activity);

        layout_listings = (RelativeLayout) findViewById(R.id.layout_saved_listings);
        //text_listings = (TextView) findViewById(R.id.text_listings);
        //text_listings.setText("getting listings");

        buildListings();
    }

    /**
     * Get listings from db manager and create a layout for each
     */
    public void buildListings() {
        List<SavedListing> listingsTable = null;

        Log.d(TAG, "getting saved listings");

        final AsyncTask<List<SavedListing>, Void, List<SavedListing>> task = new AsyncTask<List<SavedListing>, Void, List<SavedListing>>() {
            @Override
            protected List<SavedListing> doInBackground(List<SavedListing>... params) {
                try {
                    final AzureDatabaseManager db = AzureDatabaseManager.getInstance(SavedListingsActivity.this);
                    return db.getSavedListings();

                } catch (Exception e) {
                    Log.e(TAG, "Error retrieving saved listings from azure database");
                    return null;
                }
            }

            @Override
            protected void onPostExecute(final List<SavedListing> list) {
//                text_listings.setText(list.toString());

                String s = "";

                boolean color = false;

                DisplayMetrics metrics = getResources().getDisplayMetrics();

                float margin_top = 50f;

                for (SavedListing sl : list) {

                    int margin_pixels = (int) (metrics.density * margin_top + 0.5f);
                    int layout_pixels = (int) (metrics.density * 75f + 0.5f);

                    AlphaAnimation alpha = new AlphaAnimation(0.7f, 0.7f);
                    alpha.setDuration(0);
                    alpha.setFillAfter(true);

                    s += sl.getListing().getTitle() + "\n";
                    RelativeLayout new_listing_layout = new RelativeLayout(SavedListingsActivity.this);
                    RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.MATCH_PARENT,
                            layout_pixels);

                    new_listing_layout.startAnimation(alpha);

                    if (color) {
                        new_listing_layout.setBackgroundColor(Color.parseColor("#76767688"));
                        color = false;
                    }
                    else {
                        new_listing_layout.setBackgroundColor(Color.parseColor("#a8a8a8"));
                        color = true;
                    }

                    TextView tv = new TextView(SavedListingsActivity.this);
                    tv.setText(sl.getListing().getTitle());

                    tv.setTextColor(Color.parseColor("#000000"));

                    RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.WRAP_CONTENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT);

                    rlp.topMargin = margin_pixels;




                    tv.setLayoutParams(lp);

                    new_listing_layout.addView(tv);

                    layout_listings.addView(new_listing_layout, rlp);

                    margin_top += 75f;
                }


                Log.d(TAG, "getSavedListings task executed successfully? returned " + list.toString());

            }
        };

        task.execute(listingsTable);

    }

}
