package com.mostafa1075.popularmovies.pojo;

/**
 * Created using http://www.jsonschema2pojo.org/
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ReviewResponse {

    @SerializedName("results")
    @Expose
    private List<MovieReview> results = null;

    public List<MovieReview> getResults() {
        return results;
    }

    public void setResults(List<MovieReview> results) {
        this.results = results;
    }
}
