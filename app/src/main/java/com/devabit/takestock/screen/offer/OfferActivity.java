package com.devabit.takestock.screen.offer;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
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
import com.devabit.takestock.Injection;
import com.devabit.takestock.R;
import com.devabit.takestock.data.model.Advert;
import com.devabit.takestock.data.model.Offer;
import com.devabit.takestock.data.model.Photo;
import com.devabit.takestock.screen.dialog.counterOffer.CounterOfferDialog;
import com.devabit.takestock.screen.dialog.rejectOffer.RejectOfferDialog;
import com.devabit.takestock.screen.payment.PaymentActivity;
import com.devabit.takestock.screen.shipping.ShippingActivity;
import com.devabit.takestock.utils.DateUtil;
import com.devabit.takestock.widget.ControllableAppBarLayout;
import com.devabit.takestock.widget.ListVerticalSpacingItemDecoration;

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

    public static final int RC_PAYMENT_BY_CARD = 101;
    public static final int RC_SHIPPING = 102;

    @BindView(R.id.content_activity_offer) protected ViewGroup mContent;
    @BindView(R.id.appbar_layout) protected ControllableAppBarLayout mAppBarLayout;
    @BindView(R.id.toolbar) protected Toolbar mToolbar;
    @BindView(R.id.advert_image_view) protected ImageView mAdvertImageView;
    @BindView(R.id.advert_name_text_view) protected TextView mAdvertNameTextView;
    @BindView(R.id.advert_location_text_view) protected TextView mAdvertLocationTextView;
    @BindView(R.id.advert_date_text_view) protected TextView mAdvertDateTextView;
    @BindView(R.id.advert_price_text_view) protected TextView mAdvertPriceTextView;
    @BindView(R.id.recycler_view) protected RecyclerView mRecyclerView;

    private Advert mAdvert;
    private OfferContract.Presenter mPresenter;
    private OffersBuyingAdapter mOffersBuyingAdapter;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer);
        ButterKnife.bind(OfferActivity.this);
        setUpToolbar();
        setUpAppBarLayout();
        mAdvert = getIntent().getParcelableExtra(EXTRA_ADVERT);
        setUpAdvert(mAdvert);
        setUpRecyclerView(mAdvert);
        Offer offer = getIntent().getParcelableExtra(EXTRA_OFFER);
        List<Offer> offers = fetchOffers(offer);
        mOffersBuyingAdapter.addOffers(offers);
        createPresenter();
    }

    private void setUpToolbar() {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void setUpAppBarLayout() {
        mAppBarLayout.setOnStateChangeListener(new ControllableAppBarLayout.OnStateChangeListener() {
            @Override public void onStateChange(int toolbarChange) {
                switch (toolbarChange) {
                    case ControllableAppBarLayout.State.COLLAPSED:
                        mToolbar.setTitle(mAdvert.getName());
                        mToolbar.setSubtitle(getString(R.string.offer_activity_advert_price, mAdvert.getGuidePrice(), mAdvert.getPackagingName()));
                        break;
                    case ControllableAppBarLayout.State.EXPANDED:
                    case ControllableAppBarLayout.State.IDLE:
                        mToolbar.setTitle("");
                        mToolbar.setSubtitle("");
                        break;
                }
            }
        });
    }

    private void setUpAdvert(Advert advert) {
        loadAdvertImage(advert);
        mAdvertNameTextView.setText(advert.getName());
        mAdvertLocationTextView.setText(advert.getLocation());
        mAdvertDateTextView.setText(DateUtil.formatToDefaultDate(advert.getUpdatedAt()));
        mAdvertPriceTextView.setText(getString(R.string.offer_activity_advert_price, advert.getGuidePrice(), advert.getPackagingName()));
    }

    private void loadAdvertImage(Advert advert) {
        List<Photo> photos = advert.getPhotos();
        if (photos.isEmpty()) {
            mAdvertImageView.setImageResource(R.drawable.ic_image_48dp);
        } else {
            Glide.with(OfferActivity.this)
                    .load(photos.get(0).getImage())
                    .placeholder(R.color.grey_400)
                    .error(R.drawable.ic_image_48dp)
                    .centerCrop()
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(mAdvertImageView);
        }
    }

    private void setUpRecyclerView(Advert advert) {
        mRecyclerView = ButterKnife.findById(OfferActivity.this, R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(OfferActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        ListVerticalSpacingItemDecoration itemDecoration = new ListVerticalSpacingItemDecoration(getResources().getDimensionPixelSize(R.dimen.item_list_space_8dp));
        mRecyclerView.addItemDecoration(itemDecoration);
        mOffersBuyingAdapter = new OffersBuyingAdapter(OfferActivity.this, advert.getPackagingName());
        setUpListenersOnAdapter(mOffersBuyingAdapter);
        mRecyclerView.setAdapter(mOffersBuyingAdapter);
    }

    private void setUpListenersOnAdapter(OffersBuyingAdapter adapter) {
        adapter.setOnStatusChangedListener(mOnStatusChangedListener);
        adapter.setOnMakePaymentClickListener(new OffersBuyingAdapter.OnPaymentClickListener() {
            @Override public void onPayByCard(Offer offer) {
                startActivityForResult(PaymentActivity.getStartIntent(OfferActivity.this, offer), RC_PAYMENT_BY_CARD);
            }

            @Override public void onPayByBACS(Offer offer) {

            }
        });
        adapter.setOnShippingAddressClickListener(new OffersBuyingAdapter.OnShippingAddressClickListener() {
            @Override public void onShippingAddressSet(Offer offer) {
                startActivityForResult(ShippingActivity.getStartIntent(OfferActivity.this, offer), RC_SHIPPING);
            }
        });
        adapter.setOnConfirmGoodsListener(new OffersBuyingAdapter.OnConfirmGoodsListener() {
            @Override public void onConfirm(Offer offer) {
                confirmGoodsReceived(offer);
            }
        });
    }

    final OffersBuyingAdapter.OnStatusChangedListener mOnStatusChangedListener
            = new OffersBuyingAdapter.OnStatusChangedListener() {
        @Override public void onAccepted(Offer offer) {
            displayAcceptOfferDialog(offer);
        }

        @Override public void onCountered(Offer offer) {
            displayCounterOfferDialog(offer);
        }

        @Override public void onRejected(Offer offer) {
            displayRejectOfferDialog(offer);
        }
    };

    private void displayAcceptOfferDialog(final Offer offer) {
        new AlertDialog.Builder(OfferActivity.this)
                .setTitle(R.string.accept_offer_dialog_title)
                .setMessage(getString(R.string.accept_offer_dialog_message,
                        offer.getQuantity(), mAdvert.getPackagingName(),
                        offer.getPrice(), mAdvert.getPackagingName()))
                .setPositiveButton(R.string.accept_offer_dialog_accept,
                        new DialogInterface.OnClickListener() {
                            @Override public void onClick(DialogInterface dialog, int which) {
                                Offer.Accept accept = new Offer.Accept.Builder()
                                        .setOfferId(offer.getId())
                                        .setStatus(Offer.Status.ACCEPTED)
                                        .setFromSeller(false)
                                        .create();
                                mPresenter.acceptOffer(offer, accept);
                            }
                        })
                .setNegativeButton(R.string.accept_offer_dialog_cancel, null)
                .show();
    }

    private void displayCounterOfferDialog(final Offer offer) {
        CounterOfferDialog dialog = CounterOfferDialog.newInstance(mAdvert, offer, false);
        dialog.show(getSupportFragmentManager(), dialog.getClass().getSimpleName());
        dialog.setOnOfferCounteredListener(new CounterOfferDialog.OnOfferCounteredListener() {
            @Override public void onCountered(CounterOfferDialog dialog, Offer.Accept accept) {
                dialog.dismiss();
                mPresenter.acceptOffer(offer, accept);
            }
        });
    }

    private void displayRejectOfferDialog(final Offer offer) {
        RejectOfferDialog dialog = RejectOfferDialog.newInstance(offer, false);
        dialog.show(getSupportFragmentManager(), dialog.getClass().getSimpleName());
        dialog.setOnRejectOfferListener(new RejectOfferDialog.OnRejectOfferListener() {
            @Override public void onOfferRejected(RejectOfferDialog dialog, Offer.Accept accept) {
                dialog.dismiss();
                mPresenter.acceptOffer(offer, accept);
            }
        });
    }

    private void confirmGoodsReceived(Offer offer) {
        Offer.Accept accept = new Offer.Accept.Builder()
                .setOfferId(offer.getId())
                .setStatus(Offer.Status.GOODS_RECEIVED)
                .setFromSeller(false)
                .create();
        mPresenter.acceptOffer(offer, accept);
    }

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

    private void createPresenter() {
        new OfferPresenter(Injection.provideDataRepository(OfferActivity.this), OfferActivity.this);
    }

    @Override public void setPresenter(@NonNull OfferContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_PAYMENT_BY_CARD || requestCode == RC_SHIPPING && resultCode == RESULT_OK) {
            Offer offer = data.getParcelableExtra(getString(R.string.extra_offer));
            mOffersBuyingAdapter.refreshOffer(offer);
        }
    }

    @Override public void showOfferAcceptedInView(Offer offer) {
        mOffersBuyingAdapter.addOffer(offer);
        mRecyclerView.scrollToPosition(0);
    }

    @Override public void showNetworkConnectionError() {
        showSnack(R.string.error_no_network_connection);
    }

    @Override public void showUnknownError() {
        showSnack(R.string.error_unknown);
    }

    private void showSnack(@StringRes int resId) {
        Snackbar.make(mContent, resId, Snackbar.LENGTH_LONG).show();
    }

    @Override public void setProgressIndicator(boolean isActive) {
        mRecyclerView.setEnabled(!isActive);
        mRecyclerView.setAlpha(isActive ? 0.5f : 1f);
    }

    @Override protected void onPause() {
        mPresenter.pause();
        super.onPause();
    }
}
