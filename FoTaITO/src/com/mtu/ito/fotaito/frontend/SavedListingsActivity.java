package com.mtu.ito.fotaito.frontend;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mtu.ito.fotaito.R;
import com.mtu.ito.fotaito.data.AzureDatabaseManager;
import com.mtu.ito.fotaito.data.pojos.SavedListing;

import org.apache.http.client.utils.CloneUtils;

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
                    lastViewID = listing.buildUIElements(SavedListingsActivity.this, metrics, listingsCollection, lastViewID);

                    // alternate bg color
                    if (color)
                        listing.setParentColor("#c0392b");
                    else
                        listing.setParentColor("#696969");
                    color = !color;

                    layout_listings.addView(listing.parentLayout, listing.parentLayoutParams);

                }
            }

            /**
             * Create a textview object with correct settings
             *
             * @param metrics reference to a DisplayMetrics object
             *
             * @return a created textview
             */
            protected TextView createTextView(DisplayMetrics metrics) {

                int textWidthLimit = (int) (metrics.density * 300f + 0.5f);
                TextView tv = new TextView(SavedListingsActivity.this);

                tv.setTextColor(Color.parseColor("#ffffff")); // black text
                tv.setMaxWidth(textWidthLimit);
                tv.setGravity(Gravity.LEFT);
                tv.setPadding((int) (metrics.density * 10f + 0.5f), 0, 0, 0);

                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
                lp.addRule(RelativeLayout.CENTER_VERTICAL);
                tv.setLayoutParams(lp);

                return tv;
            }

            /**
             * Create an ImageView to be used in listings page
             *
             * @param metrics
             * @return imageview with proper settings
             */
            protected ImageView createGenericImageView(DisplayMetrics metrics) {
                ImageView iv = new ImageView(SavedListingsActivity.this);

                // button settings
                iv.setImageResource(R.drawable.ic_action_down);
                iv.setPadding(0, 0, 0, (int) (metrics.density * 10f + 0.5f));

                // layout parameters
                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
                lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                lp.addRule(RelativeLayout.CENTER_VERTICAL);
                iv.setLayoutParams(lp);

                return iv;
            }

            /**
             * Animate expanding of block
             *
             * @param v - view to be expanded
             * @param newHeight - final height of expanded block
             */
            public void expand(final View v, final int newHeight) {
                Log.d(TAG, "expanding to " + newHeight);

                final int initialHeight = v.getLayoutParams().height;

                Animation a = new Animation()
                {
                    @Override
                    protected void applyTransformation(float interpolatedTime, Transformation t) {
                        v.getLayoutParams().height = (int) ((newHeight - initialHeight) * interpolatedTime + initialHeight);
                        v.requestLayout();
                    }

                    @Override
                    public boolean willChangeBounds() {
                        return true;
                    }
                };

                a.setDuration((int)(newHeight / v.getContext().getResources().getDisplayMetrics().density));
                v.startAnimation(a);
            }

            public void collapse(final View v, final int newHeight) {
                Log.d(TAG, "collapsing to " + newHeight);

                final int initialHeight = v.getLayoutParams().height;

                Animation a = new Animation()
                {
                    @Override
                    protected void applyTransformation(float interpolatedTime, Transformation t) {
                        v.getLayoutParams().height = (int) ((initialHeight - newHeight) * (1 - interpolatedTime) + newHeight);
                        v.requestLayout();
                    }

                    @Override
                    public boolean willChangeBounds() {
                        return true;
                    }
                };

                a.setDuration((int)(newHeight / v.getContext().getResources().getDisplayMetrics().density));
                v.startAnimation(a);
            }

        };

        task.execute(listingsTable);

    }

}
