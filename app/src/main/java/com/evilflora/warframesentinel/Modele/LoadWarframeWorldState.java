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

            try {
                return new JSONObject(buffer.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } catch (MalformedURLException e) {
            Log.e("WarframeWorldState","Erreur URL");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}