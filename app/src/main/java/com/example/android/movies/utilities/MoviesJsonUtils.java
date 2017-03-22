package com.example.android.movies.utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public final class MoviesJsonUtils {

    private static final String GLOBAL_WRAPPER = "results";

    public static String[][] getSimpleMovieFromJson(String jsonStr) throws JSONException {

        String[][] parsedMovieData;
        JSONObject jsonData = new JSONObject(jsonStr);

        JSONArray moviesArray = jsonData.getJSONArray(GLOBAL_WRAPPER);
        parsedMovieData = new String[moviesArray.length()][5];

        for (int i = 0; i < moviesArray.length(); i++)
        {
            JSONObject movieForecast = moviesArray.getJSONObject(i);
            parsedMovieData[i] =
                    new String[]{movieForecast.getString("original_title"),
                                 movieForecast.getString("poster_path"),
                                 movieForecast.getString("overview"),
                                 movieForecast.getString("vote_average"),
                                 movieForecast.getString("release_date")};
        }
        return parsedMovieData;
    }


}
