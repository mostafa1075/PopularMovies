package com.mostafa1075.popularmovies.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by mosta on 19-Mar-18.
 */

public final class MovieContract {

    private MovieContract() {}

    public static final String AUTHORITY = "com.mostafa1075.popularmovies";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_MOVIES = "movie";

    public static class MovieEntry implements BaseColumns{

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();

        public static final String TABLE_NAME = "movie";

        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_POSTER = "poster";
        public static final String COLUMN_SYNOPSIS = "synopsis";
        public static final String COLUMN_RATING = "rating";
        public static final String COLUMN_RELEASE_DATE = "release_date";
    }
}
