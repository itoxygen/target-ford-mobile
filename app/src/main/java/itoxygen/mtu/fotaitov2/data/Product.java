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
    private String imageURL;
    private String listingID;

    public Product(String titleIn,String textIn, String imageIn, String listingID){
        title = titleIn;
        text = textIn;
        imageURL = imageIn;
    }

    public Product() {
        title = "test-product";
        text = "product-text";
        imageURL = "";
        listingID = "";
    }

    public String getTitle() {
        return title;
    }

    public String getImageUrl() {return imageURL;}

    public String getText() {
        return text;
    }

    public void setText(String in2Text) {text = in2Text;}

    public void setTitle(String in2Title) {title = in2Title;}

    public String getListingID() {return listingID;}

    public void setListingID(String listIn) {listingID=listIn;}

    public void setImageUrl(String inURL){imageURL = inURL;}



}
