package com.devabit.takestock.screen.search.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.devabit.takestock.R;
import com.devabit.takestock.data.model.Advert;
import com.devabit.takestock.data.model.Photo;
import com.devabit.takestock.utils.DateUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Victor Artemyev on 10/05/2016.
 */
public class SearchAdvertsAdapter extends RecyclerView.Adapter<SearchAdvertsAdapter.ViewHolder> {

    private static final int TYPE_HORIZONTAL = 0;
    private static final int TYPE_VERTICAL = 1;

    private static int sUserId;

    private final LayoutInflater mLayoutInflater;
    private final List<Advert> mAdverts;
    private final SparseArray<Advert> mAdvertsInProcessing;

    public interface OnEndPositionListener {
        void onEndPosition(int position);
    }

    private OnEndPositionListener mEndPositionListener;

    public interface OnItemClickListener {
        void onItemClick(Advert advert);
    }

    private static OnItemClickListener sItemClickListener;

    public interface OnWatchedChangeListener {
        void onWatchedChanged(Advert advert, boolean isWatched);
    }

    private static OnWatchedChangeListener sWatchedChangeListener;

    public SearchAdvertsAdapter(Context context) {
        mLayoutInflater = LayoutInflater.from(context);
        mAdverts = new ArrayList<>();
        mAdvertsInProcessing = new SparseArray<>();
    }

    @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        switch (viewType) {
            case TYPE_VERTICAL:
                itemView = mLayoutInflater.inflate(R.layout.item_vertical_advert, parent, false);
                break;

            default:
                itemView = mLayoutInflater.inflate(R.layout.item_horizontal_advert, parent, false);
                break;
        }

        return new ViewHolder(itemView, mAdvertsInProcessing);
    }

    @Override public void onBindViewHolder(ViewHolder holder, int position) {
        checkEndPosition(position);
        holder.bindAdvert(mAdverts.get(position));
    }

    private void checkEndPosition(int position) {
        if (isEndPosition(position)) {
            if (mEndPositionListener != null) mEndPositionListener.onEndPosition(position);
        }
    }

    private boolean isEndPosition(int position) {
        return position == getItemCount() - 1;
    }

    @Override public int getItemCount() {
        return mAdverts.size();
    }

    @Override public int getItemViewType(int position) {
        Advert advert = mAdverts.get(position);
        List<Photo> photos = advert.getPhotos();
        if (photos.isEmpty()) {
            return TYPE_HORIZONTAL;
        } else {
            Photo photo = photos.get(0);
            if (photo.getHeight() > photo.getWidth()) return TYPE_VERTICAL;
            else return TYPE_HORIZONTAL;
        }
    }

    public void addAdverts(List<Advert> adverts) {
        mAdverts.addAll(adverts);
        notifyDataSetChanged();
    }

    public void clearAdverts() {
        mAdverts.clear();
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

    public List<Advert> getAdverts() {
        return mAdverts;
    }

    public void setOnEndPositionListener(OnEndPositionListener endPositionListener) {
        mEndPositionListener = endPositionListener;
    }

    public void setUserId(int userId) {
        sUserId = userId;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        sItemClickListener = listener;
    }

    public void setOnWatchedChangeListener(OnWatchedChangeListener watchedChangeListener) {
        sWatchedChangeListener = watchedChangeListener;
    }

    public void destroy() {
        sItemClickListener = null;
        sWatchedChangeListener = null;
        sUserId = -1;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final SparseArray<Advert> mAdvertsInProcessing;
        private final Resources mResources;

        @BindView(R.id.photo_image_view) ImageView photoImageView;
        @BindView(R.id.name_text_view) TextView nameTextView;
        @BindView(R.id.location_text_view) TextView locationTextView;
        @BindView(R.id.date_text_view) TextView dateTextView;
        @BindView(R.id.price_text_view) TextView priceTextView;
        @BindView(R.id.watching_check_box) CheckBox watchingCheckBox;

        private Advert mAdvert;

        public ViewHolder(View itemView, SparseArray<Advert> advertsInProcessing) {
            super(itemView);
            mResources = itemView.getResources();
            ButterKnife.bind(ViewHolder.this, itemView);
            itemView.setOnClickListener(mClickListener);
            mAdvertsInProcessing = advertsInProcessing;
            watchingCheckBox.setOnCheckedChangeListener(mCheckedChangeListener);
        }

        private final View.OnClickListener mClickListener
                = new View.OnClickListener() {
            @Override public void onClick(View v) {
                if (sItemClickListener != null) sItemClickListener.onItemClick(mAdvert);
            }
        };

        void bindAdvert(Advert advert) {
            mAdvert = advert;
            nameTextView.setText(mAdvert.getName());
            locationTextView.setText(mAdvert.getLocation());
            String priceString = mResources.getString(R.string.guide_price_per_kg, mAdvert.getGuidePrice());
            priceTextView.setText(priceString);
            watchingCheckBox.setOnCheckedChangeListener(null);
            watchingCheckBox.setChecked(mAdvert.hasSubscriber(sUserId));
            watchingCheckBox.setOnCheckedChangeListener(mCheckedChangeListener);
            dateTextView.setText(DateUtil.formatToDefaultDate(mAdvert.getDateCreatedAt()));
            bindPhoto(mAdvert.getPhotos());
            setItemViewActive(!isAdvertProcessing(mAdvert));
        }

        private final CheckBox.OnCheckedChangeListener mCheckedChangeListener
                = new CompoundButton.OnCheckedChangeListener() {
            @Override public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (sWatchedChangeListener != null) sWatchedChangeListener.onWatchedChanged(mAdvert, isChecked);
            }
        };

        private void setItemViewActive(boolean isActive) {
            itemView.setAlpha(isActive ? 1.0f : 0.5f);
            itemView.setEnabled(isActive);
            watchingCheckBox.setEnabled(isActive);
        }

        boolean isAdvertProcessing(Advert advert) {
            return mAdvertsInProcessing.get(advert.getId(), null) != null;
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
                    .load(photo.getImagePath())
                    .placeholder(R.color.grey_400)
                    .error(R.drawable.ic_image_48dp)
                    .centerCrop()
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(photoImageView);
        }
    }
}
