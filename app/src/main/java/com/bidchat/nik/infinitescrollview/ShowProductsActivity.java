package com.bidchat.nik.infinitescrollview;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
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
    private RecyclerView gridView;

    public static final int INTERVAL = 8;
    private int mPageNo = 1;

    public final int GRID_COLUMNS = 2;

    private ProgressBar mProgressBar;
    // private int mCurrentPage = 0;
    // private int lastPageItemsCount = 0;

    private boolean isLastPage = false;

    private SwipeRefreshLayout swipeRefreshProducts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_products);

        GridLayoutManager lLayout = new GridLayoutManager(this, GRID_COLUMNS);
        gridView = (RecyclerView) findViewById(R.id.recycler_product_list);
        gridView.setLayoutManager(lLayout);
        mArrImageUrl = new ArrayList<>();
        mGridViewAdapter = new LayoutGridRecyclerViewAdapter(this, mArrImageUrl, gridView, getSupportFragmentManager());
        gridView.setAdapter(mGridViewAdapter);
        mGridViewAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                Log.d("Status", "isLastPage : " + isLastPage);
                Log.d("Status", "isLoading : " + mGridViewAdapter.getIsLoading());
                Log.d("Status", "mArrImageUrl Length : " + mArrImageUrl.size());
                if (!isLastPage)
                    fetchProducts(++mPageNo);
            }
        });

        swipeRefreshProducts = (SwipeRefreshLayout) findViewById(R.id.swiperefresh_products);
        swipeRefreshProducts.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        mPageNo = 1;
                        isLastPage = false;
                        swipeRefreshProducts.isRefreshing();
                        mGridViewAdapter.setIsLoading(false);
                        mArrImageUrl.clear();
                        fetchProducts(mPageNo);
                    }
                }
        );

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
            public void onResponse(Call<ProductResponse> call, final Response<ProductResponse> response) {
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
                    if (products.size() != INTERVAL)
                        isLastPage = true;
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
                    Handler mainHandler = new Handler(getMainLooper());
                    Runnable myRunnable = new Runnable() {
                        @Override
                        public void run() {
                            if (mProgressBar.isActivated()) {
                                mProgressBar.setVisibility(View.GONE);
                                mProgressBar.setActivated(false);
                            }
                            if (swipeRefreshProducts.isRefreshing()) {
                                swipeRefreshProducts.setRefreshing(false);
                            }
                            mGridViewAdapter.notifyDataSetChanged();
                            mGridViewAdapter.setIsLoading(false);
                        } // This is your code
                    };
                    mainHandler.post(myRunnable);
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
