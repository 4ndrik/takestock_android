package com.devabit.takestock.screen.buying.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
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
import com.devabit.takestock.data.model.OfferStatus;
import com.devabit.takestock.data.model.Photo;
import com.devabit.takestock.screen.buying.BuyingActivity;
import com.devabit.takestock.utils.DateUtil;
import com.devabit.takestock.utils.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Victor Artemyev on 31/05/2016.
 */
public class BuyingOffersAdapter extends RecyclerView.Adapter<BuyingOffersAdapter.ViewHolder> {

    private static final String TAG = Logger.makeLogTag(BuyingActivity.class);

    private final LayoutInflater mLayoutInflater;
    private final SparseArray<OfferStatus> mOfferStatuses;
    private final List<Offer> mOffers;

    private Map<Offer, Advert> mOfferAdvertMap;

    public interface OnItemClickListener {
        void onItemClick(Advert advert);
        void onAddPaymentClick(Offer offer);
    }

    private static OnItemClickListener sItemClickListener;

    public BuyingOffersAdapter(Context context, SparseArray<OfferStatus> statuses) {
        mLayoutInflater = LayoutInflater.from(context);
        mOfferStatuses = statuses;
        mOffers = new ArrayList<>();
    }

    @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.item_buying_offer, parent, false);
        return new ViewHolder(view);
    }

    @Override public void onBindViewHolder(ViewHolder holder, int position) {
        Offer offer = mOffers.get(position);
        holder.bindOffer(offer);
        Advert advert = mOfferAdvertMap.get(offer);
        holder.bindAdvert(advert);
        OfferStatus status = mOfferStatuses.get(offer.getOfferStatusId());
        holder.bindOfferStatus(status);
    }

    @Override public int getItemCount() {
        return mOffers.size();
    }

    public void addOffers(List<Offer> offers) {
        mOffers.addAll(offers);
        notifyDataSetChanged();
    }

    public void addOffers(Map<Offer, Advert> offerAdvertMap) {
        if (mOfferAdvertMap == null) {
            mOfferAdvertMap = new HashMap<>(offerAdvertMap);
        } else {
            mOfferAdvertMap.putAll(offerAdvertMap);
        }
        mOffers.addAll(offerAdvertMap.keySet());
        notifyDataSetChanged();
    }

    public void clearOffers() {
        mOffers.clear();
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        sItemClickListener = itemClickListener;
    }

    public void destroy() {
        sItemClickListener = null;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.photo_image_view) ImageView imageView;
        @BindView(R.id.name_text_view) TextView advertNameTextView;
        @BindView(R.id.date_text_view) TextView dateTextView;
        @BindView(R.id.offer_price_text_view) TextView priceTextView;
        @BindView(R.id.status_text_view) TextView statusTextView;

        final Resources resources;

        private Advert mAdvert;
        private Offer mOffer;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(ViewHolder.this, itemView);
            Context context = itemView.getContext();
            this.resources = context.getResources();
        }

        void bindOffer(Offer offer) {
            mOffer = offer;
            String date = DateUtil.formatToDefaultDate(mOffer.getDateUpdated());
            dateTextView.setText(date);
            String price = resources.getString(R.string.offer_price_per_kg, offer.getPrice(), offer.getQuantity());
            priceTextView.setText(price);
        }

        void bindOfferStatus(OfferStatus status) {
            statusTextView.setText(status.getType());
        }

        void bindAdvert(Advert advert) {
            mAdvert = advert;
            advertNameTextView.setText(advert.getName());
            List<Photo> photos = advert.getPhotos();
            if (photos.isEmpty()) {
                bindPhoto(null);
            } else {
                bindPhoto(photos.get(0));
            }
        }

        void bindPhoto(Photo photo) {
            if (photo == null) {
                imageView.setImageResource(R.drawable.ic_image_48dp);
            } else {
                Glide.with(imageView.getContext())
                        .load(photo.getImagePath())
                        .placeholder(R.color.grey_400)
                        .error(R.drawable.ic_image_48dp)
                        .centerCrop()
                        .crossFade()
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .into(imageView);
            }
        }

        @OnClick(R.id.add_payment_button) void onAddPaymentButtonClick() {
            if (sItemClickListener != null) sItemClickListener.onAddPaymentClick(mOffer);
        }


        @OnClick(R.id.content) void onContentClick() {
            if (sItemClickListener != null) sItemClickListener.onItemClick(mAdvert);
        }
    }
}
