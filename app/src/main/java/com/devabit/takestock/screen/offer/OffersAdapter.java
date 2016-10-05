package com.devabit.takestock.screen.offer;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.annotation.LayoutRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.devabit.takestock.R;
import com.devabit.takestock.data.model.Author;
import com.devabit.takestock.data.model.Offer;
import com.devabit.takestock.utils.DateUtil;
import com.devabit.takestock.widget.CircleImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Victor Artemyev on 27/09/2016.
 */

class OffersAdapter extends RecyclerView.Adapter<OffersAdapter.ViewHolder> {

    private final String mPackaging;
    private final LayoutInflater mLayoutInflater;
    private final List<Offer> mOffers;

    interface OnStatusChangedListener {
        void onAccepted(Offer offer);

        void onCountered(Offer offer);

        void onRejected(Offer offer);
    }

    private OnStatusChangedListener mStatusChangedListener;

    interface OnMakePaymentClickListener {
        void onMakePayment(Offer offer);
    }

    private OnMakePaymentClickListener mMakePaymentClickListener;

    interface OnShippingAddressClickListener {
        void onShippingAddressSet(Offer offer);
    }

    private OnShippingAddressClickListener mShippingAddressClickListener;

    OffersAdapter(Context context, String packaging) {
        mPackaging = packaging;
        mLayoutInflater = LayoutInflater.from(context);
        mOffers = new ArrayList<>();
    }

    @Override public ViewHolder onCreateViewHolder(ViewGroup parent, @LayoutRes int viewType) {
        switch (viewType) {
            case R.layout.item_offer_buying_accepted:
                return new OfferAcceptedViewHolder(inflateViewType(viewType, parent));

            case R.layout.item_offer_buying_countered:
                return new OfferCounteredViewHolder(inflateViewType(viewType, parent));

            case R.layout.item_offer_buying_rejected:
                return new OfferRejectedViewHolder(inflateViewType(viewType, parent));

            case R.layout.item_offer_buying_pending:
                return new OfferPendingViewHolder(inflateViewType(viewType, parent));

            case R.layout.item_offer_buying_pending_seller:
                return new OfferPendingSellerViewHolder(inflateViewType(viewType, parent));

            case R.layout.item_offer_buying_payment_made:
                return new OfferPaymentMadeViewHolder(inflateViewType(viewType, parent));

            case R.layout.item_offer_buying_address_received:
                return new OfferAddressReceivedViewHolder(inflateViewType(viewType, parent));

            default:
                return new OfferHistoryViewHolder(inflateViewType(viewType, parent));
        }
    }

    private View inflateViewType(@LayoutRes int resId, ViewGroup parent) {
        return mLayoutInflater.inflate(resId, parent, false);
    }

    @Override public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bindOffer(mOffers.get(position));
    }

    @Override public int getItemCount() {
        return mOffers.size();
    }

    @Override public @LayoutRes int getItemViewType(int position) {
        if (position == 0) {
            Offer offer = mOffers.get(position);
            int status = offer.getStatus();
            switch (status) {
                case Offer.Status.ACCEPTED:
                    return R.layout.item_offer_buying_accepted;

                case Offer.Status.PENDING:
                    return offer.isFromSeller() ? R.layout.item_offer_buying_pending : R.layout.item_offer_buying_pending_seller;

                case Offer.Status.REJECTED:
                    return R.layout.item_offer_buying_rejected;

                case Offer.Status.PAYMENT_MADE:
                    return R.layout.item_offer_buying_payment_made;

                case Offer.Status.ADDRESS_RECEIVED:
                    return R.layout.item_offer_buying_address_received;

                default:
                    return R.layout.item_offer_buying_countered;
            }
        } else {
            return R.layout.item_offer_buying_history;
        }
    }

    void refreshOffers(List<Offer> offers) {
        mOffers.clear();
        mOffers.addAll(offers);
        notifyDataSetChanged();
    }

    void refreshOffer(Offer offer) {
        int position = mOffers.indexOf(offer);
        mOffers.set(position, offer);
        notifyItemChanged(position);
    }

    void addOffers(List<Offer> offers) {
        int positionStart = mOffers.size();
        mOffers.addAll(offers);
        notifyItemRangeInserted(positionStart, offers.size());
    }

    void addOffer(Offer offer) {
        mOffers.add(0, offer);
        notifyItemInserted(0);
    }

    void setOnStatusChangedListener(OnStatusChangedListener statusChangedListener) {
        mStatusChangedListener = statusChangedListener;
    }

    void setOnMakePaymentClickListener(OnMakePaymentClickListener makePaymentClickListener) {
        mMakePaymentClickListener = makePaymentClickListener;
    }

    void setOnShippingAddressClickListener(OnShippingAddressClickListener shippingAddressClickListener) {
        mShippingAddressClickListener = shippingAddressClickListener;
    }

    ///////////////////////////////////////////////////////////////////////////
    // ViewHolders
    ///////////////////////////////////////////////////////////////////////////

    class OfferAddressReceivedViewHolder extends ViewHolder {

        @BindView(R.id.shipping_info_text_view) TextView shippingInfoTextView;

        OfferAddressReceivedViewHolder(View itemView) {
            super(itemView);
        }

        @Override void bindOffer(Offer offer) {
            super.bindOffer(offer);
            bindShipping(offer.getShipping()[0]);
        }

        void bindShipping(Offer.Shipping shipping) {
            Resources resources = shippingInfoTextView.getResources();
            SpannableStringBuilder builder = new SpannableStringBuilder();
//            builder.append(resources.getString(R.string.offer_buying_item_shipping_info));
//            builder.append("\n");
            builder.append(resources.getString(R.string.offer_buying_item_house));
            String house = shipping.getHouse();
            builder.append(house);
            builder.setSpan(new ForegroundColorSpan(Color.BLACK), builder.length() - house.length(), builder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            builder.append("\n");
            builder.append(resources.getString(R.string.offer_buying_item_street));
            String street = shipping.getStreet();
            builder.append(street);
            builder.setSpan(new ForegroundColorSpan(Color.BLACK), builder.length() - street.length(), builder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            builder.append("\n");
            builder.append(resources.getString(R.string.offer_buying_item_city));
            String city = shipping.getCity();
            builder.append(city);
            builder.setSpan(new ForegroundColorSpan(Color.BLACK), builder.length() - city.length(), builder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            builder.append("\n");
            builder.append(resources.getString(R.string.offer_buying_item_postcode));
            String postcode = String.valueOf(shipping.getPostcode());
            builder.append(postcode);
            builder.setSpan(new ForegroundColorSpan(Color.BLACK), builder.length() - postcode.length(), builder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            builder.append("\n");
            builder.append(resources.getString(R.string.offer_buying_item_phone));
            String phone = shipping.getPhone();
            builder.append(phone);
            builder.setSpan(new ForegroundColorSpan(Color.BLACK), builder.length() - phone.length(), builder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            shippingInfoTextView.setText(builder);
        }

        @OnClick(R.id.seller_transport_button)
        void onSellerTransportButtonClick() {

        }

        @OnClick(R.id.buyer_transport_button)
        void onBuyerTransportButtonClick() {

        }

    }

    class OfferPaymentMadeViewHolder extends ViewHolder {

        OfferPaymentMadeViewHolder(View itemView) {
            super(itemView);
        }

        @OnClick(R.id.shipping_address_button)
        void onShippingAddress() {
            if (mShippingAddressClickListener != null) mShippingAddressClickListener.onShippingAddressSet(mOffer);
        }
    }

    class OfferPendingSellerViewHolder extends ViewHolder {

        OfferPendingSellerViewHolder(View itemView) {
            super(itemView);
        }
    }

    class OfferPendingViewHolder extends ViewHolder {

        OfferPendingViewHolder(View itemView) {
            super(itemView);
        }

        @OnClick(R.id.accept_button)
        void onAcceptButtonClick() {
            if (mStatusChangedListener != null) mStatusChangedListener.onAccepted(mOffer);
        }

        @OnClick(R.id.counter_button)
        void onCounterButtonClick() {
            if (mStatusChangedListener != null) mStatusChangedListener.onCountered(mOffer);
        }

        @OnClick(R.id.reject_button)
        void onRejectButtonClick() {
            if (mStatusChangedListener != null) mStatusChangedListener.onRejected(mOffer);
        }
    }

    class OfferAcceptedViewHolder extends ViewHolder {

        OfferAcceptedViewHolder(View itemView) {
            super(itemView);
        }

        @OnClick(R.id.payment_button)
        void onPaymentButtonClick() {
            if (mMakePaymentClickListener != null) mMakePaymentClickListener.onMakePayment(mOffer);
        }
    }

    class OfferRejectedViewHolder extends ViewHolder {

        OfferRejectedViewHolder(View itemView) {
            super(itemView);
        }
    }

    class OfferCounteredViewHolder extends ViewHolder {

        OfferCounteredViewHolder(View itemView) {
            super(itemView);
        }
    }

    class OfferHistoryViewHolder extends ViewHolder {

        OfferHistoryViewHolder(View itemView) {
            super(itemView);
        }

        @Override void setStatus() {
            statusTextView.setTextColor(mOffer.isFromSeller()
                    ? ContextCompat.getColor(statusTextView.getContext(), R.color.jam)
                    : ContextCompat.getColor(statusTextView.getContext(), R.color.gold));
            statusTextView.setText(mOffer.isFromSeller()
                    ? itemView.getResources().getString(R.string.offer_buying_seller_offer)
                    : itemView.getResources().getString(R.string.offer_buying_buyer_offer));
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        final String[] offerStatuses;

        @BindView(R.id.user_image_view) CircleImageView userImageView;
        @BindView(R.id.name_text_view) TextView nameTextView;
        @BindView(R.id.date_text_view) TextView dateTextView;
        @BindView(R.id.offer_text_view) TextView offerTextView;
        @BindView(R.id.status_text_view) TextView statusTextView;
        @BindView(R.id.comment_text_view) TextView commentTextView;

        Offer mOffer;

        ViewHolder(View itemView) {
            super(itemView);
            offerStatuses = itemView.getResources().getStringArray(R.array.offer_statuses);
            ButterKnife.bind(ViewHolder.this, itemView);
        }

        void bindOffer(Offer offer) {
            mOffer = offer;
            Author user = mOffer.getAuthor();
            bindUserImage(user.getPhoto());
            nameTextView.setText(user.getUserName());
            dateTextView.setText(DateUtil.formatToDefaultDate(mOffer.getCreatedAt()));
            String offerString = offerTextView.getResources()
                    .getString(R.string.offer_item_offer, mOffer.getQuantity(), mPackaging, mOffer.getPrice(), mPackaging);
            offerTextView.setText(offerString);

            if (TextUtils.isEmpty(offer.getComment())) {
                commentTextView.setVisibility(View.GONE);
            } else {
                commentTextView.setVisibility(View.VISIBLE);
                commentTextView.setText(offer.getComment());
            }

            setStatus();
        }

        void setStatus() {
            statusTextView.setText(offerStatuses[mOffer.getStatusForBuyer() - 1]);
        }

        void bindUserImage(String image) {
            userImageView.setBorderColorResource(mOffer.isFromSeller() ? R.color.jam : R.color.gold);
            Glide.with(userImageView.getContext())
                    .load(image)
                    .error(R.drawable.ic_placeholder_user_96dp)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(userImageView);
        }
    }
}
