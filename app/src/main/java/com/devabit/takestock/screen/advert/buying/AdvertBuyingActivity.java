package com.devabit.takestock.screen.advert.buying;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
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
import android.text.Html;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.devabit.takestock.BuildConfig;
import com.devabit.takestock.Injection;
import com.devabit.takestock.R;
import com.devabit.takestock.data.model.Advert;
import com.devabit.takestock.data.model.Notification;
import com.devabit.takestock.data.model.Offer;
import com.devabit.takestock.data.model.Photo;
import com.devabit.takestock.screen.advert.detail.AdvertDetailActivity;
import com.devabit.takestock.screen.dialog.counterOffer.CounterOfferDialog;
import com.devabit.takestock.screen.dialog.rejectOffer.RejectOfferDialog;
import com.devabit.takestock.screen.payment.PayByCardActivity;
import com.devabit.takestock.screen.payment.byBACS.PayByBACSActivity;
import com.devabit.takestock.screen.shipping.ShippingActivity;
import com.devabit.takestock.ui.decoration.ListVerticalSpacingItemDecoration;
import com.devabit.takestock.ui.widget.ControllableAppBarLayout;
import com.devabit.takestock.utils.DateUtil;
import com.devabit.takestock.utils.NotificationFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Victor Artemyev on 27/09/2016.
 */

public class AdvertBuyingActivity extends AppCompatActivity implements AdvertBuyingContract.View {

    private static final String EXTRA_OFFER = "com.devabit.takestock.screen.advert.offer.EXTRA_OFFER";
    private static final String EXTRA_ADVERT = "com.devabit.takestock.screen.advert.offer.EXTRA_ADVERT";
    private static final String EXTRA_NOTIFICATION = "com.devabit.takestock.screen.advert.offer.EXTRA_NOTIFICATION";

    public static Intent getStartIntent(Context context, Pair<Offer, Advert> offerAdvertPair) {
        Intent starter = new Intent(context, AdvertBuyingActivity.class);
        starter.putExtra(EXTRA_OFFER, offerAdvertPair.first);
        starter.putExtra(EXTRA_ADVERT, offerAdvertPair.second);
        return starter;
    }

    public static Intent getStartIntent(Context context, Notification notification) {
        Intent starter = new Intent(context, AdvertBuyingActivity.class);
        starter.putExtra(EXTRA_NOTIFICATION, notification);
        return starter;
    }

    public static final int RC_PAYMENT_BY_CARD = 101;
    public static final int RC_PAYMENT_BY_BACS = 102;
    public static final int RC_SHIPPING = 103;

    @BindView(R.id.content_activity_offer) ViewGroup mContent;
    @BindView(R.id.appbar_layout) ControllableAppBarLayout mAppBarLayout;
    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.advert_image_view) ImageView mAdvertImageView;
    @BindView(R.id.advert_name_text_view) TextView mAdvertNameTextView;
    @BindView(R.id.advert_location_text_view) TextView mAdvertLocationTextView;
    @BindView(R.id.advert_date_text_view) TextView mAdvertDateTextView;
    @BindView(R.id.advert_price_text_view) TextView mAdvertPriceTextView;
    @BindView(R.id.recycler_view) RecyclerView mRecyclerView;

    Advert mAdvert;
    AdvertBuyingContract.Presenter mPresenter;
    OffersBuyingAdapter mOffersBuyingAdapter;
    @Nullable Notification mNotification;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advert_buying);
        ButterKnife.bind(AdvertBuyingActivity.this);
        setUpToolbar();
        setUpAppBarLayout();
        RecyclerView recyclerView = setUpRecyclerView();
        setUpOfferAdapter(recyclerView);
        Intent intent = getIntent();
        setUpData(intent);
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

    private void setUpData(Intent intent) {
        if (intent.hasExtra(EXTRA_NOTIFICATION)) {
            mNotification = intent.getParcelableExtra(EXTRA_NOTIFICATION);
        } else if (intent.hasExtra(Notification.EXTRA_ACTION)) {
            mNotification = NotificationFactory.build(AdvertBuyingActivity.this, intent);
        } else {
            Offer offer = intent.getParcelableExtra(EXTRA_OFFER);
            mAdvert = intent.getParcelableExtra(EXTRA_ADVERT);
            setUpOfferAdvertPair(offer, mAdvert);
        }
    }

    private void createPresenter() {
        new AdvertBuyingPresenter(Injection.provideDataRepository(AdvertBuyingActivity.this), AdvertBuyingActivity.this);
    }

    @Override public void setPresenter(@NonNull AdvertBuyingContract.Presenter presenter) {
        mPresenter = presenter;
        if (mNotification != null) {
            mPresenter.loadOfferAdvertPair(mNotification.getOfferId());
            if (mNotification.isSaved()) return;
            mPresenter.saveNotification(mNotification);
        }
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;
        if (requestCode == RC_PAYMENT_BY_CARD
                || requestCode == RC_PAYMENT_BY_BACS
                || requestCode == RC_SHIPPING) {
            Offer offer = data.getParcelableExtra(getString(R.string.extra_offer));
            mOffersBuyingAdapter.refreshOffer(offer);
        }
    }

    @Override public void showOfferAdvertPairInView(Pair<Offer, Advert> offerAdvertPair) {
        setUpOfferAdvertPair(offerAdvertPair.first, offerAdvertPair.second);
    }

    private void setUpOfferAdvertPair(Offer offer, Advert advert) {
        setUpAdvert(advert);
        List<Offer> offers = fetchOffers(offer);
        mOffersBuyingAdapter.addOffers(offers);
    }

    private void setUpAdvert(Advert advert) {
        mAdvert = advert;
        loadAdvertImage(advert);
        mAdvertNameTextView.setText(advert.getName());
        mAdvertLocationTextView.setText(advert.getLocation());
        mAdvertDateTextView.setText(DateUtil.formatToDefaultDate(advert.getUpdatedAt()));
        mAdvertPriceTextView.setText(getString(R.string.offer_activity_advert_price, advert.getGuidePrice(), advert.getPackagingName()));
        mOffersBuyingAdapter.setPackaging(advert.getPackagingName());
    }

    private void loadAdvertImage(Advert advert) {
        List<Photo> photos = advert.getPhotos();
        if (photos.isEmpty()) {
            mAdvertImageView.setImageResource(R.drawable.ic_image_48dp);
        } else {
            Glide.with(AdvertBuyingActivity.this)
                    .load(photos.get(0).getImage())
                    .placeholder(R.color.grey_400)
                    .error(R.drawable.ic_image_48dp)
                    .centerCrop()
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(mAdvertImageView);
        }
    }

    private RecyclerView setUpRecyclerView() {
        mRecyclerView = ButterKnife.findById(AdvertBuyingActivity.this, R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(AdvertBuyingActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        ListVerticalSpacingItemDecoration itemDecoration = new ListVerticalSpacingItemDecoration(getResources().getDimensionPixelSize(R.dimen.item_list_space_8dp));
        mRecyclerView.addItemDecoration(itemDecoration);
        return mRecyclerView;

    }

    private void setUpOfferAdapter(RecyclerView recyclerView) {
        mOffersBuyingAdapter = new OffersBuyingAdapter(AdvertBuyingActivity.this);
        setUpListenersOnAdapter(mOffersBuyingAdapter);
        recyclerView.setAdapter(mOffersBuyingAdapter);
    }

    private void setUpListenersOnAdapter(OffersBuyingAdapter adapter) {
        adapter.setOnStatusChangedListener(mOnStatusChangedListener);
        adapter.setOnMakePaymentClickListener(new OffersBuyingAdapter.OnPaymentClickListener() {
            @Override public void onPayByCard(Offer offer) {
                startActivityForResult(PayByCardActivity.getStartIntent(AdvertBuyingActivity.this, offer, false), RC_PAYMENT_BY_CARD);
            }

            @Override public void onPayByBACS(Offer offer) {
                startActivityForResult(PayByBACSActivity.getStartIntent(AdvertBuyingActivity.this, offer, mAdvert.getName()), RC_PAYMENT_BY_BACS);
            }
        });

        adapter.setOnShippingAddressClickListener(new OffersBuyingAdapter.OnShippingAddressClickListener() {
            @Override public void onShippingAddressSet(Offer offer) {
                startActivityForResult(ShippingActivity.getStartIntent(AdvertBuyingActivity.this, offer), RC_SHIPPING);
            }
        });

        adapter.setOnConfirmGoodsListener(new OffersBuyingAdapter.OnConfirmGoodsListener() {
            @Override public void onConfirm(Offer offer) {
                acceptOffer(offer, buildOfferAccept(offer, Offer.Status.GOODS_RECEIVED));
            }
        });

        adapter.setOnRaiseDisputeListener(new OffersBuyingAdapter.OnRaiseDisputeListener() {
            @Override public void onDispute(Offer offer) {
                acceptOffer(offer, buildOfferAccept(offer, Offer.Status.IN_DISPUTE));
            }

            @Override public void onContactSupport(Offer offer) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", "admin@wetakestock.com", null));
                startActivity(Intent.createChooser(emailIntent, getString(R.string.buying_activity_contact_email_send)));
            }
        });

        adapter.setOnContactListener(new OffersBuyingAdapter.OnContactListener() {
            @Override public void onContactSeller(Offer offer) {
                Intent contactIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", mAdvert.getUser().getEmail(), null));
                contactIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.buying_activity_contact_email_subject));
                contactIntent.putExtra(Intent.EXTRA_TEXT,
                        Html.fromHtml(getString(R.string.buying_activity_contact_email_text, mAdvert.getName(), BuildConfig.BASE_URL, mAdvert.getId())));
                contactIntent.putExtra(Intent.EXTRA_CC, new String[]{getString(R.string.admin_email)});
                startActivity(Intent.createChooser(contactIntent, getString(R.string.buying_activity_contact_email_send)));
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
        new AlertDialog.Builder(AdvertBuyingActivity.this)
                .setTitle(R.string.accept_offer_dialog_title)
                .setMessage(getString(R.string.accept_offer_dialog_message,
                        offer.getQuantity(), mAdvert.getPackagingName(),
                        offer.getPrice(), mAdvert.getPackagingName()))
                .setPositiveButton(R.string.accept_offer_dialog_accept,
                        new DialogInterface.OnClickListener() {
                            @Override public void onClick(DialogInterface dialog, int which) {
                                acceptOffer(offer, buildOfferAccept(offer, Offer.Status.ACCEPTED));
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
                acceptOffer(offer, accept);
            }
        });
    }

    private void displayRejectOfferDialog(final Offer offer) {
        RejectOfferDialog dialog = RejectOfferDialog.newInstance(offer, false);
        dialog.show(getSupportFragmentManager(), dialog.getClass().getSimpleName());
        dialog.setOnRejectOfferListener(new RejectOfferDialog.OnRejectOfferListener() {
            @Override public void onOfferRejected(RejectOfferDialog dialog, Offer.Accept accept) {
                dialog.dismiss();
                acceptOffer(offer, accept);
            }
        });
    }

    private Offer.Accept buildOfferAccept(Offer offer, int offerStatus) {
        return new Offer.Accept.Builder()
                .setOfferId(offer.getId())
                .setStatus(offerStatus)
                .setFromSeller(false)
                .create();
    }

    private void acceptOffer(Offer offer, Offer.Accept accept) {
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

    @Override public void showOfferAcceptedInView(Offer offer) {
        mOffersBuyingAdapter.addOffer(offer);
        mRecyclerView.scrollToPosition(0);
    }

    @Override public void showNotificationSavedInView(Notification notification) {
        mNotification = notification;
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
        setTouchDisabled(isActive);
        mRecyclerView.setAlpha(isActive ? 0.5f : 1f);
    }

    private void setTouchDisabled(boolean isActive) {
        Window window = getWindow();
        if (isActive) {
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        } else {
            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    }

    @OnClick(R.id.content_advert)
    void onContentAdvertClick() {
        startActivity(AdvertDetailActivity.getStartIntent(AdvertBuyingActivity.this, mAdvert.getId()));
    }

    @Override protected void onPause() {
        mPresenter.pause();
        super.onPause();
    }
}
