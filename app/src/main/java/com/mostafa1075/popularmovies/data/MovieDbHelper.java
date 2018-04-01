package com.mostafa1075.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.mostafa1075.popularmovies.data.MovieContract.MovieEntry;
/**
 * Created by mosta on 19-Mar-18.
 */

public class MovieDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "movie.db";

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_MOVIE_TABLE =
                "CREATE TABLE " + MovieEntry.TABLE_NAME + " (" +
                        MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        MovieEntry.COLUMN_MOVIE_ID + " INTEGER UNIQUE NOT NULL, " +
                        MovieEntry.COLUMN_POSTER + " BLOB NOT NULL, " +
                        MovieEntry.COLUMN_SYNOPSIS + " TEXT NOT NULL, " +
                        MovieEntry.COLUMN_RATING + " REAL NOT NULL, " +
                        MovieEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL);";

        db.execSQL(SQL_CREATE_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME);
        onCreate(db);
    }
}
