/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mtu.ito.fotaito.data;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.*;
import java.net.*;

/**
 *
 * @author mitchell
 */
public class Bonsai {
    private static final String BASE_URL = "http://bonsaibadge.herokuapp.com/api";
    private static final ObjectMapper mapper = new ObjectMapper();
    
    public static User[] getAllUsers() throws Exception {
        InputStream response = doHTTP("/users", "GET");
        return mapper.readValue(response, User[].class);
    }
    
    public static User getUser(int user_id) throws Exception {
        InputStream response = doHTTP("/users/" + user_id, "GET");
        return mapper.readValue(response, User.class);
    }
    
    public static Employer[] getAllEmployers() throws Exception {
        InputStream response = doHTTP("/employers", "GET");
        Employer[] employers = mapper.readValue(response, Employer[].class);
        for (Employer employer : employers) {
            employer.setRoles(getAllRoles(employer));
        }
        return employers;
    }
    
    public static Employer getEmployer(int employer_id) throws Exception {
        InputStream response = doHTTP("/employers/" + employer_id, "GET");
        Employer employer = mapper.readValue(response, Employer.class);
        employer.setRoles(getAllRoles(employer));
        return employer;
    }
    
    public static Role[] getAllRoles(Employer employer) throws Exception {
        InputStream response = doHTTP("/employers/" + employer.getId() + "/roles", "GET");
        return mapper.readValue(response, Role[].class);
    }
    
    public static User[] getNearbyUsers(double latitude, double longitude) throws Exception {
        InputStream response = doHTTP("/users/near/" + latitude + "/" + longitude, "GET");
        return mapper.readValue(response, User[].class);
    }
    
    public static Employer[] getNearbyEmployers(double latitude, double longitude) throws Exception {
        InputStream response = doHTTP("/employers/near/" + latitude + "/" + longitude, "GET");
        Employer[] employers = mapper.readValue(response, Employer[].class);
        for (Employer employer : employers) {
            employer.setRoles(getAllRoles(employer));
        }
        return employers;
    }
    
    private static InputStream doHTTP(String url, String method)
            throws Exception {
        URL obj = new URL(BASE_URL + url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod(method);

        int responseCode = con.getResponseCode();

        if (responseCode != 200) {
            throw new RuntimeException("Bad response code: " + responseCode);
        }
        //System.out.println("\nSending 'GET' request to URL : " + url);
        //System.out.println("Response Code : " + responseCode);

        return new BufferedInputStream(con.getInputStream());
    }
}
