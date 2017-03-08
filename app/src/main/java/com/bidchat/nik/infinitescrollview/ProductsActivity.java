package com.bidchat.nik.infinitescrollview;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

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

public class ProductsActivity extends AppCompatActivity {
    private ArrayList<String> mArrImageUrl;
    private GridRecyclerViewAdapter mGridViewAdapter;

    public static final int INTERVAL = 10;
    private int mPageNo = 1;

    public final int GRID_COLUMNS = 2;

    GridLayoutManager lLayout;
    private ProgressBar mProgressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);

        Button buttonOpen = (Button) findViewById(R.id.products_button_open);
        buttonOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initGallery();
            }
        });
    }

    public void initGallery() {
        final ViewGroup rootView = (ViewGroup) findViewById(android.R.id.content);

        FrameLayout relativeRootLayout = new FrameLayout(ProductsActivity.this);
        relativeRootLayout.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        relativeRootLayout.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
        rootView.addView(relativeRootLayout);

        lLayout = new GridLayoutManager(ProductsActivity.this, GRID_COLUMNS);
        RecyclerView gridView = new RecyclerView(ProductsActivity.this);
        gridView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT));
        gridView.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
        gridView.setHasFixedSize(true);
        gridView.setLayoutManager(lLayout);
        mArrImageUrl = new ArrayList<>();
        mGridViewAdapter = new GridRecyclerViewAdapter(ProductsActivity.this, mArrImageUrl, gridView);
        gridView.setAdapter(mGridViewAdapter);
        mGridViewAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                fetchProducts(++mPageNo);
            }
        });
        relativeRootLayout.addView(gridView);

        mProgressBar = new ProgressBar(ProductsActivity.this);
        mProgressBar.setProgressDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.custome_progressbar));
        FrameLayout.LayoutParams loaderLayoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        loaderLayoutParams.gravity= Gravity.CENTER;
        mProgressBar.setLayoutParams(loaderLayoutParams);
        mProgressBar.setActivated(true);
        mProgressBar.setVisibility(View.VISIBLE);
        relativeRootLayout.addView(mProgressBar);

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

        // Call<ProductResponse> call = service.fetchProducts();
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
                    Log.d("Tag", "Number of products: " + products.size());
                    for (int i = 0; i < products.size(); i++) {
                        Log.d("Image Url", "Url : " + products.get(i).getImage().getSrc());
                        mArrImageUrl.add(products.get(i).getImage().getSrc());
                    }
                    mGridViewAdapter.notifyDataSetChanged();
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
            }
        });
    }
}
