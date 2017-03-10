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
import android.widget.TextView;

import com.bidchat.nik.infinitescrollview.adapter.OnLoadMoreListener;
import com.bidchat.nik.infinitescrollview.adapter.ProductGridAdapter;
import com.bidchat.nik.infinitescrollview.model.ErrorResponse;
import com.bidchat.nik.infinitescrollview.model.Product;
import com.bidchat.nik.infinitescrollview.webservice.RetrofitClient;
import com.bidchat.nik.infinitescrollview.webservice.RetrofitService;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ShowProductsActivity extends AppCompatActivity {

    public static final int GRID_COLUMNS = 2;
    public static final int PAGE_SIZE = 8;

    private List<Product> products = new ArrayList<>();
    private ProductGridAdapter productGridAdapter;
    private ProgressBar progressBar;
    private SwipeRefreshLayout swipeRefreshProducts;
    private TextView textStatusMessage;

    private int pageNo = 1;
    private boolean isLastPage = false;
    // private boolean isFirstTime = true;
    private int collectionId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_products);

        this.collectionId = 422081615;

        textStatusMessage = (TextView) findViewById(R.id.text_status_message);
        GridLayoutManager layoutManager = new GridLayoutManager(this, GRID_COLUMNS);
        RecyclerView gridViewProducts = (RecyclerView) findViewById(R.id.recycler_product_list);
        gridViewProducts.setLayoutManager(layoutManager);

        productGridAdapter = new ProductGridAdapter(this, products, gridViewProducts);
        gridViewProducts.setAdapter(productGridAdapter);

        progressBar = (ProgressBar) findViewById(R.id.progress_bar_loader);
        progressBar.setVisibility(View.VISIBLE);
        fetchProducts(pageNo);

        productGridAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                Log.d("Status", "isLastPage : " + isLastPage);
                Log.d("Status", "isLoading : " + productGridAdapter.isLoading());
                Log.d("Status", "products Length : " + products.size());
                if (!isLastPage && !productGridAdapter.isLoading())
                    fetchProducts(++pageNo);
            }
        });

        swipeRefreshProducts = (SwipeRefreshLayout) findViewById(R.id.swiperefresh_products);
        swipeRefreshProducts.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        textStatusMessage.setText(getString(R.string.loadingProducts));
                        textStatusMessage.setVisibility(View.VISIBLE);
                        pageNo = 1;
                        isLastPage = false;
                        productGridAdapter.setLoading(true);
                        productGridAdapter.setFirstTime(true);
                        products.clear();
                        productGridAdapter.notifyDataSetChanged();
                        fetchProducts(pageNo);
                    }
                }
        );
    }

    /**
     * Fetches products from webservice
     *
     * @param pageNo - page number to fetch
     */
    public void fetchProducts(int pageNo) {

        final RetrofitClient retrofitClient = new RetrofitClient();
        RetrofitService retrofitService = retrofitClient.getRestService();

        Call<ProductResponse> call = retrofitService.fetchProducts(collectionId, PAGE_SIZE, pageNo);
        call.enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, final Response<ProductResponse> response) {

                Log.d("Crash Test", "fetchProducts");
                if (response.isSuccessful()) {
                    List<Product> retrievedProducts = response.body().getResults() != null ? response.body().getResults() : new ArrayList<Product>();
                    products.addAll(retrievedProducts);

                    Log.d("Tag " + ShowProductsActivity.this.pageNo, "Number of products: " + products.size());

                    if (retrievedProducts.size() != PAGE_SIZE)
                        isLastPage = true;
                    pushToMainThread();
                    Handler mainHandler = new Handler(getMainLooper());
                    Runnable myRunnable = new Runnable() {
                        @Override
                        public void run() {
                            productGridAdapter.notifyDataSetChanged();
                            if (products.size() == 0)
                                textStatusMessage.setText(getString(R.string.noProducts));
                            else
                                textStatusMessage.setVisibility(View.GONE);
                        }
                    };
                    mainHandler.post(myRunnable);
                } else {
                    pushToMainThread();
                    if (response.errorBody() != null) {
                        Converter<ResponseBody, ErrorResponse> errorConverter =
                                retrofitClient.getRestClient().responseBodyConverter(ErrorResponse.class, new Annotation[0]);
                        try {
                            ErrorResponse error = errorConverter.convert(response.errorBody());
                            Log.d("Error", "Error Message : " + error.getErrors());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                Log.e("Tag", t.toString());
                pushToMainThread();
            }
        });
    }

    public void pushToMainThread() {
        Handler mainHandler = new Handler(getMainLooper());
        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {
                swipeRefreshProducts.setRefreshing(false);
                progressBar.setVisibility(View.GONE);
                productGridAdapter.setLoading(false);
                textStatusMessage.setText(getString(R.string.errorMessage));
            }
        };
        mainHandler.post(myRunnable);
    }
}
