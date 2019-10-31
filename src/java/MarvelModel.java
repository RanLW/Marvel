/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import org.json.*; 


/**
 *
 * @author carri
 */
public class MarvelModel {
    
    //public key and private key used to connect Marvel API
    private String publicKey = "9fe01ef8f8f73986551fd8b8e3454b74";
    private String privateKey = "89a641a7bc3098d7aec71b9e722a3196bad2ab45";
    
    //generate a url and fetch to Marvel API, get a JSON response
    public JSONObject doMarvelSearch(String characterName) 
            throws UnsupportedEncodingException  {
        
        characterName = URLEncoder.encode(characterName, "UTF-8");
        if(characterName.toLowerCase().startsWith("spider")) characterName="spider-man";
        long timestamp = System.currentTimeMillis();
        String MD5Hash = getMd5(timestamp+privateKey+publicKey);
        // Create a URL for Marvel API
        String marvelURL =
                "http://gateway.marvel.com/v1/public/characters?name="
                + characterName 
                + "&ts="
                + timestamp
                +"&apikey="+publicKey
                +"&hash="
                + MD5Hash
               ;
        
        // Fetch the url and get the JSONObject
        JSONObject response = fetch(marvelURL);
        
        return response;
    }

    /*
     * Make an HTTP request to a given URL
     * 
     * @param urlString The URL of the request
     * @return A JSONObject of the response from the HTTP GET.  
     */
    private JSONObject fetch(String urlString) {
        JSONObject response = new JSONObject();
        try {
            URL url = new URL(urlString);
            /*
             * Create an HttpURLConnection.  
             */
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            // Read all the text returned by the server and clean them and make a new JSONObject
            connection.setRequestMethod("GET");
            connection.connect();
            int responsecode = connection.getResponseCode();
            String inline="";
            if(responsecode !=200){
                return null;
            }else{
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
                String str;
                while ((str = in.readLine()) != null) {
                    inline+=str;
                }
                in.close();
                JSONObject jsonO = new JSONObject(inline+"\n");
                JSONObject jsonObject = (JSONObject)jsonO.get("data");
                JSONArray results = (JSONArray) jsonObject.get("results");
                //if no result is found, return null;
                if(results.isEmpty()) return null;
                JSONObject resultObject = (JSONObject) results.get(0);
                String name = (String)resultObject.get("name");
                response.put("name", name);
                String description = (String)resultObject.get("description");
                response.put("description", description);
                JSONObject thumbnail = (JSONObject)resultObject.get("thumbnail");
                String image = (String)thumbnail.get("path")+"."+(String)thumbnail.get("extension");
                response.put("image", image);
                JSONArray urls = (JSONArray) resultObject.get("urls"); 
                Iterator itr = urls.iterator(); 
                while (itr.hasNext())  
                { 
                    JSONObject itr1 = (JSONObject) itr.next(); 
                    System.out.println(itr1.get("type") + " : " + itr1.get("url")); 
                    response.put((String)itr1.get("type"), (String)itr1.get("url")); 
                }   
            }   
            
        } catch (IOException e) {
            System.out.println("Eeek, an exception");
            // Do something reasonable.  This is left for students to do.
        }
        //return the new JSONObject
        return response;
    }
    
    //Generate a MD5 Hashing string
    public static String getMd5(String input) 
    { 
        try { 
  
            // Static getInstance method is called with hashing MD5 
            MessageDigest md = MessageDigest.getInstance("MD5"); 
  
            // digest() method is called to calculate message digest 
            //  of an input digest() return array of byte 
            byte[] messageDigest = md.digest(input.getBytes()); 
  
            // Convert byte array into signum representation 
            BigInteger no = new BigInteger(1, messageDigest); 
  
            // Convert message digest into hex value 
            String hashtext = no.toString(16); 
            while (hashtext.length() < 32) { 
                hashtext = "0" + hashtext; 
            } 
            return hashtext; 
        }  
  
        // For specifying wrong message digest algorithms 
        catch (NoSuchAlgorithmException e) { 
            throw new RuntimeException(e); 
        } 
    } 
}
