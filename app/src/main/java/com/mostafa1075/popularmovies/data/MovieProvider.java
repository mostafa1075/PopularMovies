package com.mostafa1075.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by mosta on 20-Mar-18.
 */

public class MovieProvider extends ContentProvider {

    private static final int MOVIES = 1;
    private static final int MOVIES_WITH_ID = 2;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(MovieContract.AUTHORITY, MovieContract.PATH_MOVIES, MOVIES);
        sUriMatcher.addURI(MovieContract.AUTHORITY, MovieContract.PATH_MOVIES + "/#", MOVIES_WITH_ID);
    }

    private MovieDbHelper mMovieDbHelper;

    @Override
    public boolean onCreate() {
        Context context = getContext();
        mMovieDbHelper = new MovieDbHelper(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        final SQLiteDatabase db = mMovieDbHelper.getReadableDatabase();
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            case MOVIES:
                retCursor = db.query(MovieContract.MovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case MOVIES_WITH_ID:
                retCursor = db.query(MovieContract.MovieEntry.TABLE_NAME,
                        null,
                        MovieContract.MovieEntry.COLUMN_MOVIE_ID + "=?",
                        new String[]{uri.getLastPathSegment()},
                        null,
                        null,
                        null);
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {

        final SQLiteDatabase db = mMovieDbHelper.getWritableDatabase();
        Uri returnUri;

        switch (sUriMatcher.match(uri)) {
            case MOVIES:
                long id = db.insert(MovieContract.PATH_MOVIES, null, values);
                if (id > 0)
                    returnUri = ContentUris.withAppendedId(MovieContract.MovieEntry.CONTENT_URI, id);
                else
                    throw new SQLException("Failed to insert row into " + uri);
                break;
            default:
                throw new UnsupportedOperationException(new Exception("Unknown Uri: " + uri));
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {

        final SQLiteDatabase db = mMovieDbHelper.getWritableDatabase();
        int rowsDeleted = 0;
        switch (sUriMatcher.match(uri)) {
            case MOVIES:
                rowsDeleted = db.delete(MovieContract.MovieEntry.TABLE_NAME,
                        selection,
                        selectionArgs);
                break;
            case MOVIES_WITH_ID:
                rowsDeleted = db.delete(MovieContract.MovieEntry.TABLE_NAME,
                        MovieContract.MovieEntry.COLUMN_MOVIE_ID + "=?",
                        new String[]{uri.getLastPathSegment()});
        }
        if (rowsDeleted != 0)
            getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
