package example.test.app;

import android.app.Activity;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;

import example.test.app.util.SystemUiHider;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import android.view.Menu;
import android.view.MenuItem;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */
public class FullscreenActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen);

        // Get a handle to the Map Fragment
        final GoogleMap map = ((MapFragment) getFragmentManager()
                .findFragmentById(R.id.map)).getMap();
        map.setMyLocationEnabled(true);

        final LatLng bucharest = new LatLng(44.441134,26.109588);
        map.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.cristiana))
                .title("Bucuresti")
                .snippet("Meet me here :)")
                .position(bucharest));

        //map.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 13));

        CameraPosition cameraPosition = CameraPosition.builder()
                .target(bucharest)
                .zoom(13)
                .bearing(90)
                .build();

        // Animate the change in camera view over 2 seconds
        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition),
                2000, null);

        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {
                Log.d("Map", "Map clicked");

                final HttpClient httpclient = new DefaultHttpClient();
                final HttpPost httppost = new HttpPost("http://192.168.0.102/meetings/registerUser.php");

                try {
                    // Add your data
                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                    nameValuePairs.add(new BasicNameValuePair("name", "andrei"));
                    nameValuePairs.add(new BasicNameValuePair("phone", "7777"));
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));


                    // Execute HTTP Post Request
                    Thread thread = new Thread(new Runnable(){
                    @Override
                    public void run() {
                        try {
                            //Your code goes here
                            HttpResponse response = httpclient.execute(httppost);
                            HttpEntity entity = response.getEntity();
                            if(entity != null) {
                                String responseText = EntityUtils.toString(entity);
                                Log.d("message", responseText);
                            }
                            else {
                                Log.d("Fail", "Bad Response");
                            }
                        } catch (Exception e) {
                            Log.d("Fail", "Bad Request");
                            e.printStackTrace();
                        }
                    }
                    });

                    thread.start();

                } catch (IOException e) {
                    // TODO Auto-generated catch block
                }

                map.addMarker(new MarkerOptions()
                        .title("Bucuresti")
                        .snippet("New Meeting")
                        .position(point));
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return true;
    }
}
