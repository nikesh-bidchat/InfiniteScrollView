package com.bidchat.nik.infinitescrollview;

/**
 * Created by AndroidTest on 3/6/2017.
 */

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProductResponse {
    @SerializedName("products")
    private List<Product> results;

    public List<Product> getResults() {
        return results;
    }

    public void setResults(List<Product> results) {
        this.results = results;
    }
}
