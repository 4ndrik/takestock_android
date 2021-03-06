package com.devabit.takestock.screen.adverts.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
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
import com.devabit.takestock.data.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Victor Artemyev on 10/05/2016.
 */
public class AdvertsAdapter extends RecyclerView.Adapter<AdvertsAdapter.ViewHolder> {

    private final LayoutInflater mLayoutInflater;
    private final List<Advert> mAdverts;
    private final SparseArray<Advert> mAdvertsInProcessing;
    private final Handler mHandler;
    private User mAccountUser;

    public interface OnItemClickListener {
        void onItemClick(Advert advert);
    }

    private OnItemClickListener mItemClickListener;

    public interface OnWatchingChangedListener {
        void onWatchingChanged(Advert advert, boolean isWatched);
    }

    private OnWatchingChangedListener mWatchingChangedListener;

    public AdvertsAdapter(Context context, @Nullable User accountUser) {
        mLayoutInflater = LayoutInflater.from(context);
        mAdverts = new ArrayList<>();
        mAdvertsInProcessing = new SparseArray<>();
        mHandler = new Handler();
        mAccountUser = accountUser;
    }

    @Override public ViewHolder onCreateViewHolder(ViewGroup parent, @LayoutRes int viewType) {
        View itemView = inflateItemView(viewType, parent);
        switch (viewType) {
            case R.layout.item_progress:
                return new ProgressViewHolder(itemView);

            case R.layout.item_advert_lacks_account_vertical:
            case R.layout.item_advert_lacks_account_horizontal:
                return new AdvertLacksAccountViewHolder(itemView);

            case R.layout.item_advert_account_sold_out_vertical:
            case R.layout.item_advert_account_sold_out_horizontal:
            case R.layout.item_advert_account_vertical:
            case R.layout.item_advert_account_horizontal:
                return new AdvertViewHolder(itemView);

            case R.layout.item_advert_sold_out_vertical:
            case R.layout.item_advert_sold_out_horizontal:
            case R.layout.item_advert_vertical:
            case R.layout.item_advert_horizontal:
            default:
                return new AdvertWatchingViewHolder(itemView);
        }
    }

    private View inflateItemView(@LayoutRes int resId, ViewGroup parent) {
        return mLayoutInflater.inflate(resId, parent, false);
    }

    @Override public void onBindViewHolder(ViewHolder holder, int position) {
        if (holder.getItemViewType() == R.layout.item_progress) return;
        ((AdvertAbstractViewHolder) holder).bindAdvert(mAdverts.get(position));
    }

    @Override public @LayoutRes int getItemViewType(int position) {
        Advert advert = mAdverts.get(position);
        if (advert == null) return R.layout.item_progress;

        if (advert.getPhotos().isEmpty()) return R.layout.item_advert_lacks_account_horizontal;

        Photo photo = advert.getPhotos().get(0);
        boolean isVertical = photo.getHeight() > photo.getWidth();

        if (mAccountUser == null || !mAccountUser.isVerified()) {
            return isVertical ? R.layout.item_advert_lacks_account_vertical : R.layout.item_advert_lacks_account_horizontal;
        }

        boolean isSoldOut = advert.getState() == Advert.State.SOLD_OUT;

        if (advert.getAuthorId() == mAccountUser.getId()) {
            if (isSoldOut) {
                return isVertical ? R.layout.item_advert_account_sold_out_vertical : R.layout.item_advert_account_sold_out_horizontal;
            }
            return isVertical ? R.layout.item_advert_account_vertical : R.layout.item_advert_account_horizontal;
        }

        if (isSoldOut) {
            return isVertical ? R.layout.item_advert_sold_out_vertical : R.layout.item_advert_sold_out_horizontal;
        }

        return isVertical ? R.layout.item_advert_vertical : R.layout.item_advert_horizontal;
    }

    @Override public int getItemCount() {
        return mAdverts.size();
    }

    @Override public void onViewRecycled(ViewHolder holder) {
        if (holder == null || holder.getItemViewType() == R.layout.item_progress) return;
        ((AdvertAbstractViewHolder) holder).unbind();
    }

    public void addAdverts(List<Advert> adverts) {
        int startPosition = mAdverts.size();
        mAdverts.addAll(adverts);
        notifyItemRangeInserted(startPosition, adverts.size());
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
            mHandler.post(new Runnable() {
                @Override public void run() {
                    notifyItemInserted(mAdverts.size());
                }
            });

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

    public void setAccountUser(User accountUser) {
        mAccountUser = accountUser;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mItemClickListener = listener;
    }

    public void setOnWatchingChangedListener(OnWatchingChangedListener watchedChangeListener) {
        mWatchingChangedListener = watchedChangeListener;
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
            watchingCheckBox.setChecked(mAdvert.hasSubscriber(mAccountUser.getId()));
            watchingCheckBox.setOnCheckedChangeListener(mCheckedChangeListener);
            setItemViewActive(!isAdvertProcessing(mAdvert));
        }

        boolean isAdvertProcessing(Advert advert) {
            return mAdvertsInProcessing.get(advert.getId(), null) != null;
        }

        void setItemViewActive(boolean isActive) {
            itemView.setAlpha(isActive ? 1.0f : 0.5f);
            itemView.setEnabled(isActive);
            watchingCheckBox.setEnabled(isActive);
        }
    }

    class AdvertViewHolder extends AdvertAbstractViewHolder {

//        @BindView(R.id.date_text_view) TextView dateTextView;
        @BindView(R.id.price_text_view) TextView priceTextView;

        AdvertViewHolder(View itemView) {
            super(itemView);
        }

        @Override void bindAdvert(Advert advert) {
            super.bindAdvert(advert);
//            dateTextView.setText(DateUtil.formatToDefaultDate(mAdvert.getExpiresAt()));
            String priceString = mResources.getString(R.string.advert_item_guide_price, mAdvert.getGuidePrice(), mAdvert.getPackagingName());
            priceTextView.setText(priceString);
        }
    }

    class AdvertLacksAccountViewHolder extends AdvertAbstractViewHolder {

        AdvertLacksAccountViewHolder(View itemView) {
            super(itemView);
        }
    }

    abstract class AdvertAbstractViewHolder extends ViewHolder {

        final Resources mResources;

        @BindView(R.id.photo_image_view) ImageView photoImageView;
        @BindView(R.id.name_text_view) TextView nameTextView;
        @BindView(R.id.location_text_view) TextView locationTextView;

        Advert mAdvert;

        AdvertAbstractViewHolder(View itemView) {
            super(itemView);
            mResources = itemView.getResources();
            ButterKnife.bind(AdvertAbstractViewHolder.this, itemView);
            itemView.setOnClickListener(mClickListener);
        }

        final View.OnClickListener mClickListener
                = new View.OnClickListener() {
            @Override public void onClick(View v) {
                if (mItemClickListener != null) mItemClickListener.onItemClick(mAdvert);
            }
        };

        void bindAdvert(Advert advert) {
            mAdvert = advert;
            bindPhoto(advert.getPhotos());
            nameTextView.setText(advert.getName());
            locationTextView.setText(advert.getLocation());
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

        void unbind() {
            Glide.clear(photoImageView);
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
