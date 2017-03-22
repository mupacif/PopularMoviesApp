package com.example.android.movies.utilities;

import android.util.Log;

import com.example.android.movies.domain.Movie;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public final class MoviesJsonUtils {

    private static final String JSON_GLOBAL_WRAPPER = "results";

    public static Movie[] getSimpleMovieFromJson(String jsonStr) throws JSONException {

        String[][] parsedMovieData;
        JSONObject jsonData = new JSONObject(jsonStr);
        String resultJson = jsonData.getString(JSON_GLOBAL_WRAPPER);

        Log.i("MovieJsonUtils", resultJson);
        Gson gson = new Gson();
        Movie[] movies = gson.fromJson(resultJson,Movie[].class);

       /* JSONArray moviesArray = jsonData.getJSONArray(JSON_GLOBAL_WRAPPER);
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
        }*/
        return movies;
    }


}
