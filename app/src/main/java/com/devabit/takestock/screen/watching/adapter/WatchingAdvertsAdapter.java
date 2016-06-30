package com.devabit.takestock.screen.watching.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
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

    private static OnItemClickListener sItemClickListener;

    public interface OnWatchedChangeListener {
        void onRemoved(Advert advert);
    }

    private static OnWatchedChangeListener sWatchedChangeListener;

    public WatchingAdvertsAdapter(Context context) {
        mLayoutInflater = LayoutInflater.from(context);
        mAdvertsInProcessing = new SparseArray<>();
        mAdverts = new ArrayList<>();
    }

    @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mLayoutInflater.inflate(R.layout.item_watching_advert, parent, false);
        return new ViewHolder(itemView, mAdvertsInProcessing);
    }

    @Override public void onBindViewHolder(ViewHolder holder, int position) {
        Advert advert = mAdverts.get(position);
        holder.bindAdvert(advert);
    }

    @Override public int getItemCount() {
        return mAdverts.size();
    }

    public void addAdverts(List<Advert> adverts) {
        mAdverts.addAll(adverts);
        notifyDataSetChanged();
    }

    public void clearAdverts() {
        mAdverts.clear();
        notifyDataSetChanged();
    }

    public void removeAdvert(Advert advert) {
        mAdverts.remove(advert);
        notifyDataSetChanged();
    }

    public void startAdvertProcessing(Advert advert) {
        mAdvertsInProcessing.append(advert.getId(), advert);
        notifyDataSetChanged();
    }

    public void stopAdvertProcessing(int advertId) {
        mAdvertsInProcessing.remove(advertId);
        notifyDataSetChanged();
    }

    @Nullable public Advert getAdvertInProcessing(int advertId) {
        return mAdvertsInProcessing.get(advertId, null);
    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        sItemClickListener = itemClickListener;
    }

    public void setOnWatchedChangeListener(OnWatchedChangeListener watchedChangeListener) {
        sWatchedChangeListener = watchedChangeListener;
    }

    public void destroy() {
        sItemClickListener = null;
        sWatchedChangeListener = null;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        final Resources resources;
        final SparseArray<Advert> advertInProcessing;

        @BindView(R.id.photo_image_view) ImageView photoImageView;
        @BindView(R.id.watching_button) ImageButton watchingButton;
        @BindView(R.id.date_text_view) TextView dateTextView;
        @BindView(R.id.location_text_view) TextView locationTextView;
        @BindView(R.id.name_text_view) TextView nameTextView;
        @BindView(R.id.guide_price_text_view) TextView guidePriceTextView;
        @BindView(R.id.qty_available_text_view) TextView qtyAvailableTextView;
        @BindView(R.id.additional_text_view) TextView additionalTextView;

        private Advert mAdvert;

        public ViewHolder(View itemView, SparseArray<Advert> advertInProcessing) {
            super(itemView);
            ButterKnife.bind(ViewHolder.this, itemView);
            this.resources = itemView.getResources();
            this.advertInProcessing = advertInProcessing;
        }

        void bindAdvert(Advert advert) {
            mAdvert = advert;
            bindPhoto(getAdvertPhoto(mAdvert));
            String date = DateUtil.formatToDefaultDate(advert.getDateUpdatedAt());
            dateTextView.setText(date);
            locationTextView.setText(mAdvert.getLocation());
            nameTextView.setText(mAdvert.getName());
            guidePriceTextView.setText(resources.getString(R.string.guide_price_per_kg, advert.getGuidePrice()));
            qtyAvailableTextView.setText(resources.getString(R.string.available_kg, advert.getItemsCount()));
            additionalTextView.setText(buildAdditionalInfoString(mAdvert));
            setItemViewActive(!isAdvertProcessing(mAdvert));
        }

        @Nullable Photo getAdvertPhoto(Advert advert) {
            List<Photo> photos = advert.getPhotos();
            if (photos.isEmpty()) return null;
            return photos.get(0);
        }

        void bindPhoto(Photo photo) {
            if (photo == null) {
                photoImageView.setImageResource(R.color.grey_400);
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

        SpannableStringBuilder buildAdditionalInfoString(Advert advert) {
            SpannableStringBuilder stringBuilder = new SpannableStringBuilder();
            String offersCount = advert.getOffersCount();
            stringBuilder.append(offersCount);
            stringBuilder.setSpan(new StyleSpan(Typeface.BOLD), 0, stringBuilder.length(), 0);
            String offers = resources.getString(R.string.offers).toLowerCase();
            stringBuilder.append(" ").append(offers);
            stringBuilder.append("  •  ");
            String questionsCount = "0";
            stringBuilder.append(questionsCount);
            stringBuilder.setSpan(new StyleSpan(Typeface.BOLD), stringBuilder.length() - questionsCount.length(), stringBuilder.length(), 0);
            String questions = resources.getString(R.string.questions).toLowerCase();
            stringBuilder.append(" ").append(questions);
            stringBuilder.append("  •  ");
            String daysLeftCount = advert.getDaysLeft();
            stringBuilder.append(daysLeftCount);
            stringBuilder.setSpan(new StyleSpan(Typeface.BOLD), stringBuilder.length() - daysLeftCount.length(), stringBuilder.length(), 0);
            stringBuilder.append(" ").append("days left");
            return stringBuilder;
        }

        void setItemViewActive(boolean isActive) {
            itemView.setAlpha(isActive ? 1.0f : 0.5f);
            itemView.setEnabled(isActive);
            watchingButton.setEnabled(isActive);
        }

        boolean isAdvertProcessing(Advert advert) {
            return advertInProcessing.get(advert.getId(), null) != null;
        }

        @OnClick(R.id.content) void onContentClick() {
            if (sItemClickListener != null) sItemClickListener.onItemClick(mAdvert);
        }

        @OnClick(R.id.watching_button) void onWatchingButtonClick() {
            if (sWatchedChangeListener != null) sWatchedChangeListener.onRemoved(mAdvert);
        }
    }
}
