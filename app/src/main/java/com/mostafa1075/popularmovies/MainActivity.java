package com.mostafa1075.popularmovies;


import android.content.Intent;
import android.support.v4.content.Loader;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mostafa1075.popularmovies.adapters.MovieAdapter;
import com.mostafa1075.popularmovies.data.MovieContract;
import com.mostafa1075.popularmovies.helper.EndlessRecyclerViewScrollListener;
import com.mostafa1075.popularmovies.pojo.Movie;
import com.mostafa1075.popularmovies.pojo.MovieResponse;
import com.mostafa1075.popularmovies.rest.RestClient;
import com.mostafa1075.popularmovies.rest.RestService;
import com.mostafa1075.popularmovies.utils.NetworkUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements
        MovieAdapter.MovieAdapterOnClickHandler,
        android.support.v4.app.LoaderManager.LoaderCallbacks<Cursor> {

    /**
     * Constants for outState bundle keys
     */
    private final static String PAGE_NUM_KEY = "page_num";
    private final static String SCROLL_POSITION_KEY = "scroll_position";
    private final static String SELECTED_NAV_ITEM = "nav_item";
    /**
     * Constant for number of movies per page
     */
    private static final int MOVIES_PER_PAGE = 20;
    /**
     * The key for the parcelable to be passed to DetailActivity
     */
    public static final String MOVIE_DATA_KEY = "movie data";
    private RecyclerView mRecyclerView;
    private MovieAdapter mMovieAdapter;
    private GridLayoutManager mLayoutManager;
    private EndlessRecyclerViewScrollListener mScrollListener;
    private ProgressBar mLoadingIndicator;
    private TextView mErrorMessageTv;
    private DrawerLayout mDrawerLayout;
    /**
     * The code related to SwipeRefreshLayout was adapted from:
     * https://github.com/codepath/android_guides/wiki/Implementing-Pull-to-Refresh-Guide
     */
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private Call<MovieResponse> mMoviesResponse;
    /**
     * The initial page number this activity starts with
     */
    private int mInitialPageNum;
    /**
     * The page number used for loading new pages
     */
    private int mPageNum;
    /**
     * Used for restoring scrolling position on configuration changed
     */
    private int mScrollingPosition;
    private int mSelectedNavItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            mPageNum = mInitialPageNum = savedInstanceState.getInt(PAGE_NUM_KEY);
            mScrollingPosition = savedInstanceState.getInt(SCROLL_POSITION_KEY);
            mSelectedNavItem = savedInstanceState.getInt(SELECTED_NAV_ITEM);
        } else {
            mInitialPageNum = mPageNum = 1;
            mSelectedNavItem = R.id.nav_popular;
        }

        mLoadingIndicator = findViewById(R.id.pb_loading_indicator);
        mErrorMessageTv = findViewById(R.id.tv_error_msg);

        initializeRecyclerView();
        initializeNavigationDrawer();
        loadData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mMoviesResponse != null)
            mMoviesResponse.cancel();
    }

    /**
     * This method initializes each of the RecyclerView and MovieAdapter and LayoutManager components
     * used for populating the UI. It also initializes the SwipeRefreshLayout that surrounds the
     * RecyclerView, and the ScrollListener that allows infinite scrolling.
     */
    private void initializeRecyclerView() {

        mRecyclerView = findViewById(R.id.recyclerview_movie);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new GridLayoutManager(this, getSpanCount());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mMovieAdapter = new MovieAdapter(this, this);
        mRecyclerView.setAdapter(mMovieAdapter);

        mSwipeRefreshLayout = findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reloadData();
            }
        });
        mScrollListener = new EndlessRecyclerViewScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                mPageNum++;
                loadData();
            }
        };
        // Disable infinite scrolling for favorites
        if (mSelectedNavItem != R.id.nav_fav) {
            mRecyclerView.addOnScrollListener(mScrollListener);
        }
    }

    /**
     * This method initialize the components needed for the navigation drawer.
     */
    private void initializeNavigationDrawer() {

        // show the drawer icon instead of the Home button
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);

        mDrawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.getMenu().findItem(mSelectedNavItem).setChecked(true);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // set item as selected to persist highlight
                item.setChecked(true);
                // close drawer when item is tapped
                mDrawerLayout.closeDrawers();
                if (mSelectedNavItem != item.getItemId()) {

                    // Disable infinite scrolling for favorites
                    if (mSelectedNavItem == R.id.nav_fav && item.getItemId() != R.id.nav_fav)
                        mRecyclerView.addOnScrollListener(mScrollListener);
                    else if (item.getItemId() == R.id.nav_fav)
                        mRecyclerView.removeOnScrollListener(mScrollListener);

                    mSelectedNavItem = item.getItemId();
                    reloadData();
                }
                return true;
            }
        });
    }

    /**
     * Gets the value of sort by from SharePreferences and loads the data form the API
     */
    private void loadData() {
        if (mPageNum == 1)
            mLoadingIndicator.setVisibility(View.VISIBLE);
        RestService service = RestClient.getInstance().service;

        switch (mSelectedNavItem) {
            case R.id.nav_popular:
                mMoviesResponse = service.getPopularMovies(NetworkUtils.API_KEY, "" + mPageNum);
                break;
            case R.id.nav_top:
                mMoviesResponse = service.getTopRatedMovies(NetworkUtils.API_KEY, "" + mPageNum);
                break;
            case R.id.nav_fav:
                getSupportLoaderManager().restartLoader(0, null, this);
                return;
            default:
                throw new UnsupportedOperationException("Unknown item");
        }

        mMoviesResponse.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                mMovieAdapter.addMovieData(response.body().getResults());
                mSwipeRefreshLayout.setRefreshing(false);
                if (mScrollingPosition != 0 && mPageNum == mInitialPageNum) {
                    mLayoutManager.scrollToPosition(mScrollingPosition);
                    mScrollingPosition = 0;
                }
                showData();
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                Log.e("Movies Request Failure", t.getMessage());
                if (!NetworkUtils.isOnline(MainActivity.this)) {
                    mSwipeRefreshLayout.setRefreshing(false);
                    showErrorMessage();
                }
            }
        });
    }

    /**
     * Clears previous data in the Adapter, sets the page number equal to one and load data again.
     */
    private void reloadData() {
        mPageNum = mInitialPageNum = 1;
        mMovieAdapter.clearMovieData();
        loadData();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            mDrawerLayout.openDrawer(GravityCompat.START);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(Movie movieData) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(MOVIE_DATA_KEY, movieData);
        startActivity(intent);
    }

    private void showData() {
        mRecyclerView.setVisibility(View.VISIBLE);
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        mErrorMessageTv.setVisibility(View.INVISIBLE);
    }

    private void showErrorMessage() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessageTv.setVisibility(View.VISIBLE);
        mLoadingIndicator.setVisibility(View.INVISIBLE);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this,
                MovieContract.MovieEntry.CONTENT_URI,
                null,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mMovieAdapter.addMovieData(data);
        mSwipeRefreshLayout.setRefreshing(false);
        showData();
        mLayoutManager.scrollToPosition(mScrollingPosition);
        mScrollingPosition = 0;
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    /**
     * Saving and restoring scrolling position was adapted from: https://stackoverflow.com/a/27954051
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        int scrollPosition = mLayoutManager.findFirstVisibleItemPosition();

        if (mSelectedNavItem != R.id.nav_fav)
            outState.putInt(SCROLL_POSITION_KEY, getPositionWithinPage(scrollPosition));
        else
            outState.putInt(SCROLL_POSITION_KEY, scrollPosition);

        outState.putInt(PAGE_NUM_KEY, getPageNum(scrollPosition) + mInitialPageNum - 1);
        outState.putInt(SELECTED_NAV_ITEM, mSelectedNavItem);
    }

    /**
     * @param scrollPosition The scroll position of the RecyclerView
     * @return The page number of a certain scrolling position
     */
    private int getPageNum(int scrollPosition) {
        return (int) Math.ceil((double) (scrollPosition + 1) / (double) MOVIES_PER_PAGE);
    }

    /**
     * @param scrollPosition The scroll position of the RecyclerView
     * @return The scrolling position number within the page the item belongs to
     */
    private int getPositionWithinPage(int scrollPosition) {
        return scrollPosition % MOVIES_PER_PAGE;
    }

    /**
     * Returns the amount of items that can fit horizontally in the RecyclerView
     * Used for Automatic spanning of items within the RecyclerView
     * This method is adapted from this answer: https://stackoverflow.com/a/28077579
     */
    private int getSpanCount() {
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        return Math.round(outMetrics.widthPixels / NetworkUtils.POSTER_IMAGE_WIDTH);
    }
}
