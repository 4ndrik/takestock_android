package com.devabit.takestock.screen.advert.detail;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.devabit.takestock.R;
import com.devabit.takestock.data.model.Advert;
import com.devabit.takestock.data.model.Photo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Victor Artemyev on 11/10/2016.
 */

public class AdvertListingAdapter extends RecyclerView.Adapter<AdvertListingAdapter.AdvertViewHolder> {

    private final LayoutInflater mLayoutInflater;
    private final List<Advert> mAdverts;

    public interface OnItemClickListener {
        void onItemClick(Advert advert);
    }

    private OnItemClickListener mItemClickListener;

    public AdvertListingAdapter(Context context) {
        mLayoutInflater = LayoutInflater.from(context);
        mAdverts = new ArrayList<>();
    }

    @Override public AdvertViewHolder onCreateViewHolder(ViewGroup parent, @LayoutRes int viewType) {
        View itemView = mLayoutInflater.inflate(R.layout.item_advert_listing, parent, false);
        return new AdvertViewHolder(itemView);
    }


    @Override public void onBindViewHolder(AdvertViewHolder holder, int position) {
        holder.bindAdvert(mAdverts.get(position));
    }

    @Override public int getItemCount() {
        return mAdverts.size();
    }

    public void addAdverts(List<Advert> adverts) {
        mAdverts.addAll(adverts);
        notifyDataSetChanged();
    }

    public void refreshAdverts(List<Advert> adverts) {
        mAdverts.clear();
        mAdverts.addAll(adverts);
        notifyDataSetChanged();
    }

    public void refreshAdvert(Advert advert) {
        int position = mAdverts.indexOf(advert);
        mAdverts.set(position, advert);
        notifyItemChanged(position);
    }

    public List<Advert> getAdverts() {
        return mAdverts;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mItemClickListener = listener;
    }

    class AdvertViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.photo_image_view) ImageView photoImageView;
        @BindView(R.id.name_text_view) TextView nameTextView;
        @BindView(R.id.location_text_view) TextView locationTextView;
        @BindView(R.id.price_text_view) TextView priceTextView;

        Advert mAdvert;

        AdvertViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(AdvertViewHolder.this, itemView);
        }

        void bindAdvert(Advert advert) {
            mAdvert = advert;
            nameTextView.setText(mAdvert.getName());
            locationTextView.setText(mAdvert.getLocation());
            String priceString = priceTextView.getResources()
                    .getString(R.string.advert_listing_item_guide_price, mAdvert.getGuidePrice(), mAdvert.getPackagingName());
            priceTextView.setText(priceString);
            bindPhoto(mAdvert.getPhotos());
        }

        void bindPhoto(List<Photo> photos) {
            if (photos.isEmpty()) {
                photoImageView.setImageResource(R.drawable.ic_image_48dp);
            } else {
                Photo photo = photos.get(0);
                loadPhoto(photo);
            }
        }

        void loadPhoto(Photo photo) {
            Glide.with(photoImageView.getContext())
                    .load(photo.getThumbnail())
                    .placeholder(R.color.grey_200)
                    .error(R.color.grey_400)
                    .centerCrop()
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(photoImageView);
        }

        @OnClick(R.id.content)
        void onContentClick() {
            if (mItemClickListener != null) mItemClickListener.onItemClick(mAdvert);
        }
    }
}
