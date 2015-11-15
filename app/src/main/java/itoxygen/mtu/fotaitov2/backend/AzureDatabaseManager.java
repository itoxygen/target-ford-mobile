package itoxygen.mtu.fotaitov2.backend;

import android.content.Context;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import itoxygen.mtu.fotaitov2.data.Product;

/**
 * Created by keagan on 11/9/15.
 */
public class AzureDatabaseManager {

    private static final String AZURE_URL = "https://fotaitoservice.azure-mobile.net/";
    private static final String AZURE_KEY = "PhuGHIaLjhNSDPniKIdlUssWSdKeyC68";

    private static AzureDatabaseManager _instance;
    private MobileServiceClient client;

    public AzureDatabaseManager(Context context) {

        // init the Azure connection
        try {
            client = new MobileServiceClient(AZURE_URL, AZURE_KEY, context);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

    }

    /**
     * Add a product to the db
     *
     * @param product - product to store
     * @return boolean indicating success of read
     */
    public boolean storeProduct(Product product) {
        return false;
    }

    /**
     * Get all stored products from db
     *
     * @return list of all stored products for current user
     */
    public List<Product> getStoredProducts() {
        return new ArrayList<Product>();
    }



}
