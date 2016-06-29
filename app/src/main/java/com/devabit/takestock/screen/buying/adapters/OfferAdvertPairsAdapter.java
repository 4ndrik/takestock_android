package com.devabit.takestock.screen.buying.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.*;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.devabit.takestock.R;
import com.devabit.takestock.data.models.Advert;
import com.devabit.takestock.data.models.Offer;
import com.devabit.takestock.data.models.OfferStatus;
import com.devabit.takestock.data.models.Photo;
import com.devabit.takestock.screen.buying.BuyingActivity;
import com.devabit.takestock.utils.DateUtil;
import com.devabit.takestock.utils.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static butterknife.ButterKnife.findById;

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

        @BindView(R.id.photo_image_view) ImageView imageView;
        @BindView(R.id.title_text_view) TextView advertNameTextView;
        @BindView(R.id.date_updated_text_view) TextView dateUpdatedTextView;
        @BindView(R.id.offer_price_text_view) TextView priceTextView;
        @BindView(R.id.status_text_view) TextView statusTextView;

        final Resources resources;

        private Advert mAdvert;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(ViewHolder.this, itemView);
            Context context = itemView.getContext();
            this.resources = context.getResources();
            setUpMenu();
        }

        void setUpMenu() {
            ImageButton menuButton = findById(itemView, R.id.menu_button);
            final PopupMenu popupMenu = new PopupMenu(itemView.getContext(), menuButton);
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

        void onViewAdvertAction() {
            if (sMenuItemClickListener != null) sMenuItemClickListener.viewAdvert(mAdvert);
        }

        void bindOffer(Offer offer) {
            String date = DateUtil.formatToDefaultDate(offer.getDateUpdated());
            dateUpdatedTextView.setText(date);
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
    }

    public void destroy() {
        sMenuItemClickListener = null;
    }
}
