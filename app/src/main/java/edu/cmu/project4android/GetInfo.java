package edu.cmu.project4android;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Random;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import org.json.*;
import 	java.net.HttpURLConnection;


public class GetInfo {
    MainActivity ma = null;

    /*
     * search is the public GetPicture method.  Its arguments are the search term, and the InterestingPicture object that called it.  This provides a callback
     * path such that the pictureReady method in that object is called when the picture is available from the search.
     */
    public void search(String searchTerm, MainActivity ma) {
        this.ma = ma;
        new AsyncFlickrSearch().execute(searchTerm);
    }

    private class AsyncFlickrSearch extends AsyncTask<String, Void, Object[]> {
        protected Object[] doInBackground(String... urls) {

            return search(urls[0]);
        }

        protected void onPostExecute(Object[] info) {
            ma.pictureReady(info);

        }

        /*
         * Search Flickr.com for the searchTerm argument, and return a Bitmap that can be put in an ImageView
         */
        private Object[] search(String searchTerm) {
            HttpURLConnection conn;
            int status = 0;
            JSONObject response  = new JSONObject();
            Object[] finalreturn = new Object[2];
            try {

                // pass the name on the URL line
                URL url = new URL("https://desolate-mesa-52874.herokuapp.com/MarvelServlet/" +"//"+ searchTerm);
                //URL url = new URL("https://protected-inlet-49612.herokuapp.com/MarvelServlet/" +"//"+ searchTerm);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                // tell the server what format we want back
                //conn.setRequestProperty("Accept", "application/json");

                // wait for response
                status = conn.getResponseCode();
                System.out.println("client   "+status);
                // If things went poorly, don't try to read any response, just return.
                if (status != 200) {
                    // not using msg
                    String msg = conn.getResponseMessage();
                    return null;
                    //return conn.getResponseCode();
                }
                String total = "";
                String output = "";
                // things went well so let's read the response
                BufferedReader br = new BufferedReader(new InputStreamReader(
                        (conn.getInputStream())));

                while ((output = br.readLine()) != null) {
                    total += output;

                }
                response = new JSONObject(total);
                conn.disconnect();
                Bitmap im=null;
                try {
                    URL u = new URL((String)response.get("image"));
                    im =  getImage(u);
                } catch (Exception e) {
                    e.printStackTrace();
                    return null; // so compiler does not complain
                }

                finalreturn[0] = response;
                finalreturn[1] = im;
            }catch(JSONException e){
                e.printStackTrace();
            }catch (MalformedURLException e) {
                e.printStackTrace();
            }   catch (IOException e) {
                e.printStackTrace();
            }
            return finalreturn;
        }


        /*
         * Given a URL referring to an image, return a bitmap of that image
         */
        private Bitmap getImage(final URL url) {
            try {
                System.out.println(url);
                final URLConnection conn = url.openConnection();
                conn.connect();
                BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
                Bitmap bm = BitmapFactory.decodeStream(bis);
                Bitmap r = Bitmap.createScaledBitmap(bm, 500, 500, true);
                bis.close();
                return r;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

}
