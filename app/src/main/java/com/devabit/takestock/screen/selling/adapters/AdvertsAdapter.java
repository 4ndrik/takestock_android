package com.devabit.takestock.screen.selling.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.IdRes;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.*;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.devabit.takestock.R;
import com.devabit.takestock.data.models.Advert;
import com.devabit.takestock.data.models.Photo;
import com.devabit.takestock.utils.DateUtil;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static butterknife.ButterKnife.findById;
import static com.devabit.takestock.utils.Logger.LOGE;
import static com.devabit.takestock.utils.Logger.makeLogTag;

/**
 * Created by Victor Artemyev on 24/05/2016.
 */
public class AdvertsAdapter extends RecyclerView.Adapter<AdvertsAdapter.ViewHolder> {

    private static final String TAG = makeLogTag(AdvertsAdapter.class);

    private final LayoutInflater mLayoutInflater;
    private final List<Advert> mAdverts;

    public interface OnEndPositionListener {
        void onEndPosition(int position);
    }

    private OnEndPositionListener mEndPositionListener;

    public interface OnMenuItemClickListener {
        void manageOffers(Advert advert);

        void viewMessages(Advert advert);

        void viewAdvert(Advert advert);

        void editAdvert(Advert advert);
    }

    private static OnMenuItemClickListener sMenuItemClickListener;

    public AdvertsAdapter(Context context) {
        mLayoutInflater = LayoutInflater.from(context);
        mAdverts = new ArrayList<>();
    }

    @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.item_advert_selling, parent, false);
        return new ViewHolder(view);
    }

    @Override public void onBindViewHolder(ViewHolder holder, int position) {
        checkEndPosition(position);
        holder.bindAdvert(mAdverts.get(position));
    }

    private void checkEndPosition(int position) {
        if (isEndPosition(position)) {
            if (mEndPositionListener != null) mEndPositionListener.onEndPosition(position);
        }
    }

    private boolean isEndPosition(int position) {
        return position == getItemCount() - 1;
    }

    @Override public int getItemCount() {
        return mAdverts.size();
    }

    public void addAdverts(List<Advert> adverts) {
        mAdverts.addAll(adverts);
        notifyDataSetChanged();
    }

    public void clearAdverts() {
        mAdverts.clear();
        notifyDataSetChanged();
    }

    public void setOnEndPositionListener(OnEndPositionListener endPositionListener) {
        mEndPositionListener = endPositionListener;
    }

    public void setOnItemClickListener(OnMenuItemClickListener itemClickListener) {
        sMenuItemClickListener = itemClickListener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        final Resources resources;
        final Picasso picasso;
        final ImageView imageView;
        final TextView titleTextView;
        final TextView guidePriceTextView;
        final TextView qtyAvailableTextView;
        final TextView dateUpdatedTextView;
        final TextView offersCountTextView;
        final TextView questionsCountTextView;
        final TextView daysLeftCountTextView;

        final Menu menu;

        Advert advert;

        public ViewHolder(View itemView) {
            super(itemView);
            Context context = itemView.getContext();
            resources = context.getResources();
            picasso = Picasso.with(context);
            imageView = findById(itemView, R.id.photo_image_view);
            titleTextView = findById(itemView, R.id.title_text_view);
            guidePriceTextView = findById(itemView, R.id.guide_price_text_view);
            qtyAvailableTextView = findById(itemView, R.id.qty_available_text_view);
            dateUpdatedTextView = findById(itemView, R.id.date_updated_text_view);
            offersCountTextView = findById(itemView, R.id.offers_count_text_view);
            questionsCountTextView = findById(itemView, R.id.questions_count_text_view);
            daysLeftCountTextView = findById(itemView, R.id.days_left_count_text_view);

            ImageButton menuButton = findById(itemView, R.id.menu_button);
            final PopupMenu popupMenu = new PopupMenu(context, menuButton);
            MenuInflater menuInflater = popupMenu.getMenuInflater();
            menu = popupMenu.getMenu();
            menuInflater.inflate(R.menu.advert_item_menu, menu);
            popupMenu.setOnMenuItemClickListener(mMenuItemClickListener);
            menuButton.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    popupMenu.show();
                }
            });
        }

        private final PopupMenu.OnMenuItemClickListener mMenuItemClickListener
                = new PopupMenu.OnMenuItemClickListener() {
            @Override public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.action_manage_offers:
                        onManageOffersAction();
                        return true;

                    case R.id.action_view_messages:
                        onViewMassageAction();
                        return true;

                    case R.id.action_view_advert:
                        onViewAdvertAction();
                        return true;

                    case R.id.action_edit_advert:
                        onEditAdvertAction();
                        return true;

                    default:
                        return false;
                }
            }
        };

        private void onManageOffersAction() {
            if (sMenuItemClickListener != null) sMenuItemClickListener.manageOffers(advert);
        }

        private void onViewMassageAction() {
            if (sMenuItemClickListener != null) sMenuItemClickListener.viewMessages(advert);
        }

        private void onViewAdvertAction() {
            if (sMenuItemClickListener != null) sMenuItemClickListener.viewAdvert(advert);
        }

        private void onEditAdvertAction() {
            if (sMenuItemClickListener != null) sMenuItemClickListener.editAdvert(advert);
        }

        void bindAdvert(Advert advert) {
            this.advert = advert;
            List<Photo> photos = advert.getPhotos();
            if (!photos.isEmpty()) {
                Photo photo = advert.getPhotos().get(0);
                bindPhoto(photo);
            }
            titleTextView.setText(advert.getName());
            guidePriceTextView.setText(resources.getString(R.string.guide_price_per_kg, advert.getGuidePrice()));
            qtyAvailableTextView.setText(resources.getString(R.string.available_kg, advert.getItemsCount()));
            try {
                Date date = DateUtil.API_FORMAT.parse(advert.getDateUpdatedAt());
                String dateAsString = DateUtil.DEFAULT_FORMAT.format(date);
                dateUpdatedTextView.setText(dateAsString);
            } catch (ParseException e) {
                LOGE(TAG, "BOOM:", e);
            }

            String offersCount = advert.getOffersCount();
            setMenuItemVisibility(R.id.action_manage_offers, !offersCount.equals("0"));
            offersCountTextView.setText(offersCount);

            String questionsCount = advert.getQuestionsCount();
            setMenuItemVisibility(R.id.action_view_messages, !questionsCount.equals("0"));
            questionsCountTextView.setText(questionsCount);

            daysLeftCountTextView.setText(advert.getDaysLeft());
        }

        void bindPhoto(Photo photo) {
            picasso.load(photo.getImagePath())
                    .placeholder(R.drawable.ic_image_48dp)
                    .error(R.drawable.ic_image_48dp)
                    .centerCrop()
                    .fit()
                    .into(imageView);
        }

        void setMenuItemVisibility(@IdRes int itemId, boolean visible) {
            MenuItem item = menu.findItem(itemId);
            item.setVisible(visible);
        }
    }

    public void destroy() {
        sMenuItemClickListener = null;
    }
}