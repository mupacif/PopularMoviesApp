package com.example.android.movies;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.movies.database.MovieContract;
import com.example.android.movies.domain.Movie;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.util.Arrays;

import static com.example.android.movies.utilities.MoviesJsonUtils.getSimpleMovieFromJson;
import static com.example.android.movies.utilities.NetworkUtils.buildSmallImageUrl;

public class DetailsActivity extends AppCompatActivity {

    final  String TAG = this.getClass().getName();

    private TextView title;
    private TextView releaseDate;
    private TextView avgVote;
    private TextView synopsis;
    private ImageView poster;
    private FloatingActionButton addFav;
    Movie movie;
    boolean isFavourite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        title = (TextView) findViewById(R.id.details_Title);
        releaseDate = (TextView) findViewById(R.id.details_releaseDate);
        avgVote = (TextView) findViewById(R.id.details_avgVote);
        synopsis = (TextView) findViewById(R.id.details_synopsis);
        poster = (ImageView) findViewById(R.id.details_image);

        addFav = (FloatingActionButton)findViewById(R.id.fab_details_addFav);
        addFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(!isFavourite) {
                     if (insertMovie()) {
                        Toast.makeText(DetailsActivity.this, "added to favorites", Toast.LENGTH_SHORT).show();
                        addFav.setImageResource(android.R.drawable.btn_star_big_on);
                        isFavourite=true;
                    }

                }
                else
                {

                    Uri toMovie = MovieContract.MovieEntry.CONTENT_URI.buildUpon().appendPath(movie.getId().toString()).build();
                    int deleted= getContentResolver().delete(toMovie, null,null);
                    if (deleted > 0) {
                        Toast.makeText(DetailsActivity.this, "removed from favorites", Toast.LENGTH_SHORT).show();
                        addFav.setImageResource(android.R.drawable.btn_star_big_off);
                        isFavourite=false;
                    }

                }

            }
        });

        Intent intent = getIntent();
        movie = (Movie)intent.getSerializableExtra("movie");
        inflateMovie(movie);
    }

    public void LoadMovies() {
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

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
        asyncLoader.execute(theMovieDBUrl);

    }
    public void LoadReviews() {
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

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
        asyncLoader.execute(theMovieDBUrl);

    }
    private boolean insertMovie()
    {
        ContentValues cv = new ContentValues();
        cv.put(MovieContract.MovieEntry.COL_ID, movie.getId());
        cv.put(MovieContract.MovieEntry.COL_POSTERPATH, movie.getPosterPath());
        cv.put(MovieContract.MovieEntry.COL_OVERVIEW, movie.getOverview());
        cv.put(MovieContract.MovieEntry.COL_RELEASEDATE, movie.getReleaseDate());
        cv.put(MovieContract.MovieEntry.COL_ORIGINALTITLE, movie.getOriginalTitle());
        cv.put(MovieContract.MovieEntry.COL_VOTEAVERAGE, movie.getVoteAverage());
        cv.put(MovieContract.MovieEntry.COL_BACKDROPPATH, movie.getBackdropPath());
        Uri inserted = getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, cv);
        if (inserted != null) {
            return true;
        }
        return false;
    }
    public void inflateMovie(Movie movie)
    {
        isFavourite = checkIfFavourite();

        if(isFavourite)
            addFav.setImageResource(android.R.drawable.btn_star_big_on);
        else
            addFav.setImageResource(android.R.drawable.btn_star_big_off);

        title.setText(movie.getOriginalTitle());
        releaseDate.setText(movie.getReleaseDate());
        avgVote.setText(String.valueOf(movie.getVoteAverage()));
        synopsis.setText(movie.getOverview());
        Picasso.with(poster.getContext()).load(buildSmallImageUrl(movie.getPosterPath()).toString()).centerCrop().fit().into(poster);
    }

    public boolean checkIfFavourite()
    {
        Uri toMovie = MovieContract.MovieEntry.CONTENT_URI.buildUpon().appendPath(movie.getId().toString()).build();
        Cursor datas = getContentResolver().query(toMovie,null,null,null,null);

        return datas.getCount()>0;
    }
}
