package itoxygen.mtu.fotaitov2.backend;

import itoxygen.mtu.fotaitov2.data.Product;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.*;
import java.io.*;
import java.io.InputStreamReader;
import java.net.*;

import java.io.UnsupportedEncodingException;
/**
 * Created by keagan on 11/8/15.
 * Edited by Marco on 12/2/15
 */
public class TargetConnectionManager {
    private String keyAPI = "ltVVgaQ8HQNxDADa7FPyVJifTuRIItIL";
    private String storeSlug = "";
    private String storeID = "";
    
    public TargetConnectionManager(){
    }

    //returns the slug of the nearest store  useing the longitude and latitude
    //intended for use with a gps.
    private String getIDGPS(String lat, String longitude,String range){
        //Things for the request
        String urlEndpoint = "http://api.target.com/v2/store?";
        String arguments = "nearby=" + longitude + "," + lat + 
                "&range=" + range + "&locale=en-US";
        String ID = null;
        //Final url for the request
        String  locationURL = urlEndpoint + arguments + "&key=" + keyAPI ;
        URL url = null;
        HttpURLConnection request = null;

        try{
            url = new URL(locationURL);
        }

        catch(MalformedURLException urlFail){
            System.out.println("Malformed URL");
            return null;
        }

        try{
            System.out.println("Here we go");
            request = (HttpURLConnection) url.openConnection();
            request.setRequestProperty("Accept","application/json");
            request.setRequestMethod("GET");
            request.connect();
            System.out.println(request.toString());
            InputStream inStream = new BufferedInputStream(request.getInputStream());
            JsonParser jp = new JsonParser();
            JsonElement parsed = jp.parse(new InputStreamReader((InputStream) request.getInputStream())); //Convert the input stream to a json element
            //convert the response to a gson object
            JsonObject storedata = parsed.getAsJsonObject(); //May be an array, may be an object.
            //check to see if the api request worked
            JsonObject peal1 = storedata.get("Locations").getAsJsonObject();
            
            try{
            
                if(peal1.get("Location").isJsonArray()){
                    System.out.println("Multiple Stores Found, finding closest");
                    JsonArray peal2 = storedata.get("Locations").getAsJsonObject().getAsJsonArray("Location");
                    ID = peal2.get(0).getAsJsonObject().getAsJsonPrimitive("ID").getAsString();
                    System.out.println("Store found: " + ID);
                }
            
                else{
                    ID = peal1.getAsJsonObject("Location").getAsJsonPrimitive("ID").getAsString();
                    System.out.println("Store found: " + ID);
                }
            }
            
            catch(NullPointerException pointy){
                System.out.println("Store not found");
                return null;
            }
                    //.get(0).getAsJsonObject();
            
                    //.getAsJsonPrimitive("ID").getAsString();
            
            
        }

        catch(java.io.IOException connectionFailure){
            System.out.println("Error connecting to API service");
            return null;
        }
        
        storeID = ID;
        return ID;


    }

                
    
    public String getSlug(String zip,String range){
       // String ID = getIDZip(zip,range);
        //System.out.printf("\n\n\n\n\n\n Slug Parse: ");
        //System.out.println(ID);
        String urlEndpoint = "http://api.target.com/v1/promotions/weeklyad/storeslugs?";
        String arguments = "citystatezip=" + zip;
        //Final url for the request
        String  slugURL = urlEndpoint + arguments + "&key=" + keyAPI ;
        URL url = null;
        HttpURLConnection request = null;
        String slug = null;
        try{
            url = new URL(slugURL);
        }

        catch(MalformedURLException urlFail){
            System.out.println("MalFormed URL");
            return null;
        }

        try{
            request = (HttpURLConnection) url.openConnection();
            request.setRequestProperty("Accept","application/json");
            request.setRequestMethod("GET");
            request.connect();
            System.out.println("Connection worked");
            System.out.println(request.toString());
            InputStream inStream = new BufferedInputStream(request.getInputStream());
            JsonParser jp = new JsonParser();
            JsonElement parsed = jp.parse(new InputStreamReader((InputStream) request.getInputStream())); //Convert the input stream to a json element
            //convert the response to a gson object
            JsonObject storedata = parsed.getAsJsonObject(); //May be an array, may be an object.
            
            try{

                if(storedata.get("stores").isJsonArray()){
                    System.out.println("Multiple Stores Found, Finding Closest");
                    JsonArray peal2 = storedata.getAsJsonArray("stores");
                    slug = peal2.get(0).getAsJsonObject().getAsJsonPrimitive("slug").getAsString();
                    System.out.println("Found Slug:" + slug);
                }

                else{
                    slug = storedata.getAsJsonObject("stores").getAsJsonPrimitive("slug").getAsString();
                    System.out.println("Found Slug:" + slug);
                }
            }
            
            catch(NullPointerException storeNotFound){
                    System.out.println("Store not found");
                    return null;
            }
        }

        catch(java.io.IOException connectionFailure){
            System.out.println("Error connecting to api service.");
            return null;
        }
        
        return slug;
    }
    
    private String getZip(String storeId){
        String urlEndpoint = "http://api.target.com/v2/location/";
        String doneUrl = urlEndpoint + storeID + "?locale=en-US&key=" + keyAPI;
        URL url;
        String zip = "";
        //Final url for the request
        String reqURL = doneUrl;
        HttpURLConnection request = null;

        try{
            url = new URL(reqURL);
        }

        catch(MalformedURLException urlFail){
            System.out.println("Malformed URL");
            return null;
        }

        try{
            request = (HttpURLConnection) url.openConnection();
            request.setRequestProperty("Accept","application/json");
            request.setRequestMethod("GET");
            request.connect();
            System.out.println("Connection worked");
            System.out.println(request.toString());
            JsonParser jp = new JsonParser();
            JsonElement parsed = jp.parse(new InputStreamReader((InputStream) request.getInputStream())); //Convert the input stream to a json element
           //convert the response to a gson object
            JsonObject storedata = parsed.getAsJsonObject(); //May be an array, may be an object.
            //check to see if the api request worked
            
            try{
                
                String zipRaw = storedata.getAsJsonObject("Address")
                     .getAsJsonPrimitive("PostalCode").getAsString();
                zip = zipRaw.split("-")[0];
                System.out.println(zip);
                return zip;
            }
            
            catch(Exception notFound){
                System.out.println("store not found");
                return null;
            }
                    
            
        }

        catch(java.io.IOException connectionFailure){
            System.out.println("Error connecting to api service");
            return null;
        }
        
    }
    
    
    public String getSlug(String lat,String longitude, String range){
        //get the id of the closest store
        String ID = getIDGPS(lat,longitude,range);
        String zip = getZip(ID);
        storeSlug = getSlug(zip,range);
        return storeSlug;
    }

    /**
     * Search the Target WeeklyAd API for the matching store for a match on the given searchTerm
     * @param searchTerm, 
     * @param storeSlug
     * @return Product if match is found, null if no match
     */
    public Product searchWeeklyAds(String searchTerm, String storeSlug) {
        //Things for the request
        String urlEndpoint = "http://api.target.com/v1/promotions/weeklyad/";
        URL url;
        String title = "";
        String image ="";
        String listingID = "";
        String price = "";
        //Final url for the request
        String reqURL = urlEndpoint + storeSlug + "/search?q=" + searchTerm + "&key=" + keyAPI;
        HttpURLConnection request = null;

        try{
            url = new URL(reqURL);
        }

        catch(MalformedURLException urlFail){
            System.out.println("Malformed URL");
            return null;
        }

        try{
            request = (HttpURLConnection) url.openConnection();
            request.setRequestProperty("Accept","application/json");
            request.setRequestMethod("GET");
            request.connect();
            System.out.println("Connection worked");
            System.out.println(request.toString());
            JsonParser jp = new JsonParser();
            System.out.println("parser worked");
            JsonElement parsed = jp.parse(new InputStreamReader((InputStream) request.getInputStream())); //Convert the input stream to a json element
           //convert the response to a gson object
            JsonObject storedata = parsed.getAsJsonObject(); //May be an array, may be an object.
            //check to see if the api request worked
            
            try{
                if(storedata.get("listings").isJsonArray()){
                    JsonArray listings = storedata.getAsJsonArray("listings");
                    System.out.println("Multiple results found selecting first:");
                    title = listings.get(0).getAsJsonObject().getAsJsonPrimitive("title").getAsString();
                    image = listings.get(0).getAsJsonObject().getAsJsonPrimitive("image").getAsString();
                    listingID = listings.get(0).getAsJsonObject().getAsJsonPrimitive("listingid").getAsString();
                    price = listings.get(0).getAsJsonObject().getAsJsonPrimitive("price").getAsString();
                    System.out.println("Product ID #" + listingID);
                }

                else{
                    JsonObject listing = storedata.getAsJsonObject("listings");
                    title = listing.getAsJsonPrimitive("title").getAsString();
                    image = listing.getAsJsonPrimitive("image").getAsString();
                    listingID = listing.getAsJsonPrimitive("listingID").getAsString();
                    price = listing.getAsJsonObject().getAsJsonPrimitive("price").getAsString();
                }
            }
            
            catch(Exception notFound){
                System.out.println("Item not found");
                return null;
            }
                    
            
            return new Product(title,price,image,listingID);
        }

        catch(java.io.IOException connectionFailure){
            System.out.println("Error connecting to api service");
            return null;
        }
    }

}
