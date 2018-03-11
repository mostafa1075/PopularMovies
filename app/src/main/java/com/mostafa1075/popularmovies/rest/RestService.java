package com.mostafa1075.popularmovies.rest;

import com.mostafa1075.popularmovies.pojo.MovieResponse;
import com.mostafa1075.popularmovies.pojo.ReviewResponse;
import com.mostafa1075.popularmovies.pojo.VideoResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by mosta on 10-Mar-18.
 */

/**
 * References: Retrofit documentation http://square.github.io/retrofit/
 */
public interface RestService {

    /** The page number query string to be sent to the API */
    String PAGE_NUM_PARAM = "page";
    /** The API key query string to be sent to the API */
    String API_KEY_PARAM = "api_key";
    /** The path for the top rated movies */
    String TOP_RATED_PATH = "movie/top_rated";
    /** The path for the most popular movies */
    String POPULAR_PATH = "movie/popular";
    /** The path for the movie videos */
    String VIDEOS_PATH = "movie/{movie_id}/videos";
    /** The path for the movie reviews */
    String REVIEWS_PATH = "movie/{movie_id}/reviews";

    @GET(POPULAR_PATH)
    Call<MovieResponse> getPopularMovies(
            @Query(API_KEY_PARAM) String apiKey,
            @Query(PAGE_NUM_PARAM) String pageNum);

    @GET(TOP_RATED_PATH)
    Call<MovieResponse> getTopRatedMovies(
            @Query(API_KEY_PARAM) String apiKey,
            @Query(PAGE_NUM_PARAM) String pageNum);

    @GET(VIDEOS_PATH)
    Call<VideoResponse> getMovieVideos(@Path("movie_id") String movieId,
                                       @Query(API_KEY_PARAM) String apiKey);

    @GET(REVIEWS_PATH)
    Call<ReviewResponse> getMovieReviews(@Path("movie_id") String movieId,
                                         @Query(API_KEY_PARAM) String apiKey);
}
