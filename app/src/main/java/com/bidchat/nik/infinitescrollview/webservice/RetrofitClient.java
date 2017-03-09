package com.bidchat.nik.infinitescrollview.webservice;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Tushar Vengurlekar
 * Created on 9/03/17.
 */

public class RetrofitClient {


    private final static String BASE_URL = "https://bidchat.myshopify.com";

    private RetrofitService restService;

    public RetrofitClient() {
        init(BASE_URL);
    }

    public RetrofitClient(String baseURL) {
        init(baseURL);
    }

    private void init(String baseURL) {

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient httpClient = new OkHttpClient().newBuilder()
                .readTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .addInterceptor(logging)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseURL)
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        restService = retrofit.create(RetrofitService.class);
    }

    public RetrofitService getRestService() {
        return restService;
    }
}
