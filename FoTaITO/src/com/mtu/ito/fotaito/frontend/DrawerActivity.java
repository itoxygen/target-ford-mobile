package com.mtu.ito.fotaito.frontend;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import android.widget.*;
import android.view.inputmethod.InputMethodManager;
import android.util.Log;
import com.mtu.ito.fotaito.R;

import java.util.ArrayList;

/**
 * Created by Kyle on 9/26/2014.
 */
public class DrawerActivity extends Activity {
    private static final String TAG = DrawerActivity.class.getSimpleName();

    private ArrayAdapter<DrawerItem> _drawerListAdapter;

    private DrawerLayout _drawerLayout;

    private ListView _drawerList;

    private ArrayList<DrawerItem> _itemList;

    private ActionBarDrawerToggle _drawerToggle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.drawer_activity);

        final ActionBar actionBar = getActionBar();

        _itemList = new ArrayList<DrawerItem>();

        _drawerListAdapter = new ArrayAdapter<DrawerItem>(this,
                R.layout.drawer_list_item, R.id.drawer_item, _itemList);

        _drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        _drawerList = (ListView) findViewById(R.id.left_drawer);
        _drawerList.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        _drawerList.setAdapter(_drawerListAdapter);
        _drawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent,
                                    final View view, final int position, final long id) {
                Log.d(TAG, "Navigation drawer item clicked");
                _itemList.get(position).onClicked();
                _drawerLayout.closeDrawer(_drawerList);
                //openTab(_itemList.get(position));
            }
        });

        _drawerToggle = new ActionBarDrawerToggle(this, _drawerLayout,
                R.drawable.ic_drawer, R.string.drawer_open,
                R.string.drawer_closed) {

            /** Called when a drawer has settled in a completely closed state. */
            @Override
            public void onDrawerClosed(final View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            @Override
            public void onDrawerOpened(final View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        _drawerLayout.setDrawerListener(_drawerToggle);

        // Required to allow opening drawer via app icon
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        _drawerToggle.setDrawerIndicatorEnabled(false); // Changes home button to drawer toggle
        _drawerToggle.setDrawerIndicatorEnabled(true); // Changes home button to drawer toggle
    }

    /*
    @Override
    public void onResume() {
        super.onResume();

        final FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager.getBackStackEntryCount() == 0) { // Pop fragment
    }
    */

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (_drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        if (item.getItemId() == android.R.id.home) {
            final FragmentManager fragmentManager = getFragmentManager();
            if (fragmentManager.getBackStackEntryCount() > 0) { // Pop fragment
                popFragment();
                return true;
            }
        }

        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_settings:
                openSettings();
                return true;
            case R.id.action_offers:
                openOffers();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }



//        return super.onOptionsItemSelected(item);
    }

    public void openFragment(final Class<? extends Fragment> clazz, final Bundle args) throws InstantiationException {
        closeKeyboard();

        final Fragment fragment = Fragment.instantiate(this, clazz.getName(), args);

        // Insert the fragment by replacing any existing fragment and adding it to the back stack
        final FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, fragment)
                .addToBackStack(null)
                .commit();

        _drawerToggle.setDrawerIndicatorEnabled(false); // Changes home button to up caret
    }

    public void popFragment() {
        closeKeyboard();

        final FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.popBackStack();
        _drawerToggle.setDrawerIndicatorEnabled(true); // Changes home button to drawer toggle
    }

    public void closeKeyboard() {
        final View focus = getCurrentFocus();
        if (focus instanceof EditText) {
            focus.clearFocus();
            final InputMethodManager imm = (InputMethodManager) getSystemService(
                    Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(focus.getWindowToken(), 0);
        }
    }

    protected void addDrawerItem(final DrawerItem item) {
        _itemList.add(item);
    }

    //protected void openDrawerItem(final DrawerItem item) {
    //    getActionBar().setTitle(item.toString());
    //    _drawerLayout.closeDrawer(_drawerList);
    //}

    protected void setDrawerTitle(final String title) {
        // TODO
    }

    public interface DrawerItem {
        public void onClicked();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void openSettings() {
        final Intent intent = new Intent(this, SettingsActivity.class);
        intent.putExtras(getIntent()); // Forward extras to MainActivity
        startActivityForResult(intent, 0, null);
    }

    public void openOffers() {
        final Intent intent = new Intent(this, SavedListingsActivity.class);
        intent.putExtras(getIntent()); // Forward extras to MainActivity
        startActivityForResult(intent, 0, null);
    }
}