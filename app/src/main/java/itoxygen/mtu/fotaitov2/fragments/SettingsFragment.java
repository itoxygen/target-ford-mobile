package itoxygen.mtu.fotaitov2.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appyvet.rangebar.RangeBar;

import itoxygen.mtu.fotaitov2.R;

/**
 * Created by keagan on 10/14/15.
 */
public class SettingsFragment extends Fragment {

    public static final String TAG = "SettingsFragment";

    public SettingsFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);

        // listener for scenario timer interval
        RangeBar intervalBar = (RangeBar) rootView.findViewById(R.id.rangebar_interval);
        intervalBar.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex, int rightPinIndex, String leftPinValue, String rightPinValue) {
                Log.d(TAG, "Range Bar Change: leftPinIndex = " + leftPinIndex +
                            " leftPinValue = " + leftPinValue +
                            " rightPinIndex = " + rightPinIndex +
                            " rightPinValue = " + rightPinValue);
            }
        });

        // listener for max distance to target store
        RangeBar maxDistanceBar = (RangeBar) rootView.findViewById(R.id.rangebar_store_distance);
        maxDistanceBar.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex, int rightPinIndex, String leftPinValue, String rightPinValue) {
                Log.d(TAG, "Range Bar Change: leftPinIndex = " + leftPinIndex +
                        " leftPinValue = " + leftPinValue +
                        " rightPinIndex = " + rightPinIndex +
                        " rightPinValue = " + rightPinValue);
            }
        });

        return rootView;
    }
}
