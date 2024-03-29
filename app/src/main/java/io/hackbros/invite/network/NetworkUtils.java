package io.hackbros.invite.network;

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
    private static final String API_KEY = "AIzaSyCUo5ApvI3OdDHTaUMe3glBAOUsjpIEEAA"; // Android Key

    public static JSONObject requestGeocodingData(String input) {
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
            return null;
        }
        catch (IOException e) {
            Log.e(TAG, "Error connecting to Geocoding API", e);
            return null;
        }
        finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        try {
            return new JSONObject(jsonResults.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;

    }
}
