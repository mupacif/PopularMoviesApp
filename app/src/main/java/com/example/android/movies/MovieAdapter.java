package com.example.android.movies;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.movies.domain.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.example.android.movies.utilities.NetworkUtils.buildMediumImageUrl;
import static com.example.android.movies.utilities.NetworkUtils.buildSmallImageUrl;


public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {



    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
        boolean isLandscape();
    }


    final private ListItemClickListener mOnClickListener;

    private List<Movie> movies;


    public MovieAdapter( List<Movie> movies,ListItemClickListener listener)
    {
        this.movies = movies;
        this.mOnClickListener = listener;
    }


    /**
     * create viewHolders and inflate view from
     * @param viewGroup ?
     * @param itemType ?
     * @return movie view holder
     */
    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup viewGroup, int itemType)
    {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cell_cards,viewGroup,false);
        return new MovieViewHolder(view);
    }

    /**
     *  hidrate the view with fresh a$$ data
     * @param movieViewHolder the viewHolder
     * @param position data position
     */
    @Override
    public void onBindViewHolder(MovieViewHolder movieViewHolder, int position)
    {
        Movie movie=movies.get(position);
        movieViewHolder.bind(movie);
    }

    @Override
    public int getItemCount()
    {
        return movies.size();
    }



    /**
     * Big a** movieHolder
     */
    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView moviePoster;

            //coresponding to one view
            public MovieViewHolder(View itemView)
            {
             super(itemView);


                moviePoster=(ImageView) itemView.findViewById(R.id.image);
                itemView.setOnClickListener(this);
            }

            //to fill up according to one object
            public void bind(Movie movie)
            {
                Log.i("Adapter", movie.toString());

                String urlToPosterPath=movie.getPosterPath();
                String urlToPicture = mOnClickListener.isLandscape()?
                  buildMediumImageUrl(urlToPosterPath).toString():
                        buildSmallImageUrl(urlToPosterPath).toString();
                Picasso.with(moviePoster.getContext()).load(urlToPicture).centerCrop().fit().into(moviePoster);

            }

            @Override
            public void onClick(View v) {
                int clickedPosition = getAdapterPosition();
                mOnClickListener.onListItemClick(clickedPosition);

                Log.d("MainActivity","clicked on me");
            }

    }






}
