package com.batman.droidapps.shockearth;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public final class ShockHttpJsonUtils {

    public static final String LOG_TAG = EarthShockActivity.class.getName();

    private ShockHttpJsonUtils() {
    }

    public static List<ShockDataModel> fetchShockData(String requestUrl) {

        String jsonResponse = null;

        URL url = CreateUrl(requestUrl);
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "problem making Http request", e);
        }

        List<ShockDataModel> quakeData = extractShockData(jsonResponse);
        Log.e(LOG_TAG, "Here is jsonResponse is going to be returned");
        return quakeData;
    }

    private static URL CreateUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "error with creating Url", e);
        }
        return url;
    }


    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromInputStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error with connection" + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "problem retrieving the earthquake jsonResponse");
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }

            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromInputStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();

        if (inputStream != null) {

            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader bufferedreader = new BufferedReader(inputStreamReader);
            String line = bufferedreader.readLine();
            output.append(line);

            while (line != null) {
                line = bufferedreader.readLine();
                output.append(line);
            }
        }
        return output.toString();

    }


    public static List<ShockDataModel> extractShockData(String jsonResponse) {

        if (TextUtils.isEmpty(jsonResponse)) {
            return null;
        }

        List<ShockDataModel> earthquakes = new ArrayList<>();

        try {

            JSONObject root = new JSONObject(jsonResponse);
            JSONArray elements = root.getJSONArray("features");

            for (int i = 0; i < elements.length(); i++) {

                JSONObject objectElements = elements.getJSONObject(i);
                JSONObject properties = objectElements.getJSONObject("properties");
                JSONObject geometry = objectElements.getJSONObject("geometry");
                JSONArray coordinates = geometry.getJSONArray("coordinates");

                Double mag = properties.getDouble("mag");
                String location = properties.getString("place");
                long time = properties.getLong("time");
                String earthquakeUrl = properties.getString("url");

                Double longitude = coordinates.getDouble(0);
                Double latitute = coordinates.getDouble(1);
                Double depth = coordinates.getDouble(2);

                ShockDataModel data = new ShockDataModel(mag, location, time, earthquakeUrl);
                data.setmLatitude(latitute);
                data.setmLongitude(longitude);
                data.setmDepth(depth);

                earthquakes.add(data);

            }


        } catch (JSONException e) {
            Log.e("ShockHttpJsonUtils", "Problem parsing the JSON", e);
        }

        return earthquakes;
    }

}

