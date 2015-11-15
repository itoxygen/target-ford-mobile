package itoxygen.mtu.fotaitov2.backend;

import itoxygen.mtu.fotaitov2.data.Product;

/**
 * Created by keagan on 11/8/15.
 */
public class TargetConnectionManager {

    public TargetConnectionManager() {

    }

    /**
     * Search the Target WeeklyAd API for the matching store for a match on the given searchTerm
     * @param searchTerm, storeSlug
     * @return Product if match is found, null if no match
     */
    public Product searchWeeklyAds(String searchTerm, String storeSlug) {
        return new Product();
    }
}
