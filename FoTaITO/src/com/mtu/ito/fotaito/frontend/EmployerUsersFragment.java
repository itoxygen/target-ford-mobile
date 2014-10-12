package com.mtu.ito.fotaito.frontend;

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
import android.widget.*;
import com.mtu.ito.fotaito.R;
import com.mtu.ito.fotaito.data.*;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Kyle on 9/26/2014.
 */
public class EmployerUsersFragment extends ListFragment {
    private static final String TAG = UserEmployersFragment.class.getSimpleName();

    private LocationManager _lm;

    private Location _location;

    private ListAdapter _adapter;

    private int _employer;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        _lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        _location = _lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
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

        _employer = getArguments().getInt(EmployerActivity.KEY_EMPLOYER);

        Log.i(TAG, "Employer: " + _employer);

        if (_location != null) {
            search(_location.getLatitude(), _location.getLongitude());
    }
    }

    @Override
    public void onListItemClick(final ListView list, final View view,
                                final int position, final long id) {

    }

    @SuppressWarnings("unchecked")
    private void search(final double lat, final double lon) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final User[] users = Bonsai.getNearbyUsers(lat, lon);

                    final List<Badge>[] badges = (List<Badge>[]) new List[users.length];

                    for (int i = 0; i < users.length; i++) {
                        badges[i] =  Arrays.asList(Pearson.getUserBadges(users[i].getAcclaim_id()));
                    }

                    final Employer employer = Bonsai.getEmployer(_employer);

                    _adapter = new ArrayAdapter<User>(EmployerUsersFragment.this.getActivity(),
                            R.layout.search_list_item, 0, users) {
                        final List<Badge>[] _badges = badges;

                        @Override
                        public View getView (final int position, final View convertView, final ViewGroup parent) {
                            final View view = convertView == null ? createView(parent) : convertView;

                            try {
                                final User user = getItem(position);
                                final List<Badge> badges = _badges[position];

                                int maxPercent = 0;
                                String maxRole = "N/A";

                                for (Role role : employer.getRoles()) {
                                    int count = 0;
                                    final String[] roleBadges = role.getBadges();
                                    for (int i = 0; i < roleBadges.length; i++) {
                                        if (badges.contains(roleBadges[i])) {
                                            count++;
                                        }
                                    }

                                    count = count / roleBadges.length;
                                    if (maxPercent <= count) {
                                        maxPercent = count;
                                        maxRole = role.getName();
                                    }
                                }

                                final TextView textEmployer = (TextView) view.findViewById(R.id.text_user);
                                final TextView textRole = (TextView) view.findViewById(R.id.text_role);
                                final TextView textMatch = (TextView) view.findViewById(R.id.text_match);

                                textEmployer.setText(user.getName());
                                textRole.setText(maxRole);
                                textMatch.setText(maxPercent + "%");
                            } catch (Exception e) {
                                Log.e(TAG, "IDK", e);
                            }

                            return view;
                        }

                        private View createView(final ViewGroup parent) {
                            final LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            final View ret = inflater.inflate(R.layout.search_list_item, parent, false);
                            if (ret == null) {
                                Log.i(TAG, "View is NULL");
                            }
                            return ret;
                        }
                    };

                    EmployerUsersFragment.this.getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setListAdapter(_adapter);
                            setListShown(true);
                        }
                    });
                } catch (Exception e) {
                    Log.e(TAG, "Failed to retrieve local users list.", e);

                    EmployerUsersFragment.this.getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(EmployerUsersFragment.this.getActivity(), "Failed to retrieve local users list.",
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        }).start();
    }
}