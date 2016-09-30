package com.devabit.takestock.screen.selling.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.*;
import android.widget.ImageButton;
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

import static butterknife.ButterKnife.findById;

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

    public interface OnMenuItemClickListener {
        void manageOffers(Advert advert);

        void viewQuestions(Advert advert);

        void editAdvert(Advert advert);
    }

    private OnMenuItemClickListener mMenuItemClickListener;

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

    public void setOnMenuItemClickListener(OnMenuItemClickListener itemClickListener) {
        mMenuItemClickListener = itemClickListener;
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
        PopupMenu popupMenu;

        Advert advert;

        ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(ItemViewHolder.this, itemView);
            Context context = itemView.getContext();
            resources = context.getResources();
//            setUpMenu();
        }

        private void setUpMenu() {
            ImageButton menuButton = findById(itemView, R.id.menu_button);
            popupMenu = new PopupMenu(itemView.getContext(), menuButton);
            MenuInflater menuInflater = popupMenu.getMenuInflater();
            menuInflater.inflate(R.menu.advert_item_menu, popupMenu.getMenu());
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

                    case R.id.action_edit_advert:
                        onEditAdvertAction();
                        return true;

                    default:
                        return false;
                }
            }
        };

        private void onManageOffersAction() {
            if (SellingAdvertsAdapter.this.mMenuItemClickListener != null)
                SellingAdvertsAdapter.this.mMenuItemClickListener.manageOffers(advert);
        }

        private void onViewMassageAction() {
            if (SellingAdvertsAdapter.this.mMenuItemClickListener != null)
                SellingAdvertsAdapter.this.mMenuItemClickListener.viewQuestions(advert);
        }

        private void onEditAdvertAction() {
            if (SellingAdvertsAdapter.this.mMenuItemClickListener != null)
                SellingAdvertsAdapter.this.mMenuItemClickListener.editAdvert(advert);
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

            String offersCount = advert.getOffersCount();
//            setMenuItemVisibility(R.id.action_manage_offers, !offersCount.equals("0"));
            offersCountTextView.setText(offersCount);

            String questionsCount = advert.getQuestionsCount();
//            setMenuItemVisibility(R.id.action_view_messages, !questionsCount.equals("0"));
            questionsCountTextView.setText(questionsCount);

            viewsTextView.setText(String.valueOf(advert.getAdvertsViews()));
        }

//        String buildCountString(String title, int count) {
//
//        }

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

//        void setMenuItemVisibility(@IdRes int itemId, boolean visible) {
//            MenuItem item = popupMenu.getMenu().findItem(itemId);
//            item.setVisible(visible);
//        }

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
