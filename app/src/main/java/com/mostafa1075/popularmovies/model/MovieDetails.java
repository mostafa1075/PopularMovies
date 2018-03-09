package com.mostafa1075.popularmovies.model;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.mostafa1075.popularmovies.utils.JsonUtils;
import com.mostafa1075.popularmovies.utils.NetworkUtils;

/**
 * Created by mosta on 24-Feb-18.
 */

public class MovieDetails implements Parcelable {

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

    protected MovieDetails(Parcel in) {
        title = in.readString();
        overview = in.readString();
        rating = in.readDouble();
        releaseDate = in.readString();
        posterUrl = in.readString();
    }

    public static final Creator<MovieDetails> CREATOR = new Creator<MovieDetails>() {
        @Override
        public MovieDetails createFromParcel(Parcel in) {
            return new MovieDetails(in);
        }

        @Override
        public MovieDetails[] newArray(int size) {
            return new MovieDetails[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(title);
        dest.writeString(overview);
        dest.writeDouble(rating);
        dest.writeString(releaseDate);
        dest.writeString(posterUrl);
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
