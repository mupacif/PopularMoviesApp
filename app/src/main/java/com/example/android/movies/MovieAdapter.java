package com.example.android.movies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.example.android.movies.Movie;

import java.util.List;


public class MovieAdapter extends ecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private List<Movie> movies;


    public MovieAdapter( List<Movie> movies)
    {
        this.movies = movies;
    }


    /**
     * create viewHolders and inflate view from
     * @param viewGroup
     * @param itemType
     * @return
     */
    public MovieViewHolder onCreateViewHolder(ViewGroup viewGroup, int itemType)
    {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.Layout.cell_cards,viewGroup,false);
        return new MovieViewHolder(view);
    }
  


    //ViewHolder class
    public class MovieViewHolder extends RecyclerView.ViewHolder{
        public ImageView moviePoster;

            //coresponding to one view
            public MovieViewHolder(View itemView)
            {
             super(itemView);


                moviePoster=(ImageView) itemView.findViewById(R.id.image);
            }

            //to fill up according to one object
            public void bind(Movie movie)
            {
                Picasso.with(moviePoster.getContext()).load(movie.getThumbnail()).centerCrop().fit().into(moviePoster);

            }

    }


}
