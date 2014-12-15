package com.mtu.ito.fotaito.frontend;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;
import com.microsoft.windowsazure.mobileservices.MobileServiceException;
import com.mtu.ito.fotaito.R;
import com.mtu.ito.fotaito.data.AzureDatabaseManager;
import com.mtu.ito.fotaito.data.pojos.SavedListing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Kyle Oswald
 */
public class OffersFragment extends MultiselectListFragment<SavedListing> {
    private static final String TAG = OffersFragment.class.getSimpleName();

    private AzureDatabaseManager _db;

    private AsyncTask<?, ?, ?> _task;

    private List<SavedListing> _itemList;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _db = AzureDatabaseManager.getInstance(getActivity());
        setItemAdapter(new MultiselectItemAdapter<SavedListing>() {
            @Override
            public String getPrimaryText(final SavedListing item) {
                return item.getListing().getTitle();
            }

            @Override
            public String getSecondaryText(final SavedListing item) {
                return item.getListing().getPrice();
            }

            @Override
            public String getId(final SavedListing item) {
                return item.getId();
            }
        });
    }

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final Resources res = getActivity().getResources();
        final ListView lv = getListView();

        final Drawable div = res.getDrawable(R.drawable.abc_list_divider_holo_light);
        lv.setDivider(div);

        final float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, res.getDisplayMetrics());
        lv.setDividerHeight((int) Math.ceil(px));

        setListShown(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadSavedListings();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (_task != null) {
            _task.cancel(true);
        }
    }

    @SuppressWarnings("unchecked")
    private void loadSavedListings() {
        _task = new AsyncTask<Object, Void, List<SavedListing>>() {
            @Override
            protected List<SavedListing> doInBackground(final Object... params) {
                try {
                    return _db.getSavedListings();
                } catch (MobileServiceException e) {
                    Log.e(TAG, "Failed to retrieve user saved listings.", e);
                    return Collections.emptyList();
                }
            }

            @Override
            protected void onPostExecute(final List<SavedListing> result) {
                _itemList = result;
                setItemList(result);
                setListShown(true);
                _task = null;
            }
        };

        _task.execute();
    }

    @SuppressWarnings("unchecked")
    private void deleteListings(final List<SavedListing> listings) {
        if (_task != null) { return; }

        _task = new AsyncTask<Object, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(final Object... params) {
                return _db.deleteSavedListings(listings);
            }

            @Override
            protected void onPostExecute(final Boolean result) {
                if (result) { // Just remove listings locally
                    _itemList.removeAll(listings);
                    setItemList(_itemList);
                    setListShown(true);
                    _task = null;
                    Toast.makeText(getActivity(), "Listings deleted successfully", Toast.LENGTH_LONG).show();
                } else { // One or more listings failed to delete, reload
                    loadSavedListings();
                    Toast.makeText(getActivity(), "Some listings failed to delete", Toast.LENGTH_LONG).show();
                }
            }
        };

        _task.execute();
    }

    @Override
    protected void onItemClicked(final String id) {
        final SavedListing listing = getListingForId(id);

        if (listing != null) {
            final MainActivity main = (MainActivity) getActivity();

            try {
                final Bundle args = new Bundle();
                args.putSerializable(ProductFragment.KEY_LISTING, listing);
                args.putBoolean(ProductFragment.KEY_SAVED, true);

                main.openFragment(ProductFragment.class, args);
            } catch (java.lang.InstantiationException e) {
                Log.e(TAG, "Error opening product fragment.", e);
            }
        }
    }

    private SavedListing getListingForId(final String id) {
        for (SavedListing listing : _itemList) {
            if (listing.getId().equals(id)) {
                return listing;
            }
        }

        return null;
    }

    private List<SavedListing> getSelectedListings() {
        final List<SavedListing> ret = new ArrayList<SavedListing>();

        for (String id : getSelectedSet()) {
            for (SavedListing listing : _itemList) {
                if (listing.getId().equals(id)) {
                    ret.add(listing);
                    break;
                }
            }
        }

        return ret;
    }

    @Override
    protected boolean onCreateActionMode(final ActionMode mode, final Menu menu) {
        final MenuInflater inflater = mode.getMenuInflater();
        inflater.inflate(R.menu.listings_contextual_menu, mode.getMenu());

        return true;
    }

    @Override
    protected boolean onActionItemClicked(final ActionMode mode, final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                final List<SavedListing> delete = getSelectedListings();
                deleteListings(delete);
                mode.finish();
                return true;
            default:
                return super.onActionItemClicked(mode, item);
        }
    }

    @Override
    protected void onShowSingleSelectActions(final Menu menu) {
        super.onShowSingleSelectActions(menu);
    }

    @Override
    protected void onHideSingleSelectActions(final Menu menu) {
        super.onHideSingleSelectActions(menu);
    }
}