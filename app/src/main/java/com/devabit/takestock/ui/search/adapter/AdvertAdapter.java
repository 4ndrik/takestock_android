package com.devabit.takestock.ui.search.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.devabit.takestock.R;
import com.devabit.takestock.data.model.Advert;
import com.devabit.takestock.data.model.Photo;
import com.devabit.takestock.util.Logger;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static butterknife.ButterKnife.findById;

/**
 * Created by Victor Artemyev on 10/05/2016.
 */
public class AdvertAdapter extends RecyclerView.Adapter<AdvertAdapter.ViewHolder> {

    private static final String TAG = "AdvertAdapter";

    private static final int TYPE_HORIZONTAL = 0;
    private static final int TYPE_VERTICAL = 1;

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

    public AdvertAdapter(Context context) {
        mLayoutInflater = LayoutInflater.from(context);
        mAdverts = new ArrayList<>();
    }

    @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        switch (viewType) {
            case TYPE_VERTICAL:
                itemView = mLayoutInflater.inflate(R.layout.item_vertical_advert, parent, false);
                break;

            default:
                itemView = mLayoutInflater.inflate(R.layout.item_horizontal_advert, parent, false);
                break;
        }

        return new ViewHolder(itemView);
    }

    @Override public void onBindViewHolder(ViewHolder holder, int position) {
        if (position == getItemCount() - 1 && mEndPositionListener != null) {
            mEndPositionListener.onEndPosition(position);
        }
        holder.bindAdvert(mAdverts.get(position));
    }

    @Override public int getItemCount() {
        return mAdverts.size();
    }

    @Override public int getItemViewType(int position) {
        Advert advert = mAdverts.get(position);
        List<Photo> photos = advert.getPhotos();
        if (photos.isEmpty()) {
            return TYPE_HORIZONTAL;
        } else {
             Photo photo = photos.get(0);
            if(photo.getHeight() > photo.getWidth()) return TYPE_VERTICAL;
            else return TYPE_HORIZONTAL;
        }
    }

    public void addAdverts(List<Advert> adverts) {
        mAdverts.addAll(adverts);
        notifyDataSetChanged();
    }

    public void clearAdverts() {
        mAdverts.clear();
        notifyDataSetChanged();
    }

    public List<Advert> getAdverts() {
        return mAdverts;
    }

    public void setOnEndPositionListener(OnEndPositionListener endPositionListener) {
        mEndPositionListener = endPositionListener;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        sItemClickListener = listener;
    }

    static class DefaultViewHolder extends ViewHolder {
        public DefaultViewHolder(View itemView) {
            super(itemView);
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final static DateFormat DATE_CRATED_FORMATTER = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", Locale.ENGLISH);
        private final static DateFormat DATE_NORMAL_FORMATTER = new SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH);

        private final Context mContext;
        private final Picasso mPicasso;

        private final ImageView mPhotoImageView;
        private final TextView mNameTextView;
        private final TextView mLocationTextView;
        private final TextView mDateTextView;
        private final TextView mPriceTextView;


        private Advert mAdvert;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(ViewHolder.this);
            mContext = itemView.getContext();
            mPicasso = Picasso.with(mContext);
            mPhotoImageView = findById(itemView, R.id.photo_image_view);
            mNameTextView = findById(itemView, R.id.name_text_view);
            mLocationTextView = findById(itemView, R.id.location_text_view);
            mDateTextView = findById(itemView, R.id.date_text_view);
            mPriceTextView = findById(itemView, R.id.price_text_view);
        }

        void bindAdvert(Advert advert) {
            mAdvert = advert;
            mNameTextView.setText(mAdvert.getName());
            mLocationTextView.setText(mAdvert.getLocation());
            mPriceTextView.setText(mContext.getString(R.string.guide_price_per_kg, mAdvert.getGuidePrice()));
            bindCreatedDate(mAdvert.getDateCreatedAt());
            bindPhoto(mAdvert.getPhotos());
        }

        void bindCreatedDate(String createdDate) {
            try {
                Date date = DATE_CRATED_FORMATTER.parse(createdDate);
                String dateAsString = DATE_NORMAL_FORMATTER.format(date);
                mDateTextView.setText(dateAsString);
            } catch (ParseException e) {
                Logger.LOGE(TAG, "BOOM:", e);
            }
        }

        void bindPhoto(List<Photo> photos) {
            if (photos.isEmpty()) {
                mPhotoImageView.setImageResource(R.drawable.ic_image_48dp);
            } else {
                Photo photo = photos.get(0);
                loadPhoto(photo);
            }
        }

        void loadPhoto(Photo photo) {
            mPicasso.load(photo.getImagePath())
                    .placeholder(R.drawable.ic_image_48dp)
                    .error(R.drawable.ic_image_48dp)
                    .centerCrop()
                    .fit()
                    .into(mPhotoImageView);
        }

        @Override public void onClick(View v) {
            if (sItemClickListener != null) sItemClickListener.onItemClick(mAdvert);
        }
    }
}
