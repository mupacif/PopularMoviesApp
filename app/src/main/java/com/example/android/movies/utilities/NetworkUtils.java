package com.example.android.movies.utilities;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * These utilities will be used to communicate with the network.
 */
public class NetworkUtils {


    final static String BASE_URL =
            "http://api.themoviedb.org/3/movie/";

    final static String BASE_URL_POPULAR = BASE_URL + "popular";

    final static String BASE_URL_TOPRATED = BASE_URL + "top_rated";


    final static String TRAILERS_PATH = "videos";
    final static String REVIEWS_PATH = "reviews";

    final static String APP_KEY_PARAM = "api_key";


    public final static String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/";
    public final static String IMAGE_QUERY_SIZE = "size";
    public final static String IMAGE_SMALL_SIZE = "w185";
    public final static String APP_KEY = "4b6de28efdf9617b837f66cc9b7dd021";


    /**
     * Build a url to get popular movies or top rated
     * @param isPopular if true returns popular movies url, if false return top rated url
     * @return most popular or top rated url
     */
    public static URL buildUrl(boolean isPopular) {
        Uri builtUri = Uri.parse(isPopular ? BASE_URL_POPULAR : BASE_URL_TOPRATED).buildUpon()
                .appendQueryParameter(APP_KEY_PARAM, APP_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.i("NetworkUtils",url.toString());
        return url;
    }

    /**
     * build a trailes url based on movie id
     * @param id of movie
     * @return url to movie trailers list
     */
    public static URL getTrailersUrl(long id) {
        Uri builtUri = Uri.parse(BASE_URL).buildUpon().appendPath(String.valueOf(id)).appendPath(TRAILERS_PATH).build();

        URL url = null;

        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }


    /**
     * Returns url of reviews comments
     * @param id of movie
     * @return url to reviews list
     */
    public static URL getReviewsUrl(long id) {
        Uri builtUri = Uri.parse(BASE_URL)
                .buildUpon()
                .appendPath(String.valueOf(id))
                .appendPath(REVIEWS_PATH).build();

        URL url = null;

        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

}
