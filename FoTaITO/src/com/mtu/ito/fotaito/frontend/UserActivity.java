package com.mtu.ito.fotaito.frontend;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Toast;
import com.mtu.ito.fotaito.R;
import com.mtu.ito.fotaito.data.Bonsai;
import com.mtu.ito.fotaito.data.User;

/**
 * Created by Kyle on 9/27/2014.
 */
public class UserActivity extends DrawerActivity {
    private static final String TAG = UserActivity.class.getSimpleName();

    private static final String TAB_BADGES = "My Badges";
    private static final String TAB_NFC = "NFC";
    private static final String TAB_EMPLOYERS = "Employers";

    public static final String KEY_USER = "KEY_USER";

    private String _tab;

    private NfcAdapter _nfcAdapter;

    private volatile User _user;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addTab(TAB_BADGES);
        addTab(TAB_EMPLOYERS);

        final Bundle bundle = getIntent().getExtras();
        getUser(bundle.getString(KEY_USER));

        _nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (_nfcAdapter == null) {
            Toast.makeText(this, "NFC Not Supported", Toast.LENGTH_LONG).show();
        } else {
            final NdefRecord record = NdefRecord.createMime("application/com.example.Pearson4Life.frontend.EmployerActivity",
                    ("user:" + _user).getBytes());
            final NdefMessage msg = new NdefMessage(record);
            _nfcAdapter.setNdefPushMessage(msg, this, new Activity[] { });
        }
    }

    private void getUser(final String id) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    _user = Bonsai.getUser(Integer.parseInt(id));
                } catch (Exception e) {
                    Log.e(TAG, "Failed to retrieve user for id: " + id, e);
                }
            }
        }).start();
    }

    @Override
    public void onStart() {
        super.onStart();
        openTab(TAB_BADGES);
    }

    @Override
    protected void openTab(final String tab) {
        if (_user == null) { return; }

        Fragment fragment = null;

        if (_tab != null && _tab.equals(tab)) {
            return;
        } else if (TAB_BADGES.equals(tab)) {
            fragment = new UserBadgesFragment();
        } else if (TAB_EMPLOYERS.equals(tab)) {
            fragment = new UserEmployersFragment();

            final Bundle args = new Bundle();
            args.putString(KEY_USER, _user.getAcclaim_id());
            fragment.setArguments(args);
        }

        final FragmentManager fm = getFragmentManager();

        if (fm.getBackStackEntryCount() > 0) { // Pop non-root fragments
            popFragment();
        }

        // Insert the fragment by replacing any existing fragment
        fm.beginTransaction()
                .replace(R.id.content_frame, fragment, null)
                .commit();

        _tab = tab;

        super.openTab(tab);
    }

    @Override
    public void onResume() {
        super.onResume();

        // Check to see that the Activity started due to an Android Beam
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
            processIntent(getIntent());
        }
    }

    @Override
    public void onNewIntent(final Intent intent) {
        // onResume gets called after this to handle the intent
        setIntent(intent);
    }

    private void processIntent(final Intent intent) {
        final Parcelable[] rawMsgs = intent.getParcelableArrayExtra(
                NfcAdapter.EXTRA_NDEF_MESSAGES);
        // only one message sent during the beam
        final NdefMessage msg = (NdefMessage) rawMsgs[0];
        // record 0 contains the MIME type, record 1 is the AAR, if present
        final String id = new String(msg.getRecords()[0].getPayload());

        // push nfc fragment
        final Bundle args = new Bundle();
        args.putString("id", id);
        super.openFragment(NfcFragment.class, args);
    }
}