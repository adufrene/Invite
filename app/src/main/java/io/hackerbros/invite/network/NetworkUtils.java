package io.hackerbros.invite.network;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class NetworkUtils {
    private static final String TAG = NetworkUtils.class.getSimpleName();
    private static final String GOOGLE_GEOCODING = "https://maps.googleapis.com/maps/api/geocode/json";
    private static final String API_KEY = "AIzaSyBy5wH84ZUND8BZma_EhZg0nfTPofWPgz4";

    public static ArrayList<String> requestGeocodingData(String input) {
        ArrayList<String> resultList = null;

        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder(GOOGLE_GEOCODING);
            sb.append("?key=" + API_KEY);
            sb.append("&address=" + URLEncoder.encode(input, "utf-8"));

            URL url = new URL(sb.toString());
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        }
        catch (MalformedURLException e) {
            Log.e(TAG, "Error processing Geocoding API URL", e);
            return resultList;
        }
        catch (IOException e) {
            Log.e(TAG, "Error connecting to Geocoding API", e);
            return resultList;
        }
        finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        try {
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");

            resultList = new ArrayList<String>(predsJsonArray.length());
            for (int i = 0; i < predsJsonArray.length(); ++i) {
                resultList.add(predsJsonArray.getJSONObject(i).getString("description"));
            }
        }
        catch (JSONException e) {
            Log.e(TAG, "Cannot process JSON results", e);
        }

        return resultList;
    }
}
