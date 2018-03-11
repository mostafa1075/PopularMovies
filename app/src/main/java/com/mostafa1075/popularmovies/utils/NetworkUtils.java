package com.mostafa1075.popularmovies.utils;

import android.net.Uri;

import com.mostafa1075.popularmovies.BuildConfig;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

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
     * Poster URL related constants
     */
    private static final String POSTER_BASE_URL = "http://image.tmdb.org/t/p";
    private static final String IMAGE_SIZE = "w500";
    /**
     * Video thumbnail URL related constants
     */
    private static final String VIDEO_THUMBNAIL_BASE_URL = "http://img.youtube.com/vi";
    private static final String VIDEO_THUMBNAIL_NUMBER = "0.jpg";

    private static final String VIDEO_BASE_URL = "http://www.youtube.com/watch";
    private static final String VIDEO_QUERY_KEY = "v";

    public static String buildImageUrl(String posterPath) {
        Uri imageUri = Uri.parse(POSTER_BASE_URL).buildUpon()
                .appendPath(IMAGE_SIZE)
                .appendEncodedPath(posterPath) // The additional '/' in posterPath messed this up
                .build();

        return imageUri.toString();
    }
    /** gets the video thumbnail using the video path */
    public static String buildVideoThumbnailUrl(String videoPath){
        Uri thumbnailUri = Uri.parse(VIDEO_THUMBNAIL_BASE_URL)
                .buildUpon()
                .appendPath(videoPath)
                .appendPath(VIDEO_THUMBNAIL_NUMBER)
                .build();
        return thumbnailUri.toString();
    }

    public  static Uri buildVideoUri(String videoKey){
        Uri VideoUri = Uri.parse(VIDEO_BASE_URL)
                .buildUpon()
                .appendQueryParameter(VIDEO_QUERY_KEY, videoKey)
                .build();
        return VideoUri;
    }
}
