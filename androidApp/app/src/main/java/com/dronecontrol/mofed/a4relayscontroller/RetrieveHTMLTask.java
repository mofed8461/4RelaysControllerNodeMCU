package com.dronecontrol.mofed.a4relayscontroller;



import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by mamounlaptop on 11/29/18.
 */

class RetrieveHTMLTask extends AsyncTask<String, Void, HTMLResult> {

    public Exception exception;

    protected HTMLResult doInBackground(String... urls) {
        StringBuilder strBuilder = new StringBuilder();

        try {

            URL url = new URL(urls[0]);
            HttpURLConnection connection =(HttpURLConnection) url.openConnection();

            InputStream inputStream = new BufferedInputStream(connection.getInputStream());

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                strBuilder.append(line + "\n");
            }
            inputStream.close();


        } catch (Exception e) {
            this.exception = e;

            return null;
        }

        HTMLResult res = new HTMLResult();
        res.HTML = strBuilder.toString();
        return res;
    }

    protected void onPostExecute(HTMLResult res) {
        // TODO: check this.exception
        // TODO: do something with the feed
    }
}
