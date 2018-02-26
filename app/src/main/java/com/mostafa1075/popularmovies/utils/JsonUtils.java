package com.mostafa1075.popularmovies.utils;


import com.mostafa1075.popularmovies.model.MovieDetails;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by mosta on 21-Feb-18.
 */

public class JsonUtils {

    /** Constants for the  JSONkeys */
    public static final String MOVIES_RESULTS = "results";
    public static final String MOVIE_TITLE = "title";
    public static final String MOVIE_POSTER_PATH = "poster_path";
    public static final String MOVIE_OVERVIEW = "overview";
    public static final String MOVIE_RATING = "vote_average";
    public static final String MOVIE_RELEASE_DATE = "release_date";

    /** Parses the JSON received from the API and returns all movies details in a ContentValues array */
    public static ArrayList<MovieDetails> parseAllMoviesJson(String json) throws JSONException {

        JSONObject moviesJson = new JSONObject(json);
        JSONArray moviesJsonArray = moviesJson.getJSONArray(MOVIES_RESULTS);
        int moviesCount = moviesJsonArray.length();

        ArrayList<MovieDetails> moviesData = new ArrayList<>();

        for (int i = 0; i < moviesCount; i++)
            moviesData.add(parseMovieJson(moviesJsonArray.getJSONObject(i)));

        return moviesData;
    }

    /** Parses a single movie JSONObject and returns it as a ContentValues object */
    private static MovieDetails parseMovieJson(JSONObject movieObject) {

        /* Get the required movie details */
        String title = movieObject.optString(MOVIE_TITLE);
        String posterPath = movieObject.optString(MOVIE_POSTER_PATH);
        String overview = movieObject.optString(MOVIE_OVERVIEW);
        double rating = movieObject.optDouble(MOVIE_RATING);
        String releaseDate = movieObject.optString(MOVIE_RELEASE_DATE);
        //build the URL here and pass it instead of the path and building the URL every time
        String posterUrl = NetworkUtils.buildPosterUrl(posterPath);
        return new MovieDetails(title, posterUrl, overview, rating, releaseDate);
    }

}
