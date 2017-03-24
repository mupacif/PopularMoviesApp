package com.example.android.movies;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.movies.domain.Trailer;

import java.util.List;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerHolderView> {
    List<Trailer> trailers;

    public TrailerOnClickListener callbackListener;
    public  interface TrailerOnClickListener
    {
        public void onClick(int position);
    }
    public TrailerAdapter(List<Trailer> trailers, TrailerOnClickListener callbackListener) {
        this.trailers = trailers;
        this.callbackListener = callbackListener;
    }

    @Override
    public TrailerHolderView onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trailers_list, parent, false);
        return new TrailerHolderView(view);
    }

    @Override
    public void onBindViewHolder(TrailerHolderView holder, int position) {
        Trailer trailer = trailers.get(position);
        holder.bind(trailer);
    }

    @Override
    public int getItemCount() {
        return trailers.size();
    }

    public class TrailerHolderView extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView titleTv;
        public TrailerHolderView(View itemView) {
            super(itemView);
            titleTv = (TextView) itemView.findViewById(R.id.tv_detail_trailer_Title);
            itemView.setOnClickListener(this);
        }

        public void bind(Trailer trailer) {
            titleTv.setText(trailer.getName());
        }

        @Override
        public void onClick(View view) {
            callbackListener.onClick(getAdapterPosition());
        }
    }
}