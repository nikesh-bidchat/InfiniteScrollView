package com.bidchat.nik.infinitescrollview.webservice;

import com.bidchat.nik.infinitescrollview.ProductResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

/**
 * Created by Nikesh
 * Created on 3/6/2017.
 */

public interface RetrofitService {

    @Headers("Authorization: Basic MTI5NzlmOTJiYTA0NWMzNmRhZGNjOGUyMTgwNzczNzU6YWY5MmQzMzYxYzZmNjQ4YzgxNWIzZjYwMjcwYTJkODA=")
    @GET("/admin/products.json")
    Call<ProductResponse> fetchProducts(@Query("collection_id") int collectionId, @Query("limit") int limit, @Query("page") int page);

}
