package com.mostafa1075.popularmovies;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mostafa1075.popularmovies.model.MovieDetails;
import com.mostafa1075.popularmovies.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by mosta on 22-Feb-18.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder>  {

    private ArrayList<MovieDetails> mMovieData;
    private Context mContext;
    private MovieAdapterOnClickHandler mClickHandler;

    public MovieAdapter(Context context, MovieAdapterOnClickHandler clickHandler){
        mMovieData = new ArrayList<>();
        mContext = context;
        mClickHandler = clickHandler;
    }

    @Override
    public MovieAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.movie_grid_item, parent, false);
        return new MovieAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieAdapterViewHolder holder, int position) {

        String posterUrl = mMovieData.get(position).getPosterUrl();
        Picasso.with(mContext)
                .load(posterUrl)
                .into(holder.mMovieThumbnailImageView);
    }

    @Override
    public int getItemCount() {return mMovieData.size();}

    public void addMovieData(ArrayList<MovieDetails> movieData){
        mMovieData.addAll(movieData);
        notifyItemRangeInserted(getItemCount() + 1, movieData.size());
    }

    public void clearMovieData(){
        mMovieData.clear();
        notifyDataSetChanged();
    }

    public interface MovieAdapterOnClickHandler{public void onClick(MovieDetails movieData);}

    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView mMovieThumbnailImageView;

        public MovieAdapterViewHolder(View itemView) {
            super(itemView);
            mMovieThumbnailImageView = itemView.findViewById(R.id.iv_movie_thumbnail);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            mClickHandler.onClick(mMovieData.get(position));
        }
    }

}
