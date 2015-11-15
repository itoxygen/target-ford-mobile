package itoxygen.mtu.fotaitov2.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import itoxygen.mtu.fotaitov2.R;

/**
 * Created by keagan on 10/14/15.
 */
public class HelpFragment extends Fragment {

    public HelpFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_help, container, false);

        return rootView;
    }
}
