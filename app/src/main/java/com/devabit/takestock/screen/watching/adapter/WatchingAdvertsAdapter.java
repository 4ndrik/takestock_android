package com.devabit.takestock.screen.watching.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
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
import com.devabit.takestock.utils.DateUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Victor Artemyev on 30/06/2016.
 */
public class WatchingAdvertsAdapter extends RecyclerView.Adapter<WatchingAdvertsAdapter.ViewHolder> {

    private final LayoutInflater mLayoutInflater;
    private final SparseArray<Advert> mAdvertsInProcessing;
    private final List<Advert> mAdverts;

    public interface OnItemClickListener {
        void onItemClick(Advert advert);
    }

    private OnItemClickListener mItemClickListener;

    public interface OnWatchedChangeListener {
        void onRemoved(Advert advert);
    }

    private OnWatchedChangeListener mWatchedChangeListener;

    public WatchingAdvertsAdapter(Context context) {
        mLayoutInflater = LayoutInflater.from(context);
        mAdvertsInProcessing = new SparseArray<>();
        mAdverts = new ArrayList<>();
    }

    @Override public ViewHolder onCreateViewHolder(ViewGroup parent, @LayoutRes int viewType) {
        switch (viewType) {
            case R.layout.item_progress:
                return new LoadingViewHolder(inflateItemView(viewType, parent));
            default:
                return new AdvertViewHolder(inflateItemView(viewType, parent));
        }
    }

    private View inflateItemView(@LayoutRes int resId, ViewGroup parent) {
        return mLayoutInflater.inflate(resId, parent, false);
    }

    @Override public void onBindViewHolder(ViewHolder holder, int position) {
        if(holder.getItemViewType() == R.layout.item_progress) return;
        Advert advert = mAdverts.get(position);
        ((AdvertViewHolder) holder).bindAdvert(advert);
    }

    @Override public int getItemCount() {
        return mAdverts.size();
    }

    @Override public @LayoutRes int getItemViewType(int position) {
        if (mAdverts.get(position) == null) return R.layout.item_progress;
        return R.layout.item_advert_watching;
    }

    public void refreshAdverts(List<Advert> adverts) {
        mAdverts.clear();
        mAdverts.addAll(adverts);
        notifyDataSetChanged();
    }

    public void addAdverts(List<Advert> adverts) {
        int positionStart = mAdverts.size();
        mAdverts.addAll(adverts);
        notifyItemRangeInserted(positionStart, adverts.size());
    }

    public void removeAdvert(Advert advert) {
        int position = mAdverts.indexOf(advert);
        mAdverts.remove(advert);
        notifyItemRemoved(position);
    }

    public void startAdvertProcessing(Advert advert) {
        mAdvertsInProcessing.append(advert.getId(), advert);
        notifyDataSetChanged();
    }

    public void stopAdvertProcessing(int advertId) {
        mAdvertsInProcessing.remove(advertId);
        notifyDataSetChanged();
    }

    public void setLoadingProgressEnable(boolean enable) {
        if (enable) {
            mAdverts.add(null);
            notifyItemInserted(mAdverts.size());
        } else {
            mAdverts.remove(mAdverts.size() - 1);
            notifyItemRemoved(mAdverts.size());
        }
    }

    @Nullable public Advert getAdvertInProcessing(int advertId) {
        return mAdvertsInProcessing.get(advertId, null);
    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    public void setOnWatchedChangeListener(OnWatchedChangeListener watchedChangeListener) {
        mWatchedChangeListener = watchedChangeListener;
    }

    class LoadingViewHolder extends ViewHolder {
        LoadingViewHolder(View itemView) {
            super(itemView);
        }
    }

    class AdvertViewHolder extends ViewHolder {

        final Resources resources;

        @BindView(R.id.photo_image_view) ImageView photoImageView;
        @BindView(R.id.watching_button) ImageButton watchingButton;
        @BindView(R.id.date_text_view) TextView dateTextView;
        @BindView(R.id.location_text_view) TextView locationTextView;
        @BindView(R.id.name_text_view) TextView nameTextView;
        @BindView(R.id.guide_price_text_view) TextView guidePriceTextView;
        @BindView(R.id.qty_available_text_view) TextView qtyAvailableTextView;
        @BindView(R.id.additional_text_view) TextView additionalTextView;

        private Advert mAdvert;

        AdvertViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(AdvertViewHolder.this, itemView);
            this.resources = itemView.getResources();
        }

        void bindAdvert(Advert advert) {
            mAdvert = advert;
            bindPhoto(getAdvertPhoto(mAdvert));
            dateTextView.setText(DateUtil.formatToDefaultDate(advert.getUpdatedAt()));
            locationTextView.setText(mAdvert.getLocation());
            nameTextView.setText(mAdvert.getName());
            guidePriceTextView.setText(resources.getString(R.string.advert_watching_item_guide_price, advert.getGuidePrice(), advert.getPackagingName()));
            qtyAvailableTextView.setText(resources.getString(R.string.advert_watching_item_available_unit, advert.getItemsCountNow(), advert.getPackagingName()));
            additionalTextView.setText(resources.getString(R.string.advert_watching_item_additional, advert.getOffersCount(), advert.getQuestionsCount(), advert.getDaysLeft()));
            setItemViewActive(!isAdvertProcessing(mAdvert));
        }

        @Nullable Photo getAdvertPhoto(Advert advert) {
            List<Photo> photos = advert.getPhotos();
            if (photos.isEmpty()) return null;
            return photos.get(0);
        }

        void bindPhoto(Photo photo) {
            if (photo == null) {
                photoImageView.setImageResource(R.drawable.ic_image_48dp);
                return;
            }
            Glide.with(photoImageView.getContext())
                    .load(photo.getImagePath())
                    .placeholder(R.color.grey_400)
                    .error(R.drawable.ic_image_48dp)
                    .centerCrop()
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(photoImageView);
        }

        void setItemViewActive(boolean isActive) {
            itemView.setAlpha(isActive ? 1.0f : 0.5f);
            itemView.setEnabled(isActive);
            watchingButton.setEnabled(isActive);
        }

        boolean isAdvertProcessing(Advert advert) {
            return mAdvertsInProcessing.get(advert.getId(), null) != null;
        }

        @OnClick(R.id.content) void onContentClick() {
            if (mItemClickListener != null) mItemClickListener.onItemClick(mAdvert);
        }

        @OnClick(R.id.watching_button) void onWatchingButtonClick() {
            if (mWatchedChangeListener != null) mWatchedChangeListener.onRemoved(mAdvert);
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
