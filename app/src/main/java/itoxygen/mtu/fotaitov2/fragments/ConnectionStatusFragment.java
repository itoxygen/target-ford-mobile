package itoxygen.mtu.fotaitov2.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import itoxygen.mtu.fotaitov2.MainActivity;
import itoxygen.mtu.fotaitov2.R;
import itoxygen.mtu.fotaitov2.backend.ScenarioService;

/**
 * Created by keagan on 10/14/15.
 */
public class ConnectionStatusFragment extends Fragment implements OnClickListener {

    public ConnectionStatusFragment() {}

    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_connection_status, container, false);

        // Run Service Button click handler
        Button runService = (Button) rootView.findViewById(R.id.button_run_service);
        runService.setOnClickListener(this);

        Button updateEngine = (Button) rootView.findViewById(R.id.button_connection_update_engine);
        updateEngine.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_run_service:
                runScenarioService();
                break;
            case R.id.button_connection_update_engine:
                updateEngineStatus();
                break;
        }
    }

    /*
     * Kick off a scenario matching service.
     * Run the service once... just used for testing
     */
    private void runScenarioService() {
        Intent scenarioIntent = new Intent(getActivity(), ScenarioService.class);
        getActivity().startService(scenarioIntent);
    }

    private void updateEngineStatus() {
        TextView engineStatus = (TextView) rootView.findViewById(R.id.text_engine_status);
        engineStatus.setText("RPMs: " + Double.toString(MainActivity.openXCManager.getEngineStatus()) +
                    "\nBrake: " + MainActivity.openXCManager.getBrakePedalStatus() +
                    "\nSpeed: " + MainActivity.openXCManager.getVehicleSpeed() +
                    "\nLatitude: " + MainActivity.openXCManager.getLatitude() +
                    "\nLongitude: " + MainActivity.openXCManager.getLongitude());
    }
}

