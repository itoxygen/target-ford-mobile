package com.mtu.ito.fotaito.data;

import android.content.Context;
import com.mtu.ito.fotaito.data.pojos.WeeklyAdOffer;

import java.util.List;

/**
 * Created by Kyle on 11/2/2014.
 */
public interface DatabaseManager {
    public void putOffers(List<WeeklyAdOffer> offerList);

    public List<WeeklyAdOffer> getOffers();

    public boolean login();

    public void logout();

    public Context getContext();

    public void setContext(Context context);
}
