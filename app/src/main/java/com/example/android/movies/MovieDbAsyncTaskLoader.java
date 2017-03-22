package com.example.android.movies;

import android.os.AsyncTask;


import com.example.android.movies.utilities.NetworkUtils;



import java.io.IOException;
import java.net.URL;





public class MovieDbAsyncTaskLoader extends AsyncTask<URL, Void, String> {


    private AsyncTaskCallback callback;
    public interface AsyncTaskCallback
    {
        public void initProgressBar();
        public void killProgressBar();
        public void getJson(String jsonData);
    }

    public MovieDbAsyncTaskLoader() {
    }

    public void setCallback(AsyncTaskCallback callback)
    {
        this.callback = callback;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        callback.initProgressBar();
    }

    @Override
    protected String doInBackground(URL... params)
    {
        URL searchUrl = params[0];
        String theMoviePopularResults = null;
        try {
            theMoviePopularResults = NetworkUtils.getResponseFromHttpUrl(searchUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return theMoviePopularResults;
    }

    @Override
    protected void onPostExecute(String moviesJsonData) {
       callback.killProgressBar();
        callback.getJson(moviesJsonData);
    }
}