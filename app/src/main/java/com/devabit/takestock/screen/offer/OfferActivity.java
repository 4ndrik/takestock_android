package com.devabit.takestock.screen.offer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.devabit.takestock.R;
import com.devabit.takestock.data.model.Advert;
import com.devabit.takestock.data.model.Offer;
import com.devabit.takestock.data.model.Photo;
import com.devabit.takestock.utils.DateUtil;
import com.devabit.takestock.widget.ListSpacingItemDecoration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Victor Artemyev on 27/09/2016.
 */

public class OfferActivity extends AppCompatActivity implements OfferContract.View {

    private static final String EXTRA_OFFER = "com.devabit.takestock.screen.offer.EXTRA_OFFER";
    private static final String EXTRA_ADVERT = "com.devabit.takestock.screen.offer.EXTRA_ADVERT";

    public static Intent getStartIntent(Context context, Pair<Offer, Advert> offerAdvertPair) {
        Intent starter = new Intent(context, OfferActivity.class);
        starter.putExtra(EXTRA_OFFER, offerAdvertPair.first);
        starter.putExtra(EXTRA_ADVERT, offerAdvertPair.second);
        return starter;
    }

    @BindView(R.id.content_activity_offer) protected ViewGroup mContent;
    @BindView(R.id.toolbar) protected Toolbar mToolbar;
    @BindView(R.id.advert_image_view) protected ImageView mAdvertImageView;
    @BindView(R.id.advert_name_text_view) protected TextView mAdvertNameTextView;
    @BindView(R.id.advert_location_text_view) protected TextView mAdvertLocationTextView;
    @BindView(R.id.advert_date_text_view) protected TextView mAdvertDateTextView;
    @BindView(R.id.advert_price_text_view) protected TextView mAdvertPriceTextView;

    private OffersAdapter mOffersAdapter;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer);
        ButterKnife.bind(OfferActivity.this);
        setUpToolbar();
        Advert advert = getIntent().getParcelableExtra(EXTRA_ADVERT);
        setUpAdvert(advert);
        setUpRecyclerView(advert);
        Offer offer = getIntent().getParcelableExtra(EXTRA_OFFER);
        List<Offer> offers = fetchOffers(offer);
        mOffersAdapter.addOffers(offers);
    }

    private void setUpToolbar() {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void setUpAdvert(Advert advert) {
        loadAdvertImage(advert);
        mAdvertNameTextView.setText(advert.getName());
        mAdvertLocationTextView.setText(advert.getLocation());
        mAdvertDateTextView.setText(DateUtil.formatToDefaultDate(advert.getUpdatedAt()));
        mAdvertPriceTextView.setText(
                getString(R.string.offer_activity_advert_price, advert.getGuidePrice(), advert.getPackagingName()));
    }

    private void loadAdvertImage(Advert advert) {
        List<Photo> photos = advert.getPhotos();
        if (photos.isEmpty()) {
            mAdvertImageView.setImageResource(R.drawable.ic_image_48dp);
        } else {
            Glide.with(OfferActivity.this)
                    .load(photos.get(0).getImagePath())
                    .placeholder(R.color.grey_400)
                    .error(R.drawable.ic_image_48dp)
                    .centerCrop()
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(mAdvertImageView);
        }
    }

    private void setUpRecyclerView(Advert advert) {
        RecyclerView recyclerView = ButterKnife.findById(OfferActivity.this, R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(OfferActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        ListSpacingItemDecoration itemDecoration = new ListSpacingItemDecoration(
                getResources().getDimensionPixelSize(R.dimen.item_list_space_8dp));
        recyclerView.addItemDecoration(itemDecoration);
        mOffersAdapter = new OffersAdapter(OfferActivity.this, advert.getPackagingName());
        mOffersAdapter.setOnStatusChangedListener(mOnStatusChangedListener);
        recyclerView.setAdapter(mOffersAdapter);
    }

    final OffersAdapter.OnStatusChangedListener mOnStatusChangedListener
            = new OffersAdapter.OnStatusChangedListener() {
        @Override public void onAccepted(Offer offer) {

        }

        @Override public void onCountered(Offer offer) {

        }

        @Override public void onRejected(Offer offer) {

        }
    };

    private List<Offer> fetchOffers(Offer offer) {
        List<Offer> offers = new ArrayList<>();
        offers.add(offer);
        if (offer.getChildOffers().length > 0) {
            Offer childOffer = offer.getChildOffers()[0];
            boolean hasChild = true;
            while (hasChild) {
                offers.add(childOffer);
                if (childOffer.getChildOffers().length > 0) {
                    childOffer = childOffer.getChildOffers()[0];
                } else {
                    hasChild = false;
                }
            }
        }
        Collections.reverse(offers);
        return offers;
    }

    @Override public void setPresenter(@NonNull OfferContract.Presenter presenter) {

    }

    @Override public void showNetworkConnectionError() {

    }

    @Override public void showUnknownError() {

    }
}
