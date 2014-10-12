package com.mtu.ito.fotaito.frontend;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import com.mtu.ito.fotaito.R;

public class LoginActivity extends Activity {
    private static final String TAG = LoginActivity.class.getSimpleName();

    private static final String CLIENT_ID = "";
    private static final String REDIRECT_URI = "";

    private boolean _employerMode = true;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        final View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                switch (view.getId()) {
                    case R.id.button_employer:
                        _employerMode = true;
                        break;
                    case R.id.button_user:
                        _employerMode = false;
                        break;
                    case R.id.button_ok:
                        final EditText text = (EditText) findViewById(R.id.text_login);
                        login(text.getText().toString(), _employerMode);
                        break;
                }
            }
        };

        findViewById(R.id.button_employer).setOnClickListener(clickListener);
        findViewById(R.id.button_user).setOnClickListener(clickListener);
        findViewById(R.id.button_ok).setOnClickListener(clickListener);
    }

    private void login(final String user, final boolean employerMode) {
        if (employerMode) {
            final Intent intent = new Intent(this, EmployerActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.putExtra(EmployerActivity.KEY_EMPLOYER, Integer.parseInt((user)));
            startActivity(intent);
        } else {
            final Intent intent = new Intent(this, UserActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.putExtra(UserActivity.KEY_USER, user);
            startActivity(intent);
        }
    }
}
