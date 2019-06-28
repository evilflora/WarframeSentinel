package com.evilflora.warframesentinel.Modele;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class LoadWarframeWorldState extends AsyncTask<String, Integer, JSONObject>
{
    @Override
    public JSONObject doInBackground(String... arg0) {

        HttpURLConnection connection = null;
        BufferedReader reader = null;
        String currentFileName = "LoadWarframeWorldState";

        try {
            URL url = new URL("http://content" + arg0[0] + ".warframe.com/dynamic/worldState.php");
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            InputStream stream = connection.getInputStream();

            reader = new BufferedReader(new InputStreamReader(stream));

            StringBuilder buffer = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                buffer.append(line).append("\n");
            }
            return new JSONObject(buffer.toString());

        } catch (MalformedURLException e) {
            Log.e(currentFileName, "Error in URL");
        } catch (IOException e) {
            Log.e(currentFileName, "Cannot load WorldState : " + e.getLocalizedMessage());
        } catch (JSONException e) {
            Log.e(currentFileName, "Cannot create JSON for WorldState : " + e.getLocalizedMessage());
        }
        if (connection != null) connection.disconnect();
        try {
            if (reader != null) {
                reader.close();
            }
        } catch (IOException e) {
            Log.e(currentFileName, "Cannot load WorldState : " + e.getLocalizedMessage());
        }
        return null;
    }
}