package com.devabit.takestock.screen.adverts.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.LayoutRes;
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
public class AdvertsAdapter extends RecyclerView.Adapter<AdvertsAdapter.ViewHolder> {

    private final int mUserId;
    private final LayoutInflater mLayoutInflater;
    private final List<Advert> mAdverts;
    private final SparseArray<Advert> mAdvertsInProcessing;

    public interface OnItemClickListener {
        void onItemClick(Advert advert, boolean isAccount);
    }

    private OnItemClickListener mItemClickListener;

    public interface OnWatchingChangedListener {
        void onWatchingChanged(Advert advert, boolean isWatched);
    }

    private OnWatchingChangedListener mWatchingChangedListener;

    public AdvertsAdapter(Context context, int userId) {
        mLayoutInflater = LayoutInflater.from(context);
        mAdverts = new ArrayList<>();
        mAdvertsInProcessing = new SparseArray<>();
        mUserId = userId;
    }

    @Override public ViewHolder onCreateViewHolder(ViewGroup parent, @LayoutRes int viewType) {
        switch (viewType) {
            case R.layout.item_progress:
                return new ProgressViewHolder(inflateItemView(viewType, parent));

            case R.layout.item_advert_account_vertical:
            case R.layout.item_advert_account_horizontal:
            case R.layout.item_advert_offered_vertical:
            case R.layout.item_advert_offered_horizontal:
                return new AdvertViewHolder(inflateItemView(viewType, parent));

            case R.layout.item_advert_vertical:
            case R.layout.item_advert_horizontal:
            default:
                return new AdvertWatchingViewHolder(inflateItemView(viewType, parent));
        }
    }

    private View inflateItemView(@LayoutRes int resId, ViewGroup parent) {
        return mLayoutInflater.inflate(resId, parent, false);
    }

    @Override public void onBindViewHolder(ViewHolder holder, int position) {
        if (holder.getItemViewType() == R.layout.item_progress) return;
        ((AdvertViewHolder) holder).bindAdvert(mAdverts.get(position));
    }

    @Override public @LayoutRes int getItemViewType(int position) {
        Advert advert = mAdverts.get(position);
        if (advert == null) return R.layout.item_progress;

        List<Photo> photos = advert.getPhotos();
        if (photos.isEmpty()) return R.layout.item_advert_horizontal;

        Photo photo = photos.get(0);
        boolean isVertical = photo.getHeight() > photo.getWidth();
        if (advert.getAuthorId() == mUserId) {
            return isVertical ? R.layout.item_advert_account_vertical : R.layout.item_advert_account_horizontal;
        }

        if (advert.canOffer()) {
            return isVertical ? R.layout.item_advert_vertical : R.layout.item_advert_horizontal;
        }

        return isVertical ? R.layout.item_advert_offered_vertical : R.layout.item_advert_offered_horizontal;
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

    public List<Advert> getAdverts() {
        return mAdverts;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mItemClickListener = listener;
    }

    public void setOnWatchingChangedListener(OnWatchingChangedListener watchedChangeListener) {
        mWatchingChangedListener = watchedChangeListener;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    class ProgressViewHolder extends ViewHolder {
        ProgressViewHolder(View itemView) {
            super(itemView);
        }
    }

    class AdvertWatchingViewHolder extends AdvertViewHolder {

        @BindView(R.id.watching_check_box) CheckBox watchingCheckBox;

        AdvertWatchingViewHolder(View itemView) {
            super(itemView);
            watchingCheckBox.setOnCheckedChangeListener(mCheckedChangeListener);
        }

        final CheckBox.OnCheckedChangeListener mCheckedChangeListener
                = new CompoundButton.OnCheckedChangeListener() {
            @Override public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (mWatchingChangedListener != null) mWatchingChangedListener.onWatchingChanged(mAdvert, isChecked);
            }
        };

        @Override void bindAdvert(Advert advert) {
            super.bindAdvert(advert);
            watchingCheckBox.setOnCheckedChangeListener(null);
            watchingCheckBox.setChecked(mAdvert.hasSubscriber(mUserId));
            watchingCheckBox.setOnCheckedChangeListener(mCheckedChangeListener);
            setItemViewActive(!isAdvertProcessing(mAdvert));
        }

        void setItemViewActive(boolean isActive) {
            itemView.setAlpha(isActive ? 1.0f : 0.5f);
            itemView.setEnabled(isActive);
            watchingCheckBox.setEnabled(isActive);
        }
    }

    class AdvertViewHolder extends ViewHolder {

        private final Resources mResources;

        @BindView(R.id.photo_image_view) ImageView photoImageView;
        @BindView(R.id.name_text_view) TextView nameTextView;
        @BindView(R.id.location_text_view) TextView locationTextView;
        @BindView(R.id.date_text_view) TextView dateTextView;
        @BindView(R.id.price_text_view) TextView priceTextView;

        Advert mAdvert;

        AdvertViewHolder(View itemView) {
            super(itemView);
            mResources = itemView.getResources();
            ButterKnife.bind(AdvertViewHolder.this, itemView);
            itemView.setOnClickListener(mClickListener);
        }

        final View.OnClickListener mClickListener
                = new View.OnClickListener() {
            @Override public void onClick(View v) {
                if (mItemClickListener != null) mItemClickListener.onItemClick(mAdvert, mAdvert.getAuthorId() == mUserId);
            }
        };

        void bindAdvert(Advert advert) {
            mAdvert = advert;
            nameTextView.setText(mAdvert.getName());
            locationTextView.setText(mAdvert.getLocation());
            String priceString = mResources.getString(R.string.guide_price_per_kg, mAdvert.getGuidePrice());
            priceTextView.setText(priceString);
            dateTextView.setText(DateUtil.formatToDefaultDate(mAdvert.getExpiresAt()));
            bindPhoto(mAdvert.getPhotos());
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
                    .error(R.color.grey_400)
                    .centerCrop()
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(photoImageView);
        }
    }
}
