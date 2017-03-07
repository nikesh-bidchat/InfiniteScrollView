package com.bidchat.nik.infinitescrollview;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

class GridRecyclerViewAdapter extends RecyclerView.Adapter<GridRecyclerViewAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<String> mArrImageUrl;
    private OnLoadMoreListener onLoadMoreListener;

    private int visibleThreshold = ProductsActivity.INTERVAL;
    private int lastVisibleItem, totalItemCount;
    private boolean isLoading;

    public GridRecyclerViewAdapter(Context context, ArrayList<String> arrImageUrl, RecyclerView recyclerView) {
        mContext = context;
        mArrImageUrl = arrImageUrl;
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
                                    && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
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

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        FrameLayout frameRootView = new FrameLayout(mContext);
        frameRootView.setLayoutParams(new GridView.LayoutParams(GridView.LayoutParams.MATCH_PARENT, GridView.LayoutParams.WRAP_CONTENT));
        return new ViewHolder(frameRootView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Picasso.with(mContext)
                .load(mArrImageUrl.get(position))
                .placeholder(R.drawable.ic_place_holder)
                .error(R.drawable.ic_error_place_holder)
                .into(holder.imageBackground);
    }

    @Override
    public int getItemCount() {
        return mArrImageUrl.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final FrameLayout mView;
        final ImageView imageBackground;
        final Button buttonDummy;

        ViewHolder(View view) {
            super(view);
            mView = (FrameLayout) view;

            imageBackground = new ImageView(mContext);
            imageBackground.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT));
            imageBackground.setAdjustViewBounds(true);
            mView.addView(imageBackground);

            buttonDummy = new Button(mContext);
            FrameLayout.LayoutParams buttonDummyLayoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
            buttonDummyLayoutParams.gravity = Gravity.CENTER;
            buttonDummy.setLayoutParams(buttonDummyLayoutParams);
            buttonDummy.setText(mContext.getString(R.string.open));
            mView.addView(buttonDummy);
        }
    }
}