package com.devabit.takestock.screen.selling.fragment.activeAdverts;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.LayoutRes;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.StyleSpan;
import android.util.TypedValue;
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

class ActiveAdvertsAdapter extends BaseAdvertsAdapter {

    private static final @LayoutRes int ITEM_LAYOUT_RESOURCE = R.layout.item_advert_selling_active;

    ActiveAdvertsAdapter(Context context) {
        super(context, ITEM_LAYOUT_RESOURCE);
    }

    @Override public BaseAdvertsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, @LayoutRes int viewType) {
        View itemView = inflateViewType(viewType, parent);
        switch (viewType) {
            case ITEM_LAYOUT_RESOURCE:
                return new AdvertActiveViewHolder(itemView);
            default:
                return new ProgressViewHolder(itemView);
        }
    }

    class AdvertActiveViewHolder extends ItemViewHolder {

        @BindView(R.id.photo_image_view) ImageView imageView;
        @BindView(R.id.title_text_view) TextView titleTextView;
        @BindView(R.id.guide_price_text_view) TextView guidePriceTextView;
        @BindView(R.id.qty_available_text_view) TextView qtyAvailableTextView;
        @BindView(R.id.days_left_text_view) TextView daysLeftTextView;
        @BindView(R.id.date_updated_text_view) TextView dateUpdatedTextView;
        @BindView(R.id.new_offers_count_text_view) TextView newOffersCountTextView;
        @BindView(R.id.offers_count_text_view) TextView offersCountTextView;
        @BindView(R.id.new_questions_count_text_view) TextView newQuestionsCountTextView;
        @BindView(R.id.questions_count_text_view) TextView questionsCountTextView;
        @BindView(R.id.views_text_view) TextView viewsTextView;

        AdvertActiveViewHolder(View itemView) {
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

            int newOffersCount = advert.getNewOffersCount();
            if (newOffersCount > 0) {
                newOffersCountTextView.setVisibility(View.VISIBLE);
                newOffersCountTextView.setText(resources.getString(R.string.advert_selling_new_count, newOffersCount));
            } else {
                newOffersCountTextView.setVisibility(View.GONE);
            }
            offersCountTextView.setText(buildCountString(advert.getOffersCount(), resources.getString(R.string.advert_selling_offers)));

            int newQuestionsCount = advert.getNewQuestionsCount();
            if (newQuestionsCount > 0) {
                newQuestionsCountTextView.setVisibility(View.VISIBLE);
                newQuestionsCountTextView.setText(resources.getString(R.string.advert_selling_new_count, newQuestionsCount));
            } else {
                newQuestionsCountTextView.setVisibility(View.GONE);
            }
            questionsCountTextView.setText(buildCountString(advert.getQuestionsCount(), resources.getString(R.string.advert_selling_questions)));

            viewsTextView.setText(buildCountString(String.valueOf(advert.getAdvertsViews()), resources.getString(R.string.advert_selling_views)));
        }

        Spannable buildCountString(String count, String counter) {
            SpannableStringBuilder builder = new SpannableStringBuilder();
            builder.append(count);
            int countSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16, resources.getDisplayMetrics());
            builder.setSpan(new AbsoluteSizeSpan(countSize), 0, count.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            builder.setSpan(new StyleSpan(Typeface.BOLD), 0, count.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            builder.append("\n");
            builder.append(counter);
            int counterSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 14, resources.getDisplayMetrics());
            builder.setSpan(new AbsoluteSizeSpan(counterSize), builder.length() - counter.length(), builder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            return builder;
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
