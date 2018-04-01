package com.mostafa1075.popularmovies.pojo;

/**
 * Created using http://www.jsonschema2pojo.org/
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VideoResponse {

    @SerializedName("results")
    @Expose
    private List<MovieVideo> results = null;

    public List<MovieVideo> getResults() {
        return results;
    }

    public void setResults(List<MovieVideo> results) {
        this.results = results;
    }
}
