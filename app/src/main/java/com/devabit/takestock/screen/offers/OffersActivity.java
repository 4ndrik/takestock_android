package com.devabit.takestock.screen.offers;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.devabit.takestock.Injection;
import com.devabit.takestock.R;
import com.devabit.takestock.data.model.Advert;
import com.devabit.takestock.data.model.Offer;
import com.devabit.takestock.data.model.Photo;
import com.devabit.takestock.screen.offers.dialogs.counterOffer.CounterOfferDialog;
import com.devabit.takestock.screen.offers.dialogs.rejectOffer.RejectOfferDialog;
import com.devabit.takestock.utils.DateUtil;
import com.devabit.takestock.widget.ListSpacingItemDecoration;

import java.util.List;

/**
 * Created by Victor Artemyev on 02/06/2016.
 */
public class OffersActivity extends AppCompatActivity implements OffersContract.View {

    public static Intent getStartIntent(Context context, Advert advert) {
        Intent starter = new Intent(context, OffersActivity.class);
        starter.putExtra(Advert.class.getSimpleName(), advert);
        return starter;
    }

    @BindView(R.id.content) protected ViewGroup mContent;
    @BindView(R.id.advert_photo_image_view) protected ImageView mAdvertPhotoImageView;
    @BindView(R.id.advert_name_text_view) protected TextView mAdvertNameTextView;
    @BindView(R.id.date_updated_text_view) protected TextView mAdvertDateUpdatedTextView;
    @BindView(R.id.guide_price_text_view) protected TextView mAdvertPriceTextView;
    @BindView(R.id.qty_available_text_view) protected TextView mAdvertQtyTextView;
    @BindView(R.id.swipe_refresh_layout) protected SwipeRefreshLayout mRefreshLayout;

    private Advert mAdvert;
    private OffersContract.Presenter mPresenter;
    private OffersAdapter mOffersAdapter;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offers);
        ButterKnife.bind(OffersActivity.this);
        mAdvert = getIntent().getParcelableExtra(Advert.class.getSimpleName());
        setUpAdvert(mAdvert);
        setUpToolbar();
        setUpRefreshLayout();
        setUpRecyclerView();
        createPresenter(mAdvert.getId());
    }

    private void setUpAdvert(Advert advert) {
        if (!advert.getPhotos().isEmpty()) {
            loadAdvertPhoto(advert.getPhotos().get(0));
        }
        mAdvertNameTextView.setText(advert.getName());
        mAdvertDateUpdatedTextView.setText(DateUtil.formatToDefaultDate(advert.getUpdatedAt()));
        mAdvertPriceTextView.setText(getString(R.string.guide_price_per_kg, advert.getGuidePrice()));
        mAdvertQtyTextView.setText(getString(R.string.available_kg, advert.getItemsCount()));
    }

    private void loadAdvertPhoto(Photo photo) {
        Glide.with(OffersActivity.this)
                .load(photo.getImagePath())
                .placeholder(R.color.grey_400)
                .error(R.drawable.ic_image_48dp)
                .centerCrop()
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(mAdvertPhotoImageView);
    }

    private void setUpToolbar() {
        Toolbar toolbar = ButterKnife.findById(OffersActivity.this, R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void setUpRefreshLayout() {
        mRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override public void onRefresh() {
                mPresenter.refreshOffers();
            }
        });
    }

    private void setUpRecyclerView() {
        RecyclerView recyclerView = ButterKnife.findById(OffersActivity.this, R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(
                OffersActivity.this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        ListSpacingItemDecoration itemDecoration = new ListSpacingItemDecoration(getResources().getDimensionPixelSize(R.dimen.item_list_space_8dp));
        recyclerView.addItemDecoration(itemDecoration);
        mOffersAdapter = new OffersAdapter(OffersActivity.this, mAdvert.getPackagingName());
        mOffersAdapter.setOnStatusChangedListener(mStatusChangedListener);
        recyclerView.setAdapter(mOffersAdapter);
    }

    private final OffersAdapter.OnStatusChangedListener mStatusChangedListener
            = new OffersAdapter.OnStatusChangedListener() {
        @Override public void onAccepted(Offer offer) {
            Offer.Accept accept = new Offer.Accept.Builder()
                    .setOfferId(offer.getId())
                    .setStatus(Offer.Status.ACCEPTED)
                    .setFromSeller(true)
                    .create();
            mPresenter.acceptOffer(offer, accept);
        }

        @Override public void onCountered(Offer offer) {
            displayCounterOfferMakerDialog(offer);
        }

        @Override public void onRejected(Offer offer) {
            displayRejectOfferMakerDialog(offer);
        }
    };

    private void displayCounterOfferMakerDialog(Offer offer) {
        CounterOfferDialog dialog = CounterOfferDialog.newInstance(mAdvert, offer);
        dialog.show(getFragmentManager(), dialog.getClass().getSimpleName());
        dialog.setOnCounterOfferListener(new CounterOfferDialog.OnCounterOfferListener() {
            @Override public void onOfferCountered(CounterOfferDialog dialog, Offer offer) {
                dialog.dismiss();
            }
        });
    }

    private void displayRejectOfferMakerDialog(final Offer offer) {
        RejectOfferDialog dialog = RejectOfferDialog.newInstance(offer);
        dialog.show(getFragmentManager(), dialog.getClass().getSimpleName());
        dialog.setOnRejectOfferListener(new RejectOfferDialog.OnRejectOfferListener() {
            @Override public void onOfferRejected(RejectOfferDialog dialog, Offer.Accept accept) {
                dialog.dismiss();
                mPresenter.acceptOffer(offer, accept);
            }
        });
    }

    private void createPresenter(int advertId) {
        new OffersPresenter(advertId,
                Injection.provideDataRepository(OffersActivity.this), OffersActivity.this);
    }

    @Override public void setPresenter(@NonNull OffersContract.Presenter presenter) {
        mPresenter = presenter;
        mPresenter.refreshOffers();
    }

    @Override public void showRefreshedOffersInView(List<Offer> offers) {
        mOffersAdapter.refreshOffers(offers);
    }

    @Override public void showLoadedOffersInView(List<Offer> offers) {

    }

    @Override public void showOfferAcceptedInView(Offer offer) {
        mOffersAdapter.refreshOffer(offer);
    }

    @Override public void showNetworkConnectionError() {
        showSnack(R.string.error_no_network_connection);
    }

    @Override public void showUnknownError() {
        showSnack(R.string.error_unknown);
    }

    @Override public void setRefreshingProgressIndicator(boolean isActive) {
        mRefreshLayout.setRefreshing(isActive);
    }

    private void showSnack(@StringRes int resId) {
        Snackbar.make(mContent, resId, Snackbar.LENGTH_SHORT).show();
    }

    @Override public void setLoadingProgressIndicator(boolean isActive) {
        mRefreshLayout.setRefreshing(isActive);
    }

    @Override public void showUpdatedOfferInView(Offer offer) {

    }

    @Override protected void onPause() {
        mPresenter.pause();
        super.onPause();
    }
}
