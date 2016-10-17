package com.devabit.takestock.screen.advert.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.devabit.takestock.R;
import com.devabit.takestock.data.model.Photo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Victor Artemyev on 23/05/2016.
 */
public class AdvertPhotosAdapter extends RecyclerView.Adapter<AdvertPhotosAdapter.ViewHolder> {

    private final LayoutInflater mLayoutInflater;
    private final List<Photo> mPhotos;

    public AdvertPhotosAdapter(Context context) {
        mLayoutInflater = LayoutInflater.from(context);
        mPhotos = new ArrayList<>();
    }

    @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.item_advert_photo, parent, false);
        return new ViewHolder(view);
    }

    @Override public void onBindViewHolder(ViewHolder holder, int position) {
        Photo photo = mPhotos.get(position);
        holder.loadPhoto(photo);
    }

    @Override public int getItemCount() {
        return mPhotos.size();
    }

    public void addPhotos(List<Photo> photos) {
        mPhotos.addAll(photos);
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        final ImageView mImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            mImageView = (ImageView) itemView;
        }

        void loadPhoto(Photo photo) {
            Glide.with(mImageView.getContext())
                    .load(photo.getImage())
                    .centerCrop()
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(mImageView);
        }
    }
}
