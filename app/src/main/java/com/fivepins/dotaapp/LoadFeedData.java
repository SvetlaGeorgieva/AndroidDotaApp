package com.fivepins.dotaapp;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by e30 on 1/2/2016.
 */
//public class LoadFeedData extends AsyncTask<Void, Void, ArrayList> {

public class LoadFeedData extends AsyncTask<String, Void, String> {

    private Context mContext;
    public LoadFeedData (Context context){
        mContext = context;
    }

    @Override
    protected String doInBackground(String... urls) {
        String result = "";
        try {
            URL aURL = new URL(urls[0]);

            /* Open a connection to that URL. */
            final HttpURLConnection aHttpURLConnection = (HttpURLConnection) aURL.openConnection();

            /* Define InputStreams to read from the URLConnection. */
            InputStream aInputStream = aHttpURLConnection.getInputStream();

            BufferedReader r = new BufferedReader(new InputStreamReader(aInputStream));
            StringBuilder total = new StringBuilder();
            String line;
            while ((line = r.readLine()) != null) {
                total.append(line);
            }
            result = total.toString();

        } catch (IOException e) {
            Log.d("Data retrieve ERROR.", e.toString());
        }
        return result;
    }

    @Override
    protected void onPostExecute(String result) {
        // result is what you got from your connection
        Toast.makeText(mContext, result,
                Toast.LENGTH_LONG).show();
        System.out.println("No matches");

    }
}