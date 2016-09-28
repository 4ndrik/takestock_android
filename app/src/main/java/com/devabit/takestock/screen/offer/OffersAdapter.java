package com.devabit.takestock.screen.offer;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
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
import com.devabit.takestock.data.model.Author;
import com.devabit.takestock.data.model.Offer;
import com.devabit.takestock.screen.payment.PaymentActivity;
import com.devabit.takestock.utils.DateUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Victor Artemyev on 27/09/2016.
 */

class OffersAdapter extends RecyclerView.Adapter<OffersAdapter.ViewHolder> {

    private static final int TYPE_PENDING = 1;
    private static final int TYPE_ACCEPTED = 2;
    private static final int TYPE_REJECTED = 3;

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
            case TYPE_PENDING:
                return new OfferPendingViewHolder(inflateViewType(R.layout.item_offer_pending, parent));
            case TYPE_REJECTED:
                return new OfferRejectedViewHolder(inflateViewType(R.layout.item_offer_accepted, parent));
            default:
                return new OfferPaymentAcceptedViewHolder(inflateViewType(R.layout.item_offer_payment_accepted, parent));
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
        int status = mOffers.get(position).getStatus();
        switch (status) {
            case Offer.Status.ACCEPTED:
                return TYPE_ACCEPTED;

            case Offer.Status.PENDING:
                return TYPE_PENDING;

            default:
                return TYPE_REJECTED;
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

    public void addOffers(List<Offer> offers) {
        int positionStart = mOffers.size();
        mOffers.addAll(offers);
        notifyItemRangeInserted(positionStart, offers.size());
    }

    void setOnStatusChangedListener(OnStatusChangedListener statusChangedListener) {
        mStatusChangedListener = statusChangedListener;
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

    class OfferPaymentAcceptedViewHolder extends ViewHolder {

        OfferPaymentAcceptedViewHolder(View itemView) {
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
            Author user = mOffer.getAuthor();
            bindUserImage(user.getPhoto());
            nameTextView.setText(user.getUserName());
            dateTextView.setText(DateUtil.formatToDefaultDate(mOffer.getCreatedAt()));
            String offerString = offerTextView.getResources()
                    .getString(R.string.offer_item_offer, mOffer.getQuantity(), mPackaging, mOffer.getPrice(), mPackaging);
            offerTextView.setText(offerString);
            statusTextView.setText(offerStatuses[mOffer.getStatus() - 1]);
            this.itemView.setAlpha(getAdapterPosition() > 0 ? 0.5f : 1f);
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
