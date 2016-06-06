package com.devabit.takestock.screens.buying.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.*;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.devabit.takestock.R;
import com.devabit.takestock.data.models.Advert;
import com.devabit.takestock.data.models.Offer;
import com.devabit.takestock.data.models.OfferStatus;
import com.devabit.takestock.data.models.Photo;
import com.devabit.takestock.screens.buying.BuyingActivity;
import com.devabit.takestock.util.DateFormats;
import com.devabit.takestock.util.Logger;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.util.*;

import static butterknife.ButterKnife.findById;
import static com.devabit.takestock.util.Logger.LOGE;

/**
 * Created by Victor Artemyev on 31/05/2016.
 */
public class OfferAdvertPairsAdapter extends RecyclerView.Adapter<OfferAdvertPairsAdapter.ViewHolder> {

    private static final String TAG = Logger.makeLogTag(BuyingActivity.class);

    private final LayoutInflater mLayoutInflater;
    private final SparseArray<OfferStatus> mOfferStatuses;
    private final List<Offer> mOffers;

    private Map<Offer, Advert> mOfferAdvertMap;

    public interface OnMenuItemClickListener {
        void viewAdvert(Advert advert);
    }

    private static OnMenuItemClickListener sMenuItemClickListener;

    public OfferAdvertPairsAdapter(Context context, SparseArray<OfferStatus> statuses) {
        mLayoutInflater = LayoutInflater.from(context);
        mOfferStatuses = statuses;
        mOffers = new ArrayList<>();
    }

    @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.item_offer_advert_pair, parent, false);
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

    public void setMenuItemClickListener(OnMenuItemClickListener menuItemClickListener) {
        sMenuItemClickListener = menuItemClickListener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        final Resources resources;
        final Picasso picasso;

        final ImageView imageView;
        final TextView advertNameTextView;
        final TextView dateUpdatedTextView;
        final TextView priceTextView;
        final TextView statusTextView;

        private Advert mAdvert;

        public ViewHolder(View itemView) {
            super(itemView);
            Context context = itemView.getContext();
            resources = context.getResources();
            picasso = Picasso.with(context);

            imageView = findById(itemView, R.id.photo_image_view);
            advertNameTextView = findById(itemView, R.id.title_text_view);
            dateUpdatedTextView = findById(itemView, R.id.date_updated_text_view);
            priceTextView = findById(itemView, R.id.offer_price_text_view);
            statusTextView = findById(itemView, R.id.status_text_view);

            ImageButton menuButton = findById(itemView, R.id.menu_button);
            final PopupMenu popupMenu = new PopupMenu(context, menuButton);
            MenuInflater menuInflater = popupMenu.getMenuInflater();
            menuInflater.inflate(R.menu.offer_item_menu, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.action_view_advert:
                            onViewAdvertAction();
                            return true;

                        default:
                            return false;
                    }
                }
            });
            menuButton.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    popupMenu.show();
                }
            });
        }

        private void onViewAdvertAction() {
            if (sMenuItemClickListener != null) sMenuItemClickListener.viewAdvert(mAdvert);
        }

        void bindOffer(Offer offer) {
            try {
                Date date = DateFormats.API_FORMAT.parse(offer.getDateUpdated());
                String dateAsString = DateFormats.DEFAULT_FORMAT.format(date);
                dateUpdatedTextView.setText(dateAsString);
            } catch (ParseException e) {
                LOGE(TAG, "BOOM:", e);
            }
            priceTextView.setText(resources.getString(
                    R.string.offer_price_per_kg, offer.getPrice(), offer.getQuantity()));
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
                picasso.load(photo.getImagePath())
                        .placeholder(R.drawable.ic_image_48dp)
                        .error(R.drawable.ic_image_48dp)
                        .centerCrop()
                        .fit()
                        .into(imageView);
            }
        }
    }

    public void destroy() {
        sMenuItemClickListener = null;
    }
}
