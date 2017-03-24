package com.example.android.movies;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.movies.database.MovieContract;
import com.example.android.movies.domain.Movie;
import com.example.android.movies.utilities.NetworkUtils;

import org.json.JSONException;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import static com.example.android.movies.utilities.MoviesJsonUtils.getMoviesFromJson;

public class MainActivity extends AppCompatActivity implements MovieAdapter.ListItemClickListener {

    public static final String FAVOURITE_KEY = "Favourites";
    public static final String POPULAR_KEY = "Popular";
    public static final String TOP_RATED_KEY = "Top Rated";

    public static final String TAG = "MainActivity";
    private RecyclerView recyclerView;
    private List<Movie> movies;
    private MovieAdapter movieAdapter;
    Toast toast;
    private boolean localDatabase;

    private Boolean isPopular;
    private TextView topSettingTv;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i(TAG,"onCreate");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        topSettingTv = (TextView)findViewById(R.id.tv_main_topSetting);
        toast = new Toast(getApplicationContext());

        isPopular = true;
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        if(savedInstanceState!=null)
        {
            localDatabase = savedInstanceState.getBoolean(FAVOURITE_KEY,false);
            isPopular = savedInstanceState.getBoolean(POPULAR_KEY,true);
            if(localDatabase) {
                sortFavourites();
                topSettingTv.setText(FAVOURITE_KEY);
            } else {
                Log.i(TAG,"Network searching");
                movieQuery(isPopular);
            }
        }
        else{
            movieQuery(isPopular);
        }




    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {

        Log.i(TAG,"onSaveInstanceState");
        savedInstanceState.putBoolean(FAVOURITE_KEY,localDatabase);
        savedInstanceState.putBoolean(POPULAR_KEY,isPopular);

        super.onSaveInstanceState(savedInstanceState);
    }

    public void changeSorting(boolean isPopular) {
        movieQuery(isPopular);


    }

    /**
     * Load json data from Server
     */
    private void movieQuery(boolean isPopular) {
        topSettingTv.setText(isPopular?POPULAR_KEY:TOP_RATED_KEY);
        URL theMovieDBUrl = NetworkUtils.buildUrl(isPopular);
        MovieDbAsyncTaskLoader asyncLoader = new MovieDbAsyncTaskLoader();
        asyncLoader.setCallback(new MovieDbAsyncTaskLoader.AsyncTaskCallback() {
            @Override
            public void initProgressBar() {
                findViewById(R.id.pb_main_progressBar).setVisibility(View.VISIBLE);
            }

            @Override
            public void killProgressBar() {
                findViewById(R.id.pb_main_progressBar).setVisibility(View.GONE);
            }

            @Override
            public void getJson(String jsonData) {
                try {
                    movies = Arrays.asList(getMoviesFromJson(jsonData));
                    setRecyclerAdapter(movies);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
        asyncLoader.execute(theMovieDBUrl);
    }


    /**
     * Set recycler adapter
     *
     * @param movies
     */
    public void setRecyclerAdapter(List<Movie> movies) {

            movieAdapter = new MovieAdapter(movies, this);
            recyclerView.setAdapter(new MovieAdapter(movies, this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //ajoute les entrées de menu_test à l'ActionBar
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * Method to manage popular list
     */
    private void sortMostPopular() {
        if (!isPopular) {
            isPopular = true;
            changeSorting(isPopular);
        }
        //  toast.makeText(this,R.string.action_popular,Toast.LENGTH_LONG).show();
    }

    /**
     * Method to manage rated list
     */
    private void sortMostRated() {

        if (isPopular) {
            isPopular = false;
            changeSorting(isPopular);
        }
        //toast.makeText(this,R.string.action_rated,Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onResume() {
        Log.i(TAG,"onResume");
        super.onResume();
        if(localDatabase) {
            sortFavourites();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG,"onStart");
    }


    @Override
    protected void onRestart() {
        super.onStart();
        Log.i(TAG,"onRestart");
    }

    /**
     * Event management of itemps of menu
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_popular:
                localDatabase=false;
                sortMostPopular();
                return true;
            case R.id.action_rated:
                localDatabase=false;
                sortMostRated();
                return true;
            case R.id.action_favourites:
                localDatabase=true;
                sortFavourites();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void sortFavourites() {
        topSettingTv.setText(FAVOURITE_KEY);
        Cursor c = getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI, null, null, null, null);
        List<Movie> moviesSqlite=new ArrayList<>();
        while (c.moveToNext()) {
            moviesSqlite.add(getMoviebyCursor(c));
        }
        movies = moviesSqlite;
        setRecyclerAdapter(movies);
    }

    public Movie getMoviebyCursor(Cursor c) {
        String posterPath = c.getString(c.getColumnIndex(MovieContract.MovieEntry.COL_POSTERPATH));
        String overview = c.getString(c.getColumnIndex(MovieContract.MovieEntry.COL_OVERVIEW));
        String releaseDate = c.getString(c.getColumnIndex(MovieContract.MovieEntry.COL_RELEASEDATE));
        int id = c.getInt(c.getColumnIndex(MovieContract.MovieEntry.COL_ID));
        String originalTitle = c.getString(c.getColumnIndex(MovieContract.MovieEntry.COL_ORIGINALTITLE));
        String backdropPath = c.getString(c.getColumnIndex(MovieContract.MovieEntry.COL_BACKDROPPATH));
        double voteAverage = c.getDouble(c.getColumnIndex(MovieContract.MovieEntry.COL_VOTEAVERAGE));

        Movie movie = new Movie(posterPath, overview, releaseDate, id, originalTitle, backdropPath, voteAverage);
        return movie;
    }

    /**
     * Get the index of items then send related movie to details
     *
     * @param clickedItemIndex
     */
    @Override
    public void onListItemClick(int clickedItemIndex) {


        goToDetails(clickedItemIndex);

    }

    /**
     * Open new activity with related movie
     */
    public void goToDetails(int movieIndex) {
        Movie movie = movies.get(movieIndex);
        Log.d("MainActivity", movie.getOriginalTitle());
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra("movie", movie);
        startActivity(intent);
    }
}
