package com.example.android.movies;

import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.movies.database.MovieContract;
import com.example.android.movies.domain.Movie;
import com.example.android.movies.domain.Review;
import com.example.android.movies.domain.Trailer;
import com.example.android.movies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.net.URL;
import java.util.Arrays;
import java.util.List;

import static com.example.android.movies.utilities.MoviesJsonUtils.getReviews;
import static com.example.android.movies.utilities.MoviesJsonUtils.getTrailers;
import static com.example.android.movies.utilities.NetworkUtils.buildBigImageUrl;
import static com.example.android.movies.utilities.NetworkUtils.buildReviewsUrl;
import static com.example.android.movies.utilities.NetworkUtils.buildSmallImageUrl;
import static com.example.android.movies.utilities.NetworkUtils.buildTrailersUrl;

public class DetailsActivity extends AppCompatActivity implements TrailerAdapter.TrailerOnClickListener {

    final String TAG = this.getClass().getName();

    private TextView title;
    private TextView releaseDate;
    private TextView avgVote;
    private TextView synopsis;
    private ImageView poster;
    private FloatingActionButton addFav;
    private List<Trailer> trailers;
    private Movie movie;
    boolean isFavourite;
    private CollapsingToolbarLayout collapsingToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

         collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);


        title = (TextView) findViewById(R.id.details_Title);
        releaseDate = (TextView) findViewById(R.id.details_releaseDate);
        avgVote = (TextView) findViewById(R.id.details_avgVote);
        synopsis = (TextView) findViewById(R.id.details_synopsis);
        poster = (ImageView) findViewById(R.id.details_image);

        addFav = (FloatingActionButton) findViewById(R.id.fab_details_addFav);
        addFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (!isFavourite) {
                    if (insertMovie()) {
                        Toast.makeText(DetailsActivity.this, "added to favorites", Toast.LENGTH_SHORT).show();
                        addFav.setImageResource(android.R.drawable.btn_star_big_on);
                        isFavourite = true;
                    }

                } else {

                    Uri toMovie = MovieContract.MovieEntry.CONTENT_URI.buildUpon().appendPath(movie.getId().toString()).build();
                    int deleted = getContentResolver().delete(toMovie, null, null);
                    if (deleted > 0) {
                        Toast.makeText(DetailsActivity.this, "removed from favorites", Toast.LENGTH_SHORT).show();
                        addFav.setImageResource(android.R.drawable.btn_star_big_off);
                        isFavourite = false;
                    }

                }

            }
        });

        Intent intent = getIntent();
        movie = (Movie) intent.getSerializableExtra("movie");
        inflateMovie(movie);
    }

    public void LoadTrailers(URL toTrailers) {
        MovieDbAsyncTaskLoader asyncLoader = new MovieDbAsyncTaskLoader();
        asyncLoader.setCallback(new MovieDbAsyncTaskLoader.AsyncTaskCallback() {
            @Override
            public void initProgressBar() {
            }

            @Override
            public void killProgressBar() {
            }

            @Override
            public void getJson(String jsonData) {

                RecyclerView mTrailerRV = (RecyclerView) findViewById(R.id.rv_details_trailers);
                mTrailerRV.setLayoutManager(new LinearLayoutManager(DetailsActivity.this));
                try {

                    trailers = Arrays.asList(getTrailers(jsonData));
                    mTrailerRV.setAdapter(new TrailerAdapter(trailers,DetailsActivity.this));
                    Log.i(TAG, "Async finished, trailers array size :" + trailers.size());

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
        asyncLoader.execute(toTrailers);

    }

    public void LoadReviews(URL toReviews) {
        MovieDbAsyncTaskLoader asyncLoader = new MovieDbAsyncTaskLoader();
        asyncLoader.setCallback(new MovieDbAsyncTaskLoader.AsyncTaskCallback() {
            @Override
            public void initProgressBar() {

            }

            @Override
            public void killProgressBar() {

            }

            @Override
            public void getJson(String jsonData) {
                List<Review> reviews;
                RecyclerView mReviews = (RecyclerView) findViewById(R.id.rv_details_reviews);
                mReviews.setLayoutManager(new LinearLayoutManager(DetailsActivity.this));
                try {

                    reviews = Arrays.asList(getReviews(jsonData));
                    mReviews.setAdapter(new ReviewAdapter(reviews));
                    Log.i(TAG, "Async finished reviews size " + reviews.size());

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
        asyncLoader.execute(toReviews);

    }

    private boolean insertMovie() {
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

    public void inflateMovie(Movie movie) {
        isFavourite = checkIfFavourite();
        collapsingToolbar.setTitle(movie.getOriginalTitle());
        if (isFavourite)
            addFav.setImageResource(android.R.drawable.btn_star_big_on);
        else
            addFav.setImageResource(android.R.drawable.btn_star_big_off);

        Log.i(TAG, "reviews url=" + buildReviewsUrl(movie.getId()));
        Log.i(TAG, "trailers url=" + buildTrailersUrl(movie.getId()));
        title.setText(movie.getOriginalTitle());
        releaseDate.setText(movie.getReleaseDate());
        avgVote.setText(String.valueOf(movie.getVoteAverage()));
        synopsis.setText(movie.getOverview());
        Picasso.with(this).load(buildSmallImageUrl(movie.getPosterPath()).toString()).centerCrop().fit().into(poster);

        final ImageView imageView=(ImageView)findViewById(R.id.iv_details_backdrop);
        String bigImage = buildBigImageUrl(movie.getBackdropPath()).toString();
        Log.i(TAG,"big image url:"+bigImage);
        Picasso.with(this).load(bigImage).centerCrop().fit().into(imageView);

        LoadTrailers(buildTrailersUrl(movie.getId()));
        LoadReviews(buildReviewsUrl(movie.getId()));

    }



    public boolean checkIfFavourite() {
        Uri toMovie = MovieContract.MovieEntry.CONTENT_URI.buildUpon().appendPath(movie.getId().toString()).build();
        Cursor datas = getContentResolver().query(toMovie, null, null, null, null);

        return datas.getCount() > 0;
    }

    @Override
    public void onClick(int position) {
            Trailer trailer = trailers.get(position);
          String id = trailer.getKey();

        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + id));
        try {
            startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            startActivity(webIntent);
        }
    }

    public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewHolderView> {
        List<Review> reviewList;

        public ReviewAdapter(List<Review> reviewList) {
            this.reviewList = reviewList;
        }

        @Override
        public ReviewHolderView onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reviews_list, parent, false);
            return new ReviewHolderView(view);
        }

        @Override
        public void onBindViewHolder(ReviewHolderView holder, int position) {
            Review review = reviewList.get(position);
            holder.bind(review);
        }

        @Override
        public int getItemCount() {
            return reviewList.size();
        }

        public class ReviewHolderView extends RecyclerView.ViewHolder {
            TextView authorTv;
            TextView commentTv;

            public ReviewHolderView(View itemView) {
                super(itemView);
                authorTv = (TextView) itemView.findViewById(R.id.tv_detail_review_author);
                commentTv = (TextView) itemView.findViewById(R.id.tv_detail_review_comment);
            }

            public void bind(Review review) {
                authorTv.setText(review.getAuthor());
                commentTv.setText(review.getContent());
            }
        }
    }



}
