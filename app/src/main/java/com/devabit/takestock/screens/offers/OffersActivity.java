package com.devabit.takestock.screens.offers;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
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
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.devabit.takestock.Injection;
import com.devabit.takestock.R;
import com.devabit.takestock.data.models.Advert;
import com.devabit.takestock.data.models.Offer;
import com.devabit.takestock.data.models.OfferStatus;
import com.devabit.takestock.data.models.Photo;
import com.devabit.takestock.screens.offers.adapters.OffersAdapter;
import com.devabit.takestock.screens.offers.dialogs.counterOffer.CounterOfferDialog;
import com.devabit.takestock.screens.offers.dialogs.rejectOffer.RejectOfferDialog;
import com.devabit.takestock.utils.DateUtil;
import com.devabit.takestock.utils.FontCache;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import static com.devabit.takestock.utils.Logger.LOGE;
import static com.devabit.takestock.utils.Logger.makeLogTag;

/**
 * Created by Victor Artemyev on 02/06/2016.
 */
public class OffersActivity extends AppCompatActivity implements OffersContract.View {

    private static final String TAG = makeLogTag(OffersActivity.class);

    public static Intent getStartIntent(Context context, Advert advert) {
        Intent starter = new Intent(context, OffersActivity.class);
        starter.putExtra(Advert.class.getSimpleName(), advert);
        return starter;
    }

    @BindView(R.id.content_activity_offers) protected View mContent;
    @BindView(R.id.advert_photo_image_view) protected ImageView mAdvertPhotoImageView;
    @BindView(R.id.advert_name_text_view) protected TextView mAdvertNameTextView;
    @BindView(R.id.date_updated_text_view) protected TextView mAdvertDateUpdatedTextView;
    @BindView(R.id.guide_price_text_view) protected TextView mAdvertPriceTextView;
    @BindView(R.id.qty_available_text_view) protected TextView mAdvertQtyTextView;
    @BindView(R.id.swipe_refresh_layout) protected SwipeRefreshLayout mRefreshLayout;

    private Advert mAdvert;
    private OffersAdapter mOffersAdapter;

    private OffersContract.Presenter mPresenter;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offers);
        ButterKnife.bind(OffersActivity.this);
        mAdvert = getIntent().getParcelableExtra(Advert.class.getSimpleName());
        setUpAdvert(mAdvert);
        initPresenter();
        setUpToolbar();
        setUpRefreshLayout();
        setUpOffersRecyclerView();
    }

    private void setUpAdvert(Advert advert) {
        if (!advert.getPhotos().isEmpty()) {
            loadAdvertPhoto(advert.getPhotos().get(0));
        }
        mAdvertNameTextView.setText(advert.getName());

        try {
            Date date = DateUtil.API_FORMAT.parse(advert.getDateUpdatedAt());
            String dateAsString = DateUtil.DEFAULT_FORMAT.format(date);
            mAdvertDateUpdatedTextView.setText(dateAsString);
        } catch (ParseException e) {
            LOGE(TAG, "BOOM:", e);
        }
        mAdvertPriceTextView.setText(getString(R.string.guide_price_per_kg, advert.getGuidePrice()));
        mAdvertQtyTextView.setText(getString(R.string.available_kg, advert.getItemsCount()));
    }

    private void loadAdvertPhoto(Photo photo) {
        Picasso.with(OffersActivity.this)
                .load(photo.getImagePath())
                .placeholder(R.drawable.ic_image_48dp)
                .error(R.drawable.ic_image_48dp)
                .centerCrop()
                .fit()
                .into(mAdvertPhotoImageView);
    }

    private void initPresenter() {
        new OffersPresenter(
                Injection.provideDataRepository(OffersActivity.this), OffersActivity.this);
    }

    private void setUpToolbar() {
        final Typeface boldTypeface = FontCache.getTypeface(this, R.string.font_brandon_bold);
        Toolbar toolbar = ButterKnife.findById(OffersActivity.this, R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                onBackPressed();
            }
        });
        TextView titleToolbar = ButterKnife.findById(toolbar, R.id.toolbar_title);
        titleToolbar.setTypeface(boldTypeface);
        titleToolbar.setText(R.string.offers);
    }

    private void setUpRefreshLayout() {
        mRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override public void onRefresh() {
                refreshOffers();
            }
        });
    }

    private void setUpOffersRecyclerView() {
        RecyclerView recyclerView = ButterKnife.findById(OffersActivity.this, R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(
                OffersActivity.this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        mOffersAdapter = new OffersAdapter(OffersActivity.this);
        mOffersAdapter.setStatusButtonClickListener(mStatusButtonsClickListener);
        recyclerView.setAdapter(mOffersAdapter);
    }

    private final OffersAdapter.OnStatusButtonClickListener mStatusButtonsClickListener
            = new OffersAdapter.OnStatusButtonClickListener() {
        @Override public void onAccepted(Offer offer) {
            offer.setOfferStatusId(OfferStatus.ACCEPTED);
            mPresenter.updateOffer(offer);
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
                mPresenter.saveCounterOffer(offer);
            }
        });
    }

    private void displayRejectOfferMakerDialog(Offer offer) {
        RejectOfferDialog dialog = RejectOfferDialog.newInstance(offer);
        dialog.show(getFragmentManager(), dialog.getClass().getSimpleName());
        dialog.setOnRejectOfferListener(new RejectOfferDialog.OnRejectOfferListener() {
            @Override public void onOfferRejected(RejectOfferDialog dialog, Offer offer) {
                dialog.dismiss();
                mPresenter.rejectOffer(offer);
            }
        });
    }

    private int getUserId() {
        AccountManager accountManager = AccountManager.get(OffersActivity.this);
        Account account = accountManager.getAccountsByType(getString(R.string.authenticator_account_type))[0];
        String userId = accountManager.getUserData(account, getString(R.string.authenticator_user_id));
        return Integer.valueOf(userId);
    }

    private void refreshOffers() {
        mOffersAdapter.clear();
        fetchOffers();
    }

    @Override protected void onStart() {
        super.onStart();
        fetchOffers();
    }

    @Override public void showOffersCounterOfferPairsInView(List<Pair<Offer, Offer>> pairs) {
        mOffersAdapter.addOfferCounterOfferPairList(pairs);
    }

    @Override public void showNetworkConnectionError() {
        showSnack(R.string.error_no_network_connection);
    }

    @Override public void showUnknownError() {
        showSnack(R.string.error_unknown);
    }

    private void showSnack(@StringRes int resId) {
        Snackbar.make(mContent, resId, Snackbar.LENGTH_SHORT).show();
    }

    @Override public void setProgressIndicator(boolean isActive) {
        mRefreshLayout.setRefreshing(isActive);
    }

    @Override public void showUpdatedOfferInView(Offer offer) {
        refreshOffers();
    }

    @Override public void showSavedCounterOfferInView(Offer offer) {
        refreshOffers();
    }

    private void fetchOffers() {
        mPresenter.fetchOffersByAdvertId(mAdvert.getId());
    }

    @Override public void setPresenter(@NonNull OffersContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        mOffersAdapter.destroy();
    }
}
