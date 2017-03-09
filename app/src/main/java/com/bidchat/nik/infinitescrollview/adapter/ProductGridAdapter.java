package com.bidchat.nik.infinitescrollview.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.bidchat.nik.infinitescrollview.R;
import com.bidchat.nik.infinitescrollview.ShowProductsActivity;
import com.bidchat.nik.infinitescrollview.model.Product;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductGridAdapter extends RecyclerView.Adapter<ProductGridAdapter.ProductsViewHolder> {

    private Context context;
    private List<Product> products;
    private OnLoadMoreListener onLoadMoreListener;

    private int lastVisibleItem, totalItemCount;
    private boolean isLoading;

    public ProductGridAdapter(Context context, List<Product> products, RecyclerView recyclerView) {
        this.context = context;
        this.products = products;

        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView
                    .getLayoutManager();
            recyclerView
                    .addOnScrollListener(new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrolled(RecyclerView recyclerView,
                                               int dx, int dy) {
                            super.onScrolled(recyclerView, dx, dy);
                            totalItemCount = linearLayoutManager.getItemCount();
                            lastVisibleItem = linearLayoutManager
                                    .findLastVisibleItemPosition();
                            if (!isLoading
                                    && totalItemCount <= (lastVisibleItem + ShowProductsActivity.PAGE_SIZE)) {
                                // End has been reached
                                // Do something
                                if (onLoadMoreListener != null) {
                                    onLoadMoreListener.onLoadMore();
                                }
                                isLoading = true;
                            }
                        }
                    });
        }
    }

    public void setIsLoading(boolean value) {
        isLoading = value;
    }

    public boolean getIsLoading() {
        return isLoading;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    @Override
    public ProductsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_column_product, parent, false);
        int width = parent.getMeasuredWidth();
        view.setLayoutParams(new GridView.LayoutParams(width / 2, width / 2));
        return new ProductsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ProductsViewHolder holder, final int position) {

        if (products.get(position).getImage() != null && products.get(position).getImage().getSrc() != null) {
            Picasso.with(context)
                    .load(products.get(position).getImage().getSrc())
                    .placeholder(R.drawable.progress_animation)
                    .error(R.drawable.ic_no_image).fit().centerCrop().noFade()
                    .into(holder.imageProduct);
        } else {

            holder.imageProduct.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_no_image));
        }


        holder.buttonShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Product Selected:" + products.get(position).getTitle(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    class ProductsViewHolder extends RecyclerView.ViewHolder {
        final ImageView imageProduct;
        final Button buttonShop;

        ProductsViewHolder(View view) {
            super(view);
            imageProduct = (ImageView) view.findViewById(R.id.image_product);
            buttonShop = (Button) view.findViewById(R.id.button_shop);
        }
    }
} 