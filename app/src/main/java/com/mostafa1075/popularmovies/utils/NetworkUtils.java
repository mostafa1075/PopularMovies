package com.mostafa1075.popularmovies.utils;

import android.net.Uri;

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

    private static final OkHttpClient client = new OkHttpClient();

    private static final String MOVIES_BASE_URL = "https://api.themoviedb.org/3";

    /* The main path for movies */
    private static final String MOVIE_PATH = "movie";
    /* The path for the top rated movies */
    private static final String TOP_RATED_PATH = "top_rated";
    /* The path for the most popular movies */
    private static final String POPULAR_PATH = "popular";

    /* The page number parameter to be sent to the API */
    private static final String PAGE_NUM_PARAM = "page";
    private static final String API_KEY_PARAM = "api_key";

    // TODO: look up how to hide the API key
    private static final String API_KEY = "";

    /* The whole body of this method is copied from OkHttp github page example: http://square.github.io/okhttp/.
    * It downloads a URL and returns its contents as a string.
    */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    /**
     * Builds the URL that'll be used for querying the movies
     *
     * @param sortByPath The path used to sort the movies in order
     * @param pageNum    the page number of the movies
     * @return The URL used to query the movies
     */
    public static URL getUrl(String sortByPath, String pageNum) {
        Uri MoviesQueryUri = Uri.parse(MOVIES_BASE_URL).buildUpon()
                .appendPath(MOVIE_PATH)
                .appendPath(sortByPath)
                .appendQueryParameter(API_KEY_PARAM, API_KEY)
                .appendQueryParameter(PAGE_NUM_PARAM, pageNum)
                .build();

        try {
            URL MoviesQueryUrl = new URL(MoviesQueryUri.toString());
            return MoviesQueryUrl;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
