package com.mtu.ito.fotaito.data.pojos;

import android.graphics.Color;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mtu.ito.fotaito.R;
import com.mtu.ito.fotaito.frontend.SavedListingsActivity;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

/**
 * @author Kyle Oswald
 */
public class SavedListing implements Serializable {
    // Has to be named "id" verbatim or azure freaks out
    // and i dont feel like changing the src code
    private final String id;

    public static  String TAG = SavedListing.class.getSimpleName();

    private final String _message;
    private final WeeklyAdListing _listing;
    private final String _storeId;
    private View expandableView;

    // UI vars for saved listing page

    private int marginTop;      // top margin of 1st listing
    private int marginBottom;   // spacing between each listing
    private int blockHeight;
    private boolean color;

    DisplayMetrics metrics;
    SavedListingsActivity sla;
    List<SavedListing> listingsCollection;

    public RelativeLayout parentLayout;
    public RelativeLayout.LayoutParams parentLayoutParams;
    RelativeLayout topLayout;
    RelativeLayout expandableLayout;

    public SavedListing(final String message, final WeeklyAdListing listing,
            final TargetStore store) {
        id = UUID.randomUUID().toString();
        _message = message;
        _listing = listing;
        _storeId = store.getStoreId();
        expandableView = null;
    }

    public SavedListing(final String id, final String message,
            final WeeklyAdListing listing, final String storeId) {
        this.id = id;
        _message = message;
        _listing = listing;
        _storeId = storeId;
        expandableView = null;
    }

    /**
     * Initialize all variables and objects needed for saved listings page
     */
    public int buildUIElements(SavedListingsActivity sla,
                                DisplayMetrics metrics,
                                List<SavedListing> listingsCollection,
                                int lastViewID) {
        this.metrics = metrics;
        this.sla = sla;
        this.listingsCollection = listingsCollection;

        marginBottom = (int) (metrics.density * 10f + 0.5f);
        marginTop = (int) (metrics.density * 10f + 0.5f);
        blockHeight = (int) (metrics.density * 75f + 0.5f);

        createParentLayout();
        createParentLayoutParams();
        createTopLayout();
        createExpandableLayout();

        setParentTopMargin(lastViewID);
        return parentLayout.getId();
    }

    public void createParentLayout() {
        parentLayout = new RelativeLayout(sla);

        // set click listener for the box
        parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vi) {
                //TODO fix me!
                if (expandableLayout.getLayoutParams().height == 0) {
                    // only 1 block can be expanded at a time -> collapse rest
                    for (SavedListing l : listingsCollection) {
                        if (l.expandableLayout.getLayoutParams().height > 0)
                            collapse(l.expandableLayout, 0);
                    }

                    expand(expandableLayout, blockHeight);

                } else {
                    collapse(expandableLayout, 0);
                }
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

    public void createTopLayout() {
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

    public void createExpandableLayout() {
        expandableLayout = new RelativeLayout(sla);

        // dimensions: w -> fill, h -> initially 0, to be expanded on click
        RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                0);

        rlp.addRule(RelativeLayout.BELOW, topLayout.getId());


        // add to parent
        parentLayout.addView(expandableLayout, rlp);
    }

    public void createParentLayoutParams() {
        parentLayoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);

        parentLayoutParams.bottomMargin = marginBottom;
    }

    public void setParentTopMargin(int viewID) {
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
    public void expand(final View v, final int newHeight) {
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

        a.setDuration((int)(newHeight / v.getContext().getResources().getDisplayMetrics().density));

        // start animation and refresh screens
        v.startAnimation(a);
        View parent =(View) v.getParent();
        parent.invalidate();
    }

    public void collapse(final View v, final int newHeight) {
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

        a.setDuration((int)(initialHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
        View parent = (View) v.getParent();
        parent.invalidate();
    }

    /**
     * Create a TextView object with correct settings
     *
     * @return a created TextView
     */
    protected TextView createTextView(String text) {

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
    protected ImageView createGenericImageView() {
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
//
//    public void setExpandableView(View v) { expandableView = v; }
//
//    public View getExpandableView() { return expandableView; }
}
