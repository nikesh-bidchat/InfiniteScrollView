package com.bidchat.nik.infinitescrollview;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

class LayoutGridRecyclerViewAdapter extends RecyclerView.Adapter<LayoutGridRecyclerViewAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<String> mArrImageUrl;
    private OnLoadMoreListener onLoadMoreListener;

    private int visibleThreshold = ProductsActivity.INTERVAL;
    private int lastVisibleItem, totalItemCount;
    private boolean isLoading;

    private FragmentManager mFm;

    LayoutGridRecyclerViewAdapter(Context context, ArrayList<String> arrImageUrl, RecyclerView recyclerView, FragmentManager fm) {
        mContext = context;
        mArrImageUrl = arrImageUrl;
        mFm = fm;
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

    public void setIsLoading(boolean value) {
        isLoading = value;
    }

    public boolean getIsLoading() {
        return  isLoading;
    }

    void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_column_product, parent, false);
        int width = parent.getMeasuredWidth();
        view.setLayoutParams(new GridView.LayoutParams(width / 2, width / 2));
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Picasso.with(mContext)
                .load(mArrImageUrl.get(position))
                .placeholder(R.drawable.progress_animation)
                .error(R.drawable.ic_no_image).fit().centerCrop().noFade()
                .into(holder.imageBackground);
    }

    @Override
    public int getItemCount() {
        return mArrImageUrl.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        final ImageView imageBackground;
        final Button buttonShop;

        ViewHolder(View view) {
            super(view);
            mView = view;
            imageBackground = (ImageView) view.findViewById(R.id.image_product);
            buttonShop = (Button) view.findViewById(R.id.button_shop);
            buttonShop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DialogFragment dialogShop = new ShopDialogFragment();
                    dialogShop.show(mFm, "");
                }
            });
        }
    }

    public static class ShopDialogFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(R.string.show_product_dialog_title)
                    .setMessage(R.string.show_product_dialog_body)
                    .setPositiveButton(R.string.globle_yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // FIRE ZE MISSILES!
                        }
                    })
                    .setNegativeButton(R.string.globle_no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                            dismiss();
                        }
                    });
            // Create the AlertDialog object and return it
            return builder.create();
        }
    }
} 