package com.devabit.takestock.screen.advert.detail;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.*;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.devabit.takestock.Injection;
import com.devabit.takestock.R;
import com.devabit.takestock.TakeStockAccount;
import com.devabit.takestock.data.model.Advert;
import com.devabit.takestock.data.model.Certification;
import com.devabit.takestock.data.model.Offer;
import com.devabit.takestock.data.model.User;
import com.devabit.takestock.screen.advert.adapter.AdvertPhotosAdapter;
import com.devabit.takestock.screen.advert.detail.dialogs.OfferDialog;
import com.devabit.takestock.screen.askQuestion.AskQuestionActivity;
import com.devabit.takestock.screen.entry.EntryActivity;
import com.devabit.takestock.utils.DateUtil;
import com.devabit.takestock.widget.GravitySnapHelper;
import com.devabit.takestock.widget.GridSpacingItemDecoration;
import timber.log.Timber;

import java.util.List;

public class AdvertDetailActivity extends AppCompatActivity implements AdvertDetailContract.View {

    public static final String EXTRA_ADVERT_ID = "EXTRA_ADVERT_ID";

    public static Intent getStartIntent(Context context, int advertId) {
        Intent starter = new Intent(context, AdvertDetailActivity.class);
        starter.putExtra(EXTRA_ADVERT_ID, advertId);
        return starter;
    }

    private static final int RC_ENTRY = 100;

    @BindView(R.id.content) View mContent;
    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.progress_bar) ProgressBar mProgressBar;
    @BindView(R.id.photo_recycler_view) RecyclerView mPhotosRecyclerView;
    @BindView(R.id.photo_count_text_view) TextView mPhotoCountTextView;
    @BindView(R.id.scroll_view) NestedScrollView mScrollView;
    @BindView(R.id.title_text_view) TextView mNameTextView;
    @BindView(R.id.guide_price_text_view) TextView mGuidePriceTextView;
    @BindView(R.id.minimum_order_text_view) TextView mMinimumOrderTextView;
    @BindView(R.id.qty_available_text_view) TextView mQtyAvailableTextView;
    @BindView(R.id.description_text_view) TextView mDescriptionTextView;
    @BindView(R.id.location_text_view) TextView mLocationTextView;
    @BindView(R.id.shipping_text_view) TextView mShippingTextView;
    @BindView(R.id.expiry_date_text_view) TextView mExpiryTextView;
    @BindView(R.id.certification_text_view) TextView mCertificationTextView;
    @BindView(R.id.condition_text_view) TextView mConditionTextView;
    @BindView(R.id.watching_fab) FloatingActionButton mWatchingFAB;
    @BindView(R.id.user_name_text_view) TextView mUserNameTextView;
    @BindView(R.id.user_image_view) ImageView mUserImageView;
    @BindView(R.id.user_rating_bar) RatingBar mUserRatingBar;
    @BindView(R.id.similar_listing_text_view) TextView mSimilarListingTextView;

    TakeStockAccount mAccount;
    AdvertPhotosAdapter mPhotosAdapter;
    AdvertListingAdapter mListingAdapter;
    Advert mAdvert;
    Offer mOffer;
    AdvertDetailContract.Presenter mPresenter;
    boolean mIsWatching;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advert_detail);
        ButterKnife.bind(AdvertDetailActivity.this);
        mAccount = TakeStockAccount.get(AdvertDetailActivity.this);
        setUpToolbar();
        setUpPhotosRecyclerView();
        setUpSimilarAdvertsRecyclerView();
//        setUpScrollView();
        int advertId = getIntent().getIntExtra(EXTRA_ADVERT_ID, 0);
        createPresenter(advertId);
    }

    private void setUpToolbar() {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void setUpPhotosRecyclerView() {
        final LinearLayoutManager layoutManager = new LinearLayoutManager(AdvertDetailActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mPhotosRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int firstVisibleItems = layoutManager.findLastCompletelyVisibleItemPosition();
                setPhotoPosition(firstVisibleItems);
            }
        });
        mPhotosRecyclerView.setLayoutManager(layoutManager);
        SnapHelper snapHelper = new GravitySnapHelper(Gravity.START);
        snapHelper.attachToRecyclerView(mPhotosRecyclerView);
        setUpPhotosAdapter(mPhotosRecyclerView);
    }

    private void setUpPhotosAdapter(RecyclerView recyclerView) {
        mPhotosAdapter = new AdvertPhotosAdapter(AdvertDetailActivity.this);
        recyclerView.setAdapter(mPhotosAdapter);
    }

    private void setUpSimilarAdvertsRecyclerView() {
        RecyclerView recyclerView = ButterKnife.findById(AdvertDetailActivity.this, R.id.similar_advert_recycler_view);
        LinearLayoutManager layoutManager = new GridLayoutManager(AdvertDetailActivity.this, 2);
        recyclerView.setLayoutManager(layoutManager);
        GridSpacingItemDecoration itemDecoration = new GridSpacingItemDecoration(getResources().getDimensionPixelSize(R.dimen.item_grid_space_4dp), 2);
        recyclerView.addItemDecoration(itemDecoration);
        setUpListingAdapter(recyclerView);
    }

    private void setUpListingAdapter(RecyclerView recyclerView) {
        mListingAdapter = new AdvertListingAdapter(recyclerView.getContext());
        mListingAdapter.setOnItemClickListener(new AdvertListingAdapter.OnItemClickListener() {
            @Override public void onItemClick(Advert advert) {
                startActivity(AdvertDetailActivity.getStartIntent(AdvertDetailActivity.this, advert.getId()));
            }
        });
        recyclerView.setAdapter(mListingAdapter);
    }

    private void setUpScrollView() {
        mScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            final Rect scrollBounds = new Rect();
            boolean isToolbarTitle;

            @Override public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                mScrollView.getHitRect(scrollBounds);
                if (mNameTextView.getLocalVisibleRect(scrollBounds) && isToolbarTitle) {
                    mToolbar.setTitle("");
                    isToolbarTitle = false;
                } else if (!isToolbarTitle) {
                    mToolbar.setTitle(mAdvert.getName());
                    isToolbarTitle = true;
                }
            }
        });
    }

    private void createPresenter(int advertId) {
        new AdvertDetailPresenter(advertId,
                Injection.provideDataRepository(AdvertDetailActivity.this), AdvertDetailActivity.this);
    }

    @Override public void setPresenter(@NonNull AdvertDetailContract.Presenter presenter) {
        mPresenter = presenter;
        mPresenter.loadAdvert();
    }

    @OnClick(R.id.watching_fab)
    protected void onWatchingFabClick() {
        mPresenter.addOrRemoveWatchingAdvert(mAdvert, mAccount.getUserId());
    }

    @OnClick(R.id.make_button)
    protected void onMakeOfferButtonClick() {
        displayOfferMakerDialog();
    }

    private void displayOfferMakerDialog() {
        if (mAccount.lacksAccount()) {
            startEntryActivity();
        } else {
            OfferDialog dialog = OfferDialog.newInstance(mAdvert);
            dialog.show(getFragmentManager(), dialog.getClass().getSimpleName());
            dialog.setOnOfferListener(new OfferDialog.OnOfferListener() {
                @Override public void onOfferMade(OfferDialog dialog, Offer offer) {
                    dialog.dismiss();
                    mPresenter.makeOffer(offer);
                }
            });
        }
    }

    private void startEntryActivity() {
        startActivityForResult(EntryActivity.getStartIntent(AdvertDetailActivity.this), RC_ENTRY);
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_ENTRY && resultCode == RESULT_OK) {
            Timber.d("Entry is successful.");
        }
    }

    @Override public void showAdvertInView(Advert advert) {
        mAdvert = advert;
        bindAdvert(mAdvert);
        setUpWatchingFAB(mAdvert);
    }

    private void bindAdvert(Advert advert) {
        mPhotosAdapter.addPhotos(advert.getPhotos());
        setPhotoPosition(0);
        mNameTextView.setText(advert.getName());
        String packaging = advert.getPackagingName();
        mGuidePriceTextView.setText(getString(R.string.advert_detail_guide_price_per_unit, advert.getGuidePrice(), packaging));
        mMinimumOrderTextView.setText(getString(R.string.advert_detail_minimum_order_unit, advert.getMinOrderQuantity(), packaging));
        mQtyAvailableTextView.setText(getString(R.string.advert_detail_qty_available_unit, advert.getItemsCount(), packaging));
        mDescriptionTextView.setText(advert.getDescription());
        mExpiryTextView.setText(DateUtil.formatToExpiryDate(advert.getExpiresAt()));
        Certification certification = advert.getCertification();
        mCertificationTextView.setText(certification == null ? "" : certification.getName());
        mShippingTextView.setText(advert.getShippingDisplay());
        mConditionTextView.setText(advert.getConditionDisplay());
        mLocationTextView.setText(advert.getLocation());
        setUpWatchingFAB(advert);
        bindUser(advert.getUser());
    }

    private void setPhotoPosition(int position) {
        if (position < 0) return;
        mPhotoCountTextView.setText(getString(R.string.advert_detail_photo_count, position + 1, mPhotosAdapter.getItemCount()));
    }

    private void bindUser(User user) {
        mUserNameTextView.setText(user.getUserName());
        mUserRatingBar.setRating((float) user.getAvgRating());
        Glide.with(mUserNameTextView.getContext())
                .load(user.getPhoto())
                .error(R.drawable.ic_placeholder_user_96dp)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(mUserImageView);
    }

    @Override public void showSimilarAdvertsInView(List<Advert> adverts) {
        if (adverts.isEmpty()) return;
        mSimilarListingTextView.setVisibility(View.VISIBLE);
        mListingAdapter.addAdverts(adverts);
    }

    @Override public void showOfferMadeInView(Offer offer) {
        mOffer = offer;
        showSnack(R.string.advert_detail_offer_made);
        mAdvert.setCanOffer(false);
    }

    @Override public void showAdvertAddedToWatching(Advert advert) {
        mIsWatching = true;
        setUpWatchingFAB(advert);
    }

    @Override public void showAdvertRemovedFromWatching(Advert advert) {
        mIsWatching = true;
        setUpWatchingFAB(advert);
    }

    private void setUpWatchingFAB(Advert advert) {
        int color;
        if (advert.hasSubscriber(mAccount.getUserId())) {
            mWatchingFAB.setImageResource(R.drawable.ic_eye_white_24dp);
            color = ContextCompat.getColor(AdvertDetailActivity.this, R.color.jam);
        } else {
            mWatchingFAB.setImageResource(R.drawable.ic_eye_grey_24dp);
            color = ContextCompat.getColor(AdvertDetailActivity.this, R.color.white);
        }
        mWatchingFAB.setBackgroundTintList(ColorStateList.valueOf(color));
    }

    @Override public void showAdvertWatchingError(Advert advert) {

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
        mProgressBar.setVisibility(isActive ? View.VISIBLE : View.GONE);
        mPhotosRecyclerView.setAlpha(isActive ? 0.5f : 1f);
        mScrollView.setAlpha(isActive ? 0.5f : 1f);
        setTouchDisabled(isActive);
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

    private static final ButterKnife.Setter<View, Boolean> TRANSPARENCY
            = new ButterKnife.Setter<View, Boolean>() {
        @Override public void set(@NonNull View view, Boolean isActive, int index) {
            view.setAlpha(isActive ? 0.5f : 1.0f);
        }
    };

    @OnClick(R.id.ask_button)
    protected void onAskButtonClick() {
        if (mAccount.lacksAccount()) startEntryActivity();
        else startQuestionActivity();
    }

    private void startQuestionActivity() {
        startActivity(AskQuestionActivity.getStartIntent(AdvertDetailActivity.this, mAdvert.getId()));
    }

    @Override public void onBackPressed() {
        if (mOffer != null || mIsWatching) {
            Intent intent = new Intent();
            intent.putExtra(getString(R.string.extra_advert), mAdvert);
            setResult(RESULT_OK, intent);
            finish();
        } else {
            super.onBackPressed();
        }
    }

    @Override protected void onPause() {
        mPresenter.pause();
        super.onPause();
    }
}
