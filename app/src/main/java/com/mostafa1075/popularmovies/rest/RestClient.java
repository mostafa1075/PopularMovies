package com.mostafa1075.popularmovies.rest;

import com.google.gson.Gson;
import com.mostafa1075.popularmovies.BuildConfig;
import com.mostafa1075.popularmovies.pojo.Movie;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by mosta on 10-Mar-18.
 */

/**
 * References: Retrofit documentation http://square.github.io/retrofit/
 * Singleton Pattern https://www.tutorialspoint.com/design_pattern/singleton_pattern.htm
 * Adapted from this answer: https://stackoverflow.com/questions/36960627/android-retrofit-design-patterns
 */
public class RestClient {

    private static final String MOVIES_BASE_URL = "https://api.themoviedb.org/3/";
    private static RestClient instance = new RestClient();
    public RestService service;

    private RestClient() {
        Retrofit retrofit = buildRetrofit();
        service = retrofit.create(RestService.class);
    }

    private Retrofit buildRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(MOVIES_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
    public static RestClient getInstance(){
        return instance;
    }

}
