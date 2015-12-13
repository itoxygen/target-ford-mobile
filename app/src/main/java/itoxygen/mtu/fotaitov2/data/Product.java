package itoxygen.mtu.fotaitov2.data;

/**
 * Created by keagan on 10/15/15.
 *
 * Holds all information regarding a Target Product offer.
 * Used to easily pass product around
 */
public class Product {

    private String title;
    private String imageURL;
    private String listingID;
    private String price;

    public Product(String titleIn,String priceIn, String imageIn, String listingIDin){
        title = titleIn;
        imageURL = imageIn;
        price = priceIn;
        listingID = listingIDin;
    }

    public Product() {
        title = "test-product";
        price = "";
        imageURL = "";
        listingID = "";
    }

    public void setPrice(String in){price = in;}

    public String getPrice(){return price;}

    public String getTitle() {
        return title;
    }

    public String getImageUrl() {return imageURL;}

    public void setTitle(String in2Title) {title = in2Title;}

    public String getListingID() {return listingID;}

    public void setListingID(String listIn) {listingID=listIn;}

    public void setImageUrl(String inURL){imageURL = inURL;}



}
