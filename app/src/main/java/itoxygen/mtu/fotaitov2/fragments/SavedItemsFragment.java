package itoxygen.mtu.fotaitov2.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import itoxygen.mtu.fotaitov2.R;
import itoxygen.mtu.fotaitov2.data.Product;

/**
 * Created by keagan on 10/14/15.
 */
public class SavedItemsFragment extends Fragment {

    private static final String TAG = "SavedItemsFragment";

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public SavedItemsFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Log.d(TAG, "Inflating SavedItemsFragment");

        View rootView = inflater.inflate(R.layout.fragment_saved_items, container, false);

        // setup recycler view for cards
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycle_view_saved_products);
        mLayoutManager = new LinearLayoutManager(rootView.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        ArrayList<Product> products = new ArrayList<>();
        products.add(new Product());
        products.add(new Product());
        products.add(new Product());
        products.add(new Product());
        products.add(new Product());


        mAdapter = new MyAdapter(products);
        mRecyclerView.setAdapter(mAdapter);

        return rootView;
    }
}

