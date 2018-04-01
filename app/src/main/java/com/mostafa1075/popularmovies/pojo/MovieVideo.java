package com.mostafa1075.popularmovies.pojo;

/**
 * Created using http://www.jsonschema2pojo.org/
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MovieVideo{

    @SerializedName("key")
    @Expose
    private String key;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("site")
    @Expose
    private String site;
    @SerializedName("size")
    @Expose
    private Integer size;
    @SerializedName("type")
    @Expose
    private String type;

    public MovieVideo() {}

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
