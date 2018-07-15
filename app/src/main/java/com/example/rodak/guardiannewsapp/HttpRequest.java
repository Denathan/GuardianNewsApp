package com.example.rodak.guardiannewsapp;

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

public class HttpRequest {

    public static final String LOG_TAG = HttpRequest.class.getSimpleName();

    private HttpRequest() {
    }

    public static List<NewsDto> fetchNewsData(String endpoint) {
        URL url = createUrl(endpoint);
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }
        List<NewsDto> newsList = extractFeatureFromJson(jsonResponse);
        return newsList;
    }

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException exception) {
            Log.e(LOG_TAG, "Error with creating URL", exception);
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
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.connect();
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = convertStreamToString(inputStream);
            } else {
                Log.e(LOG_TAG, "Error code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem with retrieving the news data from JSON.", e);
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

    private static String convertStreamToString(InputStream is) throws IOException {
        StringBuilder output = new StringBuilder();
        if (is != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(is, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static List<NewsDto> extractFeatureFromJson(String newsJSON) {

        if (TextUtils.isEmpty(newsJSON)) {
            return null;
        }
        List<NewsDto> newsList = new ArrayList<>();

        try {
            JSONObject baseJsonResponse = new JSONObject(newsJSON);
            JSONObject responseObject = baseJsonResponse.getJSONObject("response");
            JSONArray resultsArray = responseObject.getJSONArray("results");

            for (int i = 0; i < resultsArray.length(); i++) {
                JSONObject results = resultsArray.getJSONObject(i);

                String title = "(no title)";
                if (results.has("webTitle")) {
                    title = results.getString("webTitle");
                }

                String author = "(unknown author)";
                if (results.has("fields")) {
                    JSONObject fieldsObject = results.getJSONObject("fields");

                    if (fieldsObject.has("byline")) {
                        author = fieldsObject.getString("byline");
                    }
                }

                String sectionName = "(unknown section)";
                if (results.has("sectionName")) {
                    sectionName = results.getString("sectionName");
                }

                String webPublicationDate = "(no date available)";
                if (results.has("webPublicationDate")) {
                    webPublicationDate = results.getString("webPublicationDate");
                }

                String url = "";
                if (results.has("webUrl")) {
                    url = results.getString("webUrl");
                }

                NewsDto actualNews = new NewsDto(title, sectionName, author, webPublicationDate, url);
                newsList.add(actualNews);
            }

        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing the news JSON results", e);
        }
        return newsList;
    }
}
