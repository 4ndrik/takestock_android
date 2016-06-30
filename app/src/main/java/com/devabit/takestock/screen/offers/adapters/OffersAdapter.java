package com.devabit.takestock.screen.offers.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.devabit.takestock.R;
import com.devabit.takestock.data.model.Offer;
import com.devabit.takestock.data.model.OfferStatus;
import com.devabit.takestock.data.model.User;

import java.util.ArrayList;
import java.util.List;

import static butterknife.ButterKnife.findById;

/**
 * Created by Victor Artemyev on 02/06/2016.
 */
public class OffersAdapter extends RecyclerView.Adapter<OffersAdapter.ViewHolder> {

    private LayoutInflater mLayoutInflater;
    private final List<Pair<Offer, Offer>> mOfferCounterOfferList;

    public interface OnStatusButtonClickListener {
        void onAccepted(Offer offer);

        void onCountered(Offer offer);

        void onRejected(Offer offer);
    }

    private static OnStatusButtonClickListener sStatusButtonClickListener;

    public OffersAdapter(Context context) {
        mLayoutInflater = LayoutInflater.from(context);
        mOfferCounterOfferList = new ArrayList<>();
    }

    @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.item_offer, parent, false);
        return new ViewHolder(view);
    }

    @Override public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bindOfferCounterOfferPair(mOfferCounterOfferList.get(position));
    }

    @Override public int getItemCount() {
        return mOfferCounterOfferList.size();
    }

    public void addOfferCounterOfferPairList(List<Pair<Offer, Offer>> pairs) {
        mOfferCounterOfferList.addAll(pairs);
        notifyDataSetChanged();
    }

    public void clear() {
        mOfferCounterOfferList.clear();
        notifyDataSetChanged();
    }

    public void setStatusButtonClickListener(OnStatusButtonClickListener statusButtonClickListener) {
        sStatusButtonClickListener = statusButtonClickListener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final Context context;
        final Resources resources;

        final TextView userNameTextView;
        final TextView offerPriceTextView;
        final LinearLayout counterOfferContent;
        final TextView counterOfferPriceTextView;
        final TextView counterOfferCommentTextView;
        final TextView offerStatusTextView;
        final TextView offerCommentTextView;
        final LinearLayout statusButtonsContent;

        Offer offer;

        public ViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            resources = context.getResources();

            userNameTextView = findById(itemView, R.id.user_name_text_view);
            offerPriceTextView = findById(itemView, R.id.offer_price_text_view);
            counterOfferContent = findById(itemView, R.id.counter_offer_content);
            counterOfferPriceTextView = findById(itemView, R.id.counter_offer_price_text_view);
            counterOfferCommentTextView = findById(itemView, R.id.counter_offer_comment_text_view);
            offerStatusTextView = findById(itemView, R.id.status_text_view);
            offerCommentTextView = findById(itemView, R.id.offer_comment_text_view);

            statusButtonsContent = findById(itemView, R.id.offer_status_buttons_content);

            Button accept = findById(itemView, R.id.accept_button);
            accept.setOnClickListener(ViewHolder.this);

            Button counter = findById(itemView, R.id.counter_button);
            counter.setOnClickListener(ViewHolder.this);

            Button reject = findById(itemView, R.id.reject_button);
            reject.setOnClickListener(ViewHolder.this);

        }

        void bindOfferCounterOfferPair(Pair<Offer, Offer> pair) {
            offer = pair.first;
            User user = offer.getUser();
            userNameTextView.setText(user.getUserName());
            offerPriceTextView.setText(resources.getString(R.string.offer_price_per_kg, offer.getPrice(), offer.getQuantity()));
            int status = offer.getOfferStatusId();
            if (status == OfferStatus.PENDING) {
                setCounterOfferContentVisibility(false);
                setCounterOfferCommentVisibility(false);
                setOfferStatusVisibility(false);
                setOfferCommentVisibility(false);

                setStatusButtonsContentVisibility(true);
                return;
            } else {
                setStatusButtonsContentVisibility(false);
                setOfferStatusVisibility(true);
                setOfferStatusPerStatusId(offer.getOfferStatusId());
            }

            Offer counterOffer = pair.second;
            if (counterOffer == null) {
                setCounterOfferContentVisibility(false);
                setCounterOfferCommentVisibility(false);
            } else {
                setCounterOfferContentVisibility(true);
                setCounterOfferPrice(counterOffer);
                setCounterOfferCommentVisibility(true);
                setCounterOfferComment(counterOffer.getComment());
            }
        }

        void setCounterOfferContentVisibility(boolean visible) {
            counterOfferContent.setVisibility(visible ? View.VISIBLE : View.GONE);
        }

        void setCounterOfferPrice(Offer offer) {
            counterOfferPriceTextView.setText(resources.getString(R.string.offer_price_per_kg, offer.getPrice(), offer.getQuantity()));
        }

        void setCounterOfferCommentVisibility(boolean visible) {
            counterOfferCommentTextView.setVisibility(visible ? View.VISIBLE : View.GONE);
        }

        void setCounterOfferComment(String comment) {
            counterOfferCommentTextView.setText(comment);
        }

        void setOfferStatusVisibility(boolean visible) {
            offerStatusTextView.setVisibility(visible ? View.VISIBLE : View.GONE);
        }

        void setOfferStatusPerStatusId(int offerStatusId) {
            switch (offerStatusId) {
                case OfferStatus.ACCEPTED:
                    offerStatusTextView.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
                    offerStatusTextView.setText("Accepted");
                    break;

                case OfferStatus.COUNTERED:
                    offerStatusTextView.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
                    offerStatusTextView.setText("Countered");
                    break;

                case OfferStatus.REJECTED:
                    offerStatusTextView.setTextColor(ContextCompat.getColor(context, R.color.red_500));
                    offerStatusTextView.setText("Rejected");
                    break;
            }
        }

        void setOfferCommentVisibility(boolean visible) {
            offerCommentTextView.setVisibility(visible ? View.VISIBLE : View.GONE);
        }

        void setOfferCommentPerStatus(String comment) {
            offerCommentTextView.setText(comment);
        }

        void setStatusButtonsContentVisibility(boolean visible) {
            statusButtonsContent.setVisibility(visible ? View.VISIBLE : View.GONE);
        }

        @Override public void onClick(View v) {
            if (sStatusButtonClickListener == null) return;
            switch (v.getId()) {
                case R.id.accept_button:
                    sStatusButtonClickListener.onAccepted(offer);
                    break;

                case R.id.counter_button:
                    sStatusButtonClickListener.onCountered(offer);
                    break;

                case R.id.reject_button:
                    sStatusButtonClickListener.onRejected(offer);
                    break;
            }
        }
    }

    public void destroy() {
        sStatusButtonClickListener = null;
    }
}
