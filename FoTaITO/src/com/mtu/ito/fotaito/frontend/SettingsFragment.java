package com.mtu.ito.fotaito.frontend;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.mtu.ito.fotaito.R;
import android.content.Intent;



/**
 * Created by Kyle on 11/2/2014.
 */
public class SettingsFragment extends Fragment {
    private static final String TAG = SettingsFragment.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getView();

        // create view first, as it is needed to find button IDs
        View newView = inflater.inflate(R.layout.settings_fragment, container, false);

        // register the button to redirect user to sandbox (testing) activity
        final Button sandboxButton = (Button) newView.findViewById(R.id.button_tosandbox);
        sandboxButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Intent intent = new Intent(getActivity(), SandboxActivity.class);
                startActivity(intent);
            }
        });

        // return the view that was created earlier
        return newView;
    }
}