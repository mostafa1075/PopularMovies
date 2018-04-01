package com.mostafa1075.popularmovies.adapters;


import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mostafa1075.popularmovies.R;
import com.mostafa1075.popularmovies.pojo.Movie;
import com.mostafa1075.popularmovies.utils.DbBitmapUtility;
import com.mostafa1075.popularmovies.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mosta on 22-Feb-18.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder> {

    private static final String DATASOURCE_API = "api";
    private static final String DATASOURCE_DATABASE = "database";

    private final ArrayList<Movie> mMovieData;
    private final Context mContext;
    private final MovieAdapterOnClickHandler mClickHandler;
    private Cursor mCursor;
    private String mDataSource;

    public MovieAdapter(Context context, MovieAdapterOnClickHandler clickHandler) {
        mMovieData = new ArrayList<>();
        mContext = context;
        mClickHandler = clickHandler;
        mDataSource = DATASOURCE_API;
    }

    @Override
    public MovieAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.movie_grid_item, parent, false);
        return new MovieAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieAdapterViewHolder holder, int position) {
        Movie movie = getMovieData(position);
        String posterPath = movie.getPosterPath();
        if (posterPath != null) {
            String posterUrl = NetworkUtils.buildPosterImageUrl(posterPath);
            Picasso.with(mContext)
                    .load(posterUrl)
                    .into(holder.mMovieThumbnailImageView);
        } else {
            Bitmap posterBitmap =
                    DbBitmapUtility.getImage(movie.getPosterByteArr());
            holder.mMovieThumbnailImageView.setImageBitmap(posterBitmap);
        }
    }

    @Override
    public int getItemCount() {
        if (mDataSource.equals(DATASOURCE_API))
            return mMovieData.size();
        else if (mDataSource.equals(DATASOURCE_DATABASE) && !mCursor.isClosed())
            return mCursor.getCount();
        else
            return 0;
    }

    /**
     * store the results returned from the API response
     */
    public void addMovieData(List<Movie> movieData) {
        mDataSource = DATASOURCE_API;
        mMovieData.addAll(movieData);
        notifyItemRangeInserted(getItemCount() + 1, movieData.size());
    }

    /**
     * store the cursor returned from the query and change Datasource to database
     */
    public void addMovieData(Cursor cursor) {
        mDataSource = DATASOURCE_DATABASE;
        mCursor = cursor;
        notifyDataSetChanged();
    }

    /**
     * Clear all data by clearing the List of Movies and closing the cursor
     */
    public void clearMovieData() {
        mMovieData.clear();
        if (mCursor != null)
            mCursor.close();
        notifyDataSetChanged();
    }

    /**
     * Gets the Movie object at a certain position from API results or Cursor depending on the current Datasource
     */
    private Movie getMovieData(int position) {
        Movie movie;
        if (mDataSource.equals(DATASOURCE_API)) {
            movie = mMovieData.get(position);
        } else {
            if (mCursor.moveToPosition(position))
                movie = new Movie(mCursor);
            else
                throw new CursorIndexOutOfBoundsException("Wrong position: " + position);
        }
        return movie;
    }

    public interface MovieAdapterOnClickHandler {
        void onClick(Movie movieData);
    }

    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final ImageView mMovieThumbnailImageView;

        public MovieAdapterViewHolder(View itemView) {
            super(itemView);
            mMovieThumbnailImageView = itemView.findViewById(R.id.iv_movie_thumbnail);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            mClickHandler.onClick(getMovieData(position));
        }
    }

}
