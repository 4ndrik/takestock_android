package com.devabit.takestock.screens.sellSomething.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.devabit.takestock.R;
import com.devabit.takestock.data.models.Photo;
import com.devabit.takestock.util.FontCache;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Victor Artemyev on 05/05/2016.
 */
public class PhotoGalleryAdapter extends RecyclerView.Adapter<PhotoGalleryAdapter.ViewHolder> {

    public static final int MAX_PHOTO_COUNT = 4;

    public static final int TYPE_BUTTON = 1;
    public static final int TYPE_IMAGE = 2;

    private final LayoutInflater mLayoutInflater;
    private final List<Photo> mPhotos;

    public interface OnPickPhotoListener {
        void onPick();
    }

    private OnPickPhotoListener mPickPhotoListener;

    public PhotoGalleryAdapter(Context context) {
        mLayoutInflater = LayoutInflater.from(context);
        mPhotos = new ArrayList<>(MAX_PHOTO_COUNT);
    }

    @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case TYPE_BUTTON:
                view = mLayoutInflater.inflate(R.layout.item_button_gallery_selling, parent, false);
                return new ButtonViewHolder(view);

            case TYPE_IMAGE:
                view = mLayoutInflater.inflate(R.layout.item_image_view_gallery_selling, parent, false);
                return new ImageViewHolder(view);

            default:
                return null;
        }
    }

    @Override public void onBindViewHolder(ViewHolder holder, int position) {
        int type = holder.getItemViewType();
        switch (type) {
            case TYPE_BUTTON:
                ButtonViewHolder buttonHolder = (ButtonViewHolder) holder;
                buttonHolder.setOnClickListener(mClickListener);
                if (position == MAX_PHOTO_COUNT) {
                    buttonHolder.setVisibility(false);
                } else {
                    buttonHolder.setVisibility(true);
                }
                break;

            case TYPE_IMAGE:
                ImageViewHolder imageHolder = (ImageViewHolder) holder;
                imageHolder.loadImageFromFile(mPhotos.get(position));
                break;
        }
    }

    private final View.OnClickListener mClickListener
            = new View.OnClickListener() {
        @Override public void onClick(View v) {
            if (mPickPhotoListener != null) mPickPhotoListener.onPick();
        }
    };

    @Override public int getItemCount() {
        return mPhotos.size() + 1;
    }

    @Override public int getItemViewType(int position) {
        int itemCount = getItemCount();
        if (mPhotos.isEmpty() || position == itemCount - 1) {
            return TYPE_BUTTON;
        } else {
            return TYPE_IMAGE;
        }
    }

    public void addPhotoFile(Photo photo) {
        mPhotos.add(photo);
        notifyDataSetChanged();
    }

    public List<Photo> getPhotos() {
        return mPhotos;
    }

    public void setOnPickPhotoListener(OnPickPhotoListener pickPhotoListener) {
        mPickPhotoListener = pickPhotoListener;
    }

    static class ButtonViewHolder extends ViewHolder {

        final Button button;

        public ButtonViewHolder(View itemView) {
            super(itemView);
            button = (Button) itemView;
            Typeface typeface = FontCache.getTypeface(button.getContext(), R.string.font_brandon_bold);
            button.setTypeface(typeface);
            button.getViewTreeObserver().addOnGlobalLayoutListener(
                    new ViewTreeObserver.OnGlobalLayoutListener() {
                        @Override public void onGlobalLayout() {
                            int height = button.getHeight();
                            int padding = height / 4;
                            button.setPadding(0, padding, 0, padding);
                            button.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        }
                    });
        }

        void setOnClickListener(View.OnClickListener clickListener) {
            button.setOnClickListener(clickListener);
        }

        void setVisibility(boolean isVisible) {
            RecyclerView.LayoutParams param = (RecyclerView.LayoutParams)itemView.getLayoutParams();
            if (isVisible) {
                param.height = LinearLayout.LayoutParams.MATCH_PARENT;
                param.width = LinearLayout.LayoutParams.MATCH_PARENT;
                itemView.setVisibility(View.VISIBLE);
            } else {
                button.setVisibility(View.GONE);
                param.height = 0;
                param.width = 0;
            }
            button.setLayoutParams(param);
        }
    }

    static class ImageViewHolder extends ViewHolder {

        final Context context;
        final Picasso picasso;
        final ImageView imageView;

        public ImageViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            picasso = Picasso.with(context);
            imageView = (ImageView) itemView;
        }

        void loadImageFromFile(Photo photo) {
            picasso.load(photo.getImagePath())
                    .fit()
                    .centerCrop()
                    .into(imageView);
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
