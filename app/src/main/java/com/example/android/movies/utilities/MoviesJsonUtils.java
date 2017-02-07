package com.example.android.movies.utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by mupac_000 on 07-02-17.
 */

public final class MoviesJsonUtils {

    private static final String GLOBAL_WRAPPER = "results";

    public static String[] getSimpleMovieFromJson(String jsonStr) throws JSONException {

        String[] parsedMovieData;
        JSONObject jsonData = new JSONObject(jsonStr);

        JSONArray moviesArray = jsonData.getJSONArray(GLOBAL_WRAPPER);
        parsedMovieData = new String[moviesArray.length()];

        for (int i = 0; i < moviesArray.length(); i++) {
            JSONObject movieForecast = moviesArray.getJSONObject(i);

            parsedMovieData[i] = movieForecast.getString("original_title");

        }
        return parsedMovieData;
    }


}
