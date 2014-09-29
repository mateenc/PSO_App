package com.example.pardeep.pso_app;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.net.URLConnection;


public class HomeScreen extends Activity {

    static String[] allpowers = new String[4];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        Log.d("Message", "App started");
        PopulateFields();
        //pardeep testing 2
    }

    public void PopulateFields()
    {
        ReadRelays();
        String[] powers = new String[4];

        if(allpowers[0] == "")
        {
            powers[0] = "NA";
            powers[1] = "NA";
            powers[2] = "NA";
            powers[3] = "NA";
        }
        else
        {
            powers = allpowers[0].split(",");
            powers[0] = powers[0] + " mW";
            powers[1] = powers[1] + " mW";
            powers[2] = powers[2] + " mW";
            powers[3] = powers[3] + " mW";
        }



        final TextView O1RPLabel = (TextView)findViewById(R.id.Outlet1RealPowerLabel);
        final TextView O1APLabel = (TextView)findViewById(R.id.Outlet1ApparentPowerLabel);
        final TextView O2RPLabel = (TextView)findViewById(R.id.Outlet2RealPowerLabel);
        final TextView O2APLabel = (TextView)findViewById(R.id.Outlet2ApparentPowerLabel);

        O1RPLabel.setText(powers[0]);
        O1APLabel.setText(powers[1]);
        O2RPLabel.setText(powers[2]);
        O2APLabel.setText(powers[3]);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_screen, menu);
        return true;
    }

    public void Outlet1OnButtonClick(View v)
    {
        ToggleRelay(8, 0);
    }

    public void Outlet1OffButtonClick(View v)
    {
        ToggleRelay(8, 1);
    }

    public void Outlet2OnButtonClick(View v)
    {
        ToggleRelay(9, 0);
    }

    public void Outlet2OffButtonClick(View v)
    {
        ToggleRelay(9, 1);
    }

    public void ReadRelays()
    {
        int mum;
        URL url = null;
        URLConnection conn = null;

        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpGet httpGet = new HttpGet("http://192.168.0.4/arduino/getpower/all");
                    httpGet.addHeader(BasicScheme.authenticate(
                            new UsernamePasswordCredentials("root", "12345678"),
                            "UTF-8", false));

                    HttpResponse httpResponse = httpClient.execute(httpGet);
                    HttpEntity responseEntity = httpResponse.getEntity();

                    InputStream stream = responseEntity.getContent();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "iso-8859-1"), 8);
                    StringBuilder sb = new StringBuilder();
                    String line = null;
                    while ((line = reader.readLine()) != null) // Read line by line
                        sb.append(line + "\n");

                    String resString = sb.toString(); // Result is here
                    allpowers[0] = resString;
                    Log.i("string letta", resString);

                    stream.close(); // Close the stream

                    Log.d("Message", "Pin switched");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
        //while(allpowers[0] == null || allpowers[0] == "") { }
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void ToggleRelay(final int outlet, final int bit)
    {
        URL url = null;
        URLConnection conn = null;

        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpGet httpGet = new HttpGet("http://192.168.0.4/arduino/digital/" + String.valueOf(outlet) +"/" + String.valueOf(bit));
                    httpGet.addHeader(BasicScheme.authenticate(
                            new UsernamePasswordCredentials("root", "12345678"),
                            "UTF-8", false));

                    HttpResponse httpResponse = httpClient.execute(httpGet);
                    HttpEntity responseEntity = httpResponse.getEntity();

                    InputStream stream = responseEntity.getContent();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "iso-8859-1"), 8);
                    StringBuilder sb = new StringBuilder();
                    String line = null;
                    while ((line = reader.readLine()) != null) // Read line by line
                        sb.append(line + "\n");

                    String resString = sb.toString(); // Result is here
                    Log.i("string letta", resString);

                    stream.close(); // Close the stream

                    Log.d("Message", "Pin switched");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }

    public void ReadRelay(final int outlet)
    {
        URL url = null;
        URLConnection conn = null;
        String[] value;

        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpGet httpGet = new HttpGet("http://192.168.0.5/arduino/digital/" + String.valueOf(outlet));
                    httpGet.addHeader(BasicScheme.authenticate(
                            new UsernamePasswordCredentials("root", "12345678"),
                            "UTF-8", false));

                    HttpResponse httpResponse = httpClient.execute(httpGet);
                    HttpEntity responseEntity = httpResponse.getEntity();

                    InputStream stream = responseEntity.getContent();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "iso-8859-1"), 8);
                    StringBuilder sb = new StringBuilder();
                    String line = null;
                    while ((line = reader.readLine()) != null) // Read line by line
                        sb.append(line + "\n");

                    String resString = sb.toString(); // Result is here

                    Log.i("string letta", resString);

                    stream.close(); // Close the stream

                    Log.d("Message", "Pin switched");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }

    public void getPowerOnClick(View v)
    {
        PopulateFields();
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
