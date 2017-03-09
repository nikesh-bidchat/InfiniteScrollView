package com.bidchat.nik.infinitescrollview;

import com.bidchat.nik.infinitescrollview.model.Product;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Nikesh
 * Created on 3/6/2017.
 */

public class ProductResponse {
    @SerializedName("products")
    private List<Product> results;

    List<Product> getResults() {
        return results;
    }
}
