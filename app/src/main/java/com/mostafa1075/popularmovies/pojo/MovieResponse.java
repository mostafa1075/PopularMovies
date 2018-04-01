package com.mostafa1075.popularmovies.pojo;

/**
 * Created using http://www.jsonschema2pojo.org/
 */


import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MovieResponse {

    @SerializedName("results")
    @Expose
    private List<Movie> results = null;

    public List<Movie> getResults() {
        return results;
    }

    public void setResults(List<Movie> results) {
        this.results = results;
    }
}

