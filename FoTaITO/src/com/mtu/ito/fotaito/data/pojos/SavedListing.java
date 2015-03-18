package com.mtu.ito.fotaito.data.pojos;

import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mtu.ito.fotaito.R;
import com.mtu.ito.fotaito.data.AzureDatabaseManager;
import com.mtu.ito.fotaito.frontend.OfferActivity;
import com.mtu.ito.fotaito.frontend.ProductFragment;
import com.mtu.ito.fotaito.frontend.SavedListingsActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * SavedListing class
 *
 * Holds information for Azure DB storage as well as code
 * to display the listing graphically in the saved listings page
 */
public class SavedListing implements Serializable {
    // Has to be named "id" verbatim or azure freaks out
    private final String id;

    public static  String TAG = SavedListing.class.getSimpleName();

    private final String _message;
    private final WeeklyAdListing _listing;
    private final String _storeId;

    private AzureDatabaseManager db;

    // UI vars for saved listing page

    private int marginTop;      // top margin of 1st listing
    private int marginBottom;   // spacing between each listing
    private int blockHeight;    // height of each individual block

    private DisplayMetrics metrics;     // information regarding screen size
    private SavedListingsActivity sla;  // Reference used for View creation
    private List<SavedListing> listingsCollection;  // container for all instantiated objects

    public RelativeLayout parentLayout;
    public RelativeLayout.LayoutParams parentLayoutParams;
    private RelativeLayout topLayout;
    private RelativeLayout expandableLayout;

    public SavedListing(final String message, final WeeklyAdListing listing,
            final TargetStore store) {
        id = UUID.randomUUID().toString();
        _message = message;
        _listing = listing;
        _storeId = store.getStoreId();
    }

    public SavedListing(final String id, final String message,
            final WeeklyAdListing listing, final String storeId) {
        this.id = id;
        _message = message;
        _listing = listing;
        _storeId = storeId;

    }

    /**
     * Initialize all variables and objects needed for saved listings page
     */
    public int buildUIElements(SavedListingsActivity sla,
                                DisplayMetrics metrics,
                                List<SavedListing> listingsCollection,
                                int lastViewID,
                                AzureDatabaseManager db) {
        this.metrics = metrics;
        this.sla = sla;
        this.listingsCollection = listingsCollection;
        this.db = db;

        marginBottom = (int) (metrics.density * 10f + 0.5f);
        marginTop = (int) (metrics.density * 10f + 0.5f);
        blockHeight = (int) (metrics.density * 75f + 0.5f);

        createParentLayout();
        createParentLayoutParams();
        createTopLayout();
        createExpandableLayout2();

        setParentTopMargin(lastViewID);
        return parentLayout.getId();
    }

    /**
     * Parent Layout holds the background color and contains the Layouts for top and expandable
     * sections.
     */
    private void createParentLayout() {
        parentLayout = new RelativeLayout(sla);

        // set click listener for the box
        parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vi) {
                if (expandableLayout.getLayoutParams().height == 0) {
                    // only 1 block can be expanded at a time -> collapse rest
                    for (SavedListing l : listingsCollection) {
                        if (l.expandableLayout.getLayoutParams().height > 0)
                            collapse(l.expandableLayout, 0);
                    }
                    expand(expandableLayout, blockHeight);
                } else
                    collapse(expandableLayout, 0);
        }
        });

        // set background opacity
        AlphaAnimation alpha = new AlphaAnimation(0.7f, 0.7f);
        alpha.setDuration(0);
        alpha.setFillAfter(true);
        parentLayout.startAnimation(alpha);

        // generate & set an id
        parentLayout.setId(View.generateViewId());

    }

    /**
     * Create the parent layout parameters.
     * Separated to keep logic in different containers
     */
    private void createParentLayoutParams() {
        parentLayoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);

        parentLayoutParams.bottomMargin = marginBottom;
    }

    /**
     * Top Layout is always present, contains listing title and dropdown indicator
     */
    private void createTopLayout() {
        topLayout = new RelativeLayout(sla);

        // dimensions: w -> fill, h -> blockHeight
        RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                blockHeight);

        // add description and arrow
        topLayout.addView(createTextView(getListing().getTitle()));
        topLayout.addView(createGenericImageView());

        topLayout.setId(View.generateViewId());

        // add to parent
        parentLayout.addView(topLayout, rlp);
    }

    /**
     * Only visible when clicked. Contains detailed information and option buttons
     */
    private void createExpandableLayout2() {
        expandableLayout = (RelativeLayout) LayoutInflater.from(sla.getApplicationContext()).inflate(R.layout.expandable_listing, null);

        // dimensions: w -> fill, h -> initially 0, to be expanded on click
        RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                0);
        rlp.addRule(RelativeLayout.BELOW, topLayout.getId());

        final ImageButton trash = (ImageButton) expandableLayout.findViewById(R.id.imageButtonTrash);
        trash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setExpandableToDelete();
            }
        });

        final ImageButton map = (ImageButton) expandableLayout.findViewById(R.id.imageButtonMap);
        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchMaps();
            }
        });


        parentLayout.addView(expandableLayout, rlp);
    }

    private void setExpandableToDelete() {
        // set confirm text and change image
        TextView prodDes = (TextView) expandableLayout.findViewById(R.id.textViewProdDes);
        prodDes.setText("really delete?");

        final ImageButton trash = (ImageButton) expandableLayout.findViewById(R.id.imageButtonTrash);
        trash.setImageResource(R.drawable.listing_action_accept);
        trash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v2) {
                Log.d(TAG, "confirmed delete");
                List<SavedListing> delList = new ArrayList<>();
                delList.add(SavedListing.this);
//                        db.deleteSavedListings(delList);
                sla.buildListings();
            }
        });

        final ImageButton map = (ImageButton) expandableLayout.findViewById(R.id.imageButtonMap);
        map.setImageResource(R.drawable.listing_action_cancel);
        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v2) {
                Log.d(TAG, "cancelled delete");
                cancelDelete();
            }
        });
    }

    private void cancelDelete() {
        // set confirm text and change image
        TextView prodDes = (TextView) expandableLayout.findViewById(R.id.textViewProdDes);
        prodDes.setText("$prod_desc");

        final ImageButton trash = (ImageButton) expandableLayout.findViewById(R.id.imageButtonTrash);
        trash.setImageResource(R.drawable.listing_action_discard);
        trash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v2) {
                setExpandableToDelete();
            }
        });

        final ImageButton map = (ImageButton) expandableLayout.findViewById(R.id.imageButtonMap);
        map.setImageResource(R.drawable.listing_action_map);
        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v2) {
                Log.d(TAG, "goto map");
                launchMaps();
            }
        });
    }

    private void launchMaps() {
        // create UI for location specification
        Uri mapsIntentUri = Uri.parse("geo:47.1173798,-88.5646832?z=15");

        // create intend and set maps app w/ options
        Intent mapsIntent = new Intent(Intent.ACTION_VIEW, mapsIntentUri);
        mapsIntent.setPackage("com.google.android.apps.maps");

        // start maps!
        sla.startActivity(mapsIntent);
    }

    /**
     * Create an ImageView to be used in listings page
     *abc
     * @return imageview with proper settings
     */
    private ImageButton createGenericImageButton(int align) {
        ImageButton newButton = new ImageButton(sla);

        // layout parameters
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp.addRule(align);
        lp.addRule(RelativeLayout.CENTER_VERTICAL);
        lp.rightMargin = (int) (metrics.density * -10f + 0.5f);
        newButton.setLayoutParams(lp);

        // set transparent background
        int[] attrs = new int[] { R.attr.selectableItemBackground };
        TypedArray ta = sla.obtainStyledAttributes(attrs);
        Drawable drawableFromTheme = ta.getDrawable(0);
        ta.recycle();
        newButton.setBackground(drawableFromTheme);

        return newButton;
    }

    /**
     * Dictates spacing between each listing block
     *
     * Spacing is set to default marginTop if no viewID is supplied,
     * else layout is attached to the bottom of the layout above it
     *
     * @param viewID - ID of layout that will be above current layout
     */
    private void setParentTopMargin(int viewID) {
        if (viewID == 0)
            parentLayoutParams.topMargin = marginTop;
        else
            parentLayoutParams.addRule(RelativeLayout.BELOW, viewID);
    }

    public void setParentColor(String color) {
        parentLayout.setBackgroundColor(Color.parseColor(color));
    }

    /**
     * Animate expanding of block
     *
     * @param v - view to be expanded
     * @param newHeight - final height of expanded block
     */
    private void expand(final View v, final int newHeight) {
        Log.d(TAG, "expanding to " + newHeight);

        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = (int) (newHeight  * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        a.setDuration((int) (newHeight / v.getContext().getResources().getDisplayMetrics().density) * 2);

        // start animation and refresh screens
        v.startAnimation(a);
        View parent =(View) v.getParent();
        parent.invalidate();
    }

    private void collapse(final View v, final int newHeight) {
        Log.d(TAG, "collapsing to " + newHeight);

        final int initialHeight = v.getLayoutParams().height;

        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = (int) (initialHeight * (1 - interpolatedTime));
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        a.setDuration((int)(initialHeight / v.getContext().getResources().getDisplayMetrics().density)*2);
        v.startAnimation(a);
        View parent = (View) v.getParent();
        parent.invalidate();
    }

    /**
     * Create a TextView object with correct settings
     *
     * @return a created TextView
     */
    private TextView createTextView(String text) {

        int textWidthLimit = (int) (metrics.density * 300f + 0.5f);
        TextView tv = new TextView(sla);

        tv.setTextColor(Color.parseColor("#ffffff")); // black text
        tv.setMaxWidth(textWidthLimit);
        tv.setGravity(Gravity.LEFT);
        tv.setPadding((int) (metrics.density * 10f + 0.5f), 0, 0, 0);

        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.CENTER_VERTICAL);
        tv.setLayoutParams(lp);

        tv.setText(text);

        return tv;
    }

    /**
     * Create an ImageView to be used in listings page
     *
     * @return imageview with proper settings
     */
    private ImageView createGenericImageView() {
        ImageView iv = new ImageView(sla);

        // button settings
        iv.setImageResource(R.drawable.ic_action_down);
        iv.setPadding(0, 0, 0, (int) (metrics.density * 10f + 0.5f));

        // layout parameters
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        lp.addRule(RelativeLayout.CENTER_VERTICAL);
        iv.setLayoutParams(lp);

        return iv;
    }

    public String getId() {
        return id;
    }

    public String getMessage() {
        return _message;
    }

    public WeeklyAdListing getListing() {
        return _listing;
    }

    public String getStoreId() {
        return _storeId;
    }

}
