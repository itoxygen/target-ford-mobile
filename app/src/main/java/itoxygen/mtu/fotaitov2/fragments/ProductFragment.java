package itoxygen.mtu.fotaitov2.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.nostra13.universalimageloader.core.ImageLoader;

import itoxygen.mtu.fotaitov2.R;
import itoxygen.mtu.fotaitov2.data.Product;

/**
 * Created by keagan on 10/14/15.
 */
public class ProductFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "ProductFragment";
    private final Gson gson;
    private MobileServiceClient azureClient;
    private Product prod;

    public ProductFragment() {
        gson = new Gson();
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_product, container, false);

        displayProduct(rootView);

        // Run Service Button click handler
        Button runService = (Button) rootView.findViewById(R.id.button_takemetotarget);
        runService.setOnClickListener(this);

        return rootView;
    }

    private void displayProduct(View v) {

        // the product was passed as an extra on the intent that launched main activity
        // from notification
        // as JSON. Get a reference to it here and translate back into Java object.
        prod = gson.fromJson(getActivity().getIntent().getStringExtra("product"),
                                    Product.class);

        TextView tvTitle = (TextView) v.findViewById(R.id.text_product_title);
        tvTitle.setText(prod.getTitle());

        TextView tvDesc = (TextView) v.findViewById(R.id.text_product_price);
        tvDesc.setText(prod.getPrice());


        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(prod.getImage(), (ImageView) v.findViewById(R.id.image_product));

    }

    /**
     * Launch a google maps activitiy with a pin dropped at Target.
     *
     * For now it just launches gmaps
     */
    private void launchGoogleMapsIntent() {
        // Create a Uri from an intent string. Use the result to create an Intent.
        Uri gmmIntentUri = Uri.parse("geo:46.5464,87.4067?q=target");

        // Create an Intent from gmmIntentUri. Set the action to ACTION_VIEW
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        // Make the Intent explicit by setting the Google Maps package
        mapIntent.setPackage("com.google.android.apps.maps");

        // Attempt to start an activity that can handle the Intent
        startActivity(mapIntent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_takemetotarget:
                launchGoogleMapsIntent();
        }
    }
}
