package com.mostafa1075.popularmovies;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.Log;
import android.view.MenuItem;

import com.mostafa1075.popularmovies.adapters.ReviewAdapter;
import com.mostafa1075.popularmovies.adapters.VideosAdapter;
import com.mostafa1075.popularmovies.databinding.ActivityDetailBinding;
import com.mostafa1075.popularmovies.pojo.Movie;
import com.mostafa1075.popularmovies.pojo.MovieReview;
import com.mostafa1075.popularmovies.pojo.MovieVideo;
import com.mostafa1075.popularmovies.pojo.ReviewResponse;
import com.mostafa1075.popularmovies.pojo.VideoResponse;
import com.mostafa1075.popularmovies.rest.RestClient;
import com.mostafa1075.popularmovies.rest.RestService;
import com.mostafa1075.popularmovies.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity implements
        VideosAdapter.VideosAdapterOnClickHandler {

    // TODO: Create a better layout for DetailActivity.
    private Movie mMovieDetails;
    private ActivityDetailBinding mDetailBinding;
    private Call<ReviewResponse> mReviewsResponse;
    private Call<VideoResponse> mVideosResponse;
    private VideosAdapter mVideosAdapter;
    private ReviewAdapter mReviewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail);

        Intent intent = getIntent();
        if (intent == null)
            finish();
        mMovieDetails = intent.getParcelableExtra(MainActivity.MOVIE_DATA_KEY);

        setTitle(mMovieDetails.getTitle());
        String posterPath = mMovieDetails.getPosterPath();
        String posterUrl = NetworkUtils.buildImageUrl(posterPath);
        Picasso.with(this)
                .load(posterUrl)
                .into(mDetailBinding.ivPoster);
        mDetailBinding.tvOverview.setText(mMovieDetails.getOverview());
        mDetailBinding.tvRating.setText("" + mMovieDetails.getVoteAverage() + "/10");
        mDetailBinding.tvYear.setText(mMovieDetails.getReleaseDate().split("-")[0]);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        loadVideos();
        loadReview();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mReviewsResponse.cancel();
        mVideosResponse.cancel();
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

        mReviewAdapter = new ReviewAdapter(this);
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
}
