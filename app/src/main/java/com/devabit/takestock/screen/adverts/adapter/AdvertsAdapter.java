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

    private static final int TYPE_PROGRESS = 0;
    private static final int TYPE_HORIZONTAL = 1;
    private static final int TYPE_VERTICAL = 2;

    private final LayoutInflater mLayoutInflater;
    private final List<Advert> mAdverts;
    private final SparseArray<Advert> mAdvertsInProcessing;

    public interface OnItemClickListener {
        void onItemClick(Advert advert);
    }

    private OnItemClickListener mItemClickListener;

    public interface OnWatchingChangedListener {
        void onWatchingChanged(Advert advert, boolean isWatched);
    }

    private OnWatchingChangedListener mWatchingChangedListener;

    public AdvertsAdapter(Context context) {
        mLayoutInflater = LayoutInflater.from(context);
        mAdverts = new ArrayList<>();
        mAdvertsInProcessing = new SparseArray<>();
    }

    @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_PROGRESS:
                return new ProgressViewHolder(inflateItemView(R.layout.item_progress, parent));

            case TYPE_VERTICAL:
                return new AdvertViewHolder(inflateItemView(R.layout.item_vertical_advert, parent));

            default:
                return new AdvertViewHolder(inflateItemView(R.layout.item_horizontal_advert, parent));
        }
    }

    private View inflateItemView(@LayoutRes int resId, ViewGroup parent) {
        return mLayoutInflater.inflate(resId, parent, false);
    }

    @Override public void onBindViewHolder(ViewHolder holder, int position) {
        int viewType = holder.getItemViewType();
        if (viewType == TYPE_HORIZONTAL || viewType == TYPE_VERTICAL) {
            ((AdvertViewHolder)holder).bindAdvert(mAdverts.get(position));
        }
    }

    @Override public int getItemCount() {
        return mAdverts.size();
    }

    @Override public int getItemViewType(int position) {
        Advert advert = mAdverts.get(position);
        if (advert == null) return TYPE_PROGRESS;
        List<Photo> photos = advert.getPhotos();
        if (photos.isEmpty()) {
            return TYPE_HORIZONTAL;
        } else {
            return getItemViewTypePerPhoto(photos.get(0));
        }
    }

    private int getItemViewTypePerPhoto(Photo photo) {
        if (photo.getHeight() > photo.getWidth()) return TYPE_VERTICAL;
        else return TYPE_HORIZONTAL;
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

    class AdvertViewHolder extends ViewHolder {

        private final Resources mResources;

        @BindView(R.id.photo_image_view) ImageView photoImageView;
        @BindView(R.id.name_text_view) TextView nameTextView;
        @BindView(R.id.location_text_view) TextView locationTextView;
        @BindView(R.id.date_text_view) TextView dateTextView;
        @BindView(R.id.price_text_view) TextView priceTextView;
        @BindView(R.id.watching_check_box) CheckBox watchingCheckBox;

        private Advert mAdvert;

        AdvertViewHolder(View itemView) {
            super(itemView);
            mResources = itemView.getResources();
            ButterKnife.bind(AdvertViewHolder.this, itemView);
            itemView.setOnClickListener(mClickListener);
            watchingCheckBox.setOnCheckedChangeListener(mCheckedChangeListener);
        }

        final View.OnClickListener mClickListener
                = new View.OnClickListener() {
            @Override public void onClick(View v) {
                if (mItemClickListener != null) mItemClickListener.onItemClick(mAdvert);
            }
        };

        void bindAdvert(Advert advert) {
            mAdvert = advert;
            nameTextView.setText(mAdvert.getName());
            locationTextView.setText(mAdvert.getLocation());
            String priceString = mResources.getString(R.string.guide_price_per_kg, mAdvert.getGuidePrice());
            priceTextView.setText(priceString);
            watchingCheckBox.setOnCheckedChangeListener(null);
//            watchingCheckBox.setChecked(mAdvert.hasSubscriber(sUserId));
            watchingCheckBox.setOnCheckedChangeListener(mCheckedChangeListener);
            dateTextView.setText(DateUtil.formatToDefaultDate(mAdvert.getExpiresAt()));
            bindPhoto(mAdvert.getPhotos());
            setItemViewActive(!isAdvertProcessing(mAdvert));
        }

        final CheckBox.OnCheckedChangeListener mCheckedChangeListener
                = new CompoundButton.OnCheckedChangeListener() {
            @Override public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (mWatchingChangedListener != null) mWatchingChangedListener.onWatchingChanged(mAdvert, isChecked);
            }
        };

        void setItemViewActive(boolean isActive) {
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
                    .error(R.color.grey_400)
                    .centerCrop()
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(photoImageView);
        }
    }
}
