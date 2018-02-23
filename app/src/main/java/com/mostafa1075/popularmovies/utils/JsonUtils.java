package com.mostafa1075.popularmovies.utils;

import android.content.ContentValues;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by mosta on 21-Feb-18.
 */

public class JsonUtils {

    /* Constants for the  JSONkeys */
    public static final String MOVIES_RESULTS = "results";
    public static final String MOVIE_TITLE = "title";
    public static final String MOVIE_POSTER_PATH = "poster_path";
    public static final String MOVIE_OVERVIEW = "overview";
    public static final String MOVIE_RATING = "vote_average";
    public static final String MOVIE_RELEASE_DATE = "release_date";

    /* Parses the JSON received from the API and returns all movies details in a ContentValues array */
    public static ContentValues[] parseAllMoviesJson(String json) throws JSONException {

        JSONObject moviesJson = new JSONObject(json);
        JSONArray moviesJsonArray = moviesJson.getJSONArray(MOVIES_RESULTS);
        int moviesCount = moviesJsonArray.length();

        ContentValues[] moviesData = new ContentValues[moviesCount];

        for (int i = 0; i < moviesCount; i++)
            moviesData[i] = parseMovieJson(moviesJsonArray.getJSONObject(i));

        return moviesData;
    }

    /* Parses a single movie JSONObject and returns it as a ContentValues object */
    private static ContentValues parseMovieJson(JSONObject movieObject){

        /* Get the required movie details */
        String title = movieObject.optString(MOVIE_TITLE);
        String posterPath = movieObject.optString(MOVIE_POSTER_PATH);
        String overview = movieObject.optString(MOVIE_OVERVIEW);
        double rating = movieObject.optDouble(MOVIE_RATING);
        String releaseDate = movieObject.optString(MOVIE_RELEASE_DATE);

        ContentValues movieCv = new ContentValues();

        /* Put all the details in a movie ContentValues */
        movieCv.put(MOVIE_TITLE, title);
        movieCv.put(MOVIE_POSTER_PATH, posterPath);
        movieCv.put(MOVIE_OVERVIEW, overview);
        movieCv.put(MOVIE_RATING, rating);
        movieCv.put(MOVIE_RELEASE_DATE, releaseDate);

        return movieCv;
    }

}
