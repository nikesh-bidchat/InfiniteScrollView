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

import com.bidchat.nik.infinitescrollview.adapter.OnLoadMoreListener;
import com.bidchat.nik.infinitescrollview.adapter.ProductGridAdapter;
import com.bidchat.nik.infinitescrollview.model.Product;
import com.bidchat.nik.infinitescrollview.webservice.RetrofitClient;
import com.bidchat.nik.infinitescrollview.webservice.RetrofitService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowProductsActivity extends AppCompatActivity {

    private static final int GRID_COLUMNS = 2;
    public static final int PAGE_SIZE = 8;

    private List<Product> products = new ArrayList<>();
    private ProductGridAdapter productGridAdapter;
    private ProgressBar progressBar;
    private SwipeRefreshLayout swipeRefreshProducts;

    private int pageNo = 1;
    private boolean isLastPage = false;
    private int collectionId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_products);

        this.collectionId = 422081615;

        GridLayoutManager layoutManager = new GridLayoutManager(this, GRID_COLUMNS);
        RecyclerView gridViewProducts = (RecyclerView) findViewById(R.id.recycler_product_list);
        gridViewProducts.setLayoutManager(layoutManager);

        productGridAdapter = new ProductGridAdapter(this, products, gridViewProducts);
        gridViewProducts.setAdapter(productGridAdapter);

        productGridAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                Log.d("Status", "isLastPage : " + isLastPage);
                Log.d("Status", "isLoading : " + productGridAdapter.getIsLoading());
                Log.d("Status", "products Length : " + products.size());
                if (!isLastPage)
                    fetchProducts(++pageNo);
            }
        });

        swipeRefreshProducts = (SwipeRefreshLayout) findViewById(R.id.swiperefresh_products);
        swipeRefreshProducts.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        pageNo = 1;
                        isLastPage = false;
                        swipeRefreshProducts.isRefreshing();
                        productGridAdapter.setIsLoading(false);
                        products.clear();
                        fetchProducts(pageNo);
                    }
                }
        );

        progressBar = (ProgressBar) findViewById(R.id.progress_bar_loader);
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setActivated(true);
        fetchProducts(pageNo);
    }

    /**
     * Fetches products from webservice
     *
     * @param pageNo - page number to fetch
     */
    public void fetchProducts(int pageNo) {

        RetrofitClient retrofitClient = new RetrofitClient();
        RetrofitService retrofitService = retrofitClient.getRestService();

        Call<ProductResponse> call = retrofitService.fetchProducts(collectionId, PAGE_SIZE, pageNo);
        call.enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, final Response<ProductResponse> response) {
                if (response.isSuccessful()) {


                    List<Product> retrievedProducts = response.body().getResults() != null ? response.body().getResults() : new ArrayList<Product>();
                    products.addAll(retrievedProducts);

                    Log.d("Tag " + ShowProductsActivity.this.pageNo, "Number of products: " + products.size());


                    if (products.size() != PAGE_SIZE)
                        isLastPage = true;
                    Handler mainHandler = new Handler(getMainLooper());
                    Runnable myRunnable = new Runnable() {
                        @Override
                        public void run() {
                            swipeRefreshProducts.setRefreshing(false);
                            progressBar.setVisibility(View.GONE);
//                            productGridAdapter.updateProducts(products);
                            productGridAdapter.notifyDataSetChanged();
                        }
                    };
                    mainHandler.post(myRunnable);

                } else {
                    Log.d("Response", "Failure : " + response.errorBody());
                }

                productGridAdapter.setIsLoading(false);
            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                Log.e("Tag", t.toString());
                productGridAdapter.setIsLoading(false);
            }
        });
    }
}
