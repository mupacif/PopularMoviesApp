package com.example.android.movies;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.android.movies.domain.Movie;
import com.example.android.movies.utilities.NetworkUtils;

import org.json.JSONException;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

import static com.example.android.movies.utilities.MoviesJsonUtils.getSimpleMovieFromJson;

public class MainActivity extends AppCompatActivity implements MovieAdapter.ListItemClickListener
{


    private RecyclerView recyclerView;
    private List<Movie> movies;
    Toast toast;
    private String[][] data;
    private Boolean isPopular;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         toast = new Toast(getApplicationContext());

        isPopular=true;
        movieQuery(isPopular);

       recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));

        recyclerView.setItemAnimator(new SlideInUpAnimator());


    }



    public void changeSorting(boolean isPopular)
    {
        movieQuery(isPopular);


    }

    /**
     * Load json data from Server
     */
    private void movieQuery(boolean isPopular) {
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
                     movies = Arrays.asList(getSimpleMovieFromJson(jsonData));
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
     * @param movies
     */
    public void setRecyclerAdapter(List<Movie> movies)
    {


        recyclerView.setAdapter(new MovieAdapter(movies,this));
        Log.d("MainActivity","**gogo powa ranga**");

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
    private void sortMostPopular(){
        if(!isPopular)
        {
            isPopular=true;
            changeSorting(isPopular);
        }
      //  toast.makeText(this,R.string.action_popular,Toast.LENGTH_LONG).show();
    }

    /**
     * Method to manage rated list
     */
    private void sortMostRated(){

        if(isPopular)
        {
            isPopular=false;
            changeSorting(isPopular);
        }
        //toast.makeText(this,R.string.action_rated,Toast.LENGTH_LONG).show();
    }

    /**
     *  Event management of itemps of menu
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_popular:
                sortMostPopular();
                return true;
            case R.id.action_rated:
                sortMostRated();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Get the index of items then send related movie to details
     * @param clickedItemIndex
     */
    @Override
    public void onListItemClick(int clickedItemIndex) {



        goToDetails(clickedItemIndex);

    }

    /**
     * Open new activity with related movie
     */
    public  void goToDetails(int movieIndex)
    {
        Movie movie = movies.get(movieIndex);
        Log.d("MainActivity",movie.getOriginalTitle());
        Intent intent = new Intent(this,DetailsActivity.class);
        intent.putExtra("movie",movie);
        startActivity(intent);
    }
}
