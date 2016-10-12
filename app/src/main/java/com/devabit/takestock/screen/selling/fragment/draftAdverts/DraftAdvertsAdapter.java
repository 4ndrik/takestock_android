package com.devabit.takestock.screen.selling.fragment.draftAdverts;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.devabit.takestock.R;
import com.devabit.takestock.data.model.Advert;
import com.devabit.takestock.data.model.Photo;
import com.devabit.takestock.screen.selling.fragment.BaseAdvertsAdapter;
import com.devabit.takestock.utils.DateUtil;

import java.util.List;

/**
 * Created by Victor Artemyev on 08/10/2016.
 */

class DraftAdvertsAdapter extends BaseAdvertsAdapter {

    private static final @LayoutRes int ITEM_LAYOUT_RESOURCE = R.layout.item_advert_selling_draft;

    DraftAdvertsAdapter(Context context) {
        super(context, ITEM_LAYOUT_RESOURCE);
    }

    @Override public ViewHolder onCreateViewHolder(ViewGroup parent, @LayoutRes int viewType) {
        View itemView = inflateViewType(viewType, parent);
        switch (viewType) {
            case ITEM_LAYOUT_RESOURCE:
                return new DraftActiveViewHolder(itemView);
            default:
                return new ProgressViewHolder(itemView);
        }
    }

    class DraftActiveViewHolder extends ItemViewHolder {

        @BindView(R.id.photo_image_view) ImageView imageView;
        @BindView(R.id.title_text_view) TextView titleTextView;
        @BindView(R.id.guide_price_text_view) TextView guidePriceTextView;
        @BindView(R.id.qty_available_text_view) TextView qtyAvailableTextView;
        @BindView(R.id.days_left_text_view) TextView daysLeftTextView;
        @BindView(R.id.date_updated_text_view) TextView dateUpdatedTextView;

        DraftActiveViewHolder(View itemView) {
            super(itemView);
        }

        @Override protected void bindAdvert(Advert advert) {
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
        }

        @Override public void bindPhoto(Photo photo) {
            Glide.with(imageView.getContext())
                    .load(photo.getThumbnail())
                    .placeholder(R.color.grey_400)
                    .error(R.drawable.ic_image_48dp)
                    .centerCrop()
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(imageView);
        }
    }
}
