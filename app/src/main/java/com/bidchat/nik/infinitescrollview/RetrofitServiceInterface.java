package com.bidchat.nik.infinitescrollview;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by AndroidTest on 3/6/2017.
 */

public interface RetrofitServiceInterface {

    @Headers("Authorization: Basic MTI5NzlmOTJiYTA0NWMzNmRhZGNjOGUyMTgwNzczNzU6YWY5MmQzMzYxYzZmNjQ4YzgxNWIzZjYwMjcwYTJkODA=")
    @GET("/admin/products.json")
    public Call<ProductResponse> fetchProducts();

    @Headers("Authorization: Basic MTI5NzlmOTJiYTA0NWMzNmRhZGNjOGUyMTgwNzczNzU6YWY5MmQzMzYxYzZmNjQ4YzgxNWIzZjYwMjcwYTJkODA=")
    @GET("/admin/products.json")
    public Call<ProductResponse> fetchProducts(@Query("collection_id") String collection_id, @Query("limit") int limit, @Query("page") int page);

    @Headers("Authorization: Basic MTI5NzlmOTJiYTA0NWMzNmRhZGNjOGUyMTgwNzczNzU6YWY5MmQzMzYxYzZmNjQ4YzgxNWIzZjYwMjcwYTJkODA=")
    @GET("/admin/products.json")
    void fetchProducts(@Query("collection_id") String collection_id, @Query("limit") String limit, @Query("page") String page, Callback<ProductResponse> callback);

    // https://12979f92ba045c36dadcc8e218077375:af92d3361c6f648c815b3f60270a2d80@bidchat.myshopify.com/admin/products.json?collection_id= 422082127&limit=10&page=1
}
