package com.devabit.takestock.screen.advert.buying;

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
import com.devabit.takestock.data.model.User;
import com.devabit.takestock.data.model.Offer;
import com.devabit.takestock.utils.DateUtil;
import com.devabit.takestock.ui.widget.CircleImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Victor Artemyev on 27/09/2016.
 */

class OffersBuyingAdapter extends RecyclerView.Adapter<OffersBuyingAdapter.ViewHolder> {

    private final LayoutInflater mLayoutInflater;
    private final List<Offer> mOffers;

    private String mPackaging;

    interface OnStatusChangedListener {
        void onAccepted(Offer offer);
        void onCountered(Offer offer);
        void onRejected(Offer offer);
    }

    private OnStatusChangedListener mStatusChangedListener;

    interface OnPaymentClickListener {
        void onPayByCard(Offer offer);
        void onPayByBACS(Offer offer);
    }

    private OnPaymentClickListener mPaymentClickListener;

    interface OnShippingAddressClickListener {
        void onShippingAddressSet(Offer offer);
    }

    private OnShippingAddressClickListener mShippingAddressClickListener;

    interface OnConfirmGoodsListener {
        void onConfirm(Offer offer);
    }

    private OnConfirmGoodsListener mConfirmGoodsListener;

    interface OnRaiseDisputeListener {
        void onDispute(Offer offer);
        void onContactSupport(Offer offer);
    }

    private OnRaiseDisputeListener mOnRaiseDisputeListener;

    OffersBuyingAdapter(Context context) {
        mLayoutInflater = LayoutInflater.from(context);
        mOffers = new ArrayList<>();
    }

    @Override public ViewHolder onCreateViewHolder(ViewGroup parent, @LayoutRes int viewType) {
        View itemView = inflateViewType(viewType, parent);
        switch (viewType) {
            case R.layout.item_offer_buying_accepted:
                return new OfferAcceptedViewHolder(itemView);

            case R.layout.item_offer_buying_countered:
                return new OfferCounteredViewHolder(itemView);

            case R.layout.item_offer_buying_rejected:
                return new OfferRejectedViewHolder(itemView);

            case R.layout.item_offer_buying_pending:
                return new OfferPendingViewHolder(itemView);

            case R.layout.item_offer_buying_pending_seller:
                return new OfferPendingSellerViewHolder(itemView);

            case R.layout.item_offer_buying_payment_made:
                return new OfferPaymentMadeViewHolder(itemView);

            case R.layout.item_offer_buying_address_received:
                return new OfferAddressReceivedViewHolder(itemView);

            case R.layout.item_offer_buying_stock_in_transit:
                return new OfferStockInTransitViewHolder(itemView);

            case R.layout.item_offer_buying_goods_received:
                return new OfferGoodsReceivedViewHolder(itemView);

            case R.layout.item_offer_buying_paying_by_bacs:
                return new OfferPayingByBACSViewHolder(itemView);

            case R.layout.item_offer_buying_in_dispute:
                return new OfferInDisputeViewHolder(itemView);

            default:
                return new OfferHistoryViewHolder(itemView);
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

                case Offer.Status.STOCK_IN_TRANSIT:
                    return R.layout.item_offer_buying_stock_in_transit;

                case Offer.Status.GOODS_RECEIVED:
                    return R.layout.item_offer_buying_goods_received;

                case Offer.Status.PAYING_BY_BACS:
                    return R.layout.item_offer_buying_paying_by_bacs;

                case Offer.Status.IN_DISPUTE:
                    return R.layout.item_offer_buying_in_dispute;

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

    public void setPackaging(String packaging) {
        mPackaging = packaging;
    }

    void setOnStatusChangedListener(OnStatusChangedListener statusChangedListener) {
        mStatusChangedListener = statusChangedListener;
    }

    void setOnMakePaymentClickListener(OnPaymentClickListener makePaymentClickListener) {
        mPaymentClickListener = makePaymentClickListener;
    }

    void setOnShippingAddressClickListener(OnShippingAddressClickListener shippingAddressClickListener) {
        mShippingAddressClickListener = shippingAddressClickListener;
    }

    void setOnConfirmGoodsListener(OnConfirmGoodsListener onConfirmGoodsListener) {
        mConfirmGoodsListener = onConfirmGoodsListener;
    }

    public void setOnRaiseDisputeListener(OnRaiseDisputeListener onRaiseDisputeListener) {
        mOnRaiseDisputeListener = onRaiseDisputeListener;
    }

    ///////////////////////////////////////////////////////////////////////////
    // ViewHolders
    ///////////////////////////////////////////////////////////////////////////

    class OfferInDisputeViewHolder extends ViewHolder {

        OfferInDisputeViewHolder(View itemView) {
            super(itemView);
        }
    }

    class OfferPayingByBACSViewHolder extends ViewHolder {

        OfferPayingByBACSViewHolder(View itemView) {
            super(itemView);
        }
    }

    class OfferGoodsReceivedViewHolder extends ViewHolder {

        OfferGoodsReceivedViewHolder(View itemView) {
            super(itemView);
        }

        @OnClick(R.id.raise_dispute_button)
        void onRaiseDisputeButtonClick() {
            if (mOnRaiseDisputeListener != null) mOnRaiseDisputeListener.onDispute(mOffer);
        }

        @OnClick(R.id.contact_support_button)
        void onContactSupportButtonClick() {
            if (mOnRaiseDisputeListener != null) mOnRaiseDisputeListener.onContactSupport(mOffer);
        }
    }

    class OfferStockInTransitViewHolder extends ViewHolder {

        @BindView(R.id.dispatching_info_text_view) TextView dispatchingInfoTextView;

        OfferStockInTransitViewHolder(View itemView) {
            super(itemView);
        }

        @Override void bindOffer(Offer offer) {
            super.bindOffer(offer);
            bindShipping(offer.getShipping()[0]);
        }

        void bindShipping(Offer.Shipping shipping) {
            Resources resources = dispatchingInfoTextView.getResources();
            SpannableStringBuilder builder = new SpannableStringBuilder();
            builder.append(resources.getString(R.string.offer_buying_item_arrival_date));
            String arrivalDate = DateUtil.formatToDispatchingDate(shipping.getArrivalDate());
            builder.append(arrivalDate);
            builder.setSpan(new ForegroundColorSpan(Color.BLACK), builder.length() - arrivalDate.length(), builder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            builder.append("\n");
            builder.append(resources.getString(R.string.offer_buying_item_pick_up_date));
            String pickUpDate = DateUtil.formatToDispatchingDate(shipping.getPickUpDate());
            builder.append(pickUpDate);
            builder.setSpan(new ForegroundColorSpan(Color.BLACK), builder.length() - pickUpDate.length(), builder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            builder.append("\n");
            builder.append(resources.getString(R.string.offer_buying_item_tracking_number));
            String tracking = shipping.getTracking();
            builder.append(tracking);
            builder.setSpan(new ForegroundColorSpan(Color.BLACK), builder.length() - tracking.length(), builder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            builder.append("\n");
            builder.append(resources.getString(R.string.offer_buying_item_courier_name));
            String courierName = shipping.getCourierName();
            builder.append(courierName);
            builder.setSpan(new ForegroundColorSpan(Color.BLACK), builder.length() - courierName.length(), builder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            dispatchingInfoTextView.setText(builder);
        }

        @OnClick(R.id.confirm_goods_button)
        void onConfirmGoodsButtonClick() {
            if (mConfirmGoodsListener != null) mConfirmGoodsListener.onConfirm(mOffer);
        }
    }

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

        @OnClick(R.id.pay_by_card_button)
        void onPayByCardButtonClick() {
            if (mPaymentClickListener != null) mPaymentClickListener.onPayByCard(mOffer);
        }

        @OnClick(R.id.pay_by_bacs_button)
        void omPayByBACSButtonClick() {
            if (mPaymentClickListener != null) mPaymentClickListener.onPayByBACS(mOffer);
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
            User user = mOffer.getUser();
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
