package com.devabit.takestock.screen.selling.adapters;

import android.content.Context;
import android.content.res.Resources;
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
import com.devabit.takestock.data.model.Advert;
import com.devabit.takestock.data.model.Photo;
import com.devabit.takestock.utils.DateUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Victor Artemyev on 24/05/2016.
 */
public class SellingAdvertsAdapter extends RecyclerView.Adapter<SellingAdvertsAdapter.ViewHolder> {

    private static final int TYPE_PROGRESS = 0;
    private static final int TYPE_ITEM= 1;

    private final LayoutInflater mLayoutInflater;
    private final List<Advert> mAdverts;

    public interface OnItemClickListener {
        void onItemClicked(Advert advert);
    }

    private OnItemClickListener mItemClickListener;

    public SellingAdvertsAdapter(Context context) {
        mLayoutInflater = LayoutInflater.from(context);
        mAdverts = new ArrayList<>();
    }

    @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_PROGRESS:
                return new ProgressViewHolder(inflateViewType(R.layout.item_progress, parent));

            default:
                return new ItemViewHolder(inflateViewType(R.layout.item_advert_selling, parent));
        }
    }

    private View inflateViewType(@LayoutRes int resId, ViewGroup parent) {
        return mLayoutInflater.inflate(resId, parent, false);
    }

    @Override public void onBindViewHolder(ViewHolder holder, int position) {
        if (holder.getItemViewType() == TYPE_ITEM) {
            ((ItemViewHolder)holder).bindAdvert(mAdverts.get(position));
        }
    }

    @Override public int getItemCount() {
        return mAdverts.size();
    }

    @Override public int getItemViewType(int position) {
        Advert advert = mAdverts.get(position);
        if (advert == null) return TYPE_PROGRESS;
        else return TYPE_ITEM;
    }

    public void refreshAdverts(List<Advert> adverts) {
        mAdverts.clear();
        mAdverts.addAll(adverts);
        notifyDataSetChanged();
    }

    public void addAdverts(List<Advert> adverts) {
        mAdverts.addAll(adverts);
        notifyDataSetChanged();
    }

    public void setLoadingProgress(boolean active) {
        if (active) {
            mAdverts.add(null);
            notifyItemInserted(mAdverts.size());
        } else {
            mAdverts.remove(mAdverts.size() - 1);
            notifyItemRemoved(mAdverts.size());
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mItemClickListener = onItemClickListener;
    }

    class ProgressViewHolder extends ViewHolder {
        ProgressViewHolder(View itemView) {
            super(itemView);
        }
    }

    class ItemViewHolder extends ViewHolder {

        @BindView(R.id.photo_image_view) ImageView imageView;
        @BindView(R.id.title_text_view) TextView titleTextView;
        @BindView(R.id.guide_price_text_view) TextView guidePriceTextView;
        @BindView(R.id.qty_available_text_view) TextView qtyAvailableTextView;
        @BindView(R.id.days_left_text_view) TextView daysLeftTextView;
        @BindView(R.id.date_updated_text_view) TextView dateUpdatedTextView;
        @BindView(R.id.offers_count_text_view) TextView offersCountTextView;
        @BindView(R.id.questions_count_text_view) TextView questionsCountTextView;
        @BindView(R.id.views_text_view) TextView viewsTextView;

        Resources resources;
        Advert advert;

        ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(ItemViewHolder.this, itemView);
            Context context = itemView.getContext();
            resources = context.getResources();
        }

        void bindAdvert(Advert advert) {
            this.advert = advert;
            List<Photo> photos = advert.getPhotos();
            if (!photos.isEmpty()) {
                Photo photo = advert.getPhotos().get(0);
                bindPhoto(photo);
            }
            String date = DateUtil.formatToDefaultDate(advert.getUpdatedAt());
            dateUpdatedTextView.setText(date);
            titleTextView.setText(advert.getName());
            guidePriceTextView.setText(
                    resources.getString(R.string.advert_selling_guide_price, advert.getGuidePrice(), advert.getPackagingName()));
            qtyAvailableTextView.setText(
                    resources.getString(R.string.advert_selling_available, advert.getItemsCountNow(), advert.getPackagingName()));
            daysLeftTextView.setText(
                    resources.getString(R.string.advert_selling_days_left, advert.getDaysLeft()));

            offersCountTextView.setText(advert.getOffersCount());
            questionsCountTextView.setText(advert.getQuestionsCount());
            viewsTextView.setText(String.valueOf(advert.getAdvertsViews()));
        }

        void bindPhoto(Photo photo) {
            Glide.with(imageView.getContext())
                    .load(photo.getImagePath())
                    .placeholder(R.color.grey_400)
                    .error(R.drawable.ic_image_48dp)
                    .centerCrop()
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(imageView);
        }

        @OnClick(R.id.content_item_advert_selling)
        void onContentClick() {
            if(mItemClickListener != null) mItemClickListener.onItemClicked(this.advert);
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
