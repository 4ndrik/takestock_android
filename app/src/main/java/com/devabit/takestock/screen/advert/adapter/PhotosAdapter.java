package com.devabit.takestock.screen.advert.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.devabit.takestock.R;
import com.devabit.takestock.data.model.Photo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Victor Artemyev on 05/05/2016.
 */
public class PhotosAdapter extends RecyclerView.Adapter<PhotosAdapter.ViewHolder> {

    private static final int MAX_PHOTO_COUNT = 4;

    private final LayoutInflater mLayoutInflater;
    private final List<Photo> mPhotos;

    public interface OnPickPhotoListener {
        void onPick();
    }

    private OnPickPhotoListener mPickPhotoListener;

    public interface OnEditPhotoListener {
        void onEdit(Photo photo);
    }

    private OnEditPhotoListener mEditPhotoListener;

    public PhotosAdapter(Context context) {
        mLayoutInflater = LayoutInflater.from(context);
        mPhotos = new ArrayList<>(MAX_PHOTO_COUNT);
        mPhotos.add(null);
    }

    @Override public ViewHolder onCreateViewHolder(ViewGroup parent, @LayoutRes int viewType) {
        View itemView = inflateImageView(viewType, parent);
        switch (viewType) {
            case R.layout.item_advert_add_photo_button:
                return new ButtonViewHolder(itemView);

            default:
                return new ImageViewHolder(itemView);
        }
    }

    private View inflateImageView(@LayoutRes int resId, ViewGroup parent) {
        return mLayoutInflater.inflate(resId, parent, false);
    }

    @Override public void onBindViewHolder(ViewHolder holder, int position) {
        if (holder.getItemViewType() == R.layout.item_advert_photo_image_view) {
            ((ImageViewHolder) holder).bindPhoto(mPhotos.get(position));
        }
    }

    @Override public int getItemCount() {
        return mPhotos.size();
    }

    @Override public int getItemViewType(int position) {
        Photo photo = mPhotos.get(position);
        if (photo == null) return R.layout.item_advert_add_photo_button;
        return R.layout.item_advert_photo_image_view;
    }

    public void addPhoto(Photo photo) {
        if (mPhotos.size() == MAX_PHOTO_COUNT
                && mPhotos.get(mPhotos.size() - 1) == null) {
            mPhotos.remove(mPhotos.size() - 1);
            mPhotos.add(photo);
            notifyItemInserted(mPhotos.size());
        } else {
            mPhotos.add(mPhotos.size() - 1, photo);
            notifyItemInserted(mPhotos.size() - 1);
        }
    }

    public void removePhoto(Photo photo) {
        int position = mPhotos.indexOf(photo);
        mPhotos.remove(position);
        notifyItemRemoved(position);
    }

    public void replacePhotoWith(Photo oldPhoto, Photo newPhoto) {
        int position = mPhotos.indexOf(oldPhoto);
        mPhotos.set(position, newPhoto);
        notifyItemChanged(position);
    }

    public void setPhotos(List<Photo> photos) {
        mPhotos.clear();
        mPhotos.addAll(photos);
        if (mPhotos.size() < MAX_PHOTO_COUNT) {
            mPhotos.add(null);
        }
        notifyDataSetChanged();
    }

    public List<Photo> getPhotos() {
        if (mPhotos.get(mPhotos.size() - 1) == null) {
            return mPhotos.subList(0, mPhotos.size() - 1);
        }
        return mPhotos;
    }

    public void setOnPickPhotoListener(OnPickPhotoListener pickPhotoListener) {
        mPickPhotoListener = pickPhotoListener;
    }

    public void setOnEditPhotoListener(OnEditPhotoListener editPhotoListener) {
        mEditPhotoListener = editPhotoListener;
    }

    private class ButtonViewHolder extends ViewHolder {

        final Button button;

        ButtonViewHolder(View itemView) {
            super(itemView);
            button = (Button) itemView;
            button.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    if (mPickPhotoListener != null) mPickPhotoListener.onPick();
                }
            });
        }
    }

    private class ImageViewHolder extends ViewHolder {

        final ImageView imageView;
        Photo mPhoto;

        ImageViewHolder(View itemView) {
            super(itemView);
            this.imageView = (ImageView) itemView;
            this.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override public boolean onLongClick(View v) {
                    if (mEditPhotoListener != null) mEditPhotoListener.onEdit(mPhoto);
                    return true;
                }
            });
        }

        void bindPhoto(final Photo photo) {
            mPhoto = photo;
            Glide.with(imageView.getContext())
                    .load(photo.getImage())
                    .placeholder(R.color.grey_400)
                    .centerCrop()
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(imageView);
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
