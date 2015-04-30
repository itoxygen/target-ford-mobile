package com.mtu.ito.fotaito.frontend;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewSwitcher;
import com.mtu.ito.fotaito.R;
import com.mtu.ito.fotaito.data.AzureDatabaseManager;
import com.mtu.ito.fotaito.data.pojos.SavedListing;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

/**
 * Target Ford Mobile Team
 * MTU ITOxygen
 *
 * Product Fragment represents the item a scenario has found. It is passed in a bundle
 * through the notification and launched from OfferActivity.
 *
 * See layout.product_fragment for UI details
 *
 * Note:
 * OfferActivity bundles two things:
 *  1. Serializable SavedListing to be used to populate layout with listing information
 *  2. Boolean on whether the listing has already been saved in Azure db
 *
 */
public class ProductFragment extends Fragment {

    private static final String TAG = ProductFragment.class.getSimpleName();

    // public keys used to pass extra arguments
    public static final String KEY_LISTING = TAG + ".KEY_LISTING";
    public static final String KEY_SAVED = TAG + ".KEY_SAVED";

    // Fragment azure arguments
    private SavedListing _listing;
    private boolean _isSaved; // listing is stored in Azure
    private boolean _isSaving; // inserting / deleting from Azure in backend

    // Layout views
    private ViewSwitcher _flipper;
    private ImageView _productImage;
    private AsyncTask<URL, Void, Bitmap> _task;

    // Acton bar items
    private MenuItem _saveItem;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    /**
     * Sets the layout view
     *
     * listing information is bundled in the fragment. once retrieved the information
     * is used to properly fill the view with information about the listing that
     * has been found
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View layout = inflater.inflate(R.layout.product_fragment, container, false);

        // get bundled listing information
        final Bundle args = this.getArguments();
        _listing = (SavedListing) args.getSerializable(KEY_LISTING);
        _isSaved = args.getBoolean(KEY_SAVED);

        final TextView messageView = (TextView) layout.findViewById(R.id.text_product_message);
        messageView.setText(_listing.getMessage());

        final TextView titleView = (TextView) layout.findViewById(R.id.text_product_title);
        titleView.setText(_listing.getListing().getTitle());

        final TextView priceView = (TextView) layout.findViewById(R.id.text_product_price);
        priceView.setText(_listing.getListing().getPrice());

        // register save button
        layout.findViewById(R.id.button_save_listing).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertSavedListing();
            }
        });

        _flipper = (ViewSwitcher) layout.findViewById(R.id.flipper);
        _productImage = (ImageView) layout.findViewById(R.id.image_product);

        // Load the product image in the background
        try {
            final URL imageUrl = new URL(_listing.getListing().getImageUrl());

            _task = new AsyncTask<URL, Void, Bitmap>() {
                @Override
                protected Bitmap doInBackground(final URL... params) {
                    Log.d(TAG, "Loading product image from " + params[0].toString());

                    try {
                        final InputStream stream = params[0].openStream();

                        try {
                            return BitmapFactory.decodeStream(stream);
                        } finally {
                            stream.close();
                        }
                    } catch (IOException e) {
                        Log.e(TAG, "Error while loading product image.", e);
                    }

                    return null;
                }

                @Override
                protected void onPostExecute(final Bitmap result) {
                    // Set the bitmap and toggle the progress bar
                    if (result != null) {
                        Log.d(TAG, "Product image loaded successfully.");
                        _productImage.setImageBitmap(result);
                        _flipper.showNext();
                    }
                }
            };

            _task.execute(imageUrl);
        } catch (MalformedURLException e) {
            Log.e(TAG, "Error while loading product image.", e);
        }

        return layout;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        _task.cancel(true); // Cancel product image loading
    }


    // Methods to be used to add and remod an item from azure saved db
    // currently unused, but will need to be re implemented soon

    private void deleteSavedListing() {
        _isSaving = true;

        final AsyncTask<SavedListing, Void, Boolean> task = new AsyncTask<SavedListing, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(final SavedListing... params) {
                try {
                    final AzureDatabaseManager db = AzureDatabaseManager
                            .getInstance(ProductFragment.this.getActivity());
                    return db.deleteSavedListings(Arrays.asList(params));
                } catch (Exception e) {
                    Log.e(TAG, "Error while deleting listing.", e);
                    return false;
                }
            }

            @Override
            protected void onPostExecute(final Boolean result) {
                if (result) {
                    _isSaved = false;

//                    if (_saveItem != null) {
//                        _saveItem.setIcon(R.drawable.ic_action_not_important);
//                    }
                }

                _isSaving = false;
            }
        };

        task.execute(_listing);
    }

    private void insertSavedListing() {
        _isSaving = true;
        Log.d(TAG, "saving listing");


        final AsyncTask<SavedListing, Void, Boolean> task = new AsyncTask<SavedListing, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(final SavedListing... params) {
                try {
                    final AzureDatabaseManager db = AzureDatabaseManager
                            .getInstance(ProductFragment.this.getActivity());
                    return db.insertSavedListings(Arrays.asList(params));
                } catch (Exception e) {
                    Log.e(TAG, "Error while inserting listing.", e);
                    return false;
                }
            }

            @Override
            protected void onPostExecute(final Boolean result) {
                if (result) {
                    _isSaved = true;

                    if (_saveItem != null) {
                        _saveItem.setIcon(R.drawable.ic_action_important);
                    }
                }

                _isSaving = false;
                Log.d(TAG, "saved? " + _isSaved);
            }
        };

        task.execute(_listing);
        Log.d(TAG, "executing save listing");
    }
}