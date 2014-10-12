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
import android.widget.Toast;

/**
 * Created by Kyle on 9/26/2014.
 */
public class EmployerActivity extends DrawerActivity {
    private static final String TAG = EmployerActivity.class.getSimpleName();

    private static final String TAB_ROLES = "Roles";
    private static final String TAB_NFC = "NFC";
    private static final String TAB_USERS = "Users";

    public static final String KEY_EMPLOYER = "KEY_EMPLOYER";

    private String _tab;

    private int _employer;

    private NfcAdapter _nfcAdapter;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addTab(TAB_ROLES);
        addTab(TAB_USERS);

        final Bundle bundle = getIntent().getExtras();
        _employer = bundle.getInt(KEY_EMPLOYER);

        _nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (_nfcAdapter == null) {
            Toast.makeText(this, "NFC Not Supported", Toast.LENGTH_LONG).show();
        } else {
            final NdefRecord record = NdefRecord.createMime("application/com.example.Pearson4Life.frontend.UserActivity",
                    ("employer:" + _employer).getBytes());
            final NdefMessage msg = new NdefMessage(record);
            _nfcAdapter.setNdefPushMessage(msg, this, new Activity[] { });
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        openTab(TAB_ROLES);
    }

    @Override
    protected void openTab(final String tab) {
        Fragment fragment = null;

        if (_tab != null && _tab.equals(tab)) {
            return;
        } else if (TAB_ROLES.equals(tab)) {
            fragment = new EmployerRolesFragment();
        } else if (TAB_USERS.equals(tab)) {
            fragment = new EmployerUsersFragment();

            final Bundle args = new Bundle();
            args.putInt(KEY_EMPLOYER, _employer);
            fragment.setArguments(args);
        }

        final FragmentManager fm = getFragmentManager();

        if (fm.getBackStackEntryCount() > 0) { // Pop non-root fragments
            popFragment();
        }

        // Insert the fragment by replacing any existing fragment
        fm.beginTransaction()
                //.replace(R.id.content_frame, fragment)
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