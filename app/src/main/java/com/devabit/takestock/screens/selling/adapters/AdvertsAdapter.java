package com.devabit.takestock.screens.selling.adapters;

import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.*;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.devabit.takestock.R;
import com.devabit.takestock.data.models.Advert;
import com.devabit.takestock.data.models.Photo;
import com.devabit.takestock.util.DateFormats;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static butterknife.ButterKnife.findById;
import static com.devabit.takestock.util.Logger.LOGE;
import static com.devabit.takestock.util.Logger.makeLogTag;

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

    public interface OnItemClickListener {
        void onItemClick(Advert advert);
    }

    private static OnItemClickListener sItemClickListener;

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

    public static void setOnItemClickListener(OnItemClickListener itemClickListener) {
        sItemClickListener = itemClickListener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        final Context context;
        final Picasso picasso;
        final ImageView imageView;
        final TextView titleTextView;
        final TextView guidePriceTextView;
        final TextView qtyAvailableTextView;
        final TextView dateUpdatedTextView;
        final TextView offersMadeTextView;
        final TextView newQuestionsTextView;
        final TextView daysLeftTextView;

        Advert advert;

        public ViewHolder(View itemView) {
            super(itemView);
            this.context = itemView.getContext();
            this.picasso = Picasso.with(this.context);
            this.imageView = findById(itemView, R.id.photo_image_view);
            this.titleTextView = findById(itemView, R.id.title_text_view);
            this.guidePriceTextView = findById(itemView, R.id.guide_price_text_view);
            this.qtyAvailableTextView = findById(itemView, R.id.qty_available_text_view);
            this.dateUpdatedTextView = findById(itemView, R.id.date_updated_text_view);
            this.offersMadeTextView = findById(itemView, R.id.offers_made_text_view);
            this.newQuestionsTextView = findById(itemView, R.id.new_questions_text_view);
            this.daysLeftTextView = findById(itemView, R.id.days_left_text_view);

            ImageButton menuButton = findById(itemView, R.id.menu_more_button);
            final PopupMenu popupMenu = new PopupMenu(this.context, menuButton);
            MenuInflater menuInflater = popupMenu.getMenuInflater();
            menuInflater.inflate(R.menu.advert_selling_menu, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override public boolean onMenuItemClick(MenuItem item) {
                    return false;
                }
            });
            menuButton.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    popupMenu.show();
                }
            });
        }

        void bindAdvert(Advert advert) {
            this.advert = advert;
            List<Photo> photos = advert.getPhotos();
            if (!photos.isEmpty()) {
                Photo photo = advert.getPhotos().get(0);
                bindPhoto(photo);
            }
            titleTextView.setText(advert.getName());
            guidePriceTextView.setText(context.getString(R.string.guide_price_per_kg, advert.getGuidePrice()));
            qtyAvailableTextView.setText(context.getString(R.string.qty_available_kg, advert.getItemsCount()));
            try {
                Date date = DateFormats.API_FORMAT.parse(advert.getDateUpdatedAt());
                String dateAsString = DateFormats.DEFAULT_FORMAT.format(date);
                dateUpdatedTextView.setText(dateAsString);
            } catch (ParseException e) {
                LOGE(TAG, "BOOM:", e);
            }
            offersMadeTextView.setText(advert.getOffersCount());
            newQuestionsTextView.setText(advert.getQuestionsCount());
            daysLeftTextView.setText(advert.getDaysLeft());
        }

        void bindPhoto(Photo photo) {
            picasso.load(photo.getImagePath())
                    .placeholder(R.drawable.ic_image_48dp)
                    .error(R.drawable.ic_image_48dp)
                    .centerCrop()
                    .fit()
                    .into(imageView);
        }
    }
}
