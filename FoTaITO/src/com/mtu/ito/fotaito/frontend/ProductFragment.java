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
 * @author Kyle Oswald
 */
public class ProductFragment extends Fragment {
    private static final String TAG = ProductFragment.class.getSimpleName();

    //public static final String KEY_STORE = TAG + ".KEY_STORE";
    public static final String KEY_LISTING = TAG + ".KEY_LISTING";
    //public static final String KEY_MESSAGE = TAG + ".KEY_MSG";
    public static final String KEY_SAVED = TAG + ".KEY_SAVED";

    // Fragment arguments
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View layout = inflater.inflate(R.layout.product_fragment, container, false);

        final Bundle args = this.getArguments();
        _listing = (SavedListing) args.getSerializable(KEY_LISTING);
        _isSaved = args.getBoolean(KEY_SAVED);

        final TextView messageView = (TextView) layout.findViewById(R.id.text_product_message);
        messageView.setText(_listing.getMessage());

        final TextView titleView = (TextView) layout.findViewById(R.id.text_product_title);
        titleView.setText(_listing.getListing().getTitle());

        final TextView priceView = (TextView) layout.findViewById(R.id.text_product_price);
        priceView.setText(_listing.getListing().getPrice());

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

    @Override
    public void onCreateOptionsMenu(final Menu menu, final MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.product_fragment_menu, menu);

        _saveItem = menu.findItem(R.id.action_save);

        if (_isSaved) {
            _saveItem.setIcon(R.drawable.ic_action_important);
        }
    }

    @Override
    public void onDestroyOptionsMenu() {
        _saveItem = null;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                if (!_isSaving) {
                    if (_isSaved) {
                        deleteSavedListing();
                    } else {
                        insertSavedListing();
                    }
                }

                return true;
            case R.id.action_directions:

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

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

                    if (_saveItem != null) {
                        _saveItem.setIcon(R.drawable.ic_action_not_important);
                    }
                }

                _isSaving = false;
            }
        };

        task.execute(_listing);
    }

    private void insertSavedListing() {
        _isSaving = true;

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
            }
        };

        task.execute(_listing);
    }
}