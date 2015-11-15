package itoxygen.mtu.fotaitov2.data;

/**
 * Created by keagan on 10/15/15.
 *
 * Holds all information regarding a Target Product offer.
 * Used to easily pass product around
 */
public class Product {

    private String title;
    private String text;

    public Product() {
        title = "test-product";
        text = "product-text";
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }
}
