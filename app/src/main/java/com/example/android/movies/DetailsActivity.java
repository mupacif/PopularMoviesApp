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

                ContentValues cv = new ContentValues();
                cv.put(MovieContract.MovieEntry.COL_IDLIKEDMOVIE,movie.getId());
                Uri inserted = getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI,cv);
                if(inserted!=null)
                {
                    Toast.makeText(DetailsActivity.this,"added to favorites", Toast.LENGTH_SHORT).show();
                }

            }
        });

        Intent intent = getIntent();
        movie = (Movie)intent.getSerializableExtra("movie");
        inflateMovie(movie);
    }

    public void inflateMovie(Movie movie)
    {
        String favourited = checkIfFavourite()?"[Favourited]":"";
        title.setText(movie.getOriginalTitle()+favourited);
        releaseDate.setText(movie.getReleaseDate());
        avgVote.setText(String.valueOf(movie.getVoteAverage()));
        synopsis.setText(movie.getOverview());
        Picasso.with(poster.getContext()).load(buildSmallImageUrl(movie.getPosterPath()).toString()).centerCrop().fit().into(poster);
    }

    public boolean checkIfFavourite()
    {
        Uri toMovie = MovieContract.BASE_CONTENT_URI.buildUpon().appendPath(movie.getId().toString()).build();
        Cursor datas = getContentResolver().query(toMovie,null,null,null,null);

        return datas.getCount()>0;
    }
}
