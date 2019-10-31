package edu.cmu.project4android;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final MainActivity ma = this;
        /*
         * Find the "submit" button, and add a listener to it
         */
        Button submitButton = (Button) findViewById(R.id.submit);


        // Add a listener to the send button
        submitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View viewParam) {
                String searchTerm = ((EditText) findViewById(R.id.searchTerm)).getText().toString();
                GetInfo gp = new GetInfo();
                gp.search(searchTerm, ma); // Done asynchronously in another thread.  It calls ip.pictureReady() in this thread when complete.
            }
        });
    }



    /*
     * This is called by the GetPicture object when the picture is ready.  This allows for passing back the Bitmap picture for updating the ImageView
     */
    public void pictureReady(Object[] data) {
        //System.out.println(data==null);
        ImageView pictureView = (ImageView)findViewById(R.id.characterPicture);
        TextView textView = (TextView)findViewById(R.id.textView1);
        TextView textView2 = (TextView)findViewById(R.id.textView2);
        TextView searchView = (EditText)findViewById(R.id.searchTerm);
        TextView displayInfo = (TextView)findViewById(R.id.name);
        if (data != null) {
            try {
                JSONObject info = (JSONObject) data[0];
                Bitmap picture = (Bitmap) data[1];
                String result = "name: " + (String) info.getString("name") + "\n\n"
                        + "description: " + (String) info.getString("description") + "\n\n"
                        + "detail: " + (String) info.getString("detail") + "\n\n"
                        + "wiki: " + (String) info.getString("wiki") + "\n\n"
                        + "comiclink: " + (String) info.getString("comiclink") + "\n";
                pictureView.setImageBitmap(picture);
                pictureView.setVisibility(View.VISIBLE);
                textView.setVisibility(View.VISIBLE);
                displayInfo.setText(result);
                textView2.setVisibility(View.INVISIBLE);
            }catch(Exception e){

            }
        } else {
            pictureView.setImageResource(R.mipmap.ic_launcher);
            pictureView.setVisibility(View.INVISIBLE);
            textView2.setVisibility(View.VISIBLE);
            textView.setVisibility(View.INVISIBLE);
            displayInfo.setText("");
        }
        searchView.setText("");
        pictureView.invalidate();
    }

}
