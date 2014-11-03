package com.mtu.ito.fotaito.frontend;

import android.app.Fragment;
import android.app.ListFragment;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import com.mtu.ito.fotaito.R;

/**
 * Created by Kyle on 11/2/2014.
 */
public class OffersFragment extends ListFragment {
    private static final String TAG = OffersFragment.class.getSimpleName();

    private ListAdapter _adapter;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onListItemClick(final ListView list, final View view,
                                final int position, final long id) {

    }

}