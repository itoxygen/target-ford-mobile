package itoxygen.mtu.fotaitov2.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import itoxygen.mtu.fotaitov2.R;

/**
 * Created by keagan on 10/14/15.
 */
public class SavedItemsFragment extends Fragment {

    public SavedItemsFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_saved_items, container, false);

        return rootView;
    }
}
