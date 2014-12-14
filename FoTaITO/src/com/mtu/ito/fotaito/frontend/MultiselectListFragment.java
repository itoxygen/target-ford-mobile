package com.mtu.ito.fotaito.frontend;

import android.app.ListFragment;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import com.mtu.ito.fotaito.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

@SuppressWarnings("unchecked")
public class MultiselectListFragment<T> extends ListFragment {
	@SuppressWarnings("unused")
	private static final String TAG = MultiselectListFragment.class.getSimpleName();

    private static final int LIST_ITEM_LAYOUT = R.layout.multi_select_item;

    private List<T> _itemList;
	
	private ListView _listView;

    private MultiselectItemAdapter<T> _itemAdapter;

	private MultiselectListAdapter _listAdapter;
	
	private ActionMode.Callback _cab;
	
	private ActionMode _actionMode;
	
	// Store selection based on ids rather than objects 
	// to support persistence between cursor changes
	private HashSet<String> _selectedSet;
	
	//private Thread _queryThread;

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);

		_selectedSet = new HashSet<String>();
        _itemList = Collections.emptyList();

        _cab = new Cab();

        if (_itemAdapter == null) {
            _itemAdapter = new MultiselectItemAdapter<T>() {
                @Override
                public String getPrimaryText(final T item) {
                    return item.toString();
                }

                @Override
                public String getSecondaryText(final T item) {
                    return "";
                }

                @Override
                public String getId(final T item) {
                    return Integer.toString(item.hashCode());
                }
            };
        }
	}
	
	@Override
	public void onActivityCreated(final Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		_listView = getListView();
		_listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
		
		final Resources res = getActivity().getResources();
		final ListView lv = getListView();

        final ColorDrawable div = new ColorDrawable(res.getColor(R.color.list_divider));
		lv.setDivider(div);

		final float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, res.getDisplayMetrics());
		lv.setDividerHeight((int) Math.ceil(px));
		
		_listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(final AdapterView<?> parent, final View view,
					final int position, final long id) {
				final ListItemTag tag = (ListItemTag) view.getTag();
				
				if (_actionMode == null) { // Play stream
					MultiselectListFragment.this.onItemClicked(tag.id);
				} else { // Add / remove from selection
					if (_selectedSet.contains(tag.id)) {
						deselectItem(tag);
					} else {
						selectItem(tag);
					}
				}
			}
		});
		
		_listView.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(final AdapterView<?> parent, final View view,
					final int position, final long id) {
				final ListItemTag tag = (ListItemTag) view.getTag();
				if (_selectedSet.contains(tag.id)) {
					deselectItem(tag);
				} else {
					selectItem(tag);
				}
				
				return true;
			}
		});

		setListShown(true);
	}

    protected void setItemAdapter(final MultiselectItemAdapter<T> adapter) {
        if (adapter == null) {
            throw new IllegalArgumentException();
        }

        _itemAdapter = adapter;
    }
	
	private void selectItem(final ListItemTag item) {
		if (_actionMode == null) { // Begin selection
			_actionMode = getActivity().startActionMode(_cab);
		}
		
		_selectedSet.add(item.id);
		
		item.checkSelected.setChecked(true);
		item.view.setBackgroundResource(R.drawable.multiselect_list_item_checked);

		_actionMode.setTitle(_selectedSet.size() + " selected");
		
		if (_selectedSet.size() > 1) { // Hide single-selection actions
			onHideSingleSelectActions(_actionMode.getMenu());
		}
	}
	
	private void deselectItem(final ListItemTag item) {
		_selectedSet.remove(item.id);
		
		item.checkSelected.setChecked(false);
		item.view.setBackgroundResource(R.drawable.multiselect_list_item);
		
		if (_selectedSet.isEmpty()) { // Stop selection
			_actionMode.finish();
		} else {
			_actionMode.setTitle(_selectedSet.size() + " selected");
			
			if (_selectedSet.size() == 1) { // Show single-selection actions
				onShowSingleSelectActions(_actionMode.getMenu());
			}
		}
	}
	
	protected void onItemClicked(final String id) { }

    @SuppressWarnings("unused")
	protected void onHideSingleSelectActions(final Menu menu) { }

    @SuppressWarnings("unused")
	protected void onShowSingleSelectActions(final Menu menu) { }

    @SuppressWarnings("unused")
	protected boolean onActionItemClicked(final ActionMode mode, final MenuItem item) {
		return false;
	}

    @SuppressWarnings("unused")
	protected boolean onCreateActionMode(final ActionMode mode, final Menu menu) {
		return false;
	}

    protected void setItemList(final List<T> itemList) {
        _itemList = new ArrayList<T>(itemList);
        _listAdapter.notifyDataSetChanged();
    }

	protected Iterable<String> getSelectedSet() {
		return _selectedSet;
	}
	
	@Override
	public void onPause() {
        super.onPause();

		Log.d(TAG, "onPause()");
		
		if (_listAdapter != null) {
			_selectedSet.clear();
		}
		
		if (_actionMode != null) {
			_actionMode.finish();
		}
	}

	@Override
	public void onResume() {
        super.onResume();

		Log.d(TAG, "onResume()");
		
		if (_listAdapter == null) {
			_listAdapter = new MultiselectListAdapter();
			_listView.setAdapter(_listAdapter);
		} else {
            // damn listview won't show unless the adapter is reset
            _listView.setAdapter(_listAdapter);
        }
	}

	private class MultiselectListAdapter extends BaseAdapter {
		private final List<ListItemTag> rowViews;
		
		public MultiselectListAdapter() {
			super();
			rowViews = new ArrayList<ListItemTag>();
		}
		
		public void deselectAll() {
			for (ListItemTag tag : rowViews) {
				tag.view.setBackgroundResource(R.drawable.multiselect_list_item);
				tag.checkSelected.setChecked(false);
			}
		}

        @Override
        public int getCount() {
            return _itemList.size();
        }

        @Override
        public Object getItem(final int position) {
            return _itemList.get(position);
        }

        @Override
        public long getItemId(final int position) {
            return 0; // android is stupid, this method isn't needed
        }

        @Override
        public View getView(final int position, final View convertView, final ViewGroup parent) {
            final View view = convertView == null ? newView(parent) : convertView;

            bindView(view, position);

            return view;
        }

		private View newView(final ViewGroup parent) {
			final LayoutInflater inflater = LayoutInflater.from(MultiselectListFragment.this.getActivity());
			final View v = inflater.inflate(LIST_ITEM_LAYOUT, parent, false);
			
			final ListItemTag tag = new ListItemTag();
			tag.view = v;
			tag.textPrimary = (TextView) v.findViewById(R.id.text_item_primary);
			tag.textSecondary = (TextView) v.findViewById(R.id.text_item_secondary);
			tag.checkSelected = (CheckBox) v.findViewById(R.id.check_item);
			tag.checkSelected.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(final View v) {
					final View parent = (View) v.getParent();
					final ListItemTag tag = (ListItemTag) parent.getTag();
					
					if (tag.checkSelected.isChecked()) {
						selectItem(tag);
					} else {
						deselectItem(tag);
					}
				}
			});
			v.setTag(tag);

			rowViews.add(tag);
			
			return v;
		}

		public void bindView(final View v, final int position) {
			final ListItemTag tag = (ListItemTag) v.getTag();

            final T item = _itemList.get(position);

			tag.id = _itemAdapter.getId(item);

			tag.textPrimary.setText(_itemAdapter.getPrimaryText(item));
			
			tag.textSecondary.setText(_itemAdapter.getSecondaryText(item));
			
			if (_selectedSet.contains(tag.id)) {
				tag.checkSelected.setChecked(true);
				tag.view.setBackgroundResource(R.drawable.multiselect_list_item_checked);
			} else {
				tag.checkSelected.setChecked(false);
				tag.view.setBackgroundResource(R.drawable.multiselect_list_item);
			}
		}
	}

    public interface MultiselectItemAdapter<T> {
        public String getPrimaryText(T item);

        public String getSecondaryText(T item);

        public String getId(T item);
    }

	private class ListItemTag {
		public View view;
		public TextView textPrimary, textSecondary;
		public CheckBox checkSelected;
		public String id;
	}
	
	private class Cab implements ActionMode.Callback {
		@Override
		public boolean onActionItemClicked(final ActionMode mode, final MenuItem item) {
			return MultiselectListFragment.this.onActionItemClicked(mode, item);
		}

		@Override
		public boolean onCreateActionMode(final ActionMode mode, final Menu menu) {
			return MultiselectListFragment.this.onCreateActionMode(mode, menu);
		}

		@Override
		public void onDestroyActionMode(final ActionMode mode) {
			// TODO research when / if this is called relative to other life cycle events
			
			_actionMode = null;
			_selectedSet.clear();
			
			if (_listAdapter != null) {
				_listAdapter.deselectAll();
			}
		}

		@Override
		public boolean onPrepareActionMode(final ActionMode mode, final Menu menu) {
			return false;
		}
	}
}
