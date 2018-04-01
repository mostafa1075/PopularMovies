package com.mostafa1075.popularmovies;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.v4.app.NavUtils;
import android.support.v4.content.Loader;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.mostafa1075.popularmovies.adapters.ReviewAdapter;
import com.mostafa1075.popularmovies.adapters.VideosAdapter;
import com.mostafa1075.popularmovies.data.MovieContract;
import com.mostafa1075.popularmovies.databinding.ActivityDetailBinding;
import com.mostafa1075.popularmovies.pojo.Movie;
import com.mostafa1075.popularmovies.pojo.MovieReview;
import com.mostafa1075.popularmovies.pojo.MovieVideo;
import com.mostafa1075.popularmovies.pojo.ReviewResponse;
import com.mostafa1075.popularmovies.pojo.VideoResponse;
import com.mostafa1075.popularmovies.rest.RestClient;
import com.mostafa1075.popularmovies.rest.RestService;
import com.mostafa1075.popularmovies.utils.DbBitmapUtility;
import com.mostafa1075.popularmovies.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity implements
        VideosAdapter.VideosAdapterOnClickHandler,
        ReviewAdapter.ReviewAdapterOnClickHandler,
        android.support.v4.app.LoaderManager.LoaderCallbacks<Cursor> {

    public static final String REVIEW_DATA_KEY = "review data";

    private Movie mMovieDetails;
    private ActivityDetailBinding mDetailBinding;
    private Call<ReviewResponse> mReviewsResponse;
    private Call<VideoResponse> mVideosResponse;
    private VideosAdapter mVideosAdapter;
    private ReviewAdapter mReviewAdapter;
    private boolean mIsFavorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        mDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        if (intent == null)
            finish();
        mMovieDetails = intent.getParcelableExtra(MainActivity.MOVIE_DATA_KEY);

        populateMovieDetails();
        loadVideos();
        loadReview();

        getSupportLoaderManager().initLoader(0, null, this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mReviewsResponse.cancel();
        mVideosResponse.cancel();
    }

    private void populateMovieDetails() {
        setTitle(mMovieDetails.getTitle());
        String posterPath = mMovieDetails.getPosterPath();
        if (posterPath != null) {
            String posterUrl = NetworkUtils.buildPosterImageUrl(posterPath);
            Picasso.with(this)
                    .load(posterUrl)
                    .into(mDetailBinding.ivPoster);
        } else {
            Bitmap posterBitmap =
                    DbBitmapUtility.getImage(mMovieDetails.getPosterByteArr());
            mDetailBinding.ivPoster.setImageBitmap(posterBitmap);
        }
        String backdropPath = mMovieDetails.getBackdropPath();
        Picasso.with(this).load(NetworkUtils.buildBackdropImageUrl(backdropPath))
                .fit()
                .centerCrop()
                .into(mDetailBinding.ivBackdrop);

        mDetailBinding.tvOverview.setText(mMovieDetails.getOverview());
        mDetailBinding.ratingBar.setRating((float) (mMovieDetails.getVoteAverage() / 2));
        mDetailBinding.tvYear.setText(mMovieDetails.getReleaseDate().split("-")[0]);
    }

    private void loadVideos() {
        RecyclerView recyclerView = mDetailBinding.recyclerviewVideos;
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL,
                false);
        recyclerView.setLayoutManager(layoutManager);

        mVideosAdapter = new VideosAdapter(this, this);
        recyclerView.setAdapter(mVideosAdapter);

        RestService service = RestClient.getInstance().service;
        mVideosResponse = service.getMovieVideos("" + mMovieDetails.getId(),
                NetworkUtils.API_KEY);

        mVideosResponse.enqueue(new Callback<VideoResponse>() {
            @Override
            public void onResponse(Call<VideoResponse> call, Response<VideoResponse> response) {
                mVideosAdapter.swipeData((ArrayList<MovieVideo>) response.body().getResults());
            }

            @Override
            public void onFailure(Call<VideoResponse> call, Throwable t) {
                Log.e("Videos Request Failure", t.getMessage());
            }
        });
    }

    private void loadReview() {
        RecyclerView recyclerView = mDetailBinding.recyclerviewReviews;
        recyclerView.setHasFixedSize(true);
        SnapHelper helper = new PagerSnapHelper();
        helper.attachToRecyclerView(recyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL,
                false);
        recyclerView.setLayoutManager(layoutManager);

        mReviewAdapter = new ReviewAdapter(this, this);
        recyclerView.setAdapter(mReviewAdapter);

        RestService service = RestClient.getInstance().service;
        mReviewsResponse = service.getMovieReviews("" + mMovieDetails.getId(),
                NetworkUtils.API_KEY);

        mReviewsResponse.enqueue(new Callback<ReviewResponse>() {
            @Override
            public void onResponse(Call<ReviewResponse> call, Response<ReviewResponse> response) {
                mReviewAdapter.swipeData((ArrayList<MovieReview>) response.body().getResults());
            }

            @Override
            public void onFailure(Call<ReviewResponse> call, Throwable t) {
                Log.e("Videos Request Failure", t.getMessage());
            }
        });
    }

    /**
     * The Callback for the favorites ImageButton
     * Checks whether the Movie is a favorite or not and inserts/deletes accordingly
     */
    public void onFavoritesPressed(View view) {
        if (mIsFavorite)
            deleteMovieFromDb();
        else
            insertMovieToDb();
    }

    /**
     * Converts the Movie object to ContentValues and insert it to the SQLiteDatabase
     */
    private void insertMovieToDb() {
        // Get the Movie as ContentValues
        ContentValues movieCv = mMovieDetails.getContentValues();
        // Get the poster image as a bitmap from the ImageView
        Bitmap posterBm = ((BitmapDrawable) mDetailBinding.ivPoster.getDrawable()).getBitmap();
        // Convert the bitmap to byte array to be inserted to the SQLiteDatabase
        byte[] posterByteArr = DbBitmapUtility.getBytes(posterBm);
        // Add the poster byte array to the ContentValues
        movieCv.put(MovieContract.MovieEntry.COLUMN_POSTER, posterByteArr);
        // Insert the ContentValues to the database
        getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, movieCv);
        // set the favorite flag to true
        mIsFavorite = true;
        // Update the ImageButton Image Resource
        updateImageButtonDrawable();

        Toast.makeText(this, "Added to favorites", Toast.LENGTH_LONG).show();
    }

    /**
     * Deletes the movie from the SQLite Database
     */
    private void deleteMovieFromDb() {
        Uri queryUri = MovieContract.MovieEntry.CONTENT_URI
                .buildUpon()
                .appendPath("" + mMovieDetails.getId())
                .build();
        getContentResolver().delete(queryUri,
                null,
                null);

        mIsFavorite = false;
        updateImageButtonDrawable();
        Toast.makeText(this, "Removed from favorites", Toast.LENGTH_LONG).show();
    }

    /**
     * Updates the ImageButton depending on the mIsFavorite flag
     */
    private void updateImageButtonDrawable() {
        int imageRes;
        if (mIsFavorite)
            imageRes = R.drawable.favorites_delete_item;
        else
            imageRes = R.drawable.favorites_add_icon;

        mDetailBinding.ibFavorite.setImageResource(imageRes);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onVideoClick(MovieVideo video) {
        Intent intent = new Intent(Intent.ACTION_VIEW, NetworkUtils.buildVideoUri(video.getKey()));
        startActivity(intent);
    }

    @Override
    public void onReviewClick(MovieReview review) {
        Intent intent = new Intent(this, ReviewActivity.class);
        intent.putExtra(REVIEW_DATA_KEY, review);
        startActivity(intent);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri queryUri = MovieContract.MovieEntry.CONTENT_URI
                .buildUpon()
                .appendPath("" + mMovieDetails.getId())
                .build();
        return new CursorLoader(this,
                queryUri,
                null,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mIsFavorite = data.moveToFirst();
        updateImageButtonDrawable();
        data.close();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }


}
