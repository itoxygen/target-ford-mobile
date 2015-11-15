package itoxygen.mtu.fotaitov2.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;

import itoxygen.mtu.fotaitov2.R;
import itoxygen.mtu.fotaitov2.data.Product;

/**
 * Created by keagan on 10/14/15.
 */
public class ProductFragment extends Fragment {

    private static final String TAG = "ProductFragment";
    private final Gson gson;
    private MobileServiceClient azureClient;

    public ProductFragment() {
        gson = new Gson();
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_product, container, false);

        displayProduct(rootView);

        return rootView;
    }

    private void displayProduct(View v) {

        // the product was passed as an extra on the intent that launched main activity
        // from notification
        // as JSON. Get a reference to it here and translate back into Java object.
        Product prod = gson.fromJson(getActivity().getIntent().getStringExtra("product"),
                                    Product.class);

        TextView tvTitle = (TextView) v.findViewById(R.id.text_product_title);
        tvTitle.setText(prod.getTitle());

    }
}
