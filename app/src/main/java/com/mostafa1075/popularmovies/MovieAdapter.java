package com.mostafa1075.popularmovies;

import android.content.ContentValues;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mostafa1075.popularmovies.utils.JsonUtils;
import com.mostafa1075.popularmovies.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by mosta on 22-Feb-18.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder>  {

    private ArrayList<ContentValues> mMovieData;
    private Context mContext;
    private int dataCount;

    public MovieAdapter(Context context){
        mMovieData = new ArrayList<>();
        mContext = context;
    }

    @Override
    public MovieAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.movie_grid_item, parent, false);
        return new MovieAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieAdapterViewHolder holder, int position) {

        String posterPath = mMovieData.get(position).getAsString(JsonUtils.MOVIE_POSTER_PATH);
        String posterUrl = NetworkUtils.buildPosterUrl(posterPath);
        Picasso.with(mContext)
                .load(posterUrl)
                .into(holder.mMovieThumbnailImageView);
    }

    @Override
    public int getItemCount() {
        return mMovieData.size();
    }

    public void setMovieData(ArrayList<ContentValues> movieData){
        mMovieData = movieData;
        notifyDataSetChanged();
    }

    public void addMovieData(ContentValues[] movieData){
        mMovieData.addAll(Arrays.asList(movieData));
        notifyItemRangeInserted(getItemCount() + 1, movieData.length);
    }

    public void clearMovieData(){
        mMovieData.clear();
        notifyDataSetChanged();
    }

    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder {

        private ImageView mMovieThumbnailImageView;

        public MovieAdapterViewHolder(View itemView) {
            super(itemView);
            mMovieThumbnailImageView = itemView.findViewById(R.id.iv_movie_thumbnail);
        }

    }

}
