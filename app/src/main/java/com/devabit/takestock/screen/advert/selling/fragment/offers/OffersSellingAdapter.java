package com.devabit.takestock.screen.advert.selling.fragment.offers;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
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
import com.devabit.takestock.data.model.Offer;
import com.devabit.takestock.data.model.User;
import com.devabit.takestock.utils.DateUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Victor Artemyev on 02/06/2016.
 */
class OffersSellingAdapter extends RecyclerView.Adapter<OffersSellingAdapter.ViewHolder> {

    private final String mPackaging;
    private final LayoutInflater mLayoutInflater;
    private final List<Offer> mOffers;

    interface OnStatusChangedListener {
        void onAccepted(Offer offer);

        void onCountered(Offer offer);

        void onRejected(Offer offer);
    }

    private OnStatusChangedListener mStatusChangedListener;

    interface OnTransportArrangedListener {
        void onSellerArrange(Offer offer);

        void onBuyerArrange(Offer offer);
    }

    private OnTransportArrangedListener mTransportArrangedListener;

    interface OnConfirmStockDispatchedListener {
        void onConfirm(Offer offer);
    }

    private OnConfirmStockDispatchedListener mConfirmStockDispatchedListener;

    interface OnContactListener {
        void onContactBuyer(Offer offer);
        void onContactSupport(Offer offer);
    }

    private OnContactListener mContactListener;

    OffersSellingAdapter(Context context, String packaging) {
        mPackaging = packaging;
        mLayoutInflater = LayoutInflater.from(context);
        mOffers = new ArrayList<>();
    }

    @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflateViewType(viewType, parent);
        switch (viewType) {
            case R.layout.item_offer_selling_pending:
                return new OfferPendingViewHolder(itemView);
            case R.layout.item_offer_selling_address_received:
                return new OfferAddressReceivedViewHolder(itemView);
            case R.layout.item_offer_selling_await_confirm_stock_dispatched:
                return new OfferAwaitStockDispatchedViewHolder(itemView);
            case R.layout.item_offer_selling_stock_in_transit:
                return new OfferStockInTransitViewHolder(itemView);
            case R.layout.item_offer_selling_goods_received:
                return new OfferGoodsReceivedViewHolder(itemView);
            case R.layout.item_offer_selling_paying_by_bacs:
                return new OfferPayingByBACSViewHolder(itemView);
            case R.layout.item_offer_selling_in_dispute:
                return new OfferInDisputeViewHolder(itemView);
            default:
                return new OfferAcceptedViewHolder(itemView);
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
        int status = mOffers.get(position).getStatus();
        switch (status) {
            case Offer.Status.PENDING:
                return R.layout.item_offer_selling_pending;

            case Offer.Status.ADDRESS_RECEIVED:
                return R.layout.item_offer_selling_address_received;

            case Offer.Status.AWAIT_CONFIRM_STOCK_DISPATCHED:
                return R.layout.item_offer_selling_await_confirm_stock_dispatched;

            case Offer.Status.STOCK_IN_TRANSIT:
                return R.layout.item_offer_selling_stock_in_transit;

            case Offer.Status.GOODS_RECEIVED:
                return R.layout.item_offer_selling_goods_received;

            case Offer.Status.PAYING_BY_BACS:
                return R.layout.item_offer_selling_paying_by_bacs;

            case Offer.Status.IN_DISPUTE:
                return R.layout.item_offer_selling_in_dispute;

            default:
                return R.layout.item_offer_selling_accepted;
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

    public void addOffers(List<Offer> offers) {
        int positionStart = mOffers.size();
        mOffers.addAll(offers);
        notifyItemRangeInserted(positionStart, offers.size());
    }

    void setOnStatusChangedListener(OnStatusChangedListener statusChangedListener) {
        mStatusChangedListener = statusChangedListener;
    }

    void setOnTransportArrangedListener(OnTransportArrangedListener transportArrangedListener) {
        mTransportArrangedListener = transportArrangedListener;
    }

    void setOnConfirmStockDispatchedListener(OnConfirmStockDispatchedListener confirmStockDispatchedListener) {
        mConfirmStockDispatchedListener = confirmStockDispatchedListener;
    }

    void setOnContactListener(OnContactListener contactListener) {
        mContactListener = contactListener;
    }

    /********
     * ViewHolders
     *********/

    class OfferInDisputeViewHolder extends ViewHolder {

        OfferInDisputeViewHolder(View itemView) {
            super(itemView);
        }

        @Override void setOfferText() {
            offerTextView.setText(R.string.offer_selling_item_in_dispute_message);
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
    }

    class OfferStockInTransitViewHolder extends ViewHolder {

        OfferStockInTransitViewHolder(View itemView) {
            super(itemView);
        }

        @OnClick(R.id.contact_buyer_button)
        void OnContactBuyerButtonClick() {
            if (mContactListener != null) mContactListener.onContactBuyer(mOffer);
        }

        @OnClick(R.id.contact_support_button)
        void onContactSupportButtonClick() {
            if (mContactListener != null) mContactListener.onContactSupport(mOffer);
        }
    }

    class OfferAwaitStockDispatchedViewHolder extends ViewHolder {

        OfferAwaitStockDispatchedViewHolder(View itemView) {
            super(itemView);
        }

        @OnClick(R.id.confirm_button)
        void onConfirmButtonClick() {
            if (mConfirmStockDispatchedListener != null) mConfirmStockDispatchedListener.onConfirm(mOffer);
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
            builder.append(resources.getString(R.string.offer_selling_item_house));
            String house = shipping.getHouse();
            builder.append(house);
            builder.setSpan(new ForegroundColorSpan(Color.BLACK), builder.length() - house.length(), builder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            builder.append("\n");
            builder.append(resources.getString(R.string.offer_selling_item_street));
            String street = shipping.getStreet();
            builder.append(street);
            builder.setSpan(new ForegroundColorSpan(Color.BLACK), builder.length() - street.length(), builder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            builder.append("\n");
            builder.append(resources.getString(R.string.offer_selling_item_city));
            String city = shipping.getCity();
            builder.append(city);
            builder.setSpan(new ForegroundColorSpan(Color.BLACK), builder.length() - city.length(), builder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            builder.append("\n");
            builder.append(resources.getString(R.string.offer_selling_item_postcode));
            String postcode = String.valueOf(shipping.getPostcode());
            builder.append(postcode);
            builder.setSpan(new ForegroundColorSpan(Color.BLACK), builder.length() - postcode.length(), builder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            builder.append("\n");
            builder.append(resources.getString(R.string.offer_selling_item_phone));
            String phone = shipping.getPhone();
            builder.append(phone);
            builder.setSpan(new ForegroundColorSpan(Color.BLACK), builder.length() - phone.length(), builder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            shippingInfoTextView.setText(builder);
        }

        @OnClick(R.id.seller_transport_button)
        void OnSellerTransportButtonClick() {
            if (mTransportArrangedListener != null) mTransportArrangedListener.onSellerArrange(mOffer);
        }

        @OnClick(R.id.buyer_transport_button)
        void OnBuyerTransportButtonClick() {
            if (mTransportArrangedListener != null) mTransportArrangedListener.onBuyerArrange(mOffer);
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
    }

    class OfferRejectedViewHolder extends ViewHolder {

        OfferRejectedViewHolder(View itemView) {
            super(itemView);
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        final String[] offerStatuses;

        @BindView(R.id.user_image_view) ImageView userImageView;
        @BindView(R.id.name_text_view) TextView nameTextView;
        @BindView(R.id.date_text_view) TextView dateTextView;
        @BindView(R.id.offer_text_view) TextView offerTextView;
        @BindView(R.id.status_text_view) TextView statusTextView;

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
            statusTextView.setText(offerStatuses[mOffer.getStatus() - 1]);
            setOfferText();
        }

        void setOfferText() {
            String offerString = offerTextView.getResources()
                    .getString(R.string.offer_item_offer, mOffer.getQuantity(), mPackaging, mOffer.getPrice(), mPackaging);
            offerTextView.setText(offerString);
        }

        void bindUserImage(String image) {
            Glide.with(userImageView.getContext())
                    .load(image)
                    .error(R.drawable.ic_placeholder_user_96dp)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(userImageView);
        }
    }
}
