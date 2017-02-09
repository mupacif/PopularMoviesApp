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
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.movies.utilities.NetworkUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

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
        new theMovieDbQueryTask().execute(theMovieDBUrl);
    }


        public class theMovieDbQueryTask extends AsyncTask<URL, Void, String> {


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
        protected void onPostExecute(String theMoviePopularResults) {
            if (theMoviePopularResults != null && !theMoviePopularResults.equals("")) {
                try {
                    data = getSimpleMovieFromJson(theMoviePopularResults);
                    movies = generateMovie(data);
                    setRecyclerAdapter(movies);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("MainActivity","error fatale");
                }
                //Log.i("MainActivity",theMoviePopularResults);
            }
        }
    }

    /**
     * Generate movie
     * @param moviesObject
     * @return
     */
    public List<Movie> generateMovie(String[][] moviesObject)
    {
        List<Movie> movies = new ArrayList<>();
        for(String[] movie:moviesObject)
        {
           Movie m = new Movie(movie[0],movie[1],movie[2],movie[3],movie[4]);
            movies.add(m);
        }
        return movies;
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
        intent.putExtra("msg",movie);
        startActivity(intent);
    }
}
