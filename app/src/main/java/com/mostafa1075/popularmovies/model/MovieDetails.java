package com.mostafa1075.popularmovies.model;

import android.os.Bundle;

import com.mostafa1075.popularmovies.utils.JsonUtils;
import com.mostafa1075.popularmovies.utils.NetworkUtils;

/**
 * Created by mosta on 24-Feb-18.
 */

public class MovieDetails {

    private String title;
    private String overview;
    private double rating;
    private String releaseDate;
    private String posterUrl;

    public MovieDetails(String title, String posterUrl, String overview, double rating, String releaseDate) {
        this.title = title;
        this.posterUrl = posterUrl;
        this.overview = overview;
        this.rating = rating;
        this.releaseDate = releaseDate.split("-")[0];
    }

    // TODO: replace JsonUtils constants with the to be created MovieContract constants
    public MovieDetails(Bundle movieBundle){
        this.title = movieBundle.getString(JsonUtils.MOVIE_TITLE);
        this.posterUrl = movieBundle.getString(JsonUtils.MOVIE_POSTER_PATH);
        this.overview = movieBundle.getString(JsonUtils.MOVIE_OVERVIEW);
        this.rating = movieBundle.getDouble(JsonUtils.MOVIE_RATING);
        this.releaseDate = movieBundle.getString(JsonUtils.MOVIE_RELEASE_DATE);
    }

    // TODO: replace JsonUtils constants with the to be created MovieContract constants
    public Bundle getAsBundle(){

        Bundle movieBundle = new Bundle();

        movieBundle.putString(JsonUtils.MOVIE_TITLE, title);
        movieBundle.putString(JsonUtils.MOVIE_POSTER_PATH, posterUrl);
        movieBundle.putString(JsonUtils.MOVIE_OVERVIEW, overview);
        movieBundle.putDouble(JsonUtils.MOVIE_RATING, rating);
        movieBundle.putString(JsonUtils.MOVIE_RELEASE_DATE, releaseDate);

        return movieBundle;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterPath(String posterPath) {
        this.posterUrl = posterPath;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

}
