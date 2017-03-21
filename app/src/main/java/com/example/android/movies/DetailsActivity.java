package com.example.android.movies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.movies.domain.Movie;
import com.squareup.picasso.Picasso;

public class DetailsActivity extends AppCompatActivity {

    final  String TAG = this.getClass().getName();

    private TextView title;
    private TextView releaseDate;
    private TextView avgVote;
    private TextView synopsis;
    private ImageView poster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        title = (TextView) findViewById(R.id.details_Title);
        releaseDate = (TextView) findViewById(R.id.details_releaseDate);
        avgVote = (TextView) findViewById(R.id.details_avgVote);
        synopsis = (TextView) findViewById(R.id.details_synopsis);
        poster = (ImageView) findViewById(R.id.details_image);



        Intent intent = getIntent();
        Movie movie = (Movie)intent.getSerializableExtra("movie");
        inflateMovie(movie);
    }

    public void inflateMovie(Movie movie)
    {
        title.setText(movie.getOriginalTitle());
        releaseDate.setText(movie.getRealiseDate());
        avgVote.setText(String.valueOf(movie.getRating()));
        synopsis.setText(movie.getSynopsis());
        Picasso.with(poster.getContext()).load(movie.getThumbnail()).centerCrop().fit().into(poster);
    }
}
