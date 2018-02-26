package com.mostafa1075.popularmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mostafa1075.popularmovies.helper.EndlessRecyclerViewScrollListener;
import com.mostafa1075.popularmovies.model.MovieDetails;
import com.mostafa1075.popularmovies.utils.JsonUtils;
import com.mostafa1075.popularmovies.utils.NetworkUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements
        MovieAdapter.MovieAdapterOnClickHandler,
        SharedPreferences.OnSharedPreferenceChangeListener {

    private RecyclerView mRecyclerView;
    private MovieAdapter mMovieAdapter;
    private ProgressBar mLoadingIndicator;
    private TextView mErrorMessageTv;

    /**
     * The code related to SwipeRefreshLayout was taken and modified from:
     * https://github.com/codepath/android_guides/wiki/Implementing-Pull-to-Refresh-Guide
     */
    private SwipeRefreshLayout swipeRefreshLayout;

    private static final int GRID_COLUMNS_NUM = 2;

    private int mPageNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLoadingIndicator = findViewById(R.id.pb_loading_indicator);
        mErrorMessageTv = findViewById(R.id.tv_error_msg);
        mPageNum = 1;

        initializeRecyclerView();

        loadData();

        PreferenceManager.getDefaultSharedPreferences(this)
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    /**
    * This method initializes each of the RecyclerView and MovieAdapter and LayoutManager components
    * used for populating the UI. It also initializes the SwipeRefreshLayout that surrounds the
    * RecyclerView, and the ScrollListener that allows infinite scrolling.
    * */
    private void initializeRecyclerView() {

        mRecyclerView = findViewById(R.id.recyclerview_movie);
        mRecyclerView.setHasFixedSize(true);

        GridLayoutManager layoutManager = new GridLayoutManager(this, GRID_COLUMNS_NUM);
        mRecyclerView.setLayoutManager(layoutManager);

        mMovieAdapter = new MovieAdapter(this, this);
        mRecyclerView.setAdapter(mMovieAdapter);

        swipeRefreshLayout = findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reloadData();
            }
        });
        // The code on how to use EndlessRecyclerViewScrollListener was taken from:
        // https://github.com/codepath/android_guides/wiki/Endless-Scrolling-with-AdapterViews-and-RecyclerView
        EndlessRecyclerViewScrollListener scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                mPageNum++;
                loadData();
            }
        };
        mRecyclerView.addOnScrollListener(scrollListener);
    }

    /** Gets the value of sort by from SharePreferences and loads the data form the API */
    private void loadData() {
        /* check if it's online before loading and make sure the refresh indicator is false  */
        if (!isOnline()) {
            swipeRefreshLayout.setRefreshing(false);
            showErrorMessage();
            return;
        }

        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(this);

        String sortKey = getString(R.string.pref_sortBy_key);
        String defaultValue = getString(R.string.pref_sortBy_default);
        String sortValue = sharedPreferences.getString(sortKey, defaultValue);

        URL url = NetworkUtils.getUrl(sortValue, "" + mPageNum);
        new retrieveDataAsyncTask().execute(url);
    }

    /** Clears previous data in the Adapter, sets the page number equal to one and load data again. */
    private void reloadData() {
        mPageNum = 1;
        mMovieAdapter.clearMovieData();
        loadData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(MovieDetails movieData) {
        Intent intent = new Intent(this, DetailActivity.class);
        Bundle movieBundle = movieData.getAsBundle();
        intent.putExtras(movieBundle);
        startActivity(intent);
    }

    /**
    * Checks whether there is internet connection or not. Found in this StackOverflow post:
    *  https://stackoverflow.com/questions/1560788/how-to-check-internet-access-on-android-inetaddress-never-times-out
    */
    private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }

    private void showData() {
        mRecyclerView.setVisibility(View.VISIBLE);
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        mErrorMessageTv.setVisibility(View.INVISIBLE);
    }

    private void showErrorMessage() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessageTv.setVisibility(View.VISIBLE);
    }

    /** Scrolls the RecyclerView back to the top and reloads data */
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        mRecyclerView.scrollTo(RecyclerView.SCROLLBAR_POSITION_DEFAULT, RecyclerView.SCROLLBAR_POSITION_DEFAULT);
        reloadData();
    }

    // TODO: replace AsyncTask with an AsyncTaskLoader
    private class retrieveDataAsyncTask extends AsyncTask<URL, Void, ArrayList<MovieDetails>> {

        @Override
        protected void onPreExecute() {
            if (mPageNum == 1)
                mLoadingIndicator.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected ArrayList<MovieDetails> doInBackground(URL... urls) {

            String HttpResponse = null;
            try {
                HttpResponse = NetworkUtils.getResponseFromHttpUrl(urls[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }

            ArrayList<MovieDetails> moviesData = new ArrayList<>();
            try {
                moviesData = JsonUtils.parseAllMoviesJson(HttpResponse);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return moviesData;
        }

        @Override
        protected void onPostExecute(ArrayList<MovieDetails> result) {
            super.onPostExecute(result);
            mMovieAdapter.addMovieData(result);
            swipeRefreshLayout.setRefreshing(false);
            showData();
        }
    }
}
