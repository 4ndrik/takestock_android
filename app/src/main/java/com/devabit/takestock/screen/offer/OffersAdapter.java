package com.devabit.takestock.screen.offer;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
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
import com.devabit.takestock.screen.payment.PaymentActivity;
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

    OffersAdapter(Context context, String packaging) {
        mPackaging = packaging;
        mLayoutInflater = LayoutInflater.from(context);
        mOffers = new ArrayList<>();
    }

    @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
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

    @Override public int getItemViewType(int position) {
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

                default:
                    return R.layout.item_offer_buying_countered;
            }
        } else {
            return R.layout.item_offer_buying_history;
        }
    }

    public void refreshOffers(List<Offer> offers) {
        mOffers.clear();
        mOffers.addAll(offers);
        notifyDataSetChanged();
    }

    public void refreshOffer(Offer offer) {
        int position = mOffers.indexOf(offer);
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
            this.itemView.getContext().startActivity(PaymentActivity.getStartIntent(itemView.getContext(), mOffer));
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
            commentTextView.setText(offer.getComment());
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
