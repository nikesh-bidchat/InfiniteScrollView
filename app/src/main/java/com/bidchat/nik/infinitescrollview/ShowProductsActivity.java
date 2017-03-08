package com.bidchat.nik.infinitescrollview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ShowProductsActivity extends AppCompatActivity {

    private ArrayList<String> mArrImageUrl;
    private LayoutGridRecyclerViewAdapter mGridViewAdapter;

    public static final int INTERVAL = 8;
    private int mPageNo = 1;

    public final int GRID_COLUMNS = 2;

    private ProgressBar mProgressBar;
    private int mCurrentPage = 0;
    private int lastPageItemsCount = 0;

    private boolean isLastPage = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_products);

        GridLayoutManager lLayout = new GridLayoutManager(this, GRID_COLUMNS);
        RecyclerView gridView = (RecyclerView) findViewById(R.id.recycler_product_list);
        gridView.setLayoutManager(lLayout);
        mArrImageUrl = new ArrayList<>();
        mGridViewAdapter = new LayoutGridRecyclerViewAdapter(this, mArrImageUrl, gridView, getSupportFragmentManager());
        gridView.setAdapter(mGridViewAdapter);
        mGridViewAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (!isLastPage)
                    fetchProducts(++mPageNo);
            }
        });

        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar_loader);
        mProgressBar.setVisibility(View.VISIBLE);
        mProgressBar.setActivated(true);
        fetchProducts(mPageNo);
    }

    public void fetchProducts(int pageNo) {
        final String BASE_URL = "https://bidchat.myshopify.com";
        final String COLLECTION_ID = "422081615";

        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
        builder.readTimeout(1, TimeUnit.MINUTES);
        builder.writeTimeout(1, TimeUnit.MINUTES);
        builder.connectTimeout(1, TimeUnit.MINUTES);
        OkHttpClient client = builder.build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        RetrofitServiceInterface service = retrofit.create(RetrofitServiceInterface.class);

        Call<ProductResponse> call = service.fetchProducts(COLLECTION_ID, INTERVAL, pageNo);
        call.enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                if (mProgressBar.isActivated()) {
                    mProgressBar.setVisibility(View.GONE);
                    mProgressBar.setActivated(false);
                }
                if (response.isSuccessful()) {
                    List<Product> products = response.body().getResults();
                    Log.d("Tag " + mPageNo, "Number of products: " + products.size());
                    // for (int i = lastPageItemsCount; i < products.size(); i++) {
                    for (int i = 0; i < products.size(); i++) {
                        if (products.get(i).getImage() != null)
                            // if (!products.get(i).getImage().getSrc().trim().equalsIgnoreCase(""))
                            mArrImageUrl.add(products.get(i).getImage().getSrc());
                        else
                            mArrImageUrl.add("no_image");
                    }
                    mGridViewAdapter.notifyDataSetChanged();
                    mGridViewAdapter.setIsLoading(false);
                    /*
                    if (products.size() != INTERVAL) {
                        if (mPageNo != mCurrentPage)
                            --mPageNo;
                        lastPageItemsCount = products.size();
                    } else {
                        lastPageItemsCount = 0;
                    }
                    mCurrentPage = mPageNo;
                    */
                    if (products.size() != INTERVAL) {
                        isLastPage = true;
                    }
                } else {
                    try {
                        Log.d("Response", "Failure : " + response.errorBody().string());
                    } catch (IOException e) {
                        Log.d("Exception", "Error : " + e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("Tag", t.toString());
                mGridViewAdapter.setIsLoading(false);
            }
        });
    }
}
