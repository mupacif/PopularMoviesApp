package com.example.android.movies.utilities;

import android.util.Log;

import com.example.android.movies.domain.Movie;
import com.example.android.movies.domain.Review;
import com.example.android.movies.domain.Trailer;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public final class MoviesJsonUtils {

    private static final String JSON_GLOBAL_WRAPPER = "results";

    public static Movie[] getMoviesFromJson(String jsonStr) throws JSONException {

        if(jsonStr==null)
            throw new JSONException("no connection");

        String[][] parsedMovieData;
        JSONObject jsonData = new JSONObject(jsonStr);
        String resultJson = jsonData.getString(JSON_GLOBAL_WRAPPER);

        Log.i("MovieJsonUtils", resultJson);
        Gson gson =  gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();
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
    public static final Review[] getReviews(String jsonStr) throws JSONException {
        if(jsonStr==null)
            throw new JSONException("no connection");


        JSONObject jsonData = new JSONObject(jsonStr);
        String resultJson = jsonData.getString(JSON_GLOBAL_WRAPPER);

        Log.i("MovieJsonUtils", resultJson);
        Gson gson =  gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();
        Review[] reviews = gson.fromJson(resultJson,Review[].class);
        return reviews;
    }

    public static final Trailer[] getTrailers(String jsonStr) throws JSONException {
        if(jsonStr==null)
            throw new JSONException("no connection");

        JSONObject jsonData = new JSONObject(jsonStr);
        String resultJson = jsonData.getString(JSON_GLOBAL_WRAPPER);

        Log.i("MovieJsonUtils", resultJson);
        Gson gson =  gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();
        Trailer[] trailers = gson.fromJson(resultJson,Trailer[].class);
        return trailers;
    }


}
