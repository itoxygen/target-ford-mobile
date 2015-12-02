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
    private String price;
    private String image;


    public Product() {
        title = "Simply 59-oz juice";
        text = "simply-59-oz-juice-2032544139";
        price = "2/$6";
        image = "http://akimages.shoplocal.com/dyn_li/280.0.75.0/Retailers/Target/151129TEN_p30_31_p33_c_122804_20151129_PS.jpg";
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public String getPrice() {
        return price;
    }
    public String getImage() {
        return image;
    }

}