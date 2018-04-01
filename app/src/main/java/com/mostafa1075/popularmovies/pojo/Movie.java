package com.mostafa1075.popularmovies.pojo;

/**
 * Created using http://www.jsonschema2pojo.org/
 */


import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mostafa1075.popularmovies.data.MovieContract;

public class Movie implements Parcelable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("vote_average")
    @Expose
    private Double voteAverage;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("poster_path")
    @Expose
    private String posterPath;
    @SerializedName("backdrop_path")
    @Expose
    private String backdropPath;
    @SerializedName("overview")
    @Expose
    private String overview;
    @SerializedName("release_date")
    @Expose
    private String releaseDate;

    private byte[] posterByteArr;

    public final static Parcelable.Creator<Movie> CREATOR = new Creator<Movie>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        public Movie[] newArray(int size) {
            return (new Movie[size]);
        }

    };

    protected Movie(Parcel in) {
        this.id = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.voteAverage = ((Double) in.readValue((Double.class.getClassLoader())));
        this.title = ((String) in.readValue((String.class.getClassLoader())));
        this.posterPath = ((String) in.readValue((String.class.getClassLoader())));
        this.backdropPath = ((String) in.readValue((String.class.getClassLoader())));
        this.overview = ((String) in.readValue((String.class.getClassLoader())));
        this.releaseDate = ((String) in.readValue((String.class.getClassLoader())));
        this.posterByteArr = ((byte[])in.readValue((byte.class.getClassLoader())));
    }

    public Movie() {
    }

    public Movie(Cursor cursor) {
        int position = cursor.getPosition();

        this.id = cursor.getInt(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_ID));
        this.posterByteArr = cursor.getBlob(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_POSTER));
        this.overview = cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_SYNOPSIS));
        this.voteAverage = cursor.getDouble(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_RATING));
        this.releaseDate = cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_RELEASE_DATE));
    }

    public ContentValues getContentValues(){
        ContentValues cv = new ContentValues();
        cv.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, this.id);
        cv.put(MovieContract.MovieEntry.COLUMN_RATING, this.voteAverage);
        cv.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, this.releaseDate);
        cv.put(MovieContract.MovieEntry.COLUMN_SYNOPSIS, this.overview);
        return cv;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(Double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public byte[] getPosterByteArr() {
        return posterByteArr;
    }

    public void setPosterByteArr(byte[] posterByteArr) {
        this.posterByteArr = posterByteArr;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(voteAverage);
        dest.writeValue(title);
        dest.writeValue(posterPath);
        dest.writeValue(backdropPath);
        dest.writeValue(overview);
        dest.writeValue(releaseDate);
        dest.writeValue(posterByteArr);
    }

    public int describeContents() {
        return 0;
    }

}