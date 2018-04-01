package com.mostafa1075.popularmovies.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

import com.mostafa1075.popularmovies.BuildConfig;

/**
 * Created by mosta on 21-Feb-18.
 */

public class NetworkUtils {

    /**
     * You should add your own API key in gradle.properties as tmdbToken = "API_KEY". I found out how to do this here:
     * https://www.learnhowtoprogram.com/android/web-service-backends-and-custom-fragments/managing-api-keys
     */
    public static final String API_KEY = BuildConfig.TMDB_TOKEN;

    /**
     * Images URL related constants
     */
    private static final String POSTER_BASE_URL = "http://image.tmdb.org/t/p";
    public static final int POSTER_IMAGE_WIDTH = 342;
    private static final String POSTER_IMAGE_SIZE = "w342";
    private static final String BACKDROP_IMAGE_SIZE = "w500";
    /**
     * Video thumbnail URL related constants
     */
    private static final String VIDEO_THUMBNAIL_BASE_URL = "http://img.youtube.com/vi";
    private static final String VIDEO_THUMBNAIL_PATH = "0.jpg";
    /**
     * Video URL related constants
     */
    private static final String VIDEO_BASE_URL = "http://www.youtube.com/watch";
    private static final String VIDEO_QUERY_KEY = "v";

    public static String buildPosterImageUrl(String posterPath) {
        Uri imageUri = Uri.parse(POSTER_BASE_URL).buildUpon()
                .appendPath(POSTER_IMAGE_SIZE)
                .appendEncodedPath(posterPath)
                .build();

        return imageUri.toString();
    }

    public static String buildBackdropImageUrl(String backdropPath) {
        Uri imageUri = Uri.parse(POSTER_BASE_URL).buildUpon()
                .appendPath(BACKDROP_IMAGE_SIZE)
                .appendEncodedPath(backdropPath)
                .build();

        return imageUri.toString();
    }
    /**
     * gets the video thumbnail using the video path
     */
    public static String buildVideoThumbnailUrl(String videoPath) {
        Uri thumbnailUri = Uri.parse(VIDEO_THUMBNAIL_BASE_URL)
                .buildUpon()
                .appendPath(videoPath)
                .appendPath(VIDEO_THUMBNAIL_PATH)
                .build();
        return thumbnailUri.toString();
    }

    public static Uri buildVideoUri(String videoKey) {
        return Uri.parse(VIDEO_BASE_URL)
                .buildUpon()
                .appendQueryParameter(VIDEO_QUERY_KEY, videoKey)
                .build();

    }
    /**
     * Checks whether there is internet connection or not. Found in this StackOverflow post:
     * https://stackoverflow.com/questions/1560788/how-to-check-internet-access-on-android-inetaddress-never-times-out
     */
    public static boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }

}
