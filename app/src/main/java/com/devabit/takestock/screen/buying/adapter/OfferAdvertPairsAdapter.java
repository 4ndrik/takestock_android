package com.devabit.takestock.screen.buying.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
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
import com.devabit.takestock.data.model.Offer;
import com.devabit.takestock.data.model.Photo;
import com.devabit.takestock.utils.DateUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Victor Artemyev on 31/05/2016.
 */
public class OfferAdvertPairsAdapter extends RecyclerView.Adapter<OfferAdvertPairsAdapter.ViewHolder> {

    private static final int TYPE_LOADING = 0;
    private static final int TYPE_VIEW = 1;

    private final LayoutInflater mLayoutInflater;
    private final List<Pair<Offer, Advert>> mOfferAdvertPairs;

    public interface OnItemClickListener {
        void onItemClick(Pair<Offer, Advert> offerAdvertPair);
    }

    private OnItemClickListener mItemClickListener;

    public OfferAdvertPairsAdapter(Context context) {
        mLayoutInflater = LayoutInflater.from(context);
        mOfferAdvertPairs = new ArrayList<>();
    }

    @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_VIEW:
                return new OfferAdvertViewHolder(inflateItemView(R.layout.item_offer_buying, parent));
            default:
                return new LoadingViewHolder(inflateItemView(R.layout.item_progress, parent));
        }
    }

    private View inflateItemView(@LayoutRes int resId, ViewGroup parent) {
        return mLayoutInflater.inflate(resId, parent, false);
    }

    @Override public void onBindViewHolder(ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case TYPE_LOADING:
                break;

            case TYPE_VIEW:
                ((OfferAdvertViewHolder) holder).bindPair(mOfferAdvertPairs.get(position));
                break;
        }
    }

    @Override public int getItemCount() {
        return mOfferAdvertPairs.size();
    }

    @Override public int getItemViewType(int position) {
        Pair<Offer, Advert> pair = mOfferAdvertPairs.get(position);
        if (pair == null) return TYPE_LOADING;
        return TYPE_VIEW;
    }

    public void refreshPairs(List<Pair<Offer, Advert>> pairs) {
        mOfferAdvertPairs.clear();
        mOfferAdvertPairs.addAll(pairs);
        notifyDataSetChanged();
    }

    public void addPairs(List<Pair<Offer, Advert>> pairs) {
        int startPosition = mOfferAdvertPairs.size();
        mOfferAdvertPairs.addAll(pairs);
        notifyItemRangeInserted(startPosition, pairs.size());
    }

    public void setLoadingProgressEnable(boolean enable) {
        if (enable) {
            mOfferAdvertPairs.add(null);
            notifyItemInserted(mOfferAdvertPairs.size());
        } else {
            mOfferAdvertPairs.remove(mOfferAdvertPairs.size() - 1);
            notifyItemRemoved(mOfferAdvertPairs.size());
        }
    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    class LoadingViewHolder extends ViewHolder {
        LoadingViewHolder(View itemView) {
            super(itemView);
        }
    }

    class OfferAdvertViewHolder extends ViewHolder {

        final Resources resources;
        final String[] offerStatuses;

        @BindView(R.id.photo_image_view) ImageView imageView;
        @BindView(R.id.name_text_view) TextView advertNameTextView;
        @BindView(R.id.date_text_view) TextView offerDateTextView;
        @BindView(R.id.offer_price_text_view) TextView offerPriceTextView;
        @BindView(R.id.status_text_view) TextView statusTextView;

        private Pair<Offer, Advert> mPair;

        OfferAdvertViewHolder(View itemView) {
            super(itemView);
            this.resources = itemView.getResources();
            this.offerStatuses = resources.getStringArray(R.array.offer_statuses);
            ButterKnife.bind(OfferAdvertViewHolder.this, itemView);
        }

        void bindPair(Pair<Offer, Advert> pair) {
            mPair = pair;
            Offer offer = mPair.first;
            offerDateTextView.setText(DateUtil.formatToDefaultDate(offer.getUpdatedAt()));
            statusTextView.setText(offerStatuses[offer.getStatus() - 1]);
            Advert advert = mPair.second;
            offerPriceTextView.setText(resources.getString(
                    R.string.offer_buying_item_price, offer.getPrice(), offer.getQuantity(), advert.getPackagingName()));
            advertNameTextView.setText(advert.getName());
            bindAdvertPhotos(advert.getPhotos());
        }

        void bindAdvertPhotos(List<Photo> photos) {
            if (photos.isEmpty()) {
                imageView.setImageResource(R.drawable.ic_image_48dp);
            } else {
                Glide.with(imageView.getContext())
                        .load(photos.get(0).getImagePath())
                        .placeholder(R.color.grey_200)
                        .error(R.drawable.ic_image_48dp)
                        .centerCrop()
                        .crossFade()
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .into(imageView);
            }
        }

        @OnClick(R.id.content) void onContentClick() {
            if (mItemClickListener != null) mItemClickListener.onItemClick(mPair);
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
