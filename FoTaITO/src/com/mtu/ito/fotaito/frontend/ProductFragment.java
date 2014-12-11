package com.mtu.ito.fotaito.frontend;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;
import android.widget.ViewSwitcher;
import com.mtu.ito.fotaito.R;
import com.mtu.ito.fotaito.data.pojos.TargetStore;
import com.mtu.ito.fotaito.data.pojos.WeeklyAdListing;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author Kyle Oswald
 */
public class ProductFragment extends Fragment {
    private static final String TAG = ProductFragment.class.getSimpleName();

    public static final String KEY_STORE = TAG + ".KEY_STORE";
    public static final String KEY_LISTING = TAG + ".KEY_TAG";
    public static final String KEY_MESSAGE = TAG + ".KEY_MSG";

    private String _message;
    private WeeklyAdListing _listing;
    private TargetStore _store;

    private ViewSwitcher _flipper;
    private ImageView _productImage;
    private AsyncTask<URL, Void, Bitmap> _task;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View layout = inflater.inflate(R.layout.product_fragment, container, false);

        final Bundle args = this.getArguments();
        _message = args.getString(KEY_MESSAGE);
        _listing = (WeeklyAdListing) args.getSerializable(KEY_LISTING);
        _store = (TargetStore) args.getSerializable(KEY_STORE);

        final TextView messageView = (TextView) layout.findViewById(R.id.text_product_message);
        messageView.setText(_message);

        final TextView titleView = (TextView) layout.findViewById(R.id.text_product_title);
        titleView.setText(_listing.getTitle());

        final TextView priceView = (TextView) layout.findViewById(R.id.text_product_price);
        priceView.setText(_listing.getPrice());

        _flipper = (ViewSwitcher) layout.findViewById(R.id.flipper);
        _productImage = (ImageView) layout.findViewById(R.id.image_product);

        try {
            final URL imageUrl = new URL(_listing.getImageUrl());

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
        _task.cancel(true);
    }
}