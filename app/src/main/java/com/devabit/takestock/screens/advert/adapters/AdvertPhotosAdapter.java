package com.devabit.takestock.screens.advert.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.devabit.takestock.R;
import com.devabit.takestock.data.models.Photo;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Victor Artemyev on 23/05/2016.
 */
public class AdvertPhotosAdapter extends RecyclerView.Adapter<AdvertPhotosAdapter.ViewHolder> {

    private final LayoutInflater mLayoutInflater;
    private final List<Photo> mPhotos;

    public AdvertPhotosAdapter(Context context, List<Photo> photos) {
        mLayoutInflater = LayoutInflater.from(context);
        mPhotos = photos;
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

    static class ViewHolder extends RecyclerView.ViewHolder {

        final Picasso mPicasso;
        final ImageView mImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            mPicasso = Picasso.with(itemView.getContext());
            mImageView = (ImageView) itemView;
        }

        void loadPhoto(Photo photo) {
            mPicasso.load(photo.getImagePath())
                    .placeholder(R.drawable.ic_image_48dp)
                    .error(R.drawable.ic_image_48dp)
                    .into(mImageView);
        }
    }
}
