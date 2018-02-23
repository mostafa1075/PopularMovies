package com.mostafa1075.popularmovies;

import android.content.ContentValues;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.widget.TextView;

import com.mostafa1075.popularmovies.helper.EndlessRecyclerViewScrollListener;
import com.mostafa1075.popularmovies.utils.JsonUtils;
import com.mostafa1075.popularmovies.utils.NetworkUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.ConnectException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private MovieAdapter mMovieAdapter;
    private GridLayoutManager mLayoutManager;
    private EndlessRecyclerViewScrollListener scrollListener;
    /**
     * The code related to SwipeRefreshLayout was taken and modified from:
     * https://github.com/codepath/android_guides/wiki/Implementing-Pull-to-Refresh-Guide
     */
    private SwipeRefreshLayout swipeRefreshLayout;

    private static final int GRID_COLUMNS_NUM = 2;

    private int mPageNum;

    // TODO: optimize the code of onCreate
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.recyclerview_movie);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new GridLayoutManager(this, GRID_COLUMNS_NUM);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mMovieAdapter = new MovieAdapter(this);
        mRecyclerView.setAdapter(mMovieAdapter);

        mPageNum = 1;

        loadData();

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPageNum = 1;
                reloadData();
            }
        });

        // The code on how to use EndlessRecyclerViewScrollListener was taken from:
        // https://github.com/codepath/android_guides/wiki/Endless-Scrolling-with-AdapterViews-and-RecyclerView
        scrollListener = new EndlessRecyclerViewScrollListener(mLayoutManager) {
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


    private void loadData() {
        URL url = NetworkUtils.getUrl("top_rated", "" + mPageNum);
        new retreiveDataAsyncTask().execute(url);
    }

    private void reloadData() {
        mMovieAdapter.clearMovieData();
        loadData();
    }

    // TODO: replace AsyncTask with an AsyncTaskLoader
    private class retreiveDataAsyncTask extends AsyncTask<URL, Void, ContentValues[]> {
        @Override
        protected ContentValues[] doInBackground(URL... urls) {

            String HttpResponse = null;
            try {
                HttpResponse = NetworkUtils.getResponseFromHttpUrl(urls[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }

            ContentValues[] moviesData = null;
            try {
                moviesData = JsonUtils.parseAllMoviesJson(HttpResponse);
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
            return moviesData;
        }

        @Override
        protected void onPostExecute(ContentValues[] result) {
            super.onPostExecute(result);

            mMovieAdapter.addMovieData(result);
            swipeRefreshLayout.setRefreshing(false);
        }
    }
}
