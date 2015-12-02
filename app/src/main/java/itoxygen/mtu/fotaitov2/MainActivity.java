package itoxygen.mtu.fotaitov2;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import itoxygen.mtu.fotaitov2.backend.OpenXCManager;
import itoxygen.mtu.fotaitov2.fragments.ConnectionStatusFragment;
import itoxygen.mtu.fotaitov2.fragments.HelpFragment;
import itoxygen.mtu.fotaitov2.fragments.ProductFragment;
import itoxygen.mtu.fotaitov2.fragments.SavedItemsFragment;
import itoxygen.mtu.fotaitov2.fragments.SettingsFragment;


/**
 * Entry point of app. Also the only activity.
 *
 * Handles the navigation drawer and toolbar, as well as handling the insertion of fragments into
 * the FrameLayout.
 */
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MainActivity";

    // static ford manager.
    // not the best way to implement, but this class needs to be accessed from pretty
    // much everywhere and for now this will have to do.
    public static OpenXCManager openXCManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // init image loader
        // Create global configuration and initialize ImageLoader with this config
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();
        ImageLoader.getInstance().init(config);

        // create and populate the side drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // set default fragment to either savedlisting or product page depending on how app was
        // launched
        setInitialFragment(getIntent().getStringExtra("launchType"));

        // Initialize OpenXC connection
        openXCManager = new OpenXCManager();
        openXCManager.init(this);
        openXCManager.bind();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    /**
     * Determine if activity was launched from a notification and redirect to proper fragment
     * @param launchType - String containing "notification if launched via notification", contains
     *                   null if launched via user action
     */
    private void setInitialFragment(String launchType) {
        if (launchType != null) { // TODO: change this to whatever the string actually is
            launchFragment(new ProductFragment());
        } else {
            // set default page to SavedItemsFragment
            launchFragment(new SavedItemsFragment());
            setTitle(getResources().getStringArray(R.array.nav_titles)[0]);
            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.getMenu().getItem(0).setChecked(true);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        String[] navTitles = getResources().getStringArray(R.array.nav_titles);

        Fragment frag;
        String navTitle;

        // Find fragment to display
        int id = item.getItemId();
        if (id == R.id.nav_previous_offers) {
            frag = new SavedItemsFragment();
            navTitle = navTitles[0];
        } else if (id == R.id.nav_connection_status) {
            frag = new ConnectionStatusFragment();
            navTitle = navTitles[1];
        } else if (id == R.id.nav_settings) {
            frag = new SettingsFragment();
            navTitle = navTitles[2];
        } else if (id == R.id.nav_help) {
            frag = new HelpFragment();
            navTitle = navTitles[3];
        } else {
            // set default view
            frag = new SavedItemsFragment();
            navTitle = navTitles[0];
        }

        launchFragment(frag);
        setTitle(navTitle);

        // minimize the drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    /**
     * Handle the actual expeanding of a fragment onto the main activity.
     * @param frag - Fragment to be placed inside of page
     */
    private void launchFragment(Fragment frag) {
        FragmentManager fragManager = getFragmentManager();
        fragManager.beginTransaction().replace(R.id.frame_container, frag).commit();
    }


}
