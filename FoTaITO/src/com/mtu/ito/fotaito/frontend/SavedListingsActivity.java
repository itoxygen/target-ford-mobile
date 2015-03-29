package com.mtu.ito.fotaito.frontend;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
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

        // ensure that page is cleared
        // this is needed when a listing is deleted and the page gets rebuilt
        RelativeLayout rl = (RelativeLayout) findViewById(R.id.layout_saved_listings);
        rl.removeAllViewsInLayout();

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

            /**
             * onPostExecute is kicked off after AsyncTask is finished
             * @param listingsCollection - List of all SavedListings
             */
            @Override
            protected void onPostExecute(final List<SavedListing> listingsCollection) {


                // gather screen information. Used in conversion from dp -> pixels
                final DisplayMetrics metrics = getResources().getDisplayMetrics();

                // variables for displaying blocks
                boolean color = true;       // swapper for alternating color
                int lastViewID = 0;         // holds a reference to the previous blocks id

                // create a listing block for each item in the collection of listings
                    for (SavedListing listing : listingsCollection) {

                        // initialize each listing block
                        lastViewID = listing.buildUIElements(SavedListingsActivity.this,
                                metrics,
                                listingsCollection,
                                lastViewID,
                                AzureDatabaseManager.getInstance(SavedListingsActivity.this));

                        // alternate bg color
                        if (color)
                            listing.setParentColor("#c0392b");
                        else
                            listing.setParentColor("#696969");
                        color = !color;

                        layout_listings.addView(listing.parentLayout, listing.parentLayoutParams);

                    }
               }

        };

        task.execute(listingsTable);

    }

}
